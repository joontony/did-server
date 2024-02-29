package org.snubi.did.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class DidDto {	
	private String did;
	private String userId;
	private String userAccount;
	private String userPassword;
	private String authType;
	private String authPublicKey;
	private String email;
	private String memberName;
	private String mobileNumber;
}
