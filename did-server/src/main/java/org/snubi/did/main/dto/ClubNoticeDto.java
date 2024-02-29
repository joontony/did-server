package org.snubi.did.main.dto;

import java.util.List;
import org.snubi.did.main.entity.ClubNoticeComment;
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
public class ClubNoticeDto {
	
	private String memberId;
	private Long clubNoticeSeq;
	private Long clubSeq;
	private Long clubCategorySeq;
	private String noticeMessage;
	private String noticeTitle;
	private String noticeUrl;
	private String created;
	private String updated;
	private boolean valid;
	

}
