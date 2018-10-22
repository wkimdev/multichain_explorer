package kr.doublechain.basic.explorer.schedule.jobs;

import java.util.List;

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
import kr.doublechain.basic.explorer.service.couch.vo.FingerPrintCntResponse;
import kr.doublechain.basic.explorer.service.couch.vo.SpeedCntResponse;
import kr.doublechain.basic.explorer.service.couch.vo.SpeedDataResponse;
import kr.doublechain.basic.explorer.service.couch.vo.SpeedListVO;
import kr.doublechain.basic.explorer.service.user.UserService;

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
	
	/**
     * UserService Service
     */
	@Autowired
	UserService userService;

    @Autowired
    public WebsocketScheduler(SimpMessagingTemplate template) {
        this.template = template;
    }
	
    /**
   	 * for data change check
   	 * @throws Exception
   	 * @return String 
   	 */
       //@Scheduled(cron = "0/1 * * * * ?")
       public void checkChange(String flag) throws Exception {
       	String message = "";
       	
       	if(flag == "speed_flag") {
       		
       		message = "speed_change";
       		LOG.debug(" >>>>>>>>>>> Start speed Stream websocket flag >>>>>>>>>>> ");
       		JSONArray jsonArray = couchbaseService.selectStreamBySpeed(); // list
       		JSONArray graphJsonArray = couchbaseService.selectTodaySpeedCnt(); // graph
       		Object count = CommonUtil.convertObjectFromGson(couchbaseService.selectSpeedCntByCurrent()); // count
       		
       		List<SpeedDataResponse> list = CommonUtil.convertObjectFromJsonStringByTypeRef(jsonArray.toString(), new TypeReference<List<SpeedDataResponse>>() {});
       		List<SpeedCntResponse> graphList = CommonUtil.convertObjectFromJsonStringByTypeRef(graphJsonArray.toString(), new TypeReference<List<SpeedCntResponse>>() {});		
       		
       		SpeedListVO speedListVO = new SpeedListVO();
       		speedListVO.setSpeedDataResponse(list);
       		speedListVO.setDataResponse(graphList);
       		speedListVO.setSpeedCnt(count);
       		speedListVO.setMessage(message);
       		template.convertAndSend(WEBSOCKET_BROADCAST_CHANNEL2, speedListVO);
       		
       	} else if(flag == "door_flag") {
       		
       		message = "door_change";
       		LOG.debug(" >>>>>>>>>>> Start Door Access Stream websocket flag >>>>>>>>>>> ");
       		JSONArray jsonArray = couchbaseService.selectStreamByFingerPrint(); // list
       		JSONArray graphJsonArray = couchbaseService.selectTodayDoorAccessCnt(); // graph
       		Object count = CommonUtil.convertObjectFromGson(couchbaseService.selectFingerPrintCntByCurrent()); //count
       		
       		List<DataResponse> list = CommonUtil.convertObjectFromJsonStringByTypeRef(jsonArray.toString(), new TypeReference<List<DataResponse>>() {});
       		List<FingerPrintCntResponse> graphList = CommonUtil.convertObjectFromJsonStringByTypeRef(graphJsonArray.toString(), new TypeReference<List<FingerPrintCntResponse>>() {});
       		
       		FPrintListVO fPrintListVO = new FPrintListVO();
       		fPrintListVO.setDataResponse(list);
       		fPrintListVO.setDataResponseGraph(graphList);
       		fPrintListVO.setDoorAccessCnt(count);
       		fPrintListVO.setMessage(message);
       		template.convertAndSend(WEBSOCKET_BROADCAST_CHANNEL, fPrintListVO);
       	}
       }
       
   	/**
   	 * 지문인식 트랜잭션 리스트, 카운트, graph 데이터 전파
   	 * @throws JsonProcessingException 
   	 * @throws Exception 
   	 */
   	@Scheduled(cron = "0/1 * * * * ?")
   	public void broadcastingDoorAccess() throws Exception {
   		String message = null;
   		
   		JSONArray jsonArray = couchbaseService.selectStreamByFingerPrint(); // list
   		JSONArray graphJsonArray = couchbaseService.selectTodayDoorAccessCnt(); // graph
   		Object count = CommonUtil.convertObjectFromGson(couchbaseService.selectFingerPrintCntByCurrent()); //count
   		
   		List<DataResponse> list = CommonUtil.convertObjectFromJsonStringByTypeRef(jsonArray.toString(), new TypeReference<List<DataResponse>>() {});
   		List<FingerPrintCntResponse> graphList = CommonUtil.convertObjectFromJsonStringByTypeRef(graphJsonArray.toString(), new TypeReference<List<FingerPrintCntResponse>>() {});
   		
   		FPrintListVO fPrintListVO = new FPrintListVO();
   		fPrintListVO.setDataResponse(list);
   		fPrintListVO.setDataResponseGraph(graphList);
   		fPrintListVO.setDoorAccessCnt(count);
   		fPrintListVO.setMessage(message);
   		template.convertAndSend(WEBSOCKET_BROADCAST_CHANNEL, fPrintListVO);
   	}
   	
   	/**
   	 * 과속 트랜잭션 리스트, 카운트, graph 데이터 전파
   	 * @throws JsonProcessingException 
   	 * @throws Exception 
   	 */
   	@Scheduled(cron = "0/1 * * * * ?")
   	public void broadcastingSpeedList() throws Exception {
   		String message = null;
   		
   		JSONArray jsonArray = couchbaseService.selectStreamBySpeed(); // list
   		JSONArray graphJsonArray = couchbaseService.selectTodaySpeedCnt(); // graph
   		//JSONArray graphJsonArray = couchbaseService.selectTwoWeeksSpeedCnt(); // test - old
   		//Object twoWeeksDate = CommonUtil.convertObjectFromJSONArray(userService.getTwoWeeksDate()); // test - old
   		Object count = CommonUtil.convertObjectFromGson(couchbaseService.selectSpeedCntByCurrent()); // count
   		 
   		
   		List<SpeedDataResponse> list = CommonUtil.convertObjectFromJsonStringByTypeRef(jsonArray.toString(), new TypeReference<List<SpeedDataResponse>>() {});
   		List<SpeedCntResponse> graphList = CommonUtil.convertObjectFromJsonStringByTypeRef(graphJsonArray.toString(), new TypeReference<List<SpeedCntResponse>>() {});		
   		
   		SpeedListVO speedListVO = new SpeedListVO();
   		speedListVO.setSpeedDataResponse(list);
   		speedListVO.setDataResponse(graphList);
   		speedListVO.setSpeedCnt(count);
   		speedListVO.setMessage(message);
   		//speedListVO.setTwoWeeksDate(twoWeeksDate); // test - old
   		template.convertAndSend(WEBSOCKET_BROADCAST_CHANNEL2, speedListVO);
   	}
    	
}
