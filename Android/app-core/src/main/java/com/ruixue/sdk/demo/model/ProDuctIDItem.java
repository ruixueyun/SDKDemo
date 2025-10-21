package com.ruixue.sdk.demo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProDuctIDItem implements Serializable {


    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("code")
    private Integer code;

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static class DataDTO implements Serializable {
        @SerializedName("service_mark")
        private String serviceMark;
        @SerializedName("product_id")
        private String productId;
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private String id;
        @SerializedName("channels")
        private List<ChannelsDTO> channels;

        public String getServiceMark() {
            return serviceMark;
        }

        public void setServiceMark(String serviceMark) {
            this.serviceMark = serviceMark;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<ChannelsDTO> getChannels() {
            return channels;
        }

        public void setChannels(List<ChannelsDTO> channels) {
            this.channels = channels;
        }

        public static class ChannelsDTO implements Serializable {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("type")
            private Integer type;
            @SerializedName("wechat_appid")
            private String wechatAppid;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Integer getType() {
                return type;
            }

            public void setType(Integer type) {
                this.type = type;
            }

            public String getWechatAppid() {
                return wechatAppid;
            }

            public void setWechatAppid(String wechatAppid) {
                this.wechatAppid = wechatAppid;
            }
        }
    }
}
