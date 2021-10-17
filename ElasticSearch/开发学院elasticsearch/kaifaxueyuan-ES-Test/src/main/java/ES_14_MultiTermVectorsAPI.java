import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MultiTermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

public class ES_14_MultiTermVectorsAPI {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        MultiTermVectorsRequest request = new MultiTermVectorsRequest(); //创建一个空的MultiTermVectorsRequest。
        TermVectorsRequest tvrequest1 =
                new TermVectorsRequest("authors", "1");
        tvrequest1.setFields("user");
        request.add(tvrequest1); //将第一个术语向量请求添加到多术语向量请求中。

        XContentBuilder docBuilder = XContentFactory.jsonBuilder();
        docBuilder.startObject().field("user", "guest-user").endObject();
        TermVectorsRequest tvrequest2 =
                new TermVectorsRequest("authors", docBuilder);
        request.add(tvrequest2); //将人工文档的第二个术语请求添加到多术语请求中。

        client.close();
    }
}
