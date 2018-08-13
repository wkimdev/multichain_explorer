package kr.doublechain.basic.explorer.service.dcc;

import java.util.List;


import java.math.BigInteger;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.code.RPCCommandCode;
import kr.doublechain.basic.explorer.node.Dcc;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dcc Block Service
 *
 */
@Service("dccService")
public class DccService {
	
	/**
     * node
     */
	@Autowired
	Dcc dcc;

	/**
	 * node를 통한 RPC Call
	 */
	public Object RPCCall(String rpcCommand, List<Object> param) throws Exception {
		return dcc.RPCCall(rpcCommand, param);
	}

	/**
	 * block info 정보 가져오기
	 * @return 
	 */
	public JsonObject getInfo() throws Exception {
		List<Object> list = new ArrayList<>();
		return (JsonObject) RPCCall(RPCCommandCode.DCC_GETINFO.CODE, list);
	}
	
	/**
	 * 블록 해쉬로 블록 정보 가져오기
	 */
	public JsonObject getBlock(String blockHash) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockHash);
		list.add(1);
		return (JsonObject)RPCCall(RPCCommandCode.DCC_GETBLOCK.CODE, list);
	}
	
	/**
	 * 블록 넘버로 블록 정보 가져오기
	 */
	public JsonObject getBlock(BigInteger blockNumber) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockNumber);
		list.add(1);
		return (JsonObject)RPCCall(RPCCommandCode.DCC_GETBLOCK.CODE, list);
	}
	
	/**
	 * 블록 해쉬로 블록 정보 가져오기
	 */
	public JsonObject getBlockWithTx(String blockHash) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockHash);
		list.add(4);
		return (JsonObject)RPCCall(RPCCommandCode.DCC_GETBLOCK.CODE, list);
	}
	
	/**
	 * 블록 넘버로 블록 정보 가져오기
	 */
	public JsonObject getBlockWithTx(BigInteger blockNumber) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockNumber);
		list.add(4);
		return (JsonObject)RPCCall(RPCCommandCode.DCC_GETBLOCK.CODE, list);
	}

	/**
	 * 트랜젝션 해쉬로 트랜젝션 정보 가져오기
	 */
	public JsonObject getTx(String txHash) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(txHash);
		list.add(1);
		return (JsonObject)RPCCall(RPCCommandCode.DCC_GETTRANSACTION.CODE, list);
	}
	
	/**
	 * 블록 넘버로 블록 해쉬 가져오기
	 */
	public String getBlockHash(BigInteger blockNumber) throws Exception {
		return getBlock(blockNumber).get("hash").toString();
	}
	
	/**
	 * 블록 넘버로 다음 블록 해쉬 가져오기
	 */
	public String getNextBlockHash(BigInteger blockNumber) throws Exception {
		JsonObject block = getBlock(blockNumber);
		if (block.has("nextblockhash")) {
			return block.get("nextblockhash").toString();
		} else {
			return null;
		}
	}
	  
	/**   
	 * 트랜잭션 해쉬로 Data 가져오기
	 */
	public String getTxdata(String txHash, int n) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(txHash);
		list.add(n);
		String data = RPCCall(RPCCommandCode.DCC_GETTXDATA.CODE, list).toString();
		return data.substring(1, data.length() - 1);
	}
	
}
