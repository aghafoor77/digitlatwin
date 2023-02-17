FROM ubuntu:18.04

LABEL vnf="fgc" description="FORMAS Ganache Controler" type="container"
MAINTAINER Abdul Ghafoor abdul.ghafoor@ri.se

RUN \
    apt-get -y update && \
    apt-get -y install curl man wget telnet net-tools iputils-ping vim

RUN apt-get -y update 
RUN apt-get install openjdk-8-jre -y

RUN curl -fsSL https://deb.nodesource.com/setup_12.x |  bash -
RUN apt-get install -y nodejs
RUN npm install -g ganache-cli

ENV HOME /root
WORKDIR /home


RUN mkdir -p /home/ganashe
COPY instalganashe.sh /home/ganashe/instalganashe.sh

WORKDIR /home/ganashe


EXPOSE 8545
RUN echo "===COMMAND to OPEN SHELL ===>           sudo docker exec -it cganache bash" 
RUN ./instalganashe.sh


