package kr.doublechain.basic.explorer.schedule.jobs;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.schedule.UpdateBlockService;
import kr.doublechain.basic.explorer.service.dcc.DccService;

/**
 * DccReceiverJob
 * run method를 통한, 스케줄 동작
 *
 */
@EnableWebSocketMessageBroker
@Component
public class DccReceiverJob {
	
	@Autowired
	DccService dccService;

	@Autowired
	UpdateBlockService updateBlockService;
	
	@Autowired
	WebsocketScheduler websocketScheduler;
	
	private final Logger LOG = LoggerFactory.getLogger(DccReceiverJob.class);
	
    @Scheduled(cron = "*/1 * * * * *")
    public void dccReceiverJob() throws Exception {
       LOG.info("=============== this is dccReceiverJob start!!! ===============");
        
       BigInteger currentHeight = null;
       JsonObject currentBlock = updateBlockService.init();
		
		currentHeight = updateBlockService.checkBlock(currentBlock);
		currentBlock = dccService.getBlock(currentHeight);
		
			if (currentBlock.has("nextblockhash")) {
			updateBlockService.mergeBlock(currentBlock);
			
			// 현재 블록 넘버 + 1 update		
			currentHeight = currentHeight.add(new BigInteger("1"));
			currentBlock = dccService.getBlock(currentHeight);

			updateBlockService.mergeBlock(currentBlock);
			updateBlockService.mergeTx(currentHeight);
			LOG.info("===============Update Block : " + currentHeight+" ===============");
			
			LOG.info("============== Websocket start >>>>>>> ===============");
			
//			websocketScheduler.broadcastingAccessCnt();
//			websocketScheduler.broadcastingSpeedCnt();
			websocketScheduler.broadcastingMessage();
			websocketScheduler.broadcastingSpeedList();
			
			LOG.info("============== Websocket end >>>>>>> ===============");
			
		}
    }
	
}
