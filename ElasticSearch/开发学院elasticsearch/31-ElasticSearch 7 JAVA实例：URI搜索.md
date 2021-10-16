## ElasticSearch 7 JAVA实例：URI搜索

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/ycfm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=1624829815&amp;s2=262732513&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=4f0c3797a0b891c5&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346735700&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9AURI%E6%90%9C%E7%B4%A2%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSea&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=3&amp;pcs=1864x885&amp;pss=1864x2771&amp;cfv=0&amp;cpl=16&amp;chi=31&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-search-uri-request..html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-search-api-search.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346736&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346735687.76.77.77" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 通过提供请求参数，可以纯粹使用URI执行搜索请求。在使用这种模式执行搜索时，并不是所有的搜索选项都是公开的，但是对于快速的“curl tests”来说，这是很方便的。下面是一个例子:



 下面是一个简单的响应例子

{

  "timed_out": false,

  "took": 62,

  "_shards":{

​    "total" : 1,

​    "successful" : 1,

​    "skipped" : 0,

​    "failed" : 0

  },

  "hits":{

​    "total" : {

​      "value": 1,

​      "relation": "eq"

​    },

​    "max_score": 1.3862944,

​    "hits" : [

​      {

​        "_index" : "twitter",

​        "_type" : "_doc",

​        "_id" : "0",

​        "_score": 1.3862944,

​        "_source" : {

​          "user" : "kimchy",

​          "date" : "2009-11-15T14:12:12",

​          "message" : "trying out Elasticsearch",

​          "likes": 0

​        }

​      }

​    ]

  }

}

参数

 URI搜索允许使用以下参数:

 q：查询字符串。

 df：查询中未定义字段前缀时使用的默认字段

 analyzer：分析查询字符串时要使用的分析器名称。

 analyze_wildcard：是否应该分析通配符和前缀查询。默认为false。

 batched_reduce_size：协调节点上应立即减少的碎片结果的数量。如果请求中的潜在碎片数量可能很大，则该值应用作保护机制，以减少每个搜索请求的内存开销。

 default_operator：要使用的默认运算符可以是“与”或“或”。默认为“或”。

 lenient：如果设置为true，将导致忽略基于格式的异常。默认为false。

 explain：对于每一次命中，包含一个如何计算命中得分的解释。

 _source：设置为false以禁用对_source字段的检索。您也可以通过使用_ source _ includes & source _ excludes来检索文档的一部分(有关更多详细信息，请参见请求正文文档)

 stored_fields：每次命中时要返回的文档的选择性存储字段，以逗号分隔。不指定任何值将导致没有字段返回。

 sort：要执行的排序。可以是字段名的形式，也可以是字段名:ASC/字段名:desc。字段名可以是文档中的实际字段，也可以是表示基于分数排序的特殊分数名。可以有几个排序参数(顺序很重要)。

 track_scores：排序时，设置为true，以便仍然跟踪分数并将其作为每次命中的一部分返回。

 track_total_hits：默认值为10，000。设置为false以禁用对匹配查询的命中总数的跟踪。它还接受一个整数，在本例中，该整数代表要精确计数的命中数。(有关更多详细信息，请参见请求正文文档)。

 timeout：搜索超时，将搜索请求限定在指定的时间值内执行，过期时点击次数累计到该时间点。默认为无超时。

 terminate_after：为每个碎片收集的最大文档数，一旦达到该数量，查询执行将提前终止。如果设置，响应将有一个布尔字段terminated_early，以指示查询执行是否实际上已终止_early。默认为无终止_之后。

 from：从命中的索引开始返回。默认为0。

 size：要返回的命中数。默认值为10。

 search_type：要执行的搜索操作的类型。可以是dfs_query_then_fetch或query_then_fetch。默认为query_then_fetch。有关可以执行的不同搜索类型的更多详细信息，请参见搜索类型。

 allow_partial_search_results：如果请求会产生部分结果，则设置为false返回整体失败。默认值为true，这将在超时或部分失败的情况下允许部分结果。可以使用群集级别的设置搜索来控制此默认值。