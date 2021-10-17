# Elasticsearch映射

------

映射是存储在索引中的文档的大纲。它定义数据类型，如`geo_point`或文档和规则中存在的字段的字符串和格式，以控制动态添加的字段的映射。 例如，

```
POST http://localhost:9200/bankaccountdetails
```

**请求正文**

```json
{
   "mappings":{
      "report":{
         "_all":{
            "enabled":true
         },

         "properties":{
            "name":{ "type":"string"}, "date":{ "type":"date"},
            "balance":{ "type":"double"}, "liability":{ "type":"double"}
         }
      }
   }
}
```

**响应**

```json
{"acknowledged":true}
```

**字段数据类型**
Elasticsearch支持文档中字段的多种不同数据类型。以下数据类型用于在Elasticsearch中存储字段 -

- **核心数据类型** - 这些是几乎所有系统支持的基本数据类型，如整数，长整数，双精度，短整型，字节，双精度，浮点型，字符串，日期，布尔和二进制。
- **复杂数据类型** - 这些数据类型是核心数据类型的组合。类似数组，JSON对象和嵌套数据类型。以下是嵌套数据类型的示例 -

```
POST http://localhost:9200/tabletennis/team/1
```

**请求正文**

```json
{
   "group" : "players",
   "user" : [
      {
         "first" : "dave", "last" : "jones"
      },

      {
         "first" : "kevin", "last" : "morris"
      }
   ]
}
```

**响应**

```json
{
   "_index":"tabletennis", "_type":"team", "_id":"1", "_version":1, 
   "_shards":{"total":2, "successful":1, "failed":0}, "created":true
}
```

- **地理数据类型**
  这些数据类型用于定义地理属性。 例如，`geo_point`用于定义经度和纬度，`geo_shape`用于定义不同的几何形状，如矩形。
- **专用数据类型**
  这些数据类型用于特殊目的，如IPv4(“ip”)接受IP地址，完成数据类型用于支持自动完成建议，`token_count`用于计算字符串中的令牌数量。

## 映射类型

每个索引都具有一个或多个映射类型，用于将索引的文档划分为逻辑组。 映射可以基于以下参数有些不同 -

- **元字段**
  这些字段提供有关映射和与其关联的其他对象的信息。 例如`_index`，`_type`，`_id`和`_source`字段。
- **字段**
  不同的映射包含不同数量的字段和具有不同数据类型的字段。

## 动态映射

Elasticsearch为自动创建映射提供了一个用户友好的机制。用户可以将数据直接发布到任何未定义的映射，Elasticsearch将自动创建映射，这称为动态映射。 例如，

```
POST http://localhost:9200/accountdetails/tansferreport
```

**请求正文**

```json
{
   "from_acc":"7056443341", "to_acc":"7032460534",
   "date":"11/1/2016", "amount":10000
}
```

**响应**

```json
{
   "_index":"accountdetails", "_type":"tansferreport",
   "_id":"AVI3FeH0icjGpNBI4ake", "_version":1,
   "_shards":{"total":2, "successful":1, "failed":0},
   "created":true
}
```

## 映射参数

映射参数定义映射的结构，关于字段和关于存储的信息以及如何在搜索时分析映射的数据。 这些是以下映射参数 -

- analyzer
- boost
- coerce
- copy_to
- doc_values
- dynamic
- enabled
- fielddata
- geohash
- geohash_precision
- geohash_prefix
- format
- ignore_above
- ignore_malformed
- include_in_all
- index_options
- lat_lon
- index
- fields
- norms
- null_value
- position_increment_gap
- properties
- search_analyzer
- similarity
- store
- term_vector



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-mapping.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处