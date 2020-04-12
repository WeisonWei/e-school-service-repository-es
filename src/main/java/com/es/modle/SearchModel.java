package com.es.modle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@ApiModel(description = "数据检索参数实体")
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchModel {

    @ApiModelProperty("页索引，以 1 开始")
    private Integer page = 1;

    @ApiModelProperty("页面容量")
    private Integer size = 20;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("检索内容")
    private String keyword;

    @ApiModelProperty("普通查询语句")
    private String queryScript;

    @ApiModelProperty("排序情况")
    private JSONArray sort;

    @ApiModelProperty("聚合查询语句")
    private JSONObject aggs;

    @ApiModelProperty(hidden = true)
    private JSONObject query = null;

}
