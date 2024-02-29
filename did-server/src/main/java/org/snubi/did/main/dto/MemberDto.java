package org.snubi.did.main.dto;

import java.util.Date;
import java.util.List;

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
public class MemberDto {
	private String memberId;
	private String memberPassword;
	private String email;
	private String memberName;
	private String mobileNumber;
	private String mobileAuthNumber;
	private boolean mobileAuthFlag;
	private String deviceId;	
	private String memberPublicKey;	
	private String authType;
	private String profileFilePath;
	private String cardFilePath;
	private String birth;
	private List<String> MemberDid;
	
}
