package demo.springBoot.tool.csv;

public class CSVObjectWithBadSignatureMethodWithTwoParameters extends CSVObject {

	private String withTwoParameters;

	public String getWithTwoParameters() {
		return withTwoParameters;
	}

	@CSVAttributAnnotation(fieldName="withTwoParameters_fieldName")
	public void setWithTwoParameters(String withTwoParameters, String badParameter) {
		this.withTwoParameters = withTwoParameters;
	}
	
	
}
