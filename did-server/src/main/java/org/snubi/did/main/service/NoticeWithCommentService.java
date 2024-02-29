package org.snubi.did.main.service;

import org.springframework.stereotype.Service;
import org.snubi.did.main.dto.ClubNoticeCommentDto;
import org.snubi.did.main.dto.ClubNoticeCommentReplyDto;
import org.snubi.did.main.dto.ClubNoticeWithCommentDto;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.dto.NoticeCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface NoticeWithCommentService {
	boolean clubNoticeFileCreate(MultipartFile imagePath,ClubNoticeWithCommentDto clubNoticeWithCommentDto);	
	boolean clubNoticeFileUpdate(MultipartFile imagePath,ClubNoticeWithCommentDto clubNoticeWithCommentDto);	
	boolean clubNoticeDelete(Long clubSeq,Long clubNoticeSeq);		
	ClubNoticeWithCommentDto clubNoticeLike(MemberDto memberDto,Long clubNoticeSeq, boolean isLikeFlag);
	Page<ClubNoticeWithCommentDto> clubNoticeList(String memberId, Long clubSeq, Pageable pageable);

	boolean clubNoticeCommentCreate(ClubNoticeCommentDto clubNoticeCommentDto);
	NoticeCommentDto clubNoticeCommentUpdate(ClubNoticeCommentDto clubNoticeCommentDto);
	boolean clubNoticeCommentDelete(Long clubSeq,Long clubNoticeCommentSeq);
	Page<NoticeCommentDto> clubNoticeCommentList(Long clubNoticeSeq, Pageable pageable);	
	
	NoticeCommentDto clubNoticeCommentReplyCreate(ClubNoticeCommentReplyDto clubNoticeCommentReplyDto);
	NoticeCommentDto clubNoticeCommentReplyUpdate(ClubNoticeCommentReplyDto clubNoticeCommentReplyDto);
	boolean clubNoticeCommentReplyDelete(Long clubSeq,Long clubNoticeCommentReplySeq);
}
