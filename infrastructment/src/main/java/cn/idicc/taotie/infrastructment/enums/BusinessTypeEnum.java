package cn.idicc.taotie.infrastructment.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum BusinessTypeEnum {

    NEWS("newsBusinessHandler");

    private static final Logger logger = LoggerFactory.getLogger(BusinessTypeEnum.class);

    private String beanName;

    BusinessTypeEnum(String beanName){
        this.beanName = beanName;
    }

    public String getBeanName(){
        return beanName;
    }

    public static BusinessTypeEnum get(String name) {
        try {
            return BusinessTypeEnum.valueOf(name);
        } catch (Exception e) {
            logger.error("error type", e);
            return null;
        }
    }

}
