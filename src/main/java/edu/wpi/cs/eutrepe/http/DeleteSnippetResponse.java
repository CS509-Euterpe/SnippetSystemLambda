/*
 * CS-509 Team Eutrepe AWS Application
 */

package edu.wpi.cs.eutrepe.http;

public class DeleteSnippetResponse {
	Boolean content;
	Integer httpCode;
	String msg;
	
	public Boolean getContent() {
		return content;
	}
	public void setContent(Boolean content) {
		this.content = content;
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
