package org.snubi.did.main.dto;


import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor 
@AllArgsConstructor 
public class ResponseNiceDto {

	DataHeader dataHeader;
	DataBody dataBody;
	
	@Data
	@Builder
	@NoArgsConstructor 
	@AllArgsConstructor 
	public static class DataHeader {	
		@JsonProperty("GW_RSLT_CD")
		String GW_RSLT_CD;
		@JsonProperty("GW_RSLT_MSG")
		String GW_RSLT_MSG;
	}
	
	@Data
	@Builder
	@NoArgsConstructor 
	@AllArgsConstructor 
	public static class DataBody {		
		String rsp_cd;
		String result_cd;
		String site_code;
		String token_version_id;
		String token_val;
		float period;
	}
}
