package upc.c505.modular.ecochain.entity;

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
 * @author byh
 * @since 2025-04-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_work_log")
@ApiModel(value = "EcoChainWorkLog对象", description = "")
public class EcoChainWorkLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("生态链工作日志子关联表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("生态链建库id")
    @TableField("eco_chain_build_warehouse_id")
    private Long ecoChainBuildWarehouseId;

    @ApiModelProperty("生态链过程跟踪id")
    @TableField("eco_chain_process_tracking_id")
    private Long ecoChainProcessTrackingId;

    @TableField("eco_chain_complete_record_id")
    private Long ecoChainCompleteRecordId;

    @ApiModelProperty("评价人")
    @TableField("evaluator")
    private String evaluator;

    @ApiModelProperty("评价日期")
    @TableField("evaluation_date")
    private LocalDateTime evaluationDate;

    @ApiModelProperty("评价结果")
    @TableField("evaluate_results")
    private String evaluateResults;

    @ApiModelProperty("备注")
    @TableField("remarks")
    private String remarks;

    @ApiModelProperty("备用字段1")
    @TableField("str1")
    private String str1;


}
