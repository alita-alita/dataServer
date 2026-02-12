package cn.idicc.taotie.service.message.spider;

import com.alibaba.fastjson2.JSONObject;

public class MessageEntity {

    private String businessType;

    private String url;

    private String source;

    private JSONObject data;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "businessType='" + businessType + '\'' +
                ", url='" + url + '\'' +
                ", source='" + source + '\'' +
                ", data=" + data +
                '}';
    }
}
