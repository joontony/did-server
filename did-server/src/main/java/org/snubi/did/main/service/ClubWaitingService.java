//package org.snubi.did.main.service;
//
//import java.util.List;
//
//import org.snubi.did.main.dto.AgentClubFeeDto;
//import org.snubi.did.main.dto.AgentClubWaitingDto;
//import org.snubi.did.main.dto.BankDto;
//import org.snubi.did.main.dto.CategoryItemDto;
//import org.snubi.did.main.dto.ClubCategoryDto;
//import org.snubi.did.main.dto.ClubDto;
//import org.snubi.did.main.dto.ClubInvitationDto;
//import org.snubi.did.main.dto.ClubListDto;
//import org.snubi.did.main.dto.ClubMemberDto;
//import org.snubi.did.main.dto.EmailDto;
//import org.snubi.did.main.dto.IssuerDto;
//import org.snubi.did.main.dto.MemberDto;
//import org.snubi.did.main.dto.MobileDto;
//import org.snubi.did.main.dto.PrivateKeyDto;
//import org.snubi.did.main.dto.QrCodeDto;
//import org.snubi.did.main.dto.WaitingDto;
//import org.snubi.did.main.message.NotificationDto;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.web.multipart.MultipartFile;
//
//public class ClubWaitingService extends ClubAbstractService {
//	
//	
//	public ClubWaitingService() {
//		init();
//	}
//	
//	
//	@Override
//	public void init() {
//		// TODO Auto-generated method stub
//		
//	}
//	
//
//	@Override
//	public boolean clubValidUpdate(Long clubSeq) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean emailUpdateAndSend(EmailDto emailDto, String memberId) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean privateUpdateAndSend(PrivateKeyDto privateKeyDto, String memberId) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean clubPushLogUpdate(Long pushLogSeq) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean clubSmsLogUpdate(Long smsLogSeq) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean clubListMyRoleUpdate(ClubDto clubDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean clubUpdate(MultipartFile imagePath1, MultipartFile imagePath2, MultipartFile imagePath3,
//			MultipartFile imagePath4, MultipartFile imagePath5, ClubDto clubDto, MemberDto memberDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public List<ClubCategoryDto> clubCategoryList() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<BankDto> bankList() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<CategoryItemDto> clubCategoryitemList(Long categorySeq) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean afterClubCreate(IssuerDto issuerDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean afterExcelCreate(IssuerDto issuerDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean afterExcelCreatReInvite(IssuerDto issuerDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Page<ClubListDto> clubListAll(Pageable pageable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Page<ClubListDto> clubListMy(MemberDto memberDto, Pageable pageable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Page<ClubListDto> clubLisIssuerMy(MemberDto memberDto, Pageable pageable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ClubListDto clubListOne(Long clubSeq) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean clubAgentWaitingAdd(ClubDto clubDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean clubAgentWaitingUpdate(ClubDto clubDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Page<AgentClubWaitingDto> clubAgentWaitingList(Long clubSeq, String selectDate, Pageable pageable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Page<AgentClubFeeDto> clubAgentFeeList(Long clubSeq, Pageable pageable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Page<ClubInvitationDto> clubMemberListInvite(Long clubSeq, Pageable pageable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Page<ClubMemberDto> clubMemberList(Long clubSeq, Pageable pageable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Page<NotificationDto> clubNotificationList(MemberDto memberDto, Pageable pageable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean clubAgentMobileWaitingAddPushSms(MobileDto mobileDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean qrCodeComplete(QrCodeDto qrCodeDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public WaitingDto getWaitingNumber(Long clubInvitationSeq) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public WaitingDto getWaitingNumberApp(String did, Long clubSeq) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean findClubName(String clubName) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	
//
//}
