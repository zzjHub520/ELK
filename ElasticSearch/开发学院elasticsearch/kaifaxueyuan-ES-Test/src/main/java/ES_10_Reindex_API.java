import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.reindex.ReindexRequest;

import java.io.IOException;

public class ES_10_Reindex_API {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        ReindexRequest request = new ReindexRequest(); //创建ReindexRequest
        request.setSourceIndices("posts", "schools"); //添加要从源中复制的列表
        request.setDestIndex("shools");  //添加目标索引



        client.close();
    }
}
