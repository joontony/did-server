package org.snubi.did.main.controller;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.snubi.did.main.common.CustomResponseEntity;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.dto.MemberDto;
import org.snubi.did.main.exception.CustomException;
import org.snubi.did.main.manager.NiceManager;
import org.snubi.did.main.manager.NiceManager.NiceToken;
import org.snubi.did.main.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController extends WithAuthController {
	
	private final MemberService memberService;
	@Autowired NiceManager niceManager;
	
	@RequestMapping(value = "/member/mobile/{mobileNumber}", method = RequestMethod.GET)
	public ResponseEntity<?> memberSetMobileAndIdCreate(@PathVariable String mobileNumber) throws Exception {			
		return CustomResponseEntity.succResponse(memberService.memberSetMobileAndIdCreate(MemberDto.builder().mobileNumber(mobileNumber).build()),"");
	}
	
	@RequestMapping(value = "/member/find/private/{memberId}", method = RequestMethod.GET)
	public ResponseEntity<?> memberFindPrivateKey(@PathVariable String memberId) throws Exception {			
		return CustomResponseEntity.succResponse(memberService.memberFindPrivateKey(memberId),"");
	}
	
	@RequestMapping(value = "/member/mobile/auth", method = RequestMethod.POST)
	public ResponseEntity<?> memberMobileAuth(@RequestBody MemberDto memberDto) throws Exception {
			return CustomResponseEntity.succResponse(memberService.memberMobileAuth(memberDto),"");	
	}
	
	@RequestMapping(value = "/member/password/auth", method = RequestMethod.POST)
	public ResponseEntity<?> memberPasswordAuth(@RequestBody MemberDto memberDto) throws Exception {
			return CustomResponseEntity.succResponse(memberService.memberPasswordAuth(memberDto),"");	
	}
	
	
	@RequestMapping(value = "/member/create", method = RequestMethod.PUT)
	public ResponseEntity<?> memberCreate(@RequestBody MemberDto memberDto) throws Exception {						
			return CustomResponseEntity.succResponse(memberService.memberCreate(memberDto),"");	
	}
	
	@RequestMapping(value = "/member/deviceid/update", method = RequestMethod.PUT)
	public ResponseEntity<?> memberDeviceIdUpdate(@RequestBody MemberDto memberDto) throws Exception {						
			return CustomResponseEntity.succResponse(memberService.memberDeviceIdUpdate(memberDto),"");	
	}
	
	@RequestMapping(value = "/member/profile/update", method = RequestMethod.PUT)
	public ResponseEntity<?> memberProfileUpdate(@RequestPart("imagePath") MultipartFile imagePath) throws Exception {			
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(memberService.memberProfileUpdate(imagePath, setMemberDto()),"");	
		}	
	}	
	@RequestMapping(value = "/member/mycard/update/{isFile}", method = RequestMethod.PUT)
	public ResponseEntity<?> memberMyCardUpdate(@RequestPart("imagePath") MultipartFile imagePath,@PathVariable boolean isFile) throws Exception {			
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(memberService.memberMyCardUpdate(imagePath, isFile, setMemberDto()),"");	
		}	
	}	
	
	@RequestMapping(value = "/member/profile", method = RequestMethod.GET)
	public ResponseEntity<?> memberProfile() throws Exception {		
		if("not-login".equals(getClaims().getId())) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);				
		}else {	
			return CustomResponseEntity.succResponse(memberService.memberProfile(setMemberDto()),"");
		}	
	}
	
	@RequestMapping(value = "/member/register/{mobileNumber}", method = RequestMethod.GET)
	public ResponseEntity<?> memberGetRegister(@PathVariable String mobileNumber) throws Exception {	
			return CustomResponseEntity.succResponse(memberService.memberGetRegister(mobileNumber),"");
	}
	
	@RequestMapping(value = "/member/register/{memberId}/{registerFlag}", method = RequestMethod.PUT)
	public ResponseEntity<?> memberUpdateregister(@PathVariable String memberId, @PathVariable boolean registerFlag) throws Exception {						
			return CustomResponseEntity.succResponse(memberService.memberUpdateregister(memberId,registerFlag),"");	
	}

	@RequestMapping(value = "/app/version/os/{osName}", method = RequestMethod.GET)
	public ResponseEntity<?> appVersion(@PathVariable String osName) throws Exception {	
			return CustomResponseEntity.succResponse(memberService.appVersion(osName),"");
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/response/nice", method = RequestMethod.POST)
	public String responseNice(@RequestParam String token_version_id, @RequestParam String enc_data,@RequestParam String  integrity_value) throws Exception {
		
		log.info("---------token_version_id {}", token_version_id);
		log.info("---------enc_data {}", enc_data);
		log.info("---------integrity_value {}", integrity_value);
		//NiceToken niceToken = NiceManager.getInstance().getToken(token_version_id);
		NiceToken niceToken = niceManager.getToken(token_version_id);
		
		log.info("---------getToken {}", niceToken.getToken());
		log.info("---------getKey {}", niceToken.getKey());
		log.info("---------getIv {}", niceToken.getIv());
		String key = niceToken.getKey();
		String iv = niceToken.getIv();
		
		SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));	
		byte[] cipherEnc = Base64.getDecoder().decode(enc_data.getBytes());
		String resData = new String(c.doFinal(cipherEnc),"euc-kr");	
		
		log.info("---------size {}", niceManager.getAll().size());		
		niceManager.removeNiceToken(token_version_id);		
		return resData;
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/response/nice", method = RequestMethod.GET)
	public String responseNiceGet(@RequestParam String token_version_id, @RequestParam String enc_data,@RequestParam String  integrity_value) throws Exception {
		
		log.info("---------token_version_id {}", token_version_id);
		log.info("---------enc_data {}", enc_data);
		log.info("---------integrity_value {}", integrity_value);
		
		//NiceToken niceToken = NiceManager.getInstance().getToken(token_version_id);
		NiceToken niceToken = niceManager.getToken(token_version_id);
		log.info("---------getToken {}", niceToken.getToken());
		log.info("---------getKey {}", niceToken.getKey());
		log.info("---------getIv {}", niceToken.getIv());
		String key = niceToken.getKey();
		String iv = niceToken.getIv();
		

		
		
		SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));	
		byte[] cipherEnc = Base64.getDecoder().decode(enc_data.getBytes());
		String resData = new String(c.doFinal(cipherEnc),"euc-kr");	
		
		log.info("---------size {}", niceManager.getAll().size());		
		niceManager.removeNiceToken(token_version_id);		
		return resData;
	}
}
