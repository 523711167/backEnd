package com.pdd.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="欢迎来到Springboot", description="这个是描述接口接收参数的实体类")
@Data
public class HelloWorld {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "积分区间开始")
    private Integer integralStart;
}
