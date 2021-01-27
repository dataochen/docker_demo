# docker_demo
集成dubbo,redis,rabbitmq,mysql中间件的分布式架构

用于docker例子中
#自定义网络段
docker network create --subnet=172.18.0.0/16 dmz

#中间件
docker run --name mysql -it -d --network=dmz --ip=172.18.0.7 --restart=always -e MYSQL_ROOT_PASSWORD=root mysql:latest
docker run --name redis -itd --network=dmz --ip=172.18.0.5 --restart=always redis
docker run --name zk -itd --network=dmz --ip=172.18.0.6 --restart=always zookeeper
docker run --name rabbitmq -itd --network=dmz --ip=172.18.0.8 --restart=always rabbitmq
docker run --name dubbo-admin -itd -p 8889:8080 --network=dmz --ip=172.18.0.10 --restart=always  -e dubbo.registry.address=zookeeper://172.18.0.6:2181  chenchuxin/dubbo-admin
docker run --name jenkins -itd -p 8890:8080 --network=dmz --ip=172.18.0.11 --restart=always jenkins
docker run --name nginx -itd --network=dmz --ip=172.18.0.12 --restart=always nginx

#服务
docker run --name docker_demo_provider -itd --network=dmz --ip=172.18.0.2 --restart=always dataochen/tomcat8:latest
docker run --name docker_demo_web -itd --network=dmz --ip=172.18.0.3 --restart=always dataochen/tomcat8:latest
