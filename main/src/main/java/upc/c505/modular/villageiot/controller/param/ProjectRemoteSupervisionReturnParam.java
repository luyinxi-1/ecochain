package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/7/12 9:28
 */
@Data
@Accessors(chain = true)
public class ProjectRemoteSupervisionReturnParam {
    @ApiModelProperty("项目年份")
    private String projectYear;

    @ApiModelProperty("项目类型")
    private String projectType;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("项目编号")
    private String projectNumber;

    @ApiModelProperty("承建单位项目经理")
    private String constructionProjectLeader;

    @ApiModelProperty("承建单位项目经理电话")
    private String constructionProjectLeaderPhone;

    @ApiModelProperty("承建单位名称")
    private String constructionName;

    @ApiModelProperty("监理单位名称")
    private String supervisionName;

    @ApiModelProperty("视频配置表id")
    private Integer id;

    @ApiModelProperty("监控属性（重点单位、重点项目、党组织会议室、产业）")
    private String monitorAttributes;

    @ApiModelProperty("所属区域id")
    private Long areaId;

    @ApiModelProperty("所属区域")
    private String areaName;

    @ApiModelProperty("监控组名称（企业名称）")
    private String monitorGroupName;

    @ApiModelProperty("社会信用代码/项目编号/党组织id")
    private String entCreditCode;

    @ApiModelProperty("设备类型（海康、大华等）")
    private String deviceType;

    @ApiModelProperty("当前设备数量")
    private Integer currentDeviceNumber;

    @ApiModelProperty("许可类别（许可证类型）")
    private String coaType;

    @ApiModelProperty("许可证号")
    private String coaCode;

    @ApiModelProperty("播放器类型")
    private String videoSign;

    @ApiModelProperty("企业在线视频数量")
    private Long onlineNumber;

}
