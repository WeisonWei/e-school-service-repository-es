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
@ApiModel(description="通用查询数据列模型")
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchCommonFieldsModel {

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("查询语句")
    private String query;

    @ApiModelProperty("排序语句")
    private String sort;

    @ApiModelProperty("包含的列名")
    private String includes;

    @ApiModelProperty("排除的列名")
    private String excludes;

    @ApiModelProperty("是否Kye-Value")
    private Boolean isKV;

}
