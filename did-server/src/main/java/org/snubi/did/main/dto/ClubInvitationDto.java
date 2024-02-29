package org.snubi.did.main.dto;

import java.time.LocalDateTime;
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
public class ClubInvitationDto {
	private String email;
	private String memberName;	
	private String memberGrade;
	private String mobileNumber;
	private String dataFromIssuer;
	private boolean confirmFlag;
	private boolean valid;
	private LocalDateTime expiredDate;	
	private String extraData;
	private String localName;
}
