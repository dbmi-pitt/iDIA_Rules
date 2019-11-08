#!/bin/bash

/etc/init.d/postgresql start

echo "Arguments: $@"
./runRules.sh ${@}