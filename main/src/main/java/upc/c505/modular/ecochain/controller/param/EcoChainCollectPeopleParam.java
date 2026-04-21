package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainCollectPeopleParam {
    @ApiModelProperty("企业人员姓名")
    private String peopleName;

    @ApiModelProperty("人员职务")
    private String peopleJob;

    @ApiModelProperty("联系电话")
    private String contactPhone;

    @ApiModelProperty("所属公司")
    private String enterpriseName;

    @ApiModelProperty("头像")
    private String peoplePicture;

    @ApiModelProperty("是否被删除，默认为0")
    private Integer isDelete;

    @ApiModelProperty("收藏表id")
    private Long id;

    @ApiModelProperty("收藏id")
    private String collectId;

    @ApiModelProperty("收藏人id")
    private Long collectPeopleId;
}
