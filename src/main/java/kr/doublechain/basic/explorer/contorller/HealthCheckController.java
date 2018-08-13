package kr.doublechain.basic.explorer.contorller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.doublechain.basic.explorer.config.HealthCheck;

@RestController
public class HealthCheckController {
	
    @Autowired
    private HealthCheck healthCheck;
    
    @GetMapping("/healthinfo")
    public Health healthInfo() {
        return healthCheck.health();
    }
    

}
