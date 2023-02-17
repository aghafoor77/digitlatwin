#!/bin/bash

echo "Running ./ganache-cli - command"
ganache-cli -e 2000000000000 -h 172.17.0.2 -g 2000 -a 25 | tee -a store.txt
