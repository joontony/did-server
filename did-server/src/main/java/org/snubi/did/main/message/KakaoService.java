package org.snubi.did.main.message;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;
import org.snubi.did.main.entity.Notification;
import org.snubi.did.main.service.NetworkService.HttpService;
import org.snubi.lib.response.SnubiResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoService {

	private static final String apiKey = "NCS3TZGEQVGC8LPR";
	private static final String apiSecret = "TGB0V6JTRWMWWHMVEEUTJYARHETAQV0H";
	
	public void sendKakao(String kakaoDtoJson) {
		
		String header = getHeader();
		log.info("카카오알림톡전송 : 해더생성 {}", header);
		
		try {			
			SnubiResponse clsSnubiResponse = HttpService.postKakaoTalkServer(kakaoDtoJson, header);	
			log.info("Kakao-Json : {}",		kakaoDtoJson);
			log.info("Kakao-Result : {}",	clsSnubiResponse.getStrBuffer());				
//			ObjectMapper objectMapper = new ObjectMapper();
//		    JsonNode jsonNode = objectMapper.readTree(clsSnubiResponse.getStrBuffer().toString());
//		    String success = jsonNode.get("success").asText();	
		   
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}
	}
	
	
    private String getHeader()  {
    	try {            
            String salt = UUID.randomUUID().toString().replaceAll("-", "");
            String date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString().split("\\[")[0];

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            String signature = new String(Hex.encodeHex(sha256_HMAC.doFinal((date + salt).getBytes(StandardCharsets.UTF_8))));
            return "HMAC-SHA256 ApiKey=" + apiKey + ", Date=" + date + ", salt=" + salt + ", signature=" + signature;
        } catch (Exception e) {
            e.printStackTrace();     
            return "";
        }
    }
}
