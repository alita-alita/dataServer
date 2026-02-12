package cn.idicc.taotie.infrastructment.error;

/**
 * @Author: WangZi
 * @Date: 2022/12/14
 * @Description: IErrorCode接口
 * @version: 1.0
 */
public interface IErrorCode {
    /**
     * 错误码
     * @return String
     */
    String getCode();

    /**
     * 错误消息
     * @return String
     */
    String getMessage();
}
