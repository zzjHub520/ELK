## ElasticSearch 7 JAVA实例：Search Scroll API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/mcwm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=3882357451&amp;s2=1626035178&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=fff442f6b3e255d8&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346798386&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9ASearch%20Scroll%20API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=32&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-java-rest-high-search-scroll-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-search-uri-request..html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346798&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346798377.64.64.64" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 Scroll API可用于从搜索请求中检索大量结果。

 为了使用scrolling，需要按照指定的顺序执行以下步骤。

**初始化搜索scroll上下文**

 必须执行带有scroll参数的初始搜索请求，以通过Search API初始化scroll会话。处理此搜索请求时，Elasticsearch会检测scroll参数的存在，并在相应的时间间隔内保持搜索上下文有效。

```
SearchRequest searchRequest = new SearchRequest("posts");
SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
searchSourceBuilder.query(matchQuery("title", "Elasticsearch"));
searchSourceBuilder.size(size); //创建搜索请求及其对应的搜索源生成器。还可以选择设置大小来控制一次检索多少个结果。
searchRequest.source(searchSourceBuilder);
searchRequest.scroll(TimeValue.timeValueMinutes(1L)); //设置scroll间隔
SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
String scrollId = searchResponse.getScrollId(); //读取返回的scroll id，该id指向保持活动状态的搜索上下文，在以下搜索scroll调用中将需要它
SearchHits hits = searchResponse.getHits();  //检索第一批搜索命中
```

**检索所有相关文档**

 第二步，接收到的scroll标识符必须设置为SearchScrollRequest以及新的scroll间隔，并通过searchScroll方法发送。Elasticsearch返回另一批带有新scroll标识符的结果。然后，这个新的scroll标识符可以在后续的SearchScrollRequest中使用，以检索下一批结果，依此类推。这个过程应该在一个循环中重复，直到不再返回结果，这意味着scroll已经用尽，并且所有匹配的文档已经被检索到。

```
SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId); //通过设置所需的scroll id和scroll间隔来创建搜索scroll请求
scrollRequest.scroll(TimeValue.timeValueSeconds(30));
SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
scrollId = searchScrollResponse.getScrollId();  //读取新的scrollid，该id指向保持活动状态的搜索上下文，并且在接下来的搜索scroll调用中将需要它
hits = searchScrollResponse.getHits(); //检索另一批搜索命中
assertEquals(3, hits.getTotalHits().value);
assertEquals(1, hits.getHits().length);
assertNotNull(scrollId);
```

**清除scroll上下文**

 最后，可以使用清除Clear Scroll API删除最后一个scroll标识符，以便释放搜索上下文。scroll 到期时会自动发生这种情况，但最好在scroll 会话完成后立即进行。

**可选参数**

 构造搜索请求时，可以选择提供以下参数:

```
scrollRequest.scroll(TimeValue.timeValueSeconds(60L)); //滚动时间间隔
scrollRequest.scroll("60s"); //以字符串形式滚动间隔
```

 如果没有为搜索滚动请求设置滚动值，一旦初始滚动时间到期(即初始搜索请求中设置的滚动时间)，搜索上下文将到期。

**同步执行**

```
SearchResponse searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
```

**异步执行**

 SearchScrollRequest的异步执行要求将搜索滚动请求实例和操作侦听器实例都传递给异步方法:

```
client.scrollAsync(scrollRequest, RequestOptions.DEFAULT, scrollListener); //要执行的搜索请求和执行完成时要使用的操作侦听器
```

 异步方法不会阻塞并立即返回。如果执行成功，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。

 搜索响应的典型侦听器如下:

```
ActionListener<SearchResponse> scrollListener =
        new ActionListener<SearchResponse>() {
    @Override
    public void onResponse(SearchResponse searchResponse) {
        //成功的时候执行
    }

    @Override
    public void onFailure(Exception e) {
        //出错的时候执行
    }
};
```

**响应**

  search scroll API 返回一个 SearchResponse对象，与search API 相同。

**完整的例子**

```
final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
SearchRequest searchRequest = new SearchRequest("posts");
searchRequest.scroll(scroll);
SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
searchSourceBuilder.query(matchQuery("title", "Elasticsearch"));
searchRequest.source(searchSourceBuilder);

SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT); //通过发送初始搜索请求来初始化搜索上下文
String scrollId = searchResponse.getScrollId();
SearchHit[] searchHits = searchResponse.getHits().getHits();

while (searchHits != null && searchHits.length > 0) { //通过循环调用搜索滚动api来检索所有搜索命中，直到没有文档返回
    //处理返回的搜索结果
    SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId); //创建一个新的搜索滚动请求，保存最后返回的滚动标识符和滚动间隔
    scrollRequest.scroll(scroll);
    searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
    scrollId = searchResponse.getScrollId();
    searchHits = searchResponse.getHits().getHits();
}
ClearScrollRequest clearScrollRequest = new ClearScrollRequest(); //完成滚动后，清除滚动上下文
clearScrollRequest.addScrollId(scrollId);
ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
boolean succeeded = clearScrollResponse.isSucceeded();
```