# Elasticsearch集群API

------

此API用于获取有关集群及其节点的信息，并对其进行更改。 对于调用此API，需要指定节点名称，地址或`_local`。 例如，

```
GET http://localhost:9200/_nodes/_local
```

或者

```
Get http://localhost:9200/_nodes/127.0.0.1
```

**响应**

```json
... ...
{
   "cluster_name":"elasticsearch", "nodes":{
      "Vy3KxqcHQdm4cIM22U1ewA":{
         "name":"Red Guardian", "transport_address":"127.0.0.1:9300", 
         "host":"127.0.0.1", "ip":"127.0.0.1", "version":"2.1.1", 
         "build":"40e2c53", "http_address":"127.0.0.1:9200",
      }
   }
}
... ...
```

### 集群运行状况

此API用于通过追加`health`关键字来获取集群运行状况的状态。 例如，

```
GET http://localhost:9200/_cluster/health
```

**响应**

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

### **集群状态**

此API用于通过附加’`state`‘关键字URL来获取有关集群的状态信息。状态信息包含:版本，主节点，其他节点，路由表，元数据和块。 例如，

```
GET http://localhost:9200/_cluster/state 10. Elasticsearch — Cluster APIs
```

**响应**

```
{
   "cluster_name":"elasticsearch", "version":27, "state_uuid":"B3P7uHGKQUGsSsiX2rGYUQ",
   "master_node":"Vy3KxqcHQdm4cIM22U1ewA",

}
```

### 群集统计信息

此API有助于使用’`stats`‘关键字检索有关集群的统计信息。 此API返回碎片编号，存储大小，内存使用情况，节点数，角色，操作系统和文件系统。 例如，

```
GET http://localhost:9200/_cluster/stats
```

**响应**

```
{
   "timestamp":1454496710020, "cluster_name":"elasticsearch", "status":"yellow",
   "indices":{
      "count":5, "shards":{
         "total":23, "primaries":23, "replication":0.0,"
      }
   }
}
```

### 正在等待的群集任务

此API用于监视任何集群中的挂起任务。任务类似于创建索引，更新映射，分配碎片，故障碎片等。例如，

```
GET http://localhost:9200/_cluster/pending_tasks
```

### 集群重新路由

此API用于将分片从一个节点移动到另一个节点，或者用于取消任何分配或分配任何未分配的碎片。 例如，

```
POST http://localhost:9200/_cluster/reroute
```

**请求正文**

```
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

### 群集更新设置

此API允许使用`settings`关键字更新群集的设置。有两种类型的设置 - `persistent`(在重新启动时应用)和`transient`(在完全群集重新启动后不会生存)。

### 节点统计

此API用于检索集群的一个节点的统计信息。节点状态与集群几乎相同。 例如，

```
GET http://localhost:9200/_nodes/stats
```

**响应**

```json
{
   "cluster_name":"elasticsearch", "nodes":{
      "Vy3KxqcHQdm4cIM22U1ewA":{
         "timestamp":1454497097572, "name":"Red Guardian", 
         "transport_address":"127.0.0.1:9300", "host":"127.0.0.1", "ip":["127.0.0.1:9300",
      }
   }
}
```

### 节点hot_threads

此API可用于检索有关集群中每个节点上当前热线程的信息。 例如，

```
GET http://localhost:9200/_nodes/hot_threads
```

**响应**

```json
{Red Guardian} {Vy3KxqcHQdm4cIM22U1ewA} {127.0.0.1}{127.0.0.1:9300}Hot threads at 
   2017-02-01T10:59:48.856Z, interval = 500ms, busiestThreads = 3, 
   ignoreIdleThreads = true:0.0% (0s out of 500ms) cpu usage by thread 'Attach Listener'
      unique snapshot
      unique snapshot
```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-cluster-apis.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处