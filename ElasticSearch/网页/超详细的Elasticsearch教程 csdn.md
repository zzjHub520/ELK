# [超详细的Elasticsearch教程](https://blog.csdn.net/weixin_43908333/article/details/107337640) csdn

[飞奔的嗨少](https://blog.csdn.net/weixin_43908333)

一、Elasticsearch介绍和安装
用户访问我们的首页，一般都会直接搜索来寻找自己想要购买的商品。而商品的数量非常多，而且分类繁杂。如何能正确的显示出用户想要的商品，并进行合理的过滤，尽快促成交易，是搜索系统要研究的核心。面对这样复杂的搜索业务和数据量，使用传统数据库搜索就显得力不从心，一般我们都会使用全文检索技术，比如之前大家学习过的Solr。
不过今天，我们要讲的是另一个全文检索技术：Elasticsearch。

1.1 Elasticsearch简介
Elastic官网：https://www.elastic.co/cn/

Elastic有一条完整的产品线及解决方案：Elasticsearch、Kibana、Logstash等，前面说的三个就是大家常说的ELK技术栈

1.2 Elasticsearch下载
Elasticsearch官网：https://www.elastic.co/cn/products/elasticsearch

如上所述，Elasticsearch具备以下特点：

分布式，无需人工搭建集群（solr就需要人为配置，使用Zookeeper作为注册中心）
Restful风格，一切API都遵循Rest原则，容易上手
近实时搜索，数据更新在Elasticsearch中几乎是完全同步的。
目前Elasticsearch最新的版本是6.3.1，我们就使用5.6.8

需要虚拟机JDK1.8及以上。


百度网盘--本篇文章所用资料下载地址
链接：https://pan.baidu.com/s/14p-i1BcqaHw0qgVtCrv70Q 
提取码：lv3y
1
2
1.3 Elasticsearch 安装
1.1.3.1安装ES服务

Window版的ElasticSearch的安装很简单，类似Window版的Tomcat，解压开即安装完毕，解压后的ElasticSearch 的目录结构如下。

修改elasticsearch配置文件：conﬁg/elasticsearch.yml，增加以下两句命令，此步为允许elasticsearch跨越访问。

http:
  cors:
    enabled : true
    allow-origin : "*"
1
2
3
4
1.3.2 启动es服务
点击ElasticSearch下的bin目录下的elasticsearch.bat启动，控制台显示的日志信息如下:

如果控制台出现:

注意：9300是tcp通讯端口，集群间和TCPClient都执行该端口，9200是http协议的RESTful接口。通过浏览器访问ElasticSearch服务器，看到如下返回的json信息，代表服务启动成功

注意：ElasticSearch是使用java开发的，且本版本的es需要的jdk版本要是1.8以上，所以安装ElasticSearch之前保证JDK1.8+安装完毕，并正否则启动ElasticSearch失败.

1.3.3 安装ES图形化界面插件
ElasticSearch不同于Solr自带图形化界面，我们可以通过安装ElasticSearch的head插件，完成图形化界面的效果，完成索引数据的查看。安装插件的方式有两种，在线安装和本地安装。本文档采用本地安装方式进行head插件的安装。

elasticsearch-5-*以上版本安装head需要安装node和grunt。

1）下载head插件：https://github.com/mobz/elasticsearch-head
在资料中已经提供了elasticsearch-head-master插件压缩包：

2）将elasticsearch-head-master压缩包解压到任意目录，但是要和elasticsearch的安装目录区别开
解压后的目录结构如下:

3）下载nodejs：https://nodejs.org/en/download/在资料中已经提供了nodejs安装程序

如果没有安装node环境，请参考以下步骤:
双击安装程序，步骤截图如下:




安装完毕，可以通过cmd控制台输入：node -v 查看版本号


4）将grunt安装为全局命令 ，Grunt是基于Node.js的项目构建工具,切换到elasticsearch-head-master目录下面 使用cmd运行:

npm install -g grunt-cli
1

5）进入elasticsearch-head-master目录启动head，在命令提示符下输入命令

npm install
1

需要花费一点时间下载。
安装成功之后，执行 grunt server命令。

如果成功访问http://localhost:9100说明安装成功。


1.3.4 中文分词器的集成
将资料的ik中文分词器压缩包解压

将解压之后的目录拷贝至elasticsearch安装包的plugin目录下面,并重命名为ik-analyzer，然后重启动ElasticSearch，即可加载IK分词器。

IK提供了两个分词算法ik_smart 和 ik_max_word 其中 ik_smart 为最少切分，ik_max_word为最细粒度划分

二、ElasticSearch相关概念(术语)
Elasticsearch是面向文档(document oriented)的，这意味着它可以存储整个对象或文档(document)。然而它不仅仅是存储，还会索引(index)每个文档的内容使之可以被搜索。在Elasticsearch中，你可以对文档（而非成行成列的 数据）进行索引、搜索、排序、过滤。Elasticsearch比传统关系型数据库如下：


2.2 Elasticsearch核心概念
2.2.1 索引index
一个索引就是一个拥有几分相似特征的文档的集合。比如说，你可以有一个客户数据的索引，另一个产品目录的索引，还有一个订单数据的索引。一个索引由一个名字来标识（必须全部是小写字母的），并且当我们要对对应于这个索引中的文档进行索引、搜索、更新和删除的时候，都要使用到这个名字。在一个集群中，可以定义任意多的索引。

2.2.2 类型type
在一个索引中，你可以定义一种或多种类型。一个类型是你的索引的一个逻辑上的分类/分区，其语义完全由你来定。通常，会为具有一组共同字段的文档定义一个类型。比如说，我们假设你运营一个博客平台并且将你所有的数据存储到一个索引中。在这个索引中，你可以为用户数据定义一个类型，为博客数据定义另一个类型，当然，也可 以为评论数据定义另一个类型。

2.2.3 字段Field
相当于是数据表的字段，对文档数据根据不同属性进行的分类标识
类型名称：就是前面将的type的概念，类似于数据库中的不同表字段名：任意填写 ，可以指定许多属性，例如：

type：类型，可以是text、long、short、date、integer、object等
index：是否索引，默认为true
store：是否存储，默认为false
analyzer：分词器，这里的ik_max_word即使用ik分词器
1
2
3
4
2.2.4 映射mapping
mapping是处理数据的方式和规则方面做一些限制，如某个字段的数据类型、默认值、分析器、是否被索引等等， 这些都是映射里面可以设置的，其它就是处理es里面数据的一些使用规则设置也叫做映射，按着最优规则处理数据对性能提高很大，因此才需要建立映射，并且需要思考如何建立映射才能对性能更好。

2.2.5 文档document
一个文档是一个可被索引的基础信息单元。比如，你可以拥有某一个客户的文档，某一个产品的一个文档，当然， 也可以拥有某个订单的一个文档。文档以JSON（Javascript Object Notation）格式来表示，而JSON是一个到处存 在的互联网数据交互格式。在一个index/type里面，你可以存储任意多的文档。注意，尽管一个文档，物理上存在于一个索引之中，文档必须 被索引/赋予一个索引的type。

2.2.6 接近实时 NRT
Elasticsearch是一个接近实时的搜索平台。这意味着，从索引一个文档直到这个文档能够被搜索到有一个轻微的延迟（通常是1秒以内）

2.2.7 集群cluster
一个集群就是由一个或多个节点组织在一起，它们共同持有整个的数据，并一起提供索引和搜索功能。一个集群由一个唯一的名字标识，这个名字默认就是“elasticsearch”。这个名字是重要的，因为一个节点只能通过指定某个集群的名字，来加入这个集群

2.2.8 节点node
一个节点是集群中的一个服务器，作为集群的一部分，它存储数据，参与集群的索引和搜索功能。和集群类似，一 个节点也是由一个名字来标识的，默认情况下，这个名字是一个随机的漫威漫画角色的名字，这个名字会在启动的 时候赋予节点。这个名字对于管理工作来说挺重要的，因为在这个管理过程中，你会去确定网络中的哪些服务器对 应于Elasticsearch集群中的哪些节点。

一个节点可以通过配置集群名称的方式来加入一个指定的集群。默认情况下，每个节点都会被安排加入到一个叫 做“elasticsearch”的集群中，这意味着，如果你在你的网络中启动了若干个节点，并假定它们能够相互发现彼此， 它们将会自动地形成并加入到一个叫做“elasticsearch”的集群中。

在一个集群里，只要你想，可以拥有任意多个节点。而且，如果当前你的网络中没有运行任何Elasticsearch节点， 这时启动一个节点，会默认创建并加入一个叫做“elasticsearch”的集群。

2.2.9 分片和复制shards&replicas
一个索引可以存储超出单个结点硬件限制的大量数据。比如，一个具有10亿文档的索引占据1TB的磁盘空间，而任一节点都没有这样大的磁盘空间；或者单个节点处理搜索请求，响应太慢。为了解决这个问题，Elasticsearch提供了将索引划分成多份的能力，这些份就叫做分片。当你创建一个索引的时候，你可以指定你想要的分片的数量。每 个分片本身也是一个功能完善并且独立的“索引”，这个“索引”可以被放置到集群中的任何节点上。分片很重要，主要有两方面的原因： 1）允许你水平分割/扩展你的内容容量。 2）允许你在分片（潜在地，位于多个节点上）之上进行分布式的、并行的操作，进而提高性能/吞吐量。 至于一个分片怎样分布，它的文档怎样聚合回搜索请求，是完全由Elasticsearch管理的，对于作为用户的你来说， 这些都是透明的。

在一个网络/云的环境里，失败随时都可能发生，在某个分片/节点不知怎么的就处于离线状态，或者由于任何原因消失了，这种情况下，有一个故障转移机制是非常有用并且是强烈推荐的。为此目的，Elasticsearch允许你创建分片的一份或多份拷贝，这些拷贝叫做复制分片，或者直接叫复制。

复制之所以重要，有两个主要原因： 在分片/节点失败的情况下，提供了高可用性。因为这个原因，注意到复制分片从不与原/主要（original/primary）分片置于同一节点上是非常重要的。扩展你的搜索量/吞吐量，因为搜索可以在所有的复制上并行运行。总之，每个索引可以被分成多个分片。一个索引也可以被复制0次（意思是没有复制） 或多次。一旦复制了，每个索引就有了主分片（作为复制源的原来的分片）和复制分片（主分片的拷贝）之别。分片和复制的数量可以在索引创建的时候指定。在索引创建之后，你可以在任何时候动态地改变复制的数量，但是你事后不能改变分片的数量。
默认情况下，Elasticsearch中的每个索引被分片5个主分片和1个复制，这意味着，如果你的集群中至少有两个节 点，你的索引将会有5个主分片和另外5个复制分片（1个完全拷贝），这样的话每个索引总共就有10个分片。

三、使用Postman进行ElasticSearch的客户端操作
3.1 Postman工具的安装
Postman中文版是postman这款强大网页调试工具的windows客户端，提供功能强大的Web API & HTTP 请求调 试。软件功能非常强大，界面简洁明晰、操作方便快捷，设计得很人性化。Postman中文版能够发送任何类型的 HTTP 请求 (GET, HEAD, POST, PUT…)，且可以附带任何数量的参数。我们已经提供了Postman工具安装包。


3.2 注册Postman



3.3 使用Postman工具进行Restful接口访问
3.3.1 创建索引index和映射mapping

请求成功之后返回的数据

回到elasticsearch-head查看


请求url路径:

http://localhost:9200/webshop
1
请求实体内容:

{
	"mappings":{
		"phone":{
			"properties":{
				"id":{
					"type":"long",
					"index":true,
					"store":true
				},
				"title":{
					"type":"text",
					"analyzer":"ik_max_word"
				},
				"images": {
    				 "type": "keyword",
    				 "index": "false"
    			},
    			"price": {
			         "type": "float"
			    }
			}
		}
	}
}
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
13
14
15
16
17
18
19
20
21
22
23
24
3.3.2 删除索引

请求url：

http://127.0.0.1:9200/webshop
1

回到elasticsearch-head查看，之前存在的el_demo1索引库已经不存在。

3.3.3 先创建索引，后创建映射
我们可以在创建索引时设置mapping信息，当然也可以先创建索引然后再设置mapping。 在上一个步骤中不设置maping信息，直接使用put方法创建一个索引，然后设置mapping信息。
1)先创建一个空索引

2)设置mapping

请求的url:

http://127.0.0.1:9200/webshop/goods/_mapping
1
请求的实体内容:

{
	"goods":{
		"properties":{
			"id":{
				"type":"long",
				"index":true,
				"store":true
			},
			"title":{
				"type":"text",
				"analyzer":"ik_max_word"
			},
			"images": {
				 "type": "keyword",
				 "index": "false"
			},
			"price": {
		         "type": "float"
		    }
		}
	}
}
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
13
14
15
16
17
18
19
20
21
22
回到elasticsearch-head查看:

3.3.4 创建document(往type中添加原始数据，相当于往数据库表中添加具体的值一样)

1.指定document的id
请求url:

http://127.0.0.1:9200/webshop/goods/1
1
请求实体内容:

{
	"id":10010,
	"title":"小米手机8",
	"price":2500,
	"image":"D:\\images\\xiaomi8.jpg"
}
1
2
3
4
5
6

请求成功的返回结果:

回到elasticsearch-head查看:


2.不指定document的id
请求url:

http://127.0.0.1:9200/webshop/goods
1
请求实体内容:

{
	"id":10011,
	"title":"小米手机6",
	"price":1200,
	"image":"D:\\images\\xiaomi6.jpg"
}
1
2
3
4
5
6

请求成功的返回结果:

回到elasticsearch-head查看:

3.3.5 修改文档document
修改的原理是根据文档id进行修改。如果修改的id不存在，那么就是增加一个新的文档，如果存在就将原来的document进行覆盖。
请求url:

http://127.0.0.1:9200/webshop/goods/1
1
请求实体内容:

{
	"id":10010,
	"title":"redNoteMi8",
	"price":2800,
	"image":"D:\\images\\redNoteMi8.jpg"
}
1
2
3
4
5
6

请求成功的返回结果:

回到elasticsearch-head查看:

3.3.6 删除文档
根据id进行删除，发送的是delete请求。

http://127.0.0.1:9200/webshop/goods/AXNMRwX7IKNxQMWlTDXR
1




3.4 简单的查询
3.4.1 根据文档id进行查询

请求方式get，请求url:

http://127.0.0.1:9200/webshop/goods/1
1
3.4.2 查询文档-querystring查询

请求的方式:POST，请求的url:

http://127.0.0.1:9200/webshop/goods/_search
1
请求实体内容:

{
	"query":{
		"query_string":{
			"default_field":"title",
			"query":"redNoteMi8"
		}
	}
}
1
2
3
4
5
6
7
8

查询的结果:

3.4.3 查询文档-term查询

先通过该url查询text="redNoteMi8"可以拆分的关键字

http://127.0.0.1:9200/_analyze?analyzer=ik_smart&pretty=true&text=“小米手机8”
1


使用url查询

http://127.0.0.1:9200/webshop/goods/_search
1
{
	"query":{
		"term":{
			"title":"小米"
		}
	}
}
1
2
3
4
5
6
7


先看一下小米手机8可以拆分成哪些关键词

http://127.0.0.1:9200/_analyze?analyzer=ik_smart&pretty=true&text=“小米手机8”
1


如果搜索的关键词为小米手机8，那么由于该词不是一个关键词，所以不会查询出来


tips:match在匹配时会对所查找的关键词进行分词，然后按分词匹配查找，而term会直接对关键词进行查找。一般模糊查找的时候，多用match，而精确查找时可以使用term

3.4.4 查询文档-match查询



四、查询详解
4.1 基本查询
基本语法:

这里的query代表一个查询对象，里面可以有不同的查询属性

查询类型：
例如：match_all， match，term ，range ，query_string等等
查询条件：查询条件会根据类型的不同，写法也有差异，后面详细讲解
1
2
3
4.1.1 查询所有（match_all)

url:

http://127.0.0.1:9200/webshop/goods/_search
1
请求体

{
	"query":{
		"match_all":{
			
		}
	}
}
1
2
3
4
5
6
7
query：代表查询对象
match_all：代表查询所有
此时会将索引库里面的所有内容全部查询出来

4.1.2 匹配查询(match)
or关系：match类型查询，会把查询条件进行分词，然后进行查询,多个词条之间是or的关系

{
	"query":{
		"match":{
			"title":"小米手机8"
		}
	}
}
1
2
3
4
5
6
7
此时查询出来的结果:

在上面的案例中，不仅会查询到小米，而且与8相关的都会查询到，多个词之间是or的关系。

4.1.3 AND关系

http://127.0.0.1:9200/webshop/goods/_search
1
{
	"query":{
		"match":{
			"title":{
				"query":"小米手机8",
				"operator":"and"
			}
		}
	}
}
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
此时查询的结果是:
本例中，只有同时包含小米和手机和8的词条才会被搜索到。

or和and之间？

在 or 与 and 间二选一有点过于非黑即白。 如果用户给定的条件分词后有 5 个查询词项，想查找只包含其中 4 个词的文档，该如何处理？将 operator 操作符参数设置成 and 只会将此文档排除。
有时候这正是我们期望的，但在全文搜索的大多数应用场景下，我们既想包含那些可能相关的文档，同时又排除那些不太相关的。换句话说，我们想要处于中间某种结果。
match 查询支持 minimum_should_match 最小匹配参数， 这让我们可以指定必须匹配的词项数用来表示一个文档是否相关。我们可以将其设置为某个具体数字，更常用的做法是将其设置为一个百分数，因为我们无法控制用户搜索时输入的单词数量：


http://127.0.0.1:9200/webshop/goods/_search
1
{
   "query":{
   	   "match":{
   	   	   "title":{
   	   	   		"query":"小米手机电视",
   	   	   		"minimum_should_match": "75%"
   	   	   }
   	   }
   }
}
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

查询结果：

原因:小米手机电视经过分词可以分成小米、手机、电视三个关键词。

也就是说商品的title中至少要匹配其中的3*0.75=2.25个才能显示.
而索引库中的title目前只有小米手机和小米电视。此时他们只有2个关键词匹配，达不到75%的条件
如果将minimum_should_match的百分比进行修改:

{
   "query":{
   	   "match":{
   	   	   "title":{
   	   	   		"query":"小米手机电视",
   	   	   		"minimum_should_match": "50%"
   	   	   }
   	   }
   }
}
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

商品的title中至少要匹配其中的3*0.5=1.5个才能显示
也就是说“小米手机电视”至少需要大于等于1.5个才会显示，比如满足“小米手机”;“小米电视”的title才能够显示出来

4.1.4 多字段查询（multi_match）
multi_match与match类似，不同的是它可以在多个字段中查询.
首先我们新建一个文档，并给他设定副标题

{
   "id":10015,
   "title":"小米手机10",
   "subtitle":"XiaoMi10",
   "price":1850
}
1
2
3
4
5
6


开始执行多条件查询操作

{
    "took": 9,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 4,
        "max_score": 0.25811607,
        "hits": [
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "3",
                "_score": 0.25811607,
                "_source": {
                    "id": 10011,
                    "title": "小米电视",
                    "price": 7800,
                    "images": "http:image.bianyi.com/xiaomiTV.jpg"
                }
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "4",
                "_score": 0.1357528,
                "_source": {
                    "id": 10011,
                    "title": "小米音响",
                    "price": 100,
                    "images": "http:image.bianyi.com/xiaomiMusic.jpg"
                }
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "2",
                "_score": 0.11085624,
                "_source": {
                    "id": 10012,
                    "title": "小米手机8",
                    "price": 6500,
                    "image": "D:\\images\\xiaomi8.jpg"
                }
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "6",
                "_score": 0.11085624,
                "_source": {
                    "id": 10015,
                    "title": "小米手机10",
                    "subtitle": "XiaoMi10",
                    "price": 1850
                }
            }
        ]
    }
}
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
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
本例中，我们会在title字段和subtitle字段中查询小米这个词

4.1.5 词条匹配(term)
term 查询被用于精确值匹配，这些精确值可能是数字、时间、布尔或者那些未分词的字符串,对查询条件有严格的显示，词条必须是最小的不可分割的单位。

{
   "query":{
   	  "term":{
   	  	   "title":"小米手机"
   	  }
   }
}
1
2
3
4
5
6
7
此时”小米手机”这个词条不是最小的不可分割的词条(不是关键词)，所以在查询的时候，没有结果显示

那么再看:

此时价格满足最小的不可分割的要求。所以可以查询出来

4.1.6 多词条精确匹配(terms)
terms 查询和 term 查询一样，但它允许你指定多值进行匹配。如果这个字段包含了指定值中的任何一个值，那么这个文档满足条件：

{
   "query":{
   	  "terms":{
   	  	   "price":[1850,2800,7800]
   	  }
   }
}
1
2
3
4
5
6
7
此时会将价格是指定中的任意一个全部查询出来，最后查询结果:

{
    "took": 15,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 3,
        "max_score": 1,
        "hits": [
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "6",
                "_score": 1,
                "_source": {
                    "id": 10015,
                    "title": "小米手机10",
                    "subtitle": "XiaoMi10",
                    "price": 1850
                }
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "1",
                "_score": 1,
                "_source": {
                    "id": 10010,
                    "title": "redNoteMi8",
                    "price": 2800,
                    "image": "D:\\images\\redNoteMi8.jpg"
                }
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "3",
                "_score": 1,
                "_source": {
                    "id": 10011,
                    "title": "小米电视",
                    "price": 7800,
                    "images": "http:image.bianyi.com/xiaomiTV.jpg"
                }
            }
        ]
    }
}
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
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
五 结果过滤
5.1 指定字段过滤
默认情况下，elasticsearch在搜索的结果中，会把文档中保存在_source的所有字段都返回。
如果我们只想获取其中的部分字段，我们可以添加_source的过滤

示例: 将满足价格是3000,3400,4000的结果查询出来，但是只查询title和price字段

{
	"_source":["title","price"],
	"query":{
   		"terms":{
   	  		 "price":[1850,2800]
   		}
	}
}
1
2
3
4
5
6
7
8


5.2 指定includes和excludes
我们也可以通过：

includes：来指定想要显示的字段
excludes：来指定不想要显示的字段
二者都是可选的。
示例:将包含指定的字段(title,price)显示出来

{
	"_source":{
		"includes":["title","price"]
	},
	"query":{
   		"terms":{
   	  		 "price":[1850,2800]
   		}
	}
}
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

将除image之外的字段全部显示出来

{
	"_source":{
		"excludes":["image"]
	},
	"query":{
   		"terms":{
   	  		 "price":[1850,2800]
   		}
	}
}
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


六、高级查询
6.1 布尔组合(bool)
must： 取交集

{
   "query":{
   		"bool":{
   			"must":[
   			   {
   					"match":{
   			   			"title":"小米"
   			   		}
   			   },
   			   {
   			   		"terms":{
   			   			"price":[1850,2800]
   					}
   			   }
   			]
   		}
   }
}
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
13
14
15
16
17
18

以上查询条件是查询title包含手机，并且价格在1850,2800的数据

should 取并集

{
   "query":{
   		"bool":{
   			"should":[
   			   {
   					"match":{
   			   			"title":"小米"
   			   		}
   			   },
   			   {
   			   		"terms":{
   			   			"price":[1850,2800]
   					}
   			   }
   			]
   		}
   }
}
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
13
14
15
16
17
18
结果：

{
    "took": 4,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 5,
        "max_score": 1.1108563,
        "hits": [
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "6",
                "_score": 1.1108563,
                "_source": {
                    "id": 10015,
                    "title": "小米手机10",
                    "subtitle": "XiaoMi10",
                    "price": 1850
                }
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "1",
                "_score": 1,
                "_source": {
                    "id": 10010,
                    "title": "redNoteMi8",
                    "price": 2800,
                    "image": "D:\\images\\redNoteMi8.jpg"
                }
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "3",
                "_score": 0.25811607,
                "_source": {
                    "id": 10011,
                    "title": "小米电视",
                    "price": 7800,
                    "images": "http:image.bianyi.com/xiaomiTV.jpg"
                }
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "4",
                "_score": 0.1357528,
                "_source": {
                    "id": 10011,
                    "title": "小米音响",
                    "price": 100,
                    "images": "http:image.bianyi.com/xiaomiMusic.jpg"
                }
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "2",
                "_score": 0.11085624,
                "_source": {
                    "id": 10012,
                    "title": "小米手机8",
                    "price": 6500,
                    "image": "D:\\images\\xiaomi8.jpg"
                }
            }
        ]
    }
}
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
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
以上查询条件是查询title包含手机，或者价格在1850,2800的数据

6.2 范围查询(range)
range 查询找出那些落在指定区间内的数字或者时间

{
   "query":{
   	    "range":{
   	    	"price":{
   	    		"gte":2500,
   	    		"lte":4000
   	    	}
   	    }
   }
}
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

range查询允许以下字符：

6.3 模糊查询(fuzzy)
首先我们新增一条数据Apple

{
   "id":10016,
   "title":"apple",
   "price":1450,
   "subtitle":"苹果"
}
1
2
3
4
5
6

fuzzy查询是term查询的模糊等价。它允许用户搜索词条与实际词条的拼写出现偏差，但是偏差的编辑距离不得超过2：
我们可以通过fuzziness来指定允许的编辑距离

{
   "query":{
		"fuzzy":{
			"title":{
				"value":"appl",
				"fuzziness":1
			}
		}
   }
}
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
此时模糊查询。最大偏差设置为1.此时查询结果:有数据

我们再对查询条件进行修改:

此时查询不到数据。


七、过滤(Filter)
所有的查询都会影响到文档的评分及排名。如果我们需要在查询结果中进行过滤，并且不希望过滤条件影响评分，那么就不要把过滤条件作为查询条件来用。而是使用filter方式：

{
    "query":{
    	"bool":{
    		"must":[
    			{
    		    	"match":{
    		    		"title":"小米"
    		    	}
    			}
    	      ],
    	      "filter":{
    	      		"range":{
    	      			"price":{
    	      				"gte":1000,
    	      				"lte":2000
    	      			}
    	      		}
    	      }
    	}
    }
}
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
13
14
15
16
17
18
19
20
21


八、排序
8.1 单字段排序
sort可以让我们按照不同的字段进行排序，并且通过order指定排序的方式

{
    "query":{
    	"match":{
    		"title":"小米"
    	}
	},
	"sort":[
		{
			"price":{
				"order":"desc"
			}
		}
	]
}
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
13
14
结果：按price进行降序

{
    "took": 3,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 4,
        "max_score": null,
        "hits": [
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "3",
                "_score": null,
                "_source": {
                    "id": 10011,
                    "title": "小米电视",
                    "price": 7800,
                    "images": "http:image.bianyi.com/xiaomiTV.jpg"
                },
                "sort": [
                    7800
                ]
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "2",
                "_score": null,
                "_source": {
                    "id": 10012,
                    "title": "小米手机8",
                    "price": 6500,
                    "image": "D:\\images\\xiaomi8.jpg"
                },
                "sort": [
                    6500
                ]
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "6",
                "_score": null,
                "_source": {
                    "id": 10015,
                    "title": "小米手机10",
                    "subtitle": "XiaoMi10",
                    "price": 1850
                },
                "sort": [
                    1850
                ]
            },
            {
                "_index": "webshop",
                "_type": "goods",
                "_id": "4",
                "_score": null,
                "_source": {
                    "id": 10011,
                    "title": "小米音响",
                    "price": 100,
                    "images": "http:image.bianyi.com/xiaomiMusic.jpg"
                },
                "sort": [
                    100
                ]
            }
        ]
    }
}
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
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
8.2 多字段排序
根据价格排序，也根据得分排序

{
    "query":{
    	"match":{
    		"title":"小米"
    	}
	},
	"sort":[
		{
			"price":{
				"order":"desc"
			}
		},
		{
			"_score":{
				"order":"desc"
			}	
		}
	]
}
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
13
14
15
16
17
18
19
九、聚合aggregations
聚合可以让我们极其方便的实现对数据的统计、分析。例如：

什么品牌的手机最受欢迎？
这些手机的平均价格、最高价格、最低价格？
这些手机每月的销售情况如何？
1
2
3
实现这些统计功能的比数据库的sql要方便的多，而且查询速度非常快，可以实现实时搜索效果。

9.1 基本概念



9.2 聚合为桶
首先在索引里面批量插入数据:


http://127.0.0.1:9200/product
1
{
	"mappings":{
		"transactions":{
			"properties":{
    			"price": {
			         "type": "float"
			    },
			    "color":{
			    	"type": "keyword",
    				"index": "false"
			    },
			    "make":{
			    	"type": "keyword",
    				"index": "false"
			    },
			    "sold":{
			    	"type": "keyword",
    				"index": "false"
			    }
			}
		}
	}
}
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
13
14
15
16
17
18
19
20
21
22
23
http://127.0.0.1:9200/product/transactions/1
1
{
	"price":12000,
	"color":"green",
	"make":"toyato",
	"solid":"2014-08-19"
}
1
2
3
4
5
6
http://127.0.0.1:9200/product/transactions/2
1
{
	"price":25000,
	"color":"blue",
	"make":"ford",
	"solid":"2014-02-12"
}
1
2
3
4
5
6
http://127.0.0.1:9200/product/transactions/3
1
{
	"price":30000,
	"color":"green",
	"make":"ford",
	"solid":"2014-05-18"
}
1
2
3
4
5
6
http://127.0.0.1:9200/product/transactions/4
1
{
	"price":15000,
	"color":"blue",
	"make":"toyato",
	"solid":"2014-07-12"
}
1
2
3
4
5
6
http://127.0.0.1:9200/product/transactions/5
1
{
	"price":20000,
	"color":"red",
	"make":"honda",
	"solid":"2014-11-05"
}
1
2
3
4
5
6
http://127.0.0.1:9200/product/transactions/6
1
{
	"price":1000,
	"color":"red",
	"make":"honda",
	"solid":"2014-10-12"
}
1
2
3
4
5
6
http://127.0.0.1:9200/product/transactions/7
1
{
	"price":80000,
	"color":"red",
	"make":"bmw",
	"solid":"2014-01-01"
}
1
2
3
4
5
6
http://127.0.0.1:9200/product/transactions/8
1
{
	"price":25000,
	"color":"green",
	"make":"ford",
	"solid":"2014-05-18"
}
1
2
3
4
5
6

首先，我们按照 汽车的颜色color来划分桶：

{
   "size":0,
   "aggs" : { 
        "popular_colors" : { 
            "terms" : { 
              "field" : "color"
            }
        }
    }
}
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

查询结果:


通过聚合发现，蓝色小汽车销量最低。

9.3 桶内度量
前面的例子告诉我们每个桶里面的文档数量，这很有用。 但通常，我们的应用需要提供更复杂的文档度量。 例如，每种颜色汽车的平均价格是多少？
因此，我们需要告诉Elasticsearch使用哪个字段，使用何种度量方式进行运算，这些信息要嵌套在桶内，度量的运算会基于桶内的文档进行
现在，我们为刚刚的聚合结果添加求价格平均值的度量：
{
   "size":0,
   "aggs" : { 
        "popular_colors" : { 
            "terms" : { 
              "field" : "color"
            },
        	"aggs":{
            	"avg_price": { 
            		"avg": {
                		"field": "price" 
            		}
            	}
            }
        }
    }
}
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
13
14
15
16
17
最后的查询结果:


9.4 桶内嵌套桶
刚刚的案例中，我们在桶内嵌套度量运算。事实上桶不仅可以嵌套运算， 还可以再嵌套其它桶。也就是说在每个分组中，再分更多组。
比如：我们想统计每种颜色的汽车中，分别属于哪个制造商，按照make字段再进行分桶
{
   "size":0,
   "aggs" : { 
        "popular_colors" : { 
            "terms" : { 
              "field" : "color"
            },
        	"aggs":{
            	"avg_price": { 
            		"avg": {
                		"field": "price" 
            		}
            	},
            	"changjia":{
            		"terms":{
            			"field":"make"
            		}
            	}
            }
        }
    }
}
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
13
14
15
16
17
18
19
20
21
22
结果：

{
    "took": 99,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 8,
        "max_score": 0,
        "hits": []
    },
    "aggregations": {
        "popular_colors": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 0,
            "buckets": [
                {
                    "key": "green",
                    "doc_count": 3,
                    "avg_price": {
                        "value": 22333.333333333332
                    },
                    "changjia": {
                        "doc_count_error_upper_bound": 0,
                        "sum_other_doc_count": 0,
                        "buckets": [
                            {
                                "key": "ford",
                                "doc_count": 2
                            },
                            {
                                "key": "toyato",
                                "doc_count": 1
                            }
                        ]
                    }
                },
                {
                    "key": "red",
                    "doc_count": 3,
                    "avg_price": {
                        "value": 33666.666666666664
                    },
                    "changjia": {
                        "doc_count_error_upper_bound": 0,
                        "sum_other_doc_count": 0,
                        "buckets": [
                            {
                                "key": "honda",
                                "doc_count": 2
                            },
                            {
                                "key": "bmw",
                                "doc_count": 1
                            }
                        ]
                    }
                },
                {
                    "key": "blue",
                    "doc_count": 2,
                    "avg_price": {
                        "value": 20000
                    },
                    "changjia": {
                        "doc_count_error_upper_bound": 0,
                        "sum_other_doc_count": 0,
                        "buckets": [
                            {
                                "key": "ford",
                                "doc_count": 1
                            },
                            {
                                "key": "toyato",
                                "doc_count": 1
                            }
                        ]
                    }
                }
            ]
        }
    }
}
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
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86

飞奔的嗨少
关注

0

0

2

————————————————
版权声明：本文为CSDN博主「飞奔的嗨少」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_43908333/article/details/107337640