package org.snubi.did.main.blockchain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.entity.ChainNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BlockChain {
	
	private int reConnectionCnt = 0;
	
	@SuppressWarnings("unchecked")
	public  String CreateAccount(MemberDto memberDto, ChainNode chainNode) {
		
		
		String strBlockChainServerIp = chainNode.getNodeIp();		
		String strBlockChainServerPort = chainNode.getNodePort();	
		String strBlockChainServerAccount = chainNode.getAdminChainAddress();
		String strBlockChainServerPrivateKey = chainNode.getAdminChainPassword();
		log.info("strBlockChainServerIp {}" ,  strBlockChainServerIp);
		log.info("strBlockChainServerPort {}" ,  strBlockChainServerPort);
		log.info("strBlockChainServerAccount {}" ,  strBlockChainServerAccount);
		log.info("strBlockChainServerPrivateKey {}" ,  strBlockChainServerPrivateKey);
		// 여기서 블록체인 연결 
		
		
		
		
//		// Ethereum 노드에 연결
//        Web3j web3j = Web3j.build(new HttpService(strBlockChainServerIp + ":" + strBlockChainServerPort)); // Ethereum 노드 URL로 변경
//
//        try {
//            // 최신 블록 번호 가져오기
//            EthBlock.Block latestBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf("latest"), false)
//                    .send()
//                    .getBlock();
//
//            // 현재 블록 번호
//            long currentBlockNumber = latestBlock.getNumber().longValue();
//
//            // 주기적으로 블록 번호를 가져와서 갱신 주기 확인
//            long previousBlockNumber = currentBlockNumber;
//            while (true) {
//                Thread.sleep(1000); // 5초마다 조회 (원하는 주기로 변경 가능)
//                latestBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf("latest"), false)
//                        .send()
//                        .getBlock();
//                currentBlockNumber = latestBlock.getNumber().longValue();
//                long blockUpdateInterval = currentBlockNumber - previousBlockNumber;
//                log.info("블록 갱신 주기: {}",  blockUpdateInterval+ " 블록");
//                previousBlockNumber = currentBlockNumber;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
		
		
		
		
		
    	String account = "";
    	List<String> list = new ArrayList<>();
 	    JSONObject jObj = new JSONObject();		
 		HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        /*********************** personal_newAccount *************************/
           	
        list.add(memberDto.getMemberPassword());         
        
 	    jObj.put("jsonrpc","2.0");
 	    jObj.put("method","personal_newAccount");
 	    jObj.put("params", list);
 	    jObj.put("id","1");
 	    
 	    log.info("jObj.toJSONString() {}",  jObj.toJSONString());
 	    
 	    
		try {
			reConnectionCnt++;
			log.info("블록체인 계좌생성 재시도 카운트 {}", reConnectionCnt);
			HttpEntity<String> request = new HttpEntity<String> (jObj.toJSONString(), headers);
	 	    String apiResult = restTemplate.postForObject(strBlockChainServerIp+":"+strBlockChainServerPort, request, String.class);
	 	    JSONParser parser = new JSONParser();
	 		Object obj = parser.parse( apiResult );
			JSONObject jObjApiResult = (JSONObject) obj;
	 		account = (String)(jObjApiResult.get("result"));
	 		log.info("account "+  account);
	 		/*********************** personal unlock *************************/
	 		if(list != null) list.clear();
	 		list.add(strBlockChainServerAccount);
	 		list.add(strBlockChainServerPrivateKey);
	 		jObj.put("jsonrpc","2.0");
	 	    jObj.put("method","personal_unlockAccount");
	 	    jObj.put("params", list);
	 	    jObj.put("id","2");
	 	    request = new HttpEntity<String> (jObj.toJSONString(), headers);
	 	    restTemplate.postForObject(strBlockChainServerIp+":"+strBlockChainServerPort, request, String.class);		
	 	    log.info("personal unlock");
	 	   /*********************** admin unlock *************************/
	 		if(list != null) list.clear();
	 		list.add(account);
	 		list.add(memberDto.getMemberPassword());
	 		jObj.put("jsonrpc","2.0");
	 	    jObj.put("method","personal_unlockAccount");
	 	    jObj.put("params", list);
	 	    jObj.put("id","2");
	 	    request = new HttpEntity<String> (jObj.toJSONString(), headers);
	 	    restTemplate.postForObject(strBlockChainServerIp+":"+strBlockChainServerPort, request, String.class);		
	 	    log.info("admin unlock");
	 		/*********************** send ether 16진수 *************************/
	 		JSONObject jObjSendEther = new JSONObject();
	 		jObjSendEther.put("from", strBlockChainServerAccount);
	 		jObjSendEther.put("to", account);
	 		jObjSendEther.put("value", "0x4563918244f40000"); 
	 		List<JSONObject> objList = new ArrayList<>();
	 		objList.add(jObjSendEther);
	 		jObj.put("jsonrpc","2.0");
	 	    jObj.put("method","eth_sendTransaction");
	 	    jObj.put("params", objList);
	 	    jObj.put("id","2");
	 	    request = new HttpEntity<String> (jObj.toJSONString(), headers);
	 	    // REST API 호출
	 	    String apiResultSendEther = restTemplate.postForObject(strBlockChainServerIp+":"+strBlockChainServerPort, request, String.class);
	 	    log.info("apiResultSendEther: " + apiResultSendEther);
	 	    
	 	    ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(apiResultSendEther);
		    String transactionHash = jsonNode.get("result").asText();		    
		    log.info("transactionHash: " + transactionHash);
	        try {
	        	int x = 0;
	        	 Web3j web3j = Web3j.build(new HttpService(strBlockChainServerIp + ":" + strBlockChainServerPort)); // Ethereum 노드 URL로 변경
	             // 트랜잭션 영수증 가져오기	        	 
	        	 while(true) {
	        		 Thread.sleep(1000);
	        		 Optional<TransactionReceipt> receipt = web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt();
		             if(receipt.isPresent()) {
		            	 log.info("트랜잭션 해시: " + receipt.get().getTransactionHash());
		            	 log.info("블록 번호: " + receipt.get().getBlockNumber());
		            	 log.info("영수증 상태: " + receipt.get().getStatus());		            	 
		            	 if (receipt.get().getStatus().equals("0x1")) {
		            		 log.info("트랜잭션 영수증 성공");
		                     return account;
		                 }else {		                	
			            	 x++;
			            	 if(x > 30) {
			            		    log.info("영수증 상태: x > 30");		 
				            		break;
				             }else {
				            	    log.info("영수증 상태: x < 30");
				            		continue;
				             }
		                 } 
		            	
		             }else {
		            	 if(x % 5 == 1) log.info("트랜잭션 영수증 가져오기 출력  {}" , x);
		            	 x++;
		             } 
		             web3j.shutdown();
	        	 }
	        } catch (Exception e) {
	        	log.info("IOException {}", e.getMessage());
	        	if(reConnectionCnt < 5)	CreateAccount(memberDto, chainNode);
	        }			
		} catch (ParseException e) {
			log.error("ParseException: " + e.getMessage());
			if(reConnectionCnt < 5)	CreateAccount(memberDto, chainNode);
		} catch (Exception e) {
			log.error("Exception: " + e.getLocalizedMessage());
			if(reConnectionCnt < 5)	CreateAccount(memberDto, chainNode);
		}
		
		return null;
	}

}
