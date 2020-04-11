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
@ApiModel(description="业务聚合统计模型")
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchStatisticsModel {

    @ApiModelProperty("索引名称")
    private String tableName;

    @ApiModelProperty("统计条件")
    private String query;

    @ApiModelProperty("统计求和字段")
    private String sumField;

    @ApiModelProperty("统计分组字段")
    private String termField;
}
