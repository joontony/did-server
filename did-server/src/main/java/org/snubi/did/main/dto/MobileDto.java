package org.snubi.did.main.dto;

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
public class MobileDto {
	private Long clubSeq;
	private String mobileNumber;
	private String issuerDid;
	private List<String> questionnaire;
	private boolean waitingFlag;
}
