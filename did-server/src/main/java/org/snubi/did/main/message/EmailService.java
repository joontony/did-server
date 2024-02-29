package org.snubi.did.main.message;

import java.util.ArrayList;
import java.util.List;

import org.snubi.did.main.entity.EmailLog;
import org.snubi.did.main.entity.MemberDid;
import org.snubi.did.main.entity.Notification;
import org.snubi.did.main.entity.SmsLog;
import org.snubi.did.main.repository.NotificationRepository;
import org.snubi.did.main.service.NetworkService.HttpService;
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
public class EmailService implements NotificationService  {
	
	private final NotificationRepository notificationRepository;	
	
	public void sendEmail(List<String> destin, List<String> title, List<String> message, List<EmailLog> emailLog) {	
		try {			
			log.info("--------------------------------------------");
			log.info("이메일전송");
			log.info("--------------------------------------------");
			JsonUtil<MessageEmailDto> clsJsonUtil = new JsonUtil<MessageEmailDto>();					
			String json = clsJsonUtil.toString(
					MessageEmailDto.builder()
					.destin(destin)
					.title(title)
					.message(message)
					.build());					
			SnubiResponse clsSnubiResponse = HttpService.postMessageServerEmail(json);					
			ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(clsSnubiResponse.getStrData().toString());
		    log.info("이메일전송응답 {}", jsonNode); 	
		    
		    log.info("--------------------------------------------");
			log.info("Notification 저장");
			log.info("--------------------------------------------");	
			List<Notification> notificationList = new ArrayList<>();
			for(EmailLog item : emailLog) {
				Notification notification = Notification.builder()
			    		.senderMemberId(item.getSenderMemberId())
			    		.receiverMemberId(item.getReceiverMemberId())
			    		.receiverMobileNumber(null)
			    		.pushLog(null)
			    		.smsLog(null)
			    		.emailLog(item)
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
