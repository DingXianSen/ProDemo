package com.huidaxuan.ic2cloud.prodemo.entity;

/**
 * @ClassName: EventBusEntity
 * @Description:Event传递数据实体
 * @Author: dingchao
 * @Date: 2019/3/20 14:11
 */
public class EventBusEntity {
    private int code;
    private String data;
    private String key;

    public EventBusEntity() {
    }

    public EventBusEntity(String data, String key) {
        this.data = data;
        this.key = key;
    }

    public EventBusEntity(int code, String data, String key) {
        this.code = code;
        this.data = data;
        this.key = key;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
