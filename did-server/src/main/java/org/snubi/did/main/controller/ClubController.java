package org.snubi.did.main.controller;

import java.util.Optional;
import org.snubi.did.main.common.CustomResponseEntity;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.dto.ClubDto;
import org.snubi.did.main.dto.EmailDto;
import org.snubi.did.main.dto.IssuerDto;
import org.snubi.did.main.dto.MobileDto;
import org.snubi.did.main.dto.PrivateKeyDto;
import org.snubi.did.main.dto.QrCodeDto;
import org.snubi.did.main.entity.AgentClub;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.repository.AgentClubRepository;
import org.snubi.did.main.service.ClubService;
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
public class ClubController extends WithAuthController {
	
	private final ClubService clubService;
	private final AgentClubRepository agentClubRepository;
	
	// 사용안함 
	@RequestMapping(value = "/club/auth/create", method = RequestMethod.POST)
	public ResponseEntity<?> memberMobileAuth(@RequestBody ClubDto clubDto) throws Exception {
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubCreate(clubDto,getToken()),"");	
		}
	}
	// 고정팟 클럽생성 	
	@RequestMapping(value = "/club/auth/create/universal", method = RequestMethod.POST)
	public ResponseEntity<?> clubCreateUniversal(@RequestBody ClubDto clubDto) throws Exception {
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubCreateUniversal(clubDto,getToken()),"");	
		}
	}	
	// 고정팟 클럽생성 	+ 파일포함  
	@RequestMapping(value = "/club/auth/create/universal/withfile", method = RequestMethod.POST)
	public ResponseEntity<?> clubCreateUniversalWithFile(
			@RequestPart("imagePathCard") MultipartFile imagePathCard,
			@RequestPart ClubDto clubDto
			) throws Exception {
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubCreateUniversalWithFile(imagePathCard, clubDto, getToken()),"");	
		}
	}
	
	// 클럽 에이전트 업데이트 	
	@RequestMapping(value = "/club/agent/update/{clubSeq}/{AgentSeq}", method = RequestMethod.PUT)
	public ResponseEntity<?> clubAgentUpdate(@PathVariable Long clubSeq,@PathVariable Long AgentSeq) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubAgentUpdate(clubSeq,AgentSeq),"");		
		}
	}
	// 클럽수정 	
	@RequestMapping(value = "/club/auth/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubUpdate(
			@RequestPart("imagePath1") MultipartFile imagePath1, 
			@RequestPart("imagePath2") MultipartFile imagePath2,
			@RequestPart("imagePath3") MultipartFile imagePath3,
			@RequestPart("imagePath4") MultipartFile imagePath4,
			@RequestPart("imagePath5") MultipartFile imagePath5,
			@RequestPart ClubDto clubDto) throws Exception {			
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubUpdate(imagePath1, imagePath2, imagePath3, imagePath4, imagePath5, clubDto, setMemberDto()),"");	
		}	
	}	
	// 클럽수정 배경만 	
	@RequestMapping(value = "/club/card/update/{clubSeq}/{isFile}", method = RequestMethod.PUT)
	public ResponseEntity<?> memberMyCardUpdate(@RequestPart("imagePath") MultipartFile imagePath,@PathVariable Long clubSeq,@PathVariable boolean isFile) throws Exception {			
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubCardUpdate(imagePath,clubSeq, isFile, setMemberDto()),"");	
		}	
	}

	// 나의클럽 회원 권한 변경 
	@RequestMapping(value = "/club/list/my/role/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubListMyRoleUpdate(@RequestBody ClubDto clubDto) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			
			boolean issuer = isRoleIssuer(getClaims().getId(),clubDto.getClubSeq());
			if(issuer) {
				return CustomResponseEntity.succResponse(clubService.clubListMyRoleUpdate(clubDto),"");					
			}else {
				throw new CustomException(ErrorCode.UNAUTHORIZED);	
			}
		}
	}
	
	// 나의클럽 생성완료 플래그  
	@RequestMapping(value = "/club/valid/update/{clubSeq}", method = RequestMethod.PUT)
	public ResponseEntity<?> clubValidUpdate(@PathVariable Long clubSeq) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubValidUpdate(clubSeq),"");		
		}
	}
	
	// 나의클럽 이메일 갱신 및 전송 V1  
	@RequestMapping(value = "/club/update/email/send", method = RequestMethod.PUT)
	public ResponseEntity<?> clubEmailUpdate(@RequestBody EmailDto emailDto) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.emailUpdateAndSend(emailDto,getClaims().getId()),"");		
		}
	}
	
	// 나의클럽 이메일 갱신 및 전송 V2  
	@RequestMapping(value = "/club/update/email/privatekey/send", method = RequestMethod.PUT)
	public ResponseEntity<?> clubPrivateUpdate(@RequestBody PrivateKeyDto privateKeyDto) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.privateUpdateAndSend(privateKeyDto,getClaims().getId()),"");		
		}
	}
	
	// 푸시실행 완료 플래그  
	@RequestMapping(value = "/club/push/update/{pushLogSeq}", method = RequestMethod.PUT)
	public ResponseEntity<?> clubPushLogUpdate(@PathVariable Long pushLogSeq) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubPushLogUpdate(pushLogSeq),"");		
		}
	}	
	//SMS 실행 완료 플래그  
	@RequestMapping(value = "/club/sms/update/{smsLogSeq}", method = RequestMethod.PUT)
	public ResponseEntity<?> clubSmsLogUpdate(@PathVariable Long smsLogSeq) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubSmsLogUpdate(smsLogSeq),"");		
		}
	}	
	
	// QrCode 인식완료 issuer서버로 부터 요청받고 푸시보낸다
	@RequestMapping(value = "/club/qrcode/complete", method = RequestMethod.POST)
	public ResponseEntity<?> qrCodeComplete(@RequestBody QrCodeDto qrCodeDto) throws Exception {	
			return CustomResponseEntity.succResponse(clubService.qrCodeComplete(qrCodeDto),"");
	}
	// 클럽생성후 issuer서버로 부터 요청받고  푸시보낸다
	@RequestMapping(value = "/club/after/create/issuer", method = RequestMethod.POST)
	public ResponseEntity<?> afterClubCreate(@RequestBody IssuerDto issuerDto) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.afterClubCreate(issuerDto),"");
	}
	// 엑셀업로드후 issuer서버로 부터 요청받고 푸시/문자 보낸다
	@RequestMapping(value = "/club/after/excel/issuer", method = RequestMethod.POST)
	public ResponseEntity<?> afterExcelCreate(@RequestBody IssuerDto issuerDto) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.afterExcelCreate(issuerDto),"");
	}	
	// 엑셀업로드후 issuer서버로 부터 요청받고 푸시/문자 보낸다 valid True : 1 만 보낸다 . 클럽멤버에게 클럽초대-알람(재초대): PUSH & SMS"
	@RequestMapping(value = "/club/after/excel/reinvite", method = RequestMethod.POST)
	public ResponseEntity<?> afterExcelCreatReInvite(@RequestBody IssuerDto issuerDto) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.afterExcelCreatReInvite(issuerDto),"");
	}
	
	// 1.모바일환자접수등록(접수+대기통합) issuer서버로 전송하고 다시 리턴 받는로직 있음. 
	@RequestMapping(value = "/club/agent/mobile/reception/waiting/add", method = RequestMethod.POST)
	public ResponseEntity<?> clubAgentMobileWaitingReceptionAdd(@RequestBody MobileDto mobileDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(mobileDto.getClubSeq());
			if(clsAgentClub.isPresent()) {				
				if( clsAgentClub.get().getAgent().getAgentSeq() == 1) {
					mobileDto.setWaitingFlag(true);
					// kakao true
					return CustomResponseEntity.succResponse(clubService.clubAgentMobileWaitingAddPushSms(mobileDto),"");
				}
				else if( clsAgentClub.get().getAgent().getAgentSeq() == 3) {
					mobileDto.setWaitingFlag(false);
					// kakao true
					return CustomResponseEntity.succResponse(clubService.clubAgentMobileReceptionAdd(mobileDto),"");
				}
				else {
					throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);			
				}
			}else {
				throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
			}			
		}
	}
	// 2.모바일환자접수등록(접수+대기통합)후 > 엑셀업로드후 > issuer서버로 부터 요청받고 푸시/문자 보낸다
	@RequestMapping(value = "/club/after/mobile/excel/issuer", method = RequestMethod.POST)
	public ResponseEntity<?> afterMobileExcelCreate(@RequestBody IssuerDto issuerDto) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.afterMobileExcelCreate(issuerDto),"");
	}	
	// 환자대기수정 > 대기자명단중첫번째환자푸시전송
	@RequestMapping(value = "/club/agent/waiting/update", method = RequestMethod.POST)
	public ResponseEntity<?> clubAgentWaitingUpdate(@RequestBody ClubDto clubDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubAgentWaitingUpdate(clubDto),"");
		}
	}
	// 환자대기questionnaire수정
	@RequestMapping(value = "/club/agent/waiting/questionnaire/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubAgentWaitingQuestionnaireUpdate(@RequestBody ClubDto clubDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubAgentWaitingQuestionnaireUpdate(clubDto),"");
		}
	}
	// 클럽맴버extra-data수정
	@RequestMapping(value = "/club/member/extradata/update", method = RequestMethod.PUT)
	public ResponseEntity<?> clubMemberExtraDataUpdate(@RequestBody ClubDto clubDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			return CustomResponseEntity.succResponse(clubService.clubMemberExtraDataUpdate(clubDto),"");
		}
	}
	
	
	
	// ---------------------- GET METHOD ------------------------------------
	// 클럽분류 전체요청시 [수집정보포함]
	@RequestMapping(value = "/club/category/list", method = RequestMethod.GET)
	public ResponseEntity<?> clubCategoryList() throws Exception {		
		return CustomResponseEntity.succResponse(clubService.clubCategoryList(),"");
	}
	// 클럽분류 한건요청시 수집정보리스트
	@RequestMapping(value = "/club/category/item/list/{categorySeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubCategorItemList(@PathVariable Long categorySeq) throws Exception {			
		return CustomResponseEntity.succResponse(clubService.clubCategoryitemList(categorySeq),"");
	}
	// 은행리스트
	@RequestMapping(value = "/club/bank/list", method = RequestMethod.GET)
	public ResponseEntity<?> bankList() throws Exception {		
		return CustomResponseEntity.succResponse(clubService.bankList(),"");
	}
	// 클럽전체리스트	
	@RequestMapping(value = "/club/list/all", method = RequestMethod.GET)
	public ResponseEntity<?> clubListAll(Pageable pageable) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.clubListAll(pageable),"");
	}
	// 클럽개별정보
	@RequestMapping(value = "/club/list/one/{clubSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubListOne(@PathVariable Long clubSeq) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.clubListOne(clubSeq),"");
	}
	// 나의클럽전체리스트
	@RequestMapping(value = "/club/list/my", method = RequestMethod.GET)
	public ResponseEntity<?> clubListMy(Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubListMy(setMemberDto(),pageable),"");
		}
	}
	// 내가생성한클럽전체리스트
	@RequestMapping(value = "/club/list/my/issuer", method = RequestMethod.GET)
	public ResponseEntity<?> clubLisIssuerMy(Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubLisIssuerMy(setMemberDto(),pageable),"");
		}
	}
	
	// 환자대기리스트(전체 오늘환자 등록수 대기수 등 포함)
	@RequestMapping(value = "/club/agent/waiting/list/{clubSeq}/{selectDate}", method = RequestMethod.GET)
	public ResponseEntity<?> clubAgentWaitingList(@PathVariable Long clubSeq,@PathVariable String selectDate,Pageable pageable) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubAgentWaitingList(clubSeq,selectDate,pageable),"");
		}
	}
	// 환자접수리스트(전체 오늘환자 등록수 대기수 등 포함)
	@RequestMapping(value = "/club/agent/reception/list/{clubSeq}/{selectDate}", method = RequestMethod.GET)
	public ResponseEntity<?> clubAgentReceptionList(@PathVariable Long clubSeq,@PathVariable String selectDate,Pageable pageable) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubAgentReceptionList(clubSeq,selectDate,pageable),"");
		}
	}
	// 환자대기리스트(전체 오늘환자 접수/대기수 등 포함)
	@RequestMapping(value = "/club/agent/reception/waiting/list/{clubSeq}/{selectDate}", method = RequestMethod.GET)
	public ResponseEntity<?> clubAgentReceptionWaitingList(@PathVariable Long clubSeq,@PathVariable String selectDate,Pageable pageable) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubAgentReceptionWaitingList(clubSeq,selectDate,pageable),"");
		}
	}
	// SMS 링크 현재대기번호 출력 > 웹에서 사용하는거 
	@RequestMapping(value = "/club/guest/waiting/{clubInvitationSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> getWaitingNumber(@PathVariable Long clubInvitationSeq) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.getWaitingNumber(clubInvitationSeq),"");
	}
	// App 현재대기번호 출력 
	@RequestMapping(value = "/club/guest/waiting/{clubSeq}/{did}", method = RequestMethod.GET)
	public ResponseEntity<?> getWaitingNumberApp(@PathVariable Long clubSeq,@PathVariable String did) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.getWaitingNumberApp(did,clubSeq),"");
	}	
	
	
	// 환자송금리스트(전체 오늘환자 등록수 대기수 등 포함)
	@RequestMapping(value = "/club/agent/fee/list/{clubSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubAgentFeeList(@PathVariable Long clubSeq,Pageable pageable) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubAgentFeeList(clubSeq,pageable),"");
		}
	}	
	// 클럽 invitation 리스트
	@RequestMapping(value = "/club/member/list/invite/{clubSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubMemberListInvite(@PathVariable Long clubSeq, Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubMemberListInvite(clubSeq,pageable),"");
		}
	}		
	// 클럽 invitation 앱NO,클럽가입NO 리스트
	@RequestMapping(value = "/club/member/list/invite/noapp/noclub/{clubSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubMemberListInviteNoAppNoClub(@PathVariable Long clubSeq, Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubMemberListInviteNoAppNoClub(clubSeq,pageable),"");
		}
	}		
	// 클럽 invitation 앱YES,클럽가입NO 리스트
	@RequestMapping(value = "/club/member/list/invite/yesapp/noclub/{clubSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubMemberListInviteYesAppNoClubCount(@PathVariable Long clubSeq) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubMemberListInviteYesAppNoClubCount(clubSeq),"");
		}
	}			
	// 클럽 member 리스트
	@RequestMapping(value = "/club/member/list/{clubSeq}", method = RequestMethod.GET)
	public ResponseEntity<?> clubMemberList(@PathVariable Long clubSeq, Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubMemberList(clubSeq,pageable),"");
		}
	}	
	// 나의 로그 리스트 
	@RequestMapping(value = "/club/log/list/my", method = RequestMethod.GET)
	public ResponseEntity<?> clubMemberList(Pageable pageable) throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubNotificationList(setMemberDto(), pageable),"");
		}
	}
	// 클럽이름 중복체크 
	@RequestMapping(value = "/club/find/name/{clubName}", method = RequestMethod.GET)
	public ResponseEntity<?> findClubName(@PathVariable String clubName) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.findClubName(clubName),"");
	}
		
	
	
	// 테스트 
	@RequestMapping(value = "/test/{size}", method = RequestMethod.GET)
	public ResponseEntity<?> test(@PathVariable int size) throws Exception {		
		return CustomResponseEntity.succResponse(clubService.test(size),"");
	}
	
	// 환자접수리스트
	/*
	@RequestMapping(value = "/club/agent/reception/list/{clubSeq}/{selectDate}", method = RequestMethod.GET)
	public ResponseEntity<?> clubAgentReceptionList(@PathVariable Long clubSeq,@PathVariable String selectDate,Pageable pageable) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {				
			log.info("clubAgentReceptionList clubSeq > {} " , clubSeq);
			log.info("clubAgentReceptionList selectDate > {} " , selectDate);			
			return CustomResponseEntity.succResponse(clubService.clubAgentReceptionList(clubSeq,selectDate,pageable),"");
		}
	}
	*/	
	// 환자대기등록	??
//	@RequestMapping(value = "/club/agent/waiting/add", method = RequestMethod.POST)
//	public ResponseEntity<?> clubAgentWaitingAdd(@RequestBody ClubDto clubDto) throws Exception {	
//		if("not-login".equals(getClaims().getId())) {
//			throw new CustomException(ErrorCode.UNAUTHORIZED);				
//		}else {	
//			return CustomResponseEntity.succResponse(clubService.clubAgentWaitingAdd(clubDto),"");
//		}
//	}	
			
	// 삭제예정  모바일환자대기등록
	/*
	@RequestMapping(value = "/club/agent/mobile/waiting/add", method = RequestMethod.POST)
	public ResponseEntity<?> clubAgentMobileWaitingAddPushSms(@RequestBody MobileDto mobileDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubAgentMobileWaitingAddPushSms(mobileDto),"");
		}
	}
	*/	
	
	// 삭제예정 모바일환자접수등록
	/*
	@RequestMapping(value = "/club/agent/mobile/reception/add", method = RequestMethod.POST)
	public ResponseEntity<?> clubAgentMobilereceptionAdd(@RequestBody MobileDto mobileDto) throws Exception {	
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(clubService.clubAgentMobileReceptionAdd(mobileDto),"");
		}
	}
	*/
	
	
	
}
