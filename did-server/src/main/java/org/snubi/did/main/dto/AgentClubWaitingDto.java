package org.snubi.did.main.dto;

import java.time.LocalDateTime;
import org.snubi.did.main.entity.AgentClub;
import org.snubi.did.main.entity.MemberDid;
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
public class AgentClubWaitingDto {
	private Long agentClubWaitingSeq;
	private AgentClub agentClub;
	private MemberDid memberDid;
	private String memberName;
	private boolean flag;
	private LocalDateTime created;
	private LocalDateTime updated;
	private Long totalWaitingCount;
	
	private String invitationMobileNumber;
	private String invitationName;
	private String invitationLocalName;
	private boolean invitationEmptyLocalNameValid;
	private boolean invitationClubValid;
	
	private String mobileNumber;
	private String memberGrade;
	private int completeCount;
	private int waitingCount;
	private int displayWaitingNum;
	private String questionnaire;
	private boolean waitingFlag;
	private String localName;
	private String extraData;
}
