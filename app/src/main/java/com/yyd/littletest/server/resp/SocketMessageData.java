package com.yyd.littletest.server.resp;

import java.util.List;

/**
 * <pre>
 *     author : ZhanXuzhao
 *     e-mail : zhanxuzhao@yydrobot.com
 *     time   : 2017/08/08
 *     desc   : Socket response message 的封装， cmd可以确定data对应的实体类
 *     version: 1.0
 * </pre>
 */
public class SocketMessageData {
    /**
     * cmd:push/song
     * data:{"id":1123,"url":"http://www.yyd.com/song/丢手绢.mp3","name":"丢手绢"}
     */

    public String cmd;

    /**
     * 对应一种类型的BaseResp的json数据
     */
    public String data;
}
