package org.snubi.did.main.message;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PushDto {
	Notification notification = new Notification();
	Data data = new Data();	
	List<String> registration_ids = new ArrayList<String>(); // 변수 이름 바꾸면 에러남 
	
	
	@Builder
	public PushDto(String pushType, String title,String body,Long clubSeq, String deviceId,String podUrl,String publicKey,String credential,Long vcSignatureSeq, 
			Long clubInvitationSeq , String clubName, 
			Long chatRoomSeq,
			String sessionId) {			
		notification.title = title;
		notification.body = body;	
		data.pushType = pushType;
		data.clubSeq = clubSeq;
		data.podUrl = podUrl;
		data.publicKey = publicKey;
		data.credential = credential;
		data.vcSignatureSeq = vcSignatureSeq;
		data.clubInvitationSeq = clubInvitationSeq;
		data.title = title;
		data.body = body;
		data.clubName = clubName;
		data.chatRoomSeq = chatRoomSeq;
		data.sessionId = sessionId;
		registration_ids.add(deviceId);		
	}	
	//1 "{{club_name}}공지사항을 확인해보세요."
	//2 "{{club_name}}이 생성되었습니다. 확인해보세요."
	//3 "{{club_name}}에서 초대장이 왔습니다. 확인해보세요."
	@Getter
	@Setter
	@ToString
	public class Notification {
		String title = new String();  		
		String body = new String();
	}
	
	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	public class Data {		
		String pushType = new String();
		Long clubSeq;  						//공지사항용(클럽멤버전체) + 클럽생성 		
		String podUrl = new String(); 		//클럽생성 
		String publicKey = new String();	//클럽생성 , 클럽초대 전체멤버 
		String credential = new String();	//클럽생성 
		Long vcSignatureSeq;				//클럽생성 클럽생성자 한명에게 vcSignature		
		Long clubInvitationSeq;
		String title = new String();
		String body = new String();
		String clubName = new String();
		Long chatRoomSeq;
	    String sessionId;;

	}	
}
