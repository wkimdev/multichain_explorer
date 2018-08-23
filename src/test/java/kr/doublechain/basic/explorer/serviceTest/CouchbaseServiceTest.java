package kr.doublechain.basic.explorer.serviceTest;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.service.couch.CouchbaseService;
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
	
	//@Test
	public void selectCurrentTimeSeedingCnt() throws Exception {
		System.out.println(couchbaseService.selectBlockSpeedingCntByDate()); 
	}
	
	//@Test
	public void selectLatestBlockCntSpeeding() throws Exception {
		System.out.println(couchbaseService.selectLatestBlockCntSpeeding()); 
	}
	
	//@Test
	public void currentTime() throws Exception{
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		long pastTime = cal.getTimeInMillis()/1000;
		
		long currentTime = System.currentTimeMillis()/1000;
		Date date2 = new Date();
		date2.setTime((long)currentTime*1000);
		
		System.out.println("14일전 유닉스 : "+pastTime+" 타임 : "+cal.getTime());
		System.out.println("현재시간 유닉스 :"+currentTime+" 타임 : "+date2);
	}
	
	//@Test
	public void currentTime2() throws Exception{
		long currentTime = System.currentTimeMillis()/1000;
		Date date2 = new Date();
		date2.setTime((long)currentTime*1000);
		
	}
	
	@Test //search
	public void selectSearch() throws Exception {
		//52f69ef0ba0c75cc9fc3cb4da6204a78065029aa43c597af4b8a2f6696b687fa
		//008e581ce004414376ede53a695f6de52cff0479c7e84b9260995811eef67856
		//"1887"
		System.out.println(couchbaseService.selectFingerPrintBySearch("214cfb10b31f5d16d232c4415348f3f767ef2deb91980dcd17b23337f76c161f"));
	}
	
}
