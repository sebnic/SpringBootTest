package demo.springBoot.tool.csv;

public class CSVObjectWithBadSignatureMethodWithBadParameterType extends CSVObject {

	private Double withBadParameterType;
	
	@CSVAttributAnnotation(fieldName="withBadParameter_fieldName")
	public void setWithBadParameterType(Double withBadParameterType) {
		this.withBadParameterType = withBadParameterType;
	}
	
	public Double getWithBadParameterType() {
		return withBadParameterType;
	}
}
