package upc.c505.modular.villageiot.controller.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author: frd
 * @create-date: 2023/11/4 9:23
 */
@Data//lombok自动生成增删改查
@Accessors(chain = true)//链式编程
@EqualsAndHashCode(callSuper = false)
public class HostConfigPageSearchParam extends PageBaseSearchParam {
}
