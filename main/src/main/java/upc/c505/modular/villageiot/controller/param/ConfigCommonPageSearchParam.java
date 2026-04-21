package upc.c505.modular.villageiot.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author: frd
 * @create-date: 2024/1/16 16:58
 */
@Data//lombok自动生成增删改查
@Accessors(chain = true)//链式编程
@EqualsAndHashCode(callSuper = false)
public class ConfigCommonPageSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("视频单位类型_父类")
    private String videoTypeParent;

    @ApiModelProperty("视频单位类型_子类")
    private String videoTypeChild;

    @ApiModelProperty("设备类型（海康、大华等）")
    private String deviceType;
}
