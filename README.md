# school-service-elasticsearch
spring boot 2.2.2
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
```bash
$ ./bin/elasticsearch
```

Elastic会在默认的9200端口运行，请求该端口，会得到说明信息：
> http://localhost:9200/
```json
{
  "name" : "Weison.local",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "hdUpXDWIQ5W9V5gpbzYgEw",
  "version" : {
    "number" : "7.6.2",
    "build_flavor" : "default",
    "build_type" : "tar",
    "build_hash" : "ef48eb35cf30adf4db14086e8aabd07ef6fb113f",
    "build_date" : "2020-03-26T06:34:37.794943Z",
    "build_snapshot" : false,
    "lucene_version" : "8.4.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}

```
## 4 增删改查

> [POST] localhost:9200/conference/event
```json
{
  "host": "Dave",
  "title": "Elasticsearch at Rangespan and Exonar",
  "description": "Representatives from Rangespan and Exonar will come and discuss how they use Elasticsearch",
  "attendees": ["Dave", "Andrew", "David", "Clint"],
  "date": "2013-06-24T18:30",
  "reviews": 3
}
```


> [PUT] localhost:9200/conference/event/zOMeO3EBhhCSVQ9Aj3q4
```json
{
  "host": "Dave",
  "title": "Elasticsearch at Rangespan and Exonar",
  "description": "Representatives from Rangespan and Exonar will come and discuss how they use Elasticsearch",
  "attendees": ["Dave", "Andrew", "David", "Clint"],
  "date": "2013-06-24T18:30",
  "reviews": 5
}
```

> [DELETE] localhost:9200/conference/event/zOMeO3EBhhCSVQ9Aj3q4 

> [GET] localhost:9200/conference/event/zOMeO3EBhhCSVQ9Aj3q4
> [GET] localhost:9200/conference/event/_search?pretty
> [GET] localhost:9200/conference/event/_search?pretty
```json
{
    "query" : {
        "match" : {
            "host" : "Dave"
        },
        "match_phrase": {
            "description" : "use Elasticsearch"
        }
    }
}
```


对外暴露端口：9200
内部使用端口：9300

>https://docs.spring.io/spring-data/elasticsearch/docs/3.2.3.RELEASE/reference/html/#reference
>https://github.com/spring-projects/spring-data-elasticsearch



