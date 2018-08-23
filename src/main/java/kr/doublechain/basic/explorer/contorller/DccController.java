package kr.doublechain.basic.explorer.contorller;

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

import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.doublechain.basic.explorer.common.code.Constants;
import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.common.vo.DccResponse;
import kr.doublechain.basic.explorer.common.vo.Header;
import kr.doublechain.basic.explorer.common.vo.Meta;
import kr.doublechain.basic.explorer.service.couch.CouchbaseService;
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
	 * 4-1. 최근 마지막 생성 블록과, 포함된 speeding count. 
	 * 
	 * @return Object 
	 * @throws Exception
	 */
	@ApiOperation(value = "마지막 생성된 블록", notes = "최근 마지막 생성 블록과, 포함된 speeding count.")
    @GetMapping(value ="/blocks/latest")
    @ResponseBody
    public Object getLatestBlock() throws Exception {
    	return CommonUtil.convertObjectFromGson(couchbaseService.selectLatestBlockCntSpeeding());
    }
    
    /**
	 * 4-2. 현재날짜기준. 생성 블록에 포함된 speeding count.
	 * 
	 * @return Object 
	 * @throws Exception
	 */
	@ApiOperation(value = "금일 생성된 블록", notes = "현재날짜기준. 생성 블록에 포함된 speeding count.")
    @GetMapping(value ="/blocks/current")
    @ResponseBody
    public Object getLatestBlockByCuttent() throws Exception{
    	return CommonUtil.convertObjectFromGson(couchbaseService.selectBlockSpeedingCntByDate());
    }
    
    /**
 	 * 5. week speeding graph
 	 * 최근 2주(14일)간 발생된 일별 과속단속 카메라 촬영 건수 그래프
 	 * 
 	 * @param 
 	 * @return 
 	 * @throws Exception
 	 */
	 @ApiOperation(value = "2주간 발생된 스트림 데이터", notes = "최근 2주(14일)간 발생된 일별 과속단속 카메라 촬영 건수 그래프 데이터.")
     @RequestMapping("/blocks/graph")
     @ResponseBody
     public Object getWeekSpeeding() throws Exception{
     	return CommonUtil.convertObjectFromGson(couchbaseService.selectTwoWeeksSpeedCnt());
     }
    
    /**
	 * 6. 최근 생성 블록 요약(7)
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
	@ApiOperation(value = "최근 생성 블록 요약", notes = "최근 생성된 블록 상세 정보 (리스트 7개 호출).")
    @RequestMapping("/latestBlocks/lists")
    @ResponseBody
    public Object getLatestBlockList() throws Exception{
    	// TODO 유닉스 타임 형변환.
    	return CommonUtil.convertObjectFromJSONArray(couchbaseService.selectBlockByheight());
    }
 
    
    /**
	 * 7. 최근 생성 스피딩 정보 요약(10)
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
	@ApiOperation(value = "최근 생성 스피딩 정보", notes = "최근 생성된 스피딩 상세 정보 (리스트 10개 호출).")
    @RequestMapping("/speeding/lists")
    @ResponseBody
    public DccResponse<SpeedVO> getSpeedIdLists() throws Exception{
		
		Header header = new Header();
		Meta meta = new Meta();
		
		JSONArray jsonArray = couchbaseService.selectStreamByTxId();
		SpeedVO speedVO = CommonUtil.convertObjectFromJsonString(jsonArray.toString(), SpeedVO.class);
		
		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
    	return CommonUtil.Response(header, speedVO, meta);
    }
    
    /**
	 * Speeding Stream의 txid에 대한 컨펌숫자 
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
//	@ApiOperation(value = "블록 컨펌 수", notes = "speeding id에 대한 블록 컨펌 수.")
//    @GetMapping("/confirmations/{txid}")
//    @ResponseBody
//    public DccResponse<StreamVO> getConfirmCnt(@PathVariable("txid")String txid) throws Exception{
//		
//		JsonObject jsonObject = couchbaseService.selectBlockBySearch(txid);
//		StreamVO streamVO = CommonUtil.convertObjectFromJsonString(jsonObject.toString(), StreamVO.class);
//		
//		Header header = new Header();
//		header.setCode(Constants.HTTPSTATUS_OK.ITYPE).setMessage("EveryThing is working");
//		
//    	return CommonUtil.Response(new Header(), streamVO, new Meta());
//	
//    }

}
