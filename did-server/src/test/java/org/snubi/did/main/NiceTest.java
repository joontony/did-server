package org.snubi.did.main;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.snubi.did.main.manager.NiceManager;
import org.snubi.did.main.manager.NiceManager.NiceToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NiceTest {

	@Autowired NiceManager niceManager;
	
	@SuppressWarnings("static-access")
	@Test
	void singletonTest() {
		//NiceManager.getInstance().addNiceToken("token_version_id2", "hmac_key3", "iv3");
		
		niceManager.addNiceToken("token_version_id3", "hmac_key1", "iv1");
		
		//niceManager.addNiceToken("token_version_id2", "hmac_key2", "iv2");
		
		//NiceToken niceToken = NiceManager.getInstance().getToken("token_version_id2");
		
		NiceToken niceToken2 = niceManager.getToken("token_version_id3");
		
//		if (niceToken.equals(niceToken2)) {
//			log.error("niceToken과 niceToken2가 같은 객체입니다.");
//		} else {
//		    log.error("niceToken과 niceToken2가 다른 객체입니다.");
//		}
		
		
//		log.info("---------getToken {}", niceToken.getToken());
//		log.info("---------getKey {}", niceToken.getKey());
//		log.info("---------getIv {}", niceToken.getIv());
		
		log.info("---------getToken2 {}", niceToken2.getToken());
		log.info("---------getKey2 {}", niceToken2.getKey());
		log.info("---------getIv2 {}", niceToken2.getIv());
		
		log.info("");
		log.info("");
		
		List<NiceToken> list = niceManager.getAll();
		for(NiceToken item : list) {
			log.info("---------list getToken {}", item.getToken());
			log.info("---------list getKey {}", item.getKey());
			log.info("---------list getIv {}", item.getIv());
		}
	}
}
