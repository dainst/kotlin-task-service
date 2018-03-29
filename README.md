# Getting Started

    gradle assemble
    docker-compose build
    docker-compose up

Then you can use curl to trigger messages:

    curl -XPOST http://localhost:8080/task/hello