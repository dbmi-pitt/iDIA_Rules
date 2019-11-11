#!/bin/bash

service postgresql start
RETRIES=5

until psql -U idiarules -d idiarules -c "select 1" > /dev/null 2>&1 || [ $RETRIES -eq 0 ]; do
  echo "Waiting for postgres server, $((RETRIES--)) remaining attempts..."
  sleep 5
done
echo "Arguments: $@"
./runRules.sh $@