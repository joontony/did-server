package org.snubi.did.main.controller;

import org.snubi.did.main.common.CustomResponseEntity;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.dto.ClubNoticeCommentDto;
import org.snubi.did.main.dto.ClubNoticeDto;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.service.NoticeService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeController extends WithAuthController {
	
	private final NoticeService noticeService;	
	
	@RequestMapping(value = "/club/notice/write", method = RequestMethod.POST)
	public ResponseEntity<?> clubNoticeCreate(@RequestBody ClubNoticeDto clubNoticeDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(noticeService.clubNoticeCreate(clubNoticeDto),"");	
		}	
	}
	
	@RequestMapping(value = "/club/notice/list/{clubSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> daoMatchContractList(@PathVariable Long clubSeq,Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(noticeService.clubNoticeList(clubSeq, pageable),"");	
		}	
	}
	
	
	@RequestMapping(value = "/club/notice/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubNoticeUpdate(@RequestBody ClubNoticeDto clubNoticeDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {		
			
			boolean issuer = isRoleIssuer(getClaims().getId(),clubNoticeDto.getClubSeq());
			boolean staff  = isRoleStaff(getClaims().getId(),clubNoticeDto.getClubSeq());
			if(issuer || staff) {
				return CustomResponseEntity.succResponse(noticeService.clubNoticeUpdate(clubNoticeDto),"");					
			}else {
				throw new CustomException(ErrorCode.UNAUTHORIZED);	
			}
			
		}	
	}
	
	@RequestMapping(value = "/club/notice/delete/{clubSeq}/{clubNoticeSeq}", method = RequestMethod.DELETE)
	public ResponseEntity<?> clubNoticeUDelete(@PathVariable Long clubSeq,@PathVariable Long clubNoticeSeq) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {		
			
			boolean issuer = isRoleIssuer(getClaims().getId(),clubSeq);
			boolean staff  = isRoleStaff(getClaims().getId(),clubSeq);
			if(issuer || staff) {
				return CustomResponseEntity.succResponse(noticeService.clubNoticeUDelete(clubSeq,clubNoticeSeq),"");					
			}else {
				throw new CustomException(ErrorCode.UNAUTHORIZED);	
			}
			
		}	
	}
	
	
	/*
	
	
	
	@RequestMapping(value = "/club/notice/write", method = RequestMethod.POST)
	public ResponseEntity<?> clubNoticeWrite(@RequestBody ClubNoticeDto clubNoticeDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(noticeService.clubNoticeWrite(clubNoticeDto),"");	
		}	
	}	
	
	@RequestMapping(value = "/club/notice/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubNoticeUpdate(@RequestBody ClubNoticeDto clubNoticeDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {					
			boolean issuer = isRoleIssuer(getClaims().getId(),clubNoticeDto.getClubSeq());
			boolean staff  = isRoleStaff(getClaims().getId(),clubNoticeDto.getClubSeq());
			if(issuer || staff) {
				return CustomResponseEntity.succResponse(noticeService.clubNoticeUpdate(clubNoticeDto),"");					
			}else {
				throw new CustomException(ErrorCode.UNAUTHORIZED);	
			}
			
		}	
	}
	
	@RequestMapping(value = "/club/notice/delete/{clubSeq}/{clubNoticeSeq}", method = RequestMethod.DELETE)
	public ResponseEntity<?> clubNoticeUDelete(@PathVariable Long clubSeq,@PathVariable Long clubNoticeSeq) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {		
			
			boolean issuer = isRoleIssuer(getClaims().getId(),clubSeq);
			boolean staff  = isRoleStaff(getClaims().getId(),clubSeq);
			if(issuer || staff) {
				return CustomResponseEntity.succResponse(noticeService.clubNoticeUDelete(clubSeq,clubNoticeSeq),"");					
			}else {
				throw new CustomException(ErrorCode.UNAUTHORIZED);	
			}
			
		}	
	}
	
	*/
	
}
