package edu.wpi.cs.eutrepe.dto;

public enum Language {
	JAVA("JAVA"), JAVASCRIPT("JAVASCRIPT"), PYTHON("PYTHON"), C("C"), CPP("C++"), BASH("BASH"), GO("GO"), PERL("PERL"), TYPESCRIPT("TYPESCRIPT"), HTML("HTML"), NONE("NONE"), CSHARP("C#");

	private String value;

	private Language(String value)
	   {
	      this.value = value;
	   }

	public String toString() {
		return this.value;
	}
}
