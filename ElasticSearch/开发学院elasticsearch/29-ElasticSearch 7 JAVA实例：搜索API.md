## ElasticSearch 7 JAVA实例：搜索API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/bcjm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=1099904263&amp;s2=164385166&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=7952e1aed664670f&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346582953&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9A%E6%90%9C%E7%B4%A2API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSea&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=29&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-search-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-multi-term-vectors-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346583&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346582941.45.45.45" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 大多数搜索API是多索引、多类型的，Explain API端点除外。

路由

 执行搜索时，它将被广播到所有索引/索引碎片(副本之间的循环)。可以通过提供路由参数来控制搜索哪些碎片。例如，索引tweets时，路由值可以是用户名:

```java
POST /twitter/tweet?routing=kimchy
{
    "user" : "kimchy",
    "postDate" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
```

 在这种情况下，如果我们只想在tweets中搜索特定用户，我们可以将其指定为路由，导致搜索只命中相关碎片:

```java
POST /twitter/tweet/_search?routing=kimchy
{
    "query": {
        "bool" : {
            "must" : {
                "query_string" : {
                    "query" : "some query string here"
                }
            },
            "filter" : {
                "term" : { "user" : "kimchy" }
            }
        }
    }
}
```

 路由参数可以是用逗号分隔的字符串表示的多值。这将导致命中路由值匹配的相关碎片。

**统计组**

 搜索可以与统计组相关联，统计组维护每个组的统计聚合。稍后可以特别使用索引统计应用编程接口来检索它。例如，这里有一个搜索主体请求，它将请求与两个不同的组相关联:

```java
POST /_search
{
    "query" : {
        "match_all" : {}
    },
    "stats" : ["group1", "group2"]
}
```

**全局搜索超时**

 作为搜索请求的一部分，单个搜索可以设置超时。由于搜索请求可以来自许多来源，Elasticsearch具有动态集群级全局搜索超时的设置，适用于在搜索请求正文中未设置超时的所有请求。默认值是无全局超时。设置键是search.default_search_timeout，可以使用群集更新设置端点进行设置。将该值设置为-1将全局搜索超时重置为无超时。

**搜索取消**

 我们可以使用标准任务取消机制取消搜索。默认情况下，正在运行的搜索只检查它是否在段边界上被取消，因此取消可能会被较大的段延迟。通过将动态集群级设置search . low_level_cancel设置为true，可以提高搜索取消响应速度。然而，它伴随着更频繁的取消检查的额外开销，这在大型快速运行的搜索查询中是显而易见的。更改此设置只会影响更改后开始的搜索。



