#!/bin/bash

/etc/init.d/postgresql start
sleep 10

echo "Arguments: $@"
./runRules.sh $@