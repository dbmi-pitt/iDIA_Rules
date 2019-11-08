#!/bin/bash

/etc/init.d/postgresql start

echo "Arguments: $@"
/bin/sh ./runRules.sh ${@}