## ElasticSearch 7 模块

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/zctm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=1522347563&amp;s2=4226597234&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=37979bb19cb26871&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345641537&amp;ti=ElasticSearch%207%20%E6%A8%A1%E5%9D%97%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80%E6%96%B0%E7%89%88%E6%95%99&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2983&amp;cfv=0&amp;cpl=16&amp;chi=13&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-modules.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-analysis.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345642&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345641528.39.39.39" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 ElasticSearch由许多模块组成，这些模块负责不同的功能。ElasticSearch模块有两种类型的设置：

-  静态设置-在开始使用ElasticSearch之前，需要在配置文件中配置这些设置。您需要更新群集中的所有关注节点，以反映这些设置的更改。
-  动态设置-这些设置可以在实时ElasticSearch中设置。

 我们将在本章的以下部分讨论ElasticSearch的不同模块。

**集群级路由和分片分配**

 群集级别设置决定将碎片分配给不同的节点，并重新分配碎片以重新平衡群集。以下是控制碎片分配的设置

集群级碎片分配

 cluster.routing.allocation.enable：可选值all/primaries/new_primaries/none。

 cluster.routing.allocation .node_concurrent_recoveries：数字值，默认2。限制并发碎片恢复的数量。

 cluster.routing.allocation .node_initial_primaries_recoveries：数字值，默认4。限制并行初始主恢复的数量。

 cluster.routing.allocation .same_shard.host：布尔值，默认false。限制同一物理节点中同一碎片的多个副本的分配。

 indices.recovery.concurrent _streams：数字值，默认3。控制从对等碎片中碎片恢复时每个节点打开的网络流数量。

 indices.recovery.concurrent _small_file_streams：数字值，默认2。控制碎片恢复时每个节点上大小小于5mb的小文件的打开流数量。

 cluster.routing.rebalance.enable：可选值all/primaries/replicas/none。

 cluster.routing.allocation .allow_rebalance：可选值always/indices_primaries _active/Indices_all_active。

 cluster.routing.allocation.cluster _concurrent_rebalance：数字值，默认2。限制集群中并发碎片平衡的数量。

 cluster.routing.allocation .balance.shard：浮点值，默认0.45f。定义分配给每个节点的碎片的权重因子。

 cluster.routing.allocation .balance.index：浮点值，默认0.55f。定义在特定节点上分配的每个索引的碎片数比率。

 cluster.routing.allocation .balance.threshold：非负数浮点值，默认1.0f。 执行操作的最小优化值。

**基于磁盘的碎片分配**

 cluster.routing.allocation .disk.threshold_enabled：布尔值，默认true。 启用和禁用磁盘分配决策器。

 cluster.routing.allocation .disk.watermark.low：字符值，默认85%。表示磁盘的最大使用量。

 cluster.routing.allocation .disk.watermark.high：字符值，默认90%。分配时的最大使用量；如果在分配时达到这一点，ElasticSearch会将该碎片分配给另一个磁盘。

 cluster.info.update.interval：字符值，默认30s。检查磁盘的间隔。

 cluster.routing.allocation .disk.include_relocations：布尔值，默认true。决定在计算磁盘使用量时是否考虑当前分配的碎片。

**集群发现**

 该模块帮助集群发现和维护其中所有节点的状态。在群集中添加或删除节点时，群集的状态会发生变化。群集名称设置用于创建不同群集之间的逻辑差异。有一些模块可以帮助您使用云供应商提供的API，它们包括：Azure发现、EC2发现、Google compute engine 发现、Zen发现。

**网关**

 此模块维护整个群集重启期间的群集状态和碎片数据。以下是该模块的静态设置。

 gateway.expected_ nodes：数字值，默认0。预计群集中用于恢复本地碎片的节点数。

 gateway.expected_ master_nodes：数字值，默认0。开始恢复之前，群集中预期的主节点数。

 gateway.expected_ data_nodes：数字值，默认0。开始恢复之前群集中预期的数据节点数。

 gateway.recover_ after_time：字符值，默认5m。指定恢复过程等待开始的时间，而不考虑群集中加入的节点数量。

 gateway.recover_ after_nodes

 gateway.recover_after_ master_nodes

 gateway.recover_after_ data_nodes

**HTTP**

 该模块管理HTTP协议客户端和ElasticSearch API之间的通信。可以通过将http.enabled的值更改为false来禁用此模块。以下是控制该模块的设置(在elasticsearch.yml中配置)

 http.port：设置ElasticSearch对外服务的端口，范围从9200到9300。

 http.publish_port：该端口适用于http客户端，也适用于防火墙。

 http.bind_host：http服务的主机地址。

 http.publish_host：http客户端的主机地址。

 http.max_content_length：http请求中内容的最大容量大小。其默认值为100mb。

 http.max_initial_line_length：网址的最大大小，默认值为4kb。

 http.max_header_size：最大http头大小，默认值为8kb。

 http.compression：启用或禁用对压缩的支持，默认值为false。

 http.pipelinig：启用或禁用超文本传输协议流水线。

 http.pipelining.max_events：限制关闭一个HTTP请求之前要排队的事件数。

**索引**

 该模块维护为每个索引全局设置的设置。以下设置主要与内存使用有关。

**断路器**

 这用于防止操作导致故障。该设置主要限制JVM堆的大小。例如，indices.breaker.total.limit设置，默认为JVM堆的70%。

**字段数据缓存**

 这主要用于在字段上聚合时。建议有足够的内存来分配它。可以使用indices.fielddata.cache.size设置来控制用于字段数据缓存的内存量。

**节点查询缓存**

 该内存用于缓存查询结果。此缓存使用最近最少使用(LRU)驱逐策略。Indices.queries.cahce.size设置控制此缓存的内存大小。

**索引缓冲区**

 该缓冲区将新创建的文档存储在索引中，并在缓冲区已满时刷新它们。设置类似indices.memory.index_buffer_size控制为该缓冲区分配的堆的数量。

**碎片请求缓存**

 该缓存用于存储每个碎片的本地搜索数据。缓存可以在创建索引时启用，也可以通过发送网址参数来禁用。

```
Disable cache - ?request_cache = true
Enable cache "index.requests.cache.enable": true
```

**索引恢复**

 它在恢复过程中控制资源。以下是设置.

 indices.recovery.concurrent_streams:默认值3

 indices.recovery.concurrent_small_file_streams:默认值2

 indices.recovery.file_chunk_size:默认值512kb

 indices.recovery.translog_ops:默认值1000

 indices.recovery.translog_size:默认值512kb

 indices.recovery.compress:默认值true

 indices.recovery.max_bytes_per_sec:默认值40mb

**TTL间隔**

 生存时间(TTL)间隔定义文档的时间，在此之后文档将被删除。以下是控制此过程的动态设置。

设置 默认值

 indices.ttl.interval:默认值60s

 indices.ttl.bulk_size:默认值1000

**节点**

 每个节点都可以选择是否是数据节点。您可以通过更改node.data设置来更改此属性。将该值设置为false将定义该节点不是数据节点。