import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;

import java.io.IOException;
import java.util.List;

public class ES_07_术语向量_TermVectorsAPI {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        TermVectorsRequest request = new TermVectorsRequest("posts", "19");
        request.setFields("user");

        TermVectorsResponse response =
                client.termvectors(request, RequestOptions.DEFAULT);


        String index = response.getIndex(); //文档的索引名称。
        System.out.println(index);

        //String type = response.getType(); //文档的类型名称。
        String id = response.getId(); //文档的id。
        System.out.println(id);
        boolean found = response.getFound(); //指示是否找到文档。
        System.out.println(found);


        for (TermVectorsResponse.TermVector tv : response.getTermVectorsList()) {
            String fieldname = tv.getFieldName(); //当前字段的名称
            int docCount = tv.getFieldStatistics().getDocCount(); //当前字段-文档计数的字段统计
            long sumTotalTermFreq =
                    tv.getFieldStatistics().getSumTotalTermFreq(); //当前字段的字段统计信息-总术语频率之和
            long sumDocFreq = tv.getFieldStatistics().getSumDocFreq(); //当前字段的字段统计信息-文档频率的总和
            if (tv.getTerms() != null) {//当前字段的术语
                List<TermVectorsResponse.TermVector.Term> terms =
                        tv.getTerms(); //
                for (TermVectorsResponse.TermVector.Term term : terms) {
                    String termStr = term.getTerm(); //术语的名称
                    int termFreq = term.getTermFreq(); //术语的术语频率
                    int docFreq = term.getDocFreq(); //记录术语的频率
                    long totalTermFreq = term.getTotalTermFreq(); //术语的总术语频率
                    float score = term.getScore(); //分数
                    if (term.getTokens() != null) {
                        List<TermVectorsResponse.TermVector.Token> tokens =
                                term.getTokens(); //该术语的令牌
                        for (TermVectorsResponse.TermVector.Token token : tokens) {
                            int position = token.getPosition(); //令牌的位置
                            int startOffset = token.getStartOffset(); //令牌的起始偏移量
                            int endOffset = token.getEndOffset(); //令牌的结束偏移量
                            String payload = token.getPayload(); //令牌的有效负载
                        }
                    }
                }
            }
        }

        client.close();
    }
}
