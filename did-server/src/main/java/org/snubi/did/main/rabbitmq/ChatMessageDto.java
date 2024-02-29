package org.snubi.did.main.rabbitmq;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessageDto implements Serializable {
	private Long clubSeq;
	private String sender;
    private String receiver;
    private String message;
    private String routingKey;
    private List<String> filePath;
	private String fileUrl;
	private boolean readFlag;	
	private List<String> originalFileName;
	private List<String> pureFileName;
	private LocalDateTime updated;
}
