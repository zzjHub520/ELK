## ElasticSearch 7 映射

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/tcim?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=2307013324&amp;s2=2954587570&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=605abd4354d42429&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345519855&amp;ti=ElasticSearch%207%20%E6%98%A0%E5%B0%84%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80%E6%96%B0%E7%89%88%E6%95%99&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x3008&amp;cfv=0&amp;cpl=16&amp;chi=11&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-mapping.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-query-dsl.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345520&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345519840.54.54.54" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 映射是存储在索引中的文档的描述，它定义了数据类型，如geo_point或string，以及文档和规则中字段的格式，以控制动态添加字段的映射。例如，

```
POST /bankaccountdetails
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

响应

```
{"acknowledged":true}
```

**字段数据类型**

 ElasticSearch支持文档中字段的多种不同数据类型。以下数据类型用于在ElasticSearch中存储字段

核心数据类型

 这些是几乎所有系统都支持的基本数据类型，如integer, long, double, short, byte, double, float, string, date, Boolean和binary。

复杂数据类型

 这些数据类型是核心数据类型的组合。像数组、JSON对象和嵌套数据类型。以下是嵌套数据类型的示例

```
POST /tabletennis/team/1
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

响应

```
{
   "_index":"tabletennis", "_type":"team", "_id":"1", "_version":1, 
   "_shards":{"total":2, "successful":1, "failed":0}, "created":true
}
```

Geo数据类型

 这些数据类型用于定义地理属性。例如，地理点用于定义经度和纬度，地理形状用于定义不同的几何形状，如矩形。

专用数据类型

 这些数据类型用于特殊目的，如 IPv4 (“ip”)接受IP地址，完成数据类型用于支持自动完成建议和用于计算字符串中令牌数量的令牌计数。

**映射类型**

 每个索引都有一个或多个映射类型，用于将索引的文档分成逻辑组。基于以下参数，映射可以彼此不同

元字段

 这些字段提供关于映射和与其相关联的其他对象的信息。如_index、_type、_id和_source字段。

字段

 不同的映射包含不同数量的字段和不同数据类型的字段。

**动态映射**

 ElasticSearch为自动创建映射提供了一种用户友好的机制。用户可以将数据直接发布到任何未定义的映射中，ElasticSearch将自动创建映射，这称为动态映射。例如，

```
POST /accountdetails/tansferreport
{
   "from_acc":"7056443341", "to_acc":"7032460534",
   "date":"11/1/2016", "amount":10000
}
```

响应

```
{
   "_index":"accountdetails", "_type":"tansferreport",
   "_id":"AVI3FeH0icjGpNBI4ake", "_version":1,
   "_shards":{"total":2, "successful":1, "failed":0},
   "created":true
}
```

**映射参数**

 映射参数定义了映射的结构、关于字段和存储的信息以及在搜索时如何分析映射的数据。这些是以下映射参数

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