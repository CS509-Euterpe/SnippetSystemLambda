/*
 * CS-509 Team Eutrepe AWS Application
 */

package edu.wpi.cs.eutrepe.http;

import edu.wpi.cs.eutrepe.dto.SnippetDto;

public class SnippetResponse {
	SnippetDto content;
	Integer httpCode;
	String msg;

	public SnippetDto getSnippet() {
		return content;
	}
	public void setSnippet(SnippetDto snippet) {
		this.content = snippet;
	}
	public Integer getHttpCode() {
		return httpCode;
	}
	public void setHttpCode(Integer httpCode) {
		this.httpCode = httpCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
