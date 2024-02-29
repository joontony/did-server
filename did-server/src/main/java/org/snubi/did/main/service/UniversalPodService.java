package org.snubi.did.main.service;

import org.snubi.did.main.service.NetworkService.HttpService;
import org.snubi.lib.response.SnubiResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UniversalPodService {

	@Async
	public void asyncUniversalPod(String json)  {
		log.info("--------------------------------------------asyncUniversalPod start");
		SnubiResponse clsSnubiResponse = HttpService.postUniversalServer(json);	
		log.info("--------------------------------------------clsSnubiResponse {}", clsSnubiResponse);
		log.info("--------------------------------------------asyncUniversalPod end");
	}
}
