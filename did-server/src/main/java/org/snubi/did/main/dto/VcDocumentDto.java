package org.snubi.did.main.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;


@Data
@JsonPropertyOrder({
    "@context",
    "id",
    "type",
    "issuer",
    "issuanceDate",
    "expirationDate",
    "credentialSubject",
    "proof"
})
public class VcDocumentDto {
	
	@JsonProperty("@context")
	List<String> context;
	
	 String id;

     List<String> type;

     String issuer;

    // VC 문서애만 있고 DB에는 없음
    // String name;

    // VC 문서애만 있고 DB에는 없음
    // String description;

     String issuanceDate;

    // VC 문서애만 있고 DB에는 없음
     String expirationDate;

     CredentialSubject credentialSubject;

     Proof proof;

     
     @Data
     @JsonPropertyOrder({
    	    "id",
    	    "email",
    	    "memberName",
    	    "mobileNumber"
    	})
     public static class CredentialSubject{       
         private String id;
         private String email;
         private String memberName;
         private String mobileNumber; 
     }
   
   
    @Data
    @JsonPropertyOrder({
	    "type",
	    "created",
	    "verificationMethod",
	    "proofPurpose",
	    "proofValue"
	})
    public static class Proof{
        // proofType
        private String type;

        private String created;

        private String verificationMethod;

        private String proofPurpose;

        private String proofValue;
    }

}
