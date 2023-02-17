#!/bin/bash
image="iganache"
container="cganache"
echo "========================================================"
echo "Executing commands for Building and Running ganache image and container . . . "
echo "1.  Stopping container"
echo "2.  Removing container"
echo "3.  Removing image"
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
echo "3.  Removing image" 
sudo docker rmi $image
echo "Done"
echo "========================================================"
