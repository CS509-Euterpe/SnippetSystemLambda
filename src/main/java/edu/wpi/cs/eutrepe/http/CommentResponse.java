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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((httpCode == null) ? 0 : httpCode.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentResponse other = (CommentResponse) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (httpCode == null) {
			if (other.httpCode != null)
				return false;
		} else if (!httpCode.equals(other.httpCode))
			return false;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CommentResponse [comment=" + comment + ", httpCode=" + httpCode + ", msg=" + msg + "]";
	}

 

}
