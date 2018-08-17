package kr.doublechain.basic.explorer.serviceTest;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.doublechain.basic.explorer.common.CommonUtil;
import kr.doublechain.basic.explorer.service.CouchbaseService;
import kr.doublechain.basic.explorer.service.dcc.DccService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CouchbaseServiceTest {
	
	@Autowired
	CouchbaseService couchbaseService;
	
	@Autowired
	DccService dccService;
	
	//@Test
	public void upsertBucketBlockTest() throws Exception {
		for (int i = 0; i < 1; i++) {
			couchbaseService.upsertBucketBlock(dccService.getBlock(new BigInteger(i + "")));
		}
	}
	
	//@Test
	public void upsertBucketTransactionTest() throws Exception {
		couchbaseService.upsertBucketTransaction(dccService.getTx("29d97b40654161dc4d5c598bda14cb68134490858f3d00ec4c4a015cb755f08d"));
	}
	
	//@Test
	public void selectLastBlockTest() throws Exception {
		System.out.println(CommonUtil.convertJsonStringFromGson(couchbaseService.selectLastBlock()));
	}
	
	//@Test
	public void selectBlockByTxIdTest() throws Exception {
		System.out.println(CommonUtil.convertJsonStringFromGson(couchbaseService.selectBlockByTxId("\"10e0281aa1e254aacfc38ccfea9e9f8529c0235f071147e3e61bd8ba1a796dd6\"")));
	}
	
	//json
	@Test
	public void selectBlockByheightTest() throws Exception {
		System.out.println(CommonUtil.convertObjectFromJSONArray(couchbaseService.selectBlockByheight()));
	}
	
	//@Test
	public void selectBlockTest() throws Exception {
		System.out.println(CommonUtil.convertJsonStringFromGson(couchbaseService.selectBlock(new BigInteger("0"))));
	}

//	@Test
	public void deleteBlockTest() throws Exception {
		couchbaseService.deleteBlock(new BigInteger("10"));
	}
	
	//@Test
	public void deleteTxTest() throws Exception {
		couchbaseService.deleteTx("29d97b40654161dc4d5c598bda14cb68134490858f3d00ec4c4a015cb755f08d");
	}
	
}
