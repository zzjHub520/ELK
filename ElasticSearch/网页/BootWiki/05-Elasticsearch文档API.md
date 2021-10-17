# Elasticsearch文档API

------

**Elasticsearch**提供单文档API和多文档API，其中API调用分别针对单个文档和多个文档。

## 索引API

当使用特定映射对相应索引发出请求时，它有助于在索引中添加或更新JSON文档。 例如，以下请求将JSON对象添加到索引学校和学校映射下。

```
POST http://localhost:9200/schools/school/4
```

**请求正文**

```json
{
   "name":"City School", "description":"ICSE", "street":"West End", "city":"Meerut", 
   "state":"UP", "zip":"250002", "location":[28.9926174, 77.692485], "fees":3500, 
   "tags":["fully computerized"], "rating":"4.5"
}
```

**响应**

```json
{
   "_index":"schools", "_type":"school", "_id":"4", "_version":1,
   "_shards":{"total":2, "successful":1,"failed":0}, "created":true
}
```

## 自动索引创建

当请求将JSON对象添加到特定索引时，如果该索引不存在，那么此API会自动创建该索引以及该特定JSON对象的基础映射。 可以通过将以下参数的值更改为`false`来禁用此功能，这个值是存在于`elasticsearch.yml`文件中，打开`elasticsearch.yml`文件设置如下 。

```
action.auto_create_index:false
index.mapper.dynamic:false
```

还可以限制自动创建索引，其中通过更改以下参数的值只允许指定模式的索引名称 -

```
action.auto_create_index:+acc*,-bank*
```

(其中`+`表示允许， `-` 表示不允许)

## 版本控制

Elasticsearch还提供版本控制功能。我们可以使用版本查询参数指定特定文档的版本。 例如，

```
POST http://localhost:9200/schools/school/1?version = 1
```

**请求正文**

```json
{
   "name":"Central School", "description":"CBSE Affiliation", "street":"Nagan",
   "city":"paprola", "state":"HP", "zip":"176115", "location":[31.8955385, 76.8380405],
   "fees":2200, "tags":["Senior Secondary", "beautiful campus"], "rating":"3.3"
}
```

**响应内容**

```json
{
   "_index":"schools", "_type":"school", "_id":"1", "_version":2,
   "_shards":{"total":2, "successful":1,"failed":0}, "created":false
}
```

有两种最重要的版本控制类型： 内部版本控制是以`1`开头的默认版本，每次更新都会增加，包括删除。版本号可以在外部设置。要启用此功能，我们需要将`version_type`设置为`external`。

版本控制是一个实时过程，它不受实时搜索操作的影响。

## 操作类型

操作类型用于强制创建操作，这有助于避免覆盖现有文档。

```
POST http://localhost:9200/tutorials/chapter/1?op_type = create
```

**请求正文**

```json
{
   "Text":"this is chapter one"
}
```

**响应内容**

```json
{
   "_index":"tutorials", "_type":"chapter", "_id":"1", "_version":1,
   "_shards":{"total":2, "successful":1, "failed":0}, "created":true
}
```

### 自动生成ID

当在索引操作中未指定`ID`时，Elasticsearch自动为文档生成`ID`。

### 父级和子级

可以通过在父URL查询参数中传递父文档的ID来定义任何文档的父级。

```
POST http://localhost:9200/tutorials/article/1?parent = 1
```

**请求正文**

```json
{
   "Text":"This is article 1 of chapter 1"
}
```

> 注意 - 如果在执行此示例时遇到异常，请通过在索引中添加以下内容来重新创建索引。
>
> ```json
> {
>    "mappings": {
>       "chapter": {},
>       "article": {
>          "_parent": {
>             "type": "chapter"
>          }
>       }
>    }
> }
> ```

### 超时

默认情况下，索引操作将在主分片上最多等待1分钟，超过后就会失败并响应错误。 可以通过将值传递给`timeout`参数来显式更改这个超时值。

```
POST http://localhost:9200/tutorials/chapter/2?timeout = 3m
```

**请求正文**

```json
{
   "Text":"This is chapter 2 waiting for primary shard for 3 minutes"
}
```

### 获取API

API通过对特定文档执行`get`请求来帮助提取JSON对象。 例如，

```
GET http://localhost:9200/schools/school/1
```

**响应**

```
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

- 这个操作是实时的，不受索引刷新率的影响。
- 还可以指定版本，然后**Elasticsearch**将仅提取该版本的文档。 
- 还可以在请求中指定`_all`，以便**Elasticsearch**可以在每种类型中搜索该文档`ID`，并且它将返回第一个匹配的文档。
- 还可以从该特定文档的结果中指定所需的字段。

```
GET http://localhost:9200/schools/school/1?fields = name,fees
```

**响应**

```json
……………………..
"fields":{
   "name":["Central School"], "fees":[2200]
}
……………………..
```

还可以通过在`get`请求中添加`_source`字段来获取结果中的源部分。

```
GET http://localhost:9200/schools/school/1/_source
```

**响应**

```json
{
   "name":"Central School", "description":"CBSE Afiliation", "street":"Nagan",
   "city":"paprola", "state":"HP", "zip":"176115", "location":[31.8955385, 76.8380405],
   "fees":2200, "tags":["Senior Secondary", "beatiful campus"], "rating":"3.3"
}
```

还可以在通过将 `refresh` 参数设置为`true`进行`get`操作之前刷新碎片。

### 删除API

可以通过向**Elasticsearch**发送`HTTP DELETE`请求来删除指定的索引，映射或文档。 例如，

```
DELETE http://localhost:9200/schools/school/4
```

**响应**

```json
{
   "found":true, "_index":"schools", "_type":"school", "_id":"4", "_version":2,
   "_shards":{"total":2, "successful":1, "failed":0}
}
```

- 可以指定文档的版本以删除指定的版本。
- 可以指定路由参数以删除指定用户的文档，如果文档不属于该特定用户，则操作将失败。
- 在此操作中，可以像GET API那样指定刷新(`refresh`)和超时(`timeout`)选项。

### 更新API

脚本用于执行此操作，版本控制用于确保在获取和重建索引期间没有发生更新。 例如，使用下面脚本更新学校的费用 -

```
POST http://localhost:9200/schools_gov/school/1/_update
```

**请求正文**

```json
{
   "script":{
      "inline": "ctx._source.fees+ = inc", "params":{
         "inc": 500
      }
   }
}
```

**响应结果**

```json
{
   "_index":"schools_gov", "_type":"school", "_id":"1", "_version":2,
   "_shards":{"total":2, "successful":1, "failed":0}
}
```

> 注意 - 如果获取脚本异常，建议在`elastcisearch.yml`中添加以下行

```
script.inline: on
script.indexed: on
```

可以通过向更新的文档发送获取请求来检查更新。

```
GET http://localhost:9200/schools_gov/school/1
```

### 多获取API

它具有相同的功能，如`GET API`，但此`get`请求可以返回多个文档。使用`doc`数组来指定需要提取的所有文档的索引，类型和ID。

```
POST http://localhost:9200/_mget
```

**请求正文**

```
{
   "docs":[
      {
         "_index": "schools", "_type": "school", "_id": "1"
      },

      {
         "_index":"schools_gev", "_type":"school", "_id": "2"
      }
   ]
}
```

**响应结果**

```json
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

### 批量API

此API用于通过在单个请求中进行多个索引/删除操作来批量上传或删除JSON对象。 需要添加“`_bulk`”关键字来调用此API。此API的示例已在[Elasticsearch填充文章](http://www.yiibai.com/elasticsearch/elasticsearch_populate.html)中执行。所有其他功能与GET API相同。

```json

```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-document-apis.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处