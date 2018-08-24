package kr.doublechain.basic.explorer.service.couch;

import static com.couchbase.client.java.query.Select.select;



import java.math.BigInteger;
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
     	long currentTime = System.currentTimeMillis()/1000;
     	LOG.info("currentTime target :"+currentTime);
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT count(txid) as speedCnt FROM `" + streamBucketName + "` WHERE streamKeys = \"\\\"speeding\\\"\" AND MILLIS_TO_STR(time, '1111-11-11') =  MILLIS_TO_STR(" + currentTime + ", '1111-11-11')"));
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
 	 * 최근 생성 스피딩 정보 요약(실시간 리프레시)
 	 * 
 	 * @return JSONArray
 	 * @throws Exception
 	 */
     public JSONArray selectStreamBySpeed() throws Exception {
     	Bucket bucket = connectBucket(streamBucketName);
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT height, txid, data.json.overspeed as overspeed FROM `" + streamBucketName + "` where streamKeys = \"\\\"speeding\\\"\" order by height desc limit 10 "));
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
     	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT height, txid, data.json.who as who FROM `" + streamBucketName + "` where streamKeys = \"\\\"inout\\\"\" order by height desc limit 10 "));
     	Iterator<N1qlQueryRow> result = query.iterator();
     	JSONArray jsonList = new JSONArray();
     	while(result.hasNext()) {
     		jsonList.add(result.next());
     	}
     	return jsonList;
     }
     
      
//    익스플로러 기획서 수정전 검색 메서드.(2018.8.22)
//    /**
//     * @param search
//     * @return
//     * @throws Exception
//     */
//    public JsonObject selectBlockBySearch(String search) throws Exception {
//    	int key = 0; //1 = streamBucket, 2 = blockbucket
//    	
//    	JsonObject jsonObject = null;
//    	if (search.length() == 64) {
//    		key = 1;
//    		// it's a transaction id(=speeding id, txid, tx)
//    		Bucket streambucket = connectBucket(streamBucketName);
//    		N1qlQueryResult query = streambucket.query(N1qlQuery.simple("SELECT * FROM `" + streamBucketName + "` WHERE txid = \"" + search + "\""));
//    		Iterator<N1qlQueryRow> result = query.iterator();
//    		while(result.hasNext()) {
//              N1qlQueryRow nqr = result.next();
//              
//              // 최상의 valueObject로 던져주는 곳에 맵핑.
//              // StreamVO streamVO = streamVO();
//              jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(streamBucketName).toString());
//              //CommonUtil.checkValueObject(jsonObject, StreamVO.class);
//          }
//    	} else {
//			// it's a block number
//    		key = 2;
//    		Bucket blockbucket = connectBucket(blockBucketName);
//    		int searchNumber = Integer.parseInt(search);
//    		N1qlQueryResult query = blockbucket.query(N1qlQuery.simple("SELECT * FROM `" + blockBucketName + "` WHERE height = " + searchNumber + ""));
//    		Iterator<N1qlQueryRow> result = query.iterator();
//    		while(result.hasNext()) {
//              N1qlQueryRow nqr = result.next();
//              jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(blockBucketName).toString());
//          }
//		}
//    	jsonObject.addProperty("searchKey", key);
//    	return jsonObject;
//    }
    
    /**
     * 5. 2주간 발생된 일별 과속단속 카메라 촬영 건수.
     * TODO 일별 stream count를 시키기.
     * 
     * @return JsonObject
     * @throws Exception
     */
    public JsonObject selectTwoWeeksSpeedCnt() throws Exception {
    	Bucket bucket = connectBucket(streamBucketName);
    	Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		long pastTime = cal.getTimeInMillis()/1000;
		
		long currentTime = System.currentTimeMillis()/1000;
		Date date2 = new Date();
		date2.setTime((long)currentTime*1000);
		
		/*
		 * 
		 * 2주 간격은 동적으로 바뀌고, 거기에 따른 일별 데이터도 동적으로 바뀐다. 현재 동일 날짜만 존재함.
		 * 
		 * select count(txid) as txCnt from Streams where time between 1534412341 and 1534412538  
			  union all  
			select count(txid) as txCnt from Streams where time between 1534412337 and 1534412520 
			  union all  
			select count(txid) as txCnt from Streams where time between 1534412320 and 1534412510;
		 * */
		N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT count(*) as cnt from `" + streamBucketName + "` where time between 1533548084 and 1534757684 and name = \"IoT\" and streamKeys = \"\\\"jsonrpc\\\"\";"));
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
