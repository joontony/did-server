package org.snubi.did.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.snubi.did.main.dto.ClubVisitorBookDto;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.entity.ClubVisitorBook;
import org.snubi.did.main.entity.ClubVisitorBookLike;
import org.snubi.did.main.entity.Member;
import org.snubi.did.main.repository.ClubRepository;
import org.snubi.did.main.repository.ClubVisitorBookLikeRepository;
import org.snubi.did.main.repository.ClubVisitorBookRepository;
import org.snubi.did.main.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitorBookServiceImplement implements VisitorBookService {

	private final ClubRepository clubRepository;	
	private final MemberRepository memberRepository;	
	private final ClubVisitorBookRepository clubVisitorBookRepository;
	private final ClubVisitorBookLikeRepository clubVisitorBookLikeRepository;
	
	@Transactional
	@Override
	public boolean clubVisitorBookCreate(ClubVisitorBookDto clubVisitorBookDto) {
		log.info("--------------------------------------------");
		log.info("방명록등록");
		log.info("--------------------------------------------");		
		ClubVisitorBook clubVisitorBook =  ClubVisitorBook.builder()
				.club(clubRepository.findByClubSeq(clubVisitorBookDto.getClubSeq()).get())
				.visitorMessage(clubVisitorBookDto.getVisitorMessage())				
				.build();		
		clubVisitorBookRepository.save(clubVisitorBook);			
		return true;
	}	
	
		
	

	@Transactional
	@Override
	public boolean clubVisitorBookUpdate(ClubVisitorBookDto clubVisitorBookDto) {
		log.info("--------------------------------------------");
		log.info("방명록수정");
		log.info("--------------------------------------------");
		Optional<ClubVisitorBook> clubVisitorBook = clubVisitorBookRepository.findByClubVisitorBookSeq(clubVisitorBookDto.getClubVisitorBookSeq());
		if(clubVisitorBook.isPresent()) {
			clubVisitorBook.get().updateVisitorBook(clubVisitorBookDto.getVisitorMessage());
			clubVisitorBookRepository.save(clubVisitorBook.get());
		}
		return true;
	}
	
	@Transactional
	@Override
	public boolean clubVisitorBookDelete(Long clubSeq,Long clubVisitorBookSeq) {
		log.info("--------------------------------------------");
		log.info("방명록삭제");
		log.info("--------------------------------------------");
		Optional<ClubVisitorBook> clubVisitorBook = clubVisitorBookRepository.findByClubVisitorBookSeq(clubVisitorBookSeq);
		if(clubVisitorBook.isPresent()) {
			clubVisitorBook.get().deleteVisitorBook(true);
			clubVisitorBookRepository.save(clubVisitorBook.get());
		}
		return true;
	}
	
	@Override
	public Page<ClubVisitorBookDto> clubVisitorBookList(String memberId, Long clubSeq, Pageable pageable) {
		log.info("--------------------------------------------");
		log.info("방명록리스트");
		log.info("--------------------------------------------");
		Page<ClubVisitorBook> clubNotice = clubVisitorBookRepository.findAllByClub_ClubSeq(clubSeq, pageable);
		Page<ClubVisitorBookDto>  clubVisitorBookDtoList = clubNotice.map(
				 m -> ClubVisitorBookDto
				 .builder()	
				 .memberId(m.getCreator())
				 .clubVisitorBookSeq(m.getClubVisitorBookSeq())
				 .memberName(getMemberName(m.getCreator()))
				 .clubSeq(m.getClub().getClubSeq())
				 .visitorMessage(m.getVisitorMessage())
				 .created(m.getCreated().toString())
				 .updated(m.getUpdated().toString())
				 .valid(m.isValid())
				 .likeFlag(getLike(m.getClubVisitorBookSeq(),memberId))
				 .likeTotalCount(getLikeCount(m.getClubVisitorBookSeq()))
				 .likeUserName(getUserName(m.getClubVisitorBookSeq()))
				 .build()
				 );		
		return clubVisitorBookDtoList;
	}
	
	private boolean getLike(Long clubVisitorBookSeq, String memberId) {
		Optional<ClubVisitorBookLike> 	clubVisitorBookLike	= clubVisitorBookLikeRepository.findByClubVisitorBook_ClubVisitorBookSeqAndCreator(clubVisitorBookSeq, memberId);
		if(clubVisitorBookLike.isPresent()) {
			if(clubVisitorBookLike.get().isLikeFlag()) {
				return true;
			}else {
				return false;
			}
			
		}else {
			return false;
		}
	} 
	
	private String getMemberName(String memberId) {
		Optional<Member> member = memberRepository.findByMemberId(memberId);
		if(member.isPresent()) {
			return member.get().getMemberName();
		}
		return "";
	}
	private Integer getLikeCount(Long clubVisitorBookSeq) {
		return clubVisitorBookLikeRepository.countByClubVisitorBook_ClubVisitorBookSeqAndLikeFlag(clubVisitorBookSeq, true);
	}
	private List<String> getUserName(Long clubVisitorBookSeq){
		 List<String> userNameList = new ArrayList<>();
		 List<ClubVisitorBookLike> clubVisitorBookLikeList	= clubVisitorBookLikeRepository.findByClubVisitorBook_ClubVisitorBookSeqAndLikeFlag(clubVisitorBookSeq, true);
		 for(ClubVisitorBookLike item : clubVisitorBookLikeList) {
			 Optional<Member> member = memberRepository.findByMemberId(item.getCreator());
				if(member.isPresent()) {
					userNameList.add(   member.get().getMemberName() );
				}
		 }
		 return userNameList;
	}

	
	@Transactional
	@Override
	public ClubVisitorBookDto clubVisitorBookLike(MemberDto memberDto, Long clubVisitorBookSeq, boolean isLikeFlag) {
		log.info("--------------------------------------------");
		log.info("좋아요등록");
		log.info("--------------------------------------------");		
		Optional<ClubVisitorBookLike> 	clubVisitorBookLike	= clubVisitorBookLikeRepository.findByClubVisitorBook_ClubVisitorBookSeqAndCreator(clubVisitorBookSeq, memberDto.getMemberId());
		if(clubVisitorBookLike.isPresent()) {
			clubVisitorBookLike.get().updateVisitorBookLikeFlag(isLikeFlag);
			clubVisitorBookLikeRepository.save(clubVisitorBookLike.get());
		}else {
			Optional<ClubVisitorBook> clubVisitorBook = clubVisitorBookRepository.findByClubVisitorBookSeq(clubVisitorBookSeq);
			ClubVisitorBookLike clsClubVisitorBookLike = ClubVisitorBookLike
					.builder()
					.clubVisitorBook(clubVisitorBook.get())
					.likeFlag(isLikeFlag)
					.build();
			clubVisitorBookLikeRepository.save(clsClubVisitorBookLike);	
		}	
		Optional<ClubVisitorBook> m = clubVisitorBookRepository.findByClubVisitorBookSeq(clubVisitorBookSeq);
		if(m.isPresent()) {
			ClubVisitorBookDto clubVisitorBookDto =  ClubVisitorBookDto.builder()
					 .memberId(m.get().getCreator())
					 .clubVisitorBookSeq(m.get().getClubVisitorBookSeq())
					 .memberName(getMemberName(m.get().getCreator()))
					 .clubSeq(m.get().getClub().getClubSeq())
					 .visitorMessage(m.get().getVisitorMessage())
					 .created(m.get().getCreated().toString())
					 .updated(m.get().getUpdated().toString())
					 .valid(m.get().isValid())
					 .likeFlag(getLike(m.get().getClubVisitorBookSeq(),memberDto.getMemberId()))
					 .likeTotalCount(getLikeCount(m.get().getClubVisitorBookSeq()))
					 .likeUserName(getUserName(m.get().getClubVisitorBookSeq()))
					.build();
			return clubVisitorBookDto;
		}else {
			return null;
		}
		
		
	}	
	
	
	
	

}
