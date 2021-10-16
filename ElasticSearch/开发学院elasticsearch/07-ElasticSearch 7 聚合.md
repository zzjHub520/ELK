## ElasticSearch 7 聚合

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/ecym?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=3076488856&amp;s2=2211737519&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=d5ddd4be3647d277&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345243435&amp;ti=ElasticSearch%207%20%E8%81%9A%E5%90%88%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80%E6%96%B0%E7%89%88%E6%95%99&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x5333&amp;cfv=0&amp;cpl=16&amp;chi=7&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-aggregations.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-search-apis.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345243&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345243414.68.68.68" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 该框架收集搜索查询选择的所有数据,该框架由许多构建块组成，有助于构建复杂的数据摘要,聚合的基本结构如下所示

```
"aggregations" : {
   "<aggregation_name>" : {
      "<aggregation_type>" : {
         <aggregation_body>
      }
      [,"meta" : { [<meta_data_body>] } ]?
      [,"aggregations" : { [<sub_aggregation>]+ } ]?
   }
}
```

 有不同类型的聚合，每种聚合都有自己的目的

**度量聚合**

 这些聚合有助于根据聚合文档的字段值计算矩阵，有时一些值可以从脚本中生成。

 数值矩阵可以像平均聚合一样是单值的，也可以像统计一样是多值的。

**平均聚合**

 此聚合用于获取聚合文档中任何数字字段的平均值。例如，

```
POST  /schools/_search
{
   "aggs":{
      "avg_fees":{"avg":{"field":"fees"}}
   }
}
```

**响应**

```
{
   "took":44, "timed_out":false, "_shards":{"total":5, "successful":5, "failed":0},
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
            "_index":"schools", "_type":"school", "_id":"1", "_score":1.0,
            "_source":{
               "name":"Central School", "description":"CBSE Affiliation",
               "street":"Nagan", "city":"paprola", "state":"HP", "zip":"176115",
               "location":[31.8955385, 76.8380405], "fees":2200, 
               "tags":["Senior Secondary", "beautiful campus"], "rating":"3.3"
            }
         },
         {
            "_index":"schools", "_type":"school", "_id":"3", "_score":1.0,
            "_source":{
               "name":"Crescent School", "description":"State Board Affiliation",
               "street":"Tonk Road", "city":"Jaipur", "state":"RJ", 
               "zip":"176114", "location":[26.8535922, 75.7923988], "fees":2500, 
               "tags":["Well equipped labs"], "rating":"4.5"
            }
         }
      ]
   }, "aggregations":{"avg_fees":{"value":3233.3333333333335}}
}
```

 如果该值不在一个或多个聚合文档中，默认情况下会被忽略。您可以在聚合中添加缺失字段，将缺失值视为默认值。

```
{
   "aggs":{
      "avg_fees":{
         "avg":{
            "field":"fees"
            "missing":0
         }
      }
   }
}
```

**基数聚合**

 这种聚合给出了特定字段的不同值的计数。例如，

```
POST /schools*/_search
{
   "aggs":{
      "distinct_name_count":{"cardinality":{"field":"name"}}
   }
}
```

**响应**

```
………………………………………………
{
   "name":"Government School", "description":"State Board Afiliation",
   "street":"Hinjewadi", "city":"Pune", "state":"MH", "zip":"411057",
   "location":[18.599752, 73.6821995], "fees":500, "tags":["Great Sports"], 
   "rating":"4"
},

{
   "_index":"schools_gov", "_type": "school", "_id":"1", "_score":1.0,
   "_source":{
      "name":"Model School", "description":"CBSE Affiliation", "street":"silk city",
      "city":"Hyderabad", "state":"AP", "zip":"500030", 
      "location":[17.3903703, 78.4752129], "fees":700, 
      "tags":["Senior Secondary", "beautiful campus"], "rating":"3"
   }
}, "aggregations":{"disticnt_name_count":{"value":3}}
………………………………………………
```

 注意：基数的值是3，因为name中有三个不同的值：Government, School和Model。

**扩展统计信息聚合**

 这种聚合会生成聚合文档中特定数值字段的所有统计信息。例如，

```
POST  /schools/_search
{
   "aggs" : {
      "fees_stats" : { "extended_stats" : { "field" : "fees" } }
   }
}
```

**响应**

```
………………………………………………
{
   "aggregations":{
      "fees_stats":{
         "count":3, "min":2200.0, "max":5000.0, 
         "avg":3233.3333333333335, "sum":9700.0,
         "sum_of_squares":3.609E7, "variance":1575555.555555556, 
         "std_deviation":1255.2113589175156,
         "std_deviation_bounds":{
            "upper":5743.756051168364, "lower":722.9106154983024
         }
      }
   }
}
………………………………………………
```

**最大聚合**

 此聚合会查找聚合文档中特定数值字段的最大值。例如，

```
POST /schools*/_search
{
   "aggs" : {
      "max_fees" : { "max" : { "field" : "fees" } }
   }
}
```

**响应**

```
………………………………………………
{
   aggregations":{"max_fees":{"value":5000.0}}
}
………………………………………………
```

**最小聚合**

 此聚合会查找聚合文档中特定数值字段的最小值。例如，

```
POST /schools*/_search
{
   "aggs" : {
      "min_fees" : { "min" : { "field" : "fees" } }
   }
}
```

**响应**

```
………………………………………………
"aggregations":{"min_fees":{"value":500.0}}
………………………………………………
```

**总和聚合**

 此聚合计算聚合文档中特定数字字段的和。例如，

```
POST /schools*/_search
{
   "aggs" : {
      "total_fees" : { "sum" : { "field" : "fees" } }
   }
}
```

**响应**

```
………………………………………………
"aggregations":{"total_fees":{"value":10900.0}}
………………………………………………
```

 还有一些其他度量聚合在特殊情况下使用，如地理边界聚合和地理质心聚合，用于地理定位。

**Bucket聚合**

 这些聚合包含许多用于不同类型聚合的bucket，这些聚合有一个标准，该标准决定文档是否属于该bucket。Bucket聚合描述如下：

子聚集

 此Bucket聚合生成一组文档，这些文档映射到父Bucket聚合。类型参数用于定义父索引。例如，我们有一个品牌及其不同的模型，然后模型类型将具有以下_parent字段

```
{
   "model" : {
      "_parent" : {
         "type" : "brand"
      }
   }
}
```

 还有许多其他特殊的bucket聚合，这些聚合在许多其他情况下很有用，它们是

- Date Histogram Aggregation
- Date Range Aggregation
- Filter Aggregation
- Filters Aggregation
- Geo Distance Aggregation
- GeoHash grid Aggregation
- Global Aggregation
- Histogram Aggregation
- IPv4 Range Aggregation
- Missing Aggregation
- Nested Aggregation
- Range Aggregation
- Reverse nested Aggregation
- Sampler Aggregation
- Significant Terms Aggregation
- Terms Aggregation

**聚合元数据**

 您可以使用元标签在请求时添加一些关于聚合的数据，并可以得到响应。例如，

```
POST /school*/_search
{
   "aggs" : {
      "min_fees" : { "avg" : { "field" : "fees" } ,
         "meta" :{
            "dsc" :"Lowest Fees"
         }
      }
   }
}
```

**响应**

```
………………………………………………
{
   "aggregations":{"min_fees":{"meta":{"dsc":"Lowest Fees"}, "value":2180.0}}
}
………………………………………………
```