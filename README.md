# school-service-elasticsearch-es
spring boot 2.2.6
spring cloud config client
spring cloud eureka client  

## 1 脚本说明
deploy-image-maven.sh : 通过docker命令构建docker镜像
deploy-image-docker.sh : 通过maven插件构建&上传docker镜像
application-start.sh : 启动本服务

## 2 比图来类比传统关系型数据库:
RelationalDB -> Database -> Table -> Row -> Column -| Schema -| Index -| SQL[CRUD]
ElasticSearch -> Index -> Type -> Document -> Field -| Mapping -| Everything is index -| Query DSL[POST PUT GET DELETE]
MongoDB -> Database -> Collection -> Document -> Field

Elasticsearch提供丰富且灵活的查询语言叫做DSL查询(Query DSL),它允许你构建更加复杂、强大的查询。
DSL(Domain Specific Language特定领域语言)以JSON请求体的形式出现。

## 3 启动运行
### 3.1 启动Elasticsearch
```bash
$ ./bin/elasticsearch
```
对外暴露端口：9200
内部使用端口：9300

请求9200端口，会得到ES说明信息：
> http://localhost:9200/ 说明信息
> http://localhost:9200/_cluster/state 节点状态信息
>
### 3.2 启动可视化插件
> http://localhost：9100/ head插件
> http://localhost：5601/ kibana Dev Tools - console

### 3.3 swagger
http://localhost:8080/swagger-ui.html

## 4 索引接口
> [POST] localhost:8080/indexes/student
> [GET] localhost:8080/indexes/student
> [DELETE] localhost:8080/indexes/student

## 5 查询接口




> https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current
> https://docs.spring.io/spring-data/elasticsearch/docs/3.2.3.RELEASE/reference/html/#reference
> https://github.com/spring-projects/spring-data-elasticsearch