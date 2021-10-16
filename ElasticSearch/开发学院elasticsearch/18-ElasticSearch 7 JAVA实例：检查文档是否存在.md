## ElasticSearch 7 JAVA实例：检查文档是否存在

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/schm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=1590003599&amp;s2=2027040616&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=df4c3a0bb19a83bd&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345911761&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9A%E6%A3%80%E6%9F%A5%E6%96%87%E6%A1%A3%E6%98%AF%E5%90%A6%E5%AD%98%E5%9C%A8%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElastic&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=18&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-exists-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-get-document-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345912&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345911743.63.63.63" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



**Exists API** 

 如果文档存在，现有应用编程接口返回true，否则返回false。

**Exists Request** 

 使用GetRequest，就像获取应用程序接口一样。支持它的所有可选参数。由于exists()只返回true或false，我们建议关闭提取源和任何存储字段，这样消耗资源会少一些:

```
GetRequest getRequest = new GetRequest(
    "posts", //索引
    "1");    //文档id
getRequest.fetchSourceContext(new FetchSourceContext(false)); //禁用fetching _source.
getRequest.storedFields("_none_");
```

**同步执行**

 当以下列方式执行GetRequest时，客户端在继续执行代码之前，会等待返回布尔值:

```
boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
```

 同步调用可能会在高级REST客户端中解析REST响应失败、请求超时或类似服务器没有响应的情况下抛出IOException。

 在服务器返回4xx或5xx错误代码的情况下，高级客户端会尝试解析响应主体错误详细信息，然后抛出一个通用的ElasticsearchException，并将原始ResponseException作为抑制异常添加到其中。

**异步执行**

 执行GetRequest也可以异步方式完成，这样客户端就可以直接返回。用户需要通过向异步exists方法传递请求和侦听器来指定如何处理响应或潜在故障:

```
client.existsAsync(getRequest, RequestOptions.DEFAULT, listener); //要执行的GetRequest和执行完成时要使用的ActionListener
```

 异步方法不会阻塞并立即返回。如果执行成功，则使用onResponse方法回调操作侦听器，如果执行失败，则使用onFailure方法回调操作侦听器。失败场景和预期异常与同步执行情况相同。

 一个典型的listener如下：

```
ActionListener<Boolean> listener = new ActionListener<Boolean>() {
    @Override
    public void onResponse(Boolean exists) {//执行成功的时候调用
        
    }

    @Override
    public void onFailure(Exception e) {//出错的时候调用
        
    }
};
```

**Source exists request** 

 exists请求的一个变体是existsSource方法，该方法具有对所讨论的文档是否存储了源的附加检查。如果索引的映射选择了删除对在文档中存储JSON源的支持，那么对于该索引中的文档，此方法将返回false。



