package org.snubi.did.main.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NiceDto {

	DataHeader dataHeader;
	DataBody dataBody;
	
	@Data
	@Builder
	public static class DataHeader {		
		String CNTY_CD;
	}
	
	@Data
	@Builder
	public static class DataBody {		
		String req_dtim;
		String req_no;
		String enc_mode;
	}
}
