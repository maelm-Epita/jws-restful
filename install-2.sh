#!/usr/bin/env bash

createuser -s postgres
createdb -U postgres yakamon
mvn clean install
