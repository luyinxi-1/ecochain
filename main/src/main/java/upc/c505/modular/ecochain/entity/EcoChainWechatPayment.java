package upc.c505.modular.ecochain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xth
 * @since 2024-11-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_wechat_payment")
@ApiModel(value = "EcoChainWechatPayment对象", description = "")
public class EcoChainWechatPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("openid")
    @TableField("open_id")
    private String openId;

    @ApiModelProperty("状态。SUCCESS：支付成功；REFUND：转入退款	；NOTPAY：未支付；CLOSED：已关闭；REVOKED：已撤销（付款码支付）；USERPAYING：用户支付中（付款码支付）；PAYERROR：支付失败(其他原因，如银行返回失败)")
    @TableField("status")
    private String status;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("订单号")
    @TableField("out_trade_no")
    private String outTradeNo;

    @ApiModelProperty("费用，以分为单位")
    @TableField("fee")
    private Integer fee;

    @ApiModelProperty("订单完成日期（回调，不大有用）")
    @TableField("end_datetime")
    private LocalDateTime endDatetime;

    @ApiModelProperty("添加时间")
    @TableField(value = "add_datetime", fill = FieldFill.INSERT)
    private LocalDateTime addDatetime;

    @ApiModelProperty("缴费日期")
    @TableField("payment_date")
    private LocalDateTime paymentDate;

    @ApiModelProperty("授权状态（0待授权、1正常、2试用、3高级会员、4VIP会员、5高级VIP会员）")
    @TableField("authorize_status")
    private Integer authorizeStatus;

    @ApiModelProperty("授权到期日期")
    @TableField("end_date")
    private LocalDateTime endDate;

    @ApiModelProperty("存储容量（单位G）")
    @TableField("storage_capacity")
    private Double storageCapacity;

}
