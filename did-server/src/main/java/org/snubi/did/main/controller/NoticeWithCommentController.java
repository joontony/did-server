package org.snubi.did.main.controller;

import org.snubi.did.main.service.NoticeWithCommentService;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.snubi.did.main.common.CustomResponseEntity;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.dto.ClubNoticeCommentDto;
import org.snubi.did.main.dto.ClubNoticeCommentReplyDto;
import org.snubi.did.main.dto.ClubNoticeWithCommentDto;
import org.snubi.did.main.exception.CustomException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
public class NoticeWithCommentController extends WithAuthController {
	
	
	private final NoticeWithCommentService noticeWithCommentService;	
	
	//공지쓰기 
	@RequestMapping(value = "/club/notice/withfile/create", method = RequestMethod.POST)
	public ResponseEntity<?> clubNoticeFileCreate(@RequestPart("imagePath") MultipartFile imagePath,@RequestPart ClubNoticeWithCommentDto clubNoticeWithCommentDto) throws Exception {			
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			boolean issuer = isRoleIssuer(getClaims().getId(),clubNoticeWithCommentDto.getClubSeq());
			boolean staff  = isRoleStaff(getClaims().getId(),clubNoticeWithCommentDto.getClubSeq());			
			if(issuer || staff) {
				return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeFileCreate(imagePath, clubNoticeWithCommentDto),"");
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}				
		}	
	}		
	//공지수정 
	@RequestMapping(value = "/club/notice/withfile/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubNoticeFileUpdate(@RequestPart("imagePath") MultipartFile imagePath,@RequestPart ClubNoticeWithCommentDto clubNoticeWithCommentDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {		
			boolean isOwn = isOwnNotice(getClaims().getId(),clubNoticeWithCommentDto.getClubNoticeSeq());
			if(isOwn) {
				return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeFileUpdate(imagePath, clubNoticeWithCommentDto),"");					
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}			
		}	
	}		
	//공지리스트 
	@RequestMapping(value = "/club/notice/withfile/list/{clubSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubNoticeList(@PathVariable Long clubSeq,Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeList(getClaims().getId(), clubSeq, pageable),"");	
		}	
	}
	//공지삭제 
	@RequestMapping(value = "/club/notice/withfile/delete/{clubSeq}/{clubNoticeSeq}", method = RequestMethod.DELETE)
	public ResponseEntity<?> clubNoticeDelete(@PathVariable Long clubSeq,@PathVariable Long clubNoticeSeq) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {					
			boolean isOwn = isOwnNotice(getClaims().getId(),clubNoticeSeq);
			if(isOwn) {
				return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeDelete(clubSeq,clubNoticeSeq),"");					
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}			
		}	
	}
	//좋아요변경  
	@RequestMapping(value = "/club/notice/withfile/like/{clubNoticeSeq}/{isLikeFlag}", method = RequestMethod.PUT)
	public ResponseEntity<?> clubVisitorBookLike(@PathVariable Long clubNoticeSeq,@PathVariable boolean isLikeFlag) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeLike(setMemberDto(), clubNoticeSeq,isLikeFlag),"");	
		}	
	}
	//댓글쓰기 
	@RequestMapping(value = "/club/notice/comment/create", method = RequestMethod.POST)
	public ResponseEntity<?> clubNoticeCommentCreate(@RequestBody ClubNoticeCommentDto clubNoticeCommentDto) throws Exception {			
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeCommentCreate(clubNoticeCommentDto),"");	
		}	
	}		
	//댓글수정 
	@RequestMapping(value = "/club/notice/comment/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubNoticeCommentUpdate(@RequestBody ClubNoticeCommentDto clubNoticeCommentDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {					
			boolean isOwn = isOwnNoticeComment(getClaims().getId(),clubNoticeCommentDto.getClubNoticeCommentSeq());
			if(isOwn) {
				return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeCommentUpdate(clubNoticeCommentDto),"");					
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}			
		}	
	}			
	//댓글삭제 
	@RequestMapping(value = "/club/notice/comment/delete/{clubSeq}/{clubNoticeCommentSeq}", method = RequestMethod.DELETE)
	public ResponseEntity<?> clubNoticeCommentDelete(@PathVariable Long clubSeq, @PathVariable Long clubNoticeCommentSeq) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {		
			
			boolean issuer = isRoleIssuer(getClaims().getId(),clubSeq);
			boolean staff  = isRoleStaff(getClaims().getId(),clubSeq);
			boolean isOwn  = isOwnNoticeComment(getClaims().getId(),clubNoticeCommentSeq);
			if(issuer || staff || isOwn) {
				return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeCommentDelete(clubSeq,clubNoticeCommentSeq),"");					
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}			
		}	
	}
	//댓글리스트(대댓글포함) 
	@RequestMapping(value = "/club/notice/comment/list/{clubNoticeSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubNoticeCommentList(@PathVariable Long clubNoticeSeq,Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeCommentList(clubNoticeSeq, pageable),"");	
		}	
	}
	//대댓글쓰기(운영자)
	@RequestMapping(value = "/club/notice/comment/reply/create", method = RequestMethod.POST)
	public ResponseEntity<?> clubNoticeCommentReplyCreate(@RequestBody ClubNoticeCommentReplyDto clubNoticeCommentReplyDto) throws Exception {			
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			boolean issuer = isRoleIssuer(getClaims().getId(),clubNoticeCommentReplyDto.getClubSeq());
			if(issuer) {
				return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeCommentReplyCreate(clubNoticeCommentReplyDto),"");
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}
				
		}	
	}	
	//댓글수정 
	@RequestMapping(value = "/club/notice/comment/reply/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubNoticeCommentReplyUpdate(@RequestBody ClubNoticeCommentReplyDto clubNoticeCommentReplyDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {					
			boolean issuer = isRoleIssuer(getClaims().getId(),clubNoticeCommentReplyDto.getClubSeq());
			if(issuer) {
				return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeCommentReplyUpdate(clubNoticeCommentReplyDto),"");					
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}			
		}	
	}			
	//댓글삭제 
	@RequestMapping(value = "/club/notice/comment/reply/delete/{clubSeq}/{clubNoticeCommentReplySeq}", method = RequestMethod.DELETE)
	public ResponseEntity<?> clubNoticeCommentReplyDelete(@PathVariable Long clubSeq, @PathVariable Long clubNoticeCommentReplySeq) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {		
			
			boolean issuer = isRoleIssuer(getClaims().getId(),clubSeq);
			if(issuer) {
				return CustomResponseEntity.succResponse(noticeWithCommentService.clubNoticeCommentReplyDelete(clubSeq,clubNoticeCommentReplySeq),"");					
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}			
		}	
	}	
}
