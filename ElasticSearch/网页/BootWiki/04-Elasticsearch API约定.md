# Elasticsearch API约定

------

**web**中的应用编程接口(API)是一组函数调用或其他编程指令以访问该特定web应用中的软件组件。 例如，**Facebook API**帮助开发者通过从Facebook访问数据或其他功能来创建应用程序; 它可以是出生日期或状态更新。

**Elasticsearch**提供了一个REST API，通过HTTP通过JSON访问。 Elasticsearch使用以下约定 -

## 多索引

API中的大多数操作(主要是搜索和其他操作)用于一个或多个索引。 这有助于用户通过只执行一次查询来搜索多个位置或所有可用数据。 许多不同的符号用于在多个索引中执行操作。 我们将在本节讨论其中的一些。

### 逗号分隔符号

```
POST http://localhost:9200/index1,index2,index3/_search
```

**请求正文**

```json
{
   "query":{
      "query_string":{
         "query":"any_string"
      }
   }
}
```

**响应**

来自`index1`，`index2`，`index3`的JSON对象，其中包含`any_string`。

### 所有索引的_all关键字

```
POST http://localhost:9200/_all/_search
```

**请求正文**

```json
{
   "query":{
      "query_string":{
         "query":"any_string"
      }
   }
}
```

### 响应

来自所有索引的JSON对象，并且有`any_string`。

## 通配符(*，+， - )

```
POST http://localhost:9200/school*/_search
```

**请求正文**

```json
{
   "query":{
      "query_string":{
         "query":"CBSE"
      }
   }
}
```

**响应**
来自所有索引的JSON对象，从`school` 开始，有CBSE。

或者，也可以使用以下代码 -

```
POST http://localhost:9200/school*,-schools_gov /_search
```

**请求正文**

```json
{
   "query":{
      "query_string":{
         "query":"CBSE"
      }
   }
}
```

### 响应

来自所有索引的JSON对象，它们以“`school`”开头，但不是`schools_gov`并且在其中有CBSE。
还有一些URL查询字符串参数 -

- ```
  ignore_unavailable
  ```

   \- 如果URL中存在的一个或多个索引不存在，则不会发生错误或操作不会停止。 例如，

  ```
  schools
  ```

   索引存在，但

  ```
  book_shops
  ```

  不存在 -

  ```
  POST http://localhost:9200/school*,book_shops/_search
  ```

**请求正文**

```json
{
   "query":{
      "query_string":{
         "query":"CBSE"
      }
   }
}
```

**响应**

```json
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

看看下面的代码 -

```
POST http://localhost:9200/school*,book_shops/_search?ignore_unavailable = true
```

**请求正文**

```json
{
   "query":{
      "query_string":{
         "query":"CBSE"
      }
   }
}
```

**响应(无错误)**
来自所有索引的JSON对象，从 `school` 开始，有`CBSE`。

**allow_no_indices**

如果带有通配符的网址没有索引，这个参数是`true`值时将防止错误。

例如，不是以`schools_pri`开头的索引 -

```
POST
http://localhost:9200/schools_pri*/_search?allow_no_indices = true
```

**请求正文**

```json
{
   "query":{
      "match_all":{}
   }
}
```

**响应(无错误)**

```json
{
   "took":1,"timed_out": false, "_shards":{"total":0, "successful":0, "failed":0}, 
   "hits":{"total":0, "max_score":0.0, "hits":[]}
}
```

**expand_wildcards**

此参数确定通配符是否需要扩展为打开索引或闭合索引或两者。 此参数的值可以是打开和关闭或无和全部。

例如，关闭索引`schools` -

```
POST http://localhost:9200/schools/_close
```

**响应**

```json
{"acknowledged":true}
```

看看下面的代码 -

```
POST http://localhost:9200/school*/_search?expand_wildcards = closed
```

**请求正文**

```json
{
   "query":{
      "match_all":{}
   }
}
```

**响应**

```json
{
   "error":{
      "root_cause":[{
         "type":"index_closed_exception", "reason":"closed", "index":"schools"
      }],

      "type":"index_closed_exception", "reason":"closed", "index":"schools"
   }, "status":403
}
```

## 日期索引名称中的数学支持

**Elasticsearch**提供了根据日期和时间搜索索引的功能。我们需要以特定格式指定日期和时间。 例如，`accountdetail-2015.12.30`，索引将存储2015年12月30日的银行帐户详细信息。可以执行数学操作以获取特定日期或日期和时间范围的详细信息。

日期数字索引名称的格式 -

```
<static_name{date_math_expr{date_format|time_zone}}>
http://localhost:9200/<accountdetail-{now-2d{YYYY.MM.dd|utc}}>/_search
```

`static_name`是表达式的一部分，在每个日期数学索引(如帐户详细信息)中保持相同。 date_math_expr包含动态确定日期和时间的数学表达式，如`now-2d`。`date_format`包含日期在索引中写入的格式，如`YYYY.MM.dd`。 如果今天的日期是2015年12月30日，则`<accountdetail- {now-2d {YYYY.MM.dd}}>`将返回`accountdetail-2015.12.28`。

| 表达式                           | 解析为                   |
| -------------------------------- | ------------------------ |
| `<accountdetail-{now-d}>`        | accountdetail-2016.12.29 |
| `<accountdetail-{now-M}>`        | accountdetail-2015.11.30 |
| `<accountdetail-{now{YYYY.MM}}>` | accountdetail-2015.12    |

现在将看到`Elasticsearch`中可用于获取指定格式的响应的一些常见选项。

### 美化结果

可以通过附加一个网址查询参数(即`pretty = true`)，获得格式正确的JSON对象的响应。

```
POST http://localhost:9200/schools/_search?pretty = true
```

**请求正文**

```json
{
   "query":{
      "match_all":{}
   }
}
```

**响应**

```json
……………………..
{
   "_index" : "schools", "_type" : "school", "_id" : "1", "_score" : 1.0,
   "_source":{
      "name":"Central School", "description":"CBSE Affiliation", 
      "street":"Nagan", "city":"paprola", "state":"HP", "zip":"176115",
      "location": [31.8955385, 76.8380405], "fees":2000, 
      "tags":["Senior Secondary", "beautiful campus"], "rating":"3.5"
   }
}    
………………….
```

**人类可读输出**

此选项可以将统计响应更改为人类可读的形式(如果`human = true`)或计算机可读形式(如果`human = false`)。 例如，如果`human = true`那么`distance_kilometer = 20KM`，如果`human = false`那么`distance_meter = 20000`，则是响应需要被另一个计算机程序使用。

**响应过滤**
可以通过将其添加到`field_path`参数中来过滤对较少字段的响应。 例如，

```
POST http://localhost:9200/schools/_search?filter_path = hits.total
```

**请求正文**

```json
{
   "query":{
      "match_all":{}
   }
}
```

**响应**

```json
{"hits":{"total":3}}
```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-api-conventions.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处