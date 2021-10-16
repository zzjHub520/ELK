## ElasticSearch 7 测试

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/gctm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=1823035687&amp;s2=2504588875&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=02b914f3450cdbc2&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345693395&amp;ti=ElasticSearch%207%20%E6%B5%8B%E8%AF%95%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80%E6%96%B0%E7%89%88%E6%95%99&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=14&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-testing.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-modules.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345693&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345693385.48.48.48" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 ElasticSearch提供了一个jar文件，它可以添加到任何java集成开发环境中，并且可以用来测试与ElasticSearch相关的代码。通过使用ElasticSearch提供的框架，可以执行一系列测试。

-  单元测试
-  集成测试
-  随机测试

 首先，您需要将ElasticSearch测试依赖项添加到您的程序中。为此，您可以使用maven，并可以在pom.xml中添加以下内容。

```
<dependency>
   <groupId>org.elasticsearch</groupId>
   <artifactId>elasticsearch</artifactId>
   <version>2.1.0</version>
</dependency>
```

 EsSetup已被初始化以启动和停止弹性搜索节点，并创建索引。

```
EsSetup esSetup = new EsSetup();
```

 esSetup.execute() 函数将创建索引，您需要指定设置、类型和数据。

**单元测试**

 单元测试是使用JUnit和ElasticSearch测试框架进行的。节点和索引可以使用弹性搜索类创建，在测试方法中可以用来执行测试。ESTestCase和ESTokenStreamTestCase类用于此测试。

**集成测试**

 集成测试在一个集群中使用多个节点。电子集成测试用例类用于此测试。有多种方法可以使准备测试用例的工作变得更加容易。

 refresh()：集群中的所有索引都会刷新

 ensureGreen()：确保绿色健康集群状态

 ensureYellow()：确保黄色健康群集状态

 createIndex(name)：使用传递给此方法的名称创建索引

 flush()：集群中的所有索引都会被刷新

 flushAndRefresh()：同时执行flush()和refresh()

 indexExists(name)：验证指定索引是否存在

 clusterService()：返回集群服务java类

 cluster()：返回测试集群类

**测试集群方法**

 ensureAtLeastNumNodes(n)：确保群集中的最小节点数大于或等于指定数量。

 ensureAtMostNumNodes(n)：确保群集中最多的节点数量小于或等于指定数量。

 stopRandomNode()：停止群集中的随机节点

 stopCurrentMasterNode()：停止主节点

 stopRandomNonMaster()：停止群集中不是主节点的随机节点

 buildNode()：创建新节点

 startNode(settings)：启动新节点

 nodeSettings()：覆盖此方法以更改节点设置

**访问客户端**

 客户端用于访问群集中的不同节点并执行一些操作。方法用于获取随机客户端。ElasticSearch还提供了其他访问客户端的方法，这些方法可以使用esintegtESTscase . Internalcluster()方法来访问。

 iterator()：这有助于您访问所有可用的客户端。

 masterClient()：这将返回一个与主节点通信的客户端。

 nonMasterClient()：这将返回一个不与主节点通信的客户端。

 clientNodeClient()：这将返回当前在客户端节点上的客户端。

**随机测试**

 这种测试用于用每一个可能的数据来测试用户的代码，以便将来任何类型的数据都不会失败。随机数据是执行该测试的最佳选择。

生成随机数据

 在这个测试中，随机类由随机测试提供的实例进行实例化，并提供了许多获取不同类型数据的方法。

 getRandom() 随机类的实例

 randomBoolean() 随机Boolean

 randomByte() 随机byte

 randomShort() 随机short

 randomInt() 随机integer

 randomLong() 随机long

 randomFloat() 随机float

 randomDouble() 随机double

 randomLocale() 随机locale

 randomTimeZone() 随机time zone

 randomFrom() 随机element from array

**断言**

 ElasticsearchAssertions和ElasticsearchGeoAssertions类包含断言，用于在测试时执行一些常见的检查。例如，

```
SearchResponse seearchResponse = client().prepareSearch();
assertHitCount(searchResponse, 6);
assertFirstHit(searchResponse, hasId("6"));
assertSearchHits(searchResponse, "1", "2", "3", "4",”5”,”6”);
```