package cn.idicc.taotie;

import cn.idicc.component.login.application.config.security.interfaces.PermissionRelease;
import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.taotie.service.job.EnterpriseProductToVectorJob;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomethingController {

	@Autowired
	private EnterpriseProductToVectorJob enterpriseProductToVectorJob;;

	@TokenByPass
	@PermissionRelease
	@RequestMapping("/something")
	public String ok() {
		enterpriseProductToVectorJob.doJob();
		return "ok";
	}

}
