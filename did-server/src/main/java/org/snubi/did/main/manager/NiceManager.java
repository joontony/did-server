package org.snubi.did.main.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NiceManager {
	
	private final static List<NiceToken> niceTokenList = new ArrayList<>();
	
		
//    public NiceManager() {};
//	
//	private static class SingletonHolder {
//        private static final NiceManager INSTANCE = new NiceManager();
//    }
//	
//	public static NiceManager getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
	
	public static void addNiceToken(String token, String key, String iv) {
			if (getToken(token) != null) {
				removeNiceToken(token);
			}
			
	        NiceToken niceToken = new NiceToken(token, key, iv);      
	        log.info("---------getToken --{}--", niceToken.getToken());
			log.info("---------getKey --{}--", niceToken.getKey());
			log.info("---------getIv --{}--", niceToken.getIv());
	        niceTokenList.add(niceToken);
    }
	
	public static void removeNiceToken(String token) {
        Iterator<NiceToken> iterator = niceTokenList.iterator();
        while (iterator.hasNext()) {
            NiceToken niceToken = iterator.next();
            if (niceToken.getToken().equals(token)) {
                iterator.remove();
                break;
            }
        }
    }
	
	public static NiceToken getToken(String token) {
		log.info("---------token --{}--", token);
		log.info("---------niceTokenList.size() {}", niceTokenList.size());		
        for (NiceToken niceToken : niceTokenList) {
            if (niceToken.getToken().equals(token)) {
                return niceToken;
            }
        }
        return null; 
    }
	
	public static List<NiceToken> getAll(){
		return niceTokenList;
	}

	@Data
	@Builder
	public static class NiceToken{		
		String token;
		String key;
		String iv;			
	}
}

