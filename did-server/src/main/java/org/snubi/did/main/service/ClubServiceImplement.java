package org.snubi.did.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.common.MemberGrade;
import org.snubi.did.main.config.CustomConfig;
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
import org.snubi.did.main.dto.ExcelDto;
import org.snubi.did.main.dto.IssuerDto;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.dto.MobileDto;
import org.snubi.did.main.dto.PrivateKeyDto;
import org.snubi.did.main.dto.QrCodeDto;
import org.snubi.did.main.dto.UniversalDto;
import org.snubi.did.main.dto.WaitingDto;
import org.snubi.did.main.entity.Agent;
import org.snubi.did.main.entity.AgentClub;
import org.snubi.did.main.entity.AgentClubFee;
import org.snubi.did.main.entity.AgentClubWaiting;
import org.snubi.did.main.entity.Bank;
import org.snubi.did.main.entity.ChainNode;
import org.snubi.did.main.entity.Club;
import org.snubi.did.main.entity.ClubCategory;
import org.snubi.did.main.entity.ClubCategoryItem;
import org.snubi.did.main.entity.ClubFee;
import org.snubi.did.main.entity.ClubInvitation;
import org.snubi.did.main.entity.ClubItemValue;
import org.snubi.did.main.entity.ClubMember;
import org.snubi.did.main.entity.ClubRole;
import org.snubi.did.main.entity.EmailLog;
import org.snubi.did.main.entity.Member;
import org.snubi.did.main.entity.MemberDid;
import org.snubi.did.main.entity.Notification;
import org.snubi.did.main.entity.PushLog;
import org.snubi.did.main.entity.PushType;
import org.snubi.did.main.entity.SmsLog;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.message.EmailService;
import org.snubi.did.main.message.KakaoDto;
import org.snubi.did.main.message.KakaoService;
import org.snubi.did.main.message.NotificationDto;
import org.snubi.did.main.message.PushDto;
import org.snubi.did.main.message.PushService;
import org.snubi.did.main.message.SmsService;
import org.snubi.did.main.message.KakaoDto.KakaoOptions;
import org.snubi.did.main.message.KakaoDto.Messages;
import org.snubi.did.main.repository.AgentClubFeeRepository;
import org.snubi.did.main.repository.AgentClubRepository;
import org.snubi.did.main.repository.AgentClubWaitingRepository;
import org.snubi.did.main.repository.AgentRepository;
import org.snubi.did.main.repository.BankRepository;
import org.snubi.did.main.repository.ChainNodeRepository;
import org.snubi.did.main.repository.ClubCategoryItemRepository;
import org.snubi.did.main.repository.ClubCategoryRepository;
import org.snubi.did.main.repository.ClubFeeRepository;
import org.snubi.did.main.repository.ClubInvitationRepository;
import org.snubi.did.main.repository.ClubItemValueRepository;
import org.snubi.did.main.repository.ClubMemberRepository;
import org.snubi.did.main.repository.ClubRepository;
import org.snubi.did.main.repository.ClubRoleRepository;
import org.snubi.did.main.repository.EmailLogRepository;
import org.snubi.did.main.repository.MemberDidRepository;
import org.snubi.did.main.repository.MemberRepository;
import org.snubi.did.main.repository.NotificationRepository;
import org.snubi.did.main.repository.PushLogRepository;
import org.snubi.did.main.repository.PushTypeRepository;
import org.snubi.did.main.repository.SmsLogRepository;
import org.snubi.lib.date.DateUtil;
import org.snubi.lib.http.HttpUtilPost;
import org.snubi.lib.json.JsonUtil;
import org.snubi.lib.response.SnubiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubServiceImplement extends KuberNetesService implements ClubService, NetworkService {

	private final ClubRepository clubRepository;
	private final MemberDidRepository memberDidRepository;
	private final ClubCategoryRepository clubCategoryRepository;
	private final ClubFeeRepository clubFeeRepository;
	private final BankRepository bankRepository;
	private final ClubCategoryItemRepository clubCategoryItemRepository;
	private final ClubItemValueRepository clubItemValueRepository;
	private final ChainNodeRepository chainNodeRepository;
	private final ClubMemberRepository clubMemberRepository;
	private final MemberRepository memberRepository;
	private final PushTypeRepository pushTypeRepository;
	private final ClubInvitationRepository clubInvitationRepository;
	private final SmsLogRepository smsLogRepository;
	private final EmailLogRepository emailLogRepository;
	private final AgentRepository agentRepository;
	private final AgentClubRepository agentClubRepository;
	private final AgentClubWaitingRepository agentClubWaitingRepository;
	private final NotificationRepository notificationRepository;
	private final ClubRoleRepository clubRoleRepository;
	private final AgentClubFeeRepository agentClubFeeRepository;
	private final PushLogRepository pushLogRepository;
	private final UniversalPodService universalPodService;
	private final SmsService smsService;
	private final EmailService emailService;
	private final PushService pushService;
	private final KakaoService kakaoService;
		
	@Override
	public boolean clubCardUpdate(MultipartFile file,Long clubSeq, boolean isFile, MemberDto memberDto) {
		log.info("--------------------------------------------");
		log.info("클럽카드배경수정");
		log.info("--------------------------------------------");
		try {	
			Optional<Club> club = clubRepository.findByClubSeq(clubSeq);
			if (club.isPresent()) {
				String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
				String folderName = clubSeq + "_clubSeq";// +club.get().getClubName();		
				String fullPath = CustomConfig.strFileUploadPath + "/" + folderName + "/fileCard_" + timestamp + ".png";				
					if (!file.isEmpty()) {
						File tmpFile = new File(fullPath);
						tmpFile.getParentFile().mkdir();
						file.transferTo(tmpFile);
						club.get().updateImagePathCard(fullPath);
						clubRepository.save(club.get());
					}
					if(!isFile) {
						club.get().updateImagePathCard("");
						clubRepository.save(club.get());
					}							
			}
			return true;  	
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new CustomException(ErrorCode.FILE_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CustomException(ErrorCode.FILE_ERROR);
		}		
	}
	
	
	@Transactional
	@Override
	public boolean clubUpdate(MultipartFile imagePath1, MultipartFile imagePath2, MultipartFile imagePath3,
			MultipartFile imagePath4, MultipartFile imagePath5, ClubDto clubDto, MemberDto memberDto) {
		log.info("--------------------------------------------");
		log.info("클럽수정 ");
		log.info("--------------------------------------------");
		Optional<Club> club = clubRepository.findByClubSeq(clubDto.getClubSeq());
		if (club.isPresent()) {
			String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
			String folderName = club.get().getClubSeq() + "_clubSeq";// +club.get().getClubName();
			String fullPath01 = CustomConfig.strFileUploadPath + "/" + folderName + "/file1_" + timestamp + ".png";
			String fullPath02 = CustomConfig.strFileUploadPath + "/" + folderName + "/file2_" + timestamp + ".png";
			String fullPath03 = CustomConfig.strFileUploadPath + "/" + folderName + "/file3_" + timestamp + ".png";
			String fullPath04 = CustomConfig.strFileUploadPath + "/" + folderName + "/file4_" + timestamp + ".png";
			String fullPath05 = CustomConfig.strFileUploadPath + "/" + folderName + "/file5_" + timestamp + ".png";
			//String fullPathCard = CustomConfig.strFileUploadPath + "/" + folderName + "/fileCard_" + timestamp + ".png";
			List<String> dbPath = Arrays.asList("", "", "", "", "");
			// 구조가 적절한가?? 일단 GO!
			List<String> imageTexts = clubDto.getImageTexts();
			try {
				List<Boolean> fileExists = clubDto.getFileExists();
				if (fileExists.size() != 5) {
					throw new CustomException(ErrorCode.FILE_ERROR);
				}
				if (!imagePath1.isEmpty()) {
					File tmpFile = new File(fullPath01);
					tmpFile.getParentFile().mkdir();
					imagePath1.transferTo(tmpFile);
					dbPath.set(0, fullPath01);
					club.get().updateImagePath1(dbPath.get(0));
					club.get().updateImageText1(imageTexts.get(0));
					clubRepository.save(club.get());
				}else if(imagePath1.isEmpty() && fileExists.get(0)) {
					club.get().updateImageText1(imageTexts.get(0));
					clubRepository.save(club.get());
				}
				if (!imagePath2.isEmpty()) {
					File tmpFile = new File(fullPath02);
					tmpFile.getParentFile().mkdir();
					imagePath2.transferTo(tmpFile);
					dbPath.set(1, fullPath02);
					club.get().updateImagePath2(dbPath.get(1));
					club.get().updateImageText2(imageTexts.get(1));
					clubRepository.save(club.get());
				}else if(imagePath2.isEmpty() && fileExists.get(1)) {
					club.get().updateImageText2(imageTexts.get(1));
					clubRepository.save(club.get());
				}
				if (!imagePath3.isEmpty()) {
					File tmpFile = new File(fullPath03);
					tmpFile.getParentFile().mkdir();
					imagePath3.transferTo(tmpFile);
					dbPath.set(2, fullPath03);
					club.get().updateImagePath3(dbPath.get(2));
					club.get().updateImageText3(imageTexts.get(2));
					clubRepository.save(club.get());
				}else if(imagePath3.isEmpty() && fileExists.get(2)) {
					club.get().updateImageText3(imageTexts.get(2));
					clubRepository.save(club.get());
				}
				if (!imagePath4.isEmpty()) {
					File tmpFile = new File(fullPath04);
					tmpFile.getParentFile().mkdir();
					imagePath4.transferTo(tmpFile);
					dbPath.set(3, fullPath04);
					club.get().updateImagePath4(dbPath.get(3));
					club.get().updateImageText4(imageTexts.get(3));
					clubRepository.save(club.get());
				}else if(imagePath4.isEmpty() && fileExists.get(3)) {
					club.get().updateImageText4(imageTexts.get(3));
					clubRepository.save(club.get());
				}
				if (!imagePath5.isEmpty()) {
					File tmpFile = new File(fullPath05);
					tmpFile.getParentFile().mkdir();
					imagePath5.transferTo(tmpFile);
					dbPath.set(4, fullPath05);
					club.get().updateImagePath5(dbPath.get(4));
					club.get().updateImageText5(imageTexts.get(4));
					clubRepository.save(club.get());
				}else if(imagePath5.isEmpty() && fileExists.get(4)) {
					club.get().updateImageText5(imageTexts.get(4));
					clubRepository.save(club.get());
				}
//				if (!imagePathCard.isEmpty()) {
//					File tmpFile = new File(fullPathCard);
//					tmpFile.getParentFile().mkdir();
//					imagePathCard.transferTo(tmpFile);
//					dbPath.set(5, fullPathCard);
//					club.get().updateImagePathCard(dbPath.get(5));
//					clubRepository.save(club.get());
//				}
				
				
				
				for (int i = 0; i < fileExists.size(); i++) {
					if (!fileExists.get(i)) { // false 일때 파일삭제
						switch (i) {
						case 0:
							club.get().updateImagePath1("");
							club.get().updateImageText1("");
							clubRepository.save(club.get());
							break;
						case 1:
							club.get().updateImagePath2("");
							club.get().updateImageText2("");
							clubRepository.save(club.get());
							break;
						case 2:
							club.get().updateImagePath3("");
							club.get().updateImageText3("");
							clubRepository.save(club.get());
							break;
						case 3:
							club.get().updateImagePath4("");
							club.get().updateImageText4("");
							clubRepository.save(club.get());
							break;
						case 4:
							club.get().updateImagePath5("");
							club.get().updateImageText5("");
							clubRepository.save(club.get());
							break;
//						case 5:
//							club.get().updateImagePathCard("");
//							clubRepository.save(club.get());
//							break;
						}
					}
				}
				club.get().updateClub(clubDto.getClubUrl(), clubDto.getLocation(), clubDto.getDescription(),
						clubDto.getOperateTime(), clubDto.getPhone());
				clubRepository.save(club.get());

				if (clubDto.getBankSeq() != null && clubDto.getBankSeq() != 0) {
					Optional<ClubFee> clubFee = clubFeeRepository.findByClub_ClubSeq(club.get().getClubSeq());
					if (clubFee.isPresent()) {
						Optional<Bank> bank = bankRepository.findByBankSeq(clubDto.getBankSeq());
						if (bank.isPresent()) {
							clubFee.get().updateClubFee(bank.get(), clubDto.getFeeValue(), clubDto.getBankAccount(),
									clubDto.getBankOwnerName(), clubDto.getKakaoCodeLink());
							clubFeeRepository.save(clubFee.get());
						}
					} else {
						Optional<Bank> bank = bankRepository.findByBankSeq(clubDto.getBankSeq());
						if (bank.isPresent()) {
							ClubFee clsClubFee = ClubFee.builder().club(club.get()).bank(bank.get())
									.feeValue(clubDto.getFeeValue()).bankAccount(clubDto.getBankAccount())
									.bankOwnerName(clubDto.getBankOwnerName()).kakaoCodeLink(clubDto.getKakaoCodeLink())
									.build();
							clubFeeRepository.save(clsClubFee);
						}
					}

				}

				return true;

			} catch (IllegalStateException e) {
				e.printStackTrace();
				throw new CustomException(ErrorCode.FILE_ERROR);
			} catch (IOException e) {
				e.printStackTrace();
				throw new CustomException(ErrorCode.FILE_ERROR);
			}
		}
		return false;
	}

	@Transactional
	@Override
	public Integer clubCreate(ClubDto clubDto, String token) throws Exception {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		log.info("--------------------------------------------");
		log.info("클럽생성 ");
		log.info("--------------------------------------------");
		
		Optional<MemberDid> memberDid = memberDidRepository.findByDid(clubDto.getDid());
		Optional<ClubCategory> clubCategory = clubCategoryRepository.findByClubCategorySeq(clubDto.getClubCatogorySeq());
		if (memberDid.isPresent() && clubCategory.isPresent()) {
			Club club = Club.builder()
					.memberDid(memberDid.get())
					.clubCategory(clubCategory.get())
					.clubName(clubDto.getClubName())
					.clubPublicKey(clubDto.getClubPublicKey())
					.description(clubDto.getDescription())
					.operateTime(clubDto.getOperateTime())
					.location(clubDto.getLocation())
					.startDate(LocalDateTime.parse(clubDto.getStartDate() + " 00:00:00", formatter))
					.endDate(LocalDateTime.parse(clubDto.getEndDate() + " 00:00:00", formatter))
					.build();
			clubRepository.save(club);

			log.info("--------------------------------------------");
			log.info("클럽멤버저장 ");
			log.info("--------------------------------------------");

			Optional<ClubRole> clubRole = clubRoleRepository.findByClubRoleSeq(1L);

			ClubMember clubMember = ClubMember.builder()
					.club(club)
					.clubRole(clubRole.get())
					.expiredDate(DateUtil.toDate("yyyy-MM-dd", clubDto.getEndDate()))
					.memberDid(memberDid.get())
					.memberGrade(MemberGrade.PRESIDENT.getDescription())
					.valid(true)
					.build();
			clubMemberRepository.save(clubMember);

			log.info("--------------------------------------------");
			log.info("회비생성 "); // 카카오주소
			log.info("--------------------------------------------");
			log.info("--------------------------------------------clubDto.getBankSeq() " + clubDto.getBankSeq());
			if (clubDto.getBankSeq() != null && clubDto.getBankSeq() != 0) {
				// bank 0 or 1
				Optional<Bank> bank = bankRepository.findByBankSeq(clubDto.getBankSeq());
				log.info("--------------------------------------------bank() " + bank.get());
				// public ClubFee(Bank bank, Club club, int feeValue, String bankAccount, String
				// bankOwnerName, String kakaoCodeLink) {
				ClubFee clubFee = new ClubFee(bank.get(), club, clubDto.getFeeValue(), clubDto.getBankAccount(),
						clubDto.getBankOwnerName(), clubDto.getKakaoCodeLink());
//				ClubFee clubFee = ClubFee.builder()
//						.bank(bank.get())
//						.club(club)
//						.feeValue(clubDto.getFeeValue())
//						.bankAccount(clubDto.getBankAccount())
//						.bankOwnerName(clubDto.getBankOwnerName())
//						.kakaoCodeLink(clubDto.getKakaoCodeLink())
//						.build();
				log.info("-------------------------------------------- clubFee.getBank().getBankSeq() " + clubFee);
				log.info("-------------------------------------------- clubFee.getBank().getBankSeq() "
						+ clubFee.getBank());
				log.info("-------------------------------------------- getClubName " + club.getClubName());

				clubFeeRepository.save(clubFee);
				log.info("회비생성저장완료"); // 카카오주소
			}
			if (clubDto.getClubCatogorySeq() == 2) {
				log.info("--------------------------------------------");
				log.info("클럽타입  clubCatogorySeq(2) 의원일때");
				log.info("--------------------------------------------");

				Optional<Agent> agent = agentRepository.findByAgentSeq(1L);
				AgentClub agentClub = AgentClub.builder().agent(agent.get()).club(club).flag(true).build();
				agentClubRepository.save(agentClub);
			} else if (clubDto.getClubCatogorySeq() == 1) { // 의사회
				log.info("--------------------------------------------");
				log.info("클럽타입  clubCatogorySeq(1) 의사회일때");
				log.info("--------------------------------------------");
				Optional<Agent> agent = agentRepository.findByAgentSeq(2L);
				AgentClub agentClub = AgentClub.builder().agent(agent.get()).club(club).flag(true).build();
				agentClubRepository.save(agentClub);
			}else {
				log.info("--------------------------------------------");
				log.info("클럽타입  clubCatogorySeq(1,2 아닐때) ");
				log.info("--------------------------------------------");
				Optional<Agent> agent = agentRepository.findByAgentSeq(2L);
				AgentClub agentClub = AgentClub.builder().agent(agent.get()).club(club).flag(true).build();
				agentClubRepository.save(agentClub);
			}
			List<Long> itemList = clubDto.getClubCategoryItemSeq();
			for (Long item : itemList) {
				Optional<ClubCategoryItem> clubCategoryItem = clubCategoryItemRepository
						.findByClubCategoryItemSeq(item);

				ClubItemValue clubItemValue = ClubItemValue.builder().club(club)
						.ClubCategoryItem(clubCategoryItem.get()).value("").build();
				clubItemValueRepository.save(clubItemValue);
			}
			log.info("--------------------------------------------");
			log.info("쿠버네티스요청");
			log.info("--------------------------------------------");
			Optional<ChainNode> chainNode = chainNodeRepository.findByChainNodeSeq(CustomConfig.chainNodeNumber);
			log.info("쿠버네티스요청 getNodeIp {}", chainNode.get().getNodeIp());
			String podUrl = asyncSendYaml(token, clubDto, club, chainNode.get());
			if (!podUrl.equals("")) {
				log.info("쿠버네티스응답 도커주소 podUrl =  {}", podUrl);
				club.updatePodUrl(podUrl);
				clubRepository.save(club);
			}

//		    log.info("--------------------------------------------");
//			log.info("회원비번이메일전송:message-server");
//			log.info("--------------------------------------------");
//			List<String> destinList = new ArrayList<>();
//			List<String> titleList = new ArrayList<>();
//			List<String> messageList = new ArrayList<>();
//			List<EmailLog> emailLogList = new ArrayList<>();
//			destinList.add(memberDid.get().getMember().getEmail());
//			titleList.add("라임카드 비밀번호 안내입니다.");
//			messageList.add("라임카드 비밀번호는 " + clubDto.getMemberPassword() + " 입니다.");
//			
//			EmailLog emailLog = EmailLog.builder()
//					.senderMemberId("avchain.io")
//					.receiverMemberId(memberDid.get().getMember().getMemberId())
//					.title(titleList.get(0))
//					.message(messageList.get(0))
//					.confirmFlag(true)
//					.build();
//			emailLogRepository.save(emailLog);
//			emailLogList.add(emailLog);	
//			emailService.sendEmail(destinList, titleList, messageList, emailLogList);
			int clubSeq = club.getClubSeq().intValue();
			return clubSeq;
		}

		return null;
	}
	
	@Transactional
	@Override
	public Integer clubCreateUniversalWithFile(MultipartFile imagePathCard, ClubDto clubDto, String token) throws Exception {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		log.info("--------------------------------------------");
		log.info("클럽생성 ");
		log.info("--------------------------------------------");
				
		
		Optional<MemberDid> memberDid = memberDidRepository.findByDid(clubDto.getDid());
		Optional<ClubCategory> clubCategory = clubCategoryRepository.findByClubCategorySeq(clubDto.getClubCatogorySeq());
		if (memberDid.isPresent() && clubCategory.isPresent()) {
			Club club = Club.builder()
					.memberDid(memberDid.get())
					.clubCategory(clubCategory.get())
					.clubName(clubDto.getClubName())
					.clubPublicKey(clubDto.getClubPublicKey())
					.description(clubDto.getDescription())
					.operateTime(clubDto.getOperateTime())
					.location(clubDto.getLocation())
					.startDate(LocalDateTime.parse(clubDto.getStartDate() + " 00:00:00", formatter))
					.endDate(LocalDateTime.parse(clubDto.getEndDate() + " 00:00:00", formatter)).build();
			clubRepository.save(club);

			log.info("--------------------------------------------");
			log.info("클럽멤버저장 ");
			log.info("--------------------------------------------");

			Optional<ClubRole> clubRole = clubRoleRepository.findByClubRoleSeq(1L);

			ClubMember clubMember = ClubMember.builder().club(club).clubRole(clubRole.get())
					.expiredDate(DateUtil.toDate("yyyy-MM-dd", clubDto.getEndDate())).memberDid(memberDid.get())
					.memberGrade(MemberGrade.PRESIDENT.getDescription()).valid(true).build();
			clubMemberRepository.save(clubMember);

			log.info("--------------------------------------------");
			log.info("회비생성 "); // 카카오주소
			log.info("--------------------------------------------");
			log.info("--------------------------------------------clubDto.getBankSeq() " + clubDto.getBankSeq());
			if (clubDto.getBankSeq() != null && clubDto.getBankSeq() != 0) {
				// bank 0 or 1
				Optional<Bank> bank = bankRepository.findByBankSeq(clubDto.getBankSeq());
				log.info("--------------------------------------------bank() " + bank.get());
				// public ClubFee(Bank bank, Club club, int feeValue, String bankAccount, String
				// bankOwnerName, String kakaoCodeLink) {
				ClubFee clubFee = new ClubFee(bank.get(), club, clubDto.getFeeValue(), clubDto.getBankAccount(),
						clubDto.getBankOwnerName(), clubDto.getKakaoCodeLink());
//				ClubFee clubFee = ClubFee.builder()
//						.bank(bank.get())
//						.club(club)
//						.feeValue(clubDto.getFeeValue())
//						.bankAccount(clubDto.getBankAccount())
//						.bankOwnerName(clubDto.getBankOwnerName())
//						.kakaoCodeLink(clubDto.getKakaoCodeLink())
//						.build();
				log.info("-------------------------------------------- clubFee.getBank().getBankSeq() " + clubFee);
				log.info("-------------------------------------------- clubFee.getBank().getBankSeq() "
						+ clubFee.getBank());
				log.info("-------------------------------------------- getClubName " + club.getClubName());

				clubFeeRepository.save(clubFee);
				log.info("회비생성저장완료"); // 카카오주소
			}
			if (clubDto.getClubCatogorySeq() == 2) {
				log.info("--------------------------------------------");
				log.info("클럽타입  clubCatogorySeq(2) 의원일때");
				log.info("--------------------------------------------");

				Optional<Agent> agent = agentRepository.findByAgentSeq(1L);
				AgentClub agentClub = AgentClub.builder().agent(agent.get()).club(club).flag(true).build();
				agentClubRepository.save(agentClub);
			} else if (clubDto.getClubCatogorySeq() == 1) { // 의사회
				log.info("--------------------------------------------");
				log.info("클럽타입  clubCatogorySeq(1) 의사회일때");
				log.info("--------------------------------------------");
				Optional<Agent> agent = agentRepository.findByAgentSeq(2L);
				AgentClub agentClub = AgentClub.builder().agent(agent.get()).club(club).flag(true).build();
				agentClubRepository.save(agentClub);

			}
			List<Long> itemList = clubDto.getClubCategoryItemSeq();
			for (Long item : itemList) {
				Optional<ClubCategoryItem> clubCategoryItem = clubCategoryItemRepository
						.findByClubCategoryItemSeq(item);

				ClubItemValue clubItemValue = ClubItemValue.builder().club(club)
						.ClubCategoryItem(clubCategoryItem.get()).value("").build();
				clubItemValueRepository.save(clubItemValue);
			}
			log.info("--------------------------------------------");
			log.info("쿠버네티스요청 >> 유니버셜팟요청으로 바뀜");
			log.info("--------------------------------------------");
			log.info("유니버셜팟요청 getNodeIp {}", CustomConfig.strUniversalServeUrl);	
			
			club.updatePodUrl(CustomConfig.strUniversalServeUrl);
			clubRepository.save(club);	
			
			int clubSeq = club.getClubSeq().intValue();
			
			JsonUtil<UniversalDto> clsJsonUtil = new JsonUtil<UniversalDto>();					
			String json = clsJsonUtil.toString(
					UniversalDto.builder()
					.clubSeq(clubSeq)
					.issuerDid(memberDid.get().getDid())
					.startDate(clubDto.getStartDate() + " 00:00:00")
					.endDate(clubDto.getEndDate() + " 00:00:00")
					.valid(false)
					.build());		
			
			log.info("--------------------------------------------strUniversalServerCredentialClubCreate {}", CustomConfig.strUniversalServerCredentialClubCreate);			
			log.info("--------------------------------------------asyncUniversalPod {}", json);
			universalPodService.asyncUniversalPod(json);
			
//			SnubiResponse clsSnubiResponse = HttpService.postUniversalServer(json);	
			log.info("--------------------------------------------return clubSeq {}", clubSeq);		
			String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
			String folderName = clubSeq + "_clubSeq";// +club.get().getClubName();
			String fullPathCard = CustomConfig.strFileUploadPath + "/" + folderName + "/fileCard_" + timestamp + ".png";	
			if (!imagePathCard.isEmpty()) {
				File tmpFile = new File(fullPathCard);
				tmpFile.getParentFile().mkdir();
				imagePathCard.transferTo(tmpFile);				
				club.updateImagePathCard(fullPathCard);
				clubRepository.save(club);
				log.info("--------------------------------------------imagePathCard 저장 {}", club);		
			}			
			
			return clubSeq;
		}

		return null;
	}
	
	
	@Transactional
	@Override
	public Integer clubCreateUniversal(ClubDto clubDto, String token) throws Exception {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		log.info("--------------------------------------------");
		log.info("클럽생성 ");
		log.info("--------------------------------------------");
		Optional<MemberDid> memberDid = memberDidRepository.findByDid(clubDto.getDid());
		Optional<ClubCategory> clubCategory = clubCategoryRepository.findByClubCategorySeq(clubDto.getClubCatogorySeq());
		if (memberDid.isPresent() && clubCategory.isPresent()) {
			Club club = Club.builder()
					.memberDid(memberDid.get())
					.clubCategory(clubCategory.get())
					.clubName(clubDto.getClubName())
					.clubPublicKey(clubDto.getClubPublicKey())
					.description(clubDto.getDescription())
					.operateTime(clubDto.getOperateTime())
					.location(clubDto.getLocation())
					.startDate(LocalDateTime.parse(clubDto.getStartDate() + " 00:00:00", formatter))
					.endDate(LocalDateTime.parse(clubDto.getEndDate() + " 00:00:00", formatter)).build();
			clubRepository.save(club);

			log.info("--------------------------------------------");
			log.info("클럽멤버저장 ");
			log.info("--------------------------------------------");

			Optional<ClubRole> clubRole = clubRoleRepository.findByClubRoleSeq(1L);

			ClubMember clubMember = ClubMember.builder().club(club).clubRole(clubRole.get())
					.expiredDate(DateUtil.toDate("yyyy-MM-dd", clubDto.getEndDate())).memberDid(memberDid.get())
					.memberGrade(MemberGrade.PRESIDENT.getDescription()).valid(true).build();
			clubMemberRepository.save(clubMember);

			log.info("--------------------------------------------");
			log.info("회비생성 "); // 카카오주소
			log.info("--------------------------------------------");
			log.info("--------------------------------------------clubDto.getBankSeq() " + clubDto.getBankSeq());
			if (clubDto.getBankSeq() != null && clubDto.getBankSeq() != 0) {
				// bank 0 or 1
				Optional<Bank> bank = bankRepository.findByBankSeq(clubDto.getBankSeq());
				log.info("--------------------------------------------bank() " + bank.get());
				// public ClubFee(Bank bank, Club club, int feeValue, String bankAccount, String
				// bankOwnerName, String kakaoCodeLink) {
				ClubFee clubFee = new ClubFee(bank.get(), club, clubDto.getFeeValue(), clubDto.getBankAccount(),
						clubDto.getBankOwnerName(), clubDto.getKakaoCodeLink());
//				ClubFee clubFee = ClubFee.builder()
//						.bank(bank.get())
//						.club(club)
//						.feeValue(clubDto.getFeeValue())
//						.bankAccount(clubDto.getBankAccount())
//						.bankOwnerName(clubDto.getBankOwnerName())
//						.kakaoCodeLink(clubDto.getKakaoCodeLink())
//						.build();
				log.info("-------------------------------------------- clubFee.getBank().getBankSeq() " + clubFee);
				log.info("-------------------------------------------- clubFee.getBank().getBankSeq() "
						+ clubFee.getBank());
				log.info("-------------------------------------------- getClubName " + club.getClubName());

				clubFeeRepository.save(clubFee);
				log.info("회비생성저장완료"); // 카카오주소
			}
			if (clubDto.getClubCatogorySeq() == 2) {
				log.info("--------------------------------------------");
				log.info("클럽타입  clubCatogorySeq(2) 의원일때");
				log.info("--------------------------------------------");

				Optional<Agent> agent = agentRepository.findByAgentSeq(1L);
				AgentClub agentClub = AgentClub.builder().agent(agent.get()).club(club).flag(true).build();
				agentClubRepository.save(agentClub);
			} else if (clubDto.getClubCatogorySeq() == 1) { // 의사회
				log.info("--------------------------------------------");
				log.info("클럽타입  clubCatogorySeq(1) 의사회일때");
				log.info("--------------------------------------------");
				Optional<Agent> agent = agentRepository.findByAgentSeq(2L);
				AgentClub agentClub = AgentClub.builder().agent(agent.get()).club(club).flag(true).build();
				agentClubRepository.save(agentClub);

			}
			List<Long> itemList = clubDto.getClubCategoryItemSeq();
			for (Long item : itemList) {
				Optional<ClubCategoryItem> clubCategoryItem = clubCategoryItemRepository
						.findByClubCategoryItemSeq(item);

				ClubItemValue clubItemValue = ClubItemValue.builder().club(club)
						.ClubCategoryItem(clubCategoryItem.get()).value("").build();
				clubItemValueRepository.save(clubItemValue);
			}
			log.info("--------------------------------------------");
			log.info("쿠버네티스요청 >> 유니버셜팟요청으로 바뀜");
			log.info("--------------------------------------------");
			log.info("유니버셜팟요청 getNodeIp {}", CustomConfig.strUniversalServeUrl);	
			
			club.updatePodUrl(CustomConfig.strUniversalServeUrl);
			clubRepository.save(club);	
			
			int clubSeq = club.getClubSeq().intValue();
			
			JsonUtil<UniversalDto> clsJsonUtil = new JsonUtil<UniversalDto>();					
			String json = clsJsonUtil.toString(
					UniversalDto.builder()
					.clubSeq(clubSeq)
					.issuerDid(memberDid.get().getDid())
					.startDate(clubDto.getStartDate() + " 00:00:00")
					.endDate(clubDto.getEndDate() + " 00:00:00")
					.valid(false)
					.build());		
			
			log.info("--------------------------------------------strUniversalServerCredentialClubCreate {}", CustomConfig.strUniversalServerCredentialClubCreate);			
			log.info("--------------------------------------------asyncUniversalPod {}", json);
			universalPodService.asyncUniversalPod(json);
			
//			SnubiResponse clsSnubiResponse = HttpService.postUniversalServer(json);	
			log.info("--------------------------------------------return clubSeq {}", clubSeq);				
			return clubSeq;
		}

		return null;
	}

	@Override
	public List<ClubCategoryDto> clubCategoryList() {
		log.debug("--------------------------------------------");
		log.debug("클럽카테고리 리스트요청");
		log.debug("--------------------------------------------");
		List<ClubCategoryDto> clubCategoryDtoList = new ArrayList<>();
		List<ClubCategory> clubCategory = clubCategoryRepository.findAll();
		for (ClubCategory item : clubCategory) {
			ClubCategoryDto clubCategoryDto = ClubCategoryDto.builder().clubCategorySeq(item.getClubCategorySeq())
					.categoryCode(item.getCategoryCode()).excelPath(getClubAndFileName(item.getExcelPath()))
					.display(item.getDisplay())
					.clubCategoryitem(
							clubCategoryItemRepository.findByClubCategory_ClubCategorySeq(item.getClubCategorySeq()))
					.build();
			clubCategoryDtoList.add(clubCategoryDto);
		}
		return clubCategoryDtoList;
	}

	@Override
	public List<BankDto> bankList() {
		log.debug("--------------------------------------------");
		log.debug("은행 리스트요청");
		log.debug("--------------------------------------------");
		List<BankDto> bankDtoList = new ArrayList<>();
		List<Bank> bank = bankRepository.findAll();
		for (Bank item : bank) {
			BankDto bankDto = BankDto.builder().bankSeq(item.getBankSeq()).display(item.getDisplay())
					.bankCode(item.getBankCode()).build();
			bankDtoList.add(bankDto);
		}
		return bankDtoList;
	}

	@Override
	public List<CategoryItemDto> clubCategoryitemList(Long categorySeq) {
		log.debug("--------------------------------------------");
		log.debug("클럽 카테고리 아이템 리스트요청");
		log.debug("--------------------------------------------");
		List<CategoryItemDto> categoryItemDtoList = new ArrayList<>();
		List<ClubCategoryItem> clubCategoryItemItem = clubCategoryItemRepository
				.findByClubCategory_ClubCategorySeq(categorySeq);
		for (ClubCategoryItem item : clubCategoryItemItem) {
			CategoryItemDto categoryItemDto = CategoryItemDto.builder()
					.clubCategoryItemSeq(item.getClubCategoryItemSeq()).itemCode(item.getItemCode())
					.itemDisplay(item.getItemDispaly()).build();
			categoryItemDtoList.add(categoryItemDto);
		}
		return categoryItemDtoList;
	}

	@Override
	public boolean afterClubCreate(IssuerDto issuerDto) {
		log.info("--------------------------------------------");
		log.info("클럽생성후 issuer서버로 부터 요청받는다");
		log.info("--------------------------------------------");
		Optional<Club> club = clubRepository.findByClubSeq(issuerDto.getClubSeq());
		Optional<MemberDid> memberDid = memberDidRepository
				.findByMemberDidSeq(club.get().getMemberDid().getMemberDidSeq());
		Optional<Member> member = memberRepository.findByMemberId(memberDid.get().getMember().getMemberId());
		Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(1L);
		if (member.isPresent()) {
			try {
				JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
				String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 클럽 생성 안내. ")
						.body("["+club.get().getClubName() + "] 라임카드가 생성완료되었습니다. 확인해주세요.").clubName(club.get().getClubName())
						.deviceId(member.get().getDeviceId()).pushType(pushType.get().getTypeCode())
						.clubSeq(issuerDto.getClubSeq()).podUrl(issuerDto.getPodUrl())
						.publicKey(issuerDto.getPublicKey()).credential(issuerDto.getCredential())
						.vcSignatureSeq(issuerDto.getVcSignatureSeq()).build());
				PushLog pushLog = PushLog.builder().club(club.get()).pushType(pushType.get()).clubNoticeSeq(null)
						.senderMemberId(memberDid.get().getMember().getMemberId())
						.receiverMemberId(memberDid.get().getMember().getMemberId()).title("[라임카드] 클럽 생성 안내. ")
						.message("["+club.get().getClubName() + "] 라임카드가 생성완료되었습니다. 확인해주세요.").linkedUrl("").extraMessage("")
						.confirmFlag(false).readFlag(false).build();
				pushService.sendPush(json, pushLog);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
	
	

	@Override
	public Page<ClubListDto> clubListAll(Pageable pageable) {
		log.debug("--------------------------------------------");
		log.debug("클럽전체리스트");
		log.debug("--------------------------------------------");
		Page<Club> clubList = clubRepository.findAllBy(pageable);
		Page<ClubListDto> clubListDto = clubList.map(m -> ClubListDto.builder().clubSeq(m.getClubSeq())
				.memberDid(m.getMemberDid()).clubCategory(m.getClubCategory()).clubName(m.getClubName())
				.clubPublicKey(m.getClubPublicKey()).description(m.getDescription()).operateTime(m.getOperateTime())
				.location(m.getLocation()).phone(m.getPhone()).startDate(m.getStartDate()).endDate(m.getEndDate())
				.clubUrl(m.getClubUrl())
				.podUrl(m.getPodUrl())
				.valid(m.isValid())
				.imagePath1(getClubAndFileName(m.getImagePath1()))
				.imagePath2(getClubAndFileName(m.getImagePath2()))
				.imagePath3(getClubAndFileName(m.getImagePath3()))
				.imagePath4(getClubAndFileName(m.getImagePath4()))
				.imagePath5(getClubAndFileName(m.getImagePath5()))
				.imagePathCard(getClubAndFileName(m.getImagePathCard()))
				.imageText1(m.getImageText1())
				.imageText2(m.getImageText2())
				.imageText3(m.getImageText3())
				.imageText4(m.getImageText4())
				.imageText5(m.getImageText5())
				.created(m.getCreated()).build());

		return clubListDto;
	}

	private String getClubAndFileName(String path) {
		if (path == null || "".equals(path)) {
			return "";
		} else {
			String[] nameArray = path.split("/");
			if (path.split("/").length < 2)
				return "";
			int imageName = nameArray.length - 1;
			int clubName = nameArray.length - 2;
			//log.info("imageName {}", imageName);
			//log.info("clubName {}", clubName);
			return CustomConfig.strDefaultImageUrl + nameArray[clubName] + "/" + nameArray[imageName];
		}
	}

	@Override
	public Page<ClubListDto> clubListMy(MemberDto memberDto, Pageable pageable) {
		log.debug("--------------------------------------------");
		log.debug("나의클럽전체리스트");
		log.debug("--------------------------------------------");
		List<Long> memberDidSeqList = new ArrayList<>();
		List<Long> clubSeqList = new ArrayList<>();
		List<MemberDid> memberDidList = memberDidRepository.findByMember_MemberId(memberDto.getMemberId());
		for (MemberDid item : memberDidList) {
			memberDidSeqList.add(item.getMemberDidSeq());
			//log.info("memberDidList {}", item.getMemberDidSeq());
			List<ClubMember> clubMember = clubMemberRepository.findByMemberDid_MemberDidSeq(item.getMemberDidSeq());
			for (ClubMember item2 : clubMember) {
				clubSeqList.add(item2.getClub().getClubSeq());
				//log.info("나의클럽전체리스트 : 클럽회원 clubSeq : {}", item2.getClub().getClubSeq());
			}
		}

		Page<Club> clubLists = clubRepository.findByClubSeqIn(clubSeqList, pageable);
		log.info("clubList {}", clubLists.getSize());
		Page<ClubListDto> clubListDto = clubLists.map(m -> ClubListDto.builder().clubSeq(m.getClubSeq())
				.memberDid(m.getMemberDid())
				.clubRole(getClubRole(m.getClubSeq(), memberDidSeqList))
				.clubCategory(m.getClubCategory())
				.clubName(m.getClubName())
				.clubPublicKey(m.getClubPublicKey())
				.description(m.getDescription())
				.operateTime(m.getOperateTime())
				.location(m.getLocation())
				.phone(m.getPhone())
				.startDate(m.getStartDate())
				.endDate(m.getEndDate())
				.clubUrl(m.getClubUrl())
				.podUrl(m.getPodUrl())
				.valid(m.isValid())
				.imagePath1(getClubAndFileName(m.getImagePath1()))
				.imagePath2(getClubAndFileName(m.getImagePath2()))
				.imagePath3(getClubAndFileName(m.getImagePath3()))
				.imagePath4(getClubAndFileName(m.getImagePath4()))
				.imagePath5(getClubAndFileName(m.getImagePath5()))
				.imagePathCard(getClubAndFileName(m.getImagePathCard()))
				.created(m.getCreated())
				.feeFlag(getFeeFlag(m.getClubSeq()))
				.agentSeq( getAgentNumber(m.getClubSeq()) )
				.localName(getClubMember(m.getClubSeq(),memberDidSeqList.get(0)).getLocalName())
				.extraData(getClubMember(m.getClubSeq(),memberDidSeqList.get(0)).getExtraData())
				.build());
		return clubListDto;
	}
	
	private ClubMember getClubMember(Long clubSeq, Long memberDidSeq) {
		Optional<ClubMember> clubMember = clubMemberRepository.findByClub_ClubSeqAndMemberDid_MemberDidSeq(clubSeq, memberDidSeq);
		if (clubMember.isPresent()) {
			return clubMember.get();
		}
		return null;
	}
	
	private int getAgentNumber(Long clubSeq) {
		Optional<AgentClub> agentClub = agentClubRepository.findByClub_ClubSeq(clubSeq);
		if(agentClub.isPresent()) {
			return  (int) agentClub.get().getAgent().getAgentSeq().longValue() ;
		}
		return 0 ;
	}
	
	@Override
	public Page<ClubListDto> clubLisIssuerMy(MemberDto memberDto, Pageable pageable) {
		log.debug("--------------------------------------------");
		log.debug("내가생성한클럽전체리스트");
		log.debug("--------------------------------------------");

		List<Long> memberDidSeqList = new ArrayList<>();
		List<Long> clubSeqList = new ArrayList<>();
		List<MemberDid> memberDidList = memberDidRepository.findByMember_MemberId(memberDto.getMemberId());
		for (MemberDid item : memberDidList) {
			memberDidSeqList.add(item.getMemberDidSeq());
			//log.info("memberDidList {}", item.getMemberDidSeq());
			List<ClubMember> clubMember = clubMemberRepository
					.findByMemberDid_MemberDidSeqAndClubRole_ClubRoleSeq(item.getMemberDidSeq(), 1L);
			for (ClubMember item2 : clubMember) {
				clubSeqList.add(item2.getClub().getClubSeq());
				//log.info("나의클럽전체리스트 : 클럽ISSUER clubSeq : {}", item2.getClub().getClubSeq());
			}

		}
//		List<Club> clubList = clubRepository.findByMemberDid_MemberDidSeqIn(memberDidSeqList);
//		for(Club item : clubList) {
//			clubSeqList.add(item.getClubSeq());	
//			log.info("나의클럽전체리스트 : 클럽운영자 clubSeq : {}" , item.getClubSeq());
//		}

		Page<Club> clubLists = clubRepository.findByClubSeqIn(clubSeqList, pageable);
		log.info("clubList {}", clubLists.getSize());
		Page<ClubListDto> clubListDto = clubLists.map(m -> ClubListDto.builder().clubSeq(m.getClubSeq())
				.memberDid(m.getMemberDid()).clubRole(getClubRole(m.getClubSeq(), memberDidSeqList))
				.clubCategory(m.getClubCategory()).clubName(m.getClubName()).clubPublicKey(m.getClubPublicKey())
				.description(m.getDescription()).operateTime(m.getOperateTime()).location(m.getLocation())
				.phone(m.getPhone()).startDate(m.getStartDate()).endDate(m.getEndDate()).clubUrl(m.getClubUrl())
				.podUrl(m.getPodUrl()).valid(m.isValid()).imagePath1(getClubAndFileName(m.getImagePath1()))
				.imagePath2(getClubAndFileName(m.getImagePath2())).imagePath3(getClubAndFileName(m.getImagePath3()))
				.imagePath4(getClubAndFileName(m.getImagePath4())).imagePath5(getClubAndFileName(m.getImagePath5()))
				.imagePathCard(getClubAndFileName(m.getImagePathCard()))
				.created(m.getCreated())
				.feeFlag(getFeeFlag(m.getClubSeq()))
				.agentSeq( getAgentNumber(m.getClubSeq()) )
				.localName(getClubMember(m.getClubSeq(),memberDidSeqList.get(0)).getLocalName())
				.extraData(getClubMember(m.getClubSeq(),memberDidSeqList.get(0)).getExtraData())
				.build());
		return clubListDto;
	}
	

	private boolean getFeeFlag(Long clubSeq) {
		Optional<ClubFee> clubFee = clubFeeRepository.findByClub_ClubSeq(clubSeq);
		return clubFee.isPresent() ? true : false;
	}

	

	private ClubRole getClubRole(Long clubSeq, List<Long> memberDidSeqList) {
		Optional<ClubMember> clubMember = clubMemberRepository.findByClub_ClubSeqAndMemberDid_MemberDidSeqIn(clubSeq,
				memberDidSeqList);
		if (clubMember.isPresent()) {
			return clubMember.get().getClubRole();
		} else {
			return null;
		}
	}

	@Override
	public ClubListDto clubListOne(Long clubSeq) {
		log.info("--------------------------------------------");
		log.info("클럽정보");
		log.info("--------------------------------------------");
		String bankName = "";
		Optional<Club> club = clubRepository.findByClubSeq(clubSeq);
		Optional<MemberDid> memberDid = memberDidRepository
				.findByMemberDidSeq(club.get().getMemberDid().getMemberDidSeq());
		Optional<Member> member = memberRepository.findByMemberId(memberDid.get().getMember().getMemberId());
		Optional<AgentClub> agentClub = agentClubRepository.findByClub_ClubSeq(club.get().getClubSeq());
		Optional<ClubFee> clubFee = clubFeeRepository.findByClub_ClubSeq(clubSeq);
		if (clubFee.isPresent()) {
			Optional<Bank> bank = bankRepository.findByBankSeq(clubFee.get().getBank().getBankSeq());
			if (bank.isPresent())
				bankName = bank.get().getDisplay();
		}
		MemberDto clsMemberDto = MemberDto.builder()
				.memberId(member.get().getMemberId())
				.email(member.get().getEmail())
				.memberName(member.get().getMemberName()).mobileNumber(member.get().getMobileNumber())
				.mobileAuthFlag(member.get().isMobileAuthFlag()).deviceId(member.get().getDeviceId())
				.profileFilePath(getFileName(member.get().getProfileFilePath())).build();
		ClubListDto clubListDto = ClubListDto.builder().bankName(bankName).feeFlag(clubFee.isPresent() ? true : false)
				.bankSeq(clubFee.isPresent() ? clubFee.get().getBank().getBankSeq() : 0)
				.clubSeq(club.get().getClubSeq())
				.memberDid(club.get().getMemberDid())
				.clubCategory(club.get().getClubCategory()).clubName(club.get().getClubName())
				.clubPublicKey(club.get().getClubPublicKey()).description(club.get().getDescription())
				.operateTime(club.get().getOperateTime()).location(club.get().getLocation())
				.phone(club.get().getPhone()).startDate(club.get().getStartDate()).endDate(club.get().getEndDate())
				.clubUrl(club.get().getClubUrl()).podUrl(club.get().getPodUrl()).valid(club.get().isValid())
				.imagePath1(getClubAndFileName(club.get().getImagePath1()))
				.imagePath2(getClubAndFileName(club.get().getImagePath2()))
				.imagePath3(getClubAndFileName(club.get().getImagePath3()))
				.imagePath4(getClubAndFileName(club.get().getImagePath4()))
				.imagePath5(getClubAndFileName(club.get().getImagePath5()))
				.imagePathCard(getClubAndFileName(club.get().getImagePathCard()))
				.imageText1(club.get().getImageText1())
				.imageText2(club.get().getImageText2())
				.imageText3(club.get().getImageText3())
				.imageText4(club.get().getImageText4())
				.imageText5(club.get().getImageText5())
				.created(club.get().getCreated())
				.clubFee(clubFee.isPresent() ? clubFee.get() : new ClubFee(null, 0, null, null, null)
				// ClubFee.builder().clubFeeSeq(null).feeValue(0).bankAccount(null).bankOwnerName(null).kakaoCodeLink(null).build()
				).issuer(clsMemberDto)
				.agentSeq( getAgentNumber( club.get().getClubSeq() )) 
				.agentClub(agentClub.isPresent() ? agentClub.get() : null)
				.build();
		return clubListDto;
	}

	private String getFileName(String path) {
		if (path == null || "".equals(path)) {
			return "";
		} else {
			String[] nameArray = path.split("/");
			if (path.split("/").length < 2)
				return "";
			int imageName = nameArray.length - 1;
			int clubName = nameArray.length - 2;
			//log.info("imageName {}", imageName);
			//log.info("clubName {}", clubName);
			return CustomConfig.strDefaultImageUrl + "profile/" + nameArray[imageName];
		}
	}

	@Override
	public Page<AgentClubFeeDto> clubAgentFeeList(Long clubSeq, Pageable pageable) {
		log.info("--------------------------------------------");
		log.info("송금명단리스트");
		log.info("--------------------------------------------");
		Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(clubSeq);
		Page<AgentClubFee> agentClubFee = agentClubFeeRepository
				.findByAgentClub_AgentClubSeq(clsAgentClub.get().getAgentClubSeq(), pageable);

		Page<AgentClubFeeDto> agentClubFeeDto = agentClubFee.map(m -> AgentClubFeeDto.builder()
				.agentClubFeeSeq(m.getAgentClubFeeSeq()).memberName(m.getMemberDid().getMember().getMemberName())
				.agentClub(m.getAgentClub()).memberDid(m.getMemberDid())
				.mobileNubmer(m.getMemberDid().getMember().getMobileNumber())
				.memberGrade(getMemberGrade(clubSeq, m.getMemberDid().getMemberDidSeq())).created(m.getCreated())
				.updated(m.getUpdated()).flag(m.isFlag())
				.totalFeeCount(
						agentClubFeeRepository.countByAgentClub_AgentClubSeq(clsAgentClub.get().getAgentClubSeq()))
				.build());
		return agentClubFeeDto;
	}

	private String getMemberGrade(Long clubSeq, Long didSeq) {
		Optional<ClubMember> clubMember = clubMemberRepository.findByClub_ClubSeqAndMemberDid_MemberDidSeq(clubSeq,
				didSeq);
		if (clubMember.isPresent()) {
			return clubMember.get().getMemberGrade();
		} else {
			return null;
		}
	}

	@Override
	public Page<ClubInvitationDto> clubMemberListInvite(Long clubSeq, Pageable pageable) {
		log.debug("--------------------------------------------");
		log.debug("클럽초대리스트");
		log.debug("--------------------------------------------");
		Page<ClubInvitation> clubInvitationList = clubInvitationRepository.findByClub_ClubSeq(clubSeq, pageable);
		Page<ClubInvitationDto> clubInvitationDto = clubInvitationList
				.map(m -> ClubInvitationDto.builder()
						.memberName(m.getMemberName())
						.memberGrade(m.getMemberGrade())
						.mobileNumber(m.getMobileNumber())
						.confirmFlag(m.isConfirmFlag())
						.valid(m.isValid())
						.dataFromIssuer(m.getDataFromIssuer())
						.expiredDate(m.getExpiredDate())
						.localName(m.getLocalName())
						.extraData(m.getExtraData())
						.build());
		return clubInvitationDto;
	}
	
	
	@Override
	public Integer clubMemberListInviteYesAppNoClubCount(Long clubSeq) {
		log.info("--------------------------------------------");
		log.info("클럽초대앱YES,클럽가입NO리스트");
		log.info("--------------------------------------------");
		List<String> finalMemberList = new ArrayList<>();
		List<ClubInvitation> ciList = clubInvitationRepository.findByClub_ClubSeq(clubSeq);
		for(ClubInvitation item : ciList) {
			Optional<Member> memberMobile = memberRepository.findByMobileNumber(item.getMobileNumber());
			if(memberMobile.isPresent()) {
				Optional<ClubMember> clubMember = clubMemberRepository.findByClubInvitation_ClubInvitationSeq(item.getClubInvitationSeq());
				if(clubMember.isEmpty()) {
					finalMemberList.add(item.getMobileNumber());
				}
			}
		}
		return finalMemberList.size();
	}
	
	@Override
	public Page<ClubInvitationDto> clubMemberListInviteNoAppNoClub(Long clubSeq, Pageable pageable) {
		log.info("--------------------------------------------");
		log.info("클럽초대앱NO,클럽가입NO리스트");
		log.info("--------------------------------------------");
		List<String> finalMemberList = new ArrayList<>();
		List<ClubInvitation> ciList = clubInvitationRepository.findByClub_ClubSeq(clubSeq);
		for(ClubInvitation item : ciList) {
			Optional<Member> memberMobile = memberRepository.findByMobileNumber(item.getMobileNumber());
			if(memberMobile.isPresent()) {
				Optional<ClubMember> clubMember = clubMemberRepository.findByClubInvitation_ClubInvitationSeq(item.getClubInvitationSeq());
				if(clubMember.isPresent()) {
					finalMemberList.add(item.getMobileNumber());
				}
				finalMemberList.add(item.getMobileNumber());
			}
		}
		Page<ClubInvitation> clubInvitationList = null;
		if(finalMemberList.size() > 0) {
			 clubInvitationList = clubInvitationRepository.findByClub_ClubSeqAndMobileNumberNotIn(clubSeq, finalMemberList, pageable);
		}else {
			 clubInvitationList = clubInvitationRepository.findByClub_ClubSeq(clubSeq, pageable);
		}
		
		Page<ClubInvitationDto> clubInvitationDto = clubInvitationList
				.map(m -> ClubInvitationDto.builder()
						.memberName(m.getMemberName())
						.memberGrade(m.getMemberGrade())
						.mobileNumber(m.getMobileNumber())
						.confirmFlag(m.isConfirmFlag())
						.valid(m.isValid())
						.dataFromIssuer(m.getDataFromIssuer())
						.expiredDate(m.getExpiredDate())
						.build());
		return clubInvitationDto;
	}

	@Override
	public Page<ClubMemberDto> clubMemberList(Long clubSeq, Pageable pageable) {
		log.info("--------------------------------------------");
		log.info("클럽멤버리스트");
		log.info("--------------------------------------------");
		Page<ClubMember> clubMemberList = clubMemberRepository.findByClub_ClubSeq(clubSeq, pageable);
		Page<ClubMemberDto> clubMemberDto = clubMemberList.map(m -> ClubMemberDto.builder()
				.clubMemberSeq(m.getClubMemberSeq())
				.email(getMemberInfo(m).getEmail()).memberName(getMemberInfo(m).getMemberName())
				.memberGrade(m.getMemberGrade()).mobileNumber(getMemberInfo(m).getMobileNumber()).valid(m.isValid())
				.memberDataJson(m.getMemberDataJson()).expiredDate(m.getExpiredDate()).created(m.getCreated())
				.updated(m.getUpdated()).did(m.getMemberDid().getDid())
				.profileImage(getFileName(getMemberInfo(m).getProfileFilePath()))
				.localName(m.getLocalName())
				.extraData(m.getExtraData())
				.activeSocketFlag(getMemberInfo(m).isActiveSocketFlag())
				.build());
		return clubMemberDto;
	}

	private Member getMemberInfo(ClubMember clubMember) {
		Optional<MemberDid> memberDid = memberDidRepository
				.findByMemberDidSeq(clubMember.getMemberDid().getMemberDidSeq());
		Optional<Member> member = memberRepository.findByMemberId(memberDid.get().getMember().getMemberId());
		if (member.isPresent()) {
//			String memberMobile = member.get().getMobileNumber();	
//			String email = member.get().getEmail();
//			String memberName = member.get().getMemberName();
//			Optional<ClubInvitation> clubInvitation = clubInvitationRepository.findByMobileNumber(memberMobile);
			return member.get();
		}
		return null;
	}

	@Override
	public Page<NotificationDto> clubNotificationList(MemberDto memberDto, Pageable pageable) {
		log.debug("--------------------------------------------");
		log.debug("클럽알람로그리스트");
		log.debug("--------------------------------------------");

		Optional<Member> member = memberRepository.findByMemberId(memberDto.getMemberId());
		log.info("클럽알람로그리스트 소유자 모바일 : {}", member.get().getMobileNumber());
		Page<Notification> notification = notificationRepository.findByReceiverMemberIdOrReceiverMobileNumber(
				memberDto.getMemberId(), member.get().getMobileNumber(), pageable);
		Page<NotificationDto> notificationDto = notification
				.map(m -> NotificationDto.builder().notificationSeq(m.getNotificationSeq())
						.senderMemberId(m.getSenderMemberId())
						.receiverMemberId(m.getReceiverMemberId())
						.receiverMobileNumber(m.getReceiverMobileNumber())
						.pushLog(filterPushLog(m.getPushLog()))
						.smsLog(filterSmsLog(m.getSmsLog()))
						.emailLog(m.getEmailLog())
						.clubLog(m.getClubLog())
						.build());

		return notificationDto;
	}
	
	private PushLog filterPushLog(PushLog pushLog) {			
		if(pushLog == null) return null; 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTimeBegin = LocalDateTime.parse(LocalDate.now() + " 00:00:00", formatter);
		LocalDateTime dateTimeEnd 	= LocalDateTime.parse(LocalDate.now() + " 23:59:59", formatter);			
		LocalDateTime created = pushLog.getCreated();		
		if(pushLog.getPushType().getPushTypeSeq() == 4) { // 대기일때만 
			if (created.isAfter(dateTimeBegin) && created.isBefore(dateTimeEnd)) {
				return pushLog;
			} 
		}else {
			return pushLog;
		}
		return null;		
	}
	
	private SmsLog filterSmsLog(SmsLog smsLog) {
		if(smsLog == null) return null; 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTimeBegin = LocalDateTime.parse(LocalDate.now() + " 00:00:00", formatter);
		LocalDateTime dateTimeEnd 	= LocalDateTime.parse(LocalDate.now() + " 23:59:59", formatter);			
		LocalDateTime created = smsLog.getCreated();		
	    if("대기".contains(smsLog.getTitle())) {
	    	if (created.isAfter(dateTimeBegin) && created.isBefore(dateTimeEnd)) {
				return smsLog;
			}
	    }else {
	    	return smsLog;
	    }
		return null;
	}

	@Override
	public boolean clubListMyRoleUpdate(ClubDto clubDto) {
		log.info("--------------------------------------------");
		log.info("클럽멤버권한변경");
		log.info("--------------------------------------------");
		Optional<ClubMember> clubMember = clubMemberRepository
				.findByClub_ClubSeqAndMemberDid_MemberDidSeq(clubDto.getClubSeq(), clubDto.getMemberDidSeq());
		if (clubMember.isPresent()) {
			clubMember.get().updateMemberGreade(clubDto.getMemberGrade());
			clubMemberRepository.save(clubMember.get());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean clubValidUpdate(Long clubSeq) {
		Optional<Club> club = clubRepository.findByClubSeq(clubSeq);
		if (club.isPresent()) {
			club.get().updateValid(true);
			clubRepository.save(club.get());
			return true;
		}
		return false;
	}

	@Override
	public boolean clubPushLogUpdate(Long pushLogSeq) {
		// TODO Auto-generated method stub
		Optional<PushLog> pushLog = pushLogRepository.findByPushLogSeq(pushLogSeq);
		if (pushLog.isPresent()) {
			pushLog.get().updateReadflag(true);
			pushLogRepository.save(pushLog.get());
			return true;
		}
		return false;
	}

	@Override
	public boolean emailUpdateAndSend(EmailDto emailDto, String memberId) {
		Optional<Member> member = memberRepository.findByMemberId(memberId);
		if (member.isPresent()) {
			member.get().updateMemberEmail(emailDto.getEmail());
			memberRepository.save(member.get());

			log.info("--------------------------------------------");
			log.info("회원비번이메일전송:message-server");
			log.info("--------------------------------------------");
			List<String> destinList = new ArrayList<>();
			List<String> titleList = new ArrayList<>();
			List<String> messageList = new ArrayList<>();
			List<EmailLog> emailLogList = new ArrayList<>();
			destinList.add(emailDto.getEmail());
			titleList.add("[라임카드] 비밀번호 안내");
			messageList.add("라임카드 관리자용 웹 페이지의 비밀번호가 전송되었습니다.<br>안전한 곳에 보관해두십시오.<br>앱 재설치나 라임카드 복구시 필요한 정보입니다..<br>" + emailDto.getPassword() + "");

			EmailLog emailLog = EmailLog.builder().senderMemberId("avchain.io").receiverMemberId(memberId)
					.title(titleList.get(0)).message(messageList.get(0)).confirmFlag(true).build();
			emailLogRepository.save(emailLog);
			emailLogList.add(emailLog);
			emailService.sendEmail(destinList, titleList, messageList, emailLogList);

			return true;
		}
		return false;
	}

	@Override
	public boolean privateUpdateAndSend(PrivateKeyDto privateKeyDto, String memberId) {
		log.info("--------------------------------------------");
		log.info("회원비번이메일PrivateKey전송");
		log.info("--------------------------------------------");
		Optional<Member> member = memberRepository.findByMemberId(memberId);
		if (member.isPresent()) {
			member.get().updateMemberEmail(privateKeyDto.getEmail());
			memberRepository.save(member.get());

			List<MemberDid> memberDidList = memberDidRepository.findByMember_MemberId(memberId);
			if (memberDidList.size() > 0) {
				MemberDid memberDid = memberDidList.get(0);
				memberDid.updatePrivateKey(privateKeyDto.getMemberPrivateKey());
				memberDidRepository.save(memberDid);
			}

			log.info("--------------------------------------------");
			log.info("회원비번이메일전송:message-server");
			log.info("--------------------------------------------");
			List<String> destinList = new ArrayList<>();
			List<String> titleList = new ArrayList<>();
			List<String> messageList = new ArrayList<>();
			List<EmailLog> emailLogList = new ArrayList<>();
			destinList.add(privateKeyDto.getEmail());
			titleList.add("[라임카드] 비밀번호 안내");
			messageList.add("라임카드 관리자용 웹 페이지의 비밀번호가 전송되었습니다.<br>안전한 곳에 보관해두십시오.<br>앱 재설치나 라임카드 복구시 필요한 정보입니다. " + privateKeyDto.getPassword() + "");
			
			EmailLog emailLog = EmailLog.builder().senderMemberId("avchain.io").receiverMemberId(memberId)
					.title(titleList.get(0)).message(messageList.get(0)).confirmFlag(true).build();
			emailLogRepository.save(emailLog);
			emailLogList.add(emailLog);
			emailService.sendEmail(destinList, titleList, messageList, emailLogList);

			return true;
		}
		return false;
	}

	@Override
	public boolean clubSmsLogUpdate(Long smsLogSeq) {
		Optional<SmsLog> smsLog = smsLogRepository.findBySmsLogSeq(smsLogSeq);
		if (smsLog.isPresent()) {
			smsLog.get().updateReadflag(true);
			smsLogRepository.save(smsLog.get());
			return true;
		}
		return false;
	}

	
	@Override
	public boolean clubAgentWaitingAdd(ClubDto clubDto) {
		log.info("--------------------------------------------");
		log.info("대기자명단추가");
		log.info("--------------------------------------------");
		Optional<MemberDid> clsMemberDid = memberDidRepository.findByDid(clubDto.getDid());
		Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(clubDto.getClubSeq());
		if (clsMemberDid.isPresent() && clsAgentClub.isPresent()) {
			Optional<MemberDid> memberDid = memberDidRepository.findByMemberDidSeq(clsMemberDid.get().getMemberDidSeq());
			Optional<AgentClub> agentClub = agentClubRepository.findByAgentClubSeq(clsAgentClub.get().getAgentClubSeq());
			if (agentClub.isPresent() && memberDid.isPresent()) {
				AgentClubWaiting clsAgentClubWaiting = AgentClubWaiting.builder()
						.agentClub(agentClub.get())
						.memberDid(memberDid.get())
						.flag(clubDto.isFlag())
						.mobileNumber("").build();
				agentClubWaitingRepository.save(clsAgentClubWaiting);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean clubAgentWaitingUpdate(ClubDto clubDto) {
		log.info("--------------------------------------------");
		log.info("대기자명단업데이트");
		log.info("--------------------------------------------");
		
		Optional<MemberDid> clsMemberDid = memberDidRepository.findByDid(clubDto.getDid());
		Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(clubDto.getClubSeq());
		if(clsMemberDid.isPresent() && clsAgentClub.isPresent()) {	
			Optional<MemberDid> memberDid = memberDidRepository.findByMemberDidSeq(clsMemberDid.get().getMemberDidSeq());	
			Optional<AgentClub> agentClub = agentClubRepository.findByAgentClubSeq(clsAgentClub.get().getAgentClubSeq());		
			if(agentClub.isPresent() && memberDid.isPresent()) {					
				Optional<AgentClubWaiting> agentClubWaiting = agentClubWaitingRepository.findByAgentClubWaitingSeq(clubDto.getAgentClubWaitingSeq());
				if(agentClubWaiting.isPresent()){
					agentClubWaiting.get().updateAgentClubWaitingFlag(clubDto.isFlag());
					agentClubWaitingRepository.save(agentClubWaiting.get());
					
					
					log.info("--------------------------------------------");
					log.info("대기자명단중첫번째환자푸시전송");
					log.info("--------------------------------------------");
					
					String str1 = LocalDate.now() + " 00:00:00";
					String str2 = LocalDate.now() + " 23:59:59";
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
					LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);	
					
					log.info("ClubSeq {}", clubDto.getClubSeq());
					log.info("dateTimeBegin {}", dateTimeBegin);
					log.info("dateTimeEnd {}", dateTimeEnd);
					
					List<AgentClubWaiting> list =  agentClubWaitingRepository.findByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlagOrderByAgentClubWaitingSeqAsc( agentClub.get().getAgentClubSeq(),dateTimeBegin, dateTimeEnd , true, true);
					
					log.info("list.size {}", list.size());
					
					if( list.size() > 0 && "".equals( list.get(0).getMobileNumber() == null ? "" : list.get(0).getMobileNumber() ) ){
						// 회원가입된 사람 푸시 보내기 
						log.info("PUSH {}", memberDid.get().getMember().getMemberName());
						log.info("PUSH getAgentClubWaitingSeq {}", list.get(0).getAgentClubWaitingSeq());
						
						
						try {
							Optional<Club> club = clubRepository.findByClubSeq(clubDto.getClubSeq());							
							JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
							Optional<PushType>  pushType = pushTypeRepository.findByPushTypeSeq(4L);
							String message = "[" + club.get().getClubName() + "] " + list.get(0).getMemberDid().getMember().getMemberName() + "님 다음 차례이십니다. 진료실로 입장하십시오.";
							
							log.info("PUSH message {}", message);
							log.info("PUSH senderMemberId {}", club.get().getMemberDid().getMember().getMemberId() );
							log.info("PUSH receiverMemberId {}", list.get(0).getMemberDid().getMember().getMemberId() );
							log.info("PUSH getDeviceId {}", list.get(0).getMemberDid().getMember().getDeviceId() );
							
							int waitingNumber = 0;
							int add = 100;
							List<AgentClubWaiting> waitingNumList = agentClubWaitingRepository
									.findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlagOrderByAgentClubWaitingSeqAsc(agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true);
							for (AgentClubWaiting item : waitingNumList) {
								add++;
								if (item.getMemberDid().getDid().equals(list.get(0).getMemberDid().getDid())) {
									waitingNumber = add;
								}
							}
							
							String json = clsJsonUtil.toString(
									PushDto.builder()
									.title("[라임카드] 대기번호 안내")
									.body(message)	
									.clubName(club.get().getClubName())
									.pushType(pushType.get().getTypeCode())
									.deviceId(list.get(0).getMemberDid().getMember().getDeviceId())
									.clubSeq(clubDto.getClubSeq())
									.publicKey(""+waitingNumber)
									.clubInvitationSeq(null)
									.podUrl(""+1)
									.build());						
							PushLog pushLog = PushLog.builder()
									.pushType(pushType.get())
									.club(club.get())
									.clubNoticeSeq(null)
									.senderMemberId(club.get().getMemberDid().getMember().getMemberId())
									.receiverMemberId(list.get(0).getMemberDid().getMember().getMemberId())
									.title("[라임카드] 대기번호 안내")
									.message(message)
									.linkedUrl("")
									.extraMessage("")
									.confirmFlag(false)
									.readFlag(false)
									.build();					
							pushService.sendPush(json,pushLog);
						} catch (Exception e) {
							e.printStackTrace();					
						}
						
					}else {
						// 회원가입안된 사람 우선 안한다! 
					}
					return true;
				}						
			}
		}
		return false;
	}

	@Override
	public Page<AgentClubWaitingDto> clubAgentWaitingList(Long clubSeq, String selectDate, Pageable pageable) {

		TimeZone timeZone = TimeZone.getDefault();
		//log.info("timeZone {}", timeZone.getID());
		//log.info("--------------------------------------------");
		//log.info("대기자명단리스트");
		//log.info("대기자명단리스트");
		//log.info("--------------------------------------------");
		String str1 = selectDate + " 00:00:00";
		String str2 = selectDate + " 23:59:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
		LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);
		
		Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(clubSeq);
		Page<AgentClubWaiting> agentClubWaiting = agentClubWaitingRepository
				.findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlag(clsAgentClub.get().getAgentClubSeq(), pageable,
						dateTimeBegin, dateTimeEnd, true);		
		
		Page<AgentClubWaitingDto> agentClubWaitingDto = agentClubWaiting.map(m -> AgentClubWaitingDto.builder()
				.agentClubWaitingSeq(m.getAgentClubWaitingSeq())
				.memberName(getUserInfo(m))
				.mobileNumber(m.getMemberDid().getMember().getMobileNumber())
				.agentClub(m.getAgentClub())
				.memberDid(m.getMemberDid())
				.memberGrade(getMemberGrade(clubSeq, m.getMemberDid().getMemberDidSeq()))
				.created(m.getCreated())
				.updated(m.getUpdated())
				.questionnaire(m.getQuestionnaire())
				.waitingFlag(m.isWaitingFlag())
				.displayWaitingNum(getDisplayWaitingNum(m.getAgentClubWaitingSeq(),clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd))
				.completeCount( agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlag(clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true, true)   )
				.waitingCount(  agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlag(clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, false, true)  )
				.flag(m.isFlag())
				.totalWaitingCount( agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlag(clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true) )
				.build());
		return agentClubWaitingDto;
	}
	
	@Override
	public Page<AgentClubWaitingDto> clubAgentReceptionList(Long clubSeq, String selectDate, Pageable pageable) {

		TimeZone timeZone = TimeZone.getDefault();
		//log.info("timeZone {}", timeZone.getID());
		//log.info("--------------------------------------------");
		//log.info("대기자명단리스트");
		//log.info("대기자명단리스트");
		//log.info("--------------------------------------------");
		String str1 = selectDate + " 00:00:00";
		String str2 = selectDate + " 23:59:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
		LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);
		
		Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(clubSeq);
		Page<AgentClubWaiting> agentClubWaiting = agentClubWaitingRepository
				.findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlag(clsAgentClub.get().getAgentClubSeq(), pageable,
						dateTimeBegin, dateTimeEnd, false);		
		
		Page<AgentClubWaitingDto> agentClubWaitingDto = agentClubWaiting.map(m -> AgentClubWaitingDto.builder()
				.agentClubWaitingSeq(m.getAgentClubWaitingSeq())
				.memberName(getUserInfo(m))
				.mobileNumber(m.getMemberDid().getMember().getMobileNumber())
				.agentClub(m.getAgentClub())
				.memberDid(m.getMemberDid())
				.memberGrade(getMemberGrade(clubSeq, m.getMemberDid().getMemberDidSeq()))
				.created(m.getCreated())
				.updated(m.getUpdated())
				.questionnaire(m.getQuestionnaire())
				.waitingFlag(m.isWaitingFlag())
				.displayWaitingNum(getDisplayWaitingNum(m.getAgentClubWaitingSeq(),clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd))
				.completeCount( agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlag(clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true, false)   )
				.waitingCount(  agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlag(clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, false, false)  )
				.flag(m.isFlag())
				.totalWaitingCount( agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlag(clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, false) )
				.build());
		return agentClubWaitingDto;
	}
	
	
	@Override
	public Page<AgentClubWaitingDto> clubAgentReceptionWaitingList(Long clubSeq, String selectDate, Pageable pageable) {

		TimeZone timeZone = TimeZone.getDefault();
		//log.info("timeZone {}", timeZone.getID());
		//log.info("--------------------------------------------");
		//log.info("대기자명단리스트");
		//log.info("대기자명단리스트");
		//log.info("--------------------------------------------");
		String str1 = selectDate + " 00:00:00";
		String str2 = selectDate + " 23:59:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
		LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);
		
		Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(clubSeq);
		Page<AgentClubWaiting> agentClubWaiting = agentClubWaitingRepository
				.findByAgentClub_AgentClubSeqAndCreatedBetween(clsAgentClub.get().getAgentClubSeq(), pageable,
						dateTimeBegin, dateTimeEnd);		
		
		Page<AgentClubWaitingDto> agentClubWaitingDto = agentClubWaiting.map(m -> AgentClubWaitingDto.builder()
				.agentClubWaitingSeq(m.getAgentClubWaitingSeq())
				.memberName(getUserInfo(m))
				.mobileNumber(m.getMemberDid().getMember().getMobileNumber())
				.agentClub(m.getAgentClub())
				.memberDid(m.getMemberDid())
				.memberGrade(getMemberGrade(clubSeq, m.getMemberDid().getMemberDidSeq()))
				.created(m.getCreated())
				.updated(m.getUpdated())
				.questionnaire(m.getQuestionnaire())
				.waitingFlag(m.isWaitingFlag())
				.displayWaitingNum(getDisplayWaitingNum(m.getAgentClubWaitingSeq(),clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd))
				.completeCount( agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlag(clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true)   )
				.waitingCount(  agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlag(clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, false)  )
				.flag(m.isFlag())
				.totalWaitingCount( agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetween(clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd) )				
				.localName(getClubMemberLocalName(clubSeq,m.getMemberDid().getMemberDidSeq()))
				.invitationName(getUserInfoInvitation(getUserMobileNumber(m),clubSeq))
				.invitationLocalName(getClubMemberLocalNameInvitation(clubSeq,getUserMobileNumber(m)))
				.invitationMobileNumber(getUserMobileNumber(m))		
				.invitationClubValid(getValidInfoInvitation(clubSeq,getUserMobileNumber(m)))
				.invitationEmptyLocalNameValid(getValidEmptyLocalNameInvitation(clubSeq,getUserMobileNumber(m)))
				.extraData(getClubMemberExtraData(clubSeq,m.getMemberDid().getMemberDidSeq()))
				.build());
		return agentClubWaitingDto;
	}
	
	private String getUserMobileNumber(AgentClubWaiting agentClubWaiting) {
		if ("".equals(agentClubWaiting.getMobileNumber()) || agentClubWaiting.getMobileNumber() == null) {
			return agentClubWaiting.getMemberDid().getMember().getMobileNumber();
		} else {
			String mobile = agentClubWaiting.getMobileNumber();			
			return mobile;
		}

	}
	private boolean getValidEmptyLocalNameInvitation( Long clubSeq, String  mobileNumber) {
		Optional<ClubInvitation> clubInvitation = clubInvitationRepository.findByMobileNumberAndClub_ClubSeq(mobileNumber,clubSeq);
		if(clubInvitation.isPresent()) {
			if("".equals(clubInvitation.get().getLocalName()) || clubInvitation.get().getLocalName() == null ) {
				return false;
			}else {
				return true;
			}
		}
		return false;
	}
	private boolean getValidInfoInvitation( Long clubSeq, String  mobileNumber) {
		Optional<ClubInvitation> clubInvitation = clubInvitationRepository.findByMobileNumberAndClub_ClubSeq(mobileNumber,clubSeq);
		if(clubInvitation.isPresent()) {
			return clubInvitation.get().isValid();
		}
		return true;
	}
	private String getUserInfoInvitation(String  mobileNumber, Long clubSeq) {
		Optional<ClubInvitation> clubInvitation = clubInvitationRepository.findByMobileNumberAndClub_ClubSeq(mobileNumber,clubSeq);
		if(clubInvitation.isPresent()) {
			return clubInvitation.get().getMemberName();
		}
		return "";
	}
	private String getClubMemberLocalName(Long clubSeq, Long memberDidSeq) {
		Optional<ClubMember> clubMember = clubMemberRepository.findByClub_ClubSeqAndMemberDid_MemberDidSeq(clubSeq, memberDidSeq);
		if (clubMember.isPresent()) {
			return clubMember.get().getLocalName();
		}
		return "";
	}
	private String getClubMemberLocalNameInvitation(Long clubSeq, String mobileNumber) {		
		Optional<ClubInvitation> clubInvitation = clubInvitationRepository.findByMobileNumberAndClub_ClubSeq(mobileNumber,clubSeq);
		if(clubInvitation.isPresent()) {
			return clubInvitation.get().getLocalName();			
		}		
		return "";
	}
	private String getClubMemberExtraData(Long clubSeq, Long memberDidSeq) {
		Optional<ClubMember> clubMember = clubMemberRepository.findByClub_ClubSeqAndMemberDid_MemberDidSeq(clubSeq, memberDidSeq);
		if (clubMember.isPresent()) {
			return clubMember.get().getExtraData();
		}
		return "";
	}
	
	/*
	@Override
	public Page<AgentClubReceptionDto> clubAgentReceptionList(Long clubSeq, String selectDate, Pageable pageable) {

		String str1 = selectDate + " 00:00:00";
		String str2 = selectDate + " 23:59:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
		LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);
		
		log.info("str1  {}", str1);
		log.info("str2  {}", str2);
		
		
		Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(clubSeq);
		
		log.info("clsAgentClub  {}", clsAgentClub.isPresent());
		
		Page<AgentClubReception> agentClubReception = agentClubReceptionRepository
				.findByAgentClub_AgentClubSeqAndCreatedBetween(clsAgentClub.get().getAgentClubSeq(), pageable,
						dateTimeBegin, dateTimeEnd);	
		
		log.info("agentClubReception  {}", agentClubReception.isEmpty());
		
		Page<AgentClubReceptionDto> agentClubReceptionDto = agentClubReception.map(m -> AgentClubReceptionDto.builder()
				.agentClubReceptionSeq(m.getAgentClubReceptionSeq())
				.memberName(getReceptionUserName(m))
				.mobileNubmer(getReceptionUserMobile(m))
				.questionnaire(m.getQuestionnaire())
				.created(m.getCreated())
				.updated(m.getUpdated())
				.flag(m.isFlag())
				.build());
		return agentClubReceptionDto;
	}
	
	private String getReceptionUserName(AgentClubReception agentClubReception) {
		log.info("getReceptionUserName");		
		Optional<Member> member = memberRepository.findByMobileNumber(agentClubReception.getMobileNumber());
		if (member.isPresent()) {
			log.info("getReceptionUserName 1 {}", member.get().getMemberName());
			return member.get().getMemberName();
		}else {
			if("".equals( agentClubReception.getMobileNumber()) || agentClubReception.getMobileNumber()  == null ) {
				log.info("getReceptionUserName 2 {}", agentClubReception.getMemberDid().getMember().getMemberName());
				return agentClubReception.getMemberDid().getMember().getMemberName();
			}else {
				log.info("getReceptionUserName 3 공백");
				return "";
			}
		}		
	}
	
	private String getReceptionUserMobile(AgentClubReception agentClubReception) {			
		log.info("getReceptionUserMobile");		
		if("".equals( agentClubReception.getMobileNumber()) || agentClubReception.getMobileNumber()  == null ) {
			log.info("getReceptionUserMobile 1 {}", agentClubReception.getMemberDid().getMember().getMobileNumber());
			return agentClubReception.getMemberDid().getMember().getMobileNumber();
		}else {
			log.info("getReceptionUserMobile 2 {}", agentClubReception.getMobileNumber());
			return agentClubReception.getMobileNumber();
		}		
	}
*/
	private int getDisplayWaitingNum(Long agentClubWaitingSeq, Long agentClubSeq, LocalDateTime selectDateBegin, LocalDateTime selectDateEnd) {		
		List<AgentClubWaiting> agentClubWaitingList = agentClubWaitingRepository.findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlagOrderByAgentClubWaitingSeqAsc(agentClubSeq,selectDateBegin,selectDateEnd,true);
		int displayWaitingNum = 100;
		for(AgentClubWaiting item : agentClubWaitingList) {
			displayWaitingNum++;
			if(item.getAgentClubWaitingSeq() == agentClubWaitingSeq) {				
				return displayWaitingNum;				
			}
		}		
		return 0;
	}
	
	private String getUserInfo(AgentClubWaiting agentClubWaiting) {
		if ("".equals(agentClubWaiting.getMobileNumber()) || agentClubWaiting.getMobileNumber() == null) {
			return agentClubWaiting.getMemberDid().getMember().getMemberName();
		} else {
			String mobile = agentClubWaiting.getMobileNumber();
			if (mobile.length() >= 4) {
				return mobile.substring(mobile.length() - 4) + "님";
			}
			return agentClubWaiting.getMobileNumber() + "님";
		}

	}
	
	@Override
	public boolean clubAgentMobileReceptionAdd(MobileDto mobileDto) {
		log.info("--------------------------------------------");
		log.info("모바일접수명단추가/ 푸시/문자");
		log.info("--------------------------------------------");
		Optional<Member> member = memberRepository.findByMobileNumber(mobileDto.getMobileNumber());
		Optional<MemberDid> issuerMemberDid = memberDidRepository.findByDid(mobileDto.getIssuerDid());
		if (member.isPresent()) {
			log.info("--------------------------------------------");
			log.info("회원인사람:클럽회원Y/N");
			log.info("--------------------------------------------");
			// 가입된 사람 푸시보내기
			log.info("모바일대기자명단추가/푸시/문자 : 가입된 사람 푸시보내기");
			List<MemberDid> clsMemberDid = memberDidRepository.findByMember_MemberId(member.get().getMemberId());
			Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(mobileDto.getClubSeq());
			if (clsMemberDid.size() > 0 && clsAgentClub.isPresent()) {

				Optional<MemberDid> memberDid = memberDidRepository.findByMemberDidSeq(clsMemberDid.get(0).getMemberDidSeq());
				Optional<AgentClub> agentClub = agentClubRepository.findByAgentClubSeq(clsAgentClub.get().getAgentClubSeq());
				if (agentClub.isPresent() && memberDid.isPresent()) {
					AgentClubWaiting clsAgentClubWaiting = AgentClubWaiting.builder()
							.agentClub(agentClub.get())
							.memberDid(memberDid.get())
							.flag(true)
							.mobileNumber(mobileDto.getMobileNumber())
							.questionnaire(mobileDto.getQuestionnaire().toString())
							.waitingFlag(mobileDto.isWaitingFlag())
							.build();
					agentClubWaitingRepository.save(clsAgentClubWaiting);					
					
					/*
					AgentClubReception clsAgentClubReception = AgentClubReception.builder()
							.agentClub(agentClub.get())
							.memberDid(memberDid.get())
							.questionnaire(mobileDto.getQuestionnaire().toString())
							.flag(true)
							.mobileNumber(mobileDto.getMobileNumber())
							.build();
					agentClubReceptionRepository.save(clsAgentClubReception);
					*/
					try {
						Optional<ClubMember> clubMember = clubMemberRepository.findByClub_ClubSeqAndMemberDid_MemberDidSeq(mobileDto.getClubSeq(),	memberDid.get().getMemberDidSeq());
						if (clubMember.isEmpty()) {
							log.info("클럽멤버아닌사람 초대  PUSH {}", mobileDto.getMobileNumber());
							JsonUtil<ExcelDto> clsJsonUtilIssuer = new JsonUtil<ExcelDto>();
							String jsonIssuer;
							List<List<String>> outer = new ArrayList<>();
							List<String> inner = new ArrayList<>();
							inner.add("회원명");
							inner.add("연락처");
							inner.add("회원구분");
							inner.add("납부여부");
							inner.add("localName");
							outer.add(inner);

							inner = new ArrayList<>();
							inner.add("번호접수");
							inner.add(mobileDto.getMobileNumber());
							inner.add("회원");
							inner.add("");
							inner.add("");
							outer.add(inner);


							jsonIssuer = clsJsonUtilIssuer.toString(ExcelDto.builder()
									.did(issuerMemberDid.get().getDid())
									.clubId(mobileDto.getClubSeq().intValue())
									.sms(true)
									.push(true)
									.kakao(true)
									.excelData(outer)
									.build());
							log.info("--------------------------------------------jsonIssuer {}", jsonIssuer);

							SnubiResponse clsSnubiResponse = HttpService.postExcelIssuerServer(jsonIssuer);
							log.info("--------------------------------------------clsSnubiResponse {}",	clsSnubiResponse);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}	
					
		}else {
			log.info("--------------------------------------------");
			log.info("회원아닌사람:앱초대설치문자");
			log.info("--------------------------------------------");
			// 가입안된 사람 문자보내기
			log.info("모바일대기자명단추가/푸시/문자 : 가입안된 사람 문자보내기");
			Optional<MemberDid> clsMemberDid = memberDidRepository.findByDid(mobileDto.getIssuerDid());
			Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(mobileDto.getClubSeq());
			
			log.info("모바일대기자명단추가/푸시/문자 : clsMemberDid {} ", clsMemberDid.isPresent());
			log.info("모바일대기자명단추가/푸시/문자 : clsAgentClub {} ", clsAgentClub.isPresent());
			
			if (clsMemberDid.isPresent() && clsAgentClub.isPresent()) {
				Optional<MemberDid> memberDid = memberDidRepository.findByMemberDidSeq(clsMemberDid.get().getMemberDidSeq());
				Optional<AgentClub> agentClub = agentClubRepository.findByAgentClubSeq(clsAgentClub.get().getAgentClubSeq());
				
				log.info("모바일대기자명단추가/푸시/문자 : agentClub {} ", agentClub.isPresent());
				log.info("모바일대기자명단추가/푸시/문자 : memberDid {} ", memberDid.isPresent());
				
				if (agentClub.isPresent() && memberDid.isPresent()) {
					
					AgentClubWaiting clsAgentClubWaiting = AgentClubWaiting.builder()
							.agentClub(agentClub.get())
							.memberDid(memberDid.get())
							.flag(true)
							.mobileNumber(mobileDto.getMobileNumber())
							.questionnaire(mobileDto.getQuestionnaire().toString())
							.waitingFlag(mobileDto.isWaitingFlag())
							.build();
					agentClubWaitingRepository.save(clsAgentClubWaiting);	
					/*
					AgentClubReception clsAgentClubReception = AgentClubReception.builder()
							.agentClub(agentClub.get())
							.memberDid(memberDid.get())
							.questionnaire(mobileDto.getQuestionnaire().toString())
							.flag(true)
							.mobileNumber(mobileDto.getMobileNumber())
							.build();
					agentClubReceptionRepository.save(clsAgentClubReception);
					*/					

					try {
						Optional<Club> club = clubRepository.findByClubSeq(mobileDto.getClubSeq());
						JsonUtil<ExcelDto> clsJsonUtilIssuer = new JsonUtil<ExcelDto>();
						String jsonIssuer;
						try {
							List<List<String>> outer = new ArrayList<>();
							List<String> inner = new ArrayList<>();
							inner.add("회원명");
							inner.add("연락처");
							inner.add("회원구분");
							inner.add("납부여부");
							inner.add("localName");
							outer.add(inner);

							inner = new ArrayList<>();
							inner.add("번호접수");
							inner.add(mobileDto.getMobileNumber());
							inner.add("회원");
							inner.add("");
							inner.add("");
							outer.add(inner);

							jsonIssuer = clsJsonUtilIssuer
									.toString(ExcelDto.builder()
											.did(issuerMemberDid.get().getDid())
									.clubId(mobileDto.getClubSeq().intValue())
									.sms(true)
									.push(true)
									.kakao(true)
									.excelData(outer)
									.build());
							log.info("--------------------------------------------jsonIssuer {}", jsonIssuer);

							SnubiResponse clsSnubiResponse = HttpService.postExcelIssuerServer(jsonIssuer);
							log.info("--------------------------------------------clsSnubiResponse {}",	clsSnubiResponse);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		
		return true;
	}
	
	
	@Override
	public boolean clubAgentMobileWaitingAddPushSms(MobileDto mobileDto) {
		log.info("--------------------------------------------");
		log.info("모바일대기자명단추가/푸시/문자");
		log.info("--------------------------------------------");
		boolean isDid = false;
		Optional<Member> member = memberRepository.findByMobileNumber(mobileDto.getMobileNumber());
		Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(4L);
		Optional<MemberDid> issuerMemberDid = memberDidRepository.findByDid(mobileDto.getIssuerDid());
		if (member.isPresent() ) {
			List<MemberDid> clsMemberDid = memberDidRepository.findByMember_MemberId(member.get().getMemberId());
			if(clsMemberDid.size() >0) isDid = true;
		}
		if (member.isPresent() && isDid) {
			// 가입된 사람 푸시보내기
			log.info("모바일대기자명단추가/푸시/문자 : 가입된 사람 푸시보내기");
			List<MemberDid> clsMemberDid = memberDidRepository.findByMember_MemberId(member.get().getMemberId());
			Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(mobileDto.getClubSeq());
			if (clsMemberDid.size() > 0 && clsAgentClub.isPresent()) {

				Optional<MemberDid> memberDid = memberDidRepository.findByMemberDidSeq(clsMemberDid.get(0).getMemberDidSeq());
				Optional<AgentClub> agentClub = agentClubRepository.findByAgentClubSeq(clsAgentClub.get().getAgentClubSeq());
				if (agentClub.isPresent() && memberDid.isPresent()) {
					AgentClubWaiting clsAgentClubWaiting = AgentClubWaiting.builder()
							.agentClub(agentClub.get())
							.memberDid(memberDid.get())
							.flag(true)
							.mobileNumber("")
							.questionnaire(mobileDto.getQuestionnaire().toString())
							.waitingFlag(mobileDto.isWaitingFlag())
							.build();
					agentClubWaitingRepository.save(clsAgentClubWaiting);

					try {
						Optional<ClubMember> clubMember = clubMemberRepository.findByClub_ClubSeqAndMemberDid_MemberDidSeq(mobileDto.getClubSeq(),	memberDid.get().getMemberDidSeq());
						if (clubMember.isEmpty()) {
							log.info("클럽멤버아닌사람 초대  PUSH {}", mobileDto.getMobileNumber());
							JsonUtil<ExcelDto> clsJsonUtilIssuer = new JsonUtil<ExcelDto>();
							String jsonIssuer;
							List<List<String>> outer = new ArrayList<>();
							List<String> inner = new ArrayList<>();
							inner.add("회원명");
							inner.add("연락처");
							inner.add("회원구분");
							inner.add("납부여부");
							inner.add("localName");
							outer.add(inner);

							inner = new ArrayList<>();
							inner.add("번호접수");
							inner.add(mobileDto.getMobileNumber());
							inner.add("회원");
							inner.add("");
							inner.add("");
							outer.add(inner);

							jsonIssuer = clsJsonUtilIssuer.toString(ExcelDto.builder()
									.did(issuerMemberDid.get().getDid())
									.clubId(mobileDto.getClubSeq().intValue())
									.excelData(outer)
									.sms(true)
									.push(true)
									.kakao(true)
									.build());
							log.info("--------------------------------------------jsonIssuer {}", jsonIssuer);

							SnubiResponse clsSnubiResponse = HttpService.postExcelIssuerServer(jsonIssuer);
							log.info("--------------------------------------------clsSnubiResponse {}",	clsSnubiResponse);
							
							
							Optional<ClubInvitation> clubInvitation = clubInvitationRepository.findByMobileNumberAndClub_ClubSeq(mobileDto.getMobileNumber(),mobileDto.getClubSeq());
							log.info("--------------------------------------------clubInvitation.isPresent() {}",clubInvitation.isPresent());
							if (clubInvitation.isPresent()) {
								log.info("클럽대기 초대후 PUSH {},{}", mobileDto.getMobileNumber(), clubInvitation.get().getClubInvitationSeq());
								Optional<Club> club = clubRepository.findByClubSeq(mobileDto.getClubSeq());
								JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
								String str1 = LocalDate.now() + " 00:00:00";
								String str2 = LocalDate.now() + " 23:59:59";
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
								LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
								LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);	
								int waitingNumber = 0;
								int add = 100;
								List<AgentClubWaiting> waitingNumList = agentClubWaitingRepository
										.findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlagOrderByAgentClubWaitingSeqAsc(
												agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true);
								for (AgentClubWaiting item : waitingNumList) {
									add++;
									if (item.getMemberDid().getDid().equals(memberDid.get().getDid())) {
										waitingNumber = add;
									}
								}
								Integer waitingCount = agentClubWaitingRepository
										.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlag(
												clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true, true);		
								String message = "[" + club.get().getClubName() + "] " + member.get().getMemberName() + "님의 대기번호는 "+waitingNumber+"번입니다.";
								String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 대기번호 안내")
										.body(message)
										.clubName(club.get().getClubName())
										.pushType(pushType.get().getTypeCode())
										.deviceId(member.get().getDeviceId())
										.clubSeq(mobileDto.getClubSeq())
										.publicKey(""+waitingNumber)
										.clubInvitationSeq(clubInvitation.get().getClubInvitationSeq())
										.podUrl(""+waitingCount)
										.build());
								PushLog pushLog = PushLog.builder().pushType(pushType.get()).club(club.get())
										.clubNoticeSeq(null).senderMemberId(issuerMemberDid.get().getMember().getMemberId())
										.receiverMemberId(member.get().getMemberId()).title("[라임카드] 대기번호 안내").message(message)
										.linkedUrl("").extraMessage("").confirmFlag(false).readFlag(false).build();
								pushService.sendPush(json, pushLog);
							}								
						}else {
							Optional<ClubInvitation> clubInvitation = clubInvitationRepository.findByMobileNumberAndClub_ClubSeq(mobileDto.getMobileNumber(),mobileDto.getClubSeq());
							log.info("--------------------------------------------clubInvitation.isPresent() {}",clubInvitation.isPresent());
							if (clubInvitation.isPresent()) {
								log.info("클럽대기 클럽회원에게 PUSH {},{}", mobileDto.getMobileNumber(), clubInvitation.get().getClubInvitationSeq());
								Optional<Club> club = clubRepository.findByClubSeq(mobileDto.getClubSeq());
								JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
								String str1 = LocalDate.now() + " 00:00:00";
								String str2 = LocalDate.now() + " 23:59:59";
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
								LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
								LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);								
								int waitingNumber = 0;
								int add = 100;
								List<AgentClubWaiting> waitingNumList = agentClubWaitingRepository
										.findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlagOrderByAgentClubWaitingSeqAsc(
												agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true);
								for (AgentClubWaiting item : waitingNumList) {
									add++;
									if (item.getMemberDid().getDid().equals(memberDid.get().getDid())) {
										waitingNumber = add;
									}
								}
								Integer waitingCount = agentClubWaitingRepository
										.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlag(
												clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true, true);	
								String message = "[" + club.get().getClubName() + "] " + member.get().getMemberName() + "님의 대기번호는 "+waitingNumber+"번입니다.";

								String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 대기번호 안내")
										.body(message)
										.clubName(club.get().getClubName())
										.pushType(pushType.get().getTypeCode())
										.deviceId(member.get().getDeviceId())
										.clubSeq(mobileDto.getClubSeq())
										.clubInvitationSeq(clubInvitation.get().getClubInvitationSeq())
										.publicKey(""+waitingNumber)
										.podUrl(""+waitingCount)
										.build());
								PushLog pushLog = PushLog.builder().pushType(pushType.get()).club(club.get())
										.clubNoticeSeq(null).senderMemberId(issuerMemberDid.get().getMember().getMemberId())
										.receiverMemberId(member.get().getMemberId()).title("[라임카드] 대기번호 안내").message(message)
										.linkedUrl("").extraMessage("").confirmFlag(false).readFlag(false).build();
								pushService.sendPush(json, pushLog);
							}				
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					return true;
				}
			}
		} else {
			// 가입안된 사람 문자보내기
			log.info("모바일대기자명단추가/푸시/문자 : 가입안된 사람 문자보내기");
			Optional<MemberDid> clsMemberDid = memberDidRepository.findByDid(mobileDto.getIssuerDid());
			Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(mobileDto.getClubSeq());
			if (clsMemberDid.isPresent() && clsAgentClub.isPresent()) {
				Optional<MemberDid> memberDid = memberDidRepository.findByMemberDidSeq(clsMemberDid.get().getMemberDidSeq());
				Optional<AgentClub> agentClub = agentClubRepository.findByAgentClubSeq(clsAgentClub.get().getAgentClubSeq());
				if (agentClub.isPresent() && memberDid.isPresent()) {
					AgentClubWaiting clsAgentClubWaiting = AgentClubWaiting.builder()
							.agentClub(agentClub.get())
							.memberDid(memberDid.get())
							.flag(true)
							.mobileNumber(mobileDto.getMobileNumber())
							.questionnaire(mobileDto.getQuestionnaire().toString())
							.waitingFlag(mobileDto.isWaitingFlag())
							.build();
					agentClubWaitingRepository.save(clsAgentClubWaiting);
					
					// 소켓테스트 주석 시작 

					log.info("SMS {}", mobileDto.getMobileNumber());
					List<String> destinList = new ArrayList<>();
					List<String> messageList = new ArrayList<>();
					List<SmsLog> smsLogList = new ArrayList<>();

					try {
						Optional<Club> club = clubRepository.findByClubSeq(mobileDto.getClubSeq());
						JsonUtil<ExcelDto> clsJsonUtilIssuer = new JsonUtil<ExcelDto>();
						String jsonIssuer;
						try {
							List<List<String>> outer = new ArrayList<>();
							List<String> inner = new ArrayList<>();
							inner.add("회원명");
							inner.add("연락처");
							inner.add("회원구분");
							inner.add("납부여부");
							inner.add("localName");
							outer.add(inner);

							inner = new ArrayList<>();
							inner.add("번호접수");
							inner.add(mobileDto.getMobileNumber());
							inner.add("회원");
							inner.add("");
							inner.add("");
							outer.add(inner);

							jsonIssuer = clsJsonUtilIssuer
									.toString(
											ExcelDto.builder()
											.did(issuerMemberDid.get().getDid())
											.clubId(mobileDto.getClubSeq().intValue())
											.excelData(outer)
											.sms(true)
											.push(true)
											.kakao(true)
											.build());
							log.info("--------------------------------------------jsonIssuer {}", jsonIssuer);

							SnubiResponse clsSnubiResponse = HttpService.postExcelIssuerServer(jsonIssuer);
							log.info("--------------------------------------------clsSnubiResponse {}",	clsSnubiResponse);

							Optional<ClubInvitation> clubInvitation = clubInvitationRepository
									.findByMobileNumberAndClub_ClubSeq(mobileDto.getMobileNumber(),
											mobileDto.getClubSeq());
							log.info("--------------------------------------------clubInvitation.isPresent() {}",
									clubInvitation.isPresent());
							if (clubInvitation.isPresent()) {
								String str1 = LocalDate.now() + " 00:00:00";
								String str2 = LocalDate.now() + " 23:59:59";
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
								LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
								LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);

								Integer waitingCount = agentClubWaitingRepository
										.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlag(
												clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true, true);
//								String message = club.get().getClubName() + "의 " + waitingCount
//										+ "번으로 대기등록 되었습니다. https://club.avchain.io/wait/"
//										+ clubInvitation.get().getClubInvitationSeq();
								
								int waitingNumber = 0;
								int add = 100;
								List<AgentClubWaiting> waitingNumList = agentClubWaitingRepository
										.findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlagOrderByAgentClubWaitingSeqAsc(
												agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true);
								for (AgentClubWaiting item : waitingNumList) {
									add++;
									if (item.getMemberDid().getDid().equals(memberDid.get().getDid())) {
										waitingNumber = add;
									}
								}
								
								String message = "[" + club.get().getClubName() + "] " + clubInvitation.get().getMemberName() + "님의 대기번호는 "+waitingNumber+"번입니다. https://club.avchain.io/wait/" + clubInvitation.get().getClubInvitationSeq();
								destinList.add(mobileDto.getMobileNumber());
								messageList.add(message);

								JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
								String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 대기번호 안내")
										.body(message)
										.clubName(club.get().getClubName())
										.pushType(pushType.get().getTypeCode()).deviceId("")
										.clubSeq(mobileDto.getClubSeq())
										.publicKey(""+waitingNumber)
										.podUrl(""+waitingCount)
										.clubInvitationSeq(null)
										.build());

								SmsLog smsLog = SmsLog.builder().club(club.get())
										.senderMemberId(issuerMemberDid.get().getMember().getMemberId())
										.receiverMobileNumber(mobileDto.getMobileNumber()).message(message)
										.confirmFlag(true).title("[라임카드] 대기번호 안내").extraMessage(json).readFlag(false)
										.build();
								smsLogList.add(smsLog);
								smsLogRepository.save(smsLog);
							}

						} catch (Exception e) {
							log.info("--------------------------------------------Exception {}", e.getMessage());
							e.printStackTrace();
						}
						if (destinList.size() > 0) {
							smsService.sendSms(destinList, messageList, smsLogList);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 소켓테스트 주석 종료 
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean qrCodeComplete(QrCodeDto qrCodeDto) {
		log.info("--------------------------------------------");
		log.info("QRCode대기자명단추가완료푸시");
		log.info("--------------------------------------------");

		Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(4L);
		Optional<MemberDid> clsMemberDid = memberDidRepository.findByDid(qrCodeDto.getDid());
		Optional<AgentClub> clsAgentClub = agentClubRepository.findByClub_ClubSeq(qrCodeDto.getClubSeq());
		if (clsMemberDid.isPresent() && clsAgentClub.isPresent()) {
			Optional<MemberDid> memberDid = memberDidRepository.findByMemberDidSeq(clsMemberDid.get().getMemberDidSeq());
			Optional<AgentClub> agentClub = agentClubRepository.findByAgentClubSeq(clsAgentClub.get().getAgentClubSeq());
			if (agentClub.isPresent() && memberDid.isPresent()) {
				log.info("PUSH {}", clsMemberDid.get().getMember().getMemberName());
				int waitingNumber = 0;
				try {
					Optional<Club> club = clubRepository.findByClubSeq(qrCodeDto.getClubSeq());
					JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
					boolean completeFlag = qrCodeDto.isCompleteFlag();
					Integer waitingCount = 0;
					String message = "";
					if (completeFlag) {
						String str1 = LocalDate.now() + " 00:00:00";
						String str2 = LocalDate.now() + " 23:59:59";
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
						LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);

						waitingCount = agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlag(
								clsAgentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true, true);
						log.info("PUSH {}", message);
						//message = club.get().getClubName() + "의 대기번호 " + waitingCount + "번으로 대기등록 되었습니다.";		
						int add = 100;
						List<AgentClubWaiting> waitingNumList = agentClubWaitingRepository
								.findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlagOrderByAgentClubWaitingSeqAsc(
										agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd,true);
						for (AgentClubWaiting item : waitingNumList) {
							add++;
							if (item.getMemberDid().getDid().equals(memberDid.get().getDid())) {
								waitingNumber = add;
							}
						}
						
						message = "[" + club.get().getClubName() + "] " + clsMemberDid.get().getMember().getMemberName() + "님의 대기번호는 "+waitingNumber+"번입니다.";
						
					} else {
						//message = "[" + club.get().getClubName() + "] 대기등록에 실패했습니다. 실패이유는 " + qrCodeDto.getReason();
						message = qrCodeDto.getReason();
					}

					log.info("PUSH message {}", message);
					String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 대기번호 안내").body(message)
							.clubName(club.get().getClubName()).pushType(pushType.get().getTypeCode())
							.deviceId(clsMemberDid.get().getMember().getDeviceId()).clubSeq(qrCodeDto.getClubSeq())
							.publicKey(""+waitingNumber)
							.podUrl(""+waitingCount)
							.clubInvitationSeq(null)
							.build());
					PushLog pushLog = PushLog.builder().pushType(pushType.get()).club(club.get()).clubNoticeSeq(null)
							.senderMemberId(club.get().getMemberDid().getMember().getMemberId())
							.receiverMemberId(clsMemberDid.get().getMember().getMemberId()).title("[라임카드] 대기번호 안내")
							.message(message).linkedUrl("").extraMessage("").confirmFlag(false).readFlag(false).build();
					pushService.sendPush(json, pushLog);
					return true;

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		return false;
	}

	// clubInvitationSeq : 257
	// 클럽이름 / 휴대폰번호 / 당일날 인서트된 순번 / waitingCount
	@Override
	public WaitingDto getWaitingNumber(Long clubInvitationSeq) {
		Integer waitingCount = null;
		Integer waitingNumber = null;
		
		log.info("clubInvitationSeq {}", clubInvitationSeq);
		
		Optional<ClubInvitation> clubInvitation = clubInvitationRepository.findByClubInvitationSeq(clubInvitationSeq);
		
		log.info("getClubSeq {}", clubInvitation.get().getClub().getClubSeq());
		
		Optional<AgentClub> agentClub = agentClubRepository
				.findByClub_ClubSeq(clubInvitation.get().getClub().getClubSeq());
		String str1 = LocalDate.now() + " 00:00:00";
		String str2 = LocalDate.now() + " 23:59:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
		LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);
		
		log.info("getAgentClubSeq {}", agentClub.get().getAgentClubSeq());
		log.info("dateTimeBegin {}", dateTimeBegin);
		log.info("dateTimeEnd {}", dateTimeEnd);
		
		int add = 100;
		List<AgentClubWaiting> waitingNumList = agentClubWaitingRepository.
				findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlagOrderByAgentClubWaitingSeqAsc(agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true);		
		
		log.info("waitingNumList.size() {}", waitingNumList.size());
		
		for (AgentClubWaiting item : waitingNumList) {			
			
			add++;
			try {
				if (item.getMobileNumber().equals(clubInvitation.get().getMobileNumber())) {
					waitingNumber = add;
				}
			}catch(Exception e) {}
		}

		List<AgentClubWaiting> list = agentClubWaitingRepository
				.findByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlagOrderByAgentClubWaitingSeqAsc(
						agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true, true);
		
		log.info("list.size() {}", list.size());
		
		for (AgentClubWaiting item : list) {			
			log.info("item {},{}", item.getMobileNumber(), clubInvitation.get().getMobileNumber());			
			if (null == waitingCount)	waitingCount = 0;
			waitingCount++;
			try {
				if (item.getMobileNumber().equals(clubInvitation.get().getMobileNumber())) {
	
					WaitingDto waitingDto = WaitingDto.builder().waitingCount(waitingCount).waitingNumber(waitingNumber)
							.clubName(agentClub.get().getClub().getClubName())
							.mobileNumber(clubInvitation.get().getMobileNumber()).build();
	
					return waitingDto;
				}
			}catch(Exception e) {}
		}
		return null;
	}

	@Override
	public WaitingDto getWaitingNumberApp(String did, Long clubSeq) {
		Integer waitingCount = null;
		Integer waitingNumber = null;
		Optional<MemberDid> memberDid = memberDidRepository.findByDid(did);
		Optional<AgentClub> agentClub = agentClubRepository.findByClub_ClubSeq(clubSeq);
		String str1 = LocalDate.now() + " 00:00:00";
		String str2 = LocalDate.now() + " 23:59:59";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTimeBegin = LocalDateTime.parse(str1, formatter);
		LocalDateTime dateTimeEnd = LocalDateTime.parse(str2, formatter);
		
		int add = 100;
//		List<AgentClubWaiting> waitingNumList = agentClubWaitingRepository
//				.findByAgentClub_AgentClubSeqAndCreatedBetweenOrderByAgentClubWaitingSeqAsc(
//						agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd);
//		for (AgentClubWaiting item : waitingNumList) {
//			add++;
//			if (item.getMemberDid().getDid().equals(memberDid.get().getDid())) {
//				waitingNumber = add;
//			}
//		}
//		
//		int add = 0;
		List<AgentClubWaiting> waitingNumList = agentClubWaitingRepository
				.findByAgentClub_AgentClubSeqAndCreatedBetweenAndWaitingFlagOrderByAgentClubWaitingSeqAsc(
						agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true);
		for (AgentClubWaiting item : waitingNumList) {
			add++;
			if (item.getMemberDid().getDid().equals(did)) {
				waitingNumber = add;
			}
		}

		List<AgentClubWaiting> list = agentClubWaitingRepository
				.findByAgentClub_AgentClubSeqAndCreatedBetweenAndFlagAndWaitingFlagOrderByAgentClubWaitingSeqAsc(
						agentClub.get().getAgentClubSeq(), dateTimeBegin, dateTimeEnd, true, true);
		WaitingDto waitingDto = null;
		for (AgentClubWaiting item : list) {
			if (null == waitingCount)	waitingCount = 0;
			waitingCount++;
			if (item.getMemberDid().getDid().equals(did)) {
				 waitingDto = WaitingDto.builder()
						.waitingCount(waitingCount)
						.waitingNumber(waitingNumber)
						.clubName(agentClub.get().getClub().getClubName())
						.mobileNumber(memberDid.get().getMember().getMobileNumber()).build();				
			}
		}
		return waitingDto;
	}

	@Override
	public boolean findClubName(String clubName) {		
		return clubRepository.existsByClubNameIgnoreCase(clubName);
	}

	@Override
	public boolean clubAgentUpdate(Long clubSeq,Long AgentSeq) {		
		Optional<AgentClub> agentClub = agentClubRepository.findByClub_ClubSeq(clubSeq);
		if(agentClub.isPresent()) {
			Optional<Agent> agent = agentRepository.findByAgentSeq(AgentSeq);
			agentClub.get().updateAgent(agent.get());
			agentClubRepository.save(agentClub.get());
			return  true;
		}		
		return false;
	}
	
	
	@Override
	public boolean clubAgentWaitingQuestionnaireUpdate(ClubDto clubDto) {
		log.info("--------------------------------------------");
		log.info("대기자명단questionnaire업데이트");
		log.info("--------------------------------------------");
		Optional<AgentClubWaiting> agentClubWaiting = agentClubWaitingRepository.findByAgentClubWaitingSeq(clubDto.getAgentClubWaitingSeq());
		if(agentClubWaiting.isPresent()){
			agentClubWaiting.get().updateAgentClubWaitingQuestionnaireFlag(clubDto.getQuestionnaire().toString());
			agentClubWaitingRepository.save(agentClubWaiting.get());
			return true;
		}
		return false;
	}
	
	@Override
	public boolean clubMemberExtraDataUpdate(ClubDto clubDto) {
		log.info("--------------------------------------------");
		log.info("클럽맴버extra-data업데이트");
		log.info("--------------------------------------------");		
		log.info("클럽맴버extra-data업데이트 getExtraData {}", clubDto.getExtraData());
		
		Optional<ClubMember> clubMember = clubMemberRepository.findByClubMemberSeq(clubDto.getClubMemberSeq());		
		if(clubMember.isPresent()){
			
			ObjectMapper objectMapper = new ObjectMapper();
	        try {
				String json = objectMapper.writeValueAsString(clubDto.getExtraData());
				log.info("클럽맴버extra-data업데이트 json {}", json);
				clubMember.get().updateExtraData(json);
				clubMemberRepository.save(clubMember.get());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return true;
		}
		return false;
	}
	
	
	@Transactional
	@Override
	public boolean afterExcelCreatReInvite(IssuerDto issuerDto) {
		log.info("--------------------------------------------");
		log.info("클럽멤버에게 클럽초대-알람(재초대): PUSH & SMS");
		log.info("--------------------------------------------");
		log.info("issuerDto.getClubSeq() {}", issuerDto.getClubSeq());
		Optional<Club> club = clubRepository.findByClubSeq(issuerDto.getClubSeq());
		//String clubName = club.get().getClubName();
		//List<ClubMember> clubMemberList = clubMemberRepository.findByClub_ClubSeq(issuerDto.getClubSeq());
		List<ClubInvitation> clubInvitationList = clubInvitationRepository
				.findByClub_ClubSeqAndValid(issuerDto.getClubSeq(), true);
		//List<String> memberMobileNumberList = new ArrayList<>();
		List<String> destinList = new ArrayList<>();
		List<String> messageList = new ArrayList<>();
		List<SmsLog> smsLogList = new ArrayList<>();

		Optional<MemberDid> memberDidSender = memberDidRepository.findByMemberDidSeq(club.get().getMemberDid().getMemberDidSeq());
		Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(2L);

		log.info("초대 clubInvitationList.size() {}", clubInvitationList.size());
		int cc = 0;
		List<Long> invitationIdsToUpdate = new ArrayList<>();
		for (ClubInvitation data : clubInvitationList) {
			String inviteMobileNumber = data.getMobileNumber();
			Optional<Member> member = memberRepository.findByMobileNumber(inviteMobileNumber);
			if (member.isPresent() && issuerDto.isPush()) {
				log.info("PUSH {}", inviteMobileNumber);
				try {
				    String pushMessage = "["+club.get().getClubName() + "] " + club.get().getMemberDid().getMember().getMemberName() +"님께서 초대장을 보내셨습니다. 확인해주세요.";
					JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
					String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 초대장. ")
							.body(pushMessage)
							.clubName(club.get().getClubName()).pushType(pushType.get().getTypeCode())
							.deviceId(member.get().getDeviceId()).clubSeq(issuerDto.getClubSeq())
							.publicKey(issuerDto.getPublicKey()).clubInvitationSeq(data.getClubInvitationSeq())
							.podUrl(club.get().getPodUrl()).build());
					PushLog pushLog = PushLog.builder().pushType(pushType.get()).club(club.get()).clubNoticeSeq(null)
							.senderMemberId(memberDidSender.get().getMember().getMemberId())
							.receiverMemberId(member.get().getMemberId()).title("[라임카드] 초대장. ")
							.message(pushMessage).linkedUrl("").extraMessage("")
							.confirmFlag(false).readFlag(false).build();
					pushService.sendPush(json, pushLog);

					invitationIdsToUpdate.add(data.getClubInvitationSeq());
					//data.updateSendFlag(true);
					//clubInvitationRepository.save(data);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if(member.isEmpty() && issuerDto.isSms()){
				//log.info("SMS,ClubCategorySeq {},{},{}", inviteMobileNumber, club.get().getClubCategory().getClubCategorySeq(),cc);
				cc++;
				StringBuffer smsMessage = new StringBuffer();	
				if(club.get().getClubCategory().getClubCategorySeq() == 1) {					
					smsMessage.append( club.get().getMemberDid().getMember().getMemberName()); 
					smsMessage.append( "님께서 ["); 
					smsMessage.append( club.get().getClubName()); 
					smsMessage.append( "]회원증을 발급했습니다. 앱스토어 https://zrr.kr/ErbN"); 
				}else {
					smsMessage.append( club.get().getClubName()+" "); 
					smsMessage.append( club.get().getMemberDid().getMember().getMemberName()); 
					//smsMessage.append( " 원장님께서 진료증을 발급했습니다. 앱스토어 https://zrr.kr/ErbN"); 
					int byteCount = 0;
			        for (char c : smsMessage.toString().toCharArray()) {
			            if (isKorean(c)) {
			                byteCount += 2;
			            } else {
			                byteCount += 1; 
			            }
			        }	
				    if(byteCount <= 25) {
				    	smsMessage.append( "원장님이 진료증을 발급했습니다. 진료증앱 설치 https://zrr.kr/ErbN");
				    }else if(byteCount > 25 && byteCount <= 31) {
				    	smsMessage.append( "원장님이 진료증을 발급했습니다. 앱마켓 https://zrr.kr/ErbN");
				    }else if(byteCount > 31 && byteCount <= 35) {
				    	smsMessage.append( "님이 진료증을 발급했습니다. 앱마켓 https://zrr.kr/ErbN");
				    }else {
				    	smsMessage.append( "님이 진료증을 발급했습니다. https://zrr.kr/ErbN");
				    }
				}
				destinList.add(inviteMobileNumber);
				messageList.add(smsMessage.toString());
				try {
					JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
					String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 초대장. ")
							.body(smsMessage.toString())
							.clubName(club.get().getClubName()).pushType(pushType.get().getTypeCode()).deviceId("")
							.clubSeq(issuerDto.getClubSeq()).publicKey(issuerDto.getPublicKey())
							.clubInvitationSeq(data.getClubInvitationSeq()).podUrl(club.get().getPodUrl()).build());
					SmsLog smsLog = SmsLog.builder().club(club.get())
							.senderMemberId(memberDidSender.get().getMember().getMemberId())
							.receiverMobileNumber(inviteMobileNumber)
							.message(smsMessage.toString()).confirmFlag(true)
							.title("라임카드클럽초대").extraMessage(json).readFlag(false).build();
					smsLogList.add(smsLog);
					invitationIdsToUpdate.add(data.getClubInvitationSeq());
					
					
					//smsLogRepository.save(smsLog);
					//data.updateSendFlag(true);
					//clubInvitationRepository.save(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(invitationIdsToUpdate.size() > 0){
			log.info("invitation sendFlag Update {}", invitationIdsToUpdate.size());
			LocalDateTime currentDateTime = LocalDateTime.now();	        
			clubInvitationRepository.updateSendFlagByIds(true, currentDateTime, invitationIdsToUpdate);
		}
		if (destinList.size() > 0) {
			smsLogRepository.saveAll(smsLogList);
			log.info("SMS -- smsService.sendSms destinList.size() {}", destinList.size());
			log.info("SMS -- smsService.sendSms destinList {}", destinList);
			if(issuerDto.isOnlySaveSms()) {
		    	smsService.onlySaveSms(destinList, messageList, smsLogList);
		    }else {
		    	smsService.sendSms(destinList, messageList, smsLogList);
		    }
		}
		return true;
	}

	
	@Transactional
	@Override
	public boolean afterMobileExcelCreate(IssuerDto issuerDto) {
		log.info("--------------------------------------------");
		log.info("클럽멤버에게 클럽초대-알람: PUSH & SMS afterMobileExcelCreate");
		log.info("--------------------------------------------");
		log.info("ClubSeq() {}", issuerDto.getClubSeq());
		 
		
		Optional<Club> club = clubRepository.findByClubSeq(issuerDto.getClubSeq());
		List<ClubInvitation> clubInvitationList = clubInvitationRepository.findByClub_ClubSeqAndMobileNumber(issuerDto.getClubSeq(), issuerDto.getMobileNumber());
		List<String> destinList = new ArrayList<>();
		List<String> messageList = new ArrayList<>();
		List<SmsLog> smsLogList = new ArrayList<>();

		Optional<MemberDid> memberDidSender = memberDidRepository.findByMemberDidSeq(club.get().getMemberDid().getMemberDidSeq());
		Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(2L);
		log.info("초대 clubInvitationList.size() {}", clubInvitationList.size());
		int cc = 0;
		List<Long> invitationIdsToUpdate = new ArrayList<>();
		for (ClubInvitation data : clubInvitationList) {
			String inviteMobileNumber = data.getMobileNumber();
			Optional<Member> member = memberRepository.findByMobileNumber(inviteMobileNumber);
			if (member.isPresent() && issuerDto.isPush()) {
				log.info("PUSH {}", inviteMobileNumber);
				try {
					StringBuffer pushMessage = new StringBuffer();
					if(club.get().getClubCategory().getClubCategorySeq() == 1) {					
						pushMessage.append( "[" + club.get().getMemberDid().getMember().getMemberName()+"]님께서 ["); 						
						pushMessage.append( club.get().getClubName()); 
						pushMessage.append( "]의 회원증을 발급했습니다. 확인해주세요."); 
					}else {
						pushMessage.append( "[" + club.get().getClubName()+"]의 "); 
						pushMessage.append( "[" + club.get().getMemberDid().getMember().getMemberName()+"]"); 
						pushMessage.append( "원장님께서 진료증을 발급했습니다. 확인해주세요."); 
					}
					
					JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
					String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 초대장. ")
							.body(pushMessage.toString())
							.clubName(club.get().getClubName()).pushType(pushType.get().getTypeCode())
							.deviceId(member.get().getDeviceId()).clubSeq(issuerDto.getClubSeq())
							.publicKey(issuerDto.getPublicKey()).clubInvitationSeq(data.getClubInvitationSeq())
							.podUrl(club.get().getPodUrl()).build());
					PushLog pushLog = PushLog.builder().pushType(pushType.get()).club(club.get()).clubNoticeSeq(null)
							.senderMemberId(memberDidSender.get().getMember().getMemberId())
							.receiverMemberId(member.get().getMemberId()).title("[라임카드] 초대장. ")
							.message(pushMessage.toString()).linkedUrl("").extraMessage("")
							.confirmFlag(false).readFlag(false).build();
					pushService.sendPush(json, pushLog);

					invitationIdsToUpdate.add(data.getClubInvitationSeq());
					//data.updateSendFlag(true);
					//clubInvitationRepository.save(data);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}  else if(member.isEmpty()){
				
				List<Integer> listAllowedKakao = CustomConfig.allowedKakao;
				
				if(	issuerDto.isKakao() && 	listAllowedKakao.contains(Integer.valueOf(issuerDto.getClubSeq().toString())) ){
					try {		
						JsonUtil<KakaoDto> clsJsonUtil = new JsonUtil<KakaoDto>();
						Map<String, String> variables = new HashMap<>();
						variables.put("#{홍길동}", "고객");
						KakaoOptions kakaoOptions = KakaoOptions.builder()
								.pfId("KA01PF240201053021752288WRrkjzrh")
								.templateId("KA01TP240202070740501vhCwAWiohQM")
								.variables(variables)				
								.build();		
						List<Messages> messageLists = new ArrayList<>();
						Messages messages = Messages.builder().to(data.getMobileNumber()).kakaoOptions(kakaoOptions).build();
						messageLists.add(messages);
						String json = clsJsonUtil.toString(
								KakaoDto.builder()
								.messages(messageLists)
								.build());		
						kakaoService.sendKakao(json);
					 }catch(Exception e) {
						 
					 }
				}
				if(issuerDto.isSms()) {
					//log.info("SMS,ClubCategorySeq {},{},{}", inviteMobileNumber, club.get().getClubCategory().getClubCategorySeq(),cc);
					cc++;
					StringBuffer smsMessage = new StringBuffer();
					if(club.get().getClubCategory().getClubCategorySeq() == 1) {					
						smsMessage.append( "[" + club.get().getMemberDid().getMember().getMemberName()+"]님께서 ["); 						
						smsMessage.append( club.get().getClubName()); 
						smsMessage.append( "]의 회원증을 발급했습니다. 앱스토어 https://zrr.kr/ErbN"); 
					}else {
						smsMessage.append( "" + club.get().getClubName()+" "); 
						smsMessage.append( "" + club.get().getMemberDid().getMember().getMemberName()+""); 										
						int byteCount = 0;
				        for (char c : smsMessage.toString().toCharArray()) {
				            if (isKorean(c)) {
				                byteCount += 2;
				            } else {
				                byteCount += 1; 
				            }
				        }	
					    if(byteCount <= 25) {
					    	smsMessage.append( "원장님이 진료증을 발급했습니다. 진료증앱 설치 https://zrr.kr/ErbN");
					    }else if(byteCount > 25 && byteCount <= 31) {
					    	smsMessage.append( "원장님이 진료증을 발급했습니다. 앱마켓 https://zrr.kr/ErbN");
					    }else if(byteCount > 31 && byteCount <= 35) {
					    	smsMessage.append( "님이 진료증을 발급했습니다. 앱마켓 https://zrr.kr/ErbN");
					    }else {
					    	smsMessage.append( "님이 진료증을 발급했습니다. https://zrr.kr/ErbN");
					    }					 
					}				
					
					destinList.add(inviteMobileNumber);
					messageList.add(smsMessage.toString());
					try {
						JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
						String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 초대장. ")
								.body(smsMessage.toString())
								.clubName(club.get().getClubName()).pushType(pushType.get().getTypeCode()).deviceId("")
								.clubSeq(issuerDto.getClubSeq()).publicKey(issuerDto.getPublicKey())
								.clubInvitationSeq(data.getClubInvitationSeq()).podUrl(club.get().getPodUrl()).build());
						SmsLog smsLog = SmsLog.builder().club(club.get())
								.senderMemberId(memberDidSender.get().getMember().getMemberId())
								.receiverMobileNumber(inviteMobileNumber)
								.message(smsMessage.toString()).confirmFlag(true)
								.title("라임카드클럽초대").extraMessage(json).readFlag(false).build();
						smsLogList.add(smsLog);
						//smsLogRepository.save(smsLog);
						invitationIdsToUpdate.add(data.getClubInvitationSeq());
						//data.updateSendFlag(true);
						//clubInvitationRepository.save(data);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
			}
		}
		if(invitationIdsToUpdate.size() > 0){
			log.info("invitation sendFlag Update {}", invitationIdsToUpdate.size());
			LocalDateTime currentDateTime = LocalDateTime.now();	        
			clubInvitationRepository.updateSendFlagByIds(true, currentDateTime, invitationIdsToUpdate);
		}
		if (destinList.size() > 0) {
			smsLogRepository.saveAll(smsLogList);
			log.info("SMS -- smsService.sendSms issuerDto.isOnlySaveSms() {}", issuerDto.isOnlySaveSms());
			log.info("SMS -- smsService.sendSms destinList.size() {}", destinList.size());
			log.info("SMS -- smsService.sendSms destinList {}", destinList);
		    if(issuerDto.isOnlySaveSms()) {
		    	smsService.onlySaveSms(destinList, messageList, smsLogList);
		    }else {
		    	smsService.sendSms(destinList, messageList, smsLogList);
		    }
			
		}
		return true;
	}
	
	
	@Transactional
	@Override
	public boolean afterExcelCreate(IssuerDto issuerDto) {
		log.info("--------------------------------------------");
		log.info("클럽멤버에게 클럽초대-알람: PUSH & SMS");
		log.info("--------------------------------------------");
		log.info("ClubSeq() {}", issuerDto.getClubSeq());
		Optional<Club> club = clubRepository.findByClubSeq(issuerDto.getClubSeq());
		//String clubName = club.get().getClubName();
		//List<ClubMember> clubMemberList = clubMemberRepository.findByClub_ClubSeq(issuerDto.getClubSeq());
		List<ClubInvitation> clubInvitationList = clubInvitationRepository.findByClub_ClubSeqAndSendFlag(issuerDto.getClubSeq(), false);
		//List<String> memberMobileNumberList = new ArrayList<>();
		List<String> destinList = new ArrayList<>();
		List<String> messageList = new ArrayList<>();
		List<SmsLog> smsLogList = new ArrayList<>();

		Optional<MemberDid> memberDidSender = memberDidRepository.findByMemberDidSeq(club.get().getMemberDid().getMemberDidSeq());
		Optional<PushType> pushType = pushTypeRepository.findByPushTypeSeq(2L);
		log.info("초대 clubInvitationList.size() {}", clubInvitationList.size());
		
		
		log.info("issuerDto.isPush() {}", issuerDto.isPush());
		log.info("issuerDto.isSms() {}", issuerDto.isSms());
		
		int cc = 0;
		List<Long> invitationIdsToUpdate = new ArrayList<>();
		for (ClubInvitation data : clubInvitationList) {
			String inviteMobileNumber = data.getMobileNumber();
			Optional<Member> member = memberRepository.findByMobileNumber(inviteMobileNumber);
			if (member.isPresent() && issuerDto.isPush()) {
				log.info("PUSH {}", inviteMobileNumber);
				try {
					//String pushMessage = "["+club.get().getClubName() + "] " + club.get().getMemberDid().getMember().getMemberName() +"님께서 초대장을 보내셨습니다. 확인해주세요.";
					
					StringBuffer pushMessage = new StringBuffer();
					if(club.get().getClubCategory().getClubCategorySeq() == 1) {					
						pushMessage.append( "[" + club.get().getMemberDid().getMember().getMemberName()+"]님께서 ["); 						
						pushMessage.append( club.get().getClubName()); 
						pushMessage.append( "]의 회원증을 발급했습니다. 확인해주세요."); 
					}else {
						pushMessage.append( "[" + club.get().getClubName()+"]의 "); 
						pushMessage.append( "[" + club.get().getMemberDid().getMember().getMemberName()+"]"); 
						pushMessage.append( "원장님께서 진료증을 발급했습니다. 확인해주세요."); 
					}
					
					JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
					String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 초대장. ")
							.body(pushMessage.toString())
							.clubName(club.get().getClubName()).pushType(pushType.get().getTypeCode())
							.deviceId(member.get().getDeviceId()).clubSeq(issuerDto.getClubSeq())
							.publicKey(issuerDto.getPublicKey()).clubInvitationSeq(data.getClubInvitationSeq())
							.podUrl(club.get().getPodUrl()).build());
					PushLog pushLog = PushLog.builder().pushType(pushType.get()).club(club.get()).clubNoticeSeq(null)
							.senderMemberId(memberDidSender.get().getMember().getMemberId())
							.receiverMemberId(member.get().getMemberId()).title("[라임카드] 초대장. ")
							.message(pushMessage.toString()).linkedUrl("").extraMessage("")
							.confirmFlag(false).readFlag(false).build();
					pushService.sendPush(json, pushLog);

					invitationIdsToUpdate.add(data.getClubInvitationSeq());
					//data.updateSendFlag(true);
					//clubInvitationRepository.save(data);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}  else if(member.isEmpty() && issuerDto.isSms()){
				//log.info("SMS,ClubCategorySeq {},{},{}", inviteMobileNumber, club.get().getClubCategory().getClubCategorySeq(),cc);
				cc++;
				StringBuffer smsMessage = new StringBuffer();
				if(club.get().getClubCategory().getClubCategorySeq() == 1) {					
					smsMessage.append( "[" + club.get().getMemberDid().getMember().getMemberName()+"]님께서 ["); 						
					smsMessage.append( club.get().getClubName()); 
					smsMessage.append( "]의 회원증을 발급했습니다. 앱스토어 https://zrr.kr/ErbN"); 
				}else {
					smsMessage.append( "" + club.get().getClubName()+" "); 
					smsMessage.append( "" + club.get().getMemberDid().getMember().getMemberName()+""); 										
					int byteCount = 0;
			        for (char c : smsMessage.toString().toCharArray()) {
			            if (isKorean(c)) {
			                byteCount += 2;
			            } else {
			                byteCount += 1; 
			            }
			        }	
				    if(byteCount <= 25) {
				    	smsMessage.append( "원장님이 진료증을 발급했습니다. 진료증앱 설치 https://zrr.kr/ErbN");
				    }else if(byteCount > 25 && byteCount <= 31) {
				    	smsMessage.append( "원장님이 진료증을 발급했습니다. 앱마켓 https://zrr.kr/ErbN");
				    }else if(byteCount > 31 && byteCount <= 35) {
				    	smsMessage.append( "님이 진료증을 발급했습니다. 앱마켓 https://zrr.kr/ErbN");
				    }else {
				    	smsMessage.append( "님이 진료증을 발급했습니다. https://zrr.kr/ErbN");
				    }					 
				}				
				
				destinList.add(inviteMobileNumber);
				messageList.add(smsMessage.toString());
				try {
					JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();
					String json = clsJsonUtil.toString(PushDto.builder().title("[라임카드] 초대장. ")
							.body(smsMessage.toString())
							.clubName(club.get().getClubName()).pushType(pushType.get().getTypeCode()).deviceId("")
							.clubSeq(issuerDto.getClubSeq()).publicKey(issuerDto.getPublicKey())
							.clubInvitationSeq(data.getClubInvitationSeq()).podUrl(club.get().getPodUrl()).build());
					SmsLog smsLog = SmsLog.builder().club(club.get())
							.senderMemberId(memberDidSender.get().getMember().getMemberId())
							.receiverMobileNumber(inviteMobileNumber)
							.message(smsMessage.toString()).confirmFlag(true)
							.title("라임카드클럽초대").extraMessage(json).readFlag(false).build();
					smsLogList.add(smsLog);
					//smsLogRepository.save(smsLog);
					invitationIdsToUpdate.add(data.getClubInvitationSeq());
					//data.updateSendFlag(true);
					//clubInvitationRepository.save(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		log.info("invitationIdsToUpdate.size() {}", invitationIdsToUpdate.size());
		log.info("destinList.size() {}", destinList.size());
		
		if(invitationIdsToUpdate.size() > 0){
			log.info("invitation sendFlag Update {}", invitationIdsToUpdate.size());
			LocalDateTime currentDateTime = LocalDateTime.now();	        
			clubInvitationRepository.updateSendFlagByIds(true, currentDateTime, invitationIdsToUpdate);
		}
		if (destinList.size() > 0) {
			smsLogRepository.saveAll(smsLogList);
			log.info("SMS -- smsService.sendSms destinList.size() {}", destinList.size());
			log.info("SMS -- smsService.sendSms destinList {}", destinList);
			if(issuerDto.isOnlySaveSms()) {
		    	smsService.onlySaveSms(destinList, messageList, smsLogList);
		    }else {
		    	smsService.sendSms(destinList, messageList, smsLogList);
		    }
		}
		return true;
	}
	
	private static boolean isKorean(char c) {
        return Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES ||
                Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_JAMO ||
                Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO;
    }

	@Autowired JdbcTemplate jdbcTemplate;
	@Autowired PlatformTransactionManager transactionManager;
	
	@Getter @Setter
    public class CsvDemographyData {
        private String patientNumber; 
    	private String patientName;
    	private String mobile;       
    }
	
	
	
	@Override
	public boolean test(int size) {
		StopWatch sw = new StopWatch();	
		String csvFilePath = "/Users/taehoonjang/git/did-server/2023_센트럴제일안과.xls";		
    	List<CsvDemographyData> dataList = new ArrayList<>();	    	
    	try (FileInputStream fileInputStream = new FileInputStream(csvFilePath);
               Workbook workbook = new HSSFWorkbook(fileInputStream)) {    		
               Sheet sheet = workbook.getSheetAt(0);
               int rowIndex = 0;
               for (Row row : sheet) {
            	   if (rowIndex == 0) {
                       rowIndex++;
                       continue;
                   }
            	   CsvDemographyData csvDemographyData = new CsvDemographyData();
            	   for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                       Cell cell = row.getCell(i);
                       switch (i) {
	                       case 0:
	                           csvDemographyData.setPatientNumber(cell.getStringCellValue());
	                           break;
                           case 1:
                               csvDemographyData.setPatientName(cell.getStringCellValue());
                               break;                           
                           case 2:
                               csvDemographyData.setMobile(cell.getStringCellValue().replaceAll("-", "").replaceAll(" ", ""));
                               break;                           
                       }
                   }
            	   if(!"".equals(csvDemographyData))  {
            		   dataList.add(csvDemographyData);
            		   if(rowIndex == size) {
            			   break;
            		   }
            	   }
            	   rowIndex++;
               }

           } catch (IOException e) {
               e.printStackTrace();
           } catch (Exception e) {
               e.printStackTrace();
           }
    	 // 43844
    	 log.info("dataList.size() : {}", dataList.size());    	 
    	     	
    	 sw.start();    
		String insertSql = "INSERT INTO tb_club_invitation (data_from_issuer, mobile_number,extra_data, club_seq, club_role_seq ) VALUES (?, ?, ?, ?, ?)";
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		    @Override
		    protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
		        try {
		            jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
		                @Override
		                public void setValues(PreparedStatement ps, int i) throws SQLException {		                    
		                    ps.setString(1, dataList.get(i).getPatientName());
		                    ps.setString(2, "01000001000");	
		                    ps.setString(3, "//");	
		                    ps.setInt(4, 2);
		                    ps.setInt(5, 2);
		                }
		                @Override
		                public int getBatchSize() {
		                    return dataList.size();
		                }
		            });		         
		            
		            transactionStatus.flush();	
		            
		            List<ClubInvitation> list2 =  clubInvitationRepository.findByClub_ClubSeq(2L);		            
		            log.info("transactionStatus.flush() {}", list2.size());   
		            
		        } catch (Exception e) {
		            throw new RuntimeException("Error committing transaction", e);
		        } 
		    }
		});
		
		log.info("---------------");   
		List<ClubInvitation> list =  clubInvitationRepository.findByClub_ClubSeq(2L);		            
        log.info("transactionStatus.flush() {}", list.size());  
		
		try {		
			
			 JsonUtil<IssuerDto> clsJsonUtil = new JsonUtil<IssuerDto>();	
		        String json = clsJsonUtil.toString(        
			        IssuerDto.builder()
			        .clubSeq(2L)
			        .podUrl("")
			        .publicKey("")
			        .vcSignatureSeq(null)
			        .push(true)
			        .sms(true)
			        .build());
			
			HttpUtilPost<IssuerDto> clsHttpUtilPost = new HttpUtilPost<IssuerDto>();
			clsHttpUtilPost.setStrUrl("http://localhost:30001/club/after/excel/issuer");
			clsHttpUtilPost.setStrAuth(null);
			clsHttpUtilPost.setStrTokenType("Bearer");
			clsHttpUtilPost.setStrCharset("UTF-8");
			clsHttpUtilPost.setStrType("application/json");
			SnubiResponse clsSnubiResponse = clsHttpUtilPost.post(json);
			
			log.info("--------------------------------------------clsSnubiResponse {}", clsSnubiResponse);
			ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(clsSnubiResponse.getStrData().toString());
		    log.info("--------------------------------------------getStrData {}", clsSnubiResponse.getStrData().toString());
			
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}	
		
		sw.stop();
        log.info("성능 측정 걸린시간: {}/ms {}/second", sw.getLastTaskTimeMillis(), sw.getTotalTimeSeconds());
         // 10초 동안 스레드 일시 정지
//        try {
//        	log.info("sleep start --- )");   
//			Thread.sleep(10000);
//			log.info("sleep stop --- )");   
//			//throw new RuntimeException("Error committing transaction");
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
		
        return true;
	}

}
