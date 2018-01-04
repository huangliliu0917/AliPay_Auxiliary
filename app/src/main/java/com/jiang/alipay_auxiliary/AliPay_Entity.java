package com.jiang.alipay_auxiliary;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/1/4
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */

public class AliPay_Entity {

    private List<itemEntity> itemEntities;

    public List<itemEntity> getItemEntities() {
        return itemEntities;
    }

    public void setItemEntities(List<itemEntity> itemEntities) {
        this.itemEntities = itemEntities;
    }

    public static class itemEntity {
        private int id;
        private String money;
        private String message;
        private String qrcode;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        @Override
        public String toString() {
            return "itemEntity{" +
                    "id=" + id +
                    ", money='" + money + '\'' +
                    ", message='" + message + '\'' +
                    ", qrcode='" + qrcode + '\'' +
                    '}';
        }
    }
}
