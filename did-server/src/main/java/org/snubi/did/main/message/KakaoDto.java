package org.snubi.did.main.message;


import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoDto {
	private List<Messages> messages;
	
	@Data
	@Builder
	public static class Messages {
		private String to;
        private KakaoOptions kakaoOptions;
	}
	
	@Data
	@Builder
	public static class KakaoOptions {
		private String pfId;
        private String templateId;
        private Map<String, String> variables;
	}
}
