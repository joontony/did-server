package org.snubi.did.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.snubi.did.main.response.CustomResponse;
import org.snubi.did.main.response.DataObject;
import org.snubi.did.main.response.MyApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@Rollback(true)
public class ApiTest {
	@Autowired   MockMvc mockMvc;
    //private final static String didDevUrl = "https://avchain8.snubi.org:31001";   
	private final static String didDevUrl = "http://localhost:30001";   
    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new Gson();
    private HttpHeaders headers = new HttpHeaders();
    private StopWatch sw = new StopWatch();
    private WebClient webClient;
    private final MyApiService myApiService = new MyApiService(new RestTemplate());
    int num = 100;
	@Test
	void memberMobileTest() {
		try {
			log.info("----------------------------------------------------------------------------");
	    	log.info("memberMobileTest begin");
	    	log.info("----------------------------------------------------------------------------");
	    	//System.setProperty("https.protocols", "TLSv1.3"); // 혹은 다른 TLS 버전			
	        sw.start();
	        
	        
	        
	        // 동기 
	        /*
	        for (int i = 100; i < 999; i++) {        	
				String url = didDevUrl + "/member/mobile/01000002" + i ;				
		        String response = restTemplate.getForObject(url, String.class);
		        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		        CustomResponse customResponse = objectMapper.readValue(response, CustomResponse.class);
		        
		        JsonObject obj = gson.toJsonTree(customResponse.getData()).getAsJsonObject();
		        DataObject.Defaults defaults = objectMapper.readValue(obj.toString(), DataObject.Defaults.class);
		        log.info("memberMobileTest getMobileNumber : {}", defaults.getMobileNumber());		        
		        memberMobileAuth(defaults);		        
		    }
	        */
	        
	        // 동시에 실행할 스레드 수 설정
	        int concurrentThreads = 100;
	        
	        // ExecutorService를 사용하여 동시에 여러 스레드 실행
	        ExecutorService executorService = Executors.newFixedThreadPool(concurrentThreads);

	        for (num = 100; num < 100 + concurrentThreads; num++) {  
	            executorService.submit(() -> {
	            	String url = didDevUrl + "/member/mobile/01000002" + num ;
	            	log.info("memberMobileTest num : {}", num);	
	                String response = myApiService.fetchDataFromApi(url);
	                //log.info("memberMobileTest response : {}", response);
	                try {
		                ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
				        CustomResponse customResponse = objectMapper.readValue(response, CustomResponse.class);
				        
				        JsonObject obj = gson.toJsonTree(customResponse.getData()).getAsJsonObject();
				        DataObject.Defaults defaults = objectMapper.readValue(obj.toString(), DataObject.Defaults.class);
				        	
	                }catch(Exception e) {
	                	log.error("Exception : {}", e.getMessage());	
	                }
	            });
	        }

	        // 모든 스레드가 완료될 때까지 대기
	        executorService.shutdown();
	        executorService.awaitTermination(20, TimeUnit.SECONDS);
	        
	        
			sw.stop();
	        log.info("성능 측정 걸린시간: {}/ms {}/second", sw.getLastTaskTimeMillis(), sw.getTotalTimeSeconds());
			// 성능 측정 걸린시간: 302/ms 0.302624396/second
	        // 100개 성능 측정 걸린시간: 10987/ms 10.987150749/second
		}catch(Exception e) {
			log.info("Exception {}", e.getMessage());
		}
	}
	
	public Mono<String> fetchDataAsync(int i) {
        // 비동기로 호출
        return webClient.get()
        		.uri("/member/mobile/01000002" + i)
                .retrieve()
                .bodyToMono(String.class);
    }
	
  
   private void memberMobileAuth(DataObject.Defaults defaults) throws Exception  {
    	String url = didDevUrl + "/member/mobile/auth" ;	
    	headers.set("Content-Type", "application/json");
    	
    	JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("memberId", defaults.getMemberId());
        jsonObject.addProperty("mobileNumber", defaults.getMobileNumber());
        jsonObject.addProperty("mobileAuthNumber", defaults.getMobileAuthNumber());
        
        String requestBody = jsonObject.toString();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        CustomResponse customResponse = objectMapper.readValue(response, CustomResponse.class);
        //log.info("memberMobileAuth data : {}", customResponse.getData());
    }
	
	
}
