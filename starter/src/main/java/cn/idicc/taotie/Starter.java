package cn.idicc.taotie;

import cn.idicc.billund.common.starter.annotation.EnableBillund;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication(scanBasePackages = {"cn.idicc.billund", "cn.idicc.taotie", "cn.idicc.component.login"})
//@ComponentScan(basePackages = {"cn.idicc.billund.*","cn.idicc.taotie.*"})
@EnableBillund
public class Starter {
	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}
}
