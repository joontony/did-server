package org.snubi.did.main.response;

import lombok.Data;

public class DataObject {	
	
	@Data
	public static class Defaults {
		private String mobileAuthNumber;
		private String memberId;
		private String mobileNumber;
	}
}
