package com.yyd.littletest;

import com.yyd.littletest.Util.JsonUtil;
import com.yyd.littletest.entity.SongEntity;
import com.yyd.littletest.entity.UserEntity;
import com.yyd.littletest.server.resp.BaseResp;
import com.yyd.littletest.server.resp.CmdOneResp;
import com.yyd.littletest.server.resp.ObjectResp;
import com.yyd.littletest.server.resp.SocketMessageData;
import com.yyd.littletest.server.resp.SongResp;
import com.yyd.littletest.server.resp.UserInfoResp;

import org.junit.Test;

import static com.yyd.littletest.Activity.MainActivity.ROBOT_SSID;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public static final int RESP_CODE_SUCCESS = 0;
    public static final String CMD_ONE = "cmd_1";
    public static final String CMD_TWO = "cmd_2";
    public static final String CMD_PUSH_SONG = "push/song";

    @Test
    public void test1() {
        String dataStr = String.format("[%1$s]<%2$s>", "111", "222");
        System.out.println(dataStr);
        String s1 = "robot_ap";
        String s2 = "robot_ap";
        System.out.println(ROBOT_SSID.equals(s2));

    }

    @Test
    public void httpResponseDemo() throws Exception {
        assertEquals(4, 2 + 2);
        // 服务器生成response json的代码
        UserInfoResp userInfoRespServer = new UserInfoResp();
        userInfoRespServer.code = RESP_CODE_SUCCESS;
        userInfoRespServer.msg = "success";   //成功msg可以为null，这里只是举例
        userInfoRespServer.data = new UserEntity("james", 11);
        String responseJson = JsonUtil.toJson(userInfoRespServer);
        System.out.println(responseJson); //{"code":0,"msg":"success","data":{"name":"James","age":11}}

        // 客户端拿到response 解析的代码
        UserInfoResp userInfoRespClient = JsonUtil.fromJson(responseJson, UserInfoResp.class);
        if (userInfoRespClient.code == RESP_CODE_SUCCESS) {
            UserEntity userEntity = userInfoRespClient.data;
            // callback.onSuccess(userInfo)
        } else {
            // callback.onFailed(userInfoRespClient.code, userInfoRespClient.msg)
        }

    }

    @Test
    public void socketResponseDemo() {
        // 服务器生成response json的代码
        BaseResp<SongEntity> songResp = new BaseResp<>();
        songResp.cmd = CMD_PUSH_SONG;
        songResp.data = new SongEntity(1123L, "http://www.yyd.com/song/丢手绢.mp3", "丢手绢");;
        String responseJson = JsonUtil.toJson(songResp);
        System.out.println(responseJson);   //{"code":0,"cmd":"push/song","data":{"id":1123,"url":"http://www.yyd.com/song/丢手绢.mp3","name":"丢手绢"}}com.yyd.littletest.server.resp.SongResp@2471cca7

        // 客户端拿到response 解析的代码
        ObjectResp objectResp = JsonUtil.fromJson(responseJson, ObjectResp.class);
        switch (objectResp.cmd) {
            case CMD_PUSH_SONG:
                // DO ANYTHING
                SongResp songresp = JsonUtil.fromJson(responseJson, SongResp.class);
                System.out.println(JsonUtil.toJson(songResp));
                int code = songresp.code;
                String msg = songresp.msg;
                SongEntity songEntity1 = songresp.data;
                break;
            case CMD_TWO:
                //
                break;
        }
    }

    @Test
    public void testString() {
        String s = "{\"controller\":0,\"phone\":13570464488,\"headshot\":\"\",\"sex\":\"小公子\",\"name\":\"\",\"nickname\":\"詹徐照ggg\",\"online\":true,\"id\":100461,\"addr\":\"120.24.242.163\",\"iconUri\":\"/user/ico/100461.png\",\"iconHost\":\"http://120.24.242.163\"}";
        Object o = s;
        System.out.println("o: ");
        System.out.println((String)o);
        System.out.println("to string:");
        System.out.println(o.toString());
    }

}