package kr.doublechain.basic.explorer.serviceTest;

import java.math.BigInteger;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.couchbase.client.java.Bucket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	
	@Value("${spring.jackson.time-zone}")
	TimeZone timeZone;
	
//	@Bean
//    public LocaleResolver localeResolver() {
//      SessionLocaleResolver slr = new SessionLocaleResolver();
//      slr.setDefaultLocale(locale);
//      return slr;
//    }
	
	//@Test
	public void upsertBucketBlockTest() throws Exception {
		for (int i = 0; i < 10000; i++) {
			couchbaseService.upsertBucketBlock(dccService.getBlock(new BigInteger(i + "")));
			System.out.println("i :"+i);
		}
	}
	
	//@Test
	public void upsertBucketTransactionTest() throws Exception {
		//couchbaseService.upsertBucketTransaction(dccService.getTx("29d97b40654161dc4d5c598bda14cb68134490858f3d00ec4c4a015cb755f08d"));
		couchbaseService.upsertBucketTransaction(dccService.getTx("00007d3ee17c6b64de0dfefe903ab325905ee2b062a442f0d6cbd187f80256c2"));		
	}	
	
	//@Test 
	public void selectBlockTest() throws Exception {
		//System.out.println(CommonUtil.convertJsonStringFromGson(couchbaseService.selectBlock(new BigInteger("0"))));
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
		//System.out.println(couchbaseService.selectBlockSpeedingCntByDate()); 
	}
	
	//@Test
	public void selectLatestBlockCntSpeeding() throws Exception {
		//System.out.println(couchbaseService.selectLatestBlockCntSpeeding()); 
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
	
	//@Test
	public void selectSearch() throws Exception {
		System.out.println(couchbaseService.selectFingerPrintBySearch("ee42daf1807bb830bdd440bfc4dea744a5b36ea36eb84c07844d512dbd856ee5"));
	}
		
	
	//@Test
	public void selectSpeedingCnt() throws Exception {
		System.out.println(CommonUtil.convertObjectFromJSONArray(couchbaseService.selectTwoWeeksSpeedCnt()));
		
	}
	
	//@Test
	public void selectFingerPrintCnt() throws Exception {
    	
    	//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
//    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();	// current date
		
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		cal.add(Calendar.DATE, -13); //before 2 weeks date
		
		//System.out.println(dateFormat.format(cal.getTime()));
//		System.out.println(dateFormat.format(date)); //
		
		System.out.println(CommonUtil.convertObjectFromJSONArray(couchbaseService.selectTwoWeeksFingerPrints()));
	}
	
	//@Test
	public void selectLastBlockTest() throws Exception {
		JsonObject getLastBlock = couchbaseService.selectLastBlock();		
		JsonObject test = new JsonObject();
		test.add("row", getLastBlock.getAsJsonObject());
		System.out.println(test.getAsJsonObject("row").get("height"));		
	}
	
	//@Test
	public void selectSpeedBySearch() throws Exception {
		JsonObject jsonObject = couchbaseService.selectSpeedBySearch("3a7eab2e532d4419e07e097d1dc2fcaeeffab223b7aaaf9d40eba93eed438fce");
		JsonObject test = new JsonObject();
		test.add("row", jsonObject.getAsJsonObject());
		System.out.println(test.getAsJsonObject("row").get("height"));

	} 
	
	//@Test
	public void bcTimeTest() throws Exception {
//		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES"));
//		String bctime = date.format(new Date());
//		System.out.println("bctime :"+bctime);
		
		//SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		//System.out.println("simpledateformat : " + date.toString());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Date date = new Date();
        //dateFormat.format(date)
        System.out.println(df.format(date));
	}
	
	//@Test
	public void selectTodaySpeedCnt() throws Exception {
		//System.out.println(couchbaseService.selectTwoWeeksSpeedCnt()); // old
		//System.out.println(couchbaseService.selectTodaySpeedCnt()); //speed
		System.out.println(couchbaseService.selectTodayDoorAccessCnt());
	}
	
	//@Test
	public void selectCurrentSpeedingCnt() throws Exception {
		System.out.println(CommonUtil.convertObjectFromGson(couchbaseService.selectSpeedCntByCurrent()));
	}
	
	//@Test
	public void selectStreamBySpeed() throws Exception {
		//System.out.println(couchbaseService.selectStreamBySpeed());
		System.out.println(couchbaseService.selectStreamByFingerPrint());
	}
	
	//@Test
	public void selectCurrentDoorAccessCnt() throws Exception {
		System.out.println(CommonUtil.convertObjectFromGson(couchbaseService.selectFingerPrintCntByCurrent()));
	}
	
	//@Test
	public void selectLocaleValue() throws Exception {
		//System.out.println(locale.toString());
		//System.out.println(timeZone.getID()); //Europe/Madrid --> Db에서 처리
		String ID = timeZone.getID();
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		df.setTimeZone(TimeZone.getTimeZone(ID));
		//System.out.println("Date and time in Madrid: " + df.format(date));
		int offset = timeZone.getRawOffset();
		String text = String.format("%s%02d%02d", offset >= 0 ? "+" : "-", offset / 3600000, (offset / 60000) % 60);
		System.out.println("test >>>> "+text); //+0900
		
	}
}
