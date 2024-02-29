package org.snubi.did.main.dto;

import java.util.List;
import lombok.Data;

@Data
public class KubeDto {
	String kind;
	Spec spec;	
	
	@Data
	public static class Spec {		
		String clusterIP;
		List<Ports> ports;
	}
	
	@Data
	public static class Ports{
		String protocol;
		int nodePort;
	}	
}

