package org.snubi.did.main.service;

import org.snubi.did.main.dto.AppVersionDto;
import org.snubi.did.main.dto.DidSeqDto;
import org.snubi.did.main.dto.MemberDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MemberService {
	
	MemberDto memberSetMobileAndIdCreate(MemberDto memberDto);
	DidSeqDto memberCreate(MemberDto memberDto) throws Exception;	
	boolean memberMobileAuth(MemberDto memberDto);	
	boolean memberPasswordAuth(MemberDto memberDto);	
	boolean memberProfileUpdate(MultipartFile imagePath, MemberDto memberDto);
	boolean memberMyCardUpdate(MultipartFile imagePath,boolean isFile, MemberDto memberDto);
	MemberDto memberProfile(MemberDto memberDto);
	boolean memberFindPrivateKey(String memberId);
	boolean memberDeviceIdUpdate(MemberDto memberDto);
	boolean memberGetRegister(String mobileNumber);
	boolean memberUpdateregister(String memberId, boolean registerFlag);
	AppVersionDto appVersion(String osName);
}
