# Elasticsearch索引API

------

这些API负责管理索引的所有方面，如设置，别名，映射，索引模板。

## 创建索引

此API可用于创建索引。 当用户将`JSON`对象传递到任何索引时，可以自动创建索引，也可以在此之前创建索引。 要创建索引，只需要发送包含设置，映射和别名的发布请求，或者只发送一个没有正文的简单请求。 例如，

```
POST http://localhost:9200/colleges
```

**响应**

```json
{"acknowledged":true}
```

或者，加上一些设置 -

```
POST http://localhost:9200/colleges
```

**请求正文**

```json
{
   "settings" : {
      "index" : {
         "number_of_shards" : 5, "number_of_replicas" : 3
      }
   }
}
```

**响应**

```json
{"acknowledged":true}
```

或使用映射 -

```
POST http://localhost:9200/colleges
```

**请求正文**

```json
{
   "settings" : {
      "number_of_shards" : 3
   },

   "mappings" : {
      "type1" : {
         "_source" : { "enabled" : false }, "properties" : {
            "college_name" : { "type" : "string" }, "college type" : {"type":"string"}
         }
      }
   }
}
```

**响应**

```json
{"acknowledged":true}
```

或者，用别名 -

```
POST http://localhost:9200/colleges
```

**请求正文**

```json
{
   "aliases" : {
      "alias_1" : {}, "alias_2" : {
         "filter" : {
            "term" : {"user" : "manu" }
         },
         "routing" : "manu"
      }
   }
}
```

**响应**

```json
{"acknowledged":true}
```

## 删除索引

此API可用来删除任何索引。只需要传递一个删除请求以及指定索引的URL。 例如，

```
DELETE http://localhost:9200/colleges
```

可以通过使用`_all，*`删除所有索引。

## 获取索引

这个API可以通过发送`get`请求到一个或多个索引来调用。这将返回有关索引的信息。

```
GET http://localhost:9200/schools
```

**响应**

```json
{
   "schools":{
      "aliases":{}, "mappings":{
         "school":{
            "properties":{
               "city":{"type":"string"}, "description":{"type":"string"},
               "fees":{"type":"long"}, "location":{"type":"double"},
               "name":{"type":"string"}, "rating":{"type":"string"},
               "state":{"type":"string"}, "street":{"type":"string"},
               "tags":{"type":"string"}, "zip":{"type":"string"}
            }
         }
      },

      "settings":{
         "index":{
            "creation_date":"1454409831535", "number_of_shards":"5",
            "number_of_replicas":"1", "uuid":"iKdjTtXQSMCW4xZMhpsOVA",
            "version":{"created":"2010199"}
         }
      },
      "warmers":{}
   }
}
```

可以使用`_all`或`*`来获取所有索引的信息。

## 测试索引存在

可以通过向该索引发送获取请求来确定索引的存在。如果HTTP响应为`200`，则存在; 如果是`404`，它不存在。

## 打开/关闭索引API

通过在`post`中添加`_close`或`_open`来请求索引，可以很容易地关闭或打开一个或多个索引。 例如，
关闭索引-

```
POST http://localhost:9200/schools/_close
```

或打开索引-

```
POST http://localhost:9200/schools/_open
```

## 索引别名

此API有助于使用`_aliases`关键字向任何索引提供别名。 单个别名可以映射到多个别名，且别名不能与索引具有相同的名称。 例如，

```
POST http://localhost:9200/_aliases
```

**请求正文**

```json
{
   "actions" : [
      { "add" : { "index" : "schools", "alias" : "schools_pri" } }
   ]
}
```

**响应**

```json
{"acknowledged":true}
```

然后，

```
GET http://localhost:9200/schools_pri
```

**响应**

```json
{"schools":{"aliases":{"schools_pri":{}},"}}
```

## 索引设置

可以通过在URL结尾处附加`_settings`关键字来获取索引设置。 例如，

```
GET http://localhost:9200/schools/_settings
```

**响应**

```json
{
   "schools":{
      "settings":{
         "index":{
            "creation_date":"1454409831535", "number_of_shards":"5", 
            "number_of_replicas":"1", "uuid":"iKdjTtXQSMCW4xZMhpsOVA", 
            "version":{"created":"2010199"}
        }
      }
   }
}
```

## 分析

此API有助于分析文本并使用偏移值和数据类型发送令牌。 例如，

```
POST http://localhost:9200/_analyze
```

**请求正文**

```json
{
   "analyzer" : "standard",
   "text" : "you are reading this at YIIBAI point"
}
```

**响应**

```json
{
   "tokens":[
      {"token":"you", "start_offset":0, "end_offset":3, "type":"<ALPHANUM>", "position":0},
      {"token":"are", "start_offset":4, "end_offset":7, "type":"<ALPHANUM>", "position":1},
      {"token":"reading", "start_offset":8, "end_offset":15, "type":"<ALPHANUM>", "position":2},
      {"token":"this", "start_offset":16, "end_offset":20, "type":"<ALPHANUM>", "position":3},
      {"token":"at", "start_offset":21, "end_offset":23, "type":"<ALPHANUM>", "position":4},
      {"token":"tutorials", "start_offset":24, "end_offset":33, "type":"<ALPHANUM>", "position":5},
      {"token":"point", "start_offset":34, "end_offset":39, "type":"<ALPHANUM>", "position":6}
   ]
}
```

还可以使用任何索引分析文本，然后根据与该索引关联的分析器来分析文本。

## 索引模板

还可以创建具有映射的索引模板，这可以应用于新的索引。 例如，

```
POST http://localhost:9200/_template/template_a
```

**响应**

```json
{
   "template" : "tu*", 
      "settings" : {
         "number_of_shards" : 3
   },

   "mappings" : {
      "chapter" : {
         "_source" : { "enabled" : false }
      }
   }
}
```

以“`tu`”开头的任何索引都将具有与模板相同的设置。

## 索引统计

此API可用于提取有关特定索引的统计信息。只需要发送一个带有索引`URL`和`_stats`关键字的`get`请求。

```
GET http://localhost:9200/schools/_stats
```

**响应**

```json
………………………………………………
{"_shards":{"total":10, "successful":5, "failed":0}, "_all":{"primaries":{"docs":{
   "count":3, "deleted":0}}}, "store":{"size_in_bytes":16653, "throttle_time_in_millis":0},
………………………………………………
```

## 刷新清除数据

此API用于从索引内存中清除数据，并将其迁移到索引存储，并清除内部事务日志。 例如，

```
GET http://localhost:9200/schools/_flush
```

**响应**

```json
{"_shards":{"total":10, "successful":5, "failed":0}}
```

## 　刷新索引

默认情况下，刷新在**Elasticsearch**中一般按计划来执行，但可以使用`_refresh`显式刷新一个或多个索引。 例如，

```
GET http://localhost:9200/schools/_refresh
```

**响应**

```json
{"_shards":{"total":10, "successful":5, "failed":0}}
```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-index-apis.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处