package org.snubi.did.main.service;

import java.util.Optional;

import org.snubi.did.main.dto.ClubVisitorBookDto;
import org.snubi.did.main.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface VisitorBookService {
	boolean clubVisitorBookCreate(ClubVisitorBookDto clubVisitorBookDto);
	boolean clubVisitorBookUpdate(ClubVisitorBookDto clubVisitorBookDto);
	boolean clubVisitorBookDelete(Long clubSeq,Long clubVisitorBookSeq);
	Page<ClubVisitorBookDto> clubVisitorBookList(String memberId, Long clubSeq, Pageable pageable);	
	ClubVisitorBookDto clubVisitorBookLike(MemberDto memberDto,Long clubVisitorBookSeq, boolean isLikeFlag);
}
