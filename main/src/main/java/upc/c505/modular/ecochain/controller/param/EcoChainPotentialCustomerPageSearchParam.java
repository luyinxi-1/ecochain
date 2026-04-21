package upc.c505.modular.ecochain.controller.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EcoChainPotentialCustomerPageSearchParam extends PageBaseSearchParam {
}
