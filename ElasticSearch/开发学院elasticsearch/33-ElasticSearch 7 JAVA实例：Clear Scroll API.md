## ElasticSearch 7 JAVA实例：Clear Scroll API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/gctm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=2373082799&amp;s2=1735962305&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=f41b34490f0a2af5&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634346836292&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9AClear%20Scroll%20API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=33&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-java-rest-high-clear-scroll-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-java-rest-high-search-scroll-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634346836&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634346836278.50.51.51" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



**Clear Scroll API**

 当滚动超时时，Search Scroll API使用的搜索上下文会自动删除。但是建议使用Clear Scroll API，一旦不再需要搜索上下文，就立即释放它们。

**ClearScrollRequest**

 可以按如下方式创建一个ClearScrollRequest:

```
ClearScrollRequest request = new ClearScrollRequest(); //创建一个新的ClearScrollRequest
request.addScrollId(scrollId); //将滚动id添加到要清除的滚动标识符列表中
```

**提供滚动标识符**

 ClearScrollRequest允许在单个请求中清除一个或多个滚动标识符。

 滚动标识符可以一个接一个地添加到请求中:

```
request.addScrollId(scrollId);
```

 或者一起使用:

```
request.setScrollIds(scrollIds);
```

**同步方式执行**

```
ClearScrollResponse response = client.clearScroll(request, RequestOptions.DEFAULT);
```

**异步方式执行**

 ClearScrollRequest的异步执行需要将ClearScrollRequest实例和ActionListener实例都传递给异步方法:

```
client.clearScrollAsync(request, RequestOptions.DEFAULT, listener);
```

 异步方法不会阻塞并立即返回。如果执行成功，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。

 一个典型的ClearScrollResponse监听器像下面这样：

```
ActionListener<ClearScrollResponse> listener =
        new ActionListener<ClearScrollResponse>() {
    @Override
    public void onResponse(ClearScrollResponse clearScrollResponse) {
        //成功的时候调用
    }

    @Override
    public void onFailure(Exception e) {
        //失败的时候调用
    }
};
```

**ClearScrollResponse**

 返回的ClearScrollResponse允许检索关于发布的搜索上下文的信息:

```
boolean success = response.isSucceeded(); //如果请求成功返回true
int released = response.getNumFreed(); //返回发布的搜索上下文的数量
```