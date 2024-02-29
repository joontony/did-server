package org.snubi.did.main.dto;

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
public class IssuerDto {
	private Long clubSeq;
	private String podUrl;
	private String publicKey;
	private String credential;
	private Long vcSignatureSeq;
	private boolean sms;
	private boolean push;
	private boolean kakao;
	private boolean onlySaveSms;
	private String mobileNumber;
}
