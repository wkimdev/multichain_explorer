package kr.doublechain.basic.explorer.serviceTest;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import org.json.simple.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.schedule.UpdateBlockService;
import kr.doublechain.basic.explorer.service.dcc.DccService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateBlockServiceTest {

	@Autowired
	DccService dccService;

	@Autowired
	UpdateBlockService updateBlockService;

//	@Test
	public void checkBlockTest() throws Exception {
		System.out.println(updateBlockService.checkBlock().toString());
	}

//	@Test
	public void mergeBlockTest() throws Exception {
		updateBlockService.mergeBlock(dccService.getBlock(new BigInteger("0")));
	}

//	@Test
	public void mergeTxTest() throws Exception {
		updateBlockService
				.mergeTx(new BigInteger("0"));
	}

//	@Test
	public void Test() throws Exception {
		for (int i = 0; i < 10; i++) {
			JsonObject jo = dccService.getBlock(new BigInteger(i + ""));
			updateBlockService.mergeBlock(jo);
			updateBlockService.mergeTx(new BigInteger(i + ""));
			System.out.println(i);
		}
	}

//	@Test
	public void removeBlocksTest() throws Exception {
		updateBlockService.removeBlocks(new BigInteger("0"));
	}

//	@Test
	public void startTest() throws Exception {
		updateBlockService.start();
	}

//	@Test
	public void validBlockchainTest() throws Exception {
		System.out.println(updateBlockService.validBlockchain());
	}
	
}