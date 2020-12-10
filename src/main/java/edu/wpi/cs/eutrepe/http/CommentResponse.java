/*
 * CS-509 Team Eutrepe AWS Application
 */

package edu.wpi.cs.eutrepe.http;

import edu.wpi.cs.eutrepe.dto.CommentDto;

public class CommentResponse {
	CommentDto comment;
	Integer httpCode;
	String msg;

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
	public CommentDto getComment() {
		return comment;
	}
	public void setComment(CommentDto comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return "CommentResponse [comment=" + comment + ", httpCode=" + httpCode + ", msg=" + msg + "]";
	}

 

}
