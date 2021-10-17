# Elasticsearch搜索API

------

此API用于在**Elasticsearch**中搜索内容。 用户可以通过发送具有查询字符串的获取请求作为参数或在请求的消息正文中的查询来进行搜索。所有的搜索API都是多索引，多类型。

## 多索引

Elasticsearch允许我们搜索存在于所有索引或一些特定索引中的文档。 例如，如果我们需要搜索名称包含`central`的所有文档。

```
GET http://localhost:9200/_search?q = name:central
```

**响应**

```
{
   "took":78, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0},
   "hits":{
      "total":1, "max_score":0.19178301, "hits":[{
         "_index":"schools", "_type":"school", "_id":"1", "_score":0.19178301,
         "_source":{
            "name":"Central School", "description":"CBSE Affiliation", 
            "street":"Nagan", "city":"paprola", "state":"HP", "zip":"176115",
            "location":[31.8955385, 76.8380405], "fees":2000, 
            "tags":["Senior Secondary", "beautiful campus"], "rating":"3.5"
         }
      }]
   }
}
```

或者，同样地我们可以在`schools`，`schools_gov`索引中搜索 -

### 多类型

还可以在所有类型或某种指定类型的索引中搜索所有文档。 例如，

```
Get http://localhost:9200/schools/_search?q = tags:sports
```

**响应**

```json
{
   "took":16, "timed_out":false, "_shards":{"total":5, "successful":5, "failed":0},
   "hits":{
      "total":1, "max_score":0.5, "hits":[{
         "_index":"schools", "_type":"school", "_id":"2", "_score":0.5,
         "_source":{
            "name":"Saint Paul School", "description":"ICSE Afiliation", 
            "street":"Dawarka", "city":"Delhi", "state":"Delhi", "zip":"110075", 
            "location":[28.5733056, 77.0122136], "fees":5000, 
            "tags":["Good Faculty", "Great Sports"], "rating":"4.5"
         }
      }]
   }
}
```

### URI搜索

如下这些参数可以使用统一资源标识符在搜索操作中传递 -

| 编号 | 参数            | 说明                                                         |
| ---- | --------------- | ------------------------------------------------------------ |
| 1    | Q               | 此参数用于指定查询字符串。                                   |
| 2    | lenient         | 基于格式的错误可以通过将此参数设置为`true`来忽略。默认情况下为`false`。 |
| 3    | fields          | 此参数用于在响应中选择返回字段。                             |
| 4    | sort            | 可以通过使用这个参数获得排序结果，这个参数的可能值是`fieldName`，`fieldName:asc`和`fieldname:desc` |
| 5    | timeout         | 使用此参数限定搜索时间，响应只包含指定时间内的匹配。默认情况下，无超时。 |
| 6    | terminate_after | 可以将响应限制为每个分片的指定数量的文档，当到达这个数量以后，查询将提前终止。 默认情况下不设置`terminate_after`。 |
| 7    |                 | 从命中的索引开始返回。默认值为`0`。                          |
| 8    | size            | 它表示要返回的命中数。默认值为`10`。                         |

### 请求正文搜索

还可以在请求正文中使用查询`DSL`来指定查询，并且在前面的章节中已经给出了很多示例，

```
POST http://localhost:9200/schools/_search
```

**请求正文**

```json
{
   "query":{
      "query_string":{
         "query":"up"
      }
   }
}
```

**响应**

```
……………………………………………….
{
   "_source":{
      "name":"City School", "description":"ICSE", "street":"West End",
      "city":"Meerut", "state":"UP", "zip":"250002", "location":[28.9926174, 77.692485],
      "fees":3500, "tags":["Well equipped labs"],"rating":"4.5"
   }
}
……………………………………………….
```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-search-apis.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处