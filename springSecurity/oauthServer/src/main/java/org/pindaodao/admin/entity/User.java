package org.pindaodao.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 拼叨叨
 * @since 2023-03-30
 */
@Getter
@Setter
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("账号是否过期")
    private Boolean accountNonExpired;

    @ApiModelProperty("账号是否锁定")
    private Boolean accountNonLocked;

    @ApiModelProperty("账号密码是否过期")
    private Boolean credentialsNonExpired;

    @ApiModelProperty("是否启用账号")
    private Boolean enabled;
}
