package com.huidaxuan.ic2cloud.prodemo.entity;

import java.util.List;

/**
 * @packageName:com.huidaxuan.ic2cloud.prodemo.grouplist
 * @className: GroupListEntity
 * @description:分组数据实体
 * @author: dingchao
 * @time: 2020-08-03 09:36
 */
public class ServiceListEntity {
    private int groupId;
    private String groupTitle;
    private List<childBean> childBeans;//常规养护

    public ServiceListEntity(int groupId, String groupTitle, List<childBean> childBeans) {
        this.groupId = groupId;
        this.groupTitle = groupTitle;
        this.childBeans = childBeans;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public List<childBean> getChildBeans() {
        return childBeans;
    }

    public void setChildBeans(List<childBean> childBeans) {
        this.childBeans = childBeans;
    }

    public static class childBean {
        private int id;
        private String title;
        private String message;
        private int count;
        private Double money;

        public childBean(int id, String title, String message, int count, Double money) {
            this.id = id;
            this.title = title;
            this.message = message;
            this.count = count;
            this.money = money;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Double getMoney() {
            return money;
        }

        public void setMoney(Double money) {
            this.money = money;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public childBean(int id, String title) {
            this.id = id;
            this.title = title;
        }
    }
}
