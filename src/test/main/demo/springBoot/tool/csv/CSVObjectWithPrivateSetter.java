package demo.springBoot.tool.csv;

public class CSVObjectWithPrivateSetter extends CSVObject {

	private String withPrivateSetter;

	@CSVAttributAnnotation(fieldName="withPrivateSetter_fieldName")
	private void setWithPrivateSetter(String withPrivateSetter) {
		this.withPrivateSetter = withPrivateSetter;
	}
	
	public String getWithPrivateSetter() {
		return withPrivateSetter;
	}
}
