# docker_demo
集成dubbo,redis,rabbitmq,mysql中间件的分布式架构

用于docker例子中

#概要设计：
1.搭建zk,redis,mysql,rabbitmq,dubbo-admin,jenkins,nginx中间件服务
2.俩台业务服务器A和B,A负责提供dubbo接口包括数据库操作，redis操作，发送mq，B负责调用dubbo接口并保留http接口和消费MQ
3.使用nginx作为网关层，暴露B服务的http接口
4.后台有dubbo后台，jenkins后台，rabbitmq后台；账号密码统一为root

#详细设计：
实验镜像选择如下：
mysql: mysql:latest 数据库 使用最新的
redis: redis:latest 缓存redis 使用最新的
zookeeper: zookeeper:latest 使用最新的
rabbitmq: rabbitmq:3.8.11-management  默认版本是不支持后台的，此版本支持rabbitmq后台
dubbo-admin: chenchuxin/dubbo-admin:latest dubbo服务治理后台 使用最新版本
jenkins: jenkins:latest 使用最新的
nginx: nginx:latest 使用最新的
tomcat: dataochen/tomcat8 自定义的tomcat，用来发布业务服务，支持jdk8,远程部署

网络架构图：
![image](https://github.com/dataochen/docker_demo/doc/分布式网络架构.png)

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
