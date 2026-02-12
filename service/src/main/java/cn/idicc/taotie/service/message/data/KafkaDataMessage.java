package cn.idicc.taotie.service.message.data;

/**
 * @Author: MengDa
 * @Date: 2023/8/15
 * @Description:
 * @version: 1.0
 */
public class KafkaDataMessage {
    private String business_code;

    private String action;

    private DataEntity data;

    private String batch;


    public String getBusiness_code() {
        return business_code;
    }

    public void setBusiness_code(String business_code) {
        this.business_code = business_code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        return "BillundMessage{" +
                "business_code='" + business_code + '\'' +
                ", action='" + action + '\'' +
                ", data=" + data +
                ", batch='" + batch + '\'' +
                '}';
    }
}
