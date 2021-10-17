# ### [elasticsearch7 基本语法](https://blog.csdn.net/z457181562/article/details/93470152) csdn

[zhyingke](https://blog.csdn.net/z457181562) 2019-06-25 10:50:03



注意：
megacorp：是index
employee：是type
使用es版本是7.1.1
在postman下进行的模拟
下面的方面省略了host，完整的是类似 http://127.0.0.1:9200/website/blog/123

参考：elasticsearch权威指南
通过id进行查询
GET megacorp/employee/1?_source=last_name,age
1
_id=1 且只显示last_name,age字段

全文搜索
既能匹配出完整完整的rock climbing,又能匹配出带有rock 或climbing 词的

GET /megacorp/employee/_search
{
    "query" : {
        "match" : {
            "about" : "rock climbing"
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
短语搜索
想要确切的匹配若干个单词或者短语(phrases),match_phrase查询即可

GET /megacorp/employee/_search
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
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
多条件搜索
filter是区间过滤器(range filter),下面匹配的是:姓氏为“Smith”的员工，且年龄大于30岁的员工

GET megacorp/employee/_search
    {
        "query" : {
            "bool" : {
                "filter" : {
                    "range" : {
                        "age" : { "gt" : 30 } 
                    }
                },
                "must" : {
                    "match" : {
                        "last_name" : "smith" 
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
聚合搜索
类似于group by,下面会把interests的进行group by,获得不同兴趣的数量

GET megacorp/employee/_search
{
    "aggs": {
    "all_interests": {
      "terms": { "field": "interests.keyword" }
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
获得不同兴趣的平均年龄(进行两次分级汇总)

 GET megacorp/employee/_search
   {
  "aggs": {
    "all_interests": {
      "terms": { 
      	"field": "interests.keyword" 
      },
      "aggs":{
      	"avg_age":{
      		"avg":{
      			"field":"age"
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
新增数据
如果_id=123已经存在，则会覆盖老的，注意这里的覆盖是新的完全替代了老的值，不会仅仅替代新老共同的字段，
返回值result的val也会不一样

PUT  megacorp/employee/123
{
    "first_name" :  "z",
    "last_name" :   "yk",
    "age" :         18,
    "about":        "I like to study",
    "interests":  [ "music" ]
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
返回值：

{
    "_index": "megacorp",
    "_type": "employee",
    "_id": "123",
    "_version": 1,//操作的次数，每操作一次这个数据就+1
    "result": "created",//因为是之前没有的数据所以是created，如果再请求一次会变成updated
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 3,
    "_primary_term": 1
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
防重复新增数据

PUT website/blog/1/_create
{
  "title": "My first blog entry",
  "text":  "Just trying this out..."
}
1
2
3
4
5
局部更新
相同字段会覆盖，没有的字段会新增

POST website/blog/1/_update
// doc是固定语法，doc的值是要更新的内容
{
   "doc" : {
      "tags" : [ "testing" ],
      "views": 0
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
查询结果
website/blog/1
{
    "_index": "website",
    "_type": "blog",
    "_id": "1",
    "_version": 6,
    "_seq_no": 7,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "title": "My first external blog entry",
        "text": "Starting to get the hang of this...",
        //新增的值
        "views": 0,
        "tags": [
            "testing"
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
使用脚本更新

    对某个字段值进行数学运算
      POST  website/blog/1/_update
        {
           "script" : "ctx._source.views+=1" //views+1
        }
查询返回结果views这个字段会比之前多1
1
2
3
4
5
6
往数组里加字符串

 POST  website/blog/2/_update
 {
	"script":{
		 "source" : "ctx._source.tags.add (params.new_tag)", // params.new_tag是取参数值的意思
		  "params" : {
		      "new_tag" : "search"
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
查询更新之后的结果(部分)

{
    "_source": {
        "title": "My first external blog entry",
        "text": "Starting to get the hang of this...",
        "views": 1,
        "tags": [
            "testing",
            "search"  //这个是新添加的值
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
添加到字符串

POST website/blog/2/_update
    {
    	"script":{
    		 "source" : "ctx._source.title += (params.new_tag)",
    		  "params" : {
    		      "new_tag" : "search"
    		   }
    	}
    }
返回结果

 {
    "_source": {
        "title": "My first external blog entrysearch", // 这个search是新添加的
        "text": "Starting to get the hang of this...",
        "views": 1,
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
根据条件更新

 POST website/blog/2/_update
     {
        	"script":{
        		 "source" : "ctx.op = ctx._source.views == params.count ? 'delete':'none'", //如果views=count的值则删除
        		  "params" : {
        		      "count" : 0
        		   }
        	}
        }
    
成功的时候返回successful的数量>=1
{
    "_index": "website",
    "_type": "blog",
    "_id": "2",
    "_version": 4,
    "result": "deleted",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 12,
    "_primary_term": 1
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
更新可能不存在的数据

POST website/blog/2/_update
{
   "script" : "ctx._source.views+=1",
   "upsert": {
       "views": 1  //不存在的时候的初始值
   }
}
1
2
3
4
5
6
7
批量搜索
一次查询多条数据
POST  /_mget
{
   "docs" : [
      {
         "_index" : "website",// 要搜索的index
         "_type" :  "blog", //要搜索的type
         "_id" :    2 //要搜索的id
      },
      {
         "_index" : "website",
         "_type" :  "blog",
         "_id" :    1,
         "_source": "views" // 具体要展示的数据
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
在url里指定index和type，多个id可以用ids
POST  website/blog/_mget
{
   "ids" : [ "2", "1" ]
}
————————————————
版权声明：本文为CSDN博主「zhyingke」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/z457181562/article/details/93470152

