package org.snubi.did.main.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.config.CustomConfig;
import org.snubi.did.main.dto.ClubNoticeCommentDto;
import org.snubi.did.main.dto.ClubNoticeCommentReplyDto;
import org.snubi.did.main.dto.ClubNoticeWithCommentDto;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.dto.NoticeCommentDto;
import org.snubi.did.main.entity.Club;
import org.snubi.did.main.entity.ClubMember;
import org.snubi.did.main.entity.ClubNotice;
import org.snubi.did.main.entity.ClubNoticeComment;
import org.snubi.did.main.entity.ClubNoticeCommentReply;
import org.snubi.did.main.entity.ClubNoticeLike;
import org.snubi.did.main.entity.ClubVisitorBookLike;
import org.snubi.did.main.entity.Member;
import org.snubi.did.main.entity.MemberDid;
import org.snubi.did.main.entity.PushLog;
import org.snubi.did.main.entity.PushType;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.message.PushDto;
import org.snubi.did.main.message.PushService;
import org.snubi.did.main.repository.ClubMemberRepository;
import org.snubi.did.main.repository.ClubNoticeCommentReplyRepository;
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
public class NoticeWithCommentServiceImplement implements NoticeWithCommentService {
	private final ClubNoticeRepository clubNoticeRepository;	
	private final ClubRepository clubRepository;	
	private final PushService pushService;
	private final ClubMemberRepository clubMemberRepository;
	private final MemberDidRepository memberDidRepository;
	private final MemberRepository memberRepository;
	private final PushTypeRepository pushTypeRepository;
	private final ClubNoticeLikeRepository clubNoticeLikeRepository;	
	private final ClubNoticeCommentRepository clubNoticeCommentRepository;	
	private final ClubNoticeCommentReplyRepository clubNoticeCommentReplyRepository;	
	
	@Override
	public boolean clubNoticeFileCreate(MultipartFile imagePath, ClubNoticeWithCommentDto clubNoticeWithCommentDto) {		
		log.info("--------------------------------------------");
		log.info("공지사항등록");
		log.info("--------------------------------------------");	
		Optional<Club> club = clubRepository.findByClubSeq(clubNoticeWithCommentDto.getClubSeq());
		if (club.isPresent()) {
			String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
			String folderName = club.get().getClubSeq() + "_clubSeq";
			String fullPath = "";			
			try {
				if (!imagePath.isEmpty()) {
					fullPath = CustomConfig.strNoticeFileUploadPath + "/" + folderName + "/" + timestamp + ".png";
					File tmpFile = new File(fullPath);
					tmpFile.getParentFile().mkdir();
					imagePath.transferTo(tmpFile);					
				}
				boolean fileExists = clubNoticeWithCommentDto.isFileExists();
				if(!fileExists) fullPath = "";				
				ClubNotice clubNotice =  ClubNotice.builder()
						.club(clubRepository.findByClubSeq(clubNoticeWithCommentDto.getClubSeq()).get())
						.noticeMessage(clubNoticeWithCommentDto.getNoticeMessage())
						.noticeTitle(clubNoticeWithCommentDto.getNoticeTitle())
						.noticeUrl(clubNoticeWithCommentDto.getNoticeUrl())
						.imagePath(fullPath)
						.build();		
				ClubNotice clsClubNtice = clubNoticeRepository.save(clubNotice);	
				setPush(clsClubNtice);
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

	@Override
	public boolean clubNoticeFileUpdate(MultipartFile imagePath, ClubNoticeWithCommentDto clubNoticeWithCommentDto) {
		log.info("--------------------------------------------");
		log.info("공지사항수정");
		log.info("--------------------------------------------");
		Optional<ClubNotice> clubNotice = clubNoticeRepository.findByClubNoticeSeq(clubNoticeWithCommentDto.getClubNoticeSeq());
		if(clubNotice.isPresent()) {			
			String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
			String folderName = clubNoticeWithCommentDto.getClubSeq() + "_clubSeq";
			String fullPath = "";			
			try {
				if (!imagePath.isEmpty()) {
					fullPath = CustomConfig.strNoticeFileUploadPath + "/" + folderName + "/" + timestamp + ".png";
					File tmpFile = new File(fullPath);
					tmpFile.getParentFile().mkdir();
					imagePath.transferTo(tmpFile);					
				}else {
					fullPath = clubNotice.get().getImagePath();
				}
				boolean fileExists = clubNoticeWithCommentDto.isFileExists();
				if(!fileExists) {
					fullPath = "";	
					clubNotice.get().updateNotice(clubNoticeWithCommentDto.getNoticeTitle(), clubNoticeWithCommentDto.getNoticeMessage(), clubNoticeWithCommentDto.getNoticeUrl(),fullPath);
				}else {
					clubNotice.get().updateNotice(clubNoticeWithCommentDto.getNoticeTitle(), clubNoticeWithCommentDto.getNoticeMessage(), clubNoticeWithCommentDto.getNoticeUrl(),fullPath);
				}
								
				ClubNotice clsClubNtice = clubNoticeRepository.save(clubNotice.get());					
				
				setPush(clsClubNtice);				
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
							.title("[라임카드] 새소식. ")
							.body("[" + club.get().getClubName() + "]에서 새소식을 보내오셨습니다.")	
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
							.title("[라임카드] 새소식. ")
							.message("[" + club.get().getClubName() + "]에서 새소식을 보내오셨습니다.")
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
	public boolean clubNoticeDelete(Long clubSeq,Long clubNoticeSeq) {
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
	public Page<ClubNoticeWithCommentDto> clubNoticeList(String memberId, Long clubSeq, Pageable pageable) {
		log.info("--------------------------------------------");
		log.info("공지사항리스트");
		log.info("--------------------------------------------");
		Page<ClubNotice> clubNotice = clubNoticeRepository.findAllByClub_ClubSeqAndValid(clubSeq, false, pageable);
		Page<ClubNoticeWithCommentDto>  clubNoticeDtoList = clubNotice.map(
				 m -> ClubNoticeWithCommentDto
				 .builder()	
				 .memberId(m.getCreator())
				 .clubNoticeSeq(m.getClubNoticeSeq())
				 .clubSeq(m.getClub().getClubSeq())
				 .noticeTitle(m.getNoticeTitle())
				 .noticeMessage(m.getNoticeMessage())
				 .noticeUrl(m.getNoticeUrl())
				 .created(m.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
				 .updated(m.getUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
				 .valid(m.isValid())	
				 .imagePath(getClubAndFileName(m.getImagePath()))
				 .fileExists( "".equals(m.getImagePath()) || m.getImagePath() == null ? false : true )
				 .clubNoticeCommentDto(getClubNoticeCommentDto(m.getClubNoticeComment()))
				 .memberName(getMemberName(m.getCreator()))
				 .likeFlag( getLike(m.getClubNoticeSeq(), memberId ) )
				 .likeTotalCount(getLikeCount(m.getClubNoticeSeq()))
				 .likeUserName(getUserName(m.getClubNoticeSeq()))
				 .clubNoticeCommentCount(getClubNoticeCommenCount(m.getClubNoticeComment()))
				 .build()
				 );		
		return clubNoticeDtoList;
	}
	
	private Integer getClubNoticeCommenCount(List<ClubNoticeComment> clubNoticeCommentList) {
		int cnt = 0;
		for(ClubNoticeComment item : clubNoticeCommentList) {
			if(!item.isValid()) {
				cnt++;
			}
		}
		return cnt;
	}
	
	private boolean getLike(Long clubNoticeSeq, String memberId) {
		Optional<ClubNoticeLike> 	clubNoticeLike	= clubNoticeLikeRepository.findByClubNotice_ClubNoticeSeqAndCreator(clubNoticeSeq, memberId);
		if(clubNoticeLike.isPresent()) {
			if(clubNoticeLike.get().isLikeFlag()) {
				return true;
			}else {
				return false;
			}
			
		}else {
			return false;
		}
	} 
	
	private List<ClubNoticeCommentDto> getClubNoticeCommentDto(List<ClubNoticeComment> clubNoticeCommentList) {
		List<ClubNoticeCommentDto> list = new ArrayList<>();
		int max = 0;
		for(ClubNoticeComment item : clubNoticeCommentList) {
			ClubNoticeCommentDto clubNoticeCommentDto = ClubNoticeCommentDto
					.builder()
					.clubNoticeCommentSeq(item.getClubNoticeCommentSeq())
					.noticeComment(item.getNoticeComment())
					.valid(item.isValid())
					.created(item.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
					.updated(item.getUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
					.memberId(item.getCreator())
					.creator(item.getCreator())
					.updater(item.getUpdater())
					.memberName(getMemberName(item.getCreator()))
					.build();
			 list.add(clubNoticeCommentDto);
			 max++;
			 if(max > 1) break;
		}	
		return list;
	}
	private String getMemberName(String memberId) {
		Optional<Member> member = memberRepository.findByMemberId(memberId);
		if(member.isPresent()) {
			return member.get().getMemberName();
		}
		return "";
	}
	private Integer getLikeCount(Long clubNoticeSeq) {
		return clubNoticeLikeRepository.countByClubNotice_ClubNoticeSeqAndLikeFlag(clubNoticeSeq, true);
	}
	private List<String> getUserName(Long clubVisitorBookSeq){
		 List<String> userNameList = new ArrayList<>();
		 List<ClubNoticeLike> clubNoticeLikeList	= clubNoticeLikeRepository.findByClubNotice_ClubNoticeSeqAndLikeFlag(clubVisitorBookSeq, true);
		 for(ClubNoticeLike item : clubNoticeLikeList) {
			 Optional<Member> member = memberRepository.findByMemberId(item.getCreator());
				if(member.isPresent()) {
					userNameList.add(   member.get().getMemberName() );
				}
		 }
		 return userNameList;
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
			log.info("imageName {}", imageName);
			log.info("clubName {}", clubName);
			return CustomConfig.strDefaultImageUrl + "notice/" + nameArray[clubName] + "/" + nameArray[imageName];
		}
	}

	@Override
	public ClubNoticeWithCommentDto clubNoticeLike(MemberDto memberDto, Long clubNoticeSeq, boolean isLikeFlag) {
		log.info("--------------------------------------------");
		log.info("좋아요등록");
		log.info("--------------------------------------------");		
		Optional<ClubNoticeLike> 	clubNoticeLike	= clubNoticeLikeRepository.findByClubNotice_ClubNoticeSeqAndCreator(clubNoticeSeq, memberDto.getMemberId());
		if(clubNoticeLike.isPresent()) {
			clubNoticeLike.get().updateNoticeLikeFlag(isLikeFlag);
			clubNoticeLikeRepository.save(clubNoticeLike.get());
		}else {
			Optional<ClubNotice> clubNotice = clubNoticeRepository.findByClubNoticeSeq(clubNoticeSeq);
			ClubNoticeLike clsClubNoticeLike = ClubNoticeLike
					.builder()
					.clubNotice(clubNotice.get())
					.likeFlag(isLikeFlag)
					.build();
			clubNoticeLikeRepository.save(clsClubNoticeLike);			
		}	
		
		Optional<ClubNotice> m = clubNoticeRepository.findByClubNoticeSeq(clubNoticeSeq);
		if(m.isPresent()) {
			ClubNoticeWithCommentDto clubNoticeWithCommentDto = ClubNoticeWithCommentDto
					 .builder()	
					 .memberId(m.get().getCreator())
					 .clubNoticeSeq(m.get().getClubNoticeSeq())
					 .clubSeq(m.get().getClub().getClubSeq())
					 .noticeTitle(m.get().getNoticeTitle())
					 .noticeMessage(m.get().getNoticeMessage())
					 .noticeUrl(m.get().getNoticeUrl())
					 .created(m.get().getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
					 .updated(m.get().getUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
					 .valid(m.get().isValid())	
					 .imagePath(getClubAndFileName(m.get().getImagePath()))
					 .fileExists( "".equals(m.get().getImagePath()) || m.get().getImagePath() == null ? false : true )
					 .clubNoticeCommentDto(getClubNoticeCommentDto(m.get().getClubNoticeComment()))
					 .memberName(getMemberName(m.get().getCreator()))
					 .likeFlag( getLike(m.get().getClubNoticeSeq(), memberDto.getMemberId() ) )
					 .likeTotalCount(getLikeCount(m.get().getClubNoticeSeq()))
					 .likeUserName(getUserName(m.get().getClubNoticeSeq()))
					 .build();
					return clubNoticeWithCommentDto;
		}else {
			return null;
		}
		
	}

	@Override
	public boolean clubNoticeCommentCreate(ClubNoticeCommentDto clubNoticeCommentDto) {
		log.info("--------------------------------------------");
		log.info("댓글등록");
		log.info("--------------------------------------------");
		Optional<ClubNotice> clubNotice = clubNoticeRepository.findByClubNoticeSeq(clubNoticeCommentDto.getClubNoticeSeq());
		if(clubNotice.isPresent()) {
			ClubNoticeComment clubNoticeComment = ClubNoticeComment
					.builder()
					.clubNotice(clubNotice.get())
					.noticeComment(clubNoticeCommentDto.getNoticeComment())
					.build();
			clubNoticeCommentRepository.save(clubNoticeComment);
			return true;
		}		
		return false;
	}

	@Override
	public NoticeCommentDto clubNoticeCommentUpdate(ClubNoticeCommentDto clubNoticeCommentDto) {
		log.info("--------------------------------------------");
		log.info("댓글수정");
		log.info("--------------------------------------------");
		Optional<ClubNoticeComment> clubNoticeComment = clubNoticeCommentRepository.findByClubNoticeCommentSeq(clubNoticeCommentDto.getClubNoticeCommentSeq());
		if(clubNoticeComment.isPresent()) {
			clubNoticeComment.get().updateNoticeComment(clubNoticeCommentDto.getNoticeComment());
			clubNoticeCommentRepository.save(clubNoticeComment.get());
			return responseNoticeCommentDto(clubNoticeCommentDto.getClubNoticeCommentSeq());
		} 
		return null;
	}
	
	@Transactional
	@Override
	public boolean clubNoticeCommentDelete(Long clubSeq,Long clubNoticeCommentSeq) {
		log.info("--------------------------------------------");
		log.info("댓글삭제");
		log.info("--------------------------------------------");
		Optional<ClubNoticeComment> clubNoticeComment = clubNoticeCommentRepository.findByClubNoticeCommentSeq(clubNoticeCommentSeq);
		if(clubNoticeComment.isPresent()) {
			clubNoticeComment.get().deleteComment(true);
			clubNoticeCommentRepository.save(clubNoticeComment.get());
			return true;
		}
		return false;
	}

	@Override
	public Page<NoticeCommentDto> clubNoticeCommentList(Long clubNoticeSeq, Pageable pageable) {
		log.info("--------------------------------------------");
		log.info("댓글리스트");
		log.info("--------------------------------------------");
		Page<ClubNoticeComment> clubNoticeComment = clubNoticeCommentRepository.findAllByClubNotice_ClubNoticeSeqAndValid(clubNoticeSeq, false, pageable);
		Page<NoticeCommentDto>  clubNoticeDtoList = clubNoticeComment.map(
				 m -> NoticeCommentDto
				 .builder()	
				 .issuerId(m.getClubNotice().getClub().getMemberDid().getMember().getMemberId())
				 .memberId(m.getCreator())	
				 .memberName(getMemberName(m.getCreator()))
				 .clubNoticeCommentSeq(m.getClubNoticeCommentSeq())
				 .clubNoticeSeq(m.getClubNotice().getClubNoticeSeq())
				 .created(m.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
				 .updated(m.getUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
				 .noticeComment(m.getNoticeComment())
				 .clubNoticeCommentReplyDto(   getClubNoticeCommentReplyDto( m.getClubNoticeCommentSeq() ) )
				 .valid(m.isValid())
				 .build()
				 );		
		return clubNoticeDtoList;
	}
	
	private List<ClubNoticeCommentReplyDto> getClubNoticeCommentReplyDto(Long clubNoticeCommentSeq) {
		List<ClubNoticeCommentReply> list = clubNoticeCommentReplyRepository.findByClubNoticeComment_ClubNoticeCommentSeqOrderByClubNoticeCommentReplySeqDesc(clubNoticeCommentSeq);
		List<ClubNoticeCommentReplyDto> clubNoticeCommentReplyDtoList =  new ArrayList<>();
		for(ClubNoticeCommentReply item : list) {
		  if(!item.isValid()) {
			  ClubNoticeCommentReplyDto clubNoticeCommentReplyDto = ClubNoticeCommentReplyDto
						.builder()
						.memberId(item.getCreator())	
						 .memberName(getMemberName(item.getCreator()))
						 .clubNoticeCommentSeq(clubNoticeCommentSeq)					 
						 .clubNoticeCommentReplySeq(item.getClubNoticeCommentReplySeq())
						 .noticeCommentReply(item.getNoticeCommnetReply())
						 .created(item.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
						 .updated(item.getUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
						 .valid(item.isValid())
						.build();
			  clubNoticeCommentReplyDtoList.add(clubNoticeCommentReplyDto);
		  }			
		}				
		return clubNoticeCommentReplyDtoList;
	}
	
	private NoticeCommentDto responseNoticeCommentDto(Long clubNoticeCommentSeq) {
		Optional<ClubNoticeComment> m = clubNoticeCommentRepository.findByClubNoticeCommentSeq(clubNoticeCommentSeq);	
		if(m.isPresent()) {
			NoticeCommentDto noticeCommentDto = NoticeCommentDto
					 .builder()	
					 .memberId(m.get().getCreator())	
					 .memberName(getMemberName(m.get().getCreator()))
					 .clubNoticeCommentSeq(m.get().getClubNoticeCommentSeq())
					 .clubNoticeSeq(m.get().getClubNotice().getClubNoticeSeq())
					 .created(m.get().getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
					 .updated(m.get().getUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
					 .noticeComment(m.get().getNoticeComment())
					 .clubNoticeCommentReplyDto(   getClubNoticeCommentReplyDto( m.get().getClubNoticeCommentSeq() ) )
					 .valid(m.get().isValid())
					 .build();					
					return noticeCommentDto;
		}else {
			return null;
		}
		
	}

	@Override
	public NoticeCommentDto clubNoticeCommentReplyCreate(ClubNoticeCommentReplyDto clubNoticeCommentReplyDto) {
		log.info("--------------------------------------------");
		log.info("대댓글등록");
		log.info("--------------------------------------------");
		Optional<ClubNoticeComment> clubNoticeComment = clubNoticeCommentRepository.findByClubNoticeCommentSeq(clubNoticeCommentReplyDto.getClubNoticeCommentSeq());		
		if(clubNoticeComment.isPresent()) {
			ClubNoticeCommentReply clubNoticeCommentReply = ClubNoticeCommentReply
					.builder()
					.clubNoticeComment(clubNoticeComment.get())
					.noticeCommnetReply(clubNoticeCommentReplyDto.getNoticeCommentReply())
					.build();
			clubNoticeCommentReplyRepository.save(clubNoticeCommentReply);			
			return responseNoticeCommentDto(clubNoticeCommentReplyDto.getClubNoticeCommentSeq());
		}		
		return null;
	}

	@Override
	public NoticeCommentDto clubNoticeCommentReplyUpdate(ClubNoticeCommentReplyDto clubNoticeCommentReplyDto) {
		log.info("--------------------------------------------");
		log.info("대댓글수정");
		log.info("--------------------------------------------");
		Optional<ClubNoticeCommentReply> clubNoticeCommentReply = clubNoticeCommentReplyRepository.findByClubNoticeCommentReplySeq(clubNoticeCommentReplyDto.getClubNoticeCommentReplySeq());
		if(clubNoticeCommentReply.isPresent()) {
			clubNoticeCommentReply.get().updateNoticeCommnetReplyh(clubNoticeCommentReplyDto.getNoticeCommentReply());
			clubNoticeCommentReplyRepository.save(clubNoticeCommentReply.get());
			return responseNoticeCommentDto(clubNoticeCommentReply.get().getClubNoticeComment().getClubNoticeCommentSeq());
		} 
		return null;
	}

	@Override
	public boolean clubNoticeCommentReplyDelete(Long clubSeq, Long clubNoticeCommentReplySeq) {
		log.info("--------------------------------------------");
		log.info("대댓글삭제");
		log.info("--------------------------------------------");
		Optional<ClubNoticeCommentReply> clubNoticeCommentReply = clubNoticeCommentReplyRepository.findByClubNoticeCommentReplySeq(clubNoticeCommentReplySeq);
		if(clubNoticeCommentReply.isPresent()) {
			clubNoticeCommentReply.get().deleteComment(true);
			clubNoticeCommentReplyRepository.save(clubNoticeCommentReply.get());
			return true;
		}
		return true;
	}
}
