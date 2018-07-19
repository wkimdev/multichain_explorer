package kr.doublechain.basic.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 전성국 on 2018-05-24.
 */
@Data
@Getter @Setter
public class TransactionVo {
//    INSERT INTO tx
//    private Long txId;          // 1부터 시작하는 txid
//    private String txHash;
//    private Long version;
//    private Long lockTime;
//    private Long txSize;        // 보류


//    INSERT INTO txout
//    private Long txoutId;        // 1부터 시작하는 txout Id
//    private Long txId;
    private String txHash;
    private ArrayList<Map> tx;


    //import_tx


}
