## ElasticSearch 7 集群API

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/qcrm?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=257916431&amp;s2=2768650792&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=4a294e6103f06437&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345396753&amp;ti=ElasticSearch%207%20%E9%9B%86%E7%BE%A4API%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x3185&amp;cfv=0&amp;cpl=16&amp;chi=9&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-cluster-api.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-index-api.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345397&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345396740.50.50.50" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 该API用于获取集群及其节点的信息，并对其进行更改。为了调用这个应用编程接口，我们需要指定节点名、地址或本地。例如，

```
GET /_nodes/_local
```

响应

```
………………………………………………
{
   "cluster_name":"elasticsearch", "nodes":{
      "Vy3KxqcHQdm4cIM22U1ewA":{
         "name":"Red Guardian", "transport_address":"127.0.0.1:9300", 
         "host":"127.0.0.1", "ip":"127.0.0.1", "version":"2.1.1", 
         "build":"40e2c53", "http_address":"127.0.0.1:9200",
      }
   }
}
………………………………………………
```

 或者

```
Get /_nodes/127.0.0.1
```

响应

  与上述示例相同。

**群集健康**

 该API用于通过附加health关键字来获取集群的健康状态。例如，

```
GET /_cluster/health
```

响应

```
{
   "cluster_name":"elasticsearch", "status":"yellow", "timed_out":false,
   "number_of_nodes":1, "number_of_data_nodes":1, "active_primary_shards":23,
   "active_shards":23, "relocating_shards":0, "initializing_shards":0,
   "unassigned_shards":23, "delayed_unassigned_shards":0, "number_of_pending_tasks":0,
   "number_of_in_flight_fetch":0, "task_max_waiting_in_queue_millis":0,
   "active_shards_percent_as_number":50.0
}
```

**集群状态**

 用于通过在请求URL中附加state关键字来获取集群的状态信息。状态信息包含版本、主节点、其他节点、路由表、元数据和块。例如，

```
GET /_cluster/state 10. Elasticsearch — Cluster APIs
```

响应

```
………………………………………………

{
   "cluster_name":"elasticsearch", "version":27, "state_uuid":"B3P7uHGKQUGsSsiX2rGYUQ",
   "master_node":"Vy3KxqcHQdm4cIM22U1ewA",
}
………………………………………………
```

**集群统计**

 这个API通过使用stats关键字来帮助检索关于集群的统计信息。该应用编程接口返回碎片号、存储大小、内存使用情况、节点数量、角色、操作系统和文件系统。例如，

```
GET /_cluster/stats
```

响应

```
………………………………………………
{
   "timestamp":1454496710020, "cluster_name":"elasticsearch", "status":"yellow",
   "indices":{
      "count":5, "shards":{
         "total":23, "primaries":23, "replication":0.0,"
      }
   }
}
………………………………………………
```

**挂起群集任务**

 此API用于监控任何集群中的挂起任务。任务类似于创建索引、更新映射、分配碎片、失败碎片等。例如，

```
GET /_cluster/pending_tasks
```

**集群重路由**

 此API用于将碎片从一个节点移动到另一个节点，或者取消任何分配或分配任何未分配的碎片。例如，

```
POST /_cluster/reroute
{
   "commands" : [ 
      {
         "move" :
         {
            "index" : "schools", "shard" : 2,
            "from_node" : "nodea", "to_node" : "nodeb"
         }
      },
      {
         "allocate" : {
            "index" : "test", "shard" : 1, "node" : "nodec"
         }
      }
   ]
}
```

**集群更新设置**

 此API允许您使用settings关键字更新集群的设置。有两种类型的设置—持久性设置(在重启时应用)和瞬态设置(在完全集群重启后不存在)。

**节点统计**

 该API用于检索集群中一个或多个节点的统计信息。节点统计几乎与集群相同。例如，

```
GET /_nodes/stats
```

响应

```
………………………………………………
{
   "cluster_name":"elasticsearch", "nodes":{
      "Vy3KxqcHQdm4cIM22U1ewA":{
         "timestamp":1454497097572, "name":"Red Guardian", 
         "transport_address":"127.0.0.1:9300", "host":"127.0.0.1", "ip":["127.0.0.1:9300",
      }
   }
}
………………………………………………
```

**节点热线程**

该API帮助您检索集群中每个节点上当前热线程的信息。例如，

```
GET /_nodes/hot_threads
```

响应

```
………………………………………………
::: {Red Guardian} {Vy3KxqcHQdm4cIM22U1ewA} {127.0.0.1}{127.0.0.1:9300}Hot threads at 
   2016-02-03T10:59:48.856Z, interval = 500ms, busiestThreads = 3, 
   ignoreIdleThreads = true:0.0% (0s out of 500ms) cpu usage by thread 'Attach Listener'
      unique snapshot
      unique snapshot
………………………………………………
```