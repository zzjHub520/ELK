## ElasticSearch 7 JAVA实例：Update By Query API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/pclm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=930287083&amp;s2=777118948&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=8274fc5ead3228de&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346328838&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9AUpdate%20By%20Query%20API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=25&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-update-by-query-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-reindex-request.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346329&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346328829.67.67.68" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



**Update By Query Request**

 UpdateByQueryRequest可用于更新索引中的文档。

 它需要执行更新的现有索引(或一组索引)。

 更新查询的最简单形式如下:

```
UpdateByQueryRequest request =
        new UpdateByQueryRequest("source1", "source2"); //在一组索引上创建UpdateByQueryRequest。
```

 默认情况下，版本冲突会中止UpdateByQueryRequest进程，但您可以用以下方法来计算它们:

```
request.setConflicts("proceed"); //设置版本冲突时继续
```

 您可以通过添加查询来限制文档。

```
request.setQuery(new TermQueryBuilder("user", "kimchy")); //仅复制字段用户设置为kimchy的文档
```

 也可以通过设置大小来限制已处理文档的数量。

```
request.setSize(10); //只拷贝10个文档
```

 默认情况下UpdateByQueryRequest单次可以处理1000个文档。您可以使用sourceBatchSize更改批处理大小。

```
request.setSourceBatchSize(100); //单次处理100个文档
```

 Reindex还可以通过指定管道来使用摄取功能。

```
request.setDestPipeline("my_pipeline"); //设置管线为my_pipeline
```

 UpdateByQueryRequest还支持修改文档的脚本:

```
request.setScript(
    new Script(
        ScriptType.INLINE, "painless",
        "if (ctx._source.user == 'kimchy') {ctx._source.likes++;}",
        Collections.emptyMap())); //设置脚本来增加用户kimchy的所有文档中的likes字段。
```

 UpdateByQueryRequest可以使用带设置切片的切片滚动来并行化:

```
request.setSlices(2); //设置要使用的切片数量
```

 UpdateByQueryRequest使用Scroll参数来控制它将“搜索上下文”保持多长时间。

```
request.setScroll(TimeValue.timeValueMinutes(10)); //设置滚动时间
```

 如果您提供了路由，则路由将被复制到滚动查询，从而将该过程限制在与该路由值匹配的碎片上。

```
request.setRouting("=cat"); //设置路由
```

**可选参数**

 除了上述选项之外，还可以选择提供以下参数:

```
request.setTimeout(TimeValue.timeValueMinutes(2)); //等待按查询请求更新作为时间值执行的超时时间
request.setRefresh(true); //通过查询调用更新后刷新索引
request.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN); //设置索引选项
```

**同步执行**

 当以下列方式执行UpdateByQueryRequest时，客户端在继续执行代码之前，会等待返回UpdateByQueryResponse:

```
BulkByScrollResponse bulkResponse =
        client.updateByQuery(request, RequestOptions.DEFAULT);
```

 同步调用可能会在高级REST客户端中解析REST响应失败、请求超时或类似服务器没有响应的情况下抛出IOException。

 在服务器返回4xx或5xx错误代码的情况下，高级客户端会尝试解析响应主体错误详细信息，然后抛出一个通用的ElasticsearchException，并将原始ResponseException作为抑制异常添加到其中。

**异步执行**

 也可以异步方式执行UpdateByQueryRequest，以便客户端可以直接返回。用户需要通过将请求和侦听器传递给异步逐查询更新方法来指定如何处理响应或潜在故障:

```
client.updateByQueryAsync(request, RequestOptions.DEFAULT, listener);//要执行的UpdateByQueryRequest和执行完成时要使用的操作侦听器
```

 异步方法不会阻塞并立即返回。如果执行成功，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。失败场景和预期异常与同步执行情况相同。

  pdate-by-query的典型侦听器如下所示:

```
listener = new ActionListener<BulkByScrollResponse>() {
    @Override
    public void onResponse(BulkByScrollResponse bulkResponse) {
        //成功的时候执行
    }

    @Override
    public void onFailure(Exception e) {
         //失败的时候执行
    }
};
```

**UpdateByQueryResponse**

 返回的UpdateByQueryResponse包含有关已执行操作的信息，并允许迭代每个结果，如下所示:

```
TimeValue timeTaken = bulkResponse.getTook(); //获取总时间
boolean timedOut = bulkResponse.isTimedOut(); //检查请求是否超时
long totalDocs = bulkResponse.getTotal(); //获取处理的文档总数
long updatedDocs = bulkResponse.getUpdated(); //已更新的文档数
long deletedDocs = bulkResponse.getDeleted(); //已删除的文档数
long batches = bulkResponse.getBatches(); //已执行的批次数量
long noops = bulkResponse.getNoops(); //跳过的文档数
long versionConflicts = bulkResponse.getVersionConflicts(); //版本冲突的数量
long bulkRetries = bulkResponse.getBulkRetries(); //请求批量索引操作的重试次数
long searchRetries = bulkResponse.getSearchRetries(); //请求搜索操作重试的次数
TimeValue throttledMillis = bulkResponse.getStatus().getThrottled(); //如果当前处于睡眠状态，此请求限制自身的总时间不包括当前限制时间
TimeValue throttledUntilMillis =
        bulkResponse.getStatus().getThrottledUntil(); //任何当前休眠的剩余延迟，或者如果没有休眠，则为0
List<ScrollableHitSource.SearchFailure> searchFailures =
        bulkResponse.getSearchFailures(); //获取搜索的失败
List<BulkItemResponse.Failure> bulkFailures =
        bulkResponse.getBulkFailures(); //获取批量索引操作期间的失败
```