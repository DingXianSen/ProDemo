package com.huidaxuan.ic2cloud.meituanshoppingcart.enitty;

import java.util.List;

/**
 * @packageName:com.huidaxuan.ic2cloud.meituanshoppingcart.enitty
 * @className: ProductListEntity
 * @description:商品列表2级
 * @author: dingchao
 * @time: 2020-11-18 10:53
 */
public class ProductListEntity {
    private String typeId;
    private String typeName;
    private List<ProductEntity> productEntities;

    public ProductListEntity(String typeId, String typeName, List<ProductEntity> productEntities) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.productEntities = productEntities;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    public static class ProductEntity {
        private String productImg;
        private String productId;
        private String productName;
        private String productMonth;
        private Double productMoney;
        private int productCount;

        public ProductEntity(String productImg, String productName, String productMonth, Double productMoney, int productCount,String productId) {
            this.productImg = productImg;
            this.productName = productName;
            this.productMonth = productMonth;
            this.productMoney = productMoney;
            this.productCount = productCount;
            this.productId = productId;
        }


        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductMonth() {
            return productMonth;
        }

        public void setProductMonth(String productMonth) {
            this.productMonth = productMonth;
        }

        public Double getProductMoney() {
            return productMoney;
        }

        public void setProductMoney(Double productMoney) {
            this.productMoney = productMoney;
        }

        public int getProductCount() {
            return productCount;
        }

        public void setProductCount(int productCount) {
            this.productCount = productCount;
        }
    }
}
