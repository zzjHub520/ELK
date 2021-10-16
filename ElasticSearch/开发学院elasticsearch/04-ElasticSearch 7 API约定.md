## ElasticSearch 7 API约定

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/ycbm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=3436312236&amp;s2=1419304327&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=96e11baf9c0b5143&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634315214244&amp;ti=ElasticSearch%207%20API%E7%BA%A6%E5%AE%9A%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x4604&amp;cfv=0&amp;cpl=16&amp;chi=4&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-conventions.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-populate.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634315214&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634315214237.42.42.42" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 网络中的API是一组函数调用或其他编程指令，用于访问特定网络应用中的软件组件。例如，Facebook API帮助开发者通过从Facebook访问数据或其他功能来创建应用程序，它可以获取出生日期或更新用户状态。

 ElasticSearch提供了REST风格API，可以通过URL访问JSON。ElasticSearch使用以下约定。

**多重索引**

 API中的大多数操作，主要是针对一个或多个索引的搜索和其他操作。这有助于用户通过只执行一次查询请求，在多个地方或所有可用数据中进行搜索。许多不同的符号用于在多个索引中执行操作，我们将在这一部分讨论其中的一些。

**逗号分隔符号**

```
POST /index1,index2,index3/_search
{
   "query":{
      "query_string":{
         "query":"any_string"
      }
   }
}
```

**响应**

 index1、index2、index3中包含any_string的JSON对象。

**_all关键字**

```
POST /_all/_search
{
   "query":{
      "query_string":{
         "query":"any_string"
      }
   }
}
```

**响应**

 所有索引中包含any_string的JSON对象。

**通配符( \* , + , – )**

```
POST /school*/_search
{
   "query":{
      "query_string":{
         "query":"CBSE"
      }
   }
}
```

**响应**

 从所有school*开头的索引中找出包含“CBSE”的JSON对象。

 或者，您也可以使用以下代码

```
POST /school*,-schools_gov /_search
{
   "query":{
      "query_string":{
         "query":"CBSE"
      }
   }
}
```

**响应**

 从所有school*开头但是不包含schools_gov的索引中找出包含“CBSE”的JSON对象。

 还有一些URL查询字符串参数：

 ignore_unavailabl：如果不存在URL中的一个或多个索引，将不会发生错误或停止操作。例如，schools索引存在，但book_shops不存在

```
POST /school*,book_shops/_search
{
   "query":{
      "query_string":{
         "query":"CBSE"
      }
   }
}
```

**响应**

```
{
   "error":{
      "root_cause":[{
         "type":"index_not_found_exception", "reason":"no such index",
         "resource.type":"index_or_alias", "resource.id":"book_shops", 
         "index":"book_shops"
      }],
      "type":"index_not_found_exception", "reason":"no such index",
      "resource.type":"index_or_alias", "resource.id":"book_shops", 
      "index":"book_shops"
   },"status":404
}
```

 加上ignore_unavailable参数后的代码。

```
POST /school*,book_shops/_search?ignore_unavailable = true
{
   "query":{
      "query_string":{
         "query":"CBSE"
      }
   }
}
```

**响应**

 无任何报错信息，忽略了不存在的索引，从所有school*开头的索引中找出包含“CBSE”的JSON对象。

 allow_no_indices：如果没有指定通配符的索引，true值会防止引发错误。

 举个例子，如果没有以schools_pri开头的索引。

```
POST  /schools_pri*/_search?allow_no_indices = true
{
   "query":{
      "match_all":{}
   }
}
```

**响应 (无报错)**

```
{
   "took":1,"timed_out": false, "_shards":{"total":0, "successful":0, "failed":0}, 
   "hits":{"total":0, "max_score":0.0, "hits":[]}
}
```

 expand_wildcards：此参数决定通配符是否需要扩展为开放索引或封闭索引，或者两者兼有。该参数的值可以是open和close，也可以是none和all。

**例子**

```
POST  /schools/_close
```

**响应**

```
{"acknowledged":true}
```

 请看下面的代码。

```
POST /school*/_search?expand_wildcards = closed
{
   "query":{
      "match_all":{}
   }
}
```

**响应**

```
{
   "error":{
      "root_cause":[{
         "type":"index_closed_exception", "reason":"closed", "index":"schools"
      }],
      "type":"index_closed_exception", "reason":"closed", "index":"schools"
   }, "status":403
}
```

**索引名称中的日期数学支持**

 ElasticSearch提供了根据日期和时间搜索索引的功能。我们需要以特定的格式指定日期和时间。例如，accountdetail-2015.12.30，index将存储2015年12月30日的银行帐户详细信息。可以执行数学运算来获得特定日期或日期和时间范围的细节。

 日期数学索引名称的格式：

```
<static_name{date_math_expr{date_format|time_zone}}>
/<accountdetail-{now-2d{YYYY.MM.dd|utc}}>/_search
```

 static_name是表达式的一部分，它在每个日期的数学索引中保持不变，如帐户详细信息。date_math_expr包含像now-2d那样动态确定日期和时间的数学表达式。date_format包含日期以类似于年月日的索引形式写入的格式。如果今天的日期是2015年12月30日，则<<accountdetail-{now-2d{YYYY.MM.dd}}>将返回accountdetail-2015.12.28。

| 表达式                         | 解析为                   |
| :----------------------------- | :----------------------- |
| <accountdetail-{now-d}>        | accountdetail-2015.12.29 |
| <accountdetail-{now-M}>        | accountdetail-2015.11.30 |
| <accountdetail-{now{YYYY.MM}}> | accountdetail-2015.12    |

 我们现在将看到ElasticSearch中的一些常见选项，这些选项可以用来以指定的格式获得响应。

**漂亮的结果**

 我们可以获得格式良好的JSON对象，只需附加一个URL 查询参数，即pretty = true。

```
POST /schools/_search?pretty = true
{
   "query":{
      "match_all":{}
   }
}
```

**响应**

……………………..

```
{
   "_index" : "schools", "_type" : "school", "_id" : "1", "_score" : 1.0,
   "_source":{
      "name":"Central School", "description":"CBSE Affiliation", 
      "street":"Nagan", "city":"paprola", "state":"HP", "zip":"176115",
      "location": [31.8955385, 76.8380405], "fees":2000, 
      "tags":["Senior Secondary", "beautiful campus"], "rating":"3.5"
   }
}
```

………………….

**人类可读的输出**

 此选项可以将响应更改为人类可读的形式(如果human = true)或计算机可读的形式(如果human = false)。例如，如果human = true，那么 distance_kilometer = 20KM，如果human = false，那么distance_meter = 20000。

**响应信息过滤**

 我们可以通过添加到field_path参数中来过滤响应信息，例如。

```
POST  /schools/_search?filter_path = hits.total
{
   "query":{
      "match_all":{}
   }
}
```

响应

```
{"hits":{"total":3}}
```