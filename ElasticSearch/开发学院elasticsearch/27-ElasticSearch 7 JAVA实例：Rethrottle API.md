## ElasticSearch 7 JAVA实例：Rethrottle API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/vcnm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=1265908453&amp;s2=33973807&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=8331f576e1426f1e&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346470438&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9ARethrottle%20API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CE&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=27&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-rethrottle-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-delete-by-query-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346471&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346470428.74.74.74" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



**RethrottleRequest**

 RethrottleRequest可用于更改正在运行的reindex, update-by-query或delete-by-query的当前限制，或者完全禁用任务的限制，它要求更改任务的任务标识。

 在其最简单的形式中，您可以使用它来禁用运行任务的throttling，方法如下:

```
RethrottleRequest request = new RethrottleRequest(taskId); //创建一个RethrottleRequest，禁用特定任务id的限制
```

 通过提供requestsPerSecond参数，请求将现有任务限制更改为指定值:

```
RethrottleRequest request = new RethrottleRequest(taskId, 100.0f); //将任务限制更改为每秒100个请求
```

 根据reindex, update-by-query和delete-by-query任务是否应该重新排序，可以使用三种适当方法之一来执行重新排序请求:

```
client.reindexRethrottle(request, RequestOptions.DEFAULT);  //执行重新索引重新调用请求
client.updateByQueryRethrottle(request, RequestOptions.DEFAULT); //按查询更新也是如此
client.deleteByQueryRethrottle(request, RequestOptions.DEFAULT); //按查询删除也是如此
```

**异步执行**

 rethrottle请求的异步执行要求将rethrottle请求实例和动作监听器实例都传递给异步方法:

```
client.reindexRethrottleAsync(request,
        RequestOptions.DEFAULT, listener); //异步执行重新索引重新计时
client.updateByQueryRethrottleAsync(request,
        RequestOptions.DEFAULT, listener); //按查询更新也是如此
client.deleteByQueryRethrottleAsync(request,
        RequestOptions.DEFAULT, listener); //按查询删除也是如此
```

 异步方法不会阻塞并立即返回。如果执行成功，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。典型的监听器是这样的:

```
listener = new ActionListener<ListTasksResponse>() {
    @Override
    public void onResponse(ListTasksResponse response) {
        //成功的时候调用
    }

    @Override
    public void onFailure(Exception e) {
        //失败的时候调用
    }
};
```



**Rethrottle Response**

 Rethrottling以ListTasksResponse的形式返回已经重新启动的任务。List Tasks API将详细描述该响应对象的结构。