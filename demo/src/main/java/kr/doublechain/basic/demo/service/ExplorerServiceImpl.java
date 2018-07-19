package kr.doublechain.basic.demo.service;

import kr.doublechain.basic.demo.model.BlockVo;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 전성국 on 2018-05-23.
 */
@Service("explorerService")
public class ExplorerServiceImpl implements ExplorerService {

    @Autowired
    StoreBlock storeBlock;

    @Autowired
    MultichainRpc multichainRpc;

    @Autowired
    CouchbaseService couchbaseService;

    @Override
    public void ExplorerStart() throws Exception {

        BlockVo prevBlock= couchbaseService.selectLastBlock();
        long i;
        if (prevBlock != null){
            i = prevBlock.getHeight() + 1;
        }
        else {
            i = 0;
        }

        while(true) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(String.valueOf(i));
            arrayList.add(4);
            Map rpcBlock = (Map)multichainRpc.MultichainRpc("getblock", arrayList);
            BlockVo blockVo = storeBlock.ImportBlock(prevBlock, rpcBlock);
            System.out.println(blockVo.getHeight());
            // sout -> log
            // ArrayList 로 선언하지 말고 List
            // Thread로.
//            if(blockVo.getSatoshis() % multiple != 0) {
//                break;
//            }
            prevBlock = blockVo;
            couchbaseService.upsertBucketTransaction(blockVo);
            if(!rpcBlock.containsKey("nextblockhash")) {
                break;
            }
            i++;
        }
//        for(int i = 0; i < 200; i++) {
//            ArrayList arrayList = new ArrayList();
//            arrayList.add(String.valueOf(i));
//            arrayList.add(4);
//            Map rpcBlock = (Map)multichainRpc.MultichainRpc("getblock", arrayList);
//            BlockVo blockVo = storeBlock.ImportBlock(prevBlock, rpcBlock);
//            System.out.println(blockVo);
//            prevBlock = blockVo;
//            if(!rpcBlock.containsKey("nextblockhash")) {
//                break;
//            }
//        }
//                    ArrayList arrayList = new ArrayList();
//            arrayList.add("e880c6dbaa7b504d63f2bbc2a33065f25a71db50e4ce310c9884d16e0ea42df4");
//        System.out.println(multichainRpc.MultichainRpc("getrawtransaction", arrayList));
//        couchbaseService.upsertBucketTransaction(prevBlock);




    }

    @Override
    public void test() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
            Thread.sleep(10000);
        }
    }
}
