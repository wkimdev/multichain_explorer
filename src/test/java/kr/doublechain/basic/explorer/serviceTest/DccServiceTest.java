package kr.doublechain.basic.explorer.serviceTest;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

import kr.doublechain.basic.explorer.common.CommonUtil;
import kr.doublechain.basic.explorer.service.DccService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DccServiceTest {
	
	@Autowired
	DccService dccService;
	
	@Test
	public void getInfoCall() throws Exception {
		for (int i = 0; i < 10000; i++) {
			dccService.getInfo();
			System.out.println(i);
		}
		System.out.println(new Gson().toJson(dccService.getInfo()));
	}

	//@Test
	public void getBlockByHashTest() throws Exception {
		System.out.println(CommonUtil.convertJsonStringFromGson(dccService.getBlock("00d31ab5d89657ffe7a0eedf13c8a138eff15603f469882280cb44e3632a9177")));
	}
	
//	@Test
	public void getBlockByNumTest() throws Exception {
		System.out.println(CommonUtil.convertJsonStringFromGson(dccService.getBlock(new BigInteger("0"))));
	}
	
//	@Test
	public void getTxTest() throws Exception {
		System.out.println(CommonUtil.convertJsonStringFromGson(dccService.getTx("29d97b40654161dc4d5c598bda14cb68134490858f3d00ec4c4a015cb755f08d")));
	}
	
//	@Test
	public void getBlockHashTest() throws Exception {
		System.out.println(dccService.getBlockHash(new BigInteger("0")));
	}
	
//	@Test
	public void getNextBlockHashTest() throws Exception {
		System.out.println(dccService.getNextBlockHash(new BigInteger("0")));
	}

}
