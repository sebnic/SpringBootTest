package demo.springBoot.tool.csv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import demo.springBoot.tool.csv.CSVToolException.ErrorCase;


public class TestCSVTool {

	@Test
	public void getCSVObjectWithAnnotationTest() throws CSVToolException {
		List<CSVObject> csvObjects = new CSVTool<CSVObject>().getCSVObjects("/test.csv", CSVObject.class);
		Assert.assertEquals(csvObjects.size(), 3);
		int index = 1;
		for(CSVObject csvObject : csvObjects) {
			assertCsvObject(index, csvObject);
			Assert.assertNull(csvObject.getValueWithoutAnnotation());
			index++;
		}
	}

	private void assertCsvObject(int index, CSVObject csvObject) {
		Assert.assertEquals("value_" + index + "_1", csvObject.getFirstValue());
		Assert.assertEquals("value_" + index + "_2", csvObject.getSecondValue());
		Assert.assertEquals("value_" + index + "_3", csvObject.getThirdValue());
	}
	
	@Test
	public void getCSVObjectWithoutAnnotationTest() throws CSVToolException {
		Map<String, String> fieldName2methodName = getMapWithoutAnnotation();
		List<CSVObject> csvObjects = new CSVTool<CSVObject>().getCSVObjects("/test.csv", CSVObject.class, fieldName2methodName);
		Assert.assertEquals(csvObjects.size(), 3);
		int index = 1;
		for(CSVObject csvObject : csvObjects) {
			assertCsvObject(index, csvObject);
			Assert.assertEquals("value_" + index + "_7", csvObject.getValueWithoutAnnotation());
			index++;
		}
	}
	
	@Test
	public void getCSVObjectErrorBecauseTwoArgumentsWithAnnotation() {
		try {
			List<CSVObjectWithBadSignatureMethodWithTwoParameters> csvObjects = new CSVTool<CSVObjectWithBadSignatureMethodWithTwoParameters>().getCSVObjects("/test.csv", CSVObjectWithBadSignatureMethodWithTwoParameters.class);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.BAD_METHOD_SIGNATURE, e.getErrorCase());
		}
	}
	
	@Test
	public void getCSVObjectErrorBecauseTwoArgumentsWithoutAnnotation() {
		Map<String, String> fieldName2methodName = new HashMap<String, String>();
		fieldName2methodName.put("withTwoParameters_fieldName", "setWithTwoParameters");
		try {
			List<CSVObjectWithBadSignatureMethodWithTwoParameters> csvObjects = new CSVTool<CSVObjectWithBadSignatureMethodWithTwoParameters>().getCSVObjects("/test.csv", CSVObjectWithBadSignatureMethodWithTwoParameters.class, fieldName2methodName);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.BAD_METHOD_SIGNATURE, e.getErrorCase());
		}
	}
	
	@Test
	public void getCSVObjectErrorBecauseBadArgumentTypeWithAnnotation() {
		try {
			List<CSVObjectWithBadSignatureMethodWithBadParameterType> csvObjects = new CSVTool<CSVObjectWithBadSignatureMethodWithBadParameterType>().getCSVObjects("/test.csv", CSVObjectWithBadSignatureMethodWithBadParameterType.class);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.BAD_METHOD_SIGNATURE, e.getErrorCase());
		}
	}
	
	@Test
	public void getCSVObjectErrorBecauseBadArgumentTypeWithoutAnnotation() {
		Map<String, String> fieldName2methodName = new HashMap<String, String>();
		fieldName2methodName.put("withBadParameterType_fieldName", "setWithBadParameterType");
		try {
			List<CSVObjectWithBadSignatureMethodWithTwoParameters> csvObjects = new CSVTool<CSVObjectWithBadSignatureMethodWithTwoParameters>().getCSVObjects("/test.csv", CSVObjectWithBadSignatureMethodWithTwoParameters.class, fieldName2methodName);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.BAD_METHOD_SIGNATURE, e.getErrorCase());
		}
	}
	
	@Test
	public void getCSVObjectErrorUnknownCSVFileWithAnnotation() {
		try {
			List<CSVObject> csvObjects = new CSVTool<CSVObject>().getCSVObjects("/unknownFile.csv", CSVObject.class);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.NO_ACCESS_CSV_FILE, e.getErrorCase());
		}
	}
	
	@Test
	public void getCSVObjectErrorUnknownCSVFileWithoutAnnotation() {
		Map<String, String> fieldName2methodName = getMapWithoutAnnotation();
		try {
			List<CSVObject> csvObjects = new CSVTool<CSVObject>().getCSVObjects("/unknownFile.csv", CSVObject.class, fieldName2methodName);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.NO_ACCESS_CSV_FILE, e.getErrorCase());
		}
	}

	private Map<String, String> getMapWithoutAnnotation() {
		Map<String, String> fieldName2methodName = new HashMap<String, String>();
		fieldName2methodName.put("first_fieldName", "setFirstValue");
		fieldName2methodName.put("second_fieldName", "setSecondValue");
		fieldName2methodName.put("third_fieldName", "setThirdValue");
		fieldName2methodName.put("withoutAnnotation_fieldName", "setValueWithoutAnnotation");
		return fieldName2methodName;
	}
	
	@Test
	public void getCSVObjectErrorBadConstructorWithAnnotation() {
		try {
			List<CSVObjectBadConstructor> csvObjects = new CSVTool<CSVObjectBadConstructor>().getCSVObjects("/test.csv", CSVObjectBadConstructor.class);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.BUILDING_CONSTRUCTOR_ERROR, e.getErrorCase());
		}
	}
	
	@Test
	public void getCSVObjectErrorBadConstructorWithoutAnnotation() {
		try {
			List<CSVObjectBadConstructor> csvObjects = new CSVTool<CSVObjectBadConstructor>().getCSVObjects("/test.csv", CSVObjectBadConstructor.class, getMapWithoutAnnotation());
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.BUILDING_CONSTRUCTOR_ERROR, e.getErrorCase());
		}
	}
}
