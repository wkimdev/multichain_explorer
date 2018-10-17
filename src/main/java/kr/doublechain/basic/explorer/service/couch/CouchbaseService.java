package kr.doublechain.basic.explorer.service.couch;

import static com.couchbase.client.java.query.Select.select;


import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

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
    
	public Bucket connectBucket(String bucketName) {
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
    	  N1qlQueryResult query = streambucket.query(N1qlQuery.simple("SELECT * FROM `" + streamBucketName + "` WHERE META(`"+ streamBucketName + "`).id = \"" + search + "\""));
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
	 	 N1qlQueryResult query = streambucket.query(N1qlQuery.simple("SELECT * FROM `" + streamBucketName + "` WHERE META(`"+ streamBucketName + "`).id = \""+ search +"\""));
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
     	
     	// kor time
     	//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
     	//Date date = new Date();	// current date
		
     	// bc time
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Date date = new Date();
 		
		String sql = " SELECT count(*) as speedCnt FROM `" + streamBucketName + 
				 	 "` WHERE streamKeys = \"\\\"speeding\\\"\" " +
					 "  AND data.json.date like \"" + df.format(date) +" %\" ";
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple(sql));
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
     	
     	// kor time
     	// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
     	// Date date = new Date();	// current date
		
		// bc time
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Date date = new Date();
		
		String sql = " SELECT count(*) as fingerPrintCnt FROM `" + streamBucketName + 
					 "` WHERE streamKeys = \"\\\"inout\\\"\" " + 
					 "  AND data.json.date like \"" + df.format(date) +" %\" ";
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple(sql));
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
     	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();	// current date
		
     	String sql = "SELECT txid, data.json.vihiclespeed as vihiclespeed, data.json.location as location, data.json.date as date FROM `" + streamBucketName +
				 "` where streamKeys = \"\\\"speeding\\\"\" " +
				 "\n  AND -MILLIS(data.json.date) < 0 " +
     			 "\n  order by -MILLIS(data.json.date) limit 10 ";
     	
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple(sql));
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
     	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();	// current date
		
		String sql = "SELECT data.json.date as date, data.json.person as person, data.json.state as state, txid FROM `" + streamBucketName + "` where streamKeys = \"\\\"inout\\\"\" " +
					 "\n  AND -MILLIS(data.json.date) < 0 " +
	    			 "\n  order by -MILLIS(data.json.date) limit 10 ";
		
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple(sql));
     	Iterator<N1qlQueryRow> result = query.iterator();
     	JSONArray jsonList = new JSONArray();
     	while(result.hasNext()) {
     		jsonList.add(result.next());
     	}
     	return jsonList;
     }
     
    /**
     * 2주간 발생된 일별 과속단속 카메라 촬영 건수. - old
     * 일별 stream count를 시키기.
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
    	String sql = " select count(*) as speedCnt, SUBSTR(DATE_FORMAT_STR(data.json.date, '1111-11-11'), 5, 2) || '/' || SUBSTR(DATE_FORMAT_STR(data.json.date, '1111-11-11'), 8, 2) as date  " + 
					 "\n from `" + streamBucketName + "` " +											
					 "\n where streamKeys = \"\\\"speeding\\\"\" " + 
					 "\n and data.json.date BETWEEN \"" + dateFormat.format(cal.getTime()) + "\" and \"" + dateFormat.format(date) + "\" " +
					 "\n group by DATE_FORMAT_STR(data.json.date, '1111-11-11') " + 											
					 "\n order by DATE_FORMAT_STR(data.json.date, '1111-11-11') DESC ";
		N1qlQueryResult query = bucket.query(N1qlQuery.simple(sql));
		//LOG.debug(sql);
    	Iterator<N1qlQueryRow> result = query.iterator();
    	JSONArray jsonList = new JSONArray();
    	while(result.hasNext()) {
    		jsonList.add(result.next());
    	}
    	return jsonList;
    }
    
    /**
     * 현재 날짜 시간 발생된 일별 과속단속 카메라 촬영 건수. - 2018/10/10
     * 
     * @return JsonObject
     * @throws Exception
     */
    public JSONArray selectTodaySpeedCnt() throws Exception {
    	JsonObject jsonObject = null;
    	Bucket bucket = connectBucket(streamBucketName);
    	
    	//kor time
    	// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	// Date date = new Date();	// current date
		
		// bc time
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    df.setTimeZone(TimeZone.getTimeZone("GMT+2"));
	    Date date = new Date();
		
    	String sql = " select count(*) as speedCnt, SUBSTR(data.json.date, 11, 2) as date  " + 
					 "\n from `" + streamBucketName + "` " +											
					 "\n where streamKeys = \"\\\"speeding\\\"\" " + 
					 "\n AND data.json.date like \"" + df.format(date) +" %\" " +
					 "\n group by SUBSTR(data.json.date, 11, 2) " + 											
					 "\n order by SUBSTR(data.json.date, 11, 2) ASC ";
		N1qlQueryResult query = bucket.query(N1qlQuery.simple(sql));
		//LOG.debug(sql);
    	Iterator<N1qlQueryRow> result = query.iterator();
    	JSONArray jsonList = new JSONArray();
    	while(result.hasNext()) {
    		jsonList.add(result.next());
    	}
    	return jsonList;
    }
    
    /**
     * 현재 날짜 시간 발생된 일별 과속단속 카메라 촬영 건수. - 2018/10/10
     * 
     * @return JsonObject
     * @throws Exception
     */
    public JSONArray selectTodayDoorAccessCnt() throws Exception {
    	JsonObject jsonObject = null;
    	Bucket bucket = connectBucket(streamBucketName);
    	
    	// kor time
    	// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	// Date date = new Date();	// current date
		
		// bc time
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    df.setTimeZone(TimeZone.getTimeZone("GMT+2"));
	    Date date = new Date();
		
    	String sql = " select count(*) as fingerPrintCnt, SUBSTR(data.json.date, 11, 2) as date  " + 
					 "\n from `" + streamBucketName + "` " +											
					 "\n where streamKeys = \"\\\"inout\\\"\" " + 
					 "\n AND data.json.date like \"" + df.format(date) +" %\" " +
					 "\n group by SUBSTR(data.json.date, 11, 2) " + 											
					 "\n order by SUBSTR(data.json.date, 11, 2) ASC ";
		N1qlQueryResult query = bucket.query(N1qlQuery.simple(sql));
//		LOG.debug(sql);
    	Iterator<N1qlQueryRow> result = query.iterator();
    	JSONArray jsonList = new JSONArray();
    	while(result.hasNext()) {
    		jsonList.add(result.next());
    	}
    	return jsonList;
    }
    
    /**
     * 2주간 발생된 일별 출입인증 시도 건수. - old
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
    	
		N1qlQueryResult query = bucket.query(N1qlQuery.simple(" select count(*) as fingerPrintCnt, SUBSTR(DATE_FORMAT_STR(data.json.date, '1111-11-11'), 5, 2) || '/' || SUBSTR(DATE_FORMAT_STR(data.json.date, '1111-11-11'),8,2) as date  " + 
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
        bucket.upsert(RawJsonDocument.create(jsonObject.get("txid").getAsString(), jsonObject.toString()));
    }
    
    public void upsertBucketStream(JsonObject jsonObject) throws Exception {
    	Bucket bucket = connectBucket(streamBucketName);
        bucket.upsert(RawJsonDocument.create(jsonObject.get("txid").getAsString(), jsonObject.toString()));
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
