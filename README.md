# Sandbox
For playing around with new features.

[![Build Status](https://travis-ci.org/mle-enso/sandbox.svg?branch=master)](https://travis-ci.org/mle-enso/sandbox)
[![GPLv3](https://img.shields.io/badge/licence-GPLv3-brightgreen.svg)](http://www.gnu.org/licenses/gpl-3.0.html)

## Preparation

To minimize the manual work to be done before playing around with different frameworks, technologies and remote systems
one has to [install Docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/#install-docker-ce-1).
That only manual step is sufficient to get all further preconditions (e.g. Kafka, MongoDB, …) for integration tests on the "Docker-way".

## Starting

Clone this project and simply execute all included tests via ```mvn clean verify```.

## Try it instantly

* …by inspecting it through Spring Boot's actuators: https://sandbox-pro.herokuapp.com/actuator
* …by Spring Data REST's HAL browser: http://sandbox-pro.herokuapp.com/browser/index.html#/productOpinions
* …by taking a look into the product opinion GUI: http://sandbox-pro.herokuapp.com/productOpinions/gui
