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
 * DccReceiverJob run method를 통한, 스케줄 동작
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

	private boolean initFlag = true;

	private BigInteger currentHeight;

	private JsonObject currentBlock;

	private final Logger LOG = LoggerFactory.getLogger(DccReceiverJob.class);

	@Scheduled(cron = "*/1 * * * * *")
	public void dccReceiverJob() throws Exception {
		LOG.info("=============== this is dccReceiverJob start!!! ===============");

		if (this.initFlag) {
			this.currentHeight = null;
			this.currentBlock = updateBlockService.init(this.initFlag);
			this.initFlag = false;
		} else {
			String flag = null;

			this.currentHeight = updateBlockService.checkBlock(this.currentBlock);
			this.currentBlock = dccService.getBlock(this.currentHeight);

			while (this.currentBlock.has("nextblockhash")) {
				updateBlockService.mergeBlock(this.currentBlock);

				// 현재 블록 넘버 + 1 update
				this.currentHeight = this.currentHeight.add(new BigInteger("1"));
				this.currentBlock = dccService.getBlock(this.currentHeight);

				updateBlockService.mergeBlock(this.currentBlock);
				updateBlockService.mergeTx(this.currentHeight, this.initFlag); // flag check

				LOG.info("===============Update Block : " + this.currentHeight + " ===============");
				updateBlockService.checkUpdateStreams(flag);
			}
		}
	}

}
