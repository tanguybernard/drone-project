from dronekit import connect, VehicleMode,Command
from pymavlink import mavutil
import time

#Set up option parsing to get connection string
import argparse
parser = argparse.ArgumentParser(description='Print out vehicle state information. Connects to SITL on local PC by default.')
parser.add_argument('--connect',default='tcp:127.0.0.1:5760',
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
        print " Altitude: ", vehicle.location.global_relative_frame.alt
        #Break and return from function just below target altitude.
        if vehicle.location.global_relative_frame.alt>=aTargetAltitude*0.95:
            print "Reached target altitude"
            break
        time.sleep(1)

arm_and_takeoff(10)

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
        cmd = Command(0,0,0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,mavutil.mavlink.MAV_CMD_NAV_WAYPOINT, 0, 0, 0, 0, 0, 0,missionitem['lattitude'], missionitem['longitude'], 0)
        cmds.add(cmd)

        # Passage en mode Boucle
    vehicle.commands.next=0

    cmd = Command(0, 0, 0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT, mavutil.mavlink.MAV_CMD_DO_JUMP, 0, 0,
                         1, -1, 0, 0, 0, 0, 0)
    cmds.add(cmd)


        ####
    cmds.upload()  # Send commands


    # Reset mission set to first (0) waypoint
    vehicle.commands.next=0


while not vehicle.mode.name=='AUTO':  #Wait until mode has changed
    print " Waiting for mode change ..."
    # Set the vehicle into auto mode
    vehicle.mode = VehicleMode("AUTO")
    time.sleep(1)

@vehicle.on_attribute('location')
def listener(self, attr_name, value):
    import requests
    #url = 'http://http://m2gla-drone.istic.univ-rennes1.fr:8080/intervention/' + args.idIntervention + '/drone'
    #data = '{"query":{"bool":{"must":[{"text":{"record.document":"SOME_JOURNAL"}},{"text":{"record.articleTitle":"farmers"}}],"must_not":[],"should":[]}},"from":0,"size":50,"sort":[],"facets":{}}'
    #response = requests.get(url, data=data)
    print " GlobalRelative: %s" % value.global_relative_frame

while vehicle.mode.name == 'AUTO':
    time.sleep(0.1)
