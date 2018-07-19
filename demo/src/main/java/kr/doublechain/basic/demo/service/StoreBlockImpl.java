package kr.doublechain.basic.demo.service;

import jdk.nashorn.internal.ir.Block;
import kr.doublechain.basic.demo.common.RawTransaction;
import kr.doublechain.basic.demo.model.BlockVo;
import kr.doublechain.basic.demo.model.TransactionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 전성국 on 2018-05-23.
 */
@Service("storeBlock")
@EnableAsync
public class StoreBlockImpl implements StoreBlock {
    @Autowired
    CouchbaseService couchbaseService;

    @Autowired
    MultichainRpc multichainRpc;

    @Override
    public BlockVo ImportBlock(BlockVo prevBlock, Map rpcBlock) throws Exception {
        BlockVo blockVo = new BlockVo();
        Long ssCreated;

        blockVo.setValueIn( (long)0 );
        blockVo.setValueOut( (long)0 );
        blockVo.setValueDestroyed( (long)0 );
        ArrayList<String> txHashArray = new ArrayList<>();

        ArrayList<Map> txs = fixValueTx((ArrayList)rpcBlock.get("tx"));
        blockVo.setValueIn( findVin(txs) );
        blockVo.setValueOut( findVout(txs) );
        blockVo.setBlockNumTx( (long)txs.size() );
        ArrayList<String> tx = new ArrayList<>();
        for (int i = 0; i <  txs.size(); i++) {
            tx.add(i, txs.get(i).get("txid").toString());
            TransactionVo transactionVo = new TransactionVo();
            transactionVo.setTxHash(txs.get(i).get("txid").toString());
            transactionVo.setTx(txs);
            couchbaseService.upsertBucketTransaction(transactionVo);
            for(int j = 0; j < ( (ArrayList)txs.get(i).get("vin") ).size(); j++) {
//                ( (Map)( (ArrayList)txs.get(i).get("vin") ).get(j) )
            }
        }
        blockVo.setTx(tx);

        blockVo.setHash( rpcBlock.get("hash").toString() );
        blockVo.setVersion( Long.parseLong( ( rpcBlock.get("version").toString() ) ) );
        blockVo.setBlockId( Long.parseLong( ( rpcBlock.get("height").toString() ) ) + 1 );
        blockVo.setHashMerkleRoot( rpcBlock.get("merkleroot").toString() );
        blockVo.setHeight( Long.parseLong( ( rpcBlock.get("height").toString() ) ) );
        blockVo.setChainWork( rpcBlock.get("chainwork").toString() );
        blockVo.setNTime( Long.parseLong( ( rpcBlock.get("time").toString() ) ) );
        blockVo.setNBits( rpcBlock.get("bits").toString() );
        blockVo.setNNonce( Long.parseLong( ( rpcBlock.get("nonce").toString() ) ) );

        if ( rpcBlock.containsKey("previousblockhash") ) {
            blockVo.setHashPrev(rpcBlock.get("previousblockhash").toString());
        }
        if ( prevBlock != null ) {
            blockVo.setPrevBlockId(prevBlock.getBlockId());
        }

        if (prevBlock == null ) {
            blockVo.setSeconds( (long)0 );
        }
        else {
            blockVo.setSeconds( prevBlock.getSeconds() + Long.parseLong( rpcBlock.get("time").toString() ) - prevBlock.getNTime() );
        }

        if ( prevBlock == null ) {
            blockVo.setValueIn( (long)0 );
            blockVo.setSatoshis( blockVo.getValueOut() - blockVo.getValueIn() );
        }
        else {
            if ( prevBlock.getSatoshis() == null || prevBlock.getSatoshis() < 0 || blockVo.getValueIn() == null ) {
                blockVo.setSatoshis( -1 - blockVo.getValueDestroyed() );
                // b['satoshis'] = -1 - b['value_destroyed']
            }
            else {
                blockVo.setSatoshis( prevBlock.getSatoshis() + blockVo.getValueOut() - blockVo.getValueIn() );
            }
        }

        if ( prevBlock == null ) {
            ssCreated = (long)0;
            blockVo.setTotalSs( BigInteger.valueOf(0) );
        }
        else if ( prevBlock.getSatoshis() < 0 ) {
            ssCreated = (long)0;
            blockVo.setTotalSs( BigInteger.valueOf(0) );
        }
        else {
            ssCreated = prevBlock.getSatoshis() * ( blockVo.getNTime() - prevBlock.getNTime() );
            blockVo.setTotalSs( prevBlock.getTotalSs().add(BigInteger.valueOf(ssCreated)));
        }

//        if (prevBlock == null) {
//
//        }
//        else {
//            if (prevBlock.getSatoshis() == 0 || prevBlock.getSatoshis() < 0 ) {
//
//            }
//            else {
//                ssCreated = prevBlock.getSatoshis() * (blockVo.getNTime() - prevBlock.getNTime() );
//                blockVo.setTotalSs( prevBlock.getTotalSs() + ssCreated );
//            }
//        }
//
//        if (blockVo.getHeight() == null || blockVo.getHeight() < 2 ) {
//            blockVo.setSearchBlockId(null);
//        }
//        else {
////            모르겠다
//        }

            /*
             if prev_satoshis is None or prev_satoshis < 0 or b['value_in'] is None:
            # XXX Abuse this field to save work in adopt_orphans.
            b['satoshis'] = -1 - b['value_destroyed']
            # print("if prev_satoshis is None or prev_satoshis < 0 or b['value_in'] is None:")
        else:
            b['satoshis'] = prev_satoshis + b['value_out'] - b['value_in'] \
                            - b['value_destroyed']
            # print("b['satoshis'] ", b['satoshis'])
             */

        /*

            tx['tx_id'] = store.tx_find_id_and_value(tx, pos == 0)

            if tx['tx_id']:
                all_txins_linked = False
            else:
                if store.commit_bytes == 0:
                    tx['tx_id'] = store.import_and_commit_tx(tx, pos == 0, chain)
                else:
                    tx['tx_id'] = store.import_tx(tx, pos == 0, chain)
                if tx.get('unlinked_count', 1) > 0:
                    all_txins_linked = False

            if tx['value_in'] is None:
                b['value_in'] = None
            elif b['value_in'] is not None:
                b['value_in'] += tx['value_in']
            b['value_out'] += tx['value_out']
            b['value_destroyed'] += tx['value_destroyed']
         */

        /*
        ArrayList<TransactionVo> transaction = new ArrayList<>();
        for(int i = 0; i < txs.size(); i++) {
            TransactionVo transactionVo = new TransactionVo();
            Set<String> txKeys = ((HashMap) txs.get(i)).keySet();
            for( Iterator j = txKeys.iterator(); j.hasNext(); ) {
         */





        return blockVo;
    }

    @Override
    public Long findVin(ArrayList tx) throws Exception {
        long value = 0;
        for(int i = 0; i < tx.size(); i++) {
//            System.out.println( (Map)tx.get(i) );
            //( ( (Map)(tx.get(i) ).get("vin"))
            for(int j = 0; j < ( (ArrayList)( (Map)tx.get(i) ).get("vin") ).size(); j++) {
                if ( ( ( (Map)( (ArrayList)( (Map)tx.get(i) ).get("vin") ).get(j) ).containsKey("txid") ) ) {
                    MultichainRpc multichainRpc = new MultichainRpcImpl();
                    ArrayList arrayList = new ArrayList();
                    arrayList.add( ( ( (Map)( (ArrayList)( (Map)tx.get(i) ).get("vin") ).get(j) ).get("txid") ).toString() );

                    RawTransaction rawTx = RawTransaction.parse((String)multichainRpc.MultichainRpc("getrawtransaction", arrayList));

                    for(int k = 0; k < rawTx.getOutputs().size(); k++) {
//                        System.out.println(rawTx.getOutputs().get(k).getAmount());
                        if (k == 0) {
                        }
//                            System.out.println(( ( (Map)( (ArrayList)( (Map)tx.get(i) ).get("vin") ).get(j) ).get("txid") ).toString() + " : " + rawTx.getOutputs().get(k).getAmount());
                        value += rawTx.getOutputs().get(k).getAmount();
                    }


//                    for(int k = 0; k < ( (ArrayList)vin.get("vout") ).size(); k++) {
//                        value += (long)( Double.parseDouble((((Map) ((ArrayList) vin.get("vout")).get(k)).get("value")).toString()) * 100000000 );
////                        System.out.println(Double.parseDouble((((Map) ((ArrayList) vin.get("vout")).get(k)).get("value")).toString()));
//
//                    }
                }
            }
        }
        return value;
    }

    @Override
    public Long findVout(ArrayList tx) {
        long value = 0;

        for(int i = 0; i < tx.size(); i++) {
            for(int j = 0; j <  ( (ArrayList)( (Map)tx.get(i) ).get("vout") ).size(); j++) {
                if( ( (Map)( ( (ArrayList)( (Map)tx.get(i) ).get("vout") ).get(j) ) ).containsKey("value") ) {
                    if (j == 0) {

//                        System.out.println((long)( Double.parseDouble((((Map) (((ArrayList) ((Map) tx.get(i)).get("vout")).get(j))).get("value")).toString())));
                    }
                    value += (long)( Double.parseDouble((((Map) (((ArrayList) ((Map) tx.get(i)).get("vout")).get(j))).get("value")).toString()));
//                    System.out.println((((Map) (((ArrayList) ((Map) tx.get(i)).get("vout")).get(j))).get("value")));
//                    value += Long.parseLong( ( ( (Map)( ( (ArrayList)( (Map)tx.get(i) ).get("vout") ).get(j) ) ).get("value") ).toString() );
                }
            }
//            if ( ( ( (Map)tx.get(i) ).containsValue("value") ) ) {
//                value += Long.parseLong( ( ( (Map)tx.get(i) ).get("value") ).toString() );
//            }
        }

        return value;
    }

    @Override
    public ArrayList<Map> fixValueTx(ArrayList<Map> txs) {
        for (int i = 0; i < txs.size(); i++) {
            String hex = ( txs.get(i).get("hex") ).toString();
            RawTransaction tx = RawTransaction.parse(hex);
            ArrayList<Map> vout = (ArrayList)txs.get(i).get("vout");

            for (int j = 0; j < vout.size(); j++ ) {
//                .out.println(tx.getOutputs().get(j).getAmount());
                ( (Map)((ArrayList) txs.get(i).get("vout")).get(j) ).put("value", tx.getOutputs().get(j).getAmount());
            }
        }
        return txs;
    }

    @Override
    public BlockVo findLastBlock() {
        BlockVo blockVo = new BlockVo();


        return blockVo;
    }

    @Override
    @Async
    public void storeBlock() throws Exception {
        BlockVo prevBlock= couchbaseService.selectLastBlock();
//        System.out.println(prevBlock);
        long nextHeight;
        String nextHash;

        if (prevBlock.getHeight() == null) {
            nextHeight = 0;
            nextHash = multichainRpc.getHash(0);
            prevBlock = null;
        } else {
            nextHeight = prevBlock.getHeight() + 1;
            nextHash = multichainRpc.getNextHash(nextHeight - 1);
        }

        String rpcHash = nextHash;
        if( rpcHash == null ) {
            nextHeight--;
            nextHash = multichainRpc.getNextHash(nextHeight - 1);
            rpcHash = nextHash;
        }
        BlockVo blockVo;
        while( rpcHash != null ) {
            String hash = rpcHash;
            Map rpcBlock;
            rpcBlock = multichainRpc.getBlock(rpcHash);
            blockVo = ImportBlock(prevBlock, rpcBlock);

            prevBlock = blockVo;
//            System.out.println(blockVo);
            couchbaseService.upsertBucketTransaction(blockVo);
            if(rpcBlock.containsKey("nextblockhash")) {
                rpcHash = ( rpcBlock.get("nextblockhash") ).toString();
//                System.out.println(nextHeight);
                nextHeight++;
            } else {
//                System.out.println("Sleep");
                Thread.sleep(5000);
            }
        }
//        System.out.println("end");

    }

}
