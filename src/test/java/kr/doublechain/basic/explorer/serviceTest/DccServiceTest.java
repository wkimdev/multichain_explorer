package kr.doublechain.basic.explorer.serviceTest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.service.dcc.DccService;

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
	public void getBlockCountTest() throws Exception {
		System.out.println(dccService.getBlockCount());
	}
	
	//@Test
	public void getBlockByHashTest() throws Exception {
		//System.out.println(CommonUtil.convertJsonStringFromGson(dccService.getBlock("00d31ab5d89657ffe7a0eedf13c8a138eff15603f469882280cb44e3632a9177")));
		System.out.println(CommonUtil.convertJsonStringFromGson(dccService.getBlock("05d0631af7f3c31bcddd41077d76ddfb80a15ddd6eb802b24c5f1c1dc472e2d6")));
	}
	
//	@Test
	public void getBlockByNumTest() throws Exception {
		System.out.println(CommonUtil.convertJsonStringFromGson(dccService.getBlock(new BigInteger("0"))));
	}
	
	//@Test
	public void getTxTest() throws Exception {
		//이미지 쪽 받아오는거 수정
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

//	@Test
	public void publishTest() throws Exception {
		List<Object> list = new ArrayList<>();
		list.add("IoT");
		list.add("javaTest");
		JsonObject String = new JsonObject();
		JsonObject json = new JsonObject();
		json.addProperty("name", "jsontest");
		String.add("json", json);
		list.add(CommonUtil.convertObjectFromGson(String));
		System.out.println(dccService.RPCCall("publish", list));
	}
	
	//@Test
	public void getTxdataTest() throws Exception {
		List<Object> list = new ArrayList<>();
		list.add("9dc460ad46e4e0859c7fe411d5e9c9c37b6df3e21d0dfcb82839cf9199cf054d");
		list.add(0);
		System.out.println(dccService.RPCCall("gettxoutdata", list));
	}
	
	//@Test
	public void getConfirmationCheck() throws Exception {
		System.out.println(dccService.confirmationCheck());
	}
	
}
