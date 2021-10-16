## ElasticSearch 7 搜索API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/lcim?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=37066009&amp;s2=1802269826&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=119bb5ee3552c197&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634315331405&amp;ti=ElasticSearch%207%20%E6%90%9C%E7%B4%A2API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=6&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-search-apis.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-document-apis.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634315331&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634315331391.50.50.50" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 该API用于在 ElasticSearch中搜索内容。用户可以通过发送以查询字符串作为参数的get请求进行搜索，也可以在post请求的消息体中进行查询。所有的搜索APIS都支持多索引、多类型的（新版本的ES已经弱化了类型的概念，一个索引只支持一个类型）。

**多索引**

 ElasticSearch允许我们搜索所有索引或某些特定索引中的文档。例如，如果我们需要搜索所有name包含central的文档。

```
GET  /_search?q = name:central
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

 或者，我们可以在schools、schools_gov索引中搜索相同的内容

```
GET /schools,schools_gov/_search?q = name:model
```

**多类型**

 注意：新版本的ES已经弱化了类型的概念，一个索引只支持一个类型。

```
GET /schools/_search?q = tags:sports
```

**响应**

```
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

**URI搜索**

 我们可以在URI中传递许多参数

- Q：用于指定搜索的字符串
- lenient：若将此参数设置为true，就可以忽略基于格式的错误。默认情况下，它为false。
- fields:该参数帮助我们从选择性字段中获得响应。
- sort:我们可以通过使用这个参数改变排序结果，这个参数允许的值是字段名:ASC/字段名:desc
- timeout:我们可以通过使用该参数来限制搜索时间，并且响应仅包含在指定时间内的命中。默认情况下，不指定timeout。
- terminate_after:对于每个分片，我们可以将响应限制在指定数量的文档，一旦达到这个数量，查询将提前终止。默认情况下，不指定terminate_after。
- from:从指定的索引开始返回. 默认为0.
- size:表示要返回的记录数. 默认为10.

**请求体查询**

 我们也可以在请求体中使用DSL来指定查询条件，在前面的章节中已经给出了许多例子，例如：

```
POST /schools/_search
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