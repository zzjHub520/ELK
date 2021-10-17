elasticsearch7.x基本搜索（搜索）

好大一个消失点 2020-04-20 14:46:53  1355  收藏 3
分类专栏： ElasticSearch 文章标签： elasticsearch es 搜索引擎
版权

ElasticSearch
专栏收录该内容
4 篇文章0 订阅
订阅专栏
match分词搜索
全文搜索

match_all 搜索全部
GET xxx/_search
{
	"query":{
		"match_all":{}
	}
}
1
2
3
4
5
6
匹配单字段搜索

field 字段名
GET xxx/_search
{
	"query":{
		"match":{
			"field":"内容"
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
匹配单字段多词组搜索

filed 字段名，词组使用空格隔开
注意：数据应该保存成为一个数组

GET xxx/_search
{
	"query":{
		"match":{
			"field":"内容1 内容2 内容3"
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
匹配多字段搜索

must相当于MySQL条件中的 AND
should相当于MySQL条件中的 OR
must

GET xxx/_search
{
	"query":{
		"bool":{
			"must":[
			{
				"match":{
					"field1":"content"
				}
			},
			{
				"match":{
					"field2":"content"
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
should

GET xxx/_search
{
	"query":{
		"bool":{
			"should":[
				{
					"match":{
						"field1":"content"
					}
				},
				{
					"match":{
						"field2":"content"
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
匹配多字段搜索相同内容

multi_match 多字段
query 搜索内容
fields 搜索的字段
operator 字段的匹配方式 ，属性有OR, AND
GET xxx/_search
{
	"query":{
		"multi_match": {
	        "query": "content",
	        "fields": ["field_name1", "field_name2"],
	        "operator": "OR"
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
搜索分页

from 起始数
size 获得的数量
GET xxx/_search
{
	"query":{
		"match_all":{}
	},
	"from":0,
	"size":10
}
1
2
3
4
5
6
7
8
搜索过滤

filter 过滤
field 要过滤的字段名
gte 大于等于 附加 gt 大于
lte 小于等于 附加 lt 小于
GET xxx/_search
{
	"query":{
		"bool":{
			"should":[
				{
					"match_all":{}
				}
			],
			"filter":{
				"range":{
					"field":{
						"gte":22,
						"lte":23
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
term精确搜索
精确搜索

term 精确查找（单个）
terms 精确查找（多个）
注意：这里的字段类型应保证为非分词的类型，如：keyword

term

{
	"query":{
		"term": {
	        "_id":1
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
terms

{
	"query":{
		"terms": {
	        "_id":[1,2,3]
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
term高亮搜索
高亮搜索

highlight 高亮查找
pre_tags 标签前缀
post_tags 标签后缀
fields 规定的字段，支持多个
注意：如果不声明前缀和后缀，那边默认使用 <em></em>

{
	"query":{
		"match":{
			"field":"content"
		}
	},
	"highlight":{
		"pre_tags":"<p class = \"text_high_light\">",
		"post_tags":"</p>",
		"fields":{
			"field":{}
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
知识源于学习
————————————————
版权声明：本文为CSDN博主「好大一个消失点」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_42681513/article/details/105632063