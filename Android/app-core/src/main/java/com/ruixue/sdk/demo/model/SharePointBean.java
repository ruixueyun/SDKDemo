package com.ruixue.sdk.demo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SharePointBean {

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

    public static class DataDTO {
        @SerializedName("id")
        private Integer id;
        @SerializedName("tag")
        private String tag;
        @SerializedName("title")
        private String title;
        @SerializedName("failed_msg")
        private String failedMsg;
        @SerializedName("type")
        private Integer type;
        @SerializedName("status")
        private Integer status;
        @SerializedName("time_interval")
        private Integer timeInterval;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFailedMsg() {
            return failedMsg;
        }

        public void setFailedMsg(String failedMsg) {
            this.failedMsg = failedMsg;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getTimeInterval() {
            return timeInterval;
        }

        public void setTimeInterval(Integer timeInterval) {
            this.timeInterval = timeInterval;
        }
    }
}
