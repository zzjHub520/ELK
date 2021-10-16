## ElasticSearch 7 文档API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/aclm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=1411069182&amp;s2=2602132018&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=1a4c80db8a7c612c&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634315260613&amp;ti=ElasticSearch%207%20%E6%96%87%E6%A1%A3API%20%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x5023&amp;cfv=0&amp;cpl=16&amp;chi=5&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-document-apis.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-conventions.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634315261&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634315260599.49.49.50" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 ElasticSearch提供单文档和多文档API，其中API调用分别针对单文档和多文档。

**索引API**

 当使用特定映射对相应的索引发出请求时，它有助于在索引中添加或更新JSON文档。例如，下面的请求将把JSON对象添加到schools索引和school映射下。

```
POST /schools/school/4
{
   "name":"City School", "description":"ICSE", "street":"West End", "city":"Meerut", 
   "state":"UP", "zip":"250002", "location":[28.9926174, 77.692485], "fees":3500, 
   "tags":["fully computerized"], "rating":"4.5"
}
```

**响应**

```
{
   "_index":"schools", "_type":"school", "_id":"4", "_version":1,
   "_shards":{"total":2, "successful":1,"failed":0}, "created":true
}
```

**自动创建索引**

 当请求将JSON对象添加到特定的索引时，如果该索引不存在，那么该应用编程接口会自动创建该索引以及该JSON对象的基础映射。可以通过将elasticsearch.yml文件中的下列参数值更改为false来禁用此功能。

```
action.auto_create_index:false
index.mapper.dynamic:false
```

 您还可以限制索引的自动创建，其中通过更改以下参数的值，只允许具有特定模式的索引名称

```
action.auto_create_index:+acc*,-bank*
```

(其中+表示允许，而-表示不允许)

**版本控制**

 ElasticSearch还提供版本控制功能。我们可以使用版本查询参数来指定文档的特定版本。例如，

```
POST /schools/school/1?version = 1
{
   "name":"Central School", "description":"CBSE Affiliation", "street":"Nagan",
   "city":"paprola", "state":"HP", "zip":"176115", "location":[31.8955385, 76.8380405],
   "fees":2200, "tags":["Senior Secondary", "beautiful campus"], "rating":"3.3"
}
```

**响应**

```
{
   "_index":"schools", "_type":"school", "_id":"1", "_version":2,
   "_shards":{"total":2, "successful":1,"failed":0}, "created":false
}
```

 有两种最重要的版本控制类型；内部版本控制是默认版本，从1开始，每次更新时递增，包括删除。版本号可以在外部设置。要启用此功能，我们需要将version_type设置为外部。

 版本控制是一个实时过程，不受实时搜索操作的影响。

**操作类型**

 操作类型用于强制创建操作，这有助于避免覆盖现有文档。

```
POST /tutorials/chapter/1?op_type = create
{
   "Text":"this is chapter one"
}
```

**响应**

```
{
   "_index":"tutorials", "_type":"chapter", "_id":"1", "_version":1,
   "_shards":{"total":2, "successful":1, "failed":0}, "created":true
}
```

**自动生成ID**

 当索引操作中未指定ID时，ElasticSearch会自动为该文档生成ID。

**父子关系**

 您可以通过在URL查询参数中传递父文档的标识来定义任何文档的父文档。

```
POST /tutorials/article/1?parent = 1
{
   "Text":"This is article 1 of chapter 1"
}
```

 注意：如果在执行本示例时出现异常，请通过在索引中添加以下内容来重新创建索引。

```
{
   "mappings": {
      "chapter": {},
      "article": {
         "_parent": {
            "type": "chapter"
         }
      }
   }
}
```

**超时**

 默认情况下，索引操作将在主分片上等待最多1分钟，超时后提示失败并响应错误。通过将值传递给timeout参数，可以显式更改此超时值。

```
POST /tutorials/chapter/2?timeout = 3m
{
   "Text":"This is chapter 2 waiting for primary shard for 3 minutes"
}
```

**Get API**

 API通过对特定文档执行get请求来帮助获取JSON对象类型。例如：

```
GET /schools/school/1
{
   "_index":"schools", "_type":"school", "_id":"1", "_version":2,
   "found":true, "_source":{
      "name":"Central School", "description":"CBSE Affiliation", 
      "street":"Nagan", "city":"paprola", "state":"HP", "zip":"176115",
      "location":[31.8955385,76.8380405], "fees":2200, 
      "tags":["Senior Secondary", "beautiful campus"], "rating":"3.3"
   }
}
```

 此操作是实时的，不受索引刷新率的影响。

 您也可以指定版本，然后ElasticSearch将只获取该版本的文档。

 您还可以在请求中指定_all，以便ElasticSearch可以在每种类型中搜索该文档id，并返回第一个匹配的文档。

 您也可以从特定文档的结果中指定所需的字段。

```
GET /schools/school/1?fields = name,fees
```

**响应**

……………………..

```
"fields":{
   "name":["Central School"], "fees":[2200]
}
```

……………………..

 您也可以通过在get请求中添加_source部分来获取结果中的源部分。

```
GET /schools/school/1/_source
```

**响应**

```
{
   "name":"Central School", "description":"CBSE Afiliation", "street":"Nagan",
   "city":"paprola", "state":"HP", "zip":"176115", "location":[31.8955385, 76.8380405],
   "fees":2200, "tags":["Senior Secondary", "beatiful campus"], "rating":"3.3"
}
```

 您也可以在执行get操作之前通过将refresh参数设置为true来刷新碎片。

**Delete API**

 您可以通过向ElasticSearch发送HTTP DELETE请求来删除特定的索引、映射或文档。例如，

```
DELETE /schools/school/4
{
   "found":true, "_index":"schools", "_type":"school", "_id":"4", "_version":2,
   "_shards":{"total":2, "successful":1, "failed":0}
}
```

 可以指定文档的version来删除该特定版本。

 可以指定路由参数从特定用户删除文档，如果文档不属于该特定用户，操作将失败。

 在此操作中，您可以像获取应用程序接口一样指定refresh和timeout选项。

**Update API**

 用于更新文档

```
POST  /schools_gov/school/1/_update
{
   "script":{
      "inline": "ctx._source.fees+ = inc", "params":{
         "inc": 500
      }
   }
}
```

**响应**

```
{
   "_index":"schools_gov", "_type":"school", "_id":"1", "_version":2,
   "_shards":{"total":2, "successful":1, "failed":0}
}
```

 注意：如果出现脚本异常，建议在elastcisearch.yml中添加以下行

```
script.inline: on
script.indexed: on
```

 您可以通过向更新的文档发送get请求来检查更新。

```
GET /schools_gov/school/1
```

**Multi Get API**

 它拥有与GET API相同的功能，但是这个GET请求可以返回多个文档。我们使用文档数组来指定需要获取的所有文档的索引和id。

```
POST /_mget
{
   "docs":[
      {
         "_index": "schools", "_id": "1"
      },
      {
         "_index":"schools_gev", "_id": "2"
      }
   ]
}
```

**响应**

```
{
   "docs":[
      {
         "_index":"schools", "_type":"school", "_id":"1",
         "_version":1, "found":true, "_source":{
            "name":"Central School", "description":"CBSE Afiliation",
            "street":"Nagan", "city":"paprola", "state":"HP", "zip":"176115",
            "location":[31.8955385,76.8380405], "fees":2000, 
            "tags":["Senior Secondary", "beatiful campus"], "rating":"3.5"
         }
      },
      {
         "_index":"schools_gev", "_type":"school", "_id":"2", "error":{
            "root_cause":[{
               "type":"index_not_found_exception", "reason":"no such index", 
               "index":"schools_gev"
            }],
            "type":"index_not_found_exception", "reason":"no such index", 
            "index":"schools_gev"
         }
      }
   ]
}
```

**Bulk API**

 该API用于通过在一个请求中进行多个索引/删除操作来批量上传或删除JSON对象。我们需要添加“_bulk”关键字来调用这个应用程序接口。这个API的例子已经在填充ElasticSearch文章中执行过了。所有其他功能与GET API相同。