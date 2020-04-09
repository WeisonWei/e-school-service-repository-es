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
对外暴露端口：9200
内部使用端口：9300

Elastic会在默认的9200端口运行，请求该端口，会得到说明信息：
> http://localhost:9200/ 说明信息
> http://localhost:9200/_cluster/state 节点状态信息
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

### 3.2 Head插件安装
> https://github.com/mobz/elasticsearch-head

```jql
使用内置服务器运行
git clone git://github.com/mobz/elasticsearch-head.git
cd elasticsearch-head
npm install
npm run start
open http：// localhost：9100 /
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
## 5 局部更新
> 需要doc包裹一下
[POST]localhost:9200/school/student/13/_update
```java
{"doc":
     {
        "name": "昌平"
     }
}
```
## 6 搜索 即：查询
### 6.1 全量查询
[GET]localhost:9200/school/student/_search?pretty
### 6.2 简单查询
[GET]localhost:9200/school/student/_search?q=name:昌平
### 6.3 DSL(Domain Specific Language)特定领域语言
#### 6.3.1 简单查询
[POST]localhost:9200/school/student/_search
```java
{
	"query":{
		"match":{
			"name":"昌平",
			"age":"昌平"
		}
	}
}
```
#### 6.3.2 复杂查询
[POST]localhost:9200/school/student/_search

```java
{
	"query":{
		"bool":{
			"filter":{
			"range":{
				"age":{
					"gt":5
				}
			}
		},
		"must":{
			"match":{
			"version":2
			}
		}
		}
	}
}
```
#### 6.3.3 全文检索
[POST]localhost:9200/school/student/_search
```java
{
	"query":{
		"match":{
			"name": "Leo Wade"
			}
	}
}
```
#### 6.3.4 高亮显示
[POST]localhost:9200/school/student/_search
```java
{
	"query":{
		"match":{
			"name": "Leo Wade"
			}
	},
	"highlight":{
		"fields":{
			"name":{}
		}
	}
}
```

#### 6.3.5 按需返回
[GET]localhost:9200/school/student/11/_source -->只返回原始数据
[GET]localhost:9200/school/student/11/_source?_source=id,name --返回原始数据中的某些字段
```java
{
    "name": "Terence",
    "id": 11
}
```
#### 6.3.6 文档是否存在
[HEAD]localhost:9200/school/student/18
存在返回200
不存在返回404
```java
404Not Found
```

## 7 批量操作[增删改查]

### 7.1 批量查询
[POST]localhost:9200/school/student/_mget

```java
{
    "ids":["11","12"]
}
```
返回
```java
{
    "docs": [
        {
            "_index": "school",
            "_type": "student",
            "_id": "11",
            "_version": 3,
            "_seq_no": 12,
            "_primary_term": 5,
            "found": true,
            "_source": {
                "id": 11,
                "name": "Terence",
                "age": 5,
                "addressId": 3,
                "version": 3
            }
        },
        {
            "_index": "school",
            "_type": "student",
            "_id": "12",
            "_version": 3,
            "_seq_no": 8,
            "_primary_term": 5,
            "found": true,
            "_source": {
                "id": 12,
                "name": "Wade",
                "age": 6,
                "addressId": 2,
                "version": 2
            }
        }
    ]
}
```

### 7.2 批量插入
[POST]localhost:9200/school/student/_bulk
```java
{"create":{"_index":"school","_type":"student","_id":"448"}}
{"id": 128,"name": "Wade1","age": 6,"addressId": 2,"version": 2}
{"create":{"_index":"school","_type":"student","_id":"449"}}
{"id": 129,"name": "Wade2","age": 6,"addressId": 2,"version": 2}
```

### 7.3 批量删除
[POST]localhost:9200/school/student/_bulk
```java
{"delete":{"_index":"school","_type":"student","_id":"448"}}
{"delete":{"_index":"school","_type":"student","_id":"449"}}
```

### 7.4 批量操作 数量大小
根据硬件，应用环境不同，没有固定值，需要去测试得到这个值


## 8 分页
[GET]localhost:9200/school/student/_search?size=5&from=10

分页每页量不能太大，太大会影响性能

## 8 映射 Mapping
es会自动映射字段类型，但有时候因为分词的需要，需要明确字段类型，主要是字符串的映射；
5.0之后：string --> text(需要全文搜索，即分词) 和 keyword(不需要做分词)

### 8.1 新建index
[PUT]localhost:9200/school/user
7.X不再支持声明索引类型
```json
{
  "settings": {
    "number_of_shards": "2",
    "number_of_replicas": "0"
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text"
      },
      "age": {
        "type": "integer"
      },
      "mail": {
        "type": "keyword"
      },
      "hobby": {
        "type": "text"
      }
    }
  }
}
```
查询索引库映射：
[GET]localhost:9200/school/_mapping

新增数据：

[GET]localhost:9200/school/student/_search

[GET] http://localhost:9200/_cat/indices?v 查询index

## 9 结构化查询
### 9.1 term & terms
term 精确匹配：数字 日期 布尔 不能做分词的关键字
[POST]localhost:9200/school/student/_search
```java
{
	"query":{
		"term":{
			"name": "Leo Wade"
			}
	}
}

{
	"query":{
		"terms":{
			"name":[ "Leo","Wade"]
			}
	}
}
```
### 9.2 range
[POST]localhost:9200/school/student/_search
gt --> greater than 大于
gte --> greater than equal 大于等于
lt --> less than 小于
lte --> less than equal 小于等于
```java
{
  "query": {
    "range": {
      "age": {
        "gte": 4,
        "lte": 6
      }
    }
  }
}
```
### 9.3 exist
[POST]localhost:9200/school/student/_search
包含某字段的doc
```java
{
  "query": {
    "exists": {
      "field": "name"
    }
  }
}
```
### 9.4 match
[POST]localhost:9200/school/student/_search
可以进行结构化查询和分词查询
```java
{
  "query": {
    "match": {
      "age": 5
    }
  }
}
```

### 9.5 bool
[POST]localhost:9200/school/student/_search
操作符：
must --> 一定要匹配
must not --> 都不要匹配
should --> 至少有一个匹配
```java
{
  "query": {
    "bool": {
      "must": {"match":{"addressid":2}},
      "must_not": {"match":{"version":4}},
      "should": [{"term":[{"age":6}]}]
    }
  }
}
```

### 9.6 过滤查询
[POST]localhost:9200/school/student/_search
操作符：filter
```json
{
  "query": {
    "bool": {
      "filter": {"term":{"age":5}}
    }
  }
}
``` 
> 精确匹配时最好用过滤查询



## 10 分词
标准分词器
IK中文分词器

## 11 全文搜索
结构所搜
单词搜索 match
多词搜索 match operator:and or query:{} 也可设置相似度minimum_should_match =80%  数值不固定 需反复调整
组合搜索 上边的都用上就是组合查询
权重  通过设置查询条件的权重boost 影响查询结果的得分score







>
>https://docs.spring.io/spring-data/elasticsearch/docs/3.2.3.RELEASE/reference/html/#reference
>https://github.com/spring-projects/spring-data-elasticsearch



