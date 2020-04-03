# school-service-elasticsearch
spring boot 2.2.2  
spring cloud config client  
spring cloud eureka client  

## 1 脚本说明
deploy-image-maven.sh : 通过docker命令构建docker镜像
deploy-image-docker.sh : 通过maven插件构建&上传docker镜像
application-start.sh : 启动本服务

## 2 比图来类比传统关系型数据库:
Relational DB -> Databases -> Tables -> Rows -> Columns
Elasticsearch -> Indices   -> Types  -> Documents -> Fields

Elasticsearch提供丰富且灵活的查询语言叫做DSL查询(Query DSL),它允许你构建更加复杂、强大的查询。
DSL(Domain Specific Language特定领域语言)以JSON请求体的形式出现。


对外暴露端口：9200
内部使用端口：9300

>https://docs.spring.io/spring-data/elasticsearch/docs/3.2.3.RELEASE/reference/html/#reference
>https://github.com/spring-projects/spring-data-elasticsearch