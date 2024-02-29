package org.snubi.did.main.service;

import org.snubi.did.main.config.CustomConfig;
import org.snubi.did.main.dto.DidDto;
import org.snubi.did.main.dto.UniversalDto;
import org.snubi.did.main.message.KakaoDto;
import org.snubi.did.main.message.MessageEmailDto;
import org.snubi.did.main.message.MessageSmsDto;
import org.snubi.did.main.message.PushDto;
import org.snubi.lib.http.HttpUtilPost;
import org.snubi.lib.response.SnubiResponse;
import org.springframework.stereotype.Service;

@Service
public interface NetworkService {
	static class HttpService {	
		
		public static SnubiResponse postUniversalServer(String json)   {	
			try {				
				HttpUtilPost<UniversalDto> clsHttpUtilPost = new HttpUtilPost<UniversalDto>();
				clsHttpUtilPost.setStrUrl(CustomConfig.strUniversalServerCredentialClubCreate);
				clsHttpUtilPost.setStrAuth(null);
				clsHttpUtilPost.setStrTokenType("Bearer");
				clsHttpUtilPost.setStrCharset("UTF-8");
				clsHttpUtilPost.setStrType("application/json");
				SnubiResponse clsSnubiResponse = clsHttpUtilPost.post(json);
				return clsSnubiResponse;	
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}			
			return null;			
		}
		
		public static SnubiResponse postResolverServer(String json)   {	
			try {				
				HttpUtilPost<DidDto> clsHttpUtilPost = new HttpUtilPost<DidDto>();
				clsHttpUtilPost.setStrUrl(CustomConfig.strResolverServerDidCreateDocument);
				clsHttpUtilPost.setStrAuth(null);
				clsHttpUtilPost.setStrTokenType("Bearer");
				clsHttpUtilPost.setStrCharset("UTF-8");
				clsHttpUtilPost.setStrType("application/json");
				SnubiResponse clsSnubiResponse = clsHttpUtilPost.post(json);
				return clsSnubiResponse;	
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}			
			return null;			
		}
		
		public static SnubiResponse postIssuerServer(String json) {	
			try {				
				HttpUtilPost<DidDto> clsHttpUtilPost = new HttpUtilPost<DidDto>();
				clsHttpUtilPost.setStrUrl(CustomConfig.strIssuerServercredentialAvchainvc);
				clsHttpUtilPost.setStrAuth(null);
				clsHttpUtilPost.setStrTokenType("Bearer");
				clsHttpUtilPost.setStrCharset("UTF-8");
				clsHttpUtilPost.setStrType("application/json");
				SnubiResponse clsSnubiResponse = clsHttpUtilPost.post(json);
				return clsSnubiResponse;	
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}			
			return null;			
		}
		
		public static SnubiResponse postExcelIssuerServer(String json) {	
			try {				
				HttpUtilPost<DidDto> clsHttpUtilPost = new HttpUtilPost<DidDto>();
				clsHttpUtilPost.setStrUrl(CustomConfig.strIssuerServerCredentialExcel);
				clsHttpUtilPost.setStrAuth(null);
				clsHttpUtilPost.setStrTokenType("Bearer");
				clsHttpUtilPost.setStrCharset("UTF-8");
				clsHttpUtilPost.setStrType("application/json");
				SnubiResponse clsSnubiResponse = clsHttpUtilPost.post(json);
				return clsSnubiResponse;	
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}			
			return null;			
		}
		
		public static SnubiResponse postMessageServerEmail(String json) {	
			try {				
				HttpUtilPost<MessageEmailDto> clsHttpUtilPost = new HttpUtilPost<MessageEmailDto>();
				clsHttpUtilPost.setStrUrl(CustomConfig.strMessageServerEmailSend);
				clsHttpUtilPost.setStrAuth(null);
				clsHttpUtilPost.setStrTokenType("Bearer");
				clsHttpUtilPost.setStrCharset("UTF-8");
				clsHttpUtilPost.setStrType("application/json");
				SnubiResponse clsSnubiResponse = clsHttpUtilPost.post(json);
				return clsSnubiResponse;	
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}			
			return null;			
		}
		
		public static SnubiResponse postMessageServerSms(String json) {	
			try {				
				HttpUtilPost<MessageSmsDto> clsHttpUtilPost = new HttpUtilPost<MessageSmsDto>();
				clsHttpUtilPost.setStrUrl(CustomConfig.strMessageServerSmsSend);
				clsHttpUtilPost.setStrAuth(null);
				clsHttpUtilPost.setStrTokenType("Bearer");
				clsHttpUtilPost.setStrCharset("UTF-8");
				clsHttpUtilPost.setStrType("application/json");
				SnubiResponse clsSnubiResponse = clsHttpUtilPost.post(json);
				return clsSnubiResponse;	
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}			
			return null;			
		}

		public static SnubiResponse postPushServer(String json) {	
			try {				
				HttpUtilPost<PushDto> clsHttpUtilPost = new HttpUtilPost<PushDto>();
				clsHttpUtilPost.setStrUrl(CustomConfig.strPushURL);
				clsHttpUtilPost.setStrAuth(CustomConfig.strAPIKey);
				clsHttpUtilPost.setStrTokenType("key=");
				clsHttpUtilPost.setStrCharset("UTF-8");
				clsHttpUtilPost.setStrType("application/json");
				SnubiResponse clsSnubiResponse = clsHttpUtilPost.post(json);
				return clsSnubiResponse;	
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}			
			return null;			
		}
		
		public static SnubiResponse postKakaoTalkServer(String json, String header) {	
			try {				
				HttpUtilPost<KakaoDto> clsHttpUtilPost = new HttpUtilPost<KakaoDto>();
				clsHttpUtilPost.setStrUrl(CustomConfig.strKakaoTalkSend);
				clsHttpUtilPost.setStrAuth(header);
				clsHttpUtilPost.setStrTokenType("");
				clsHttpUtilPost.setStrCharset("UTF-8");
				clsHttpUtilPost.setStrType("application/json");
				SnubiResponse clsSnubiResponse = clsHttpUtilPost.postKakao(json);
				return clsSnubiResponse;	
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}			
			return null;			
		}
	}
}
