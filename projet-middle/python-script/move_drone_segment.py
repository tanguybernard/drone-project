from dronekit import connect, VehicleMode,Command
from pymavlink import mavutil
import time
#Set up option parsing to get connection string
import argparse
parser = argparse.ArgumentParser(description='Print out vehicle state information. Connects to SITL on local PC by default.')
parser.add_argument('--connect',default='tcp:127.0.0.1:5760',required=True,
                   help="vehicle connection target string. If not specified, SITL automatically started and used.")
parser.add_argument('--mission',help='Mission')
parser.add_argument('--idDrone',help='Identifiant du drone',required=True)
parser.add_argument('--idIntervention',help='Identifiant de l\'intervention',required=True)

args = parser.parse_args()

import  mimetypes

def get_content_type(filename):
    return mimetypes.guess_type(filename)[0] or 'application/octet-stream'

def encode_multipart_formdata(fields, files):
    """
    fields is a sequence of (name, value) elements for regular form fields.
    files is a sequence of (name, filename, value) elements for data to be uploaded as files
    Return (content_type, body) ready for httplib.HTTP instance
    """
    BOUNDARY = '----------ThIs_Is_tHe_bouNdaRY_$'
    CRLF = '\r\n'
    L = []
    L.append('--' + BOUNDARY)
    L.append('Content-Disposition: form-data; name="%s"' % "idIntervention")
    L.append('')
    L.append(fields["idIntervention"])
    L.append('--' + BOUNDARY)
    L.append('Content-Disposition: form-data; name="%s"' % "latitude")
    L.append('')
    L.append(fields["latitude"])
    L.append('--' + BOUNDARY)
    L.append('Content-Disposition: form-data; name="%s"' % "longitude")
    L.append('')
    L.append(fields["longitude"])
    for (key,filename, value) in files:
        L.append('--' + BOUNDARY)
        L.append('Content-Disposition: form-data; name="%s"; filename="%s"' % (key, filename))
        L.append('Content-Type: %s' % get_content_type(filename))
        L.append('')
        L.append(value)
    L.append('--' + BOUNDARY + '--')
    L.append('')
    body = CRLF.join(L)
    content_type = 'multipart/form-data; boundary=%s' % BOUNDARY
    return content_type, body

def post_multipart(host, fields, files):
    import requests

    content_type, body = encode_multipart_formdata(fields, files)
    headers = {
        'content-type': content_type,
        'content-length': str(len(body))
    }
    r = requests.post(host, data=body, headers=headers)
    return r.text

vehicle = connect(args.connect, wait_ready=True)
def arm_and_takeoff(aTargetAltitude):
    """
    Arms vehicle and fly to aTargetAltitude.
    """

    print "Basic pre-arm checks"
    # Don't try to arm until autopilot is ready
    while not vehicle.is_armable:
        print " Waiting for vehicle to initialise..."
        time.sleep(1)


    print "Arming motors"
    # Copter should arm in GUIDED mode
    vehicle.mode = VehicleMode("GUIDED")
    vehicle.armed = True

    # Confirm vehicle armed before attempting to take off
    while not vehicle.armed:
        print " Waiting for arming..."
        time.sleep(1)

    print "Taking off!"
    vehicle.simple_takeoff(aTargetAltitude) # Take off to target altitude

    # Wait until the vehicle reaches a safe height before processing the goto (otherwise the command
    #  after Vehicle.simple_takeoff will execute immediately).
    while True:

        if not vehicle.armed:
            vehicle.armed = True
            while not vehicle.armed:
                print " Waiting for arming..."
                time.sleep(1)
            print "Taking off!"
            vehicle.simple_takeoff(aTargetAltitude) # Take off to target altitude

        print " Altitude: ", vehicle.location.global_relative_frame.alt
        #Break and return from function just below target altitude.
        if vehicle.location.global_relative_frame.alt>=aTargetAltitude*0.95:
            print "Reached target altitude"
            break
        time.sleep(1)

nextPoint = 0
arm_and_takeoff(15)

if args.mission != None:

    # Get commands object from Vehicle.
    cmds = vehicle.commands

    cmds.clear()

    #ajout d'un point qui ne sert a rien pour ne pas sauter la premiere mission
    cmds.add(Command( 0, 0, 0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,mavutil.mavlink.MAV_CMD_NAV_WAYPOINT, 0, 0, 0, 0, 0, 0, 10, 10, 10))
    vehicle.flush() # Send commands
    #aller
    val = eval(args.mission)
    #boucle
    for lat,lon in val:
        cmd = Command(0,0,0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,mavutil.mavlink.MAV_CMD_NAV_WAYPOINT, 0, 0, 0, 0, 0, 0,lat, lon, 15)
        cmds.add(cmd)

    #retour
    if len(val) > 1 :
        for i in reversed(range(1, len(val) - 1)):
            lat,lon = val[i]
            cmd = Command(0,0,0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,mavutil.mavlink.MAV_CMD_NAV_WAYPOINT, 0, 0, 0, 0, 0, 0,lat, lon, 15)
            cmds.add(cmd)
        cmd = Command(0,0,0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,mavutil.mavlink.MAV_CMD_DO_JUMP, 0, 0, 1, -1, 0, 0, 0, 0, 0)
        cmds.add(cmd)
    cmds.upload() # Send commands


    # Reset mission set to first (0) waypoint
    vehicle.commands.next=0


while not vehicle.mode.name=='AUTO':  #Wait until mode has changed
    print " Waiting for mode change ..."
    # Set the vehicle into auto mode
    vehicle.mode = VehicleMode("AUTO")
    time.sleep(1);

@vehicle.on_attribute('location')
def listener(self, attr_name, value):
    import requests
    lat = str(value.global_relative_frame.lat)
    lon = str(value.global_relative_frame.lon)
    server ='http://m2gla-drone.istic.univ-rennes1.fr:8080'
    url = server +'/intervention/' + args.idIntervention + '/drone'
    data = '{ "battery": ' + str(vehicle.battery.current) +',"id":"'+ str(args.idDrone) +'","latitude": "' + lat + '","longitude": "' + lon + '"}'
    headers = {"content-type": "application/json"}

    #on met a jour la position du drone sur le serveur
    requests.patch(url, data=data,headers=headers)

    global nextPoint
    import urllib,os
    #si on est arrive a un nouveau point de la mission
    if vehicle.commands.next != nextPoint:
        filename = 'tmp/'+str(time.time())+'.png'

        #recuperation de la limage de la carte a la position du drone
        urllib.urlretrieve("https://maps.googleapis.com/maps/api/staticmap?center=" +lat +"," + lon + "&zoom=19&size=640x512&maptype=satellite&key=AIzaSyDMiGs7FfMIZANrYC6tBx6D-CFXMt0eY64&style=feature:road.local&scale=1",filename)

        files = [('file', filename, open(filename, 'rb').read())]
        data ={"idIntervention":str(args.idIntervention),"latitude":lat ,"longitude":lon}
        url = server + '/photo/intervention/' + args.idIntervention

        #on ajoute limage au serveur
        post_multipart(url,data,files)

        #suppression de limage
        os.remove(filename)

        nextPoint = vehicle.commands.next

while vehicle.mode.name == 'AUTO' and vehicle.commands != None and vehicle.armed:
    time.sleep(0.1)

vehicle.close()