package org.snubi.did.main.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class CustomConfig {
	
	public static List<String> allowedOrigins;
	@Value("#{'${allowed.origins}'.split(',')}")
	public void setAllowedOrigins(List<String> prop) {
		CustomConfig.allowedOrigins = prop;
	}
	
	public static List<Integer> allowedKakao;
	@Value("#{'${allowed.kakao}'.split(',')}")
	public void setAllowedKakao(List<Integer> prop) {
		CustomConfig.allowedKakao = prop;
	}
	
	public static String strNiceReturnURL;
	@Value("${nice.return.url}")	
	public void setStrNiceReturnURLl(String prop) {
		CustomConfig.strNiceReturnURL = prop;
	}
	
	public static Long chainNodeNumber;
	@Value("${blockchain.node.number}")
	public void setChainNodeNumber(Long prop) {
		CustomConfig.chainNodeNumber = prop;
	}
	
	public static String strAPIKey;  // CbCustomConfig.strAPIKey >> null : static 변수는 @Value 이 동작하지 않는다. 아래처럼 해야함 
	@Value("${push.api.key}")
	public void setStrAPIKey(String prop) {
		CustomConfig.strAPIKey = prop;
	}
	
	public static String strPushURL;
	@Value("${push.url}")	
	public void setPushUrl(String prop) {
		CustomConfig.strPushURL = prop;
	}
	
	public static int strFileUploadSize = 10;
	
	public static String strFileUploadPath;
	@Value("${didserver.interface.home.upload}")
	public void setStrFileUploadPath(String prop) {
		CustomConfig.strFileUploadPath = prop;
	}
	
	public static String strNoticeFileUploadPath;
	@Value("${didserver.interface.home.upload.notice}")
	public void setStrNoticeFileUploadPath(String prop) {
		CustomConfig.strNoticeFileUploadPath = prop;
	}
	
	public static String strFileUploadPfofilePath;
	@Value("${didserver.interface.home.upload.profile}")
	public void setStrFileUploadPfofilePathh(String prop) {
		CustomConfig.strFileUploadPfofilePath = prop;
	}	
	
	public static String strFileUploadMyCardPath;
	@Value("${didserver.interface.home.upload.mycard}")
	public void setStrFileUploadMyCardPathh(String prop) {
		CustomConfig.strFileUploadMyCardPath = prop;
	}	
	
	public static String strYamlPath;
	@Value("${yaml.path}")
	public void setStrYamlPath(String prop) {
		CustomConfig.strYamlPath = prop;
	}
	
	public static String strTokenPrefix;
	@Value("${http.response.auth.token}")
	public void setStrTokenPrefix(String prop) {
		CustomConfig.strTokenPrefix = prop + " ";
	}

	public static String strSecrete;
	@Value("${security.oauth2.resource.jwt.key-value}")
	public void setStrSecrete(String prop) {
		CustomConfig.strSecrete = prop;
	}
	
	public static String strResponseAuthHeader;
	@Value("${http.response.auth.header}")
	public void setStrResponseAuthHeader(String prop) {
		CustomConfig.strResponseAuthHeader = prop;
	}

	public static String strResponseAuthClaimIssue;
	@Value("${http.response.auth.claims.issue}")
	public void setStrResponseAuthClaimIssue(String prop) {
		CustomConfig.strResponseAuthClaimIssue = prop;
	}
	
	public static String strKubenetesServerUrl;
	@Value("${kubernetes.server.url}")
	public void setStrKubenetesServerUrl(String prop) {
		CustomConfig.strKubenetesServerUrl = prop;
	}
	
	public static String strKubenetesServerIp;
	@Value("${kubernetes.server.ip}")
	public void setStrKubenetesServerIp(String prop) {
		CustomConfig.strKubenetesServerIp = prop;
	}
	
	public static String strResolverServerUrl;
	@Value("${resolver.server.url}")
	public void setStrResolverServerUrl(String prop) {
		CustomConfig.strResolverServerUrl = prop;
	}
	
	public static String strIssuerServerUrl;
	@Value("${issuer.server.url}")
	public void setStrIssuerServerUrl(String prop) {
		CustomConfig.strIssuerServerUrl = prop;
	}
	
	public static String strResolverServerDidCreateDocument;
	@Value("${resolver.server.did.create.document}")
	public void setStrResolverServerDidCreateDocument(String prop) {
		CustomConfig.strResolverServerDidCreateDocument = prop;
	}
	
	public static String strUniversalServerCredentialClubCreate;
	@Value("${universal.server.credential.club.create}")
	public void setStrUniversalServerCredentialClubCreate(String prop) {
		CustomConfig.strUniversalServerCredentialClubCreate = prop;
	}
	
	public static String strUniversalServeUrl;
	@Value("${universal.server.url}")
	public void setStrUniversalServeUrl(String prop) {
		CustomConfig.strUniversalServeUrl = prop;
	}
	
	public static String strIssuerServercredentialAvchainvc;
	@Value("${issuer.server.credential.avchainvc}")
	public void setStrIssuerServercredentialAvchainvc(String prop) {
		CustomConfig.strIssuerServercredentialAvchainvc = prop;
	}
	
	public static String strIssuerServerCredentialExcel;
	@Value("${issuer.server.credential.club.excel.upload}")
	public void setStrIssuerServerCredentialExcel(String prop) {
		CustomConfig.strIssuerServerCredentialExcel = prop;
	}
	
	public static String strMessageServerEmailSend;
	@Value("${message.server.url.email.send}")
	public void setStrMessageServerEmailSend(String prop) {
		CustomConfig.strMessageServerEmailSend = prop;
	}
	
	public static String strKakaoTalkSend;
	@Value("${kakao.talk}")
	public void setStrKakaoTalkSend(String prop) {
		CustomConfig.strKakaoTalkSend = prop;
	}
	
	public static String strMessageServerSmsSend;
	@Value("${message.server.url.sms.send}")
	public void setStrMessageServerSmsSend(String prop) {
		CustomConfig.strMessageServerSmsSend = prop;
	}
	
	public static String strDefaultImageUrl;
	@Value("${default.image.url}")
	public void setStrDefaultImageUrl(String prop) {
		CustomConfig.strDefaultImageUrl = prop;
	}
}
