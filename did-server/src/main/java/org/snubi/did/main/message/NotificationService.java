package org.snubi.did.main.message;

import java.util.List;
import org.snubi.did.main.entity.Notification;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
	boolean notificationCreate(List<Notification> notificationList);
}
