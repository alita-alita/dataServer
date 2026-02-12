package cn.idicc.taotie.infrastructment.request.spider;

import com.alibaba.fastjson2.JSONObject;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SpiderEntity implements Serializable {

    @NotNull(message = "source can not be null")
    private String source;
    @NotNull(message = "url can not be null")
    @URL
    private String url;
    @NotNull(message = "business can not be null")
    private String business;
    @NotNull(message = "data can not be null")
    private JSONObject data;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
