#!/bin/bash

python create_drone.py --instance $1 --latitude $2 --longitude $3 &
./create-mavproxy.sh $1 
