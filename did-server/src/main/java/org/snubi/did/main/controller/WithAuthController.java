package org.snubi.did.main.controller;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.common.RoleType;
import org.snubi.did.main.config.CustomConfig;
import org.snubi.did.main.dto.ClubNoticeDto;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.entity.ClubMember;
import org.snubi.did.main.entity.ClubNotice;
import org.snubi.did.main.entity.ClubNoticeComment;
import org.snubi.did.main.entity.ClubVisitorBook;
import org.snubi.did.main.entity.MemberDid;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.repository.ClubMemberRepository;
import org.snubi.did.main.repository.ClubNoticeCommentRepository;
import org.snubi.did.main.repository.ClubNoticeRepository;
import org.snubi.did.main.repository.ClubVisitorBookRepository;
import org.snubi.did.main.repository.MemberDidRepository;
import org.snubi.lib.jwt.JTWClaimsUtil;
import org.snubi.lib.jwt.JWTClaims;
import org.snubi.lib.misc.Misc;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WithAuthController{
	
	
	@Autowired MemberDidRepository memberDidRepository;
	@Autowired ClubMemberRepository clubMemberRepository;
	@Autowired ClubVisitorBookRepository clubVisitorBookRepository;
	@Autowired ClubNoticeRepository clubNoticeRepository;
	@Autowired ClubNoticeCommentRepository clubNoticeCommentRepository;
	
	//@Autowired
	protected HttpServletRequest request;
	@Autowired
	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	protected JWTClaims clsJWTClaims = new JWTClaims();		
	
	protected boolean isOnlyOwn(String strId)  {
		try {
			JWTClaims clsJWTClaims = this.getClaims();
			return clsJWTClaims.getId().equals(strId);
		} catch(Exception Ex) {			
			log.error("## WithAuthController ## isOnlyOwn");			
		}
		return false;
	}
	
	protected String getToken() throws Exception {
		String token = "";
		try {
			token = request.getHeader("Authorization").replace(CustomConfig.strTokenPrefix, "");
		} catch(Exception Ex) {
			log.info("Exception {}", Ex.getMessage());
		}
		return token;
	}
	
	protected JWTClaims getClaims() throws Exception {
		try {
			String strHTTPHeader = request.getHeader("Authorization").replace(CustomConfig.strTokenPrefix, "");	
			//log.info("strHTTPHeader {}", strHTTPHeader);			
			String strKey = CustomConfig.strSecrete;			
			this.clsJWTClaims = (new JTWClaimsUtil()).getClaims(strHTTPHeader,strKey);	
			//log.info("this.clsJWTClaims.getId() {}", this.clsJWTClaims.getId());				
			if(Misc.isEmtyString(this.clsJWTClaims.getId()) == true) {
				throw new CustomException(ErrorCode.UNAUTHORIZED);	
			}
		} catch(Exception Ex) {
			log.info("Exception {}", Ex.getMessage());
			clsJWTClaims.setId("not-login");
		}
		return clsJWTClaims;
	}
	
	protected boolean isRoleIssuer(String memberId, Long clubSeq) {
		if(RoleType.ISSUER.name().equals( getRole(memberId,clubSeq) )) {
			return true;		
		}
		return false;		
	}
	
	protected boolean isRoleStaff(String memberId, Long clubSeq) {		
		if(RoleType.STAFF.name().equals( getRole(memberId,clubSeq) )) {
			return true;		
		}
		return false;		
	}
	// 여러개 일경우 ....
    private String getRole(String memberId, Long clubSeq) {
    	List<MemberDid> memberDid = memberDidRepository.findByMember_MemberId(memberId);
    	log.info("memberDid {}", memberDid.toString());
		if (memberDid.size() > 0) {	
			log.info("clubNoticeDto.getClubSeq() {}", clubSeq);
			log.info("memberDid.get(0).getMemberDidSeq() {}", memberDid.get(0).getMemberDidSeq());			
			Optional<ClubMember> clubMember = 	clubMemberRepository
					.findByClub_ClubSeqAndMemberDid_MemberDidSeq(clubSeq, memberDid.get(0).getMemberDidSeq());	
			if(clubMember.isPresent()) {				
				log.info("clubMember {}", clubMember.toString());				
				log.info("getRoleType {}", clubMember.get().getClubRole().getRoleType());				
				return clubMember.get().getClubRole().getRoleType();
			}
			
		 }
		return "";
    }
    
    protected boolean isOwnVisitorBook(String memberId,Long clubVisitorBookSeq) {    
    	Optional<ClubVisitorBook> clubVisitorBook = clubVisitorBookRepository.findByClubVisitorBookSeqAndCreator(clubVisitorBookSeq,memberId);
		if(clubVisitorBook.isPresent()) {
			return true;
		}
		return false;
    }
    protected boolean isOwnNotice(String memberId,Long clubNoticeSeq) {    
    	Optional<ClubNotice> clubNotice = clubNoticeRepository.findByClubNoticeSeqAndCreator(clubNoticeSeq,memberId);
		if(clubNotice.isPresent()) {
			return true;
		}
		return false;
    }
    protected boolean isOwnNoticeComment(String memberId,Long clubNoticeCommentSeq) {    
    	Optional<ClubNoticeComment> clubNoticeComment = clubNoticeCommentRepository.findByClubNoticeCommentSeqAndCreator(clubNoticeCommentSeq,memberId);
		if(clubNoticeComment.isPresent()) {
			return true;
		}
		return false;
    }
	
    protected MemberDto setMemberDto() throws Exception {			
		return MemberDto
				.builder()
				.memberId(getClaims().getId())
				.memberName(getClaims().getName())
				.build();			
	}
    
}
