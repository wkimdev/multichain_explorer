package kr.doublechain.basic.demo.service;

import kr.doublechain.basic.demo.common.RawTransaction;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

/**
 * Created by 전성국 on 2018-05-24.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        String rawTx = "01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff0c023a140101062f503253482fffffffff020088526a740000001976a91426b2fc376df764fccd191e38bc6979e676991ddd88ac0000000000000000716a4c6e53504b62463044022036130fd16516a27dbc731916330d7ddb723fccd2989ee44f2387513373163172022073dc9b12536c319e9daf1ebf9703f2cec240bcbdbb24a811a1d5596d12016b48022102190dcead346b51a7e0732126228cc9f4379f823a9d81dbb67d25d28e016bdb5100000000";
        RawTransaction tx = RawTransaction.parse(rawTx);

        for(int i = 0; i < tx.getOutputs().size(); i++) {
            System.out.println(tx.getOutputs().get(i).getAmount());
        }

//        System.out.println( tx.getOutputs().size() );
//        ExplorerService explorerService = new ExplorerServiceImpl();
//        explorerService.ExplorerStart();
    }

}
