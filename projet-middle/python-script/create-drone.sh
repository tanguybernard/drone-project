#!/bin/bash

./create-mavproxy.sh $1
python create_drone.py --instance $1 --lattitude $2 --longitude $3
