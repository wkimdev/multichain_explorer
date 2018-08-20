package kr.doublechain.basic.explorer.listener;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.service.UpdateBlockService;
import kr.doublechain.basic.explorer.service.dcc.DccService;

/**
 * block을 다운받기 위한 listener
 *
 */
@EnableAsync
@Component
public class UpdateBlockListener {

	@Autowired
	DccService dccService;

	@Autowired
	UpdateBlockService updateBlockService;
	
	/**
	 * Node에서 블록정보를 불러와 db에 update 
	 * TODO scheduler 적용하기.
	 * 
	 * @return void
	 * @throws Exception
	 */
//	@Async
//	@EventListener(ApplicationReadyEvent.class)
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
				updateBlockService.mergeTx(currentHeight);
				System.out.println("Update Block : " + currentHeight);
			} else {
				Thread.sleep(1000);
			}
		}

	}

}
