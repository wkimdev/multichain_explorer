package kr.doublechain.basic.explorer.contorller;

import java.math.BigInteger;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.doublechain.basic.explorer.common.code.Constants;
import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.common.vo.DccResponse;
import kr.doublechain.basic.explorer.common.vo.Header;
import kr.doublechain.basic.explorer.common.vo.Meta;
import kr.doublechain.basic.explorer.service.couch.CouchbaseService;
import kr.doublechain.basic.explorer.service.couch.vo.DataResponse;
import kr.doublechain.basic.explorer.service.couch.vo.FPrintListVO;
import kr.doublechain.basic.explorer.service.couch.vo.FingerPrintCntResponse;
import kr.doublechain.basic.explorer.service.couch.vo.FingerPrintCntVO;
import kr.doublechain.basic.explorer.service.couch.vo.FingerPrintVO;
import kr.doublechain.basic.explorer.service.couch.vo.SpeedCntResponse;
import kr.doublechain.basic.explorer.service.couch.vo.SpeedCntVO;
import kr.doublechain.basic.explorer.service.couch.vo.SpeedDataResponse;
import kr.doublechain.basic.explorer.service.couch.vo.SpeedListVO;
import kr.doublechain.basic.explorer.service.couch.vo.SpeedVO;
import kr.doublechain.basic.explorer.service.dcc.DccService;
import kr.doublechain.basic.explorer.service.user.UserService;

/**
 * Dcc API for Explorer
 * Block Controller 
 * Stream 의 스피딩, 지문인식 데이터
 *
 */
@RestController
@CrossOrigin
@Api(tags = "Explorer")
@RequestMapping("${api.contextRoot}")
public class DccController {
	
	private static final Logger LOG = LoggerFactory.getLogger(DccController.class);
	
	/**
     * Dcc Node Service
     */
	@Autowired
	DccService dccService;
	
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
	
	/**
	 * Explorer 노드 블록 정보 호출 for test
	 * @param 
	 * @return String
	 * @throws Exception
	 */
	@ApiOperation(value = "block info", notes = "node로 부터 블록 정보를 가져온다.")
	@GetMapping(value="/getinfo")
    public DccResponse<Object> getBlockByHash() throws Exception {
		return CommonUtil.Response(new Header(), CommonUtil.convertObjectFromGson(dccService.getInfo()), new Meta());
    }	
    
    /**
	 * Speeding Stream Search API - 검색어(txId for Stream Search)
	 *  
	 * @param 
	 * @return StreamVO
	 * @throws Exception
	 */
	@ApiOperation(value = "스피딩 스트림 검색", notes = "speeding Id 검색어로 스트림 상세 정보를 가져온다.")
    @GetMapping(value="/search/speeds/{searches}")
    @ResponseBody
    public DccResponse<SpeedVO> searchStream(@PathVariable("searches")String search, HttpServletResponse response) throws Exception {
		Header header = new Header();
		Meta meta = new Meta();

		JsonObject jsonObject = couchbaseService.selectSpeedBySearch(search);
		
		
		// 현재 검색한 트랜잭션의 블럭 넘버 정보 호출
		JsonObject searchHeight = new JsonObject();
		searchHeight.add("row", jsonObject.getAsJsonObject());
		JsonElement searchBlock = searchHeight.getAsJsonObject("row").get("height");		
		
		
		// DB에서 최신 블럭 호출.
		JsonObject getLastBlock = couchbaseService.selectLastBlock();		
		JsonObject test = new JsonObject();
		test.add("row", getLastBlock.getAsJsonObject());
		JsonElement lastBlock = test.getAsJsonObject("row").get("height");
		
		// confirm check
		BigInteger CheckConfirmNum = getConfirmCheck(lastBlock, searchBlock);
		jsonObject.addProperty("checkConfirmNum", CheckConfirmNum);
		
		SpeedVO speedVO = CommonUtil.convertObjectFromJsonString(jsonObject.toString(), SpeedVO.class);
		
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
		return CommonUtil.Response(header, speedVO, meta);
    }
	
	/**
	 * FingerPrint Stream Search API - 검색어(txId for Stream Search)
	 *  
	 * @param 
	 * @return StreamVO
	 * @throws Exception
	 */
	@ApiOperation(value = "지문 스트림 검색", notes = "finger txid Id 검색어로 스트림 상세 정보를 가져온다.")
    @GetMapping(value="/search/fingerprints/{searches}")
    @ResponseBody
    public DccResponse<FingerPrintVO> searchFingerPrint(@PathVariable("searches")String search, HttpServletResponse response) throws Exception {
		Header header = new Header();
		Meta meta = new Meta();

		JsonObject jsonObject = couchbaseService.selectFingerPrintBySearch(search);
		
		// 현재 검색한 트랜잭션의 블럭 넘버 정보 호출
		JsonObject searchHeight = new JsonObject();
		searchHeight.add("row", jsonObject.getAsJsonObject());
		JsonElement searchBlock = searchHeight.getAsJsonObject("row").get("height");		
		
		
		// DB에서 최신 블럭 호출.
		JsonObject getLastBlock = couchbaseService.selectLastBlock();		
		JsonObject test = new JsonObject();
		test.add("row", getLastBlock.getAsJsonObject());
		JsonElement lastBlock = test.getAsJsonObject("row").get("height");
		
		// confirm check
		BigInteger CheckConfirmNum = getConfirmCheck(lastBlock, searchBlock);
		jsonObject.addProperty("checkConfirmNum", CheckConfirmNum);
				
		FingerPrintVO fingerPrintVO = CommonUtil.convertObjectFromJsonString(jsonObject.toString(), FingerPrintVO.class);
		
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
		return CommonUtil.Response(header, fingerPrintVO, meta);
	}
	
	/**
	 * confirm = 현재 디비에서 조회되는 최신블록(지문 * 스피딩 통합) - 선택한 블록 높이.
	 *  
	 * @param 
	 * @return BigInteger
	 * @throws Exception
	 */
	@ApiOperation(value = "컨펌 체크", notes = "검색한 트랜잭션에 대한 블럭 컨펌정보를 반환한다.")
    @GetMapping(value="/search/confirmcheck")
    @ResponseBody
    public BigInteger getConfirmCheck(JsonElement lastBlock, JsonElement searchBlock) throws Exception{		
		BigInteger b1 = BigInteger.valueOf(lastBlock.getAsInt());		
		BigInteger b2 = BigInteger.valueOf(searchBlock.getAsInt());		
		return b1.subtract(b2);
    }
		
	
    /**
	 * 현재날짜기준. Stream에 포함된 speeding count.
	 * 
	 * @return Object 
	 * @throws Exception
	 */
	@ApiOperation(value = "금일 생성된 스피딩 스트림 카운트", notes = "현재날짜기준. 생성 스트림에 포함된 speeding count.")
    @GetMapping(value ="/speedCnts/current")
    @ResponseBody
    public Object getSpeedCnyByCurrent() throws Exception{
    	return CommonUtil.convertObjectFromGson(couchbaseService.selectSpeedCntByCurrent());
    }
	
    /**
	 * 현재날짜기준. Stream에 포함된 fingerPrint count.
	 * 
	 * @return Object 
	 * @throws Exception
	 */
	@ApiOperation(value = "금일 생성된 지문 스트림 카운트", notes = "현재날짜기준. 생성 스트림에 포함된 fingerPrint count.")
    @GetMapping(value ="/fingerPrintCnts/current")
    @ResponseBody
    public Object getFingerPrintCnyByCurrent() throws Exception{
    	return CommonUtil.convertObjectFromGson(couchbaseService.selectFingerPrintCntByCurrent());
    }
	
    /**
	 * 최근 생성 스피딩 정보 요약
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
	@ApiOperation(value = "최근 생성 스피딩 정보", notes = "최근 생성된 스피딩 상세 정보 (리스트 10개 호출).")
    @RequestMapping("/speeds/lists")
    @ResponseBody
    public DccResponse<SpeedListVO> getSpeedIdLists() throws Exception{
		
		Header header = new Header();
		Meta meta = new Meta();
		
		JSONArray jsonArray = couchbaseService.selectStreamBySpeed();
		List<SpeedDataResponse> list = CommonUtil.convertObjectFromJsonStringByTypeRef(jsonArray.toString(), new TypeReference<List<SpeedDataResponse>>() {});
		SpeedListVO speedListVO = new SpeedListVO();
		speedListVO.setSpeedDataResponse(list);
		
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
    	return CommonUtil.Response(header, speedListVO, meta);
    }
	
    /**
	 * 최근 생성 지문 정보 요약
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
	@ApiOperation(value = "최근 생성 지문 정보", notes = "최근 생성된 스피딩 상세 정보 (리스트 10개 호출).")
    @RequestMapping("/fingerPrints/lists")
    @ResponseBody
    public DccResponse<FPrintListVO> getFingerPrintIdLists() throws Exception{
		Header header = new Header();
		Meta meta = new Meta();
		
		JSONArray jsonArray = couchbaseService.selectStreamByFingerPrint();
		List<DataResponse> list = CommonUtil.convertObjectFromJsonStringByTypeRef(jsonArray.toString(), new TypeReference<List<DataResponse>>() {});
		FPrintListVO fPrintListVO = new FPrintListVO();
		fPrintListVO.setDataResponse(list);
		
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
    	return CommonUtil.Response(header, fPrintListVO, meta);
    }
	
	/**
 	 * 현재 날짜 기준 2주간 date
 	 * 
 	 * @param 
 	 * @return 
 	 * @throws Exception
 	 */
	 @ApiOperation(value = "2주 기간 날짜 리스트", notes = "현재 날짜 기준 2주간 date.")
     @RequestMapping("/twoweeks")
     @ResponseBody
     public Object getTwoWeeksDate() throws Exception {
     	return CommonUtil.convertObjectFromJSONArray(userService.getTwoWeeksDate());
     }
	
    
    /**
 	 * 2week speeding graph
 	 * 최근 2주(14일)간 발생된 일별 과속단속 카메라 촬영 건수 그래프-txid기준 카운트 
 	 * 
 	 * @param 
 	 * @return 
 	 * @throws Exception
 	 */
	 @ApiOperation(value = "2주간 발생된 스트림 데이터", notes = "최근 2주(14일)간 발생된 일별 과속단속 카메라 촬영 건수 그래프 데이터.")
     @RequestMapping("/speeds/graph")
     @ResponseBody
     public DccResponse<SpeedCntVO> getTwoWeeksSpeeds() throws Exception {
		 
		Header header = new Header();
		Meta meta = new Meta();
		
		//JSONArray jsonArray = couchbaseService.selectTodaySpeedCnt(); // graph - 시간별 (origin)
		JSONArray jsonArray = couchbaseService.selectTwoWeeksSpeedCnt(); // graph - 2주내 날짜별
		List<SpeedCntResponse> list = CommonUtil.convertObjectFromJsonStringByTypeRef(jsonArray.toString(), new TypeReference<List<SpeedCntResponse>>() {});		
		SpeedCntVO speedCntVO = new SpeedCntVO();
		speedCntVO.setDataResponse(list);
			
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
    	return CommonUtil.Response(header, speedCntVO, meta);
     }
	 
	 /**
 	 * 2week speeding graph
 	 * 최근 2주(14일)간 일별 출입인증 시도 건수 그래프 
 	 * 
 	 * @param 
 	 * @return 
 	 * @throws Exception
 	 */
	 @ApiOperation(value = "2주간 발생된 스트림 데이터", notes = "최근 2주(14일)간 발생된 일별 출입인증 시도 건수 그래프 데이터.")
     @RequestMapping("/fingerPrints/graph")
     @ResponseBody
     public DccResponse<FingerPrintCntVO> getTwoWeeksFingerPrints() throws Exception {
		 
		Header header = new Header();
		Meta meta = new Meta();
			
		//JSONArray jsonArray = couchbaseService.selectTwoWeeksFingerPrints();
		JSONArray jsonArray = couchbaseService.selectTodayDoorAccessCnt();
		List<FingerPrintCntResponse> list = CommonUtil.convertObjectFromJsonStringByTypeRef(jsonArray.toString(), new TypeReference<List<FingerPrintCntResponse>>() {});		
		FingerPrintCntVO fingerPrintCntVO = new FingerPrintCntVO();
		fingerPrintCntVO.setDataResponse(list);
			
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
    	return CommonUtil.Response(header, fingerPrintCntVO, meta);
     }

}
