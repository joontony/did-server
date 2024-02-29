package org.snubi.did.main.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class CustomResponse {
	private String code		;
    private Object data		;
    private String message	;
    private String token 	;
}
