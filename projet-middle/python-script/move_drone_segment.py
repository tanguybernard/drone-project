from dronekit import connect, VehicleMode,Command
from pymavlink import mavutil
import time
import ee
#Set up option parsing to get connection string
import argparse
parser = argparse.ArgumentParser(description='Print out vehicle state information. Connects to SITL on local PC by default.')
parser.add_argument('--connect',default='tcp:127.0.0.1:5760',required=True,
                   help="vehicle connection target string. If not specified, SITL automatically started and used.")
parser.add_argument('--mission',help='Mission')
parser.add_argument('--idDrone',help='Identifiant du drone',required=True)
parser.add_argument('--idIntervention',help='Identifiant de l\'intervention',required=True)
parser.add_argument('--imageFolder',help='Dossier des images',required=True)

args = parser.parse_args()

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
arm_and_takeoff(70)

if args.mission != None:
    import json
    dataMission  = json.loads(args.mission)
    # Get commands object from Vehicle.
    cmds = vehicle.commands

    cmds.clear()

    #ajout d'un point qui ne sert a rien pour ne pas sauter la premiere mission
    cmds.add(Command( 0, 0, 0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,mavutil.mavlink.MAV_CMD_NAV_WAYPOINT, 0, 0, 0, 0, 0, 0, 10, 10, 10))
    vehicle.flush() # Send commands
    #aller
    for missionitem in dataMission['mission']:
        cmd = Command(0,0,0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,mavutil.mavlink.MAV_CMD_NAV_WAYPOINT, 0, 0, 0, 0, 0, 0,missionitem['lattitude'], missionitem['longitude'], 70)
        cmds.add(cmd)

    #retour
    if len(dataMission['mission']) > 1 :
        for i in reversed(range(1, len(dataMission['mission']) - 1)):
            cmd = Command(0,0,0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,mavutil.mavlink.MAV_CMD_NAV_WAYPOINT, 0, 0, 0, 0, 0, 0,dataMission['mission'][i]['lattitude'], dataMission['mission'][i]['longitude'], 70)
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
    url = 'http://m2gla-drone.istic.univ-rennes1.fr:8080/intervention/' + args.idIntervention + '/drone'
    data = '{ "battery": ' + str(vehicle.battery.current) +',"id":"'+ str(args.idDrone) +'", "ip": "127.0.0.1","latitude": "' + lat + '","longitude": "' + lon + '","name": "drone1","port": "14551"}'
    headers = {"content-type": "application/json"}
    requests.patch(url, data=data,headers=headers)
    global nextPoint
    import urllib
    if vehicle.commands.next != nextPoint:
        urllib.urlretrieve("https://maps.googleapis.com/maps/api/staticmap?center=" +lat +"," + lon + "&zoom=19&size=640x512&maptype=satellite&key=AIzaSyDMiGs7FfMIZANrYC6tBx6D-CFXMt0eY64&style=feature:road.local&scale=1", args.imageFolder + "/" + str(time.time()) + ".png")
        nextPoint = vehicle.commands.next

while vehicle.mode.name == 'AUTO' and vehicle.commands != None and vehicle.armed:
    time.sleep(0.1)

vehicle.close()