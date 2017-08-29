package com.yyd.littletest.server.resp;

/**
 * <pre>
 *     author : ZhanXuzhao
 *     e-mail : zhanxuzhao@yydrobot.com
 *     time   : 2017/08/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BaseResp<T> {
    /**
     * response code
     */
    public int code;

    /**
     * response message
     */
    public String msg;

    /**
     * cmd, 决定 data T 的具体类型
     */
    public String cmd;

    /**
     * response json data, 对应实体类
     */
    public T data;
}
