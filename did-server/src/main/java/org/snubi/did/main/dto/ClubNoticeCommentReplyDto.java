package org.snubi.did.main.dto;

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
public class ClubNoticeCommentReplyDto {
	private String memberId;
	private String memberName;
	private Long clubSeq;
	private Long clubNoticeCommentSeq;
	private Long clubNoticeCommentReplySeq;
	private String noticeCommentReply;
	private String created;
	private String updated;
	private boolean valid;

}
