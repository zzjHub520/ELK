# Elasticsearch填充

------

在本节中，我们将向**Elasticsearch**添加一些索引，映射和数据。此数据将用于本教程中解释的示例中。

## 创建索引

```c
POST http://localhost:9200/schools
```

**请求正文**
它可以包含索引特定的设置，但是现在，它的默认设置为空。

**响应**

```json
{"acknowledged": true}
```

这意味着创建索引成功

### 创建映射和添加数据

Elasticsearch将根据请求体中提供的数据自动创建映射，我们将使用其批量功能在此索引中添加多个JSON对象。

```
POST http://localhost:9200/schools/_bulk
```

### 请求体

```json
{
   "index":{
      "_index":"schools", "_type":"school", "_id":"1"
   }
}
{
   "name":"Central School", "description":"CBSE Affiliation", "street":"Nagan",
   "city":"paprola", "state":"HP", "zip":"176115", "location":[31.8955385, 76.8380405],
   "fees":2000, "tags":["Senior Secondary", "beautiful campus"], "rating":"3.5"
}
{
   "index":{
      "_index":"schools", "_type":"school", "_id":"2"
   }
}
{
   "name":"Saint Paul School", "description":"ICSE 
   Afiliation", "street":"Dawarka", "city":"Delhi", "state":"Delhi", "zip":"110075",
   "location":[28.5733056, 77.0122136], "fees":5000,
   "tags":["Good Faculty", "Great Sports"], "rating":"4.5"
}
{
   "index":{"_index":"schools", "_type":"school", "_id":"3"}
}
{
   "name":"Crescent School", "description":"State Board Affiliation", "street":"Tonk Road", 
   "city":"Jaipur", "state":"RJ", "zip":"176114","location":[26.8535922, 75.7923988],
   "fees":2500, "tags":["Well equipped labs"], "rating":"4.5"
}
```

响应结果 - 

```json
{
   "took":328, "errors":false,"items":[
      {
         "index":{
            "_index":"schools", "_type":"school", "_id":"1", "_version":1, "_shards":{
               "total":2, "successful":1, "failed":0
            }, "status":201
         }
      },

      {
         "index":{
            "_index":"schools", "_type":"school", "_id":"2", "_version":1, "_shards":{
               "total":2, "successful":1, "failed":0
            }, "status":201
         }
      },

      {
         "index":{
            "_index":"schools", "_type":"school", "_id":"3", "_version":1, "_shards":{
               "total":2, "successful":1, "failed":0
            }, "status":201
         }
      }
   ]
}
```

### 添加另一个索引

**创建索引**

```
POST http://localhost:9200/schools_gov
```

**请求正文**

它可以包含索引特定的设置，但现在它的默认设置为空。

**响应**

```
{"acknowledged": true} (This means index is created)
```

### 创建映射和添加数据

```
POST http://localhost:9200/schools_gov/_bulk
```

**请求正文**

```json
{
   "index":{
      "_index":"schools_gov", "_type":"school", "_id":"1"
   }
}
{
   "name":"Model School", "description":"CBSE Affiliation", "street":"silk city",
   "city":"Hyderabad", "state":"AP", "zip":"500030", "location":[17.3903703, 78.4752129],
   "fees":200, "tags":["Senior Secondary", "beautiful campus"], "rating":"3"
}
{
   "index":{
      "_index":"schools_gov", "_type":"school", "_id":"2"
   }
}
{
   "name":"Government School", "description":"State Board Affiliation",
   "street":"Hinjewadi", "city":"Pune", "state":"MH", "zip":"411057",
   "location": [18.599752, 73.6821995], "fees":500, "tags":["Great Sports"], "rating":"4"
}
```

**响应**

```json
{
   "took":179, "errors":false, "items":[
      {
        "index":{
           "_index":"schools_gov", "_type":"school", "_id":"1", "_version":1, "_shards":{
              "total":2, "successful":1, "failed":0
            }, "status":201
         }
      },

      {
         "index":{
            "_index":"schools_gov", "_type":"school", "_id":"2", "_version":1, "_shards":{
               "total":2, "successful":1, "failed":0
            }, "status":201
         }
      }
   ]
}
```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-populate.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处