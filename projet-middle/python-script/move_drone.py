from dronekit import connect, VehicleMode,Command
from pymavlink import mavutil
import time

#Set up option parsing to get connection string
import argparse
parser = argparse.ArgumentParser(description='Print out vehicle state information. Connects to SITL on local PC by default.')
parser.add_argument('--connect',default='tcp:127.0.0.1:5760',
                   help="vehicle connection target string. If not specified, SITL automatically started and used.")
parser.add_argument('--mission',help='Mission')
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

    # Call clear() on Vehicle.commands and upload the command to the vehicle.
    cmds.clear()
    for missionitem in dataMission['mission']:
        cmd = Command(0,0,0, mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,mavutil.mavlink.MAV_CMD_NAV_WAYPOINT, 0, 0, 0, 0, 0, 0,missionitem['lattitude'], missionitem['longitude'], 0)
        cmds.add(cmd)
    cmds.upload() # Send commands

    # Reset mission set to first (0) waypoint
    vehicle.commands.next=0


while not vehicle.mode.name=='AUTO':  #Wait until mode has changed
    print " Waiting for mode change ..."
    # Set the vehicle into auto mode
    vehicle.mode = VehicleMode("AUTO")
    time.sleep(1)