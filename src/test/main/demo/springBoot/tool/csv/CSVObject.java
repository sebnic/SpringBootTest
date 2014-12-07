package demo.springBoot.tool.csv;

public class CSVObject {

	private String firstValue;
	
	private String secondValue;
	
	private String thirdValue;
	
	private String valueWithoutAnnotation;

	public String getFirstValue() {
		return firstValue;
	}

	@CSVAttributAnnotation(fieldName="first_fieldName")
	public void setFirstValue(String firstValue) {
		this.firstValue = firstValue;
	}

	public String getSecondValue() {
		return secondValue;
	}

	@CSVAttributAnnotation(fieldName="second_fieldName")
	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}

	public String getThirdValue() {
		return thirdValue;
	}

	@CSVAttributAnnotation(fieldName="third_fieldName")
	public void setThirdValue(String thirdValue) {
		this.thirdValue = thirdValue;
	}

	public String getValueWithoutAnnotation() {
		return valueWithoutAnnotation;
	}

	public void setValueWithoutAnnotation(String valueWithoutAnnotation) {
		this.valueWithoutAnnotation = valueWithoutAnnotation;
	}
}
