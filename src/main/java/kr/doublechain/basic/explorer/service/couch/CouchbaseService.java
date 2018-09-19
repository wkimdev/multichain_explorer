package kr.doublechain.basic.explorer.service.couch;

import static com.couchbase.client.java.query.Select.select;



import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.dsl.Sort;
import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.common.utils.CommonUtil;
import kr.doublechain.basic.explorer.contorller.DccController;

/**
 * CouchbaseService
 *
 */
@Service("couchbaseService")
public class CouchbaseService {
	
	private static final Logger LOG = LoggerFactory.getLogger(DccController.class);
	
	@Autowired
	private Cluster couchbaseCluster;

	@Value("${couchbase.bucket.blocks.name}")
    private String blockBucketName;
    
    @Value("${couchbase.bucket.txs.name}")
    private String txBucketName;

    @Value("${couchbase.bucket.streams.name}")
    private String streamBucketName;
    
    @Value("${couchbase.bucket.user}")
    private String userName;

    @Value("${couchbase.bucket.password}")
    private String bucketPassword;
	
	public Bucket connectBucket(String bucketName) {
		couchbaseCluster.authenticate(userName, bucketPassword);
		return couchbaseCluster.openBucket(bucketName);
	}
	
	/**
	 * 최신 블록값 호출
	 */
    public JsonObject selectLastBlock() throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
        N1qlQueryResult query = bucket.query(select("*").from(blockBucketName).where("height").orderBy(Sort.desc("height")).limit(1).offset(0));
        Iterator<N1qlQueryRow> result = query.iterator();
        JsonObject jsonObject = null;
        while(result.hasNext()) {
            N1qlQueryRow nqr = result.next();
            jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(blockBucketName).toString());
        }
    	return jsonObject;
    }
    
    /**
	 * Speeding Stream 검색
	 * 
	 * @param search
	 * @return JsonObject
	 * @throws Exception
	 */
     public JsonObject selectSpeedBySearch(String search) throws Exception {
    	  JsonObject jsonObject = null;
    	  Bucket streambucket = connectBucket(streamBucketName);
    	  N1qlQueryResult query = streambucket.query(N1qlQuery.simple("SELECT * FROM `" + streamBucketName + "` WHERE streamKeys = \"\\\"speeding\\\"\" and txid = \"" + search + "\""));
    	  Iterator<N1qlQueryRow> result = query.iterator();    	  
    	      	  
    	  while(result.hasNext()) {
            N1qlQueryRow nqr = result.next();
            jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(streamBucketName).toString());
    	  }
    	  return jsonObject;
      }
     
     /**
 	 * fingerPrint Stream 검색
 	 * 
 	 * @param search
 	 * @return JsonObject
 	 * @throws Exception
 	 */
     public JsonObject selectFingerPrintBySearch(String search) throws Exception {
     	 JsonObject jsonObject = null;
	 	 Bucket streambucket = connectBucket(streamBucketName);
	 	 N1qlQueryResult query = streambucket.query(N1qlQuery.simple("SELECT * FROM `" + streamBucketName + "` WHERE streamKeys = \"\\\"inout\\\"\" and txid = \""+ search +"\""));
	 	 Iterator<N1qlQueryRow> result = query.iterator();
	 	  
	 	 while(result.hasNext()) {
	        N1qlQueryRow nqr = result.next();
	        jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(streamBucketName).toString());
	 	 }
	 	 return jsonObject;
      }
     
     /**
      * 현재 날짜의 Speeding 총 카운트
      * 
      * @return JsonObject
      * @throws Exception
      */
     public JsonObject selectSpeedCntByCurrent() throws Exception {
     	Bucket bucket = connectBucket(streamBucketName);
     	// time기준으로 count할때 사용함.
     	// long currentTime = System.currentTimeMillis()/1000;
     	// LOG.info("currentTime target :"+currentTime);
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT count(txid) as speedCnt FROM `" + streamBucketName + "` WHERE streamKeys = \"\\\"speeding\\\"\" AND DATE_FORMAT_STR(data.json.date, '1111-11-11') = CLOCK_STR('1111-11-11')"));
     	Iterator<N1qlQueryRow> result = query.iterator();
     	JsonObject jsonObject = new JsonObject();
     	try {
     		N1qlQueryRow nqr = result.next();
         	jsonObject = CommonUtil.convertGsonFromString(nqr.value().toString());
     	} catch(Exception e) {
     		e.printStackTrace();
     	}
     	return jsonObject;
     }
     
     /**
      * 현재 날짜의 FingerPrint 총 카운트
      * 
      * @return JsonObject
      * @throws Exception
      */
     public JsonObject selectFingerPrintCntByCurrent() throws Exception {
     	Bucket bucket = connectBucket(streamBucketName);     	
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT count(txid) as fingerPrintCnt FROM `" + streamBucketName + "` WHERE streamKeys = \"\\\"inout\\\"\" AND DATE_FORMAT_STR(data.json.date, '1111-11-11') = CLOCK_STR('1111-11-11')"));
     	Iterator<N1qlQueryRow> result = query.iterator();
     	JsonObject jsonObject = new JsonObject();
     	try {
     		N1qlQueryRow nqr = result.next();
         	jsonObject = CommonUtil.convertGsonFromString(nqr.value().toString());
     	} catch(Exception e) {
     		e.printStackTrace();
     	}
     	return jsonObject;
     }
     
     /**
 	 * 최근 생성 스피딩 정보 요약(실시간 리프레시)
 	 * 
 	 * @return JSONArray
 	 * @throws Exception
 	 */
     public JSONArray selectStreamBySpeed() throws Exception {
     	Bucket bucket = connectBucket(streamBucketName);
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT height, txid, data.json.overspeed as overspeed, data.json.location as location, data.json.date as date FROM `" + streamBucketName + "` where streamKeys = \"\\\"speeding\\\"\" order by data.json.date desc limit 10 "));
     	Iterator<N1qlQueryRow> result = query.iterator();
     	JSONArray jsonList = new JSONArray();
     	while(result.hasNext()) {
     		jsonList.add(result.next());
     	}
     	return jsonList;
     }
     
     /**
 	 * 최근 생성 지문 정보 요약
 	 * 
 	 * @return JSONArray
 	 * @throws Exception
 	 */
     public JSONArray selectStreamByFingerPrint() throws Exception {
     	Bucket bucket = connectBucket(streamBucketName);
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT data.json.date as date, data.json.person as person, data.json.open as status, txid, height FROM `" + streamBucketName + "` where streamKeys = \"\\\"inout\\\"\" order by data.json.date desc limit 10 "));
     	Iterator<N1qlQueryRow> result = query.iterator();
     	JSONArray jsonList = new JSONArray();
     	while(result.hasNext()) {
     		jsonList.add(result.next());
     	}
     	return jsonList;
     }
     
     /**
      * 2 weeks date 조회
      * 
      * @return JsonObject
      * @throws Exception
      */
     public JsonObject selectTwoWeeksSpeeding() throws Exception {
    	 
     	Bucket bucket = connectBucket(streamBucketName);
     	long currentTime = System.currentTimeMillis()/1000;
     	LOG.info("currentTime target :"+currentTime);
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT count(txid) as fingerPrintCnt FROM `" + streamBucketName + "` WHERE streamKeys = \"\\\"inout\\\"\" AND MILLIS_TO_STR(time, '1111-11-11') =  MILLIS_TO_STR(" + currentTime + ", '1111-11-11')"));
     	Iterator<N1qlQueryRow> result = query.iterator();
     	JsonObject jsonObject = new JsonObject();
     	try {
     		N1qlQueryRow nqr = result.next();
         	jsonObject = CommonUtil.convertGsonFromString(nqr.value().toString());
     	} catch(Exception e) {
     		e.printStackTrace();
     	}
     	return jsonObject;
     }
     
    /**
     * 2주간 발생된 일별 과속단속 카메라 촬영 건수.
     * TODO 일별 stream count를 시키기.
     * 
     * @return JsonObject
     * @throws Exception
     */
    public JSONArray selectTwoWeeksSpeedCnt() throws Exception {
    	JsonObject jsonObject = null;
    	Bucket bucket = connectBucket(streamBucketName);    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
		Date date = new Date();	// current date
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -13); //before 2 weeks date
    	
		N1qlQueryResult query = bucket.query(N1qlQuery.simple(" select count(txid) as speedCnt, SUBSTR(DATE_FORMAT_STR(data.json.date, '1111-11-11'), 5, 2) || '/' || SUBSTR(DATE_FORMAT_STR(data.json.date, '1111-11-11'), 8, 2) as date  " + 
											" from `" + streamBucketName + "` " +											
											" where streamKeys = \"\\\"speeding\\\"\" " + 
											" and data.json.date BETWEEN \"" + dateFormat.format(cal.getTime()) + "\" and \"" + dateFormat.format(date) + "\" " +
											" group by DATE_FORMAT_STR(data.json.date, '1111-11-11') " + 											
											" order by DATE_FORMAT_STR(data.json.date, '1111-11-11') DESC "));
		
    	Iterator<N1qlQueryRow> result = query.iterator();
    	JSONArray jsonList = new JSONArray();
    	while(result.hasNext()) {
    		jsonList.add(result.next());
    	}
    	return jsonList;
    }
    
    /**
     * 2주간 발생된 일별 출입인증 시도 건수. 
     * 
     * @return JsonObject
     * @throws Exception
     */
    public JSONArray selectTwoWeeksFingerPrints() throws Exception {
    	JsonObject jsonObject = null;
    	Bucket bucket = connectBucket(streamBucketName);    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
		Date date = new Date();	// current date
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -13); //before 2 weeks date
    	
		N1qlQueryResult query = bucket.query(N1qlQuery.simple(" select count(txid) as fingerPrintCnt, SUBSTR(DATE_FORMAT_STR(data.json.date, '1111-11-11'), 5, 2) || '/' || SUBSTR(DATE_FORMAT_STR(data.json.date, '1111-11-11'),8,2) as date  " + 
											" from `" + streamBucketName + "` " +											
											" where streamKeys = \"\\\"inout\\\"\" " + 
											" and data.json.date BETWEEN \"" + dateFormat.format(cal.getTime()) + "\" and \"" + dateFormat.format(date) + "\" " +
											" group by DATE_FORMAT_STR(data.json.date, '1111-11-11') " + 											
											" order by DATE_FORMAT_STR(data.json.date, '1111-11-11') DESC "));
		
    	Iterator<N1qlQueryRow> result = query.iterator();
    	JSONArray jsonList = new JSONArray();
    	while(result.hasNext()) {
    		jsonList.add(result.next());
    	}
    	return jsonList;
    }
    
    /**
	 * 6. 최근 생성 블록 요약(7)
	 * select block info by height limit 7
	 * TODO date format 변경가능
	 * 
	 * @return JSONArray
	 * @throws Exception
	 */
    public JSONArray selectBlockByheight() throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
    	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT height, time, array_count(tx) as speeding FROM `" + blockBucketName + "` order by height desc limit 7"));
    	Iterator<N1qlQueryRow> result = query.iterator();
    	JSONArray jsonList = new JSONArray();
    	while(result.hasNext()) {
    		jsonList.add(result.next());
    	}
    	return jsonList;
    }
    
    public JsonObject selectBlock(BigInteger blockNumber) throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
    	N1qlQueryResult query = bucket.query(select("*").from(blockBucketName).where("height=" + blockNumber.toString()).orderBy(Sort.desc("height")).limit(1).offset(0));
        Iterator<N1qlQueryRow> result = query.iterator();
        JsonObject jsonObject = null;
        while(result.hasNext()) {
            N1qlQueryRow nqr = result.next();
            //jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(blockBucketName).toString());
            //jsonObject.add("Blocks", CommonUtil.convertGsonFromString(nqr.value().get(blockBucketName).toString()));
        }
    	return null;
    }
    
    /**
	 * block 정보 업데이트 - 쓰레드가 돌면서 호출을 한다.
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
    public void upsertBucketBlock(JsonObject jsonObject) throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
        bucket.upsert(RawJsonDocument.create(jsonObject.get("height").toString(), jsonObject.toString()));
    }
    
    public void upsertBucketTransaction(JsonObject jsonObject) throws Exception {
    	Bucket bucket = connectBucket(txBucketName);
        bucket.upsert(RawJsonDocument.create(jsonObject.get("txid").toString(), jsonObject.toString()));
    }
    
    public void upsertBucketStream(JsonObject jsonObject) throws Exception {
    	Bucket bucket = connectBucket(streamBucketName);
        bucket.upsert(RawJsonDocument.create(jsonObject.get("txid").toString(), jsonObject.toString()));
    }
    
    public void deleteBlock(BigInteger blockNumber) throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
    	bucket.query(N1qlQuery.simple("DELETE FROM `" + blockBucketName + "` WHERE height = " + blockNumber.toString()));
    }
    
    public void deleteTx(String txid) throws Exception {
    	Bucket bucket = connectBucket(txBucketName);
    	bucket.query(N1qlQuery.simple("DELETE FROM `" + txBucketName + "` WHERE txid = \"" + txid + "\""));
    }
    
    public void deleteStrean(String txid) throws Exception {
    	Bucket bucket = connectBucket(streamBucketName);
    	bucket.query(N1qlQuery.simple("DELETE FROM `" + streamBucketName + "` WHERE txid = \"" + txid + "\""));
    }

}
