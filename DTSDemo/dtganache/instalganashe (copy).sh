#!/bin/bash

echo "Running ./ganache-cli - command"
ganache-cli -e 10000000000 -h  -g 2000 -a 25 | tee -a store.txt
