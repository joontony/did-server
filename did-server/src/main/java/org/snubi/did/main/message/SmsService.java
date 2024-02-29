package org.snubi.did.main.message;

import java.util.ArrayList;
import java.util.List;
import org.snubi.did.main.entity.Notification;
import org.snubi.did.main.entity.SmsLog;
import org.snubi.did.main.repository.NotificationRepository;
import org.snubi.did.main.service.NetworkService;
import org.snubi.lib.json.JsonUtil;
import org.snubi.lib.response.SnubiResponse;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService, NetworkService  {	
	
	private final NotificationRepository notificationRepository;
	
	public void sendSms(List<String> destin, List<String> message) {	
		try {			
			log.info("--------------------------------------------");
			log.info("문자전송 [인증번호] : 로그저장안한다. 회원에게 로그 안내려주려고 다만 문자전송로그로 확인가능하다.");
			log.info("--------------------------------------------");
			JsonUtil<MessageSmsDto> clsJsonUtil = new JsonUtil<MessageSmsDto>();					
			String json = clsJsonUtil.toString(
					MessageSmsDto.builder()
					.destin(destin)
					.message(message)
					.build());				
			SnubiResponse clsSnubiResponse = HttpService.postMessageServerSms(json);					
			ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(clsSnubiResponse.getStrData().toString());
		    log.info("문자전송응답 {}", jsonNode); 					
			
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}			
	}
	
	public void sendSms(List<String> destin, List<String> message, List<SmsLog> smsLog) {	
		try {			
			log.info("--------------------------------------------");
			log.info("문자전송 [클럽초대]");
			log.info("--------------------------------------------");
			JsonUtil<MessageSmsDto> clsJsonUtil = new JsonUtil<MessageSmsDto>();					
			String json = clsJsonUtil.toString(
					MessageSmsDto.builder()
					.destin(destin)
					.message(message)
					.build());		
			SnubiResponse clsSnubiResponse = HttpService.postMessageServerSms(json);					
			ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(clsSnubiResponse.getStrData().toString());
		    log.info("문자전송응답 {}", jsonNode); 		
			
			log.info("--------------------------------------------");
			log.info("Notification 저장");
			log.info("--------------------------------------------");	
			List<Notification> notificationList = new ArrayList<>();
			for(SmsLog item : smsLog) {
				Notification notification = Notification.builder()
			    		.senderMemberId(item.getSenderMemberId())
			    		.receiverMemberId(null)
			    		.receiverMobileNumber(item.getReceiverMobileNumber())
			    		.pushLog(null)
			    		.smsLog(item)
			    		.emailLog(null)
			    		.build();
				notificationList.add(notification);			    
			}
			notificationCreate(notificationList);
			log.info("Notification 저장완료");
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}			
	}
	
	public void onlySaveSms(List<String> destin, List<String> message, List<SmsLog> smsLog) {	
		try {			
			log.info("--------------------------------------------");
			log.info("문자전송로그만 저장 [클럽초대]");
			log.info("Notification 저장");
			log.info("--------------------------------------------");	
			List<Notification> notificationList = new ArrayList<>();
			for(SmsLog item : smsLog) {
				Notification notification = Notification.builder()
			    		.senderMemberId(item.getSenderMemberId())
			    		.receiverMemberId(null)
			    		.receiverMobileNumber(item.getReceiverMobileNumber())
			    		.pushLog(null)
			    		.smsLog(item)
			    		.emailLog(null)
			    		.build();
				notificationList.add(notification);			    
			}
			notificationCreate(notificationList);
			log.info("Notification 저장완료");
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}			
	}

	@Override
	public boolean notificationCreate(List<Notification> notificationList) {			
		notificationRepository.saveAll(notificationList);
		return true;
	}
}
