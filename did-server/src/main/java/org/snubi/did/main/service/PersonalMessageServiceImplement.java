package org.snubi.did.main.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.snubi.did.main.config.CustomConfig;
import org.snubi.did.main.dto.PersonalMessageDto;
import org.snubi.did.main.entity.ChatMessage;
import org.snubi.did.main.entity.ChatRoom;
import org.snubi.did.main.entity.Club;
import org.snubi.did.main.entity.PersonalMessage;
import org.snubi.did.main.entity.PushLog;
import org.snubi.did.main.entity.PushType;
import org.snubi.did.main.message.PushDto;
import org.snubi.did.main.message.PushService;
import org.snubi.did.main.rabbitmq.ChatMessageDto;
import org.snubi.did.main.rabbitmq.SocketSessionManager;
import org.snubi.did.main.repository.ChatMessageRepository;
import org.snubi.did.main.repository.ChatRoomRepository;
import org.snubi.did.main.repository.ClubRepository;
import org.snubi.did.main.repository.PersonalMessageRepository;
import org.snubi.did.main.repository.PushTypeRepository;
import org.snubi.lib.json.JsonUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalMessageServiceImplement implements PersonalMessageService {
	
	private final PushService pushService;
	private final PushTypeRepository pushTypeRepository;
	private final PersonalMessageRepository personalMessageRepository; 
	private final ClubRepository clubRepository; 
	private final SocketSessionManager sessionManager;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final RabbitTemplate rabbitTemplate;
	
	@Override
	public boolean socketMessageCreate(MultipartFile[] files, ChatMessageDto chatMessageDto) {		
		log.info("--------------------------------------------");
		log.info("1:1파일전송");
		log.info("--------------------------------------------");		
		Optional<Club> club = clubRepository.findByClubSeq(chatMessageDto.getClubSeq());		
		try {			
			if (club.isPresent()) {				
				
			   List<String> fileOriginNameArray = new ArrayList<>();	
			   List<String> filePathArray = new ArrayList<>();	
			   List<String> pureFilenameArray = new ArrayList<>();	
			   log.info("1:1파일전송 files.length : {}", files.length);
			   for(MultipartFile file : files) {
				    
					if (!file.isEmpty()) {
						String filename = file.getOriginalFilename();
					    String extension = filename.substring(filename.lastIndexOf(".") + 1);	
					    String pureFilename = filename.substring(0, filename.lastIndexOf("."));
						String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
						String folderName = club.get().getClubSeq() + "_clubSeq";	
						String fullPath = CustomConfig.strFileUploadPath + "/" + folderName + "/"+pureFilename+"_" + timestamp + "." + extension;
						
						File tmpFile = new File(fullPath);
						tmpFile.getParentFile().mkdir();
						file.transferTo(tmpFile);
						fileOriginNameArray.add(filename);
						filePathArray.add(fullPath);			
						pureFilenameArray.add(pureFilename);
						log.info("1:N메시지전송 filename : {}", filename);
					}					
			   }
			   log.info("1:N메시지전송 fileOriginNameArray.size() : {}", fileOriginNameArray.size());
			   
			   ChatRoom chatRoom = getChatRoom(chatMessageDto);  
		        boolean accessFlag = sessionManager.getAllUserSessions().containsKey(chatMessageDto.getReceiver());
		        if (!accessFlag) {
		            log.info("오프라인 사용자(받는 사람) 푸시 전송 사용자 아이디: {}", chatMessageDto.getReceiver());
		            PushLog pushLog = pushMessage(chatRoom,chatMessageDto);
		            saveMessage(chatRoom, chatMessageDto, true, pushLog, fileOriginNameArray, filePathArray);
		        }else {
		        	log.info("온라인 사용자(받는 사람) 사용자 아이디: {}", chatMessageDto.getReceiver());
		        	saveMessage(chatRoom, chatMessageDto, false, null, fileOriginNameArray, filePathArray);
		        }        
		        chatMessageDto.setOriginalFileName(fileOriginNameArray);
		        chatMessageDto.setFilePath(filePathArray);
		        chatMessageDto.setPureFileName(pureFilenameArray);
		        chatMessageDto.setFileUrl(CustomConfig.strDefaultImageUrl + chatMessageDto.getClubSeq() +"_clubSeq/");
		        rabbitTemplate.convertAndSend("chat.exchange", "room.key", chatMessageDto);													
			}	
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}
	
	@Override
    public void sendMessageToRabbit(ChatMessageDto chatMessageDto) {
    	ChatRoom chatRoom = getChatRoom(chatMessageDto);  
        boolean accessFlag = sessionManager.getAllUserSessions().containsKey(chatMessageDto.getReceiver());
        if (!accessFlag) {
            log.info("오프라인 사용자(받는 사람) 푸시 전송 사용자 아이디: {}", chatMessageDto.getReceiver());
            PushLog pushLog = pushMessage(chatRoom,chatMessageDto);
            saveMessage(chatRoom, chatMessageDto, true, pushLog, new ArrayList<>(), new ArrayList<>());
        }else {
        	log.info("온라인 사용자(받는 사람) 사용자 아이디: {}", chatMessageDto.getReceiver());
        	saveMessage(chatRoom, chatMessageDto, false, null, new ArrayList<>(),new ArrayList<>());
        }          
        rabbitTemplate.convertAndSend("chat.exchange", "room.key", chatMessageDto);	
    }
	
	private ChatRoom getChatRoom(ChatMessageDto chatMessageDto) {    	
        Optional<ChatRoom> chatRoom =  chatRoomRepository.findByClub_ClubSeqAndSessionId(chatMessageDto.getClubSeq(),chatMessageDto.getRoutingKey());
    	if(chatRoom.isPresent()) {
    		return chatRoom.get();
    	}else {
    		// 나중에 삭제예정 
    		Optional<Club> club = clubRepository.findByClubSeq(chatMessageDto.getClubSeq());
			ChatRoom clsChatRoom = ChatRoom.builder()
					.club(club.get())
					.sessionId(chatMessageDto.getRoutingKey())
					.build();
			return chatRoomRepository.save(clsChatRoom);
    	}
    }
    
    private void saveMessage(ChatRoom chatRoom, ChatMessageDto chatMessageDto, boolean pushFlag, PushLog pushLog, List<String> fileOriginNameArray, List<String> filePathArray) {  
    	boolean readFlag = !pushFlag;    	
		ChatMessage chatMessage = ChatMessage.builder()
				.chatRoom(chatRoom)
				.pushLog(pushLog)
				.senderMemberId(chatMessageDto.getSender())
				.receiverMemberId(chatMessageDto.getReceiver())
				.filePath(filePathArray.toString())
				.originalFileName(fileOriginNameArray.toString())
				.readFlag(readFlag)
				.message(chatMessageDto.getMessage())
				.build();
		chatMessageRepository.save(chatMessage);    	
    }
    
    private PushLog pushMessage(ChatRoom chatRoom,ChatMessageDto chatMessageDto) {
    	Optional<Club> club = clubRepository.findByClubSeq(chatMessageDto.getClubSeq());
    	Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(6L);
    	JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();	
    	try {
	    	String json = clsJsonUtil.toString(
					PushDto.builder()
					.title("라임카드]알림")
					.body(chatMessageDto.getMessage())	
					.clubName(club.get().getClubName())
					.pushType(pushType.get().getTypeCode())
					.deviceId("")
					.clubSeq(chatMessageDto.getClubSeq())
					.publicKey("")
					.clubInvitationSeq(null)
					.podUrl("")
					.chatRoomSeq(chatRoom.getChatRoomSeq())
					.sessionId(chatMessageDto.getRoutingKey())
					.build());
			PushLog pushLog = PushLog.builder()
					.pushType(pushType.get())
					.club(club.get())
					.clubNoticeSeq(null)
					.senderMemberId(chatMessageDto.getSender())
					.receiverMemberId(chatMessageDto.getReceiver())
					.title("라임카드]알림")
					.message(chatMessageDto.getMessage())
					.linkedUrl("")
					.extraMessage("")
					.confirmFlag(false)
					.readFlag(false)
					.build();	
			return pushService.sendPushReturn(json,pushLog);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
	@Override
	public boolean personalMessageCreate(MultipartFile[] files, PersonalMessageDto personalMessageDto) {		
		log.info("--------------------------------------------");
		log.info("1:N메시지전송");
		log.info("--------------------------------------------");		
		Optional<Club> club = clubRepository.findByClubSeq(personalMessageDto.getClubSeq());
		Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(5L);
		JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();	
		try {			
			if (club.isPresent()) {				
				
			   List<String> fileOriginNameArray = new ArrayList<>();	
			   List<String> filePathArray = new ArrayList<>();	
			   log.info("1:N메시지전송 files.length : {}", files.length);
			   for(MultipartFile file : files) {				   
					if (!file.isEmpty()) {
						String filename = file.getOriginalFilename();
					    String extension = filename.substring(filename.lastIndexOf(".") + 1);	
					    String pureFilename = filename.substring(0, filename.lastIndexOf("."));
						String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
						String folderName = club.get().getClubSeq() + "_clubSeq";	
						String fullPath = CustomConfig.strFileUploadPath + "/" + folderName + "/"+pureFilename+"_" + timestamp + "." + extension;
						File tmpFile = new File(fullPath);
						tmpFile.getParentFile().mkdir();
						file.transferTo(tmpFile);
						fileOriginNameArray.add(filename);
						filePathArray.add(fullPath);						
						log.info("1:N메시지전송 filename : {}", filename);
					}					
			   }
			   log.info("1:N메시지전송 fileOriginNameArray.size() : {}", fileOriginNameArray.size());
				List<String> receiverMemberIdArray = personalMessageDto.getReceiverMemberId();
				for(String item : receiverMemberIdArray) {
					String json = clsJsonUtil.toString(
							PushDto.builder()
							.title(personalMessageDto.getTitle())
							.body(personalMessageDto.getMessage())	
							.clubName(club.get().getClubName())
							.pushType(pushType.get().getTypeCode())
							.deviceId("")
							.clubSeq(personalMessageDto.getClubSeq())
							.publicKey("")
							.clubInvitationSeq(null)
							.podUrl("")
							.build());
					PushLog pushLog = PushLog.builder()
							.pushType(pushType.get())
							.club(club.get())
							.clubNoticeSeq(null)
							.senderMemberId(club.get().getMemberDid().getMember().getMemberId())
							.receiverMemberId(item)
							.title(personalMessageDto.getTitle())
							.message(personalMessageDto.getMessage())
							.linkedUrl("")
							.extraMessage("")
							.confirmFlag(false)
							.readFlag(false)
							.build();					
					PushLog savedPushLog = pushService.sendPushReturn(json,pushLog);	
							
					if (files.length > 0) {					
						PersonalMessage personalMessage = PersonalMessage.builder()
								.club(club.get())
								.pushLog(savedPushLog)
								.title(personalMessageDto.getTitle())
								.message(personalMessageDto.getMessage())
								.confirmFlag(true)
								.filePath(filePathArray.toString())
								.originalFileName(fileOriginNameArray.toString())
								.receiverMemberId(item)
								.build();
						personalMessageRepository.save(personalMessage);
					}else{
						PersonalMessage personalMessage = PersonalMessage.builder()
								.club(club.get())
								.pushLog(savedPushLog)
								.title(personalMessageDto.getTitle())
								.message(personalMessageDto.getMessage())
								.confirmFlag(true)
								.filePath("")
								.originalFileName("")
								.receiverMemberId(item)
								.build();
						personalMessageRepository.save(personalMessage);
					}			
				}
								
			}	
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}
	/*
	@Override
	public boolean personalMessageCreate(MultipartFile file, PersonalMessageDto personalMessageDto) {		
		log.info("--------------------------------------------");
		log.info("1:1메시지전송");
		log.info("--------------------------------------------");		
		Optional<Club> club = clubRepository.findByClubSeq(personalMessageDto.getClubSeq());
		Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(5L);
		JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();	
		try {			
			if (club.isPresent()) {				
				String filename = file.getOriginalFilename();
			    String extension = filename.substring(filename.lastIndexOf(".") + 1);	
				String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
				String folderName = club.get().getClubSeq() + "_clubSeq";	
				String fullPath = CustomConfig.strFileUploadPath + "/" + folderName + "/message_" + timestamp + "." + extension;
				log.info("--------------------------------------------fullPath " + fullPath);
				if (!file.isEmpty()) {
					File tmpFile = new File(fullPath);
					tmpFile.getParentFile().mkdir();
					file.transferTo(tmpFile);
				}
				
				List<String> receiverMemberIdArray = personalMessageDto.getReceiverMemberId();
				for(String item : receiverMemberIdArray) {
					String json = clsJsonUtil.toString(
							PushDto.builder()
							.title(personalMessageDto.getTitle())
							.body(personalMessageDto.getMessage())	
							.clubName(club.get().getClubName())
							.pushType(pushType.get().getTypeCode())
							.deviceId("")
							.clubSeq(personalMessageDto.getClubSeq())
							.publicKey("")
							.clubInvitationSeq(null)
							.podUrl("")
							.build());
					PushLog pushLog = PushLog.builder()
							.pushType(pushType.get())
							.club(club.get())
							.clubNoticeSeq(null)
							.senderMemberId(club.get().getMemberDid().getMember().getMemberId())
							.receiverMemberId(item)
							.title(personalMessageDto.getTitle())
							.message(personalMessageDto.getMessage())
							.linkedUrl("")
							.extraMessage("")
							.confirmFlag(false)
							.readFlag(false)
							.build();					
					PushLog savedPushLog = pushService.sendPushReturn(json,pushLog);	
							
					if (!file.isEmpty()) {					
						PersonalMessage personalMessage = PersonalMessage.builder()
								.club(club.get())
								.pushLog(savedPushLog)
								.title(personalMessageDto.getTitle())
								.message(personalMessageDto.getMessage())
								.confirmFlag(true)
								.filePath(fullPath)
								.originalFileName(filename)
								.receiverMemberId(item)
								.build();
						personalMessageRepository.save(personalMessage);
					}else{
						PersonalMessage personalMessage = PersonalMessage.builder()
								.club(club.get())
								.pushLog(savedPushLog)
								.title(personalMessageDto.getTitle())
								.message(personalMessageDto.getMessage())
								.confirmFlag(true)
								.filePath("")
								.originalFileName("")
								.receiverMemberId(item)
								.build();
						personalMessageRepository.save(personalMessage);
					}			
				}
								
			}	
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}
*/
	@Override
	public Page<PersonalMessageDto> personalMessageList(Long clubSeq, String receiverMemberId, Pageable pageable) {
		
		Page<PersonalMessage> personalMessage = personalMessageRepository.findByClub_ClubSeqAndReceiverMemberId(clubSeq, receiverMemberId, pageable);
		Page<PersonalMessageDto> personalMessageDto = personalMessage.map(m -> PersonalMessageDto.builder()
				.personalMessageSeq(m.getPersonalMessageSeq())
				.title(m.getTitle())
				.message(m.getMessage())
				.filePath(m.getFilePath())
				.fileUrl(CustomConfig.strDefaultImageUrl + clubSeq +"_clubSeq/")
				.originalFileName(m.getOriginalFileName())
				.updated(m.getUpdated())
				.confirmFlag(m.isConfirmFlag())
				.readFlag(m.isReadFlag())
				.pushLog(m.getPushLog())
				.build()
				);
		
		return personalMessageDto;
	}
	
	private String getClubAndFileName(String path) {
		if (path == null || "".equals(path)) {
			return "";
		} else {
			String[] nameArray = path.split("/");
			if (path.split("/").length < 2)
				return "";
			int imageName = nameArray.length - 1;
			int clubName = nameArray.length - 2;
			log.info("imageName {}", imageName);
			log.info("clubName {}", clubName);
			return CustomConfig.strDefaultImageUrl + nameArray[clubName] + "/" + nameArray[imageName];
		}
	}

//	@Override
//	public boolean sendMessageAdmin() {
//		Map<String, List<String>> userSessionsMap = sessionManager.getAllUserSessions();
//    	log.info("userSessionsMap : " + userSessionsMap.size());
//		for (Map.Entry<String, List<String>> entry : userSessionsMap.entrySet()) {
//    	    String userId = entry.getKey();
//    	    List<String> sessionList = entry.getValue();
//
//    	    log.info("User ID: " + userId);
//    	    log.info("Sessions: " + sessionList);
//    	    
//    	    String destination = "/queue/private-message/" + userId;
//            log.info("sendMessageToUser destination : {}",destination);
//            messagingTemplate.convertAndSend(destination, "memberId="+ userId );
//    	    // 각 세션에 대한 추가 로직도 수행 가능
//    	    for (String sessionId : sessionList) {
//    	        // 세션에 대한 추가 로직
//    	    	log.info("Session ID: " + sessionId);
//    	    }
//    	}
//		return true;
//	}
}
