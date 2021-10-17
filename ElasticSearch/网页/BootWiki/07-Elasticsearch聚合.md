# Elasticsearch聚合

------

框架集合由搜索查询选择的所有数据。框架中包含许多构建块，有助于构建复杂的数据描述或摘要。聚合的基本结构如下所示 -

```json
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

有以下不同类型的聚合，每个都有自己的目的 -

### 指标聚合

这些聚合有助于从聚合文档的字段值计算矩阵，并且某些值可以从脚本生成。
数字矩阵或者是平均聚合的单值，或者是像`stats`一样的多值。

### 平均聚合

此聚合用于获取聚合文档中存在的任何数字字段的平均值。 例如，

```
POST http://localhost:9200/schools/_search
```

**请求正文**

```json
{
   "aggs":{
      "avg_fees":{"avg":{"field":"fees"}}
   }
}
```

**响应**

```json
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

如果该值不存在于一个或多个聚合文档中，则默认情况下将忽略该值。您可以在聚合中添加缺少的字段，将缺少值视为默认值。

```json
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

### 基数聚合

此聚合给出特定字段的不同值的计数。 例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```
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

> 注 - 基数的值为`3`，因为名称 - Government, School 和 Model中有三个不同的值。

### 扩展统计聚合

此聚合生成聚合文档中特定数字字段的所有统计信息。 例如，

```
POST http://localhost:9200/schools/school/_search
```

**请求正文**

```json
{
   "aggs" : {
      "fees_stats" : { "extended_stats" : { "field" : "fees" } }
   }
}
```

**响应**

```json
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
```

### 最大聚合

此聚合查找聚合文档中特定数字字段的最大值。 例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "aggs" : {
      "max_fees" : { "max" : { "field" : "fees" } }
   }
}
```

**响应**

```json
{
   aggregations":{"max_fees":{"value":5000.0}}
}
```

### 最小聚合

此聚合查找聚合文档中特定数字字段的最小值。 例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "aggs" : {
      "min_fees" : { "min" : { "field" : "fees" } }
   }
}
```

**响应**

```json
"aggregations":{"min_fees":{"value":500.0}}
```

### 总和聚合

此聚合计算聚合文档中特定数字字段的总和。 例如，

```
POST http://localhost:9200/schools*/_search
```

**请求正文**

```json
{
   "aggs" : {
      "total_fees" : { "sum" : { "field" : "fees" } }
   }
}
```

**响应**

```json
"aggregations":{"total_fees":{"value":10900.0}}
```

在特殊情况下使用的一些其他度量聚合，例如地理边界聚集和用于地理位置的地理中心聚集。

### 桶聚合

这些聚合包含用于具有标准的不同类型的桶聚合，该标准确定文档是否属于某一个桶。桶聚合已经在下面描述 -

**子聚集**

此存储桶聚合会生成映射到父存储桶的文档集合。类型参数用于定义父索引。 例如，我们有一个品牌及其不同的模型，然后模型类型将有以下`_parent`字段 -

```json
{
   "model" : {
      "_parent" : {
         "type" : "brand"
      }
   }
}
```

还有许多其他特殊的桶聚合，这在许多其他情况下是有用的，它们分别是 -

- 日期直方图汇总/聚合
- 日期范围汇总/聚合
- 过滤聚合
- 过滤器聚合
- 地理距离聚合
- GeoHash网格聚合
- 全局汇总
- 直方图聚合
- IPv4范围聚合
- 失踪聚合
- 嵌套聚合
- 范围聚合
- 反向嵌套聚合
- 采样器聚合
- 重要条款聚合
- 术语聚合

### 聚合元数据

可以通过使用元标记在请求时添加关于聚合的一些数据，并可以获得响应。 例如，

```
POST http://localhost:9200/school*/report/_search
```

**请求正文**

```json
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

```json
{
   "aggregations":{"min_fees":{"meta":{"dsc":"Lowest Fees"}, "value":2180.0}}
}
```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-aggregations.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处