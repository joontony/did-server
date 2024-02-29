package org.snubi.did.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class UniversalDto {
	private String issuerDid;
	private int clubSeq;
	private String startDate;
	private String endDate;
	private boolean valid;
}
