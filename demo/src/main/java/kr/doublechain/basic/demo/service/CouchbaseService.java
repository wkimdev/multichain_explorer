package kr.doublechain.basic.demo.service;

import static com.couchbase.client.java.query.Select.select;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.query.dsl.Sort;
import jdk.nashorn.internal.ir.Block;
import kr.doublechain.basic.demo.common.CommonUtils;
import kr.doublechain.basic.demo.model.BlockVo;
import kr.doublechain.basic.demo.model.TransactionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;


import java.util.ArrayList;

/**
 * Created by 전성국 on 2018-05-23.
 */
@Service("couchbaseService")
public class CouchbaseService {
    @Autowired
    private Bucket bucket;

    @Value("${couchbase.bucket.name}")
    private String bucketName;

    private final int perPAGE = 10;

    public void upsertBucketTransaction(BlockVo blockVo) throws Exception {
        JsonObject jsonObject = JsonObject.fromJson(CommonUtils.convertJsonStringFromObject(blockVo));
        bucket.upsert(JsonDocument.create(blockVo.getHeight().toString(), jsonObject));
    }

    public void upsertBucketTransaction(TransactionVo transactionVo) throws Exception {
        JsonObject jsonObject = JsonObject.fromJson(CommonUtils.convertJsonStringFromObject(transactionVo));
        bucket.upsert(JsonDocument.create(transactionVo.getTxHash(), jsonObject));
    }

//    public List<BlockVo> findAll(int page) throws Exception {
//
//        if(page == 0) {
//            page = 1;
//        }
//
//        List<BlockVo> resultList = new ArrayList<>();
//
//        int offset = perPAGE * (page - 1);
//
//        N1qlQueryResult query = bucket.query(select("*").from(bucketName).where("blockHash IS NOT NULL").limit(perPAGE).offset(offset));
//        Iterator<N1qlQueryRow> result = query.iterator();
//        while(result.hasNext()) {
//            N1qlQueryRow nqr = result.next();
//            resultList.add(EvaUtils.convertObjectFromJsonString(nqr.value().get(bucketName).toString(), BlockVo.class));
//        }
//
//        return resultList;
//    }


    public BlockVo selectLastBlock() throws Exception {
        BlockVo blockVo = new BlockVo();
        int offset = 0;
        N1qlQueryResult query = bucket.query(select("*").from(bucketName).where("height").orderBy(Sort.desc("height")).limit(1).offset(offset));
        Iterator<N1qlQueryRow> result = query.iterator();
        while(result.hasNext()) {
            N1qlQueryRow nqr = result.next();
            blockVo = convertObjectFromJsonString(nqr.value().get(bucketName).toString(), BlockVo.class);
        }
        return blockVo;
    }
    public static <T> T convertObjectFromJsonString(String content, Class<T> clazz) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        T object = (T) mapper.readValue(content, clazz);

        return object;
    }


}
