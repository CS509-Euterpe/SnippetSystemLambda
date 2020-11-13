package edu.wpi.cs.eutrepe.dto;

import java.time.LocalDate;
import java.util.Date;

public class CommentDto {
	Integer id;
	@Override
	public String toString() {
		return "CommentDto [id=" + id + ", snippetId=" + snippetId + ", timestamp=" + timestamp + ", text=" + text
				+ ", startLine=" + startLine + ", endLine=" + endLine + ", startchar=" + startChar + ", endChar="
				+ endChar + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endChar == null) ? 0 : endChar.hashCode());
		result = prime * result + ((endLine == null) ? 0 : endLine.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((snippetId == null) ? 0 : snippetId.hashCode());
		result = prime * result + ((startLine == null) ? 0 : startLine.hashCode());
		result = prime * result + ((startChar == null) ? 0 : startChar.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		CommentDto other = (CommentDto) obj;
		if (endChar == null) {
			if (other.endChar != null)
				return false;
		} else if (!endChar.equals(other.endChar))
			return false;
		if (endLine == null) {
			if (other.endLine != null)
				return false;
		} else if (!endLine.equals(other.endLine))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (snippetId == null) {
			if (other.snippetId != null)
				return false;
		} else if (!snippetId.equals(other.snippetId))
			return false;
		if (startLine == null) {
			if (other.startLine != null)
				return false;
		} else if (!startLine.equals(other.startLine))
			return false;
		if (startChar == null) {
			if (other.startChar != null)
				return false;
		} else if (!startChar.equals(other.startChar))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSnippetId() {
		return snippetId;
	}
	public void setSnippetID(String snippetId) {
		this.snippetId = snippetId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getStartLine() {
		return startLine;
	}
	public void setStartLine(Integer startLine) {
		this.startLine = startLine;
	}
	public Integer getEndLine() {
		return endLine;
	}
	public void setEndLine(Integer endLine) {
		this.endLine = endLine;
	}
	public Integer getStartChar() {
		return startChar;
	}
	public void setStartChar(Integer startChar) {
		this.startChar = startChar;
	}
	public Integer getEndChar() {
		return endChar;
	}
	public void setEndChar(Integer endChar) {
		this.endChar = endChar;
	}
	String snippetId;
	String timestamp;
	String text;
	Integer startLine;
	Integer endLine;
	Integer startChar;
	Integer endChar;
	
	
	
}
