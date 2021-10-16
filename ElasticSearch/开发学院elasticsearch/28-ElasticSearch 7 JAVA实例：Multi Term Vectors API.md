## ElasticSearch 7 JAVA实例：Multi Term Vectors API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/icqm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=4292369425&amp;s2=234343604&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=a1000a126bbddd4c&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346527771&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9AMulti%20Term%20Vectors%20API%2C%E5%AD%A6%E4%B9%A0ElasticSearc&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=28&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-multi-term-vectors-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-rethrottle-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346528&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346527759.75.75.75" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 Multi Term Vectors API允许一次获得多个向量。

**Multi Term Vectors Request**

 创建MultiTermVectorsRequest有两种方法。

 第一种方法是创建一个空的MultiTermVectorsRequest，然后向其中添加单独的TermVectorsRequest求。

```java
MultiTermVectorsRequest request = new MultiTermVectorsRequest(); //创建一个空的MultiTermVectorsRequest。
TermVectorsRequest tvrequest1 =
    new TermVectorsRequest("authors", "1");
tvrequest1.setFields("user");
request.add(tvrequest1); //将第一个术语向量请求添加到多术语向量请求中。

XContentBuilder docBuilder = XContentFactory.jsonBuilder();
docBuilder.startObject().field("user", "guest-user").endObject();
TermVectorsRequest tvrequest2 =
    new TermVectorsRequest("authors", docBuilder);
request.add(tvrequest2); //将人工文档的第二个术语请求添加到多术语请求中。
```

 当所有TermVectorsRequest共享相同的参数(如索引和其他设置)时，可以使用第二种方法。在这种情况下，可以创建一个模板TermVectorsRequest，并设置所有必要的设置，该模板请求可以与执行这些请求的所有文档id一起传递给MultiTermVectorsRequest。

```java
TermVectorsRequest tvrequestTemplate =
    new TermVectorsRequest("authors", "fake_id"); //创建模板TermVectorsRequest。
tvrequestTemplate.setFields("user");
String[] ids = {"1", "2"};
MultiTermVectorsRequest request =
    new MultiTermVectorsRequest(ids, tvrequestTemplate); //将文档的id和模板传递给MultiTermVectorsRequest。
```

**同步执行**

 当以下列方式执行MultiTermVectorsRequest时，客户端会在继续执行代码之前等待返回MultiTermVectorsResponse:

```java
MultiTermVectorsResponse response =
    client.mtermvectors(request, RequestOptions.DEFAULT);
```

 同步调用可能会在高级REST客户端中解析REST响应失败、请求超时或类似服务器没有响应的情况下抛出IOException。

 在服务器返回4xx或5xx错误代码的情况下，高级客户端会尝试解析响应主体错误详细信息，然后抛出一个通用的ElasticsearchException，并将原始ResponseException作为抑制异常添加到其中。

**异步执行**

 也可以异步方式执行MultiTermVectorsRequest，以便客户端可以直接返回。用户需要通过将请求和侦听器传递给异步MultiTermVectors方法来指定如何处理响应或潜在故障:

```java
client.mtermvectorsAsync(
    request, RequestOptions.DEFAULT, listener);
```

 异步方法不会阻塞并立即返回。如果执行成功，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。失败场景和预期异常与同步执行情况相同。

 MultiTermVectors的典型监听器如下所示:

```java
listener = new ActionListener<MultiTermVectorsResponse>() {
    @Override
    public void onResponse(MultiTermVectorsResponse mtvResponse) {
        
    }
    @Override
    public void onFailure(Exception e) {
        
    }
};
```



**Multi Term Vectors Response**

 MultiTermVectorsResponse允许获得TermVectorsResponse的列表，每个响应都可以按照Term Vectors API中的描述进行检查。

```java
List<TermVectorsResponse> tvresponseList =
    response.getTermVectorsResponses(); //获取MultiTermVectorsResponse列表
if (tvresponseList != null) {
    for (TermVectorsResponse tvresponse : tvresponseList) {
    }
}
```

