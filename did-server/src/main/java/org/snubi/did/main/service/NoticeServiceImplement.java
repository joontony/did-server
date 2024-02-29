package org.snubi.did.main.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.config.CustomConfig;
import org.snubi.did.main.dto.ClubNoticeCommentDto;
import org.snubi.did.main.dto.ClubNoticeDto;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.entity.Club;
import org.snubi.did.main.entity.ClubMember;
import org.snubi.did.main.entity.ClubNotice;
import org.snubi.did.main.entity.ClubNoticeComment;
import org.snubi.did.main.entity.ClubNoticeLike;
import org.snubi.did.main.entity.Member;
import org.snubi.did.main.entity.MemberDid;
import org.snubi.did.main.entity.PushLog;
import org.snubi.did.main.entity.PushType;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.message.PushDto;
import org.snubi.did.main.message.PushService;
import org.snubi.did.main.repository.ClubMemberRepository;
import org.snubi.did.main.repository.ClubNoticeCommentRepository;
import org.snubi.did.main.repository.ClubNoticeLikeRepository;
import org.snubi.did.main.repository.ClubNoticeRepository;
import org.snubi.did.main.repository.ClubRepository;
import org.snubi.did.main.repository.MemberDidRepository;
import org.snubi.did.main.repository.MemberRepository;
import org.snubi.did.main.repository.PushTypeRepository;
import org.snubi.lib.json.JsonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImplement implements NoticeService {

//	private final ClubNoticeRepository clubNoticeRepository;	
//	private final ClubRepository clubRepository;	
//	private final PushService pushService;
//	private final ClubMemberRepository clubMemberRepository;
//	private final MemberDidRepository memberDidRepository;
//	private final MemberRepository memberRepository;
//	private final PushTypeRepository pushTypeRepository;
//	private final ClubNoticeLikeRepository clubNoticeLikeRepository;	
//	private final ClubNoticeCommentRepository clubNoticeCommentRepository;	
	
	private final ClubNoticeRepository clubNoticeRepository;	
	private final ClubRepository clubRepository;	
	private final PushService pushService;
	private final ClubMemberRepository clubMemberRepository;
	private final MemberDidRepository memberDidRepository;
	private final MemberRepository memberRepository;
	private final PushTypeRepository pushTypeRepository;
	
	@Transactional
	@Override
	public boolean clubNoticeCreate(ClubNoticeDto clubNoticeDto) {
		log.info("--------------------------------------------");
		log.info("공지사항등록");
		log.info("--------------------------------------------");		
		ClubNotice clubNotice =  ClubNotice.builder()
				.club(clubRepository.findByClubSeq(clubNoticeDto.getClubSeq()).get())
				.noticeMessage(clubNoticeDto.getNoticeMessage())
				.noticeTitle(clubNoticeDto.getNoticeTitle())
				.noticeUrl(clubNoticeDto.getNoticeUrl())
				.build();		
		ClubNotice clsClubNtice = clubNoticeRepository.save(clubNotice);	
		setPush(clsClubNtice);
		return true;
	}	
	
	private void setPush(ClubNotice clubNotice) {		
		log.info("--------------------------------------------");
		log.info("공지사항등록-알람");
		log.info("--------------------------------------------");	
		List<ClubMember> clubMemberList = clubMemberRepository.findByClub_ClubSeq(clubNotice.getClub().getClubSeq());
		for(ClubMember item : clubMemberList) {
			Optional<Club> club = clubRepository.findByClubSeq(clubNotice.getClub().getClubSeq());
			Optional<MemberDid> memberDidSender = memberDidRepository.findByMemberDidSeq(club.get().getMemberDid().getMemberDidSeq());	
			Optional<MemberDid> memberDidReceiver = memberDidRepository.findByMemberDidSeq(item.getMemberDid().getMemberDidSeq());			
			Optional<Member> member = memberRepository.findByMemberId(memberDidReceiver.get().getMember().getMemberId());
			Optional<PushType>  pushType = pushTypeRepository.findByPushTypeSeq(3L);
			if(member.isPresent()) {								
				try {
					JsonUtil<PushDto> clsJsonUtil = new JsonUtil<PushDto>();	
					String json = clsJsonUtil.toString(
							PushDto.builder()
							.title("라임카드클럽새소식")
							.body("["+club.get().getClubName()+"]의 새소식을 확인해보세요.")	
							.clubName(club.get().getClubName())
							.pushType(pushType.get().getTypeCode())
							.deviceId(member.get().getDeviceId())
							.clubSeq(clubNotice.getClub().getClubSeq())
							.build());
					
					PushLog pushLog = PushLog.builder()
							.pushType(pushType.get())
							.club(club.get())
							.clubNoticeSeq(clubNotice.getClubNoticeSeq())
							.senderMemberId(memberDidSender.get().getMember().getMemberId())
							.receiverMemberId(memberDidReceiver.get().getMember().getMemberId())
							.title("라임카드클럽새소식")
							.message("["+club.get().getClubName()+"]의 새소식을 확인해보세요.")
							.linkedUrl("")
							.extraMessage("")
							.confirmFlag(false)
							.build();
					
					pushService.sendPush(json,pushLog);						
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		}
	}
	
	

	@Transactional
	@Override
	public boolean clubNoticeUpdate(ClubNoticeDto clubNoticeDto) {
		log.info("--------------------------------------------");
		log.info("공지사항수정");
		log.info("--------------------------------------------");
		Optional<ClubNotice> clubNotice = clubNoticeRepository.findByClubNoticeSeq(clubNoticeDto.getClubNoticeSeq());
		if(clubNotice.isPresent()) {
			// 신규버전 때문에 주석 잡음 
			//clubNotice.get().updateNotice(clubNoticeDto.getNoticeTitle(), clubNoticeDto.getNoticeMessage(), clubNoticeDto.getNoticeUrl());
			//clubNoticeRepository.save(clubNotice.get());
		}
		return true;
	}
	
	@Transactional
	@Override
	public boolean clubNoticeUDelete(Long clubSeq,Long clubNoticeSeq) {
		log.info("--------------------------------------------");
		log.info("공지사항삭제");
		log.info("--------------------------------------------");
		Optional<ClubNotice> clubNotice = clubNoticeRepository.findByClubNoticeSeq(clubNoticeSeq);
		if(clubNotice.isPresent()) {
			clubNotice.get().deleteNotice(true);
			clubNoticeRepository.save(clubNotice.get());
		}
		return true;
	}
	
	@Override
	public Page<ClubNoticeDto> clubNoticeList(Long clubSeq, Pageable pageable) {
		log.info("--------------------------------------------");
		log.info("공지사항리스트");
		log.info("--------------------------------------------");
		Page<ClubNotice> clubNotice = clubNoticeRepository.findAllByClub_ClubSeq(clubSeq, pageable);
		Page<ClubNoticeDto>  clubNoticeDtoList = clubNotice.map(
				 m -> ClubNoticeDto
				 .builder()	
				 .clubNoticeSeq(m.getClubNoticeSeq())
				 .clubSeq(m.getClub().getClubSeq())
				 .noticeTitle(m.getNoticeTitle())
				 .noticeMessage(m.getNoticeMessage())
				 .noticeUrl(m.getNoticeUrl())
				 .created(m.getCreated().toString())
				 .updated(m.getUpdated().toString())
				 .valid(m.isValid())
				 .build()
				 );		
		return clubNoticeDtoList;
	}
	
	

	
	
/*
	@Transactional
	@Override
	public boolean clubNoticeWrite(ClubNoticeDto clubNoticeDto) {
		log.info("--------------------------------------------");
		log.info("공지사항등록");
		log.info("--------------------------------------------");		
		ClubNotice clubNotice =  ClubNotice.builder()
				.club(clubRepository.findByClubSeq(clubNoticeDto.getClubSeq()).get())
				.noticeMessage(clubNoticeDto.getNoticeMessage())
				.noticeTitle(clubNoticeDto.getNoticeTitle())
				.noticeUrl(clubNoticeDto.getNoticeUrl())
				.build();		
		ClubNotice clsClubNtice = clubNoticeRepository.save(clubNotice);	
		setPush(clsClubNtice);
		return true;
	}	


	@Transactional
	@Override
	public boolean clubNoticeUpdate(ClubNoticeDto clubNoticeDto) {
		log.info("--------------------------------------------");
		log.info("공지사항수정");
		log.info("--------------------------------------------");
		Optional<ClubNotice> clubNotice = clubNoticeRepository.findByClubNoticeSeq(clubNoticeDto.getClubNoticeSeq());
		if(clubNotice.isPresent()) {
			clubNotice.get().updateNotice(clubNoticeDto.getNoticeTitle(), clubNoticeDto.getNoticeMessage(), clubNoticeDto.getNoticeUrl(),"");
			clubNoticeRepository.save(clubNotice.get());
		}
		return true;
	}
*/	
	

	
	
	
	
	
	

}
