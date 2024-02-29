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
public class ClubNoticeWithCommentDto {
	private String memberId;
	private String memberName;
	private Long clubNoticeSeq;
	private Long clubSeq;
	private Long clubCategorySeq;
	private String noticeMessage;
	private String noticeTitle;
	private String noticeUrl;
	private String created;
	private String updated;
	private boolean valid;
	private boolean fileExists;
	private String imagePath;
	private List<ClubNoticeCommentDto> clubNoticeCommentDto;
	private boolean likeFlag;
	private Integer likeTotalCount;
	private Integer clubNoticeCommentCount;
	private List<String> likeUserName;
}
