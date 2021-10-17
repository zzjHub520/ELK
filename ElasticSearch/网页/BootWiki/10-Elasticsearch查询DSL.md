# Elasticsearch查询DSL

------

在**Elasticsearch**中，通过使用基于JSON的查询进行搜索。 查询由两个子句组成 -

- **叶查询子句** - 这些子句是匹配，项或范围的，它们在特定字段中查找特定值。
- **复合查询子句** - 这些查询是叶查询子句和其他复合查询的组合，用于提取所需的信息。

Elasticsearch支持大量查询。 查询从查询关键字开始，然后以`JSON`对象的形式在其中包含条件和过滤器。以下描述了不同类型的查询 -

## 匹配所有查询

这是最基本的查询; 它返回所有内容，并为每个对象的分数为`1.0`。 例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "query":{
      "match_all":{}
   }
}
```

**响应**

```json
{
   "took":1, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0},
   "hits":{
      "total":5, "max_score":1.0, "hits":[
         {
            "_index":"schools", "_type":"school", "_id":"2", "_score":1.0,
            "_source":{
               "name":"Saint Paul School", "description":"ICSE Affiliation",
               "street":"Dawarka", "city":"Delhi", "state":"Delhi", 
               "zip":"110075", "location":[28.5733056, 77.0122136], "fees":5000, 
               "tags":["Good Faculty", "Great Sports"], "rating":"4.5"
            }
         },

         {
            "_index":"schools_gov", "_type":"school", "_id":"2", "_score":1.0,
            "_source":{
               "name":"Government School", "description":"State Board Affiliation",
               "street":"Hinjewadi", "city":"Pune", "state":"MH", "zip":"411057",
               "location":[18.599752, 73.6821995], "fees":500, "tags":["Great Sports"],
               "rating":"4"
            }
         },

         {
            "_index":"schools", "_type":"school", "_id":"1", "_score":1.0,
            "_source":{
               "name":"Central School", "description":"CBSE Affiliation",
               "street":"Nagan", "city":"paprola", "state":"HP", 
               "zip":"176115", "location":[31.8955385, 76.8380405], 
               "fees":2200, "tags":["Senior Secondary", "beautiful campus"], 
               "rating":"3.3"
            }
         },

         {
            "_index":"schools_gov", "_type":"school", "_id":"1", "_score":1.0,
            "_source":{
               "name":"Model School", "description":"CBSE Affiliation",
               "street":"silk city", "city":"Hyderabad", "state":"AP", 
               "zip":"500030", "location":[17.3903703, 78.4752129], "fees":700, 
               "tags":["Senior Secondary", "beautiful campus"], "rating":"3"
            }
         },

         {
            "_index":"schools", "_type":"school", "_id":"3", "_score":1.0,
            "_source":{
               "name":"Crescent School", "description":"State Board Affiliation",
               "street":"Tonk Road", "city":"Jaipur", "state":"RJ", "zip":"176114",
               "location":[26.8535922, 75.7923988], "fees":2500, 
               "tags":["Well equipped labs"], "rating":"4.5"
            }
         }
      ]
   }
}
```

## 全文查询

这些查询用于搜索整个文本，如章节或新闻文章。 此查询根据与特定索引或文档相关联的分析器一起工作。 在本节中，我们将讨论不同类型的全文查询。

## 匹配查询

此查询将文本或短语与一个或多个字段的值匹配。 例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "query":{
      "match" : {
         "city":"pune"
      }
   }
}
```

**响应**

```json
{
   "took":1, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0},
   "hits":{
      "total":1, "max_score":0.30685282, "hits":[{
         "_index":"schools_gov", "_type":"school", "_id":"2", "_score":0.30685282, 
         "_source":{
            "name":"Government School", "description":"State Board Afiliation",
            "street":"Hinjewadi", "city":"Pune", "state":"MH", "zip":"411057",
            "location":[18.599752, 73.6821995], "fees":500, 
            "tags":["Great Sports"], "rating":"4"
         }
      }]
   }
}
```

## multi_match查询

此查询将文本或短语与多个字段匹配。 例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "query":{
      "multi_match" : {
         "query": "hyderabad",
         "fields": [ "city", "state" ]
      }
   }
}
```

**响应**

```json
{
   "took":16, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0},
   "hits":{
      "total":1, "max_score":0.09415865, "hits":[{
         "_index":"schools_gov", "_type":"school", "_id":"1", "_score":0.09415865,
         "_source":{
            "name":"Model School", " description":"CBSE Affiliation", 
            "street":"silk city", "city":"Hyderabad", "state":"AP", 
            "zip":"500030", "location":[17.3903703, 78.4752129], "fees":700, 
            "tags":["Senior Secondary", "beautiful campus"], "rating":"3"
         }
      }]
   }
}
```

## 查询字符串查询

此查询使用查询解析器和`query_string`关键字。 例如，

```
POST http://localhost:9200/schools/_search
```

**请求正文**

```json
{
   "query":{
      "query_string":{
         "query":"good faculty"
      }
   }
}
```

**响应**

```json
{
   "took":16, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0}, 
   "hits":{
      "total":1, "max_score":0.09492774, "hits":[{
         "_index":"schools", "_type":"school", "_id":"2", "_score":0.09492774, 
         "_source":{
            "name":"Saint Paul School", "description":"ICSE Affiliation",
            "street":"Dawarka", "city":"Delhi", "state":"Delhi",
            "zip":"110075", "location":[28.5733056, 77.0122136],
            "fees":5000, "tags":["Good Faculty", "Great Sports"],
            "rating":"4.5" 
         }
      }]
   }
}
```

## 期限等级查询

这些查询主要处理结构化数据，如数字，日期和枚举。 例如，

```
POST http://localhost:9200/schools/_search
```

**请求正文**

```json
{
   "query":{
      "term":{"zip":"176115"}
   }
}
```

**响应**

```json
{
   "took":1, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0},
   "hits":{
      "total":1, "max_score":0.30685282, "hits":[{
         "_index":"schools", "_type":"school", "_id":"1", "_score":0.30685282,
         "_source":{
            "name":"Central School", "description":"CBSE Affiliation",
            "street":"Nagan", "city":"paprola", "state":"HP", "zip":"176115",
            "location":[31.8955385, 76.8380405], "fees":2200, 
            "tags":["Senior Secondary", "beautiful campus"], "rating":"3.3"
         }
      }]
   }
}
```

## 范围查询

此查询用于查找值的范围之间的值的对象。 为此，需要使用类似 -

- gte − 大于和等于
- gt − 大于
- lte − 小于和等于
- lt − 小于

例如 - 

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "query":{
      "range":{
         "rating":{
            "gte":3.5
         }
      }
   }
}
```

**响应**

```json
{
   "took":31, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0},
   "hits":{
      "total":3, "max_score":1.0, "hits":[
         {
            "_index":"schools", "_type":"school", "_id":"2", "_score":1.0,
            "_source":{
               "name":"Saint Paul School", "description":"ICSE Affiliation",
               "street":"Dawarka", "city":"Delhi", "state":"Delhi", 
               "zip":"110075", "location":[28.5733056, 77.0122136], "fees":5000, 
               "tags":["Good Faculty", "Great Sports"], "rating":"4.5"
            }
         }, 

         {
            "_index":"schools_gov", "_type":"school", "_id":"2", "_score":1.0, 
            "_source":{
               "name":"Government School", "description":"State Board Affiliation",
               "street":"Hinjewadi", "city":"Pune", "state":"MH", "zip":"411057",
               "location":[18.599752, 73.6821995] "fees":500, 
               "tags":["Great Sports"], "rating":"4"
            }
         },

         {
            "_index":"schools", "_type":"school", "_id":"3", "_score":1.0,
            "_source":{
               "name":"Crescent School", "description":"State Board Affiliation",
               "street":"Tonk Road", "city":"Jaipur", "state":"RJ", "zip":"176114", 
               "location":[26.8535922, 75.7923988], "fees":2500,
               "tags":["Well equipped labs"], "rating":"4.5"
            }
         }
      ]
   }
}
```

其他类型的期限级查询是 -

- **存在的查询** - 如果某一个字段有非空值。
- **缺失的查询** - 这与**存在查询**完全相反，此查询搜索没有特定字段的对象或有空值的字段。
- **通配符或正则表达式查询** - 此查询使用正则表达式来查找对象中的模式。

**类型查询** - 具有特定类型的文档。 例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "query":{
      "type" : {
         "value" : "school"
      }
   }
}
```

**响应**
存在于指定的索引中的所有学校的`JSON`对象。

## 复合查询

这些查询是通过使用如**和**，**或**，**非**和**或**等，用于不同索引或具有函数调用等的布尔运算符彼此合并的不同查询的集合。例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "query":{
      "filtered":{
         "query":{
            "match":{
               "state":"UP"
            }
         },

         "filter":{
            "range":{
               "rating":{
                  "gte":4.0
               }
            }
         }
      }
   }
}
```

**响应**

```json
{
   "took":16, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0},
   "hits":{"total":0, "max_score":null, "hits":[]}
}
```

## 连接查询

这些查询用于包含多个映射或文档的位置。 **连接查询**有两种类型 -

**嵌套查询**
这些查询处理嵌套映射(将在下一章阅读了解更多内容)。

**has_child和has_parent查询**

这些查询用于检索在查询中匹配的文档的子文档或父文档。 例如，

```
POST http://localhost:9200/tutorials/_search
```

**请求正文**

```json
{
   "query":
   {
      "has_child" : {
         "type" : "article", "query" : {
            "match" : {
               "Text" : "This is article 1 of chapter 1"
            }
         }
      }
   }
}
```

**响应**

```json
{
   "took":21, "timed_out":false, "_shards":{"total":5, "successful":5, "failed":0},
   "hits":{
      "total":1, "max_score":1.0, "hits":[{
         "_index":"tutorials", "_type":"chapter", "_id":"1", "_score":1.0,
         "_source":{
            "Text":"this is chapter one"
         }
      }]
   }
}
```

## 地理查询

这些查询处理地理位置和地理点。这些查询有助于查找任何位置附近的学校或任何其他地理对象。需要使用地理位置数据类型。 例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "query":{
      "filtered":{
         "filter":{
            "geo_distance":{
               "distance":"100km",
               "location":[32.052098, 76.649294]
            }
         }
      }
   }
}
```

**响应**

```
{
   "took":6, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0},
   "hits":{
      "total":2, "max_score":1.0, "hits":[
         {
            "_index":"schools", "_type":"school", "_id":"2", "_score":1.0,
            "_source":{
               "name":"Saint Paul School", "description":"ICSE Affiliation",
               "street":"Dawarka", "city":"Delhi", "state":"Delhi", "zip":"110075",
               "location":[28.5733056, 77.0122136], "fees":5000,
               "tags":["Good Faculty", "Great Sports"], "rating":"4.5"
            }
         },

         {
            "_index":"schools", "_type":"school", "_id":"1", "_score":1.0,
            "_source":{
               "name":"Central School", "description":"CBSE Affiliation",
               "street":"Nagan", "city":"paprola", "state":"HP", "zip":"176115",
               "location":[31.8955385, 76.8380405], "fees":2000,
               "tags":["Senior Secondary", "beautiful campus"], "rating":"3.5"
            }
         }
      ]
   }
}
```

> 注意 - 如果在执行上述示例时遇到异常，请将以下映射添加到索引。

```json
{
   "mappings":{
      "school":{
         "_all":{
            "enabled":true
         },

         "properties":{
            "location":{
               "type":"geo_point"
            }
         }
      }
   }
}
```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-query-dsl.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处