package org.snubi.did.main.websocket;

import org.springframework.context.ApplicationEvent;

public class NewDataEvent extends ApplicationEvent {

    public NewDataEvent(Object source) {
        super(source);        
    }
}