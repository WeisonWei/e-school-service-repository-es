package com.es.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.es.modle.SearchCommonFieldsModel;
import com.es.modle.SearchModel;
import com.es.modle.v1.ResponsePage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;


@Component
@Slf4j
public class ElasticSearchService {

    public enum HTTPMethod {
        POST("POST"), GET("GET"), PUT("PUT"), DELETE("DELETE"), HEAD("HEAD");
        @Getter
        private String code;

        HTTPMethod(String code) {
            this.code = code;
        }
    }

    private static final String KEYWORD = ".keyword";

    private static final String KEY_BOOL = "bool";
    private static final String KEY_MUST = "must";
    private static final String KEY_MATCH = "match";
    private static final String KEY_TERM = "term";
    private static final String KEY_RANGE = "range";

    private static final String KEY_CTIME = "ctime";
    private static final String KEY_UTIME = "utime";

    private static final String KEY_START_CTIME = "startCTime"; // 开始创建时间
    private static final String KEY_END_CTIME = "endCTime"; // 结束创建时间
    private static final String KEY_START_UTIME = "startUTime"; // 开始更新时间
    private static final String KEY_END_UTIME = "endUTime"; // 结束更新时间

    private static final String KEY_LTE = "lte"; // 小于等于
    private static final String KEY_LT = "lt"; // 小于
    private static final String KEY_GTE = "gte"; // 大于等于
    private static final String KEY_GT = "gt"; // 大于

    public static final int LIMIT_SIZE = 500; // 限制最大分页容量值


    String SEPARATOR = "-";

    @Resource
    RestHighLevelClient restHighLevelClient;

    @Value("weixx")
    String datasourceUrl;

    public void createIndex(String name) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(name);
        buildSetting(request);
        buildIndexMapping(request);
        request.alias(new Alias("twitter_alias").filter(QueryBuilders.termQuery("user", "kimchy")));

        //同步创建
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();

        //异步创建
        Cancellable async = restHighLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, getCreateIndexListener());

    }

    //设置分片
    public void buildSetting(CreateIndexRequest request) {
        Settings.Builder settingsBuilder = Settings.builder().put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2);
        request.settings(settingsBuilder);
    }

    public void buildIndexMappingSimple(CreateIndexRequest request) {
        request.mapping(
                "{\n" +
                        "  \"properties\": {\n" +
                        "    \"message\": {\n" +
                        "      \"type\": \"text\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);
    }

    public void buildIndexMappingMiddle(CreateIndexRequest request) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "text");
        Map<String, Object> properties = new HashMap<>();
        properties.put("message", message);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        request.mapping(mapping);
    }

    public void buildIndexMappingComplex(CreateIndexRequest request) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("message");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        request.mapping(builder);
    }

    public void buildIndexMappingBooks(CreateIndexRequest request) throws IOException {
        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> number = new HashMap<>();
        number.put("type", "text");
        Map<String, Object> price = new HashMap<>();
        price.put("type", "float");
        Map<String, Object> title = new HashMap<>();
        title.put("type", "text");
        Map<String, Object> province = new HashMap<>();
        province.put("type", "text");
        Map<String, Object> publishTime = new HashMap<>();
        publishTime.put("type", "date");
        Map<String, Object> properties = new HashMap<>();
        properties.put("number", number);
        properties.put("price", price);
        properties.put("title", title);
        properties.put("province", province);
        properties.put("publishTime", publishTime);
        Map<String, Object> book = new HashMap<>();
        book.put("properties", properties);
        jsonMap.put("books", book);
        request.mapping(jsonMap);
    }

    public void buildIndexMapping(CreateIndexRequest request) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("properties")
                .startObject()
                .field("name")
                .startObject()
                .field("index", "true")
                .field("type", "keyword")
                .endObject()
                .field("age")
                .startObject()
                .field("index", "true")
                .field("type", "integer")
                .endObject()
                .field("money")
                .startObject()
                .field("index", "true")
                .field("type", "double")
                .endObject()
                .field("address")
                .startObject()
                .field("index", "true")
                .field("type", "text")
                .field("analyzer", "ik_max_word")
                .endObject()
                .field("birthday")
                .startObject()
                .field("index", "true")
                .field("type", "date")
                .field("format", "strict_date_optional_time||epoch_millis")
                .endObject()
                .endObject()
                .endObject();
        request.mapping(builder);
    }

    private ActionListener<CreateIndexResponse> getCreateIndexListener() {
        ActionListener<CreateIndexResponse> listener =
                new ActionListener<CreateIndexResponse>() {
                    @Override
                    public void onResponse(CreateIndexResponse createIndexResponse) {
                        log.info("index [" + createIndexResponse.index() + "] 创建成功");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        log.error("index 创建失败: " + e.getMessage());
                    }
                };

        return listener;
    }

    private ActionListener<Boolean> getGetIndexListener() {
        ActionListener<Boolean> listener = new ActionListener<Boolean>() {
            @Override
            public void onResponse(Boolean exists) {
                log.info("index is exists :[" + exists + "]");
            }

            @Override
            public void onFailure(Exception e) {
                log.error("index is exists 查询失败: " + e.getMessage());
            }
        };
        return listener;
    }

    private ActionListener<AcknowledgedResponse> delIndexListener() {
        ActionListener<AcknowledgedResponse> listener = new ActionListener<AcknowledgedResponse>() {

            @Override
            public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                log.info("index [" + acknowledgedResponse.isAcknowledged() + "] 删除成功");
            }

            @Override
            public void onFailure(Exception e) {
                log.error("index 删除成功: " + e.getMessage());
            }
        };
        return listener;
    }

    public boolean isIndexExists(String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        getIndexRequest.local(false);
        getIndexRequest.humanReadable(true);
        getIndexRequest.includeDefaults(false);
        //getIndexRequest.indicesOptions(IndicesOptions);
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        Cancellable cancellable = restHighLevelClient.indices().existsAsync(getIndexRequest, RequestOptions.DEFAULT, getGetIndexListener());

        return exists;
    }

    public boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

        Cancellable cancellable = restHighLevelClient.indices().deleteAsync(deleteIndexRequest, RequestOptions.DEFAULT, delIndexListener());

        return delete.isAcknowledged();
    }


    /**
     * 通过 DataSourceUrl 构建 Index
     *
     * @param index
     * @return
     */
    public final String getIndex(String index) {
        if (StringUtils.isNotBlank(datasourceUrl)) {
            String[] params = datasourceUrl.split("\\?")[0].split("/");
            return params[params.length - 1] + SEPARATOR + index;
        }
        return index;
    }

    /**
     * 指定查询属性字段
     *
     * @param field
     * @return
     */
    public final String getKeyword(String field) {
        return field;
    }

    /**
     * 校验查询实体
     *
     * @param model
     * @param isDSL
     * @throws Exception
     */
    public final void validatedModel(SearchModel model, boolean isDSL) throws Exception {
        if (model.getSize() == null || model.getSize() < 0 || model.getSize() > LIMIT_SIZE) {
            model.setSize(20);
        }
        if (model.getPage() == null || model.getPage() <= 0) {
            model.setPage(1);
        }
        if (StringUtils.isBlank(model.getTableName())) {
            throw new Exception("表名[tableName]不能为空！");
        }
        if (isDSL) {
            String queryScript = model.getQueryScript();
            if (StringUtils.isBlank(queryScript)) {
                throw new Exception("查询脚本[queryScript]不能为空！");
            } else {
                JSONObject query = JSONObject.parseObject(queryScript);
                model.setQuery(query);
            }
        } else {
            String keyword = model.getKeyword();
            if (StringUtils.isBlank(keyword)) {
                throw new Exception("查询内容[keyword]不能为空！");
            }
        }
    }

    public final ResponsePage.Page assembleResult(SearchResponse response, SearchModel model, Boolean isRawResult) {
        ResponsePage.Page page = new ResponsePage.Page();
        if (response == null || response.getHits() == null) {
            return page;
        }
        List list = new ArrayList();
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits().value;
        if (isRawResult != null && isRawResult) {
            // 直接使用response
            list.add(response);
        } else {
            SearchHit[] resultHits = hits.getHits();
            int len = resultHits.length;
            for (int i = 0; i < len; i++) {
                SearchHit hit = resultHits[i];
                list.add(hit.getSourceAsMap());
            }
        }

        page.setContent(list);
        page.setTotalElements(total);

        page.setEmpty(page.getContent().isEmpty());

        if (model.getSize() == null || model.getSize() == 0) {
            page.setTotalPages(1);
            page.setSize(total);
            page.setNumber(1);
            page.setFirst(true);
            page.setLast(true);
            page.setNumberOfElements((model.getPage() - 1) * page.getSize());
            return page;
        }
        page.setNumber(model.getPage());
        if (total % model.getSize() > 0) {
            page.setTotalPages(total / model.getSize() + 1);
        } else {
            page.setTotalPages(total / model.getSize());
        }
        page.setSize(model.getSize());
        if (page.getNumber() == 1) {
            page.setFirst(true);
        } else {
            page.setFirst(false);
        }

        if (page.getNumber() == page.getTotalPages()) {
            page.setLast(true);
        } else {
            page.setLast(false);
        }
        return page;

    }


    /**
     * 组装请求对象
     *
     * @param model
     * @return
     */
    public final SearchResponse doSearch(SearchModel model, QueryBuilder queryBuilder) throws IOException {

        SearchRequest searchRequest = new SearchRequest(); // 新建查询请求对象

        String index = model.getTableName();
        if (StringUtils.isNotBlank(index)) {
            searchRequest.indices(getIndex(index));
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); // 构造查询源
        searchSourceBuilder.query(queryBuilder);
        if (model.getSize() > 0) {
            searchSourceBuilder.size(model.getSize()).from((model.getPage() - 1) * model.getSize());
        }
        JSONArray sorts = model.getSort();
        if (sorts != null && !sorts.isEmpty()) {
            int len = sorts.size();
            for (int i = 0; i < len; i++) {
                JSONObject sort = sorts.getJSONObject(i);
                String key = String.valueOf(sort.keySet().iterator().next());
                String value = String.valueOf(sort.getString(key));
                FieldSortBuilder order = SortBuilders.fieldSort(key)
                        .order(SortOrder.fromString(value));
                searchSourceBuilder.sort(order);
            }
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 通过给定的查询条件数字进行组装 Match 查询语句
     *
     * @param jsonArray
     * @return
     */
    public final JSONObject matchObject(JSONArray jsonArray) {

        JSONObject mustJSONObject = new JSONObject();

        mustJSONObject.put(KEY_MUST, new JSONArray());
        jsonArray.stream().forEach(json -> {
            JSONObject matchJSONObject = new JSONObject();
            matchJSONObject.put(KEY_MATCH, json);
            mustJSONObject.getJSONArray(KEY_MUST).add(matchJSONObject);
        });

        JSONObject boolJSONObject = new JSONObject();
        boolJSONObject.put(KEY_BOOL, mustJSONObject);
        return boolJSONObject;
    }

    private boolean isOk(Request request) throws Exception {
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        return response.getStatusLine().getReasonPhrase().equals("OK");
    }

    /**
     * 校验当前索引是否存在
     *
     * @param index
     * @return
     * @throws Exception
     */
    public final boolean isExists(String index) throws Exception {
        Request request = new Request(HTTPMethod.POST.getCode(), getIndex(index));
        return isOk(request);
    }

    /**
     * 将指定索引中的字段设置为可聚合查询，即 将 fielddata 置为 true
     *
     * @param index
     * @param fields
     * @return
     * @throws Exception
     */
    public boolean updateMappingsForFiledData(String index, String... fields) throws Exception {

        if (fields == null) {
            return false;
        }

        String endPoint = getIndex(index) + "/_mapping/" + index; // 装置请求地址

        JSONObject properties = new JSONObject();
        JSONObject textFieldData = new JSONObject();
        textFieldData.put("type", "text");
        textFieldData.put("fielddata", true);
        JSONObject field = null;
        int length = fields.length;
        for (int i = 0; i < length; i++) {
            field = new JSONObject();
            field.put(fields[i], textFieldData);
        }
        properties.put("properties", field);

        Request request = new Request(HTTPMethod.PUT.getCode(), endPoint);
        request.setJsonEntity(properties.toJSONString());
        return isOk(request);
    }

    /**
     * 通过缩影名称及列名进行分组统计查询
     *
     * @param index
     * @param field
     * @return
     * @throws IOException
     */
    public SearchResponse countForGroupBySingleField(String index, String field, QueryBuilder queryBuilder) throws IOException {
        if (StringUtils.isBlank(index) || StringUtils.isBlank(field)) {
            return null;
        }
        TermsAggregationBuilder termsAggBuilder = AggregationBuilders.terms(getIndex(index)).field(field).size(LIMIT_SIZE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) { // 增加过滤分组筛选条件
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(termsAggBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(getIndex(index));
        searchRequest.types(index);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return search;
    }

    /**
     * 查询单索引数据，如果 field 或 values 为空，则默认查询全部
     *
     * @param index
     * @param field
     * @param values
     * @return
     */
    public SearchResponse searchForInSingleField(String index, String field, Integer page, Integer size, Object... values) throws IOException {
        SearchResponse search;
        SearchRequest request = new SearchRequest();
        request.indices(getIndex(index)).types(index);
        SearchSourceBuilder sourceBuilder = SearchSourceBuilder.searchSource();
        if (StringUtils.isNotBlank(field) && ArrayUtils.isNotEmpty(values)) {
            TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(field, values);
            sourceBuilder.query(termsQueryBuilder);
        }
        if (size > 0) {
            sourceBuilder.from((page - 1) * size).size(size);
        } else {
            sourceBuilder.from(0).size(10000);
        }
        request.source(sourceBuilder);
        search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return search;
    }

    /**
     * 对指定条件的数据进行统计分组求和
     *
     * @param index
     * @param queryBuilder
     * @param aggregationBuilder
     * @return
     * @throws IOException
     */
    public SearchResponse statisticsSum(String index, QueryBuilder queryBuilder, AggregationBuilder aggregationBuilder) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(aggregationBuilder);
        searchSourceBuilder.query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(this.getIndex(index));
        searchRequest.types(index);

        SearchResponse search = null;

        search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        return search;
    }

    /**
     * 校验 SearchCommonFieldsModel
     *
     * @param model
     * @throws Exception
     */
    public void checkCommonFieldsModel(SearchCommonFieldsModel model) throws Exception {
        if (StringUtils.isBlank(model.getTableName())) {
            throw new Exception("参数[tableName]不能为空！");
        }

        if (StringUtils.isBlank(model.getQuery())) {
            throw new Exception("参数[query]不能为空！");
        }

        if (StringUtils.isBlank(model.getSort())) {
            model.setSort(null);
        }

        if (model.getIncludes() == null) {
            model.setIncludes("");
        }

        if (model.getExcludes() == null) {
            model.setExcludes("");
        }

        if (model.getIsKV() == null) {
            model.setIsKV(true);
        }
    }

    /**
     * 组装排序逻辑
     *
     * @param sort
     * @param searchSourceBuilder
     */
    public void assembleSort(String sort, SearchSourceBuilder searchSourceBuilder) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(sort);
        Set<String> keySet = jsonObject.keySet();
        keySet.forEach(key -> {
            String value = String.valueOf(jsonObject.get(key));
            FieldSortBuilder order = SortBuilders.fieldSort(key).order(SortOrder.fromString(value));
            searchSourceBuilder.sort(order);
        });

    }

    /**
     * 组装查询逻辑
     *
     * @param query
     * @param searchSourceBuilder
     */
    public void assembleQuery(String query, SearchSourceBuilder searchSourceBuilder) {
        //boolean validArray = JSONObject.isValidArray(query);
        //boolean validQuery = JSONObject.isValidObject(query);
        boolean validArray = true;
        boolean validQuery = false;
        JSONObject jsonObject;
        if (validArray) {
            jsonObject = matchObject(JSONObject.parseArray(query));
            searchSourceBuilder.query(QueryBuilders.wrapperQuery(jsonObject.toJSONString()));
        } else if (validQuery) {
            jsonObject = JSONObject.parseObject(query);
            searchSourceBuilder.query(QueryBuilders.wrapperQuery(jsonObject.toJSONString()));
        } else if (query != null) {
            searchSourceBuilder.query(QueryBuilders.queryStringQuery(query));
        }
    }

    /**
     * 将JSON对象组装为 key-value JSONArray
     *
     * @param json
     * @return
     */
    public String assembleJsonArray(JSONObject json) {
        if (json != null) {
            Set<String> keySet = json.keySet();
            JSONArray jsonArray = new JSONArray();
            keySet.forEach(key -> {
                JSONObject value = new JSONObject();
                value.put(key, json.get(key));
                jsonArray.add(value);
            });

            return jsonArray.size() > 0 ? jsonArray.toJSONString() : null;
        }
        return null;
    }

    /**
     * 合并 query 查询脚本
     *
     * @param query
     * @param json
     * @return
     */
    public String assembleQuery(String query, JSONObject json) {
        if (StringUtils.isBlank(query)) {
            query = "{\"bool\":{\"must\":[]}}";
        }
        if (json != null && json.keySet().size() > 0) {
            JSONArray jsonArray = new JSONArray();

            assembleTerm(json, jsonArray);
            assembleRangeTime(json, jsonArray, KEY_CTIME, KEY_START_CTIME, KEY_END_CTIME);
            assembleRangeTime(json, jsonArray, KEY_UTIME, KEY_START_UTIME, KEY_END_UTIME);

            if (true && jsonArray.size() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(query);
                if (jsonObject.containsKey(KEY_BOOL) && jsonObject.getJSONObject(KEY_BOOL).containsKey(KEY_MUST)) {
                    jsonObject.getJSONObject(KEY_BOOL).getJSONArray(KEY_MUST).addAll(jsonArray);
                } else {
                    return this.matchObject(jsonArray).toJSONString();
                }
                return jsonObject.toJSONString();
            } else if (jsonArray.size() > 0) {
                return this.matchObject(jsonArray).toJSONString();
            } else {
                return query;
            }
        }
        return query;
    }

    /**
     * 组装精确匹配查询参数
     *
     * @param json
     * @param jsonArray
     */
    private void assembleTerm(JSONObject json, JSONArray jsonArray) {
        Set<String> keySet = json.keySet();
        keySet.forEach(key -> {
            if (key.contains(KEY_START_CTIME) ||
                    key.contains(KEY_END_CTIME) ||
                    key.contains(KEY_START_UTIME) ||
                    key.contains(KEY_END_UTIME)) {
                return;
            }
            JSONObject value = new JSONObject();
            value.put(key, json.get(key));
            JSONObject term = new JSONObject();
            term.put(KEY_TERM, value);
            jsonArray.add(term);
        });
    }

    /**
     * 组装时间区间查询参数
     *
     * @param json
     * @param jsonArray
     * @param field
     * @param begin
     * @param end
     */
    private void assembleRangeTime(JSONObject json, JSONArray jsonArray, String field, String begin, String end) {
        JSONObject range = new JSONObject();
        JSONObject ctime = new JSONObject();
        JSONObject values = new JSONObject();
        if (json.containsKey(begin) && StringUtils.isNotBlank(json.getString(begin)) &&
                json.containsKey(end) && StringUtils.isNotBlank(json.getString(end))) {
            values.put(KEY_GTE, json.getDate(begin).getTime());
            values.put(KEY_LT, json.getDate(end).getTime());
            ctime.put(field, values);
            range.put(KEY_RANGE, ctime);
            jsonArray.add(range);
        } else if (json.containsKey(begin) && StringUtils.isNotBlank(json.getString(begin))) {
            values.put(KEY_GTE, json.getDate(begin).getTime());
            ctime.put(field, values);
            range.put(KEY_RANGE, ctime);
            jsonArray.add(range);
        } else if (json.containsKey(end) && StringUtils.isNotBlank(json.getString(end))) {
            values.put(KEY_LT, json.getDate(end).getTime());
            ctime.put(field, values);
            range.put(KEY_RANGE, ctime);
            jsonArray.add(range);
        }
    }

    /**
     * 基础查询实现
     *
     * @param request
     * @param options
     * @return
     * @throws Exception
     */
    public SearchResponse doSearch(SearchRequest request, RequestOptions options) throws Exception {
        return restHighLevelClient.search(request, options);
    }

    /**
     * 基础查询实现精简版本
     *
     * @param request
     * @return
     * @throws Exception
     */
    public SearchResponse doSearch(SearchRequest request) throws Exception {
        return this.doSearch(request, RequestOptions.DEFAULT);
    }

    /**
     * 基础游标滚动查询实现
     *
     * @param scrollRequest
     * @param options
     * @return
     * @throws Exception
     */
    public SearchResponse doScroll(SearchScrollRequest scrollRequest, RequestOptions options) throws Exception {
        return restHighLevelClient.scroll(scrollRequest, options);
    }

    /**
     * 基础游标滚动查询实现精简版
     *
     * @param scrollRequest
     * @return
     * @throws Exception
     */
    public SearchResponse doScroll(SearchScrollRequest scrollRequest) throws Exception {
        return this.doScroll(scrollRequest, RequestOptions.DEFAULT);
    }

    /**
     * 基础游标滚动查询结束后清除游标
     *
     * @param clearScrollRequest
     * @param options
     * @return
     * @throws Exception
     */
    public ClearScrollResponse doClearScroll(ClearScrollRequest clearScrollRequest, RequestOptions options) throws Exception {
        return restHighLevelClient.clearScroll(clearScrollRequest, options);
    }

    /**
     * 基础游标滚动查询结束后清除游标精简版
     *
     * @param clearScrollRequest
     * @return
     * @throws Exception
     */
    public ClearScrollResponse doClearScroll(ClearScrollRequest clearScrollRequest) throws Exception {
        return this.doClearScroll(clearScrollRequest, RequestOptions.DEFAULT);
    }

    /**
     * 通过索引名称及列名进行分组统计最大值或最小值
     *
     * @param index
     * @param field
     * @return
     * @throws IOException
     */
    public SearchResponse maxOrMinForGroupBySingleField(String index, String field, String field1, QueryBuilder queryBuilder) throws IOException {
        if (StringUtils.isBlank(index) || StringUtils.isBlank(field)) {
            return null;
        }
        TermsAggregationBuilder termsAggBuilder = AggregationBuilders.terms(getIndex(index)).field(field).size(LIMIT_SIZE);
        termsAggBuilder.subAggregation(AggregationBuilders.max("max").field(field1));
        termsAggBuilder.subAggregation(AggregationBuilders.min("min").field(field1));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) { // 增加过滤分组筛选条件
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(termsAggBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(getIndex(index));
        searchRequest.types(index);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return search;
    }
}
