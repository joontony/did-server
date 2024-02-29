//package org.snubi.did.main.service;
//
//import org.snubi.did.main.config.CustomConfig;
//import org.springframework.stereotype.Service;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//public abstract class ClubAbstractService implements ClubService {	
//	
//
//	private String getClubAndFileName(String path) {
//		if (path == null || "".equals(path)) {
//			return "";
//		} else {
//			String[] nameArray = path.split("/");
//			if (path.split("/").length < 2)
//				return "";
//			int imageName = nameArray.length - 1;
//			int clubName = nameArray.length - 2;
//			log.info("imageName {}", imageName);
//			log.info("clubName {}", clubName);
//			return CustomConfig.strDefaultImageUrl + nameArray[clubName] + "/" + nameArray[imageName];
//		}
//	}
//
//}
