package org.snubi.did.main.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

	private final SimpMessagingTemplate messagingTemplate;
	
	// chatRoom 생성 여기서하면 로드가 걸리므로 메시지리스트 호출시 없으면 생성하자! 여기서는 그냥오류리턴만!
    @RabbitListener(queues = "chat.queue")
    public void receiveMessage(ChatMessageDto chatMessageDto) {
        log.info("MQ데이터받아서소켓으로전송한다. 여기서 온/오프라인사용자 모두 소켓전송, 오프라인사용자는 푸시전송  {} " , chatMessageDto);         
        String destination = "/queue/subscribe-message/" + chatMessageDto.getRoutingKey();
        messagingTemplate.convertAndSend(destination, chatMessageDto);      
    }
    
//    private ChatRoom getChatRoom(ChatMessageDto chatMessageDto) {    	
//        Optional<ChatRoom> chatRoom =  chatRoomRepository.findByClub_ClubSeqAndSessionId(chatMessageDto.getClubSeq(),chatMessageDto.getRoutingKey());
//    	if(chatRoom.isPresent()) {
//    		return chatRoom.get();
//    	}else {
//    		// 나중에 삭제예정 
//    		Optional<Club> club = clubRepository.findByClubSeq(chatMessageDto.getClubSeq());
//			ChatRoom clsChatRoom = ChatRoom.builder()
//					.club(club.get())
//					.sessionId(chatMessageDto.getRoutingKey())
//					.build();
//			return chatRoomRepository.save(clsChatRoom);
//    	}
//    }
//    
//    private void saveMessage(ChatRoom chatRoom, ChatMessageDto chatMessageDto, boolean pushFlag, PushLog pushLog) {  
//    	boolean readFlag = !pushFlag;    	
//		ChatMessage chatMessage = ChatMessage.builder()
//				.chatRoom(chatRoom)
//				.pushLog(pushLog)
//				.senderMemberId(chatMessageDto.getSender())
//				.receiverMemberId(chatMessageDto.getReceiver())
//				.filePath(null)
//				.originalFileName(null)
//				.readFlag(readFlag)
//				.message(chatMessageDto.getMessage())
//				.build();
//		chatMessageRepository.save(chatMessage);    	
//    }
//    
//    private PushLog pushMessage(ChatRoom chatRoom,ChatMessageDto chatMessageDto) {
//    	Optional<Club> club = clubRepository.findByClubSeq(chatMessageDto.getClubSeq());
//    	Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(6L);
//    	JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();	
//    	try {
//	    	String json = clsJsonUtil.toString(
//					PushDto.builder()
//					.title("라임카드]알림")
//					.body(chatMessageDto.getMessage())	
//					.clubName(club.get().getClubName())
//					.pushType(pushType.get().getTypeCode())
//					.deviceId("")
//					.clubSeq(chatMessageDto.getClubSeq())
//					.publicKey("")
//					.clubInvitationSeq(null)
//					.podUrl("")
//					.chatRoomSeq(chatRoom.getChatRoomSeq())
//					.sessionId(chatMessageDto.getRoutingKey())
//					.build());
//			PushLog pushLog = PushLog.builder()
//					.pushType(pushType.get())
//					.club(club.get())
//					.clubNoticeSeq(null)
//					.senderMemberId(chatMessageDto.getSender())
//					.receiverMemberId(chatMessageDto.getReceiver())
//					.title("라임카드]알림")
//					.message(chatMessageDto.getMessage())
//					.linkedUrl("")
//					.extraMessage("")
//					.confirmFlag(false)
//					.readFlag(false)
//					.build();	
//			return pushService.sendPushReturn(json,pushLog);
//    	} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	return null;
//    }
}
