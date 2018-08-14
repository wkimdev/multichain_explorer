package kr.doublechain.basic.explorer.service;

import static com.couchbase.client.java.query.Select.select;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.google.gson.reflect.TypeToken;

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
    	// limit, offset ?
    	// height에 따라 정렬해서 블록정보를 가져온다.
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
     * searching by blockhash
     * select by Blocks Bucket
	 */
    public JsonObject selectBlockByBlockhash(String blockhash) throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
    	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT * FROM `" + blockBucketName + "` WHERE hash = \"" + blockhash + "\""));
    	Iterator<N1qlQueryRow> result = query.iterator();
        JsonObject jsonObject = null;
        while(result.hasNext()) {
            N1qlQueryRow nqr = result.next();
            jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(blockBucketName).toString());
        }
    	return jsonObject;
    }
    
    /**
     * searching by txId
     * select by Streams Bucket
	 * 리턴값이 null인 경우도 pass 하도록 처리해야함.
	 */
    public JsonObject selectBlockByTxId(String txId) throws Exception {
    	Bucket bucket = connectBucket(txBucketName);
    	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT * FROM `" + streamBucketName + "` WHERE txid = \"" + txId +"\""));
    	Iterator<N1qlQueryRow> result = query.iterator();
        JsonObject jsonObject = null;
        while(result.hasNext()) {
            N1qlQueryRow nqr = result.next();	//?
            jsonObject = CommonUtil.convertGsonFromString(nqr.value().get(streamBucketName).toString());
        }
    	return jsonObject;
    }
    
    /**
     * select block info by height
     * limit 7
	 */
    public JsonObject selectBlockByheight() throws Exception {
    	Bucket bucket = connectBucket(blockBucketName);
    	N1qlQueryResult query = bucket.query(N1qlQuery.simple("SELECT * FROM `" + blockBucketName + "` order by height desc limit 7 "));
    	Iterator<N1qlQueryRow> result = query.iterator();
    	JSONArray jsonList = new JSONArray();
        while(result.hasNext()) {
            N1qlQueryRow nqr = result.next();
            jsonList.add(CommonUtil.convertGsonFromString(nqr.value().get(blockBucketName).toString()));
        }
        System.out.println(jsonList);
        JsonObject newObject = new JsonObject();
        newObject.addProperty("Blocks", jsonList.toJSONString());
    	return newObject;
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
