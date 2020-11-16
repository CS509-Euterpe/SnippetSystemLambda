package edu.wpi.cs.eutrepe.dto;


public class Region {
	
	int startLine;
	@Override
	public String toString() {
		return "Region [startLine=" + startLine + ", endLine=" + endLine + ", startChar=" + startChar + ", endChar="
				+ endChar + "]";
	}

	int endLine;
	int startChar;
	int endChar;
	
	public Region(int startLine, int endLine, int startChar, int endChar) {
		super();
		this.startLine = startLine;
		this.endLine = endLine;
		this.startChar = startChar;
		this.endChar = endChar;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endChar;
		result = prime * result + endLine;
		result = prime * result + startChar;
		result = prime * result + startLine;
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
		Region other = (Region) obj;
		if (endChar != other.endChar)
			return false;
		if (endLine != other.endLine)
			return false;
		if (startChar != other.startChar)
			return false;
		if (startLine != other.startLine)
			return false;
		return true;
	}
	


}
