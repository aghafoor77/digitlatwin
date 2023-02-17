#!/bin/bash
container="cganache"
echo "========================================================"
echo "Executing commands for Building and Running ganache image and container . . . "
echo "1.  Stopping container"
echo "2.  Removing container"
echo "========================================================"
echo ""
echo "________________________________________________________"
echo "1.  Stopping container"
sudo docker stop $container .
echo ""
echo "________________________________________________________"
echo "2.  Removing container"
sudo docker rm $container
echo ""
echo "________________________________________________________"
echo "Done"
echo "========================================================"
