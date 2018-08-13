package kr.doublechain.basic.explorer.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import kr.doublechain.basic.explorer.service.dcc.DccService;

/**
 * Dcc Block Controller
 *
 */
@RestController
public class DccController {
	
	/**
     * Service
     */
	@Autowired
	DccService dccService;
	
	/**
	 * Dcc 노드 블록 정보 호출
	 * @param 
	 * @return 
	 * @throws Exception
	 */
    @RequestMapping("/api/getinfo")
    public JsonObject getBlockByHash() throws Exception {
    	return dccService.getInfo();
    }
    
    // 2. 검색 API
    // 4. lastblock 실시간 refresh
    // 5. week speeding grapgh
    // 7. 최근 생성 스피딩 정보 요약(실시간 리프레시)
    
//  @Autowired
//	ExplorerService explorerService;
//
//	@Autowired
//	btcTest btctest;
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
