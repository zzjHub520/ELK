# [elasticsearch 高级搜索示例 es7.0](https://www.cnblogs.com/newguy/p/13434545.html) 博客园

# 1 基础数据

## 1.1 创建索引

```json
PUT mytest
{
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "text",
            "analyzer": "standard"
          }
        }
      },
      "tag": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "content": {
        "type": "text",
        "fields": {
          "std": {
            "type": "text",
            "analyzer": "standard"
          },
          "cn": {
            "type": "text",
            "analyzer": "ik_smart"
          }
        }
      },
      "score": {
        "type": "byte"
      },
      "time":{
        "type": "date"
      }
    }
  },
  "settings": {
    "index": {
      "number_of_shards": "1",
      "number_of_replicas": "0"
    }
  }
}
```

## 1.2 写入数据

```json
POST mytest/_doc/001
{
  "title": "好评不错",
  "tag": "精彩",
  "content": "这里必须有一些内容，来表示这个评论是好评",
  "score": 90,
  "time": 1596441469000
}
POST mytest/_doc/002
{
  "title": "一般评价",
  "tag": "普通",
  "content": "这里可以有一些内容，来表示这个评论是一般的",
  "score": 80,
  "time": 1596355069000
}
POST mytest/_doc/003
{
  "title": "很差的评价",
  "tag": "TCL",
  "content": "这里没有一些内容，来表示这个评论是OK的",
  "score": 20,
  "time": 1596268669000
}
POST mytest/_doc/004
{
  "title": "超级好评",
  "tag": "精彩",
  "content": "这里必须有一些内容，来表示这个评论是好评好评好评好评好评好评好评好评好评好评好评好评好评",
  "score": 2,
  "time": 1596441469000
}
```

# 2 短语匹配

## 2.1 不指定匹配的 fields 时候, 是否会查找全部字段?

不指定 fields 搜索

```json
POST mytest/_search
{
  "explain": true,
  "query": {
    "match": {
      "content": "好评"
    }
  }
}
```

搜索结果为 3 条。在 explain 的结果中可以看到, details 评分统计了两个字段的结果

为了验证上面结果, 在指定域搜索只能看到一条结果

```json
POST mytest/_search
{
  "query": {
    "match": {
      "content.cn": "好评"
    }
  }
}
```

## 2.2 match 和 match_phrase 区别

```json
POST mytest/_search
{
  "explain": false,
  "query": {
    "match": {
      "content.cn":{
        "query": "这里可以有一些内容",
        "analyzer": "ik_smart"
      }
    }
  }
}
```

"这里可以有一些内容" 通过 ik_smart 分词器被分成了 [这里 可以 有 一些 内容]
五个词都放入到 es 中搜索, 所以这里可以搜索出三个结果
可以通过比例限制匹配的结果, 限制到 90% 后, 只能匹配到 1 条结果

```json
POST mytest/_search
{
  "query": {
    "match": {
      "content.cn":{
        "query": "这里可以有一些内容",
        "analyzer": "ik_smart",
        "minimum_should_match": "90%"
      }
    }
  }
}
```

match_phrase 只能匹配到 1 条结果, 如果把文本换成 "这里可以有内容一些", 就不能匹配到内容了
这时需要使用 slop 完成近似匹配, 允许有顺序的差异. 同时, 使用 match 匹配的时候, 词的顺序不影响结果得分

```json
POST mytest/_search
{
  "query": {
    "match_phrase": {
      "content.cn":{
        "query": "这里可以有一些内容",
        "analyzer": "ik_smart",
        "slop": 0
      }
    }
  }
}
```

## 2.3 精确搜索的时候, 可以使用默认分词器, 以达到精确的目的

在搜索中输入 "以有一些内容" 不完整的内容, 也可以搜索到精确的结果
修改为 match 可以

```json
POST mytest/_search
{
  "query": {
    "match_phrase": {
      "content.std":{
        "query": "以有一些内容",
        "analyzer": "standard"
      }
    }
  }
}
```

# 3 自定义打分

## 3.1 打分公式

```plint
score(q,d) = queryNorm(q)            //归一化因子
             ·coord(q,d)             //协调因子
             ·∑( tf(t in d)          //词频
                ·idf(t)²             //逆向文档频率
                ·t.getBoost()        //权重
                ·norm(t,d)           //字段长度归一值
              )(t in q)
```

queryNorm 查询归化因子: 会被应用到每个文档, 不能被更改, 总而言之, 可以被忽略
coord 协调因子: 可以为那些查询词包含度高的文档提供奖励, 文档里出现的查询词越多, 它越有机会成为好的匹配结果
协调因子将评分与文档里匹配词的数量相乘，然后除以查询里所有词的数量，如果使用协调因子，评分会变成：
文档里有 fox → 评分： 1.5 * 1 / 3 = 0.5
文档里有 quick fox → 评分： 3.0 * 2 / 3 = 2.0
文档里有 quick brown fox → 评分： 4.5 * 3 / 3 = 4.5
协调因子能使包含所有三个词的文档比只包含两个词的文档评分要高出很多

## 3.2 提升查询权重

```json
POST mytest/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "match": {
            "title": {"query": "好评", "boost": 10}
          }
        },
        {"match": {"content": "有一些内容"}
        }
      ]
    }
  }
}
```

## 3.3 结合 function_score 查询 与 field_value_factor 查询可以实现按照文档的字段来影响文档评分

```json
POST mytest/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {"query": "好评","fields": ["title", "content"]}
      },
      "functions": [
        {"field_value_factor": {"field": "score"}}
      ]
    }
  }
}
```

new_score = old_score * number_of_votes
这样会导致 votes 为 0 的文档评分为 0，而且 votes 值过大会掩盖掉全文评分

## 3.4 一般会使用 modifier 参数来平滑 votes 的值

```json
POST mytest/_search
{
  "query": {
    "function_score": {
      "query": {
        "multi_match": {"query": "好评","fields": ["title", "content"]}
      },
      "functions": [
        {"field_value_factor": {"field": "score", "modifier": "log1p"}}
      ]
    }
  }
}
```

应用值为 log1p 的 modifier 后的评分计算公式：
new_score = old_score * log(1 + number_of_votes)

modifier 的可以为: none ,log ,log1p ,log2p ,ln ,ln1p ,ln2p ,square ,sqrt ,reciprocal

## 3.5 factor 可以通过将 votes 字段与 factor 的积来调节受欢迎程度效果的高低

```json
"functions": [
        {"field_value_factor": {"field": "score", "modifier": "ln1p", "factor": 10}}
      ]
```

添加了 factor 会使公式变成这样：
new_score = old_score * log(1 + factor * number_of_votes)

## 3.6 通过参数 boost_mode 来控制函数与查询评分 _score 合并后的结果，参数接受的值

multiply: 评分 _score 与函数值的积（默认）
sum: 评分 _score 与函数值的和
min: 评分 _score 与函数值间的较小值
max: 评分 _score 与函数值间的较大值
replace: 函数值替代评分 _score

```json
"functions":[],
"boost_mode": "sum"
```

之前请求的公式现在变成下面这样：
new_score = old_score + log(1 + 0.1 * number_of_votes)

## 3.7 可以使用 max_boost 参数限制一个函数的最大效果

```json
"boost_mode": "sum"
"max_boost": 1.5
```

无论 field_value_factor 函数的结果如何，最终结果都不会大于 1.5 。
注意 max_boost 只对函数的结果进行限制，不会对最终评分 _score 产生直接影响。

## 3.8 评分模式 score_mode

每个函数返回一个结果，所以需要一种将多个结果缩减到单个值的方式，然后才能将其与原始评分 _score 合并。
评分模式 score_mode 参数正好扮演这样的角色， 它接受以下值：

multiply ： 函数结果求积（默认）。
sum ： 函数结果求和。
avg ： 函数结果的平均值。
max ： 函数结果的最大值。
min ： 函数结果的最小值。
first ： 使用首个函数（可以有过滤器，也可能没有）的结果作为最终结果
在本例中，我们将每个过滤器匹配结果的权重 weight 求和，并将其作为最终评分结果，所以会使用 sum 评分模式。
不与任何过滤器匹配的文档会保有其原始评分， _score 值的为 1 。

## 3.9 衰减函数

这需要使用 function_score 查询提供的一组 衰减函数（decay functions）
linear 线性函数
exp 指数函数
gauss 高斯函数

所有三个函数都接受如下参数：
origin：中心点 或字段可能的最佳值，落在原点 origin 上的文档评分 _score 为满分 1.0 。
scale：衰减率，即一个文档从原点 origin 下落时，评分 _score 改变的速度。（例如，每 £10 欧元或每 100 米）。
decay：从原点 origin 衰减到 scale 所得的评分 _score ，默认值为 0.5 。
offset：以原点 origin 为中心点，为其设置一个非零的偏移量 offset 覆盖一个范围，而不只是单个原点。在范围 -offset <= origin <= +offset 内的所有评分 _score 都是 1.0

```json
      "functions": [
        {
          "exp": {
            "time": {
              "origin": "1596530751000",
              "scale": "10d",
              "offset": "5d",
              "decay": 0.5
            }
          }
        }
      ]
```

