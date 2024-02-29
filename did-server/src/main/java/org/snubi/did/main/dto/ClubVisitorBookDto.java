package org.snubi.did.main.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class ClubVisitorBookDto {
	private String memberId;
	private String memberName;
	private Long clubVisitorBookSeq;
	private Long clubSeq;
	private String visitorMessage;
	private String created;
	private String updated;
	private boolean valid;
	private boolean likeFlag;
	private Integer likeTotalCount;
	private List<String> likeUserName;
}
