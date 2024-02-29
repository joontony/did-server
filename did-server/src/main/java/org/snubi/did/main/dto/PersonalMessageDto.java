package org.snubi.did.main.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.snubi.did.main.entity.Club;
import org.snubi.did.main.entity.PushLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class PersonalMessageDto {
	private Long personalMessageSeq;
	private Long clubSeq;
	private Club club;
	private PushLog pushLog;
	private String title;
	private String message;
	private String filePath;
	private String fileUrl;
	private boolean confirmFlag;
	private boolean readFlag;
	private String senderMemberId;
	private List<String> receiverMemberId;	
	private String originalFileName;
	private LocalDateTime updated;
}
