package kr.doublechain.basic.explorer.service;

import java.math.BigInteger;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import kr.doublechain.basic.explorer.common.CommonUtil;

@Service("updateBlockService")
public class UpdateBlockService {

	@Autowired
	private CouchbaseService couchbaseService;

	@Autowired
	private DccService dccService;

	/**
	 * DB에 저장된 가장 최신의 유효한 블록 번호를 리턴한다.
	 * 
	 * @param void
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public BigInteger checkBlock() throws Exception {

		JsonObject currentBlock = couchbaseService.selectLastBlock();

		while (true) {
			if (currentBlock == null) {
				removeBlocks(new BigInteger("0"));
				return new BigInteger("0");
			} else {
				BigInteger currentHeight = new BigInteger(currentBlock.get("height").toString());
				String currentHash = dccService.getBlockHash(currentHeight);
				if (currentHash.equals(currentBlock.get("hash").toString())) {
					removeBlocks(currentHeight);
					return currentHeight;
				} else {
					currentHeight = currentHeight.subtract(BigInteger.valueOf(1));
					currentBlock = couchbaseService.selectBlock(currentHeight);
				}
			}
		}
	}
	
	public BigInteger checkBlock(JsonObject currentBlock) throws Exception {

		while (true) {
			if (currentBlock == null) {
				removeBlocks(new BigInteger("1"));
				return new BigInteger("0");
			} else {
				BigInteger currentHeight = new BigInteger(currentBlock.get("height").toString());
				String currentHash = dccService.getBlockHash(currentHeight);
				if (currentHash.equals(currentBlock.get("hash").toString())) {
					removeBlocks(currentHeight.add(new BigInteger("1")));
					return currentHeight;
				} else {
					currentHeight = currentHeight.subtract(BigInteger.valueOf(1));
					currentBlock = couchbaseService.selectBlock(currentHeight);
				}
			}
		}
	}

	/**
	 * DB에 Block 정보를 merge한다.
	 * 
	 * @param JsonObject
	 * @return void
	 * @throws Exception
	 */
	public void mergeBlock(JsonObject block) throws Exception {
		couchbaseService.upsertBucketBlock(block);
	}
	
	/**
	 * DB에 Transaction 정보를 merge한다.
	 * 
	 * @param JsonObject
	 * @return void
	 * @throws Exception
	 */
	public void mergeTx(BigInteger height) throws Exception {

//		if ((block.get("height").toString()).equals("0")) {
//			JsonObject genesisBlock = dccService.getBlockWithTx(new BigInteger("0"));
//			JsonObject genesisTransaction = CommonUtil.convertGsonFromString(
//					genesisBlock.get("tx").toString().substring(1, genesisBlock.get("tx").toString().length() - 1));
//			
//			genesisTransaction.add("height", block.get("height"));
//			genesisTransaction.add("blockhash", block.get("hash"));
//			genesisTransaction.add("confirmations", block.get("confirmations"));
//			genesisTransaction.add("time", block.get("time"));
//			genesisTransaction.add("blocktime", block.get("time"));
//			
//			couchbaseService.upsertBucketTransaction(genesisTransaction);
//		} else {
//			List<String> transactionList = CommonUtil.getTxList(block);
//			for (int i = 0; i < transactionList.size(); i++) {
//				JsonObject tx = dccService.getTx(transactionList.get(i));
//				tx.add("height", block.get("height"));
//				couchbaseService.upsertBucketTransaction(tx);
//				mergeStream(tx);
//			}
//		}
		JsonObject block = dccService.getBlockWithTx(height);
		JsonArray transactions = block.getAsJsonArray("tx");
		
		for (int i = 0; i < transactions.size(); i++) {
			
			JsonObject transaction = transactions.get(i).getAsJsonObject();
			
			transaction.add("height", block.get("height"));
			transaction.add("blockhash", block.get("hash"));
			transaction.add("confirmations", block.get("confirmations"));
			transaction.add("time", block.get("time"));
			transaction.add("blocktime", block.get("time"));
			
			couchbaseService.upsertBucketTransaction(transaction);
			mergeStream(transaction);
		}
	}
	
	public void mergeStream(JsonObject transaction) throws Exception {
		
		if (transaction.has("vout")) {
			
			JsonArray txList = transaction.getAsJsonArray("vout");
			for (int i = 0 ; i < txList.size(); i++) {
				
				if ( txList.get(i).getAsJsonObject().has("items") ) {
					
					JsonArray items = txList.get(i).getAsJsonObject().getAsJsonArray("items");
					for(int j = 0; j < items.size(); j++) {
						
						JsonObject item = items.get(j).getAsJsonObject();
						if ("\"stream\"".equals(item.get("type").toString())) {
							
							String publishers = new String();
							JsonArray publisherArray = item.getAsJsonArray("publishers");
							for(int k = 0; k < publisherArray.size(); k++) {
								publishers += publisherArray.get(k);
							}
							
							String keys = new String();
							JsonArray keyArray = item.getAsJsonArray("keys");
							for(int k = 0; k < keyArray.size(); k++) {
								keys += keyArray.get(k);
							}
							item.add("height", transaction.get("height"));
							item.add("txid", transaction.get("txid"));
							item.add("time", transaction.get("time"));
							item.addProperty("publishers", publishers);
							item.addProperty("streamKeys", keys);
							item.remove("keys");
							couchbaseService.upsertBucketStream(item);
						}
					}
				}
			}
		}
	}
	
	/**
	 * DB에 저장된 height 이상의 Block과 tx를 삭제한다.
	 * 
	 * @param BigInteger
	 * @return void
	 * @throws Exception
	 */
	public void removeBlocks(BigInteger height) throws Exception {
		BigInteger currentHeight = new BigInteger(height.toString());
		while (true) {
			JsonObject currentBlock = couchbaseService.selectBlock(currentHeight);
			if (currentBlock == null) {
				return;
			}
			
			List<String> transactionList = CommonUtil.getTxList(currentBlock);
			for (int i = 0; i < transactionList.size(); i++) {
				couchbaseService.deleteTx(transactionList.get(i));
				couchbaseService.deleteStrean(transactionList.get(i));
			}
			couchbaseService.deleteBlock(currentHeight);
			currentHeight = currentHeight.add(new BigInteger("1"));
		}
	}
	

	
	public JsonObject init() throws Exception {
		
		BigInteger currentHeight = checkBlock();
		JsonObject currentBlock = null;
		
		currentBlock = dccService.getBlock(currentHeight);			
				
		mergeBlock(currentBlock);
		mergeTx(currentHeight);
		
		while (true) {
			
			if (currentBlock.has("nextblockhash")) {
				currentHeight = currentHeight.add(new BigInteger("1"));
				currentBlock = dccService.getBlock(currentHeight);
				
				mergeBlock(currentBlock);
				mergeTx(currentHeight);
				System.out.println("Update Block : " + currentHeight);
				Thread.sleep(50);
			} else {
				break;
			}
		}
		return currentBlock;
	}
	
	public void start() throws Exception {
		BigInteger currentHeight = null;
		JsonObject currentBlock = init();

//		BigInteger currentHeight = checkBlock();
//		JsonObject currentBlock = null;
		while (true) {

			currentHeight = checkBlock(currentBlock);
			currentBlock = dccService.getBlock(currentHeight);

			if (currentBlock.has("nextblockhash")) {
				mergeBlock(currentBlock); // nextblockhash 업데이트

				currentHeight = currentHeight.add(new BigInteger("1"));
				currentBlock = dccService.getBlock(currentHeight);

				mergeBlock(currentBlock);
				mergeTx(currentHeight);
				System.out.println("Update Block : " + currentHeight);
				Thread.sleep(50);
			} else {
				Thread.sleep(1000);
			}
		}

	}
}
