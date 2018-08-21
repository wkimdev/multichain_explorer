package kr.doublechain.basic.explorer.contorller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.common.vo.DccResponse;
import kr.doublechain.basic.explorer.common.vo.Header;
import kr.doublechain.basic.explorer.common.vo.Meta;
import kr.doublechain.basic.explorer.service.couch.CouchbaseService;
import kr.doublechain.basic.explorer.service.dcc.DccService;

/**
 * Dcc API for Explorer
 * Block Controller
 *
 */
@RestController
@Api(tags = "Explorer")
@RequestMapping("${api.contextRoot}")
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
	@ApiOperation(value = "block info", notes = "node로 부터 블록 정보를 가져온다.")
	@GetMapping(value="/getinfo")
    public DccResponse<Object> getBlockByHash() throws Exception {
		return CommonUtil.Response(new Header(), CommonUtil.convertObjectFromGson(dccService.getInfo()), new Meta());
    }
    
    /**
	 * 2. Search API - 검색어(block number or txId)
	 *  
	 * @param 
	 * @return Object
	 * @throws Exception
	 */
	@ApiOperation(value = "전역 검색", notes = "블록 넘버 혹은 speeding Id 검색어로 블록 상세 정보를 가져온다.")
    @GetMapping(value="/search/{searches}")
    @ResponseBody
    public DccResponse<Object> search(@PathVariable("searches")String search) throws Exception {
		
		/**
		 * 1. uri를 이상하게 요청했을 경우
		 * 2. 요청한 데이터가 없을 경우. (검색실패시 얼랏 메세지 > No matching records found.) 
		 */
		// stream vo와 blocks vo구분?
		//couchbaseService.selectBlockBySearch(search);
//		if(VO.getKey() == 1) {
			// streamBucket
			//error
//		}
		
    	return CommonUtil.Response(new Header(), CommonUtil.convertObjectFromGson(couchbaseService.selectBlockBySearch(search)), new Meta());
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
    public Object getLatestBlock() throws Exception{
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
    public Object getSpeedIdLists() throws Exception{
    	return CommonUtil.convertObjectFromJSONArray(couchbaseService.selectStreamByTxId());
    }
    
    /**
	 * 8. txid에 대한 컨펌숫자 
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
	@ApiOperation(value = "블록 컨펌 수", notes = "speeding id에 대한 블록 컨펌 수.")
    @GetMapping("/confirmations/{txid}")
    @ResponseBody
    public Object getConfirmCnt(@PathVariable("txid")String txid) throws Exception{
    	return CommonUtil.convertObjectFromGson(couchbaseService.selectConfirmCntByTxId(txid));
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
