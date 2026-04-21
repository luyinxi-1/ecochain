package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EcoChainDistributorPageSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("分享人电话")
    private String sharerPhone;

    @ApiModelProperty("分享人姓名")
    private String sharerName;

    @ApiModelProperty("分享人信用代码")
    private String sharerSocialCreditCode;

    @ApiModelProperty("注册人电话")
    private String registrantPhone;

    @ApiModelProperty("注册人姓名")
    private String registrantName;

    @ApiModelProperty("开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;


    @ApiModelProperty("是否授权过期")
    private Integer isAuthorizeExpire;
}
