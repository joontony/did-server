package org.snubi.did.main.websocket;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class DataUpdatedEvent extends ApplicationEvent {

    public DataUpdatedEvent(Object source) {
        super(source);
    }
}