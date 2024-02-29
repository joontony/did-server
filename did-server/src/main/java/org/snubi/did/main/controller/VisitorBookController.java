package org.snubi.did.main.controller;

import org.snubi.did.main.common.CustomResponseEntity;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.dto.ClubVisitorBookDto;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.service.VisitorBookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VisitorBookController extends WithAuthController {
	
	private final VisitorBookService visitorBookService;
	
	@RequestMapping(value = "/club/visitor/book/write", method = RequestMethod.POST)
	public ResponseEntity<?> clubVisitorBookCreate(@RequestBody ClubVisitorBookDto clubVisitorBookDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(visitorBookService.clubVisitorBookCreate(clubVisitorBookDto),"");	
		}	
	}
	
	@RequestMapping(value = "/club/visitor/book/list/{clubSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubVisitorBookList(@PathVariable Long clubSeq,Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(visitorBookService.clubVisitorBookList(getClaims().getId(), clubSeq, pageable),"");	
		}	
	}
	
	
	@RequestMapping(value = "/club/visitor/book/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubVisitorBookUpdate(@RequestBody ClubVisitorBookDto clubVisitorBookDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			boolean isOwn = isOwnVisitorBook(getClaims().getId(),clubVisitorBookDto.getClubVisitorBookSeq());
			if(isOwn) {
				return CustomResponseEntity.succResponse(visitorBookService.clubVisitorBookUpdate(clubVisitorBookDto),"");					
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}
			
		}	
	}
	
	@RequestMapping(value = "/club/visitor/book/delete/{clubSeq}/{clubVisitorBookSeq}", method = RequestMethod.DELETE)
	public ResponseEntity<?> clubVisitorBookDelete(@PathVariable Long clubSeq,@PathVariable Long clubVisitorBookSeq) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {					
			boolean isOwn = isOwnVisitorBook(getClaims().getId(),clubVisitorBookSeq);	
			boolean issuer = isRoleIssuer(getClaims().getId(),clubSeq);
			if(isOwn || issuer) {
				return CustomResponseEntity.succResponse(visitorBookService.clubVisitorBookDelete(clubSeq,clubVisitorBookSeq),"");					
			}else {
				throw new CustomException(ErrorCode.FORBIDDEN);	
			}
			
		}	
	}
	
	@RequestMapping(value = "/club/visitor/book/like/{clubVisitorBookSeq}/{isLikeFlag}", method = RequestMethod.PUT)
	public ResponseEntity<?> clubVisitorBookLike(@PathVariable Long clubVisitorBookSeq,@PathVariable boolean isLikeFlag) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(visitorBookService.clubVisitorBookLike(setMemberDto(), clubVisitorBookSeq,isLikeFlag),"");	
		}	
	}
}
