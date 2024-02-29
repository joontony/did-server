package org.snubi.did.main.dto;

import java.time.LocalDateTime;
import org.snubi.did.main.entity.AgentClub;
import org.snubi.did.main.entity.Member;
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
public class AgentClubFeeDto {
	private Long agentClubFeeSeq;
	private AgentClub agentClub;
	private MemberDid memberDid;
	private boolean flag;
	private LocalDateTime created;
	private LocalDateTime updated;
	private Long totalFeeCount;
	private String memberName;
	private String mobileNubmer;
	private String memberGrade;
}
