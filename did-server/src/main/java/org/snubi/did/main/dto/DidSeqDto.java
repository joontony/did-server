package org.snubi.did.main.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class DidSeqDto {
	private String did;
	private String vcSignatureSeq;
    private VcDocumentDto vcDocument;
}
