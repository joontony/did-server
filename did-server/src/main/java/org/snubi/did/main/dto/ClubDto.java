package org.snubi.did.main.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class ClubDto {
	private Long clubSeq;
	private String did;
	private Long memberDidSeq;
	private Long clubCatogorySeq;
	private String clubName;
	private String clubPublicKey;
	private String clubUrl;
	private String description;
	private String operateTime;
	private String location;
	private String phone;
	private String startDate;
	private String endDate;	
	private Long bankSeq;
	private int feeValue;
	private String bankAccount;
	private String bankOwnerName;
	private Long agentClubSeq;
	private boolean flag;
	private String selectDate;
	private String memberPassword;
	private List<Boolean> fileExists;	
	private List<Long> clubCategoryItemSeq;
	private String memberGrade;
	private String kakaoCodeLink;
	private Long agentClubWaitingSeq;
	// 신규추가 
	private List<String> imageTexts;
	private List<String> questionnaire;
	
	private Long clubMemberSeq;
	//private Map<String, List<Map<String, Object>>> extraData;
	private Object extraData;

}
