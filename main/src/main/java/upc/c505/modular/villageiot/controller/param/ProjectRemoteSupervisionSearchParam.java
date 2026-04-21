package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author: frd
 * @create-date: 2024/7/12 9:24
 */
@Data
@Accessors(chain = true)
public class ProjectRemoteSupervisionSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("项目类型")
    private String projectType;

    @ApiModelProperty("项目年份")
    private String projectYear;

    @ApiModelProperty("模糊搜索参数")
    private String searchParam;

    @ApiModelProperty("承建单位名称")
    private String constructionName;

    @ApiModelProperty("监理单位名称")
    private String supervisionName;
}
