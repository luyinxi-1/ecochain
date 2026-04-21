package upc.c505.exception;

import java.util.List;

/**
 * 这是一个全局异常处理接口，用于被handler实现。
 * @author wzy
 */
public interface CommonError {

    /**
     * 获取错误码
     * @return 错误码 errCode
     */
    int getErrCode();

    /**
     * 获取错误信息
     * @return 错误信息errMsg
     */
    String getErrMsg();

    /**
     * 获取多条错误信息
     * @return 错误信息list
     */
    List<RuntimeException> getErrList();

    /**
     * 设置错误信息
     * @param errMsg 错误信息
     * @return CommonError
     */
    CommonError setErrMsg(String errMsg);

    /**
     * 设置多条错误信息
     * @param errMsgList 错误信息list
     * @return CommonError
     */
    CommonError setErrMsg(List<RuntimeException> errMsgList);


}
