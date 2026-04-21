package upc.c505.modular.ecochain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @author la
 * @since 2025-04-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_ai_simulation_reverse_assessment")
@ApiModel(value = "EcoChainAiSimulationReverseAssessment对象", description = "")
public class EcoChainAiSimulationReverseAssessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("AI模拟反向测评")
    @TableId("id")
    private Long id;

    @ApiModelProperty("人员姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty("过程跟踪数量")
    @TableField("process_tracking_number")
    private Long processTrackingNumber;

    @ApiModelProperty("完成记录数量")
    @TableField("completed_records_number")
    private Long completedRecordsNumber;

    @ApiModelProperty("工作得分（过程跟踪数量*工作记录得分）")
    @TableField("work_score")
    private Long workScore;

    @ApiModelProperty("绩效得分（完成记录数量*签单成交得分）")
    @TableField("performance_score")
    private Long performanceScore;

    @ApiModelProperty("基础分（公司给每个人员单独配置）")
    @TableField("basic_score")
    private Long basicScore;

    @ApiModelProperty("总计得分（工作得分、绩效得分、基础分之和）")
    @TableField("total_score")
    private Long totalScore;

    @ApiModelProperty("k值（基础工资÷总计得分）")
    @TableField("k_value")
    private Double kValue;

    @ApiModelProperty("排名（根据K值从小到大排序，1,2,3,4,5这样排）")
    @TableField("rank_number")
    private Integer rankNumber;

    @ApiModelProperty("创建时间")
    @TableField(value = "add_datetime", fill = FieldFill.INSERT)
    private LocalDateTime addDatetime;

    @ApiModelProperty("创建人")
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("操作人")
    @TableField(value = "operator", fill = FieldFill.UPDATE)
    private String operator;

    @ApiModelProperty("操作时间")
    @TableField(value = "operation_datetime", fill = FieldFill.UPDATE)
    private LocalDateTime operationDatetime;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("从业人员id")
    @TableField("people_enterprise_id")
    private Long peopleEnterpriseId;

    @ApiModelProperty("基础工资")
    @TableField("basic_salary")
    private Long basicSalary;
}
