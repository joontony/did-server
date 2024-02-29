package org.snubi.did.main.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseNiceResultDto {
	String token_version_id;
	String enc_data;
	String integrity_value;
}
