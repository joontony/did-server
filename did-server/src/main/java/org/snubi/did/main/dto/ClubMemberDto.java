package org.snubi.did.main.dto;

import java.time.LocalDateTime;
import java.util.Date;
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
public class ClubMemberDto {
	private Long clubMemberSeq;
	private String email;
	private String memberName;	
	private String memberGrade;
	private String mobileNumber;
	//private String dataFromIssuer;
	private String memberDataJson;
	//private boolean confirmFlag;
	private boolean valid;
	private Date expiredDate;	
	private LocalDateTime created;
	private LocalDateTime updated;
	private String did;
	private String profileImage;
	private String extraData;
	private String localName;
	private boolean activeSocketFlag;
}
