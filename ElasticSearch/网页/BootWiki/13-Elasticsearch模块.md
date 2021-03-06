# Elasticsearch模块

------

**Elasticsearch**由多个模块组成，这些模块负责其功能。 这些模块有以下两种类型的设置 -

- **静态设置** - 在启动**Elasticsearch**之前，需要在配置文件(`elasticsearch.yml`)中配置这些设置。需要更新集群中的所有关注节点以反映这些设置的更改。
- **动态设置** - 这些设置可以在实时Elasticsearch上设置。

我们将在本章的以下部分讨论**Elasticsearch**中的每个模块。

## 集群级路由和碎片分配

集群级别设置决定将碎片分配给不同节点，以及将碎片重新分配给平衡集群。这些是以下设置来控制碎片分配 -

集群级碎片分配- 

- `cluster.routing.allocation.enable` 可能的值及说明 - 
  - `all` - 此默认值允许为所有种类的碎片分配碎片。
  - `primaries` - 这允许只为主碎片分配碎片。
  - `new_primaries`- 这允许只为新索引的主碎片分配碎片。
  - `none`- 这不允许任何碎片分配。
- `cluster.routing.allocation.node_concurrent_recoveries` - 一个数字值(默认为`2` )，它限制了并发碎片恢复的数量。
- `cluster.routing.allocation.node_initial_primaries_recoveries` - 一个数字值(默认为 `4` )，它限制了并行初始初级恢复的数量。
- `cluster.routing.allocation.same_shard.host` - 布尔值(默认为`false`), 它限制了同一物理节点中同一碎片的多个副本的分配。
- `indices.recovery.concurrent _streams` - 一个数字值(默认为`3` )，它控制在从对等体碎片恢复碎片时每个节点的开放网络流的数量。
- `indices.recovery.concurrent_small_file_streams` - 一个数字值(默认为`2` )，这控制了在碎片恢复时对于小于`5mb`的小文件的每个节点的开放流的数量。
- `cluster.routing.rebalance.enable`可能的值及说明 - 
  - `all` - 此默认值允许平衡所有种类的碎片。
  - `primaries`- 这允许只对主碎片进行碎片平衡。
  - `replicas` - 这允许只对副本碎片进行碎片平衡。
  - `none` - 这不允许任何类型的碎片平衡。
- `cluster.routing.allocation.allow_rebalance` 可能的值及说明 - 
  - `always` - 此默认值始终允许重新平衡。
  - `indices_primaries _active` - 这允许在分配集群中的所有主碎片时进行重新平衡。
  - `Indices_all_active` - 这允许在分配所有主碎片和副本碎片时重新平衡。
- `cluster.routing.allocation.cluster _concurrent_rebalance` - 一个数字值(默认为`2` ), 这限制了集群中的并发碎片平衡数。
- `cluster.routing.allocation.balance.shard` - 一个浮点数值(默认为`0.45f` ),这定义了在每个节点上分配的碎片的权重因子。
- `cluster.routing.allocation.balance.index` - 一个浮点数值(默认为`0.55f` ),这定义了在特定节点上分配的每个索引的碎片数量的比率。
- `cluster.routing.allocation.balance.threshold` - 一个浮点数值(默认为`1.0f` ),这定义了在特定节点上分配的每个索引的碎片数量的比率。
- `cluster.routing.allocation .balance.threshold` - 非负浮点值(默认为`1.0f`)这是应该执行的操作的最小优化值。

### 基于磁盘的碎片分配

| 设置                                                | 可能的值               | 描述                                                         |
| --------------------------------------------------- | ---------------------- | ------------------------------------------------------------ |
| cluster.routing.allocation.disk.threshold_enabled   | 布尔值(默认为`true`)   | 这启用和禁用磁盘分配决策程序。                               |
| cluster.routing.allocation.disk.watermark.low       | 字符串值(默认为`85％`) | 这表示磁盘的最大使用; 此后，无法将其他碎片分配给该磁盘。     |
| cluster.routing.allocation.disk.watermark.high      | 字符串值(默认为`90％`) | 这表示分配时的最大使用量; 如果在分配时达到这一点，Elasticsearch将把该碎片分配给另一个磁盘。 |
| cluster.info.update.interval                        | 字符串值(默认`30s`)    | 这是磁盘用法，检查两个时间之间的间隔。                       |
| cluster.routing.allocation.disk.include_relocations | 布尔值(默认为`true`)   | 这决定在计算磁盘使用率时是否考虑当前分配的分片。             |
|                                                     |                        |                                                              |
|                                                     |                        |                                                              |

## 发现

此模块帮助集群发现和维护其中的所有节点的状态。在从集群添加或删除节点时集群的状态发生更改。集群名称设置用于在不同集群之间创建逻辑差异。有一些模块，可以帮助您使用云供应商提供的API -

- Azure发现
- EC2发现
- Google计算引擎发现
- Zen发现

## 网关

此模块在整个群集重新启动时维护群集状态和分片数据。以下是此模块的静态设置 -

| 设置                          | 可能的值             | 描述                                                         |
| ----------------------------- | -------------------- | ------------------------------------------------------------ |
| gateway.expected_nodes        | 数值(默认为`0`)      | 预期在群集中用于恢复本地碎片的节点数。                       |
| gateway.expected_master_nodes | 数值(默认为`0`)      | 在开始恢复之前预期在群集中的主节点数。                       |
| gateway.expected_data_nodes   | 数值(默认为`0`)      | 开始恢复之前群集中预期的数据节点数。                         |
| gateway.recover_after_time    | 字符串值(默认为`5m`) | 这用于指定恢复进程将等待启动的时间，而不考虑在集群中加入的节点数。1. gateway.recover_after_nodes 2. gateway.recover_after_master_nodes 3. gateway.recover_after_data_nodes |

## HTTP

此模块管理HTTP客户端和Elasticsearch API之间的通信。可以通过将`http.enabled`的值更改为`false`来禁用此模块。 以下是控制此模块的设置(在`elasticsearch.yml`中配置)

| 编号 | 设置                         | 描述                                                 |
| ---- | ---------------------------- | ---------------------------------------------------- |
| 1    | http.port                    | 访问**Elasticsearch**的端口，范围为`9200-9300`。     |
| 2    | http.publish_port            | 此端口用于HTTP客户端，并且在防火墙的情况下也很有用。 |
| 3    | http.bind_host               | http服务的主机地址。                                 |
| 4    | http.publish_host            | http客户端的主机地址。                               |
| 5    | http.max_content_length      | 这是http请求中内容的最大值。其默认值为`100mb`。      |
| 6    | http.max_initial_line_length | 这是URL的最大值，其默认值为`4kb`。                   |
| 7    | http.max_header_size         | 这是最大http报头大小，其默认值为`8kb`。              |
| 8    | http.compression             | 这启用或禁用对压缩的支持，其默认值为`false`。        |
| 9    | http.pipelinig               | 这将启用或禁用HTTP通道线。                           |
| 10   | http.pipelining.max_events   | 这会限制在关闭HTTP请求之前排队的事件数。             |

## 索引

此模块维护对每个索引全局设置的设置。以下设置主要与内存使用有关 -

### 断路器

这用于防止操作引起`OutOfMemroyError`。 该设置主要限制JVM堆大小。 例如，`indices.breaker.total.limit`设置，JVM堆的默认为`70％`。

### Fielddata缓存

这主要用于在字段上聚合时。建议分配它足够的内存。 可以使用`indices.fielddata.cache.size`设置控制用于字段数据高速缓存的内存量。

### 节点查询缓存

此内存用于缓存查询结果。此缓存使用最近最少使用(LRU)逐出策略。 `Indices.queries.cahce.size`设置控制此缓存的内存大小。

### 索引缓冲区

此缓冲区将新创建的文档存储在索引中，并在缓冲区已满时将其刷新。设置为`indices.memory.index_buffer_size`控制为此缓冲区分配的堆的大小。

### Shard请求缓存

此缓存用于存储每个分片的本地搜索数据。缓存可以在创建索引期间启用，也可以通过发送URL参数来禁用。

```
Disable cache - ?request_cache = true
Enable cache "index.requests.cache.enable": true
```

### 索引恢复

它在恢复过程中控制资源。以下是一些设置 -

| 设置                                           | 默认值 |
| ---------------------------------------------- | ------ |
| indices.recovery.concurrent_streams            | 3      |
| indices.recovery.concurrent_small_file_streams | 2      |
| indices.recovery.file_chunk_size               | 512kb  |
| indices.recovery.translog_ops                  | 1000   |
| indices.recovery.translog_size                 | 512kb  |
| indices.recovery.compress                      | true   |
| indices.recovery.max_bytes_per_sec             | 40mb   |

### TTL间隔

生存时间(TTL)间隔定义文档的时间，之后文档将被删除。 以下是控制此过程的动态设置 -

| 设置                  | 默认值 |
| --------------------- | ------ |
| indices.ttl.interval  | 60     |
| indices.ttl.bulk_size | 1000   |

### 节点

每个节点有一个选项是否是数据节点。可以通过更改`node.data`设置更改此属性。将值设置为`false`将定义该节点不是数据节点。



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-modules.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处