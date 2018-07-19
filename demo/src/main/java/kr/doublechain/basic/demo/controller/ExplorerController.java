package kr.doublechain.basic.demo.controller;

import kr.doublechain.basic.demo.config.HealthCheck;
import kr.doublechain.basic.demo.service.ExplorerService;
import kr.doublechain.basic.demo.service.StoreBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 전성국 on 2018-05-23.
 */
@RestController
public class ExplorerController {

    @Autowired
    private HealthCheck healthCheck;

    @Autowired
    private ExplorerService explorerService;

    @Autowired
    private StoreBlock storeBlock;


    @GetMapping("/healthinfo")
    public Health healthInfo() {
        return healthCheck.health();
    }

    @RequestMapping("/explorer")
    void startExplorer() throws Exception {
//        explorerService.ExplorerStart();
        storeBlock.storeBlock();
        explorerService.test();
    }

}
