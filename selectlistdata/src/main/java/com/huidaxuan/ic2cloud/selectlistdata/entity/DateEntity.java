package com.huidaxuan.ic2cloud.selectlistdata.entity;

/**
 * @packageName:com.huidaxuan.ic2cloud.selectlistdata.entity
 * @className: DateEntity
 * @description:时间实体
 * @author: dingchao
 * @time: 2020-09-27 10:51
 */
public class DateEntity {
    private String weekName; //日一二三四五
    private String dateName;//9.27,9.28
    private String dateAllName;//2020-9-27

    public DateEntity() {
    }

    public DateEntity(String dateName, String dateAllName) {
        this.dateName = dateName;
        this.dateAllName = dateAllName;
    }

    public DateEntity(String weekName, String dateName, String dateAllName) {
        this.weekName = weekName;
        this.dateName = dateName;
        this.dateAllName = dateAllName;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public String getDateAllName() {
        return dateAllName;
    }

    public void setDateAllName(String dateAllName) {
        this.dateAllName = dateAllName;
    }
}
