import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;

public class ES_04_检查文档是否存在 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        GetRequest getRequest = new GetRequest(
                "posts", //索引
                "10");    //文档id
        getRequest.fetchSourceContext(new FetchSourceContext(false)); //禁用fetching _source.
        getRequest.storedFields("_none_");


        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);


        System.out.println("文档存在否："+exists);


        client.close();
    }
}
