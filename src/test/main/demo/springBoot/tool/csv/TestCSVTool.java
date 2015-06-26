package demo.springBoot.tool.csv;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import demo.springBoot.tool.csv.CSVToolException.ErrorCase;


public class TestCSVTool {
	
	private Map<String, Method> name2TestedMethod;
	
	public TestCSVTool() {
		Map<String, Method> methodName2method = getMethodName2Method();
		name2TestedMethod = new HashMap<String, Method>();
		name2TestedMethod.put("first_fieldName", methodName2method.get("setFirstValue"));
		name2TestedMethod.put("second_fieldName", methodName2method.get("setSecondValue"));
		name2TestedMethod.put("third_fieldName", methodName2method.get("setThirdValue"));
		name2TestedMethod.put("withoutAnnotation_fieldName", methodName2method.get("setValueWithoutAnnotation"));
	}
	
	private Map<String, Method> getMethodName2Method() {
		Map<String, Method> name2Method = new HashMap<String, Method>();
		for(Method method : CSVObject.class.getDeclaredMethods()) {
			name2Method.put(method.getName(), method);
		}
		return name2Method;
	}

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
		Assert.assertEquals(3*index, csvObject.getSecondValue());
		Assert.assertEquals("value_" + index + "_3", csvObject.getThirdValue());
	}
	
	@Test
	public void getCSVObjectWithoutAnnotationTest() throws CSVToolException {
		List<CSVObject> csvObjects = new CSVTool<CSVObject>().getCSVObjects("/test.csv", CSVObject.class, name2TestedMethod);
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
	public void getCSVObjectErrorBecauseTwoArgumentsWithoutAnnotation() throws NoSuchMethodException, SecurityException {
		name2TestedMethod.put("withTwoParameters_fieldName", CSVObjectWithBadSignatureMethodWithTwoParameters.class.getMethod("setWithTwoParameters", String.class, String.class));
		try {
			List<CSVObjectWithBadSignatureMethodWithTwoParameters> csvObjects = new CSVTool<CSVObjectWithBadSignatureMethodWithTwoParameters>().getCSVObjects("/test.csv", CSVObjectWithBadSignatureMethodWithTwoParameters.class, name2TestedMethod);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.BAD_METHOD_SIGNATURE, e.getErrorCase());
		}
		name2TestedMethod.remove("withTwoParameters_fieldName");
	}
	
	
	@Test
	public void getCSVObjectErrorBecauseBadArgumentTypeWithoutAnnotation() throws NoSuchMethodException, SecurityException {
		name2TestedMethod.put("withBadParameterType_fieldName", CSVObjectWithBadSignatureMethodWithTwoParameters.class.getMethod("setWithTwoParameters", String.class, String.class));
		try {
			List<CSVObjectWithBadSignatureMethodWithTwoParameters> csvObjects = new CSVTool<CSVObjectWithBadSignatureMethodWithTwoParameters>().getCSVObjects("/test.csv", CSVObjectWithBadSignatureMethodWithTwoParameters.class, name2TestedMethod);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.BAD_METHOD_SIGNATURE, e.getErrorCase());
		}
		name2TestedMethod.remove("withBadParameterType_fieldName");
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
		try {
			List<CSVObject> csvObjects = new CSVTool<CSVObject>().getCSVObjects("/unknownFile.csv", CSVObject.class, name2TestedMethod);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.NO_ACCESS_CSV_FILE, e.getErrorCase());
		}
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
			List<CSVObjectBadConstructor> csvObjects = new CSVTool<CSVObjectBadConstructor>().getCSVObjects("/test.csv", CSVObjectBadConstructor.class, name2TestedMethod);
			Assert.fail("The CSVToolException has not been launched.");
		} catch (CSVToolException e) {
			Assert.assertEquals(ErrorCase.BUILDING_CONSTRUCTOR_ERROR, e.getErrorCase());
		}
	}
}
