# Elasticsearch版本之间迁移

------

在任何系统或软件中，当我们升级到较新版本时，需要按照几个步骤来维护应用程序设置，配置，数据和其他事情。 这些步骤是使应用程序在新系统中保持稳定或保持数据的完整性(防止数据损坏)所必需的。

以下是升级Elasticsearch的步骤 -

- 从 http://www.elastic.co/ 阅读了解如何更改文档。
- 在非生产环境(如UAT，E2E，SIT或DEV环境)中测试升级版本。
- 如果没有数据备份，则无法回滚到上一个Elasticsearch版本。 建议在升级到更高版本之前进行数据备份。
- 可以使用完全群集重新启动或滚动升级进行升级。 滚动升级适用于新版本(适用于2.x和更高版本)。当您使用滚动升级方法进行迁移时，不要中断服务。

| 旧版   | 新版 | 升级方法         |
| ------ | ---- | ---------------- |
| 0.90.x | 2.x  | 完全群集重新启动 |
| 1.x    | 2.x  | 完全群集重新启动 |
| 2.x    | 2.y  | 滚动升级(y> x)   |

- 在迁移前进行数据备份，并按照说明执行备份过程。 快照和恢复模块可用于进行备份。此模块可用于创建索引或完整集群的快照，并可存储在远程存储库中。

## 快照和还原模块

在开始备份过程之前，需要在Elasticsearch中注册快照存储库。

```
PUT /_snapshot/backup1
{
   "type": "fs", "settings": {
      ... repository settings ...
   }
}
```

> 注意 - 上面的文本是对`http://localhost:9200/_snapshot/backup1`的HTTP PUT请求(可以是远程服务器的IP地址，而不是`localhost`)。其余的文本是请求正文。可以使用fiddler2和Windows中的其他网络工具。

我们使用共享文件系统(类型：fs)进行备份; 它需要在每个主节点和数据节点中注册。只需要添加具有备份存储库路径的`path.repo`变量作为值。

添加存储库路径后，需要重新启动节点，然后可以通过执行以下命令来执行注册 -

```json
PUT http://localhost:9200/_snapshot/backup1
{
   "type": "fs", "settings": {
      "location": "/mount/backups/backup1", "compress": true
   }
}
```

## 完全群集重新启动

此升级过程包括以下步骤 -

**第1步** - 禁用碎片分配程序，并关闭节点。

```json
PUT http://localhost:9200/_cluster/settings
{
   "persistent": {
      "cluster.routing.allocation.enable": "none"
   }
}
```

在升级`0.90.x`到`1.x`的情况下使用以下请求 -

```json
PUT http://localhost:9200/_cluster/settings
{
   "persistent": {
      "cluster.routing.allocation.disable_allocation": false,
      "cluster.routing.allocation.enable": "none"
   }
}
```

**第2步**  - 对Elasticsearch进行同步刷新 -

```json
POST http://localhost:9200/_flush/synced
```

**第3步** - 在所有节点上，终止所有 `elastic` 服务。
**第4步** - 在每个节点上执行以下操作 -

- 在Debian或Red Hat节点中 - 可以使用rmp或dpkg通过安装新软件包来升级节点。 不要覆盖配置文件。
- 在Windows(zip文件)或UNIX(tar文件) - 提取新版本，而不覆盖`config`目录。 您可以从旧安装复制文件或可以更改`path.conf`或`path.data`。

**第5步**  - 从群集中的主节点(`node.master`设置为`true`，`node.data`设置为`false`的节点)开始重新启动节点。等待一段时间以建立群集。可以通过监视日志或使用以下请求进行检查 -

```
GET _cat/health or http://localhost:9200/_cat/health
GET _cat/nodes or http://localhost:9200/_cat/health
```

**第6步** - 使用`GET _cat/health`请求监视集群的形成进度，并等待黄色响应，响应将是这样 -

```shell
1451295971 17:46:11 elasticsearch yellow 1 1 5 5 0 0 5 0 - 50.0%
```

**第6步** - 启用分片分配过程，这是在**第1步**中禁用的，使用以下请求 -

```shell
PUT http://localhost:9200/_cluster/settings
{
   "persistent": {
      "cluster.routing.allocation.enable": "all"
   }
}
```

在将`0.90.x`升级到`1.x`的情况下，请使用以下请求 -

```shell
PUT http://localhost:9200/_cluster/settings
{
   "persistent": {
      "cluster.routing.allocation.disable_allocation": true,
      "cluster.routing.allocation.enable": "all"
   }
}
```

## 滚动升级

它与完全群集重新启动相同，但**第3步**除外。在此，停止一个节点并进行升级。升级后，重新启动节点并对所有节点重复这些步骤。 启用分片分配过程后，可以通过以下请求监视：

```shell
GET http://localhost:9200/_cat/recovery
```



来源：BootWiki
链接：https://www.bootwiki/elasticsearch/elasticsearch-migrations-between-versions.html
著作权归作者所有
商业转载请联系作者获得授权，非商业转载请注明出处