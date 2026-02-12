package cn.idicc.taotie.service.message.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author: MengDa
 * @Date: 2023/8/15
 * @Description:
 * @version: 1.0
 */
public class DataEntity {
    private Map<String,String> element;


    private Long key_id;

    public Map<String, String> getElement() {
        return element;
    }

    public void setElement(Map<String, String> element) {
        this.element = element;
    }

    public Long getKey_id() {
        return key_id;
    }

    public void setKey_id(Long key_id) {
        this.key_id = key_id;
    }

    @Override
    public String toString() {
        return "DataEntity{" +
                "element=" + element +
                ", key_id=" + key_id +
                '}';
    }
}
