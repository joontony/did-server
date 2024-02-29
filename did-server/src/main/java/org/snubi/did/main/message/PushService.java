package org.snubi.did.main.message;

import java.util.ArrayList;
import java.util.List;

import org.snubi.did.main.entity.Notification;
import org.snubi.did.main.entity.PushLog;
import org.snubi.did.main.repository.NotificationRepository;
import org.snubi.did.main.repository.PushLogRepository;
import org.snubi.did.main.service.NetworkService;
import org.snubi.lib.response.SnubiResponse;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushService implements NotificationService , NetworkService {
	
	private final PushLogRepository pushLogRepository;
	private final NotificationRepository notificationRepository;
	
	public void sendPush(String clsPushNotification, PushLog pushLog) {
		log.info("--------------------------------------------");
		log.info("푸시전송");
		log.info("--------------------------------------------");		
		try {			
			SnubiResponse clsSnubiResponse = HttpService.postPushServer(clsPushNotification);	
			log.info("Push-Json : {}",		clsPushNotification);
			log.info("Push-Result : {}",	clsSnubiResponse.getStrBuffer());				
			ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(clsSnubiResponse.getStrBuffer().toString());
		    String success = jsonNode.get("success").asText();	
		    boolean confirmFlag = false;
		    if("1".equals(success)) confirmFlag = true;
		    //log.info("Push-Result : success {} : [0:FAIL,1:SUCCESS]",	success);		    
		    pushLog.updatePushLog(clsPushNotification,confirmFlag);
		    pushLogRepository.save(pushLog);
		    
		    List<Notification> notificationList = new ArrayList<>();
		    Notification notification = Notification.builder()
		    		.senderMemberId(pushLog.getSenderMemberId())
		    		.receiverMemberId(pushLog.getReceiverMemberId())
		    		.receiverMobileNumber(null)
		    		.pushLog(pushLog)
		    		.smsLog(null)
		    		.emailLog(null)
		    		.build();
		    notificationList.add(notification);	
		    notificationCreate(notificationList);
		    log.info("Notification 저장완료");		
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}
	}
	
	public PushLog sendPushReturn(String clsPushNotification, PushLog pushLog) {
		log.info("--------------------------------------------");
		log.info("푸시전송:(메시지)알림");
		log.info("--------------------------------------------");		
		try {			
			SnubiResponse clsSnubiResponse = HttpService.postPushServer(clsPushNotification);	
			log.info("Push-Json : {}",		clsPushNotification);
			log.info("Push-Result : {}",	clsSnubiResponse.getStrBuffer());				
			ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(clsSnubiResponse.getStrBuffer().toString());
		    String success = jsonNode.get("success").asText();	
		    boolean confirmFlag = false;
		    if("1".equals(success)) confirmFlag = true;
		    //log.info("Push-Result : success {} : [0:FAIL,1:SUCCESS]",	success);		    
		    pushLog.updatePushLog(clsPushNotification,confirmFlag);
		    PushLog savedPushLog = pushLogRepository.save(pushLog);
		    
		    List<Notification> notificationList = new ArrayList<>();
		    Notification notification = Notification.builder()
		    		.senderMemberId(pushLog.getSenderMemberId())
		    		.receiverMemberId(pushLog.getReceiverMemberId())
		    		.receiverMobileNumber(null)
		    		.pushLog(pushLog)
		    		.smsLog(null)
		    		.emailLog(null)
		    		.build();
		    notificationList.add(notification);	
		    notificationCreate(notificationList);
		    log.info("Notification 저장완료");		
		    return savedPushLog;
		} catch (Exception Ex) {
			Ex.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean notificationCreate(List<Notification> notificationList) {			
		notificationRepository.saveAll(notificationList);
		return true;
	}
	
}
