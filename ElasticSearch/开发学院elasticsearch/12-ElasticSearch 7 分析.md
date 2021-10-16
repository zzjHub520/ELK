## ElasticSearch 7 分析

<iframe id="iframeu4097238_0" name="iframeu4097238_0" src="https://pos.baidu.com/qcam?conwid=760&amp;conhei=90&amp;rdid=4097238&amp;dc=3&amp;di=u4097238&amp;s1=1092317370&amp;s2=2674897443&amp;dri=0&amp;dis=0&amp;dai=2&amp;ps=230x654&amp;enu=encoding&amp;exps=110261,110252,110011&amp;ant=0&amp;aa=1&amp;psi=b2f92045004d658a&amp;dcb=___adblockplus_&amp;dtm=HTML_POST&amp;dvi=0.0&amp;dci=-1&amp;dpt=none&amp;tsr=0&amp;tpr=1634345585261&amp;ti=ElasticSearch%207%20%E5%88%86%E6%9E%90%2C%E5%AD%A6%E4%B9%A0ElasticSearch%207%20%E6%95%99%E7%A8%8B%2CElasticSearch%207%20%E6%9C%80%E6%96%B0%E7%89%88%E6%95%99&amp;ari=2&amp;ver=1012&amp;dbv=2&amp;drs=1&amp;pcs=1864x885&amp;pss=1864x2751&amp;cfv=0&amp;cpl=16&amp;chi=12&amp;cce=true&amp;cec=UTF-8&amp;tlm=1627002759&amp;prot=2&amp;rw=885&amp;ltu=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-analysis.html&amp;ltr=https%3A%2F%2Fwww.kaifaxueyuan.com%2Fserver%2Felasticsearch7%2Felasticsearch-mapping.html&amp;ecd=1&amp;uc=1920x1032&amp;pis=-1x-1&amp;sr=1920x1080&amp;tcn=1634345585&amp;qn=c0ab48ad9bbcf2ab&amp;tt=1634345585249.62.62.62" width="760" height="90" scrolling="no" frameborder="0" style="box-sizing: border-box;"></iframe>



 当在搜索操作期间处理查询时，分析模块分析任何索引中的内容.该模块由analyzer, tokenizer, tokenfilters和charfilters组成.如果未定义分析器，则默认情况下，内置analyzers, token, filters和tokenizers将在分析模块中注册.例如.

```
POST /pictures
{
   "settings": {
      "analysis": {
         "analyzer": {
            "index_analyzer": {
               "tokenizer": "standard", "filter": [
                  "standard", "my_delimiter", "lowercase", "stop", 
                     "asciifolding", "porter_stem"
               ]
            },
            "search_analyzer": {
               "tokenizer": "standard", "filter": [
                  "standard", "lowercase", "stop", "asciifolding", "porter_stem"
               ]
            }
         },
         "filter": {
            "my_delimiter": {
               "type": "word_delimiter",
               "generate_word_parts": true,
               "catenate_words": true,
               "catenate_numbers": true,
               "catenate_all": true,
               "split_on_case_change": true,
               "preserve_original": true,
               "split_on_numerics": true,
               "stem_english_possessive": true
            }
         }
      }
   }
}
```

**分析器**

 分析器由tokenizer和可选的token filter组成.这些分析器以逻辑名称注册在分析模块中，这些逻辑名称可以在映射定义中引用，也可以在一些API中引用.有如下几种默认分析仪

 1.Standard analyzer (standard)：可以为此分析器设置stopwords和max_token_length设置.默认情况下，stopwords列表为空，最大令牌长度为255.

 2.Simple analyzer (simple)：该分析器由小写标记器组成.

 3.Whitespace analyzer (whitespace)：该分析器由空白标记器组成.

 4.Stop analyzer (stop)：可以配置stopwords和stopwords_path.默认情况下，stopwords初始化为英文stopwords，stopwords_path包含一个包含stop words的文本文件的路径.

 5.Keyword analyzer (keyword)：该分析器将整个流标记为单个标记.它可以用作邮政编码.

 6.Pattern analyzer (pattern)：这个分析器主要处理正则表达式.像小写、模式、标志、停止字这样的设置可以在这个分析器中设置.

 7.Language analyzer：这个分析器处理印地语、阿拉伯语、管道语等语言.

 8.Snowball analyzer (snowball)：该分析器使用标准令牌化器，带有标准过滤器、小写过滤器、停止过滤器和雪球过滤器.

 9.Custom analyzer (custom)：该分析器用于创建带有令牌化器的定制分析器，令牌化器带有可选的令牌过滤器和计费过滤器.可以在此分析器中配置令牌化器、过滤器、char_filter和position_increment_gap等设置.

**令牌化器**

 令牌化器用于从ElasticSearch中的文本生成令牌.通过考虑空格或其他标点符号，文本可以被分解成记号.ElasticSearch有很多内置的标记器，可以在自定义分析器中使用.

 1.Standard tokenizer (standard)：基于语法的令牌化器，可以为此令牌化器配置max_token_length.

 2.Edge NGram tokenizer (edgeNGram)：为此令牌化器设置min_gram、max_gram、token_chars等设置.

 3.Keyword tokenizer (keyword)：生成整个输入作为输出，并且可以为此设置缓冲区大小.

 4.Letter tokenizer (letter)：捕获整个单词，直到遇到一个非字母.

 5.Lowercase tokenizer (lowercase)：与Letter tokenizer的工作原理相同，但是在创建令牌后，它会将其更改为小写.

 6.NGram Tokenizer (nGram)：为此令牌化器设置min_gram(默认值为1)、max_gram(默认值为2)和token_chars等设置.

 7.Whitespace tokenizer (whitespace)：根据空白分隔文本.

 8.Pattern tokenizer (pattern)：使用正则表达式作为令牌分隔符.可以为此令牌化器设置模式、标志和组设置.

 9.UAX Email URL Tokenizer (uax_url_email)：与lie标准令牌化器相同，但它将电子邮件和网址视为单一令牌.

 10.Path hierarchy tokenizer (path_hierarchy)：该令牌化器生成输入目录路径中存在的所有可能路径.此令牌化器可用的设置有分隔符(默认为/)、替换、缓冲区大小(默认为1024)、反转(默认为false)和跳过(默认为0).

 11.Classic tokenizer (classic)：在基于语法的标记的基础上工作的.可以为此令牌化器设置max_token_length.

 12.Thai tokenizer (thai)：该标记器用于泰语，并使用内置的泰语分段算法.

**令牌过滤器**

 令牌过滤器从令牌化器接收输入，然后这些过滤器可以修改、删除或添加输入中的文本.ElasticSearch提供了大量内置令牌过滤器.其中大部分已经在前面的章节中解释过了.

**字符过滤器**

 这些过滤器在标记器之前处理文本.字符过滤器查找特殊字符或html标记或指定的模式，然后要么删除它们，要么将其更改为适当的单词，如“&”和“删除html标记”.以下是同义词. txt中指定了同义词的分析器示例

```
{
   "settings":{
      "index":{
         "analysis":{
            "analyzer":{
               "synonym":{
                  "tokenizer":"whitespace", "filter":["synonym"]
               }
            },
            "filter":{
               "synonym":{
                  "type":"synonym", "synonyms_path":"synonym.txt", "ignore_case":"true"
               }
            }
         }
      }
   }
}
```