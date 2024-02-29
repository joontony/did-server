package org.snubi.did.main;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.snubi.did.main.common.CustomResponseEntity;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.entity.AgentClub;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.repository.AgentClubRepository;
import org.snubi.did.main.response.CustomResponse;
import org.snubi.did.main.response.DataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(true)
public class MemberTest {
	
	@Autowired    MockMvc mockMvc;
    private final static String didDevUrl = "https://avchain8.snubi.org:31001";    
    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new Gson();

    @Autowired AgentClubRepository agentClubRepository;
    
	@Test
	void memberMobileTest() throws Exception {
		testByte();
//		Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(2L);
//		if(clsAgentClub.isPresent()) {				
//			if( clsAgentClub.get().getAgent().getAgentSeq() == 1) 
//				log.info("clsAgentClub 1");
//			else if( clsAgentClub.get().getAgent().getAgentSeq() == 3) 
//				log.info("clsAgentClub 3");
//			else 
//				log.info("clsAgentClub blank");		
//		}else {
//			log.info("clsAgentClub error");
//		}		
		
		/*
		 * 
    	log.info("memberMobileTest begin");
    	System.setProperty("https.protocols", "TLSv1.3"); // 혹은 다른 TLS 버전
		StopWatch sw = new StopWatch();
        sw.start();
        //for (int i = 0; i < 10; i++) {        	
			String url = didDevUrl + "/member/mobile/01000005556" ;				
	        String response = restTemplate.getForObject(url, String.class);
	        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
	        CustomResponse customResponse = objectMapper.readValue(response, CustomResponse.class);
	        
	        JsonObject obj = gson.toJsonTree(customResponse.getData()).getAsJsonObject();
	        DataObject.Defaults defaults = objectMapper.readValue(obj.toString(), DataObject.Defaults.class);
	        log.info("memberMobileTest getMobileAuthNumber : {}", defaults.getMobileAuthNumber());
	        
	        
	        // 테스트 코드 작성
	        //assertEquals("Expected Response", response);
			
//			mockMvc.perform(MockMvcRequestBuilders.get(url)
//	                .contentType(MediaType.APPLICATION_JSON)).andExpect(result -> {
//	                	MockHttpServletResponse response = result.getResponse();
//	                	log.info("memberMobileTest response : {}", response.getContentAsString());
//	                	//sw.stop();
//	                    //log.info("성능 측정 걸린시간: {}/ms {}/second", sw.getLastTaskTimeMillis(), sw.getTotalTimeSeconds());
//	                });
		//}
		sw.stop();
        log.info("성능 측정 걸린시간: {}/ms {}/second", sw.getLastTaskTimeMillis(), sw.getTotalTimeSeconds());
		// 성능 측정 걸린시간: 1946/ms 1.946777956/second
		 
		 */
	}
	
    private void testByte() {
    	String text = "안녕하세요안녕하세요안녕하하하1원장님이 진료증을 발급했습니다. 앱마켓 https://zrr.kr/ErbN"; // 예시 문자열
    	
    	// 바이트 수를 계산할 변수
        int byteCount = 0;
        for (char c : text.toCharArray()) {
            if (isKorean(c)) {
                byteCount += 2;
            } else {
                byteCount += 1; 
            }
        }
        log.info("UTF-16 인코딩으로 변환한 바이트 수: {}" , byteCount);
        if(byteCount <= 25) {
        	log.info( "원장님이 진료증을 발급했습니다. 진료증앱 설치 https://zrr.kr/ErbN");
	    }else if(byteCount > 25 && byteCount <= 31) {
	    	log.info( "원장님이 진료증을 발급했습니다. 앱마켓 https://zrr.kr/ErbN");
	    }else if(byteCount > 31 && byteCount <= 35) {
	    	log.info( "님이 진료증을 발급했습니다. 앱마켓 https://zrr.kr/ErbN");
	    }else {
	    	log.info( "님이 진료증을 발급했습니다. https://zrr.kr/ErbN");
	    }
        
        // 문자열을 UTF-16 인코딩으로 바이트로 변환
        //byte[] utf16Bytes = text.getBytes(java.nio.charset.StandardCharsets.UTF_16);
        // 바이트 수 출력
        //int byteCount = utf16Bytes.length;
        
    }
    
 // 주어진 문자가 한글인지 확인하는 메서드
    private static boolean isKorean(char c) {
        return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES ||
                Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_JAMO ||
                Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO;
    }
}
