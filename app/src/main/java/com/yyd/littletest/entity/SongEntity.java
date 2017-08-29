package com.yyd.littletest.entity;

/**
 * <pre>
 *     author : ZhanXuzhao
 *     e-mail : zhanxuzhao@yydrobot.com
 *     time   : 2017/08/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SongEntity {
    public Long id;
    public String url;
    public String name;

    public SongEntity(Long id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }
}
