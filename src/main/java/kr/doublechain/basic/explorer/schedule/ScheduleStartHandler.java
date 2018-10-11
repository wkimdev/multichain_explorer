package kr.doublechain.basic.explorer.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kr.doublechain.basic.explorer.schedule.jobs.WebsocketScheduler;

@Component
public class ScheduleStartHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduleStartHandler.class);
	
	//@Autowired
	//WebsocketScheduler websocketScheduler;
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() throws Exception {
		LOG.info("application ready event start");
		String flag = "flag";
		
		try {
			//websocketScheduler.broadcastingDoorAccess(flag);
			//websocketScheduler.broadcastingSpeedList(flag);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
