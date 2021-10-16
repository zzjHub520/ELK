## ElasticSearch 7 JAVA实例：Multi-Get API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/fccm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=3618520666&amp;s2=2493898760&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=4e0485b2aafb5af0&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346194565&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9AMulti-Get%20API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CEl&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2837&amp;cfv=0&amp;cpl=16&amp;chi=23&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-multiget-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-bulk-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346195&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346194555.51.51.51" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 multiGet API在一个http请求中并行执行多个获取请求。

**Multi-Get Request**

 MultiGetRequest的构造函数为空，你可以添加MultiGetRequest.Item到查询中。

```java
MultiGetRequest request = new MultiGetRequest();
request.add(new MultiGetRequest.Item(
    "index",         //索引
    "example_id"));  //文档id
request.add(new MultiGetRequest.Item("index", "another_id")); //添加另一个要提取的项目
```

**可选参数**

```java
request.add(new MultiGetRequest.Item("index", "example_id")
    .fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE));  //禁用源检索，默认情况下启用
String[] includes = new String[] {"foo", "*r"};
String[] excludes = Strings.EMPTY_ARRAY;
FetchSourceContext fetchSourceContext =
        new FetchSourceContext(true, includes, excludes);
request.add(new MultiGetRequest.Item("index", "example_id")
    .fetchSourceContext(fetchSourceContext));  //为特定字段配置源包含
String[] includes = Strings.EMPTY_ARRAY;
String[] excludes = new String[] {"foo", "*r"};
FetchSourceContext fetchSourceContext =
        new FetchSourceContext(true, includes, excludes);
request.add(new MultiGetRequest.Item("index", "example_id")
    .fetchSourceContext(fetchSourceContext));  //为特定字段配置源排除
request.add(new MultiGetRequest.Item("index", "example_id")
    .storedFields("foo"));  //配置特定存储字段的检索(要求字段在映射中单独存储)
MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
MultiGetItemResponse item = response.getResponses()[0];
String value = item.getResponse().getField("foo").getValue(); //检索foo存储字段(要求该字段在映射中单独存储)
request.add(new MultiGetRequest.Item("index", "with_routing")
    .routing("some_routing"));    //路由值
request.add(new MultiGetRequest.Item("index", "with_version")
    .versionType(VersionType.EXTERNAL)  //版本
    .version(10123L));              //版本类型
  preference, realtime和refresh可以在主请求上设置，但不能在任何项目上设置:
request.preference("some_preference");  //偏好值
request.realtime(false);//将实时标志设置为false(默认true)
request.refresh(true); //在检索文档之前执行刷新(默认false)
```

**同步执行**

 当以下列方式执行多重网格请求时，客户端在继续执行代码之前，会等待多重网格响应返回:

```java
MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
```

 同步调用可能会在高级REST客户端中解析REST响应失败、请求超时或类似服务器没有响应的情况下抛出IOException。

 在服务器返回4xx或5xx错误代码的情况下，高级客户端会尝试解析响应主体错误详细信息，然后抛出一个通用的ElasticsearchException，并将原始ResponseException作为抑制异常添加到其中。

**异步执行**

 也可以异步方式执行MultiGetRequest，以便客户端可以直接返回。用户需要通过向异步多获取方法传递请求和侦听器来指定如何处理响应或潜在故障:

```java
client.mgetAsync(request, RequestOptions.DEFAULT, listener); //要执行的多重请求和执行完成时要使用的操作侦听器
```

 异步方法不会阻塞并立即返回。如果执行成功，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。失败场景和预期异常与同步执行情况相同。

 典型的multi-get监听器如下所示:

```java
listener = new ActionListener<MultiGetResponse>() {
    @Override
    public void onResponse(MultiGetResponse response) {
        //成功的时候执行
    }

    @Override
    public void onFailure(Exception e) {
        //失败的时候执行
    }
};
```

**Multi Get Response**

 返回的MultiGetResponse包含一个MultiGetItemResponse列表，按请求的顺序排列在GetResponse中。MultiGetResponse包含获取成功时的获取GetResponse或MultiGetResponse。失败则提示失败，成功看起来就像普通的GetResponse。

```java
MultiGetItemResponse firstItem = response.getResponses()[0];
assertNull(firstItem.getFailure()); //getFailure返回null意味着没有失败。
GetResponse firstGet = firstItem.getResponse();//getResponse返回GetResponse。
String index = firstItem.getIndex();
String id = firstItem.getId();
if (firstGet.isExists()) {
    long version = firstGet.getVersion();
    String sourceAsString = firstGet.getSourceAsString(); //以字符串形式检索文档
    Map<String, Object> sourceAsMap = firstGet.getSourceAsMap();//以Map<String, Object>的形式检索文档
    byte[] sourceAsBytes = firstGet.getSourceAsBytes(); //以 byte[]形式检索文档
} else {
    //处理找不到文档的情况。请注意，虽然返回的响应有404个状态代码，但返回的是有效的GetResponse，而不是引发的异常。这种响应不包含任何源文档，其isExists方法返回false。
}
```

 当对不存在的索引执行的子请求之一getFailure将包含异常:

```java
assertNull(missingIndexItem.getResponse());//getResponse为空。
Exception e = missingIndexItem.getFailure().getFailure();//getFailure不是并且包含异常。
ElasticsearchException ee = (ElasticsearchException) e; //  这个异常是一个ElasticsearchException
// TODO status is broken! fix in a followup
// assertEquals(RestStatus.NOT_FOUND, ee.status());//它的状态为“未找到”。如果不是多重获取，它应该是一个HTTP 404。
assertThat(e.getMessage(),
    containsString("reason=no such index [missing_index]"));//getMessage解释了原因。
```

 如果请求了特定的文档版本，并且现有文档具有不同的版本号，则会引发版本冲突: 

```java
MultiGetRequest request = new MultiGetRequest();
request.add(new MultiGetRequest.Item("index", "example_id")
    .version(1000L));
MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
MultiGetItemResponse item = response.getResponses()[0];
assertNull(item.getResponse());//getResponse为空。
Exception e = item.getFailure().getFailure();//getFailure不是并且包含异常。
ElasticsearchException ee = (ElasticsearchException) e;//这个异常是一个ElasticsearchException
// TODO status is broken! fix in a followup
// assertEquals(RestStatus.CONFLICT, ee.status());//它的状态为CONFLICT。如果不是多重获取，它应该是一个HTTP 409。
assertThat(e.getMessage(),
    containsString("version conflict, current version [1] is "
        + "different than the one provided [1000]")); //getMessage解释了实际原因
```