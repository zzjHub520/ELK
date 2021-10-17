import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

import java.io.IOException;

public class ES_11_UpdateByQueryAPI {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        UpdateByQueryRequest request =
                new UpdateByQueryRequest("source1", "source2"); //在一组索引上创建UpdateByQueryRequest。

        client.close();
    }
}
