import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Map;

public class ES_09_MultiGetAPI {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        MultiGetRequest request = new MultiGetRequest();
        request.add(new MultiGetRequest.Item(
                "index",         //索引
                "example_id"));  //文档id
        request.add(new MultiGetRequest.Item("index", "another_id")); //添加另一个要提取的项目

        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);

        MultiGetItemResponse firstItem = response.getResponses()[0];
        //assertNull(firstItem.getFailure()); //getFailure返回null意味着没有失败。
        GetResponse firstGet = firstItem.getResponse();//getResponse返回GetResponse。
        String index = firstItem.getIndex();
        String id = firstItem.getId();
        System.out.println(firstGet);
        if (firstGet.isExists()) {
            long version = firstGet.getVersion();
            String sourceAsString = firstGet.getSourceAsString(); //以字符串形式检索文档
            Map<String, Object> sourceAsMap = firstGet.getSourceAsMap();//以Map<String, Object>的形式检索文档
            byte[] sourceAsBytes = firstGet.getSourceAsBytes(); //以 byte[]形式检索文档
        } else {
            //处理找不到文档的情况。请注意，虽然返回的响应有404个状态代码，但返回的是有效的GetResponse，而不是引发的异常。这种响应不包含任何源文档，其isExists方法返回false。
        }

        client.close();
    }
}
