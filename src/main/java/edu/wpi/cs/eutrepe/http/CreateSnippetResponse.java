package edu.wpi.cs.eutrepe.http;

import edu.wpi.cs.eutrepe.dto.SnippetDto;

public class CreateSnippetResponse {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((httpCode == null) ? 0 : httpCode.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
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
		CreateSnippetResponse other = (CreateSnippetResponse) obj;
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
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CreateSnippetResponse [snippet=" + content + ", httpCode=" + httpCode + ", msg=" + msg + "]";
	}
}
