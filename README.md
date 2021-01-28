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
jenkins: jenkinsci/blueocean:latest 官方推荐使用镜像，使用最新的  
nginx: nginx:latest 使用最新的  
tomcat: dataochen/tomcat8 自定义的tomcat，用来发布业务服务，支持jdk8,远程部署,指定远程操作的用户名和密码


#网络架构图：  
![image](https://github.com/dataochen/docker_demo/blob/main/doc/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E7%BD%91%E7%BB%9C%E6%9E%B6%E6%9E%84.png)  

#实施步骤：
1.自定义网络段  
docker network create --subnet=172.18.0.0/16 dmz  
2.下载docker镜像和运行容器  
 docker pull 镜像：版本  
 docker run xxx  
 
#中间件
参数说明：--network：指定为自定义的网络 --ip:固定ip --restart=always docker容器重启时自动启动  
docker run --name mysql -it -d --network=dmz --ip=172.18.0.7 --restart=always -e MYSQL_ROOT_PASSWORD=root mysql:latest
docker run --name redis -itd --network=dmz --ip=172.18.0.5 --restart=always redis
docker run --name zk -itd --network=dmz --ip=172.18.0.6 --restart=always zookeeper
docker run --name rabbitmq -itd --network=dmz --ip=172.18.0.8  -p 8891:15672 --restart=always rabbitmq
docker run --name dubbo-admin -itd -p 8889:8080 --network=dmz --ip=172.18.0.10 --restart=always -e dubbo.registry.address=zookeeper://172.18.0.6:2181 chenchuxin/dubbo-admin
docker run --name jenkins -itd -p 8890:8080 --network=dmz --ip=172.18.0.11 --restart=always jenkins
docker run --name nginx -itd --network=dmz --ip=172.18.0.12  -p 8888:80 --restart=always nginx

#服务
docker run --name docker_demo_provider -itd --network=dmz --ip=172.18.0.2 --restart=always dataochen/tomcat8:latest  
docker run --name docker_demo_web -itd --network=dmz --ip=172.18.0.3 --restart=always dataochen/tomcat8:latest  

3.其他说明  
如果要宿主机重启的时候 docker也重启 需要执行systemctl enable docker.service,然后当docker重启的时候，由于容器配置了--restart，也会跟着重启。

测试url:192.168.237.130改为自己的宿主机IP  
http://192.168.237.130:8888/docker_demo_web/demo  

后台地址：192.168.237.130改为自己的宿主机IP  
http://192.168.237.130:8889/  dubbo后台  
http://192.168.237.130:8890/  jenkins后台  
http://192.168.237.130:8891/  rabbitmq后台  

4.其他问题的解决方案  
Docker 容器设置固定IP教程 https://jingyan.baidu.com/article/1974b289df52fdb5b1f77493.html  
tomcat配置远程访问部署 https://blog.csdn.net/jasnet_u/article/details/90441091  
VMware虚拟机Linux增加磁盘空间的扩容操作 https://blog.csdn.net/lyd135364/article/details/78623119  
解决 docker 磁盘空间不足问题 https://blog.csdn.net/shm19990131/article/details/106816587/  
docker中的容器时区更改 https://www.jianshu.com/p/6c9aef17968d  


