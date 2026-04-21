package upc.c505.modular.auth.controller.param.tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import upc.c505.modular.auth.entity.SysAuthModel;
import upc.c505.utils.MyBeanUtils;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * @author sxz
 */
@ApiModel("权限树节点")
@Data
@Accessors(chain = true)
@Slf4j
public class AuthNode {

    @ApiModelProperty("子节点列表")
    private List<AuthNode> authTreeList;

    @ApiModelProperty("此节点的id")
    private Long id;

    @ApiModelProperty("此节点的名称")
    private String authName;

    @ApiModelProperty("节点类型,0:权限模块，1：菜单，2：按钮，3：其他")
    private Integer type;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("此节点的url")
    private String url;

    @ApiModelProperty("是否拥有权限")
    private Boolean isHad = false;

    @ApiModelProperty("父节点id")
    private Long parentId;

    public void getModelChildren(List<SysAuthModel> authModels, ConcurrentMap<Long, List<AuthNode>> authMap) {
        List<AuthNode> authNodes = new ArrayList<>();
        ListIterator<SysAuthModel> iterator = authModels.listIterator();
        while (iterator.hasNext()) {
            SysAuthModel next = iterator.next();
            //找到了子节点
            if (Objects.equals(next.getParentId(), id)) {
                AuthNode currentAuthModel = MyBeanUtils
                        .copy(next, new AuthNode())
                        .setType(0)
                        .setAuthName(next.getAuthModelName());
                authNodes.add(currentAuthModel);
//                iterator.remove();
                //递推子节点
                currentAuthModel.getModelChildren(authModels, authMap);
                //如果没有子节点了，说明是叶子节点，然后就把它的权限点设置进去
                if (CollectionUtils.isEmpty(currentAuthModel.getAuthTreeList())) {
                    currentAuthModel.setAuthTreeList(authMap.get(currentAuthModel.id));
                }
            }
        }
        authNodes.sort(Comparator.comparing(AuthNode::getSeq));
        this.authTreeList = authNodes;
        if (CollectionUtils.isEmpty(authTreeList)) {
            authTreeList = authMap.get(id);
        }
    }
}
