FROM maven:3.6.3 AS builder
COPY . /usr/local/src/sort-script
WORKDIR /usr/local/src/sort-script

FROM elasticsearch:7.17.0
RUN sh pp_curl.sh
