package org.snubi.did.main.exception;


import org.snubi.did.main.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    private static final long serialVersionUID = 1L;
	ErrorCode errorCode;
}
