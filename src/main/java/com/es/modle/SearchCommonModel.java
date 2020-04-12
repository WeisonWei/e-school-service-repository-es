package com.es.modle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Author MaxSoft
 **/
@ApiModel(description="通用查询模型")
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchCommonModel {

    @ApiModelProperty("表名")
    String tableName;

    @ApiModelProperty("查询语句")
    String query;

    @ApiModelProperty("页码")
    Integer page;

    @ApiModelProperty("页大小")
    Integer size;

    @ApiModelProperty("排序语句")
    String sort;

}
