#!/bin/bash

service postgresql start
RETRIES=5

until psql -h 127.0.0.1 -U                     # replace with the empty string -d                     # replace with the empty string -c "select 1" > /dev/null 2>&1 || [ $RETRIES -eq 0 ]; do
  echo "Waiting for postgres server, $((RETRIES--)) remaining attempts..."
  sleep 10
done
echo "Arguments: $@"
./runRules.sh $@