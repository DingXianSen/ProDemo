package com.huidaxuan.ic2cloud.selectlistdata.entity;

/**
 * @packageName:com.huidaxuan.ic2cloud.selectlistdata.entity
 * @className: ServiceStationEntity
 * @description:预约服务工位实体
 * @author: dingchao
 * @time: 2020-09-27 16:26
 */
public class ServiceStationEntity {
    private String time;
    private String stationNowNum;
    private String stationAllNum;
    private boolean selFlag;

    public ServiceStationEntity(String time, String stationNowNum, String stationAllNum, boolean selFlag) {
        this.time = time;
        this.stationNowNum = stationNowNum;
        this.stationAllNum = stationAllNum;
        this.selFlag = selFlag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStationNowNum() {
        return stationNowNum;
    }

    public void setStationNowNum(String stationNowNum) {
        this.stationNowNum = stationNowNum;
    }

    public String getStationAllNum() {
        return stationAllNum;
    }

    public void setStationAllNum(String stationAllNum) {
        this.stationAllNum = stationAllNum;
    }

    public boolean isSelFlag() {
        return selFlag;
    }

    public void setSelFlag(boolean selFlag) {
        this.selFlag = selFlag;
    }
}
