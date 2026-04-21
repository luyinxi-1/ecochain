package upc.c505.modular.village.controller.param;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;
import upc.c505.modular.village.entity.VillageConstructionProjectStaff;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class VillageAssociatedUnitProjectStaffParam extends PageBaseSearchParam implements Serializable {
    @ApiModelProperty("美丽乡村建设_美丽乡村建设者表")
    private Long id;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private LocalDateTime addDatetime;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationDatetime;

    @ApiModelProperty("乡镇街道")
    private String townStreet;

    @ApiModelProperty("登记人姓名")
    private String registrantName;

    @ApiModelProperty("登记日期")
    private LocalDateTime registrantTime;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("联系方式")
    private String contactPhone;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("职务或工作种类")
    private String positionJobType;

    @ApiModelProperty("其它联系方式")
    private String ontactPhoneOther;

    @ApiModelProperty("备注")
    private String notes;

    @ApiModelProperty("工作职责描述")
    private String jobDescription;

    @ApiModelProperty("头像")
    private String picture;

    @ApiModelProperty("身份证图片")
    private String idPicture;

    @ApiModelProperty("项目编号（可作为查询主键）")
    private String projectNumber;

    @ApiModelProperty("单位社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty("人员主键")
    private Long builderId;

    @ApiModelProperty("关联表主键")
    private Long staffId;

    @ApiModelProperty("1：建设者（政府人员），0：其他")
    private String staffType;

}
