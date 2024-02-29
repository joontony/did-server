package org.snubi.did.main.controller;

import java.util.Optional;
import org.snubi.did.main.common.CustomResponseEntity;
import org.snubi.did.main.dto.PersonalMessageDto;
import org.snubi.did.main.entity.Member;
import org.snubi.did.main.rabbitmq.ChatMessageDto;
import org.snubi.did.main.repository.MemberRepository;
import org.snubi.did.main.service.PersonalMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PersonalMessageController extends WithAuthController {
	
	private final PersonalMessageService personalMessageService;
	@Autowired private MemberRepository memberRepository;
	
	// 1:1 파일전송
	@RequestMapping(value = "/club/socket/message/create", method = RequestMethod.POST)
	public ResponseEntity<?> socketMessageCreate(
			@RequestParam("files") MultipartFile[] files, 
			@RequestPart ChatMessageDto chatMessageDto) throws Exception {			
		//if("not-login".equals(getClaims().getId())) {
		//	throw new CustomException(ErrorCode.UNAUTHORIZED);				
		//}else {	
			return CustomResponseEntity.succResponse(personalMessageService.socketMessageCreate(files, chatMessageDto),"");	
		//}	
	}	
	
	// 1:1 채팅용으로  stomp에서 요청한다  
	@MessageMapping("/rabbit/send-message")
    public void sendMessageToRabbit(ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor headerAccessor) {    	
    	log.info("[1:1채팅] RabbitMQ 브로커에 온/오프라인 사용자를 위한 메시지를 전송한다.");           
    	personalMessageService.sendMessageToRabbit(chatMessageDto);
    }  
	
	// 사용자가 웹 소켓 연결하면 실행됨
	@EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        log.info("Received a new web socket connection");
        
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());               
        String accessUserId = accessor.getFirstNativeHeader("sender"); 
        log.info("accessUserId connected : " + accessUserId);
        Optional<Member> member =  memberRepository.findByMemberId(accessUserId);
        if(member.isPresent()) { 
        	member.get().updateActiveSocketFlag(true); 
        	memberRepository.save(member.get());
        }
    }

    // 사용자가 웹 소켓 연결을 끊으면 실행됨
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    	 StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());               
         String accessUserId = accessor.getFirstNativeHeader("sender"); 
         log.info("accessUserId Disconnected : " + accessUserId);
         Optional<Member> member =  memberRepository.findByMemberId(accessUserId);
         if(member.isPresent()) {
        	 member.get().updateActiveSocketFlag(false); 
        	 memberRepository.save(member.get());
         }
         
    }	
    
    // 1:N 메시지전송
	@RequestMapping(value = "/club/personal/message/create", method = RequestMethod.POST)
	public ResponseEntity<?> personalMessageCreate(
			@RequestParam("files") MultipartFile[] files, 
			@RequestPart PersonalMessageDto personalMessageDto) throws Exception {			
		//if("not-login".equals(getClaims().getId())) {
		//	throw new CustomException(ErrorCode.UNAUTHORIZED);				
		//}else {	
			return CustomResponseEntity.succResponse(personalMessageService.personalMessageCreate(files, personalMessageDto),"");	
		//}	
	}	
	// 1:N 메시지리스트
	@RequestMapping(value = "/club/personal/message/list/{clubSeq}/{receiverMemberId}", method = RequestMethod.GET)
	public ResponseEntity<?> clubListMy(@PathVariable Long clubSeq,@PathVariable String receiverMemberId, Pageable pageable) throws Exception {		
		//if("not-login".equals(getClaims().getId())) {
		//	throw new CustomException(ErrorCode.UNAUTHORIZED);				
		//}else {	
			return CustomResponseEntity.succResponse(personalMessageService.personalMessageList(clubSeq,receiverMemberId,pageable),"");
		//}
	}
	
	// 테스트용 
//	@RequestMapping(value = "/club/personal/message/entity/listener", method = RequestMethod.GET)
//	public ResponseEntity<?> sendMessageAdmin() throws Exception {		
//		//if("not-login".equals(getClaims().getId())) {
//		//	throw new CustomException(ErrorCode.UNAUTHORIZED);				
//		//}else {	
//			return CustomResponseEntity.succResponse(personalMessageService.sendMessageAdmin(),"");
//		//}
//	}

}
