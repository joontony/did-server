package org.snubi.did.main.service;

import org.snubi.did.main.dto.PersonalMessageDto;
import org.snubi.did.main.rabbitmq.ChatMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface PersonalMessageService {
	boolean personalMessageCreate(MultipartFile[] files,PersonalMessageDto personalMessageDto);
	boolean socketMessageCreate(MultipartFile[] files,ChatMessageDto chatMessageDto);
	Page<PersonalMessageDto> personalMessageList(Long clubSeq, String receiverMemberId,Pageable pageable);
	void sendMessageToRabbit(ChatMessageDto chatMessageDto);
}
