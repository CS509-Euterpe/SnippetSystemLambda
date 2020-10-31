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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
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
		DeleteSnippetResponse other = (DeleteSnippetResponse) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
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
		return "DeleteSnippetResponse [content=" + content + ", httpCode=" + httpCode + ", msg=" + msg + "]";
	}

}
