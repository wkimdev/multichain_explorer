package kr.doublechain.basic.explorer.contorller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.doublechain.basic.explorer.common.code.Constants;
import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.common.vo.DccResponse;
import kr.doublechain.basic.explorer.common.vo.Header;
import kr.doublechain.basic.explorer.common.vo.Meta;
import kr.doublechain.basic.explorer.service.couch.CouchbaseService;
import kr.doublechain.basic.explorer.service.couch.vo.FPrintListVO;
import kr.doublechain.basic.explorer.service.couch.vo.FingerPrintVO;
import kr.doublechain.basic.explorer.service.couch.vo.SpeedVO;
import kr.doublechain.basic.explorer.service.dcc.DccService;

/**
 * Dcc API for Explorer
 * Block Controller 
 * Stream 의 스피딩, 지문인식 데이터
 *
 */
@RestController
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
		FingerPrintVO fingerPrintVO = CommonUtil.convertObjectFromJsonString(jsonObject.toString(), FingerPrintVO.class);
		
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
		return CommonUtil.Response(header, fingerPrintVO, meta);
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
    public DccResponse<SpeedVO> getSpeedIdLists() throws Exception{
		
		Header header = new Header();
		Meta meta = new Meta();
		
		JSONArray jsonArray = couchbaseService.selectStreamBySpeed();
		SpeedVO speedVO = CommonUtil.convertObjectFromJsonString(jsonArray.toString(), SpeedVO.class);
		
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
    	return CommonUtil.Response(header, speedVO, meta);
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
		FPrintListVO vo = new FPrintListVO(); 
		ObjectMapper mapper = new ObjectMapper();
		
		System.out.println("=====================================================");
		System.out.println(jsonArray.toString());
		LOG.info(jsonArray.toString());
		List<FPrintListVO.DataResponse> list = mapper.readValue(jsonArray.toString(), List.class);
		
		//FPrintListVO fPrintListVO = CommonUtil.convertObjectFromString(list.toString(), FPrintListVO.class);
		
		FPrintListVO fPrintListVO = new FPrintListVO(); 
//		ObjectMapper mapper = new ObjectMapper();
//		List<FPrintListVO.DataResponse> list = mapper.readValue(test, List.class);
		
		
//		JsonObject jsonObject = couchbaseService.selectFingerPrintBySearch(search);
//		FingerPrintVO fingerPrintVO = CommonUtil.convertObjectFromJsonString(jsonObject.toString(), FingerPrintVO.class);
		
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
    	return CommonUtil.Response(header, fPrintListVO, meta);
    }
	
    
    /**
 	 * week speeding graph
 	 * 최근 2주(14일)간 발생된 일별 과속단속 카메라 촬영 건수 그래프
 	 * 
 	 * @param 
 	 * @return 
 	 * @throws Exception
 	 */
//	 @ApiOperation(value = "2주간 발생된 스트림 데이터", notes = "최근 2주(14일)간 발생된 일별 과속단속 카메라 촬영 건수 그래프 데이터.")
//     @RequestMapping("/blocks/graph")
//     @ResponseBody
//     public Object getWeekSpeeding() throws Exception{
//     	return CommonUtil.convertObjectFromGson(couchbaseService.selectTwoWeeksSpeedCnt());
//     }
//    
}
