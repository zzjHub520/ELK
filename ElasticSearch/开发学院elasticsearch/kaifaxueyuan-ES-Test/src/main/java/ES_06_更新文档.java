import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class ES_06_更新文档 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        UpdateRequest request = new UpdateRequest(
                "posts", //索引
                "1");   //文档id

        String jsonString = "{" +
                "\"updated\":\"2017-01-01\"," +
                "\"reason\":\"daily update\"" +
                "}";
        request.doc(jsonString, XContentType.JSON); //以JSON格式的字符串形式提供的部分文档源

        request.upsert(jsonString, XContentType.JSON);  //以字符串形式提供的Upsert文档源

        UpdateResponse updateResponse = client.update(
                request, RequestOptions.DEFAULT);


        String index = updateResponse.getIndex();
        String id = updateResponse.getId();
        long version = updateResponse.getVersion();
        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
            //处理第一次创建文档的情况(upsert)
            System.out.println("upsert");
        } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            //处理文档更新的情况
            System.out.println("update");
        } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
            //处理文档被删除的情况
            System.out.println("delete");
        } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
            //处理文档不受更新影响的情况，即没有对文档执行任何操作(noop)
            System.out.println("noop");
        }




        client.close();
    }
}
