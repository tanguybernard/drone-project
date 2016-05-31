from dronekit import connect, VehicleMode
import argparse

parser = argparse.ArgumentParser(description='Print out vehicle state information. Connects to SITL on local PC by default.')
parser.add_argument('--connect',default='tcp:127.0.0.1:5760',required=True,
                   help="vehicle connection target string. If not specified, SITL automatically started and used.")

args = parser.parse_args()
vehicle = connect(args.connect, wait_ready=True)

import time

while not vehicle.mode.name=='GUIDED':  #Wait until mode has changed
    # Set the vehicle into auto mode
    vehicle.mode = VehicleMode("GUIDED")
    time.sleep(1);