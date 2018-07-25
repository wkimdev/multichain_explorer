package kr.doublechain.basic.explorer.service;

import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.code.RPCCommandCode;
import kr.doublechain.basic.explorer.common.CommonUtil;


@Service("dccService")
public class DccService {

//	public static String host;
//
//	public static String port;
//
//	public static String user;
//
//	public static String password;
//
//	@Value("${dcc.host}")
//	public void setHost(String host) {
//		DccService.host = host;
//	}
//
//	@Value("${dcc.port}")
//	public void setPort(String port) {
//		DccService.port = port;
//	}
//
//	@Value("${dcc.user}")
//	public void setUser(String user) {
//		DccService.user = user;
//	}
//
//	@Value("${dcc.password}")
//	public void setPassword(String password) {
//		DccService.password = password;
//	}

	@Autowired
	private HttpClient client;
	
	@Autowired
	private HttpPost request;
	
	
	/**
	 * RPC Call
	 */
	public Object RPCCall(String rpcCommand, List<Object> param) throws Exception {
		//return RemoteCall(user, password, host, port, rpcCommand, param);
		return RemoteCall(rpcCommand, param);
	}

	/**
	 * info 정보 가져오기
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
	
	//public Object RemoteCall(String user, String password, String host, String port, String rpcCommand,
	//		List<Object> param) throws Exception {

	public Object RemoteCall(String rpcCommand,
			List<Object> param) throws Exception {
		JSONObject json = new JSONObject();
		json.put("method", rpcCommand);
		json.put("params", new JSONArray(param));
		request.setEntity(new StringEntity(json.toString()));

		HttpResponse response = client.execute(request);

		String contents = EntityUtils.toString(response.getEntity());
		JsonObject jsonObject = CommonUtil.convertGsonFromString(contents);

		if (jsonObject.get("error").isJsonNull()) {
			return jsonObject.get("result");
		} else {
			return null;
		}

	}

}
