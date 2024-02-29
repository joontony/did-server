package org.snubi.did.main.dto;

import java.time.LocalDateTime;
import java.util.Date;

import org.snubi.did.main.entity.AgentClub;
import org.snubi.did.main.entity.ClubCategory;
import org.snubi.did.main.entity.ClubFee;
import org.snubi.did.main.entity.ClubRole;
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
public class ClubListDto {
	private Long clubSeq;
	private MemberDid memberDid;
	private ClubCategory clubCategory;
	private String clubName;
	private String clubPublicKey;
	private String description;
	private String operateTime;	
	private String location;
	private String phone;
	private LocalDateTime startDate;
	private LocalDateTime endDate;	
	private String clubUrl;
	private String podUrl;
	private boolean valid;
	private String imagePath1;
	private String imagePath2;
	private String imagePath3;
	private String imagePath4;
	private String imagePath5;
	private String imagePathCard;
	private String imageText1;
	private String imageText2;
	private String imageText3;
	private String imageText4;
	private String imageText5;
	private LocalDateTime created;
	private ClubFee clubFee;
	private MemberDto issuer;
	private AgentClub agentClub;
	private ClubRole clubRole;
	private String bankName;
	private Long bankSeq;
	private boolean feeFlag;
	private int agentSeq;
	private String extraData;
	private String localName;
}
