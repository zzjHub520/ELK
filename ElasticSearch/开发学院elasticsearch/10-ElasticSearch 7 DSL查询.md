## ElasticSearch 7 DSL查询

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/lcpm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=3590760516&amp;s2=683041075&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=dc79fe56d5e537fb&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345452636&amp;ti=ElasticSearch%207%20DSL%E6%9F%A5%E8%AF%A2%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x8000&amp;cfv=0&amp;cpl=16&amp;chi=10&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-query-dsl.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-cluster-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345453&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345452630.52.52.52" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 在ElasticSearch中，搜索是通过基于JSON的查询来实现的。查询由两种子句组成：

-  叶查询子句：这些子句是匹配、术语或范围，它们在特定字段中查找特定值。
-  复合查询子句：这些查询是叶查询子句和其他复合查询的组合，以提取所需的信息。

 ElasticSearch支持大数据量查询。查询以query关键字开始，然后以JSON对象的形式包含条件和过滤器。不同类型的查询描述如下

**Match All查询**

 这是最基本的查询；它返回所有内容，每个对象的得分为1.0。例如，

```
POST /schools*/_search
{
   "query":{
      "match_all":{}
   }
}
```

响应

```
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

**全文检索**

 这些查询用于搜索全文，如章节或新闻文章。该查询根据与特定索引或文档相关联的分析器工作。在本节中，我们将讨论不同类型的全文检索。

**Match查询**

 此查询将文本或短语与一个或多个字段的值相匹配。例如，

```
POST /schools*/_search
{
   "query":{
      "match" : {
         "city":"pune"
      }
   }
}
```

响应

```
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

**multi_match查询**

 此查询将文本或短语与多个字段匹配。例如，

```
POST /schools*/_search
{
   "query":{
      "multi_match" : {
         "query": "hyderabad",
         "fields": [ "city", "state" ]
      }
   }
}
```

响应

```
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

**Query String查询**

 该查询使用查询解析器和query_string关键字。例如，

```
POST /schools/_search
{
   "query":{
      "query_string":{
         "query":"good faculty"
      }
   }
}
```

响应

```
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

**Term查询**

 这些查询主要处理数字、日期等结构化数据。例如，

```
POST /schools/_search
{
   "query":{
      "term":{"zip":"176115"}
   }
}
```

**响应**

```
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

**Range查询**

 该查询用于查找值在值范围之间的对象。为此，我们需要使用如下运算符

-  gte:大于等于
-  gt:大于
-  lte:小于等于
-  lt:小于

 举个例子：

```
POST /schools*/_search
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

响应

```
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

 其他类型的term级查询有：

-  Exists查询：判断某个字段具有非空值。
-  Missing查询：与Exists查询相反，该查询搜索没有特定字段或字段值为空的对象。
-  Wildcard or regexp查询：该查询使用正则表达式来查找对象中的模式。
-  Type query查询：特定类型的文档。例如，

```
POST /schools*/_search
{
   "query":{
      "type" : {
         "value" : "school"
      }
   }
}
```

响应

 指定索引中存在的所有school JSON对象。

**复合查询**

 这些查询是通过使用布尔运算符(如and、or、not或for不同索引或具有函数调用等)彼此合并的不同查询的集合。例如，

```
POST /schools*/_search
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

响应

```
{
   "took":16, "timed_out":false, "_shards":{"total":10, "successful":10, "failed":0},
   "hits":{"total":0, "max_score":null, "hits":[]}
}
```

**Joining查询**

 这些查询用于包含多个映射或文档的情况。有两种类型的joining查询：

嵌套查询

 这些查询处理嵌套映射(您将在下一章中了解更多)。

has_child和has_parent查询

 这些查询用于检索在查询中匹配的文档的子文档或父文档。例如，

```
POST /tutorials/_search
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

响应

```
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

**地理位置查询**

 这些查询处理地理位置，这些查询有助于找到指定位置附近的学校或任何其他地点。您需要使用地理点数据类型。例如，

```
POST /schools*/_search
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

响应

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

 注意，如果在执行上述示例时出现异常，请将以下mapping添加到您的索引中。

```
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