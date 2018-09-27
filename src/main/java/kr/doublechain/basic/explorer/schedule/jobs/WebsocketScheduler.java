package kr.doublechain.basic.explorer.schedule.jobs;

import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.service.couch.CouchbaseService;
import kr.doublechain.basic.explorer.service.couch.vo.DataResponse;
import kr.doublechain.basic.explorer.service.couch.vo.FPrintListVO;
import kr.doublechain.basic.explorer.service.couch.vo.Message;

/**
 * 
 * WebsocketScheduler
 * 
 * created by basquiat
 *
 */
@EnableWebSocketMessageBroker
@Component
public class WebsocketScheduler {
	
	private static final Logger LOG = LoggerFactory.getLogger(WebsocketScheduler.class);
	
	@Value("${websocket.broadcast.channel}")
	private String WEBSOCKET_BROADCAST_CHANNEL;
	
	@Value("${websocket.broadcast.channel2}")
	private String WEBSOCKET_BROADCAST_CHANNEL2;

	private final SimpMessagingTemplate template;
	
	/**
     * Couchbase Service
     */
	@Autowired
	CouchbaseService couchbaseService;

    @Autowired
    public WebsocketScheduler(SimpMessagingTemplate template) {
        this.template = template;
    }
	
	/**
	 * 6초 간격으로 지문인식 트랜잭션 리스트 전파
	 * @throws JsonProcessingException 
	 * @throws Exception 
	 */
	@Scheduled(cron = "0/6 * * * * ?")
	public void broadcastingMessage() throws Exception {
		JSONArray jsonArray = couchbaseService.selectStreamByFingerPrint();
		List<DataResponse> list = CommonUtil.convertObjectFromJsonStringByTypeRef(jsonArray.toString(), new TypeReference<List<DataResponse>>() {});
		FPrintListVO fPrintListVO = new FPrintListVO();
		fPrintListVO.setDataResponse(list);
		template.convertAndSend(WEBSOCKET_BROADCAST_CHANNEL, fPrintListVO);
	}
	
	/**
	 * 6초 간격으로 지문인식 트랜잭션 id 카운트 전파
	 * @throws JsonProcessingException 
	 * @throws Exception 
	 */
	@Scheduled(cron = "0/6 * * * * ?")
	public void broadcastingAccessCnt() throws Exception {
		Object message = CommonUtil.convertObjectFromGson(couchbaseService.selectFingerPrintCntByCurrent());
		//System.out.println("message : "+message);
		///LOG.info(message); 	
		template.convertAndSend(WEBSOCKET_BROADCAST_CHANNEL2, CommonUtil.convertObjectFromGson(couchbaseService.selectFingerPrintCntByCurrent()));
	}
	
}
