## ElasticSearch 7 JAVA实例：搜索

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/ecmm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=2353343360&amp;s2=2617648819&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=b3e916c7cc5c5cb1&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346658744&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9A%E6%90%9C%E7%B4%A2%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=3&amp;pcs=1864x885&amp;pss=1864x2771&amp;cfv=0&amp;cpl=16&amp;chi=30&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-search-api-search.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-search-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346659&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346658733.46.46.46" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 搜索API允许您执行搜索查询，并返回与该查询匹配的搜索命中。查询可以使用简单的查询字符串作为参数来提供，也可以使用请求体来提供。

**多索引搜索**

 所有搜索API都支持多索引语法，可以跨多个索引应用。例如，我们可以搜索twitter索引中的所有文档:

```
GET /twitter/_search?q=user:kimchy
```

 我们还可以在多个索引中搜索带有特定标签的所有文档(例如，当每个用户有一个索引时):

```
GET /kimchy,elasticsearch/_search?q=tag:wow
```

 或者，我们可以使用_all搜索所有可用的索引:

```
GET /_all/_search?q=tag:wow
```

**部分回应**

 为了确保快速响应，如果一个或多个碎片出现故障，搜索API将使用部分结果进行响应。有关更多信息，请参见碎片故障。