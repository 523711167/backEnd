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
@ApiModel(value = "Role对象", description = "")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("角色代码")
    private String name;

    @ApiModelProperty("角色名称")
    private String displayName;
}
