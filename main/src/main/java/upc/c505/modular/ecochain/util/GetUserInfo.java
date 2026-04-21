package upc.c505.modular.ecochain.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.people.entity.PeopleEnterprise;
import upc.c505.modular.people.service.IPeopleEnterpriseService;
import upc.c505.modular.supenterprise.entity.SupEnterprise;
import upc.c505.modular.supenterprise.service.ISupEnterpriseService;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @Author: xth
 * @Date: 2024/9/23 9:28
 */
@Component
public class GetUserInfo {

    @Autowired
    private ISupEnterpriseService supEnterpriseService;

    @Autowired
    private IPeopleEnterpriseService peopleEnterpriseService;


    /**
     * 超级管理员登录，返回-1
     * 政府人员或二级管理员用户登录，返回0
     * 企业人员或企业用户登录，返回1
     * 其他人员登录，返回2
     * @return String
     */
    public String getUserType() {
        Integer userType = UserUtils.get().getUserType();
        if (userType == -1) {
            return "-1";
        } else if (userType == -2 || userType == 2) {
            return "0";
        } else if (userType == 1 || userType == 5) {
            return "1";
        } else {
            return "2";
        }
    }

    /**
     * 查询当前登录用户（企业用户）的社会信用代码
     * @return
     */
    public String getSocialCreditCode() {
        if (ObjectUtils.isNotEmpty(UserUtils.get()) && ObjectUtils.isNotEmpty(UserUtils.get().getUserType())) {
            if (UserUtils.get().getUserType() == 1) {
                Long supEnterpriseId = UserUtils.get().getSupEnterpriseId();

                if (ObjectUtils.isNotEmpty(supEnterpriseId)) {
                    SupEnterprise supEnterprise = supEnterpriseService.getById(supEnterpriseId);
                    if (ObjectUtils.isNotEmpty(supEnterprise)) {
                        return supEnterprise.getSocialCreditCode();
                    }
                }
            }
            if (UserUtils.get().getUserType() == 5) {
                Long peopleEnterpriseId = UserUtils.get().getPeopleEnterpriseId();

                if (ObjectUtils.isNotEmpty(peopleEnterpriseId)) {
                    PeopleEnterprise peopleEnterprise = peopleEnterpriseService.getById(peopleEnterpriseId);
                    if (ObjectUtils.isNotEmpty(peopleEnterprise)) {
                        return peopleEnterprise.getSocialCreditCode();
                    }
                }
            }
        }


        return "";
    }

    /**
     * 查询当前登录用户（企业用户）的企业名称
     * @return
     */
    public String getEnterpriseName() {
        if (ObjectUtils.isNotEmpty(UserUtils.get()) && ObjectUtils.isNotEmpty(UserUtils.get().getUserType())) {
            if (UserUtils.get().getUserType() == 1) {
                Long supEnterpriseId = UserUtils.get().getSupEnterpriseId();

                if (ObjectUtils.isNotEmpty(supEnterpriseId)) {
                    SupEnterprise supEnterprise = supEnterpriseService.getById(supEnterpriseId);
                    if (ObjectUtils.isNotEmpty(supEnterprise)) {
                        return supEnterprise.getSupEnterpriseName();
                    }
                }
            }
            if (UserUtils.get().getUserType() == 5) {
                Long peopleEnterpriseId = UserUtils.get().getPeopleEnterpriseId();

                if (ObjectUtils.isNotEmpty(peopleEnterpriseId)) {
                    PeopleEnterprise peopleEnterprise = peopleEnterpriseService.getById(peopleEnterpriseId);
                    if (ObjectUtils.isNotEmpty(peopleEnterprise)) {
                        return peopleEnterprise.getEnterpriseName();
                    }
                }
            }
        }


        return "";
    }

    /**
     * 判断当前登录用户（企业用户）是否是企业管理员
     * 企业管理员返回“1”，企业普通员工返回“0”
     * @return
     */
    public String getIsAdmin(){
        Long peopleEnterpriseId = UserUtils.get().getPeopleEnterpriseId();
        if(ObjectUtils.isNotEmpty(peopleEnterpriseId)){
            PeopleEnterprise peopleEnterprise = peopleEnterpriseService.getById(peopleEnterpriseId);
            if (ObjectUtils.isNotEmpty(peopleEnterprise) && peopleEnterprise.getIsAdmin() == 1) {
                return "1";
            } else {
                return "0";
            }
        }
        return "0";
    }

    /**
     * 获取当前登录用户所在企业的区域id
     * @return
     */

    public Long getAreaId(){
        if (ObjectUtils.isNotEmpty(UserUtils.get()) && ObjectUtils.isNotEmpty(UserUtils.get().getUserType())) {
            if (UserUtils.get().getUserType() == 1) {
                Long supEnterpriseId = UserUtils.get().getSupEnterpriseId();
                if (supEnterpriseId != 0 && ObjectUtils.isNotEmpty(supEnterpriseId)) {
                    SupEnterprise byId = supEnterpriseService.getById(supEnterpriseId);
                    if (ObjectUtils.isNotEmpty(byId)) {
                        return byId.getAreaId();
                    }
                } else {
                    if (ObjectUtils.isNotEmpty(UserUtils.get().getAreaId())) {
                        return UserUtils.get().getAreaId();
                    }
                }
            }
            if (UserUtils.get().getUserType() == 5) {
                Long peopleEnterpriseId = UserUtils.get().getPeopleEnterpriseId();
                if (peopleEnterpriseId != 0 && ObjectUtils.isNotEmpty(peopleEnterpriseId)) {
                    PeopleEnterprise byId = peopleEnterpriseService.getById(peopleEnterpriseId);
                    if (ObjectUtils.isNotEmpty(byId)) {
                        return byId.getEnterpriseAreaId();
                    }
                } else {
                    if (ObjectUtils.isNotEmpty(UserUtils.get().getAreaId())) {
                        return UserUtils.get().getAreaId();
                    }
                }
            }
        }

        return null;
    }

}

