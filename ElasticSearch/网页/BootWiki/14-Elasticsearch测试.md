# Elasticsearch测试

------

**Elasticsearch**提供了一个jar文件，可以将其添加到任何Java IDE，并可用于测试与Elasticsearch相关的代码。 可以使用**Elasticsearch**提供的框架执行一系列测试 -

- 单元测试
- 集成测试
- 随机测试

要开始测试，需要向程序添加**Elasticsearch**测试依赖关系。您可以使用[maven](http://www.yiibai.com/maven/)来实现此目的，并且可在`pom.xml`中添加以下内容。

```xml
<dependency>
   <groupId>org.elasticsearch</groupId>
   <artifactId>elasticsearch</artifactId>
   <version>2.1.0</version>
</dependency>
```

**EsSetup**初始化用来启动和停止Elasticsearch节点，并创建索引。

```java
EsSetup esSetup = new EsSetup();
```

`esSetup.execute()`函数与`createIndex`用于创建索引，需要指定设置，类型和数据。

## 单元测试

单元测试是通过使用JUnit和Elasticsearch测试框架来进行的。可以使用Elasticsearch类创建节点和索引，并且在测试方法中可以用来执行测试。`ESTestCase`和`ESTokenStreamTestCase`类用于此测试。

## 集成测试

集成测试在集群中使用多个节点。 `ESIntegTestCase`类用于这种类型的测试。 有多种方法使得准备测试用例的工作更容易。

| 编号 | 方法              | 描述                           |
| ---- | ----------------- | ------------------------------ |
| 1    | refresh()         | 刷新群集中的所有索引           |
| 2    | ensureGreen()     | 确保绿色健康集群状态           |
| 3    | ensureYellow()    | 确保黄色的健康群集状态         |
| 4    | createIndex(name) | 使用传递给此方法的名称创建索引 |
| 5    | flush()           | 刷新群集中的所有索引           |
| 6    | flushAndRefresh() | 执行 flush() 和 refresh()      |
| 7    | indexExists(name) | 验证指定索引是否存在           |
| 8    | clusterService()  | 返回集群服务java类             |
| 9    | cluster()         | 返回测试集群类                 |

### 测试集群方法

| 编号 | 方法                     | 描述                                       |
| ---- | ------------------------ | ------------------------------------------ |
| 1    | ensureAtLeastNumNodes(n) | 确保群集中最小节点数量大于或等于指定数量。 |
| 2    | ensureAtMostNumNodes(n)  | 确保群集中最大节点数小于或等于指定数。     |
| 3    | stopRandomNode()         | 停止集群中的随机节点                       |
| 4    | stopCurrentMasterNode()  | 停止主节点                                 |
| 5    | stopRandomNonMaster()    | 停止集群中的随机节点(不是主节点)           |
| 6    | buildNode()              | 创建一个新节点                             |
| 7    | startNode(settings)      | 启动一个新节点                             |
| 8    | nodeSettings()           | 覆盖此方法以更改(更新)节点设置             |

### 访问客户端

客户端用于访问集群中的不同节点并执行某些操作。`ESIntegTestCase.client()`方法用于获取随机客户端。Elasticsearch还提供了其他方法来访问客户端，这些方法可以使用`ESIntegTestCase.internalCluster()`方法访问。

| 编号 | 方法               | 描述                               |
| ---- | ------------------ | ---------------------------------- |
| 1    | iterator()         | 这用于访问所有可用的客户端。       |
| 2    | masterClient()     | 返回一个正在与主节点通信的客户端。 |
| 3    | nonMasterClient()  | 返回一个不与主节点通信的客户端。   |
| 4    | clientNodeClient() | 将返回当前在客户端节点上的客户端。 |

## 随机测试

随机测试是用于使用所有可能的数据测试用户的代码，以便将来不会出现任何类型的数据失败。 随机数据是进行此测试的最佳选择。

### 生成随机数据

在这个测试中，`Random`类由`RandomizedTest`提供的实例实例化，并提供了许多方法来获取不同类型的数据。

| 方法             | 返回值           |
| ---------------- | ---------------- |
| getRandom()      | 随机类的实例     |
| randomBoolean()  | 随机布尔值       |
| randomByte()     | 随机字节值       |
| randomShort()    | 随机短整型值     |
| randomInt()      | 随机整型值       |
| randomLong()     | 随机长整型值     |
| randomFloat()    | 随机浮点值       |
| randomDouble()   | 随机双精度浮点值 |
| randomLocale()   | 随机区域设置     |
| randomTimeZone() | 随机时区         |
| randomFrom()     | 数组中的随机元素 |

## 断言

`ElasticsearchAssertions`和`ElasticsearchGeoAssertions`类包含断言，用于在测试时执行一些常见检查。 例如，

```java
SearchResponse seearchResponse = client().prepareSearch();
assertHitCount(searchResponse, 6);
assertFirstHit(searchResponse, hasId("6"));
assertSearchHits(searchResponse, "1", "2", "3", "4",”5”,”6”);
```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-testing.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处