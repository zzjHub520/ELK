## ElasticSearch 7 JAVA实例：删除文档

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/hcom?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=2249922460&amp;s2=1449361803&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=77116c65865f178c&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345971895&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9A%E5%88%A0%E9%99%A4%E6%96%87%E6%A1%A3%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSear&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=19&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-delete-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-exists-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345972&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345971883.52.52.52" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



**DeleteRequest**

 想要删除一个文档，必须构建一个DeleteRquest，如下：

```java
DeleteRequest request = new DeleteRequest(
        "posts",    //索引
        "1");       //文档id
```

**可选参数**

 DeleteRequest同样有一些可选参数：

```java
request.routing("routing"); //路由值
request.timeout(TimeValue.timeValueMinutes(2)); //以TimeValue形式设置超时
request.timeout("2m");  //以字符串形式设置超时
request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL); //以WriteRequest.RefreshPolicy实例的形式设置刷新策略
request.setRefreshPolicy("wait_for");  //以字符串的形式设置刷新策略            
request.version(2); //版本
request.versionType(VersionType.EXTERNAL); //版本类型
```

**同步执行**

 当以下列方式执行删除请求时，客户端在继续执行代码之前，会等待返回删除响应:

```java
DeleteResponse deleteResponse = client.delete(
        request, RequestOptions.DEFAULT);
```

 同步调用可能会在高级REST客户端中解析REST响应失败、请求超时或类似服务器没有响应的情况下抛出IOException。

 在服务器返回4xx或5xx错误代码的情况下，高级客户端会尝试解析响应主体错误详细信息，然后抛出一个通用的ElasticsearchException，并将原始ResponseException作为抑制异常添加到其中。

**异步执行**

 也可以异步方式执行DeleteRequest，以便客户端可以直接返回而无需等待。用户需要通过向异步删除方法传递请求和侦听器来指定如何处理响应或潜在问题:

```java
client.deleteAsync(request, RequestOptions.DEFAULT, listener); //要执行的删除请求和执行完成时要使用的操作侦听器
```

 异步方法不会阻塞并立即返回。如果执行成功，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。失败场景和预期异常与同步执行情况相同。

 一个典型的监听器如下：

```java
listener = new ActionListener<DeleteResponse>() {
    @Override
    public void onResponse(DeleteResponse deleteResponse) {
        //执行成功时调用
    }

    @Override
    public void onFailure(Exception e) {
        //执行失败时调用
    }
};
```

**DeleteResponse**

 返回的DeleteResponse允许检索关于已执行操作的信息，如下所示:

```java
String index = deleteResponse.getIndex();
String id = deleteResponse.getId();
long version = deleteResponse.getVersion();
ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
    //处理成功分片数少于总分片数的情况
}
if (shardInfo.getFailed() > 0) {
    for (ReplicationResponse.ShardInfo.Failure failure :
            shardInfo.getFailures()) {//处理潜在的故障
        String reason = failure.reason(); 
    }
}
```

 还可以检查文档是否被找到:

```java
DeleteRequest request = new DeleteRequest("posts", "does_not_exist");
DeleteResponse deleteResponse = client.delete(
        request, RequestOptions.DEFAULT);
if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
    //如果找不到要删除的文档，执行一些操作
}
```

 如果存在版本冲突，将引发弹性响应异常:

```java
try {
    DeleteResponse deleteResponse = client.delete(
        new DeleteRequest("posts", "1").setIfSeqNo(100).setIfPrimaryTerm(2),
            RequestOptions.DEFAULT);
} catch (ElasticsearchException exception) {
    if (exception.status() == RestStatus.CONFLICT) {
        //引发的异常表明返回了版本冲突错误
    }
}
```