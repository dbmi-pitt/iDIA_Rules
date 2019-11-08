#!/bin/bash

/etc/init.d/postgresql start

/bin/sh ./runRules.sh ${@}