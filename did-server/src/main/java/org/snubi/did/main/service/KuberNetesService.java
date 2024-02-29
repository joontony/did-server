package org.snubi.did.main.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.snubi.did.main.common.ErrorCode;
import org.snubi.did.main.config.CustomConfig;
import org.snubi.did.main.dto.ClubDto;
import org.snubi.did.main.dto.KubeDto;
import org.snubi.did.main.entity.ChainNode;
import org.snubi.did.main.entity.Club;
import org.snubi.did.main.exception.CustomException;
import org.snubi.lib.analysis.AnalysisUtil;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KuberNetesService {
	
	final static String strToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IkJKT2RtNi0zY01TTzhzQmRVMTJEUjVOQjBlWndOYlJlN1JoYVJqNVdQcTQifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImRlZmF1bHQtdG9rZW4tZmQycGciLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGVmYXVsdCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjkwNTA4MDgyLTEzYmYtNDE2NC04ZWVlLTI2MmY3NmEyNmE5MSIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDpkZWZhdWx0OmRlZmF1bHQifQ.NDNjzkHcvfJudhshMU0-m6cZ57POHJ9vRZlwuLMSf5_wMRrl54GpYGM4ffN8J9CkxI0p5TtrJ_gpToK9oBeKB_o0SveHe4hYSBImFtGnCrtDhSN-6xMLtP3H8137wknp6W0zZ_0h-fIy2bGTaOkj0gIY1UPN62axdBHaEaGCKs6XL5dAav5AwXTf2z8vHoBvgpGyLiPO9UhhGmcLc_d0mFiarCBAMqnm1KSTxLmRRU1D02lWl31NqIeI5BE0GZskRuynSSTUmRtT5zh7RnTghQexEHWRSxdOKP5wM-CvYhnsR3HBBbV8G_PnIEfmtq6KdcVck2EkAexfADNTsgMc-A";	

	final static TrustManager[] arrTrustManager = new TrustManager[] {
		new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}
		    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
		    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
		}
	};
	
	private int reconnect = 0;
	
	
	
	
	//@Async
	protected String asyncSendYaml(String token, ClubDto clubDto, Club club, ChainNode chainNode)  {		
		log.info("asyncSendYaml ------------");
	  try {
		  
		log.info("첫번째 파일 getPodYamlPath {}", club.getClubCategory().getPodYamlPath());
		log.info("두번째 파일 getSvsYamlPath {}", club.getClubCategory().getSvsYamlPath());
		
		
		String yamlPodPathStr 		= AnalysisUtil.readFile(club.getClubCategory().getPodYamlPath());
		String yamlServicePathStr	 = AnalysisUtil.readFile(club.getClubCategory().getSvsYamlPath());
		
		
		
		yamlPodPathStr = yamlPodPathStr.replace("{{POD NAME}}",								"issuer-pod-" + club.getClubSeq() + "-" + club.getClubCategory().getClubCategorySeq() 										);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - METADATA NAME}}",				"issuer-pod-" + club.getClubSeq() + "-" + club.getClubCategory().getClubCategorySeq()  + "-app" 							);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV member_did_seq}}",		club.getMemberDid().getMemberDidSeq().toString()																			);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV club_public_key}}",		club.getClubPublicKey()																										);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV club_category_seq}}",		club.getClubCategory().getClubCategorySeq().toString()																		);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV did}}",					club.getMemberDid().getDid()																								);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV jwt}}",					token																														);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV BLOCKCHAIN_IP}}",			chainNode.getNodeIp()																										);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV BLOCKCHAIN_PORT}}",		chainNode.getNodePort()																										);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV PLATFORM_ACCOUNT}}",		chainNode.getAdminChainAddress()																							);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV PLATFORM_PASSWORD}}",		chainNode.getAdminChainPassword()																							);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV DID_RESOLVER_URL}}",		CustomConfig.strResolverServerUrl																							);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV MYSQL_ROOT_PASSWORD}}",	"avchain1004!!"																												);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV MYSQL_USER}}",			"root"																														);
		
		//yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV START_DATE}}",			DateUtil.toString(clubDto.getStartDate(), "yyyy-MM-dd")																						);
		//yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV END_DATE}}",			DateUtil.toString(clubDto.getEndDate(), "yyyy-MM-dd")																						);
		
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV START_DATE}}",			clubDto.getStartDate()																						);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV END_DATE}}",				clubDto.getEndDate()																 	 				);
		
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV DID_ISSUER_URL}}",		CustomConfig.strIssuerServerUrl												);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV club_id}}",				club.getClubSeq()+""																							);
		yamlPodPathStr = yamlPodPathStr.replace("{{POD YAML - ENV valid}}",					club.isValid() ? "1" : "0"													);
		
		
		log.info("첫번째 파일 쿠버네티스 전송  yamlPodPathStr {}", yamlPodPathStr);				
		sendKuberNetes(false,"/api/v1/namespaces/default/pods/",yamlPodPathStr);	
		yamlServicePathStr = yamlServicePathStr.replace("{{SVS YAML - METADATA NAME}}",		  "issuer-pod-" + club.getClubSeq() + "-" + club.getClubCategory().getClubCategorySeq()   									);
		yamlServicePathStr = yamlServicePathStr.replace("{{POD YAML - METADATA LABELS APP}}", "issuer-pod-" + club.getClubSeq() + "-" + club.getClubCategory().getClubCategorySeq()  + "-app"							);	
		log.info("두번째 파일 쿠버네티스 전송  yamlServicePathStr {}", yamlServicePathStr);
		return sendKuberNetes(true,"/api/v1/namespaces/default/services/",yamlServicePathStr);
				
	   } catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(ErrorCode.YAML_ERROR);					
	   }

	}

	
	public String sendKuberNetes(boolean lastYaml, String uri, String strBody) {	
		log.info("쿠버네티스 전송 실행중 ...  sendKuberNetes uri {}", uri);
		String podUrl = "";
		String strBuffer = new String();
		StringBuffer bufBuffer = new StringBuffer();
		OutputStream clsOutputStream = null;
		BufferedReader clsBufferedReader = null;
		HttpURLConnection clsHttpURLConnection = null;
		try {				
			SSLContext clsSSLContext = SSLContext.getInstance("SSL");
//			DESC : 현제 kube 서버의 SSL이 정상적이지 않으므로, Context 를 변경하여 인증서정보를 무시하도록 한다
			clsSSLContext.init(null, arrTrustManager, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(clsSSLContext.getSocketFactory());
			URL clsURL = new URL(CustomConfig.strKubenetesServerUrl + uri);
			clsHttpURLConnection = (HttpURLConnection) clsURL.openConnection();
			clsHttpURLConnection.setRequestProperty("Content-Type", "application/yaml");
			clsHttpURLConnection.setRequestMethod("POST");
			clsHttpURLConnection.setRequestProperty("Authorization", "Bearer " + strToken);			
			clsHttpURLConnection.setDoOutput(true);
			clsHttpURLConnection.connect();
			clsOutputStream = clsHttpURLConnection.getOutputStream();
			clsOutputStream.write(strBody.getBytes("UTF-8"));	   
		 	clsOutputStream.flush();
		 	clsOutputStream.close();
			clsBufferedReader = new BufferedReader(new InputStreamReader(clsHttpURLConnection.getInputStream(),"UTF-8"));
			while ((strBuffer = clsBufferedReader.readLine()) != null) {
				bufBuffer.append(strBuffer);
			}
		} catch(Exception Ex) {			
			reconnect++;
			log.info("쿠버네티스 전송 실행중 ...   reconnect // Exception {} // {}",reconnect, Ex.getMessage());
			if(reconnect < 3) sendKuberNetes( lastYaml,  uri,  strBody);
			// throw new CustomException(ErrorCode.KUBE_ERROR);
			// return "";
		} finally {
	    	try {clsOutputStream.close();         	} catch(Exception Ex) {}
	 	    try {clsBufferedReader.close();         } catch(Exception Ex) {}
	 	    try {clsHttpURLConnection.disconnect();	} catch(Exception Ex) {}
	    }
		if(lastYaml) {
			// 디비갱신 			
			log.info("쿠버네티스 전송 실행중 ...  (두번째 파일만)  쿠버네티스응답 JSON :  {}", bufBuffer.toString());
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //  필요한 것만 정의 
			String json = bufBuffer.toString();
	        try {
	            KubeDto kubeDto = objectMapper.readValue(json, KubeDto.class);	            
	            int port = kubeDto.getSpec().getPorts().get(0).getNodePort();
	            String strKubenetesServerIp = CustomConfig.strKubenetesServerIp;	
	            log.info("쿠버네티스응답 도커주소(ContainerUri) {}", strKubenetesServerIp + ":"+ port);
	            podUrl = strKubenetesServerIp + ":"+ port;
	            return podUrl;
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
			
		}
		return podUrl;
	}
	
	public String kuberNetesDestory(Long studyId) {		
		String pod = sendKuberNetesDestory("/api/v1/namespaces/default/pods/cdm"+studyId,"", studyId);
		log.info("쿠버네티스 Destory 응답 pod {}", pod);		
		String service = sendKuberNetesDestory("/api/v1/namespaces/default/services/cdm"+studyId,"", studyId);
		log.info("쿠버네티스 Destory 응답 service {}", service);		
		return "";
	}
	
	public String sendKuberNetesDestory(String uri, String strBody,Long studyId) {	
		log.info("쿠버네티스 Destory 요청 실행중 ...  sendKuberNetesDestory uri {}", CustomConfig.strKubenetesServerUrl + uri);
		String strBuffer = new String();
		StringBuffer bufBuffer = new StringBuffer();
		BufferedReader clsBufferedReader = null;
		HttpURLConnection clsHttpURLConnection = null;
		try {				
			SSLContext clsSSLContext = SSLContext.getInstance("SSL");
//			DESC : 현제 kube 서버의 SSL이 정상적이지 않으므로, Context 를 변경하여 인증서정보를 무시하도록 한다
			clsSSLContext.init(null, arrTrustManager, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(clsSSLContext.getSocketFactory());
			URL clsURL = new URL(CustomConfig.strKubenetesServerUrl + uri);			
			clsHttpURLConnection = (HttpURLConnection) clsURL.openConnection();
			clsHttpURLConnection.setRequestProperty("Accept", "*/*");
			clsHttpURLConnection.setRequestMethod("DELETE");
			clsHttpURLConnection.setRequestProperty("Authorization", "Bearer " + strToken);			
			clsHttpURLConnection.setDoOutput(false);
			clsHttpURLConnection.connect();
			clsBufferedReader = new BufferedReader(new InputStreamReader(clsHttpURLConnection.getInputStream(),"UTF-8"));
			while ((strBuffer = clsBufferedReader.readLine()) != null) {
				bufBuffer.append(strBuffer);
				log.info("쿠버네티스 Destory 요청 실행중 ...  strBuffer {}", strBuffer.toString());
			}
		} catch(Exception Ex) {				
			Ex.printStackTrace();
			//throw new CustomException(ErrorCode.KUBE_ERROR);
			return "";
		} finally {
//	    	try {clsOutputStream.close();         	} catch(Exception Ex) {}
	 	    try {clsBufferedReader.close();         } catch(Exception Ex) {}
	 	    try {clsHttpURLConnection.disconnect();	} catch(Exception Ex) {}
	    }		
		return bufBuffer.toString();
	}
}
