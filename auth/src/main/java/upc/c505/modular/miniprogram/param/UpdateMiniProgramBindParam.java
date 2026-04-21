package upc.c505.modular.miniprogram.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 小程序绑定更新请求参数
 */
@Data
@Accessors(chain = true)
public class UpdateMiniProgramBindParam {
    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 小程序AppId
     */
    @ApiModelProperty("小程序AppId")
    private String appId;

    /**
     * 小程序密钥
     */
    @ApiModelProperty("小程序密钥")
    private String appSecret;

    /**
     * 欢迎词
     */
    @ApiModelProperty("欢迎词")
    private String welcomeMessage;

    /**
     * 小程序名称
     */
    @ApiModelProperty("小程序名称")
    private String miniProgramName;

    /**
     * 小程序简介
     */
    @ApiModelProperty("小程序简介")
    private String miniProgramDesc;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String companyName;

    /**
     * 企业信用代码
     */
    @ApiModelProperty("企业信用代码")
    private String creditCode;

    /**
     * 预留字段1
     */
    @ApiModelProperty("预留字段1")
    private String reserveField1;

    /**
     * 预留字段2
     */
    @ApiModelProperty("预留字段2")
    private String reserveField2;

    /**
     * 预留字段3
     */
    @ApiModelProperty("预留字段3")
    private String reserveField3;

    /**
     * 预留字段4
     */
    @ApiModelProperty("预留字段4")
    private String reserveField4;

    /**
     * 预留字段5
     */
    @ApiModelProperty("预留字段5")
    private String reserveField5;

    /**
     * 预留字段6
     */
    @ApiModelProperty("预留字段6")
    private String reserveField6;

    /**
     * 预留字段7
     */
    @ApiModelProperty("预留字段7")
    private String reserveField7;

    /**
     * 预留字段8
     */
    @ApiModelProperty("预留字段8")
    private String reserveField8;

    /**
     * 预留字段9
     */
    @ApiModelProperty("预留字段9")
    private String reserveField9;

    /**
     * 预留字段10
     */
    @ApiModelProperty("预留字段10")
    private String reserveField10;

    /**
     * 预留字段11
     */
    @ApiModelProperty("预留字段11")
    private String reserveField11;

    /**
     * 预留字段12
     */
    @ApiModelProperty("预留字段12")
    private String reserveField12;

    /**
     * 预留字段13
     */
    @ApiModelProperty("预留字段13")
    private String reserveField13;

    /**
     * 预留字段14
     */
    @ApiModelProperty("预留字段14")
    private String reserveField14;

    /**
     * 预留字段15
     */
    @ApiModelProperty("预留字段15")
    private String reserveField15;

    /**
     * 预留字段16
     */
    @ApiModelProperty("预留字段16")
    private String reserveField16;

    /**
     * 预留字段17
     */
    @ApiModelProperty("预留字段17")
    private String reserveField17;

    /**
     * 预留字段18
     */
    @ApiModelProperty("预留字段18")
    private String reserveField18;

    /**
     * 预留字段19
     */
    @ApiModelProperty("预留字段19")
    private String reserveField19;

    /**
     * 预留字段20
     */
    @ApiModelProperty("预留字段20")
    private String reserveField20;
}
