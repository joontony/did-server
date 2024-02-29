package org.snubi.did.main.exception;

import java.io.IOException;
import javax.servlet.ServletException;

import org.snubi.did.main.common.CustomResponseEntity;
import org.snubi.did.main.common.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
		@ExceptionHandler(NoHandlerFoundException.class)
		protected ResponseEntity<?> noHandlerFoundException(NoHandlerFoundException e) {
			log.error("NoHandlerFoundException {}", e.getMessage());
			return CustomResponseEntity.failResponse(ErrorCode.NOT_FOUND, e);
	    }
		
		@ExceptionHandler({Exception.class,IOException.class,ServletException.class})
	    protected ResponseEntity<?> handleException(Exception e) {
			log.error("Exception {}", e.getMessage());
			//return CustomResponseEntity.failResponse(ErrorCode.INTERNAL_SERVER_ERROR);
			return CustomResponseEntity.failResponse(e.getMessage(), e);
		}
		
		@ExceptionHandler(CustomException.class)
	    protected ResponseEntity<?> handleCustomException(CustomException e) {
			log.error("CustomException {}", e.getMessage());
	        return CustomResponseEntity.failResponse(e.getErrorCode(), e);
	    }
		
		/*
		@ExceptionHandler(NoHandlerFoundException.class)
		protected ResponseEntity<CustomResponse> noHandlerFoundException(NoHandlerFoundException e) {
			CustomResponse response = CustomResponse.builder()
					.code(HttpStatus.NOT_FOUND.toString())
					.message(e.getMessage())
					.build();				
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
		
		
		@ExceptionHandler(Exception.class)
	    protected ResponseEntity<CustomResponse> handleException(Exception e) {
			 CustomResponse response = CustomResponse.builder()
						.code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
						.data(null)
						.message(e.toString())
						.token("")
						.build();		
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
		
		
		@ExceptionHandler(EmailDuplicateException.class)
	    public ResponseEntity<CustomResponse> handleEmailDuplicateException(EmailDuplicateException e){
	        CustomResponse response = CustomResponse.builder()
					.code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
					.data(null)
					.message(e.getMessage())
					//.message(e.toString())
					.token("")
					.build();
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
		 */
}
