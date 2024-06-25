# VNClient
How to run the VNC server in a docker container
> At the root of the project run the following commands:
> ``$ cd ./vnc_server``
> 
> Build the docker image:
> ``$ docker build -t vnc_ubuntu .``
> 
> Run the docker container:
> ``$ docker run -dt --rm --name vnc_ubuntu -p 5901:5901 vnc_ubuntu``
> 
> Access the container:
> ``$ docker exec -ti vnc_ubuntu /bin/bash``
>
> Run this command if you're running this project on a **window machine**:
> ```sed -i -e 's/\r$//' start-vnc.sh```
> 
> In the container run:
> ``./start-vnc.sh``

Now we have the vnc server running in the container with address ```localhost:5901``` and password ```123```.

To connect to the server, go to class ```ClientModuleApplication``` and run the main function in the class.