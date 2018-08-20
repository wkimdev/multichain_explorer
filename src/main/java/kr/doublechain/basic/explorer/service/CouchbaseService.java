package kr.doublechain.basic.explorer.service;

import static com.couchbase.client.java.query.Select.select;


import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONArray;
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

import kr.doublechain.basic.explorer.common.CommonUtil;


/**
 * CouchbaseService
 *
 */
@Service("couchbaseService")
public class CouchbaseService {

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
	 * 2. 검색(block number or txId)
	 * 
	 * @param search
	 * @return JsonObject
	 * @throws Exception
	 */
    public JsonObject selectBlockBySearch(String search) throws Exception {
    	
    	JsonObject jsonObject = null;
    	if (search.length() == 64) {
    		// it's a transaction id(=speeding id, txid, tx)
    		Bucket streambucket = connectBucket(streamBucketName);
    		N1qlQueryResult query = streambucket.query(N1qlQuery.simple("SELECT * FROM `" + streamBucketName + "` WHERE txid = \"" + search + "\""));
    		Iterator<N1qlQueryRow> result = query.iterator();
    		while(result.hasNext()) {
              N1qlQueryRow nqr = result.next();
              jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(streamBucketName).toString());
          }
    	} else {
			// it's a block number
    		Bucket blockbucket = connectBucket(blockBucketName);
    		int searchNumber = Integer.parseInt(search);
    		N1qlQueryResult query = blockbucket.query(N1qlQuery.simple("SELECT * FROM `" + blockBucketName + "` WHERE height = " + searchNumber + ""));
    		Iterator<N1qlQueryRow> result = query.iterator();
    		while(result.hasNext()) {
              N1qlQueryRow nqr = result.next();
              jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(blockBucketName).toString());
          }
		}
    	return jsonObject;
    }
    
    /**
     * 4-1. 최근 마지막 생성 블록에 포함된 Speeding 개수
     * 
     * @return 
     * @throws Exception
     */
    public JsonObject selectLatestBlockCntSpeeding() throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
    	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT height, array_count(tx) AS txcnt FROM `" + blockBucketName + "` order by height desc limit 1"));
    	Iterator<N1qlQueryRow> result = query.iterator();
    	N1qlQueryRow nqr = result.next();
    	JsonObject jsonObject = new JsonObject();
        jsonObject = CommonUtil.convertGsonFromString(nqr.value().toString());
    	return jsonObject;
    }
    
    
    /**
     * 4-2. 동일 날짜에 생성된 블록의 Speeding 총 개수
     * 쿼리 값이 없을때의 Exception 처리
     * 
     * @return JsonObject
     * @throws Exception
     */
    public JsonObject selectBlockSpeedingCntByDate() throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
    	long currentTime = System.currentTimeMillis()/1000;
    	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT array_count(tx) AS tx From `" + blockBucketName + "` WHERE time = " + currentTime +" "));
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
     * 5. 2주간 발생된 일별 과속단속 카메라 촬영 건수.
     * 일별 stream count를 시키기.
     * 
     * @return JsonObject
     * @throws Exception
     */
    public JsonObject selectTwoWeeksSpeedCnt() throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
    	Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		long pastTime = cal.getTimeInMillis()/1000;
		
		long currentTime = System.currentTimeMillis()/1000;
		Date date2 = new Date();
		date2.setTime((long)currentTime*1000);
		
    	// SELECT * from Streams where time between 1534232726 and 1534232740 and name = "IoT" and streamKeys = "\"jsonrpc\"";
		//SELECT count(*) as cnt from Streams where time between 1533548084 and 1534757684 and name = "IoT" and streamKeys = "\"jsonrpc\"";
    	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT array_count(tx) AS tx From `" + blockBucketName + "` WHERE time = " + currentTime +" "));
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
	 * 
	 * @param 
     * @return 
	 * @return 
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
    
    /**
	 * 7. 최근 생성 스피딩 정보 요약(실시간 리프레시)
	 * select Stream Data info by txId limit 7
	 * TODO order by 명확하게 하
	 * 
	 * @param 
     * @return 
	 * @return 
	 * @throws Exception
	 */
    public JSONArray selectStreamByTxId() throws Exception {
    	Bucket bucket = connectBucket(txBucketName);
    	// SELECT time, txid, data.json.overspeed FROM Streams where time between 1534233460 and 1534233500;
    	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT * FROM `" + streamBucketName + "` order by txid desc limit 7 "));
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
