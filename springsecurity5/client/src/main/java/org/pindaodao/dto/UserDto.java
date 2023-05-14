package org.pindaodao.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserDto对象", description = "用户信息")
public class UserDto {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty(value = "企业名称", required = true)
    private String name;

    @ApiModelProperty(value = "企业地址", required = true)
    private String address;

    @ApiModelProperty(value = "企业统一信用代码", required = true)
    private String code;

    @ApiModelProperty(value = "企业法人姓名", required = true)
    private String juridicalName;

    @ApiModelProperty(value = "联系电话", required = true)
    private String phone;

    @ApiModelProperty("企业简介")
    private String introduction;
}
