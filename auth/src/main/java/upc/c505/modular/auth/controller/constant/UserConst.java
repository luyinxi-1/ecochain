package upc.c505.modular.auth.controller.constant;

/**
 * 存放用户类型
 * @author sxz
 * @date 2023/9/1
 **/
public class UserConst {

    /**
     * 超级管理员为-1
     */
    public final static Integer ADMIN = -1;

    /**
     * 二级管理员为-2
     */
    public final static Integer SECOND_ADMIN = -2;

    /**
     * 企业用户为1
     */
    public final static Integer PEOPLE_ENTERPRISE = 1;

    /**
     * 政府人员为2
     */
    public final static Integer PEOPLE_GOVERNMENT = 2;

    /**
     * 网格人员为3
     */
    public final static Integer PEOPLE_GRIDER = 3;

    /**
     * 居民用户为4
     */
    public final static Integer PEOPLE_POPULATION = 4;

    /**
     * 企业人员为5
     */
    public final static Integer PEOPLE_EMPLOYEE = 5;
    /**
     * 默认密码
     */
    public final static String DEFAULT_PASSWORD = "Aa123456+";
}
