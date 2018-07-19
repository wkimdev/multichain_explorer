package kr.doublechain.basic.demo.service;

import kr.doublechain.basic.demo.model.BlockVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 전성국 on 2018-05-23.
 */
public interface StoreBlock {
    public BlockVo ImportBlock(BlockVo prevBlock, Map rpcBlock) throws Exception;

    public Long findVin(ArrayList tx) throws Exception;

    public Long findVout(ArrayList tx);

    public ArrayList<Map> fixValueTx(ArrayList<Map> txs);

    public BlockVo findLastBlock();

    public void storeBlock() throws Exception;
}
