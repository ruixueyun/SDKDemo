package com.ruixue.sdk.demo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PayPointBean implements Serializable {

    @SerializedName("data")
    private DataDTO data;
    @SerializedName("code")
    private Integer code;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static class DataDTO implements Serializable {
        @SerializedName("pay_goods")
        private PayGoodsDTO payGoods;

        public PayGoodsDTO getPayGoods() {
            return payGoods;
        }

        public void setPayGoods(PayGoodsDTO payGoods) {
            this.payGoods = payGoods;
        }

        public static class PayGoodsDTO implements Serializable {
            @SerializedName("third_goods")
            private List<ThirdGoodsDTO> thirdGoods;
            @SerializedName("public")
            private List<PublicDTO> publicX;

            public List<ThirdGoodsDTO> getThirdGoods() {
                return thirdGoods;
            }

            public void setThirdGoods(List<ThirdGoodsDTO> thirdGoods) {
                this.thirdGoods = thirdGoods;
            }

            public List<PublicDTO> getPublicX() {
                return publicX;
            }

            public void setPublicX(List<PublicDTO> publicX) {
                this.publicX = publicX;
            }

            public static class ThirdGoodsDTO implements Serializable {
                @SerializedName("type")
                private String type;
                @SerializedName("tag")
                private List<TagDTO> tag;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public List<TagDTO> getTag() {
                    return tag;
                }

                public void setTag(List<TagDTO> tag) {
                    this.tag = tag;
                }

                public static class TagDTO implements Serializable {
                    @SerializedName("ruixue_tag")
                    private String ruixueTag;
                    @SerializedName("third_tag")
                    private String thirdTag;

                    public String getRuixueTag() {
                        return ruixueTag;
                    }

                    public void setRuixueTag(String ruixueTag) {
                        this.ruixueTag = ruixueTag;
                    }

                    public String getThirdTag() {
                        return thirdTag;
                    }

                    public void setThirdTag(String thirdTag) {
                        this.thirdTag = thirdTag;
                    }
                }
            }

            public static class PublicDTO implements Serializable {
                @SerializedName("tag")
                private String tag;

                public String getTag() {
                    return tag;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }
            }
        }
    }
}
