package org.snubi.did.main.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.snubi.did.main.config.CustomConfig;
import org.snubi.did.main.dto.NiceDto;
import org.snubi.did.main.dto.NiceDto.DataBody;
import org.snubi.did.main.dto.NiceDto.DataHeader;
import org.snubi.did.main.dto.ResponseNiceDto;
import org.snubi.did.main.manager.NiceManager;
import org.snubi.lib.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthContorller extends WithAuthController {	

	@Autowired NiceManager niceManager;
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/request/nice", method = RequestMethod.GET)
	public String requestNice(Model model) throws Exception {		
		
		String token_version_id = "";
		String enc_data = "";
		String integrity_value = "";
		
		try {		
			
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	        String formattedDateTime = currentDateTime.format(formatter);	        
			JsonUtil<NiceDto> clsJsonUtil = new JsonUtil<NiceDto>();			  
			DataHeader dataHeader = DataHeader.builder().CNTY_CD("ko").build();
			DataBody dataBody = DataBody.builder().req_dtim( formattedDateTime ).req_no(formattedDateTime).enc_mode("1").build();			 
	        String json = clsJsonUtil.toString(        
	        		NiceDto.builder()
	        		.dataHeader(dataHeader)
		            .dataBody(dataBody)
		            .build());
	        log.info("--------------------------------------------json {}", json);	
	        long cs = new Date().getTime()/1000; 
	        // access_token + ":" +cs + ":" + clientId(username)
	        String auth = "7b4aa4a3-42b7-4a37-bf94-7b523d7b8512" + ":" + cs + ":" + "b7cfdac4-9603-4fda-a4fa-3c0f06cb78ca";	        
	        log.info("--------------------------------------------auth {}", auth);
	        
	    
	        byte[] encodedBytes = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
	        String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);
	        log.info("--------------------------------------------encodedString {}", encodedString);
	        
	        String urlString = "https://svc.niceapi.co.kr:22001/digital/niceid/api/v1.0/common/crypto/token";
			StringBuffer sbufferBuffer = new StringBuffer();
			String strBuffer = new String();
			OutputStream clsOutputStream = null;			
			URL 				clsURL 					= null;
			HttpURLConnection 	clsHttpURLConnection 	= null;
			BufferedReader 		clsBufferedReader 		= null;
		    try {
		    	clsURL = new URL(urlString);
		 	    clsHttpURLConnection = (HttpURLConnection)clsURL.openConnection();
		 	    clsHttpURLConnection.setRequestProperty("Content-Type", "application/json");
		 	    clsHttpURLConnection.setRequestMethod("POST");
		 	    clsHttpURLConnection.setRequestProperty("ProductID","2101979031");
		 	    clsHttpURLConnection.setDoOutput(true);
		 	    clsHttpURLConnection.setRequestProperty("Authorization", "Bearer" + " " + encodedString);		 	   
		 	    clsHttpURLConnection.connect();
		 	    clsOutputStream = clsHttpURLConnection.getOutputStream();
		 	    clsOutputStream.write(json.getBytes("UTF-8"));	   
		 	    clsOutputStream.flush();
		 	    clsOutputStream.close();				
		 	    clsBufferedReader = new BufferedReader(new InputStreamReader(clsHttpURLConnection.getInputStream(),"UTF-8"));
		 	    while ((strBuffer = clsBufferedReader.readLine()) != null) {
		 	    	sbufferBuffer.append(strBuffer);
		 	    }	 	    
		 	  
		 	    log.info("--------------------------------------------clsSnubiResponse {}", sbufferBuffer.toString());
		 	    
		 	    ObjectMapper objectMapper = new ObjectMapper();
		 	    ResponseNiceDto responseNiceDto = objectMapper.readValue(sbufferBuffer.toString(), ResponseNiceDto.class);
			   
			    log.info("--------------------------------------------responseNiceDto {}", responseNiceDto.getDataHeader().getGW_RSLT_MSG());
			    token_version_id = responseNiceDto.getDataBody().getToken_version_id();
		 	    
			    String value = dataBody.getReq_dtim() + dataBody.getReq_no() + responseNiceDto.getDataBody().getToken_val();
			    MessageDigest md = MessageDigest.getInstance("SHA-256");
			    md.update(value.getBytes());
			    byte[] arrHashValue = md.digest();			    
			    byte[] encodedBytes2 = Base64.getEncoder().encode(arrHashValue);
		        String resultVal = new String(encodedBytes2, StandardCharsets.UTF_8);
		        log.info("--------------------------------------------resultVal {}", resultVal);
		        
		        String requestno = formattedDateTime;
		        String returnurl = CustomConfig.strNiceReturnURL;//"https://rhymecard-dev.avchain.io/response/nice";
		        //String returnurl = "http://localhost:30001/response/nice";
		        //String returnurl = "http://172.30.1.49:30001";
		        String sitecode = responseNiceDto.getDataBody().getSite_code();
		        String popupyn = "Y";
		        String receivedata = "xxx";
		        Map<String, String> jsonMap = new HashMap<>();
		        jsonMap.put("requestno", requestno);
		        jsonMap.put("returnurl", returnurl);
		        jsonMap.put("sitecode", sitecode);
		        jsonMap.put("popupyn", popupyn);
		        jsonMap.put("receivedata", receivedata);
		        Gson gson = new Gson();
		        String reqData = gson.toJson(jsonMap);
		        
		        int length = resultVal.length();
		        int lastThreeIndex = length - 16;
		        String key = resultVal.substring(0, 16);                              
		        String iv = resultVal.substring(lastThreeIndex, length);    
		        String hmac_key = resultVal.substring(0, 32); 
		        
		        // http://localhost:30001/request/nice/token          
		        // http://172.30.1.49:30001/request/nice/token   		        
		        // https://rhymecard-dev.avchain.io/request/nice/token   
		        // https://rhymecard-dev.avchain.io/form.html 
		        
		        
		        log.info("--------------------------------------------reqData {}", reqData);
		        log.info("--------------------------------------------key {}", key);
		        log.info("--------------------------------------------iv {}", iv);
		        log.info("--------------------------------------------hmac_key {}", hmac_key);
		        		        
		        // 암호화                               
		        SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");                              
		        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");                              
		        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));                              
		        byte[] encrypted = c.doFinal(reqData.trim().getBytes());    		        
		        byte[] encodedBytes3 = Base64.getEncoder().encode(encrypted);
		        enc_data = new String(encodedBytes3, StandardCharsets.UTF_8);			        
		        byte[] hmacSha256 = hmac256(hmac_key.getBytes(), enc_data.getBytes());    
		        integrity_value = Base64.getEncoder().encodeToString(hmacSha256); 		        
		       
	            log.info("--------------------------------------------token_version_id {}", token_version_id);
	            log.info("--------------------------------------------enc_data {}", enc_data);
	            log.info("--------------------------------------------integrity_value {}", integrity_value);
	            
	            
	           // NiceManager.getInstance().addNiceToken(token_version_id, key, iv);
	            niceManager.addNiceToken(token_version_id, key, iv);
	            
	            model.addAttribute("token_version_id", token_version_id);
	            model.addAttribute("enc_data", enc_data);
	            model.addAttribute("integrity_value", integrity_value);
	          
	            return "form";
		    } catch(Exception Ex) {
		    	throw Ex;
		    } finally {
		    	try {clsOutputStream.close();         	} catch(Exception Ex) {}
		 	    try {clsBufferedReader.close();         } catch(Exception Ex) {}
		 	    try {clsHttpURLConnection.disconnect();	} catch(Exception Ex) {}
		    }
			
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}			
		return null;		
	}
	
	public static byte[] hmac256(byte[] secretKey,byte[] message){                              
	      byte[] hmac256 = null;                              
	      try{                              
	            Mac mac = Mac.getInstance("HmacSHA256");                              
	            SecretKeySpec sks = new SecretKeySpec(secretKey, "HmacSHA256");                              
	            mac.init(sks);                              
	            hmac256 = mac.doFinal(message);                              
	            return hmac256;                              
	      } catch(Exception e){                              
	            throw new RuntimeException("Failed to generate HMACSHA256 encrypt");                              
	      }                              
	}                              
	   
}
