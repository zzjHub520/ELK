## ElasticSearch 7 JAVA实例：Multi-Search API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/ncjm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=2907911153&amp;s2=3552508660&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=aebdecc935ef6616&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346891130&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9AMulti-Search%20API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=34&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-java-rest-high-multi-search-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-java-rest-high-clear-scroll-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346891&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346891121.55.55.55" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



**Multi-Search API**

 multiSearch API在一个http请求中并行执行多个搜索请求。

**Multi-SearchRequest**

 MultiSearchRequest的构造函数是空的，您可以将所有希望执行的搜索添加到其中:

```
MultiSearchRequest request = new MultiSearchRequest(); //创建一个空的MultiSearchRequest 。
SearchRequest firstSearchRequest = new SearchRequest();//创建一个空的SearchRequest，并像常规搜索一样填充它。
SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
searchSourceBuilder.query(QueryBuilders.matchQuery("user", "kimchy"));
firstSearchRequest.source(searchSourceBuilder);
request.add(firstSearchRequest); //将SearchRequest添加到MultiSearchRequest中。
SearchRequest secondSearchRequest = new SearchRequest();  //构建第二个SearchRequest，并将其添加到MultiSearchRequest中。
searchSourceBuilder = new SearchSourceBuilder();
searchSourceBuilder.query(QueryBuilders.matchQuery("user", "luca"));
secondSearchRequest.source(searchSourceBuilder);
request.add(secondSearchRequest);
```

**可选参数**

 MultiSearchRequest支持所有的SearchRequest参数，例如：

```
SearchRequest searchRequest = new SearchRequest("posts"); //将请求限制为索引
```

**同步方式执行**

```
MultiSearchResponse response = client.msearch(request, RequestOptions.DEFAULT);
```

**异步方式执行**

```
client.searchAsync(searchRequest, RequestOptions.DEFAULT, listener);
```

 异步方法不会阻塞并立即返回。如果执行成功，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。

 MultiSearchResponse的典型监听器如下所示:

```
ActionListener<MultiSearchResponse> listener = new ActionListener<MultiSearchResponse>() {
    @Override
    public void onResponse(MultiSearchResponse response) {
        //成功的时候调用
    }

    @Override
    public void onFailure(Exception e) {
        //失败的时候调用
    }
};
```

**MultiSearchResponse**

 通过执行MultiSearchRequest方法返回MultiSearchResponse。 如果请求失败，每个MultiSearchResponse.Item都包含getFailure中的异常；如果请求成功，则包含getResponse中的搜索响应:

```
MultiSearchResponse.Item firstResponse = response.getResponses()[0]; //第一次搜索
assertNull(firstResponse.getFailure()); // 它执行成功了，所以getFailure返回null。                 
SearchResponse searchResponse = firstResponse.getResponse();//getResponse中有一个searchResponse。
assertEquals(4, searchResponse.getHits().getTotalHits().value);
MultiSearchResponse.Item secondResponse = response.getResponses()[1];//第二次搜索
assertNull(secondResponse.getFailure());
searchResponse = secondResponse.getResponse();
assertEquals(1, searchResponse.getHits().getTotalHits().value);
```