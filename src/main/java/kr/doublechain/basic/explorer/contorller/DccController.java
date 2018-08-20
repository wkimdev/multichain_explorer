package kr.doublechain.basic.explorer.contorller;

import java.math.BigInteger;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.common.CommonUtil;
import kr.doublechain.basic.explorer.service.CouchbaseService;
import kr.doublechain.basic.explorer.service.dcc.DccService;

/**
 * Dcc API
 * Block Controller
 *
 */
@RestController
@RequestMapping(value = "api")
public class DccController {
	
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
	 * 0. Dcc 노드 블록 정보 호출
	 * @param 
	 * @return String
	 * @throws Exception
	 */
    @RequestMapping("/getinfo")
    public String getBlockByHash() throws Exception {
    	return CommonUtil.convertJsonStringFromGson(dccService.getInfo());
    }
    
    /**
	 * 2. Search API - 검색어(block number or txId)
	 *  
	 * @param 
	 * @return Object
	 * @throws Exception
	 */
    @PostMapping(value="/search")
    @ResponseBody
    public Object searching(@RequestParam(value = "search", required = true)String search) throws Exception {
    	return CommonUtil.convertObjectFromGson(couchbaseService.selectBlockBySearch(search));
    }
    
    /**
	 * 4-1. 최근 마지막 생성 블록과, 포함된 speeding count. 
	 * 
	 * @return Object 
	 * @throws Exception
	 */
    @GetMapping(value ="/blocks/latest")
    @ResponseBody
    public Object getLatestBlock() throws Exception{
    	return CommonUtil.convertObjectFromGson(couchbaseService.selectLatestBlockCntSpeeding());
    }
    
    /**
	 * 4-2. 현재날짜기준. 생성 블록에 포함된 speeding count.
	 * 
	 * @return Object 
	 * @throws Exception
	 */
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
     @RequestMapping("/graph")
     @ResponseBody
     public Object getWeekSpeeding() {
     	return null;
     }
    
    /**
	 * 6. 최근 생성 블록 요약(7)
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
    @RequestMapping("/latestBlocks/lists")
    @ResponseBody
    public Object getLatestBlockList() throws Exception{
    	// TODO 유닉스 타임 형변환.
    	return CommonUtil.convertObjectFromJSONArray(couchbaseService.selectBlockByheight());
    }
 
    
    /**
	 * 7. 최근 생성 스피딩 정보 요약(실시간 리프레시)
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
    @RequestMapping("/speedIdLists")
    @ResponseBody
    public Object getSpeedIdLists() throws Exception{
    	return CommonUtil.convertObjectFromJSONArray(couchbaseService.selectStreamByTxId());
    }
    
//
//	@RequestMapping("/explorer")
//	int startExplorer() throws Exception {
//		String[] argv = new String[5];
//		argv[0] = "--config";
//		argv[1] = "sk_token.conf";
//		argv[2] = "--commit-bytes";
//		argv[3] = "100000";
//		argv[4] = "--no-serve";
//
//		return explorerService.explorerStart(argv);
//	}
//
//	@RequestMapping("/btc")
//	Map btc() throws Exception {
//		return btctest.btcTest("getinfo", new ArrayList());
//	}
    
//	@RequestMapping(value = "/btc/{apiName}", method = RequestMethod.GET)
//	Map rpc(@PathVariable("apiName") String apiName) throws Exception {
//		return btctest.btcTest(apiName, new ArrayList());
//	}
    
//	@RequestMapping(value = "/btc/{apiName}/{param_1}", method = RequestMethod.GET)
//	Map rpcwithParam_1(@PathVariable("apiName") String apiName, @PathVariable("param_1") String param_1) throws Exception {
//		ArrayList params = new ArrayList();
//		params.add(param_1);
//		return btctest.btcTest(apiName, params);
//	}
    
//	@RequestMapping(value = "/btc/{apiName}/{param_1}/{param_2}", method = RequestMethod.GET)
//	Map rpcwithParam_2(@PathVariable("apiName") String apiName, @PathVariable("param_1") String param_1, @PathVariable("param_2") int param_2) throws Exception {
//		ArrayList params = new ArrayList();
//		params.add(param_1);
//		params.add(param_2);
//		return btctest.btcTest(apiName, params);
//	}
  
	/*
	@RequestMapping(value = "/readUser/{userName}", method = RequestMethod.GET)
	void readUser(@PathVariable("userName") String userName, HttpServletResponse response) {
		qrcodeService.readUser(userName, response);
	}
	 */

	/*
	@RequestMapping("/sample")
	SampleEntity getMapData() {
		return sampleService.getEntity();
	}

	@RequestMapping("/")
	String home() {
		return "Spring boot sample. Welcome!";
	}

	@RequestMapping("/samples")
	List<SampleEntity> getMapList() {
		return sampleService.getEntities();
	}
	*/


}
