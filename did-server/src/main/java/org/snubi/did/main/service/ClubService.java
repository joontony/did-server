package org.snubi.did.main.service;

import java.util.List;

import org.snubi.did.main.dto.AgentClubFeeDto;
import org.snubi.did.main.dto.AgentClubWaitingDto;
import org.snubi.did.main.dto.BankDto;
import org.snubi.did.main.dto.CategoryItemDto;
import org.snubi.did.main.dto.ClubCategoryDto;
import org.snubi.did.main.dto.ClubDto;
import org.snubi.did.main.dto.ClubInvitationDto;
import org.snubi.did.main.dto.ClubListDto;
import org.snubi.did.main.dto.ClubMemberDto;
import org.snubi.did.main.dto.EmailDto;
import org.snubi.did.main.dto.IssuerDto;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.dto.MobileDto;
import org.snubi.did.main.dto.PrivateKeyDto;
import org.snubi.did.main.dto.QrCodeDto;
import org.snubi.did.main.dto.WaitingDto;
import org.snubi.did.main.message.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ClubService {
	Integer clubCreate(ClubDto clubDto, String token) throws Exception;
	Integer clubCreateUniversal(ClubDto clubDto, String token) throws Exception;
	Integer clubCreateUniversalWithFile(MultipartFile imagePathCard, ClubDto clubDto, String token) throws Exception;
	boolean clubValidUpdate(Long clubSeq);
	boolean emailUpdateAndSend(EmailDto emailDto, String memberId);
	boolean privateUpdateAndSend(PrivateKeyDto privateKeyDto, String memberId);	
	boolean clubPushLogUpdate(Long pushLogSeq);
	boolean clubSmsLogUpdate(Long smsLogSeq);
	boolean clubListMyRoleUpdate(ClubDto clubDto);
	boolean clubUpdate(MultipartFile imagePath1, MultipartFile imagePath2, MultipartFile imagePath3, MultipartFile imagePath4, MultipartFile imagePath5, ClubDto clubDto, MemberDto memberDto);		
	boolean clubCardUpdate(MultipartFile imagePath,Long clubSeq, boolean isFile, MemberDto memberDto);
	List<ClubCategoryDto> clubCategoryList();
	List<BankDto> bankList();
	List<CategoryItemDto> clubCategoryitemList(Long categorySeq);
	boolean afterClubCreate(IssuerDto issuerDto);
	boolean afterMobileExcelCreate(IssuerDto issuerDto);
	boolean afterExcelCreate(IssuerDto issuerDto);	
	boolean afterExcelCreatReInvite(IssuerDto issuerDto);	
	Page<ClubListDto> clubListAll(Pageable pageable);
	Page<ClubListDto> clubListMy(MemberDto memberDto, Pageable pageable);
	Page<ClubListDto> clubLisIssuerMy(MemberDto memberDto, Pageable pageable);
	ClubListDto clubListOne(Long clubSeq);	
	boolean clubAgentWaitingUpdate(ClubDto clubDto);
	
	Page<AgentClubWaitingDto> clubAgentWaitingList(Long clubSeq,String selectDate, Pageable pageable);
	Page<AgentClubWaitingDto> clubAgentReceptionList(Long clubSeq,String selectDate, Pageable pageable);
	Page<AgentClubWaitingDto> clubAgentReceptionWaitingList(Long clubSeq,String selectDate, Pageable pageable);
	
	Page<AgentClubFeeDto> clubAgentFeeList(Long clubSeq, Pageable pageable);
	Page<ClubInvitationDto> clubMemberListInvite(Long clubSeq, Pageable pageable);
	Page<ClubInvitationDto> clubMemberListInviteNoAppNoClub(Long clubSeq, Pageable pageable);
	Integer clubMemberListInviteYesAppNoClubCount(Long clubSeq);
	Page<ClubMemberDto> clubMemberList(Long clubSeq, Pageable pageable);	
	Page<NotificationDto> clubNotificationList(MemberDto memberDto, Pageable pageable);	
	boolean qrCodeComplete(QrCodeDto qrCodeDto);	
	WaitingDto getWaitingNumber(Long clubInvitationSeq);
	WaitingDto getWaitingNumberApp(String did, Long clubSeq);
	boolean findClubName(String clubName); 
	//Page<AgentClubReceptionDto> clubAgentReceptionList(Long clubSeq,String selectDate, Pageable pageable);	
	boolean clubAgentUpdate(Long clubSeq,Long AgentSeq);
	boolean clubAgentMobileReceptionAdd(MobileDto mobileDto);
	boolean clubAgentWaitingAdd(ClubDto clubDto);
	boolean clubAgentMobileWaitingAddPushSms(MobileDto mobileDto);
	boolean test(int size);
	
	boolean clubAgentWaitingQuestionnaireUpdate(ClubDto clubDto);
	boolean clubMemberExtraDataUpdate(ClubDto clubDto);
	
}
