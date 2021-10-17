import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class ES_08_BulkAPI {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest("posts", "3")); //向批量请求添加删除请求
        request.add(new UpdateRequest("posts", "2")
                .doc(XContentType.JSON,"other", "test"));//向批量请求添加更新请求。
        request.add(new IndexRequest("posts").id("4")
                .source(XContentType.JSON,"field", "baz"));//使用SMILE格式添加索引请求


        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

        for (BulkItemResponse bulkItemResponse : bulkResponse) { //迭代所有操作的结果
            DocWriteResponse itemResponse = bulkItemResponse.getResponse(); //检索操作的响应(成功与否)，可以是索引响应、更新响应或删除响应，它们都可以被视为DocWriteResponse实例

            switch (bulkItemResponse.getOpType()) {
                case INDEX:    //处理索引操作的响应
                    System.out.println("index");
                case CREATE:
                    IndexResponse indexResponse = (IndexResponse) itemResponse;
                    System.out.println("create");
                    break;
                case UPDATE:   //处理更新操作的响应
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    System.out.println("update");
                    break;
                case DELETE:   //处理删除操作的响应
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                    System.out.println("delete");
            }
        }

        client.close();
    }
}
