package com.es.utils;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class ElasticSearchUtil {

    @Resource
    RestHighLevelClient restHighLevelClient;

    /**
     * 判断索引是否存在
     *
     * @param indexName
     * @return
     */
    public boolean isIndexExists(String indexName) {
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            getIndexRequest.humanReadable(true);
            boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            return exists;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     */
    public boolean deleteIndex(String indexName) {
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
            AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            boolean acknowledged = delete.isAcknowledged();
            return acknowledged;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
