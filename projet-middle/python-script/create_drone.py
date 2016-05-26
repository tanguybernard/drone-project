print "Start simulator (SITL)"
import dronekit_sitl

import argparse
parser = argparse.ArgumentParser(description='Print out vehicle state information. Connects to SITL on local PC by default.')
parser.add_argument('--instance',default=0,help='Instance number')

args = parser.parse_args()

sitl = dronekit_sitl.SITL()
sitl.download('copter', '3.3', verbose=True)
sitl_args = ['-I0', '--model', 'quad','--instance',args.instance ]
sitl_args.append('--home=1,1,584,353')
sitl.launch(sitl_args, await_ready=True, restart=True)
sitl.block_until_ready(verbose=False) # explicitly wait until receiving commands
code = sitl.complete(verbose=False) # wait until exit
sitl.poll() # returns None or return code
sitl.stop() # terminates SITL