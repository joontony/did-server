package org.snubi.did.main.service;

import org.snubi.did.main.dto.ClubNoticeCommentDto;
import org.snubi.did.main.dto.ClubNoticeDto;
import org.snubi.did.main.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface NoticeService {
	
	boolean clubNoticeCreate(ClubNoticeDto clubNoticeDto);
	boolean clubNoticeUpdate(ClubNoticeDto clubNoticeDto);
	boolean clubNoticeUDelete(Long clubSeq,Long clubNoticeSeq);
	Page<ClubNoticeDto> clubNoticeList(Long clubSeq, Pageable pageable);	
	
//	boolean clubNoticeFileCreate(MultipartFile imagePath,ClubNoticeDto clubNoticeDto);	
//	boolean clubNoticeFileUpdate(MultipartFile imagePath,ClubNoticeDto clubNoticeDto);	
//	boolean clubNoticeDelete(Long clubSeq,Long clubNoticeSeq);
//	Page<ClubNoticeDto> clubNoticeList(Long clubSeq, Pageable pageable);		
//	boolean clubNoticeLike(MemberDto memberDto,Long clubNoticeSeq, boolean isLikeFlag);
//	
//	boolean clubNoticeCommentCreate(ClubNoticeCommentDto clubNoticeCommentDto);
//	boolean clubNoticeCommentUpdate(ClubNoticeCommentDto clubNoticeCommentDto);
//	boolean clubNoticeCommentDelete(Long clubSeq,Long clubNoticeCommentSeq);
}
