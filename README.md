# Getting Started

    gradle assemble
    docker-compose build
    docker-compose up

Then you can use curl to trigger messages:

    curl -XPOST http://localhost:8080/task/create/test

# Test Scaling

Run multiple worker instances with

    docker-compose up --scale task-worker=3
    
Create multiple tasks

    curl -XPOST http://localhost:8080/task/create/test1
    curl -XPOST http://localhost:8080/task/create/test2
    curl -XPOST http://localhost:8080/task/create/test3
    curl -XPOST http://localhost:8080/task/create/test4
    curl -XPOST http://localhost:8080/task/create/test5
    
You should now see that different workers pick up the tasks.