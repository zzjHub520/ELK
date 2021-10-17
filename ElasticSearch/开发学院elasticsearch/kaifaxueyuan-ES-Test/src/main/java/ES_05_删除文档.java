import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ES_05_删除文档 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        DeleteRequest request = new DeleteRequest(
                "posts",    //索引
                "1");       //文档id

        DeleteResponse deleteResponse = client.delete(
                request, RequestOptions.DEFAULT);

        String index = deleteResponse.getIndex();
        System.out.println(index);

        String id = deleteResponse.getId();
        System.out.println(id);

        long version = deleteResponse.getVersion();
        System.out.println(version);

        ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            //处理成功分片数少于总分片数的情况
            System.out.println("deal with shard");
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {//处理潜在的故障
                String reason = failure.reason();
                System.out.println(reason);
            }
        }

        client.close();
    }
}
