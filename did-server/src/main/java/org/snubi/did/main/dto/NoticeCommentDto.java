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
public class NoticeCommentDto {
	private String issuerId;
	private String memberId;
	private String memberName;
	private Long clubNoticeSeq;
	private Long clubNoticeCommentSeq;
	private String noticeComment;
	private String created;
	private String updated;
	private boolean valid;
	private List<ClubNoticeCommentReplyDto> clubNoticeCommentReplyDto;
}
