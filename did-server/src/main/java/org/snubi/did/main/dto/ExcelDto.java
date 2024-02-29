package org.snubi.did.main.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class ExcelDto {
	private String did;
	private int clubId;
	private List<List<String>> excelData;
    private boolean push;
    private boolean sms;
    private boolean kakao;
    
}
