package org.snubi.did.main.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NewDataEventListener {

    @EventListener
    public void handleNewDataEvent(NewDataEvent event) {
    	log.info("data add to the table!");
    }
    
    @EventListener
    public void handleDataUpdatedEvent(DataUpdatedEvent event) {        
        log.info("data updated to the table!");
    }
   
}
