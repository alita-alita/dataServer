package cn.idicc.taotie;


import cn.idicc.component.login.application.config.security.interfaces.TokenByPass;
import cn.idicc.taotie.infrastructment.po.data.EnterpriseProductVectorPO;
import cn.idicc.taotie.service.job.EnterpriseProductToVectorJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {

	private static final Logger log = LoggerFactory.getLogger(Health.class);

	@GetMapping("/ok")
	@TokenByPass
	public String ok() {
//        industryChainService.exportChain(72L,"CHAIN.72.5","dev");
//        String fileId = chainVectorService.buildVector("CHAIN.72.8");
//        log.error(fileId);
//        chainVectorService.recall("机构",10);
//		job.doJob();
		return "ok";
	}


}
