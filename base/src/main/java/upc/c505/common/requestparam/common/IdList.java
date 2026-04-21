package upc.c505.common.requestparam.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用于接收id列表的类
 *
 * @author qiutian
 */
@Data
public class IdList<T> {
    @ApiModelProperty("id列表")
    private List<T> idList;
}
