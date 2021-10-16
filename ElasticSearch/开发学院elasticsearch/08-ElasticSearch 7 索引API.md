## ElasticSearch 7 索引API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/hctm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=2487876654&amp;s2=3175171228&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=e5814e1b17555c9e&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345318399&amp;ti=ElasticSearch%207%20%E7%B4%A2%E5%BC%95API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x4984&amp;cfv=0&amp;cpl=16&amp;chi=8&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-index-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-aggregations.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345318&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345318351.131.131.131" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 这些API负责管理索引的所有方面，如设置、别名、映射、索引模板。

**创建索引**

 这个API用来创建索引，当用户将JSON对象传递给索引时，可以自动创建索引，也可以在此之前手动创建索引。要创建索引，您只需要发送一个带有settings、mappings和aliases的post请求，或者只发送一个不带正文的简单请求。例如，

```
POST /colleges
```

**响应**

```
{"acknowledged":true}
  或者，附带一些settings.
POST /colleges
{
   "settings" : {
      "index" : {
         "number_of_shards" : 5, "number_of_replicas" : 3
      }
   }
}
```

**响应**

```
{"acknowledged":true}
```

 或者，附带一些mapping.

```
POST /colleges
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

```
{"acknowledged":true}
```

 或者，附带一些alias。

```
POST /colleges
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

```
{"acknowledged":true}
```

**删除索引**

 该API用来删除任何索引。您只需要传递一个带有特定索引名称的删除请求。例如，

```
DELETE /colleges
```

 使用_all，*即可删除所有索引。

**获取索引**

 这个API可以通过向一个或多个索引发送get请求，这将返回关于索引的信息。

```
GET /schools
```

**响应**

```
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

 您可以使用_all或*获取所有的信息。

**检查索引是否存在**

 向某个索引发送GET请求，就可以确定该索引是否存在。如果HTTP响应为200，则存在；如果是404，则不存在。

**开启/关闭索引**

 通过在请求中添加_close或_open来请求索引，关闭或打开一个或多个索引非常容易。例如，

```
POST /schools/_close
```

或者

```
POST /schools/_open
```

**索引别名**

 这个API通过使用_aliases关键字帮助给索引赋予别名。单个别名可以映射到多个别名，别名不能与索引同名。例如，

```
POST /_aliases
{
   "actions" : [
      { "add" : { "index" : "schools", "alias" : "schools_pri" } }
   ]
}
```

**响应**

```
{"acknowledged":true}
```

然后，

```
GET /schools_pri
```

**响应**

```
………………………………………………
{"schools":{"aliases":{"schools_pri":{}},"}}
………………………………………………
```

**索引设置**

 可以通过在网址末尾附加_settings关键字来获得索引的设置。例如，

```
GET /schools/_settings
```

**响应**

```
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

**分词**

 该API帮助分析文本并发送带有偏移值和数据类型的token。例如，

```
POST /_analyze
{
   "analyzer" : "standard",
   "text" : "you are reading this at tutorials point"
}
```

**响应**

```
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

 您也可以使用索引来分析文本，然后将根据与该索引相关联的分析器来分析文本。

**索引模板**

 您还可以创建带有映射的索引模板，这些模板可以应用于新的索引。例如，

```
POST /_template/template_a
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

 任何以“tu”开头的索引都将具有与template_a相同的设置。

**索引统计**

 该API帮助您提取特定索引的统计信息。您只需要发送一个带有索引网址和_stats关键字的获取请求。

```
GET /schools/_stats
```

**响应**

```
………………………………………………
{"_shards":{"total":10, "successful":5, "failed":0}, "_all":{"primaries":{"docs":{
   "count":3, "deleted":0}}}, "store":{"size_in_bytes":16653, "throttle_time_in_millis":0},
………………………………………………
```

**Flush**

 此API用于清除索引内存中的数据并将其迁移到索引存储中，还可以清除内部事务日志。例如，

```
GET /schools/_flush
```

**响应**

```
{"_shards":{"total":10, "successful":5, "failed":0}}
```

**刷新**

 默认情况下，ElasticSearch会定时刷新，但是您可以使用_refresh显式刷新一个或多个索引。例如，

```
GET /schools/_refresh
```

**响应**

```
{"_shards":{"total":10, "successful":5, "failed":0}}
```