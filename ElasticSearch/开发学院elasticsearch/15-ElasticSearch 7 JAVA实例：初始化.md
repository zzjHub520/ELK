## ElasticSearch 7 JAVA实例：初始化

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/qcmm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=868938950&amp;s2=614141960&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=ca08c87096df50a3&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345732228&amp;ti=ElasticSearch%207%20JAVA%E5%AE%9E%E4%BE%8B%EF%BC%9A%E5%88%9D%E5%A7%8B%E5%8C%96%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearc&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=3&amp;pcs=1864x885&amp;pss=1864x2771&amp;cfv=0&amp;cpl=16&amp;chi=15&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-java-initialization.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-testing.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345732&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345732216.76.76.76" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 ElasticSearch提供了很多API供开发者调用，在过去的版本中，应用的比较广泛的是通过Transport端口调用相应的功能，但是新版本的ElasticSearch已经不推荐使用Transport端口，而是推荐使用REST的方式调用功能。本实例以Java REST Client为作为参考。

**添加pom引用**

```
<dependency>
<groupId>org.elasticsearch</groupId>
<artifactId>elasticsearch</artifactId>
<version>7.1.1</version>
</dependency>
<dependency>
<groupId>org.elasticsearch.client</groupId>
<artifactId>elasticsearch-rest-high-level-client</artifactId>
<version>7.1.1</version>
</dependency>
<dependency>
<groupId>org.apache.logging.log4j</groupId>
<artifactId>log4j-api</artifactId>
<version>2.8.2</version>
</dependency>
<dependency>
<groupId>org.apache.logging.log4j</groupId>
<artifactId>log4j-core</artifactId>
<version>2.8.2</version>
</dependency>
<dependency>
<groupId>org.elasticsearch.client</groupId>
<artifactId>elasticsearch-rest-client</artifactId>
<version>7.1.1</version>
</dependency>
<dependency>
<groupId>org.elasticsearch.client</groupId>
<artifactId>elasticsearch-rest-client-sniffer</artifactId>
<version>7.1.1</version>
</dependency>
```

 Java High Level REST Client需要Java 1.8，并且依赖于Elasticsearch core项目。

**初始化**

 RestHighLevelClient实例需要按如下方式构建:

```
RestHighLevelClient client = new RestHighLevelClient(
        RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")));
```

**关闭客户端**

 RestHighLevelClient将根据提供的构建器在内部创建用于执行请求的客户端。该客户端维护一个连接池并启动一些线程，因此当您处理好请求时，您应该将其关闭以释放这些资源，改操作通过调用close()方法来完成:

```
client.close();
```