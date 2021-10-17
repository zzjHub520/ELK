ElasticSearch(二) 环境搭建

Clown95 2019-06-07 17:31:48  160  收藏
分类专栏： Elasticsearch 入门 文章标签： ElasticSearch
版权

Elasticsearch 入门
专栏收录该内容
7 篇文章0 订阅
订阅专栏
title: ElasticSearch(二) 环境搭建
tags: Elastic Stack
author: Clown95

ElasticSearch环境搭建
我们使用的环境centos7以及elasticsearch6.1.1

我们将禁用 CentOS 7 服务器上的 SELinux。 编辑 SELinux 配置文件。

sudo vim /etc/sysconfig/selinux
1
将 SELINUX 的值从enforcing改成disabled，改完重启系统。

接着我们更换阿里云软件源

sudo curl -o /etc/yum.repos.d/CentOS-Base.repo http:   mirrors.aliyun.com/repo/Centos-7.repo
1
更换完成后，我们清除下yum缓存

yum clean all
1
安装JDK
删除OpenJDK
CentOS系统自带OpenJDK,使用命令查看

java -version
1


OpenJDK也能用于环境配置，如果不想折腾的可以用OpenJDK,但是官方建议安装Oracle的JDK8.

首先我们使用命令查看 OpenJDK的相关文件：
rpm -qa | grep java
1


接着我们使用命令删除这些文件：
rpm -e --nodeps `rpm -qa | grep java`
1
直接输入命令可能会提示权限不够，我们可以使用sudo 或者使用root账号来执行命令。

命令完成后我们再次使用java -version 来验证下是否删除。
安装JDK8
从Oracle 下载JDK1.8

Oracal账号(仅用于JDK下载)
用户名：541509124@qq.com
密码：LR4ever.1314

使用rmp 命令安装

sudo rpm -ivh jdk-8u211-linux-x64.rpm
1
最后，检查 java JDK 版本，确保它正常工作

java -version
1
安装Elasticsearch
Elasticsearch安装
在安装 Elasticsearch 之前，将 elastic.co 的密钥添加到服务器。
sudo rpm --import https:   artifacts.elastic.co/GPG-KEY-elasticsearch
1
如果执行上面的命令出现Peer reports incompatible or unsupported protocol version 这种错误。
先执行下下面的命令,然后再添加elastic.co 的密钥。
sudo yum update -y nss curl libcurl
1
接下来，使用 wget下载 Elasticsearch 6.1.1.
wget https:   artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.1.1.rpm
1
然后使用 rpm 安装它
sudo rpm -ivh elasticsearch-6.1.1.rpm
1


Elasticsearch文件结构
Elasticsearch 已经安装好了，下面我们简单的看下es目录结构：

elasticsearch                     -- path.home, es的安装目录
├─bin                             -- ${path.home}/bin, 启动脚本方式目录
├─config                          -- ${path.home}/config, 配置文件目录
├─data                            -- ${path.home}/data, 数据存放目录
│  └─elasticsearch                -- ${path.home}/data/${cluster.name}
├─lib                             -- ${path.home}/lib, 运行程序目录
├─logs                            -- ${path.home}/logs, log目录 
└─plugins                         -- ${path.home}/plugins, 插件目录
    ├─head
    │  └─...
    └─marvel
        └─...
1
2
3
4
5
6
7
8
9
10
11
12
Elasticsearch 配置文件
Elasticsearch配置文件位于config目录中

进入配置目录编辑 elasticsaerch.yml 配置文件。
sudo vim /etc/elasticsearch/elasticsearch.yml
1
在 Network 块中，取消注释 network.host 和 http.port 行，并且修改
192.168.0.1 为0.0.0.0 ，方便我们可以外网访问，
network.host: 0.0.0.0
http.port: 9200
1
2
注意： 改成0.0.0.0 是很愚蠢的行为，因为这样很不安全，最好改成自己Centos的外网IP



使用wq! 保存文件并退出编辑器。Elasticsearch 配置到此结束。Elasticsearch 将在本机的 9200 端口运行，
其他配置文件说明
elasticsearch.yml es的相关配置
- cluster.name 集群名称,以此作为是否同一集群的判断条件
- node.name 节点名称,以此作为集群中不同节点的区分条件
- network.host / http.port 网络地址和端口,用于http和transport服务使用
- path.data 数据存储地址
- path.log 日志存储地址

同一集群的节点cluster.name 必须一样。

jvm.options jvm的相关参数
jvm.options 主要是配置JVM相关的东西，我们最主要的就是配置heap的大小,配置默认的是2g。

-Xms2g
-Xmx2g
1
2
可能有的同学可能笔记本或者虚拟机的内存不大，但是配置文件中heap分配的比较大，会照成Eelasticsearch运行不起来的情况，这时候我们只需要把heap改小就行。

检查Elasticsearch并设置开机启动
重新加载 systemd，将 Elasticsearch 置为开机启动.
sudo systemctl daemon-reload
sudo systemctl enable elasticsearch
1
2
接着我们启动Eelasticsearch
sudo systemctl start elasticsearch
1
等待 Eelasticsearch 启动成功，然后检查服务器上打开的端口，确保 9200 端口的状态是LISTEN
netstat -plntu
1
如果9200端口没有被监听，使用下面命令查看 es 状态，查找错误。
systemctl status elasticsearch -l
1


使用curl 命令检查是否出现json文件，如果出现即代表启动成功
curl -X GET http://localhost:9200
1
安装Kibana
下载并安装Kibana
用 wget下载 Kibana 6.1.1
wget https:   artifacts.elastic.co/downloads/kibana/kibana-6.1.1-x86_64.rpm
1
然后使用 rpm命令安装：
sudo rpm -ivh kibana-6.1.1-x86_64.rpm
1
配置Kibana
进入配置目录编辑 kibana.yml 配置文件
sudo vim /etc/kibana/kibana.yml
1
去掉配置文件中server.port、server.host 和elasticsearch.url 这三行的注释。
server.port: 5601
server.host: "localhost"
elasticsearch.url: "http://localhost:9200"
1
2
3

3. 保存并退出。

检查Kibana并设置开机启动
将 Kibana 设为开机启动，并且启动 Kibana 。
sudo systemctl enable kibana
sudo systemctl start kibana
1
2
检查 Kibana是否运行在端口 5601 上。
netstat -plntu
1


浏览器输入http://localhost:5601检查安装

安装中文分词器
常见的中文分词系统有IK 和jieba，这里我们选择IK ,具体的介绍我们后面再说。

首先我们使用elasticsearch-plugin 来安装IK 。
sudo /usr/share/elasticsearch/bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.1.1/elasticsearch-analysis-ik-6.1.1.zip
1
注意： IK的版本要和ES的版本完全匹配
/usr/share/elasticsearch 是你ES所在的安装目录，请根据自己的情况调整。

最终终端出现Installed analysis-ik,代表安装成功。

安装完毕后我们必须要重新启动下ES

sudo systemctl restart elasticsearch
1
附加：配置chrome+sense插件 (可替代kibana)
因为我们已经给es配置了外网访问，这时候我们就可以使用我们自己的生产环境来使用es。 这里我给大家推荐一个组合 chrome +sense插件

我们首先下载 sense 插件

链接:https://pan.baidu.com/s/1vbgfBH5Cv6JzXRdpctO0nA  密码:rc7m
1
解压文件sense文件

进入sense目录，找到index.html，修改localhost 为你es环境的外网IP

 <input id="es_server" type="text" class="span5" value="localhost:9200"/>
1
进入扩展程序中心，启用开发者模式，点击加载已解压的扩展程序，选择刚才的文件夹就行了。

这时候我们打开chrome浏览器，点击sense 可以看到以下界面

————————————————
版权声明：本文为CSDN博主「Clown95」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/yang731227/article/details/91129131