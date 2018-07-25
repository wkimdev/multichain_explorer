package kr.doublechain.basic.explorer.listener;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.service.DccService;
import kr.doublechain.basic.explorer.service.UpdateBlockService;

@EnableAsync
@Component
public class UpdateBlockListener {

	@Autowired
	DccService dccService;

	@Autowired
	UpdateBlockService updateBlockService;

	@Async
	@EventListener(ApplicationReadyEvent.class)
	public void start() throws Exception {
		BigInteger currentHeight = null;
		JsonObject currentBlock = updateBlockService.init();

		while (true) {

			currentHeight = updateBlockService.checkBlock(currentBlock);
			currentBlock = dccService.getBlock(currentHeight);

			if (currentBlock.has("nextblockhash")) {
				updateBlockService.mergeBlock(currentBlock); // nextblockhash 업데이트

				currentHeight = currentHeight.add(new BigInteger("1"));
				currentBlock = dccService.getBlock(currentHeight);

				updateBlockService.mergeBlock(currentBlock);
				updateBlockService.mergeTx(currentBlock);
				System.out.println("Update Block : " + currentHeight);
			} else {
				Thread.sleep(1000);
			}
		}

	}

}
