package org.snubi.did.main.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.snubi.did.main.blockchain.BlockChain;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.config.CustomConfig;
import org.snubi.did.main.dto.AppVersionDto;
import org.snubi.did.main.dto.DidDto;
import org.snubi.did.main.dto.DidSeqDto;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.dto.VcDocumentDto;
import org.snubi.did.main.entity.AppVersion;
import org.snubi.did.main.entity.ChainNode;
import org.snubi.did.main.entity.Member;
import org.snubi.did.main.entity.MemberAccount;
import org.snubi.did.main.entity.MemberDid;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.message.SmsService;
import org.snubi.did.main.repository.AppVersionRepository;
import org.snubi.did.main.repository.ChainNodeRepository;
import org.snubi.did.main.repository.MemberAccountRepository;
import org.snubi.did.main.repository.MemberDidRepository;
import org.snubi.did.main.repository.MemberRepository;
import org.snubi.lib.date.DateUtil;
import org.snubi.lib.json.JsonUtil;
import org.snubi.lib.response.SnubiResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImplement implements MemberService, NetworkService {

	private final MemberRepository memberRepository;	
	private final ChainNodeRepository chainNodeRepository;	
	private final MemberAccountRepository memberAccountRepository;
	private final MemberDidRepository memberDidRepository;
	private final SmsService smsService;
	private final AppVersionRepository appVersionRepository;
	
	@Transactional
	@Override
	public MemberDto memberSetMobileAndIdCreate(MemberDto memberDto)  {
		log.info("--------------------------------------------");
		log.info("회원가입-핸드폰-사용자아이디생성 ");
		log.info("--------------------------------------------");			
		int randomPIN4 = (int) (Math.random() * 9000) + 1000;
		String idTail = String.valueOf(randomPIN4);
		
		String extractedNumbers = memberDto.getMobileNumber().substring(3, 7);
		String firstThreeDigits = memberDto.getMobileNumber().substring(0, 3);		
		int extractedValue = Integer.parseInt(extractedNumbers);		
	    if (extractedValue >= 1 && extractedValue <= 1999 || "070".equals(firstThreeDigits) ) {
	    	throw new CustomException(ErrorCode.NOT_USE_ERROR);	
	    }else if(extractedValue == 0) {
	    	idTail="1004";
	    }			
		
//		if(
//				"01000001000".equals(memberDto.getMobileNumber()) ||
//				"01000001001".equals(memberDto.getMobileNumber()) ||
//				"01000001002".equals(memberDto.getMobileNumber()) ||
//				"01000001003".equals(memberDto.getMobileNumber()) ||
//				"01000001004".equals(memberDto.getMobileNumber()) ||
//				"01000001005".equals(memberDto.getMobileNumber()) ||
//				"01000001006".equals(memberDto.getMobileNumber()) ||
//				"01000001007".equals(memberDto.getMobileNumber()) ||
//				"01000001008".equals(memberDto.getMobileNumber()) ||
//				"01000001009".equals(memberDto.getMobileNumber()) ||
//				"01000001010".equals(memberDto.getMobileNumber()) ||
//				"01000006795".equals(memberDto.getMobileNumber()) ||
//				"01000001871".equals(memberDto.getMobileNumber()) ||
//				"01000007478".equals(memberDto.getMobileNumber()) ||
//				"01000007510".equals(memberDto.getMobileNumber()) ||
//				"01000009503".equals(memberDto.getMobileNumber()) ||
//				"01000005765".equals(memberDto.getMobileNumber()) ||
//				"01000008764".equals(memberDto.getMobileNumber()) ||
//				"01000009522".equals(memberDto.getMobileNumber()) ||
//				"01000002227".equals(memberDto.getMobileNumber()) ||
//				"01000008711".equals(memberDto.getMobileNumber()) 
//		) {
//			idTail="1004";
//		}
		String autoId = "ID" + DateUtil.getThisDateString("yyyyMMddHHmmss") + idTail;	
		Optional<Member> memberMobile = memberRepository.findByMobileNumber(memberDto.getMobileNumber());
		if(memberMobile.isPresent()) {
			memberRepository.delete(memberMobile.get());
		}
		
		Member member = Member.builder()
				.memberId(autoId)
				.mobileNumber(memberDto.getMobileNumber())
				.mobileAuthNumber(idTail)				
				.build();
		memberRepository.save(member);		
		memberDto.setMemberId(autoId);
		memberDto.setMobileAuthNumber(idTail);		
		
		List<String> destin = new ArrayList<>();
		destin.add(memberDto.getMobileNumber());
		
		List<String> message = new ArrayList<>();
		message.add("[라임카드]의 핸드폰 인증번호는 " + idTail + " 입니다.");
		
		smsService.sendSms(destin,message);
		
		return memberDto;
	}
	
	@Override
	public boolean memberMobileAuth(MemberDto memberDto) {
		log.info("--------------------------------------------");
		log.info("회원가입-핸드폰-인증");
		log.info("--------------------------------------------");
		
		boolean isMember = memberRepository.existsByMemberIdAndMobileAuthNumber(memberDto.getMemberId(), memberDto.getMobileAuthNumber());
		if(isMember) {
			Optional<Member> member = memberRepository.findByMemberIdAndMobileNumberAndMobileAuthNumber(memberDto.getMemberId(), memberDto.getMobileNumber() , memberDto.getMobileAuthNumber());			
			member.get().updateAuthFlag(true);
			memberRepository.save(member.get());	
			return true;			
		}		
		return false;
	}
	
	@Override
	public boolean memberPasswordAuth(MemberDto memberDto) {
		log.info("--------------------------------------------");
		log.info("회원복원-비밀번호인증");
		log.info("--------------------------------------------");
		
		boolean isMember = memberAccountRepository.existsByMember_MemberIdAndChainAddressPw(memberDto.getMemberId(), memberDto.getMemberPassword());
		if(isMember) {			
			return true;			
		}		
		return false;
	}
	
	@Transactional
	@Override
	public DidSeqDto memberCreate(MemberDto memberDto) throws Exception {
		log.info("--------------------------------------------");
		log.info("회원가입-DB저장");
		log.info("--------------------------------------------");		
		Optional<Member> member = memberRepository.findByMemberId(memberDto.getMemberId());
		if(member.isPresent()) {
			member.get().updateMember(memberDto.getEmail(),memberDto.getMemberName(),memberDto.getDeviceId(),DateUtil.toDate("yyyy-MM-dd",memberDto.getBirth()) );
			memberRepository.save(member.get());			
			

			log.info("--------------------------------------------");
			log.info("회원가입-계좌생성");
			log.info("--------------------------------------------");
			Optional<ChainNode> chainNode = chainNodeRepository.findByChainNodeSeq(CustomConfig.chainNodeNumber);
			String chainAddress = new BlockChain().CreateAccount(memberDto,chainNode.get());
			String chainAddressPw = memberDto.getMemberPassword();	
			log.info("회원가입-계좌생성 chainAddress : {}" , chainAddress);
			log.info("회원가입-계좌생성 chainAddressPw : {}" , chainAddressPw);
			
			Optional<MemberAccount> isMemberAccount = memberAccountRepository.findByMember_MemberId(memberDto.getMemberId());
			if(isMemberAccount.isEmpty()) {	
				log.info("회원가입-계좌생성");				
				MemberAccount memberAccount = MemberAccount.builder()
						.member(member.get())
						.chainNode(chainNodeRepository.findByChainNodeSeq(CustomConfig.chainNodeNumber).get())
						.chainAddress(chainAddress)
						.chainAddressPw(chainAddressPw)
						.build();
				memberAccountRepository.save(memberAccount);
			}else {
				log.info("회원가입-계좌생성:중복처리저장안한다.");	
			}
			
			
//			log.info("--------------------------------------------");
//			log.info("회원가입-계좌생성후 10초대기");
//			log.info("--------------------------------------------");
//			try{
//			    Thread.sleep(10000);
//			}catch(InterruptedException e){
//			    e.printStackTrace();
//			}
			
			log.info("--------------------------------------------");
			log.info("회원가입-DID생성:resolver-server");
			log.info("회원가입-DID생성:parm getMemberId : {}" , memberDto.getMemberId());
			log.info("회원가입-DID생성:parm chainAddress : {}" , chainAddress);
			log.info("회원가입-DID생성:parm chainAddressPw : {}" , chainAddressPw);
			log.info("회원가입-DID생성:parm getMemberPublicKey : {}" , memberDto.getMemberPublicKey());
			log.info("--------------------------------------------");
			JsonUtil<DidDto> clsJsonUtil = new JsonUtil<DidDto>();					
			String json = clsJsonUtil.toString(
					DidDto.builder()
					.userId(memberDto.getMemberId())
					.userAccount(chainAddress)
					.userPassword(chainAddressPw)
					.authType(memberDto.getAuthType())
					.authPublicKey(memberDto.getMemberPublicKey())
					.build());		
			log.info("--------------------------------------------json {}", json);
			log.info("--------------------------------------------strResolverServerDidCreateDocument {}", CustomConfig.strResolverServerDidCreateDocument);				
			SnubiResponse clsSnubiResponse = HttpService.postResolverServer(json);	
			log.info("--------------------------------------------clsSnubiResponse {}", clsSnubiResponse);
			ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(clsSnubiResponse.getStrData().toString());
		    log.info("--------------------------------------------getStrData {}", clsSnubiResponse.getStrData().toString());
		    String did = jsonNode.get("did").asText();		    
		    log.info("did {}", did); 	
		   
		    
		    
		    log.info("--------------------------------------------");
			log.info("회원가입-DID생성:issuer-server");
			log.info("--------------------------------------------");	
			json = clsJsonUtil.toString(
					DidDto.builder()
					.did(did)
					.userId(member.get().getMemberId())
					.email(memberDto.getEmail())
					.memberName(memberDto.getMemberName())
					.mobileNumber(memberDto.getMobileNumber())
					.build());	
			log.info("--------------------------------------------json {}", json);
			log.info("--------------------------------------------strIssuerServercredentialAvchainvc {}", CustomConfig.strIssuerServercredentialAvchainvc);	
			clsSnubiResponse = HttpService.postIssuerServer(json);	
			
			
			jsonNode = objectMapper.readTree(clsSnubiResponse.getStrData().toString());
			log.info("--------------------------------------------getStrData {}", clsSnubiResponse.getStrData().toString());
			String vcSignatureSeq  = jsonNode.get("vcSignatureSeq").asText();
			
			
			log.info("vcDocument --- {}", jsonNode.get("vcDocument").toString());
			
			VcDocumentDto vcDocumentDto  =  objectMapper.readValue(jsonNode.get("vcDocument").toString(), VcDocumentDto.class);// . .readTree(jsonNode.get("vcDocument").toString()); 
			log.info("vcDocumentDto {}", vcDocumentDto);
			log.info("vcSignatureSeq {}", vcSignatureSeq);
		    
		    log.info("--------------------------------------------");
			log.info("회원가입-DID저장");
			log.info("--------------------------------------------");
			
			List<MemberDid> list=  memberDidRepository.findByMember_MemberId(memberDto.getMemberId());
			log.info("List<MemberDid> list.size() {}", list.size());
			if(list.size() == 0) {
				 log.info("회원가입-DID저장");
				 MemberDid memberDid = MemberDid.builder()
				    		.member(member.get())
				    		.did(did)
				    		.memberPublicKey(memberDto.getMemberPublicKey())
				    		.expiredDate(DateUtil.toDate("yyyy-MM-dd","2099-12-31"))
				    		.valid(true)
				    		.build();
				  memberDidRepository.save(memberDid);
			}else {
				 log.info("회원가입-DID저장:중복처리");
				MemberDid memberDid = list.get(0);
				memberDid.updateDid(did);
				memberDidRepository.save(memberDid);
			}
		   
			return DidSeqDto.builder().did(did).vcSignatureSeq(vcSignatureSeq).vcDocument(vcDocumentDto).build();
		}		
		return null;
	}
	
	

	@Override
	public boolean memberProfileUpdate(MultipartFile file, MemberDto memberDto) {
		log.info("--------------------------------------------");
		log.info("회원프로필수정");
		log.info("--------------------------------------------");
		String timestamp = String.valueOf( new Timestamp(System.currentTimeMillis()).getTime() );
		String fullPath = CustomConfig.strFileUploadPfofilePath + "/" + memberDto.getMemberId()+"_"+timestamp + ".png";
		try {	        	
        	if(!file.isEmpty()) {
        		File tmpFile = new File(fullPath);
	        	tmpFile.getParentFile().mkdir();
	        	file.transferTo(tmpFile);	
	        	Optional<Member> member = memberRepository.findByMemberId(memberDto.getMemberId());
	    		if(member.isPresent()) {
	    			member.get().updateProfile(fullPath);
	    			memberRepository.save(member.get());
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
	
	@Override
	public boolean memberMyCardUpdate(MultipartFile file, boolean isFile, MemberDto memberDto) {
		log.info("--------------------------------------------");
		log.info("회원카드배경수정");
		log.info("--------------------------------------------");
		String timestamp = String.valueOf( new Timestamp(System.currentTimeMillis()).getTime() );
		String fullPath = CustomConfig.strFileUploadMyCardPath + "/" + memberDto.getMemberId()+"_"+timestamp + ".png";
		try {	        	
        	if(!file.isEmpty() && isFile) {
        		File tmpFile = new File(fullPath);
	        	tmpFile.getParentFile().mkdir();
	        	file.transferTo(tmpFile);	
	        	Optional<Member> member = memberRepository.findByMemberId(memberDto.getMemberId());
	    		if(member.isPresent()) {
	    			member.get().updateCard(fullPath);
	    			memberRepository.save(member.get());
	    		}
        	}         	
        	if(!isFile) {
        		Optional<Member> member = memberRepository.findByMemberId(memberDto.getMemberId());
	    		if(member.isPresent()) {
	    			member.get().updateCard("");
	    			memberRepository.save(member.get());
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
	

	@Override
	public MemberDto memberProfile(MemberDto memberDto) {
		log.debug("--------------------------------------------");
		log.debug("회원프로필");
		log.debug("--------------------------------------------");	
		Optional<Member> member = memberRepository.findByMemberId(memberDto.getMemberId());	
		List<MemberDid> memberDidList = memberDidRepository.findByMember_MemberId(memberDto.getMemberId());
		List<String> didList = new ArrayList<>();
		for(MemberDid item : memberDidList) {
			didList.add(item.getDid());
		}		
		String birth = member.get().getBirth().toString();
		MemberDto clsMemberDto = MemberDto.builder()
				.memberId(member.get().getMemberId())
				.email(member.get().getEmail())
				.memberName(member.get().getMemberName())
				.mobileNumber(member.get().getMobileNumber())
				.mobileAuthFlag(member.get().isMobileAuthFlag())
				.deviceId(member.get().getDeviceId())
				.birth(birth.length() > 10 ? birth.substring(0,10) : birth)
				.profileFilePath(getFileName(member.get().getProfileFilePath()))
				.cardFilePath(getMyCardFileName(member.get().getCardFilePath() ))
				.MemberDid(didList)
				.build();
		return clsMemberDto;	
	}	
	
	private String getFileName(String path) {		
		if(path == null || "".equals(path)) {			
			return "";
		}else {	
			String[] nameArray = path.split("/");
			if(path.split("/").length < 2) return ""; 
			int imageName = nameArray.length - 1;
			int clubName = nameArray.length - 2;			
			log.info("imageName {}", imageName);
			log.info("clubName {}", clubName);
			return CustomConfig.strDefaultImageUrl + "profile/" + nameArray[imageName];
		}
	}
	private String getMyCardFileName(String path) {		
		if(path == null || "".equals(path)) {			
			return "";
		}else {	
			String[] nameArray = path.split("/");
			if(path.split("/").length < 2) return ""; 
			int imageName = nameArray.length - 1;
			int clubName = nameArray.length - 2;			
			log.info("imageName {}", imageName);
			log.info("clubName {}", clubName);
			return CustomConfig.strDefaultImageUrl + "mycard/" + nameArray[imageName];
		}
	}

	@Override
	public boolean memberFindPrivateKey(String memberId) {
		List<MemberDid> memberDidList = memberDidRepository.findByMember_MemberId(memberId);
		if(memberDidList.size() > 0) {
			if(memberDidList.get(0).getMemberPrivateKey() == null || "".equals(memberDidList.get(0).getMemberPrivateKey())){
				return false;
			}else {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean memberDeviceIdUpdate(MemberDto memberDto) {
		Optional<Member> member = memberRepository.findByMemberId(memberDto.getMemberId());
		if(member.isPresent()) {
			member.get().updateDeviceId(memberDto.getDeviceId());
			memberRepository.save(member.get());
			return true;
		}
		return false;
	}

	@Override
	public boolean memberGetRegister(String mobileNumber) {
		Optional<Member> member = memberRepository.findByMobileNumber(mobileNumber);
		if(member.isPresent()) {
			return member.get().isRegisterFlag();
		}
		return false;
	}

	@Override
	public boolean memberUpdateregister(String memberId, boolean registerFlag) {
		Optional<Member> member = memberRepository.findByMemberId(memberId);
		if(member.isPresent()) {
			member.get().updateRegisterFlag(registerFlag);
			memberRepository.save(member.get());
			return true;
		}
		return false;
	}

	@Override
	public AppVersionDto appVersion(String osName) {
		Optional<AppVersion> appVersion =  appVersionRepository.findByOs(osName); 
		if(appVersion.isPresent()) {
			AppVersionDto appVersionDto = AppVersionDto.builder()
					.marketLink(appVersion.get().getMarketLink())
					.version(appVersion.get().getVersion())
					.os(appVersion.get().getOs())
					.build();			
			return appVersionDto;
		}
		return null;
	}

}




