package com.es.controller;

import com.es.modle.Response;
import com.es.service.ElasticSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@Api(value = "IndexController", tags = {"索引管理接口"})
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Slf4j
public class IndexController {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    ElasticSearchService elasticSearchService;

    @ApiOperation(value = "创建索引接口", notes = "创建索引")
    @PostMapping(value = "/indexes/{name}")
    public Response createIndex(@PathVariable("name") String indexName) {
        try {
            elasticSearchService.createIndex(indexName);
            return new Response(200, "创建成功", "Nice!");
        } catch (IOException e) {
            return new Response(500, "创建失败", "Shit!");
        }
    }

    @ApiOperation(value = "是否存在索引接口", notes = "查询索引是否存在")
    @GetMapping(value = "/indexes/{name}")
    public Response indexExists(@PathVariable("name") String indexName) {
        try {
            boolean isExists = elasticSearchService.isIndexExists(indexName);
            String result = isExists ? "索引存在" : "索引不存在";
            return new Response(200, result, isExists);
        } catch (IOException e) {
            return new Response(500, "查询失败", "Shit!");
        }
    }

    @ApiOperation(value = "删除除索引接口", notes = "删除除索引")
    @DeleteMapping(value = "/indexes/{name}")
    public Response deleteIndex(@PathVariable("name") String indexName) {
        try {
            boolean isDelete = elasticSearchService.deleteIndex(indexName);
            return new Response(200, "删除成功", null);
        } catch (IOException e) {
            return new Response(500, "删除失败", null);
        }
    }
}
