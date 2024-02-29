package org.snubi.did.main.common;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

// https://developer.mozilla.org/ko/docs/Web/HTTP/Status/400

@AllArgsConstructor
@Getter
public enum ErrorCode {	
	// 400 Bad Request : 클라이언트 오류(예: 잘못된 요청 구문, 유효하지 않은 요청 메시지 프레이밍, 또는 변조된 요청 라우팅)
	BAD_REQUEST (HttpStatus.BAD_REQUEST,""),
	// 401 Unauthorized : 해당 리소스에 유효한 인증 자격 증명이 없기 때문, 403과 비슷하지만, 401 Unauthorized의 경우에는 인증이 가능합니다.
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증정보가 유효하지 않습니다."),
	// 403 Forbidden : 서버에 요청이 전달되었지만, 권한 때문에 거절되었다는 것, 401과 비슷하지만, 로그인 로직(틀린 비밀번호로 로그인 행위)처럼 반응하여 재인증(re-authenticating)을 하더라도 지속적으로 접속을 거절합니다.
	FORBIDDEN(HttpStatus.FORBIDDEN,"인증되었지만 접근권한이 없습니다."),
	// 404 Not Found : 요청받은 리소스를 찾을 수 없다, 브로큰 링크(broken link) 또는 데드 링크(dead link)
	NOT_FOUND(HttpStatus.NOT_FOUND,"요청받은 리소스를 찾을수 없습니다."),
	// 500 Internal Server Error : 서버 에러 응답 코드는 요청을 처리하는 과정에서 서버가 예상하지 못한 상황에 놓였다는 것을 나타냅니다.
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 오류입니다."),	
	
	INPUT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"입력형식 오류입니다."),
	NOT_USE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"사용되지 않는 번호입니다."),
	KUBE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"KUBE 오류입니다."),
	YAML_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"YAML 오류입니다."),
    FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"파일 오류입니다."),
    CLUB_NAME_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"클럽이름중복 오류입니다."),
    BLOCK_CHAIN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"블록체인 오류입니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
