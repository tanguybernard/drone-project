#!/bin/bash

sleep 5
portSitl=$((5760+$(($1*10))))
portMav=$((14550+$1))
mavproxy.py --master tcp:127.0.0.1:$portSitl --sitl 127.0.0.1:5501 --out 127.0.0.1:$portMav --map --console 
