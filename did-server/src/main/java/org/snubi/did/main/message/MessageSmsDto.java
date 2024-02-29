package org.snubi.did.main.message;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSmsDto {
	 private List<String> destin;
	 private List<String> message;
}
