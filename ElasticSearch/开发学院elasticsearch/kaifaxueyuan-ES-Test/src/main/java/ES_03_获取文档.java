import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Map;

public class ES_03_获取文档 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        GetRequest getRequest = new GetRequest(
                "posts", //索引名称
                "1");   //文档id

        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        String index = getResponse.getIndex();
        String id = getResponse.getId();
        if (getResponse.isExists()) {
            long version = getResponse.getVersion();
            System.out.println("version: "+version);

            String sourceAsString = getResponse.getSourceAsString(); //以字符串形式检索文档
            System.out.println("soureAsString: " +sourceAsString);

            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap(); //以Map<String, Object>的形式检索文档
            System.out.println("sourceASMap: "+sourceAsMap);

            byte[] sourceAsBytes = getResponse.getSourceAsBytes(); //以byte[]形式检索文档
            System.out.println("sourceAsByte: "+ sourceAsBytes);

        } else {
            System.out.println("null");
        }


        client.close();
    }
}
