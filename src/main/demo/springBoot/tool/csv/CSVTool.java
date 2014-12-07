package demo.springBoot.tool.csv;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import demo.springBoot.tool.csv.CSVToolException.ErrorCase;

/**
 * 
 * @author Sébastien Nicaisse
 * 
 * This class provides methods to handle CSV files.
 *
 */
public class CSVTool {

	/**
	 * Build objects using informations from a CSV file. The link between the field name and the method name is done by the using of {@link demo.springBoot.tool.csv.CSVAttributAnnotation} The file is
	 * parsed according to <a href=https://tools.ietf.org/html/rfc4180 RFC4180>RFC4180</a>
	 * 
	 * @param fileName
	 *            CSV file name where the informations are extracted.
	 * @param csvObjectClass
	 *            Class of objects to build.
	 * @return The list of built objects
	 * @throws CSVToolException
	 *             This exception is launched: <li>when the file is not reachable,</li> <li>when the constructor is not reachable,</li> <li>when a method is not reachable.</li> <li>When the method
	 *             signature of a setter is bad (each setters signature must be composed a string parameter only).</li>
	 */
	public static List<? extends Object> getCSVObjects(String fileName, Class<? extends Object> csvObjectClass) throws CSVToolException {
		Map<String, String> csvFieldName2objectMethodName = new HashMap<String, String>();
		for (Method method : csvObjectClass.getMethods()) {
			CSVAttributAnnotation csvAttributAnnotation = method.getAnnotation(CSVAttributAnnotation.class);
			if (csvAttributAnnotation != null) {
				Type[] types = method.getGenericParameterTypes();
				if (types.length == 1 && types[0].getTypeName().equals(String.class.getName())) {
					csvFieldName2objectMethodName.put(csvAttributAnnotation.fieldName(), method.getName());
				} else {
					throw new CSVToolException(ErrorCase.BAD_METHOD_SIGNATURE, "The method '" + method.getName() + "' has a bad signature.");
				}
			}
		}
		return getCSVObjects(fileName, csvObjectClass, csvFieldName2objectMethodName);
	}

	/**
	 * Build objects using informations from a CSV file. The file is parsed according to <a href=https://tools.ietf.org/html/rfc4180 RFC4180>RFC4180</a>
	 * 
	 * @param fileName
	 *            CSV file name where the informations are extracted.
	 * @param csvObjectClass
	 *            Class of objects to build.
	 * @param csvFieldName2objectMethodName
	 *            Map linking the field name of CSV file and the method name of the object to build.
	 * @return The list of built objects
	 * @throws CSVToolException
	 *             This exception is launched: <li>when the file is not reachable,</li> <li>when the constructor is not reachable,</li> <li>when a method is not reachable.</li>
	 */
	public static List<? extends Object> getCSVObjects(String fileName, Class<? extends Object> csvObjectClass, Map<String, String> csvFieldName2objectMethodName) throws CSVToolException {
		File file = new File(fileName);
		CSVParser csvParser;
		try {
			csvParser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT);
		} catch (IOException e) {
			throw new CSVToolException(ErrorCase.NO_ACCESS_CSV_FILE, "The file '" + fileName + "' is not reachable.");
		}
		List<? extends Object> csvObjects = new ArrayList<Object>();
		try {
			for (CSVRecord csvRecord : csvParser.getRecords()) {
				Object csvObject;
				try {
					csvObject = csvObjectClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new CSVToolException(ErrorCase.BUILDING_CONSTRUCTOR_ERROR, "The constructor with no argument is not reachable.");
				}
				for (String csvFieldName : csvFieldName2objectMethodName.keySet()) {
					if (csvRecord.isSet(csvFieldName)) {
						String methodName = csvFieldName2objectMethodName.get(csvFieldName);
						String value = csvRecord.get(csvFieldName);
						Method method;
						try {
							method = csvObjectClass.getMethod(methodName, String.class);
						} catch (NoSuchMethodException | SecurityException e) {
							throw new CSVToolException(ErrorCase.NO_ACCESS_METHOD, "The method '" + methodName + "' is not reachable.");
						}
						try {
							method.invoke(csvObject, value);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							throw new CSVToolException(ErrorCase.EXECUTE_METHOD_ERROR, "Detected error when the method '" + methodName + "' has been executed.");
						}
					}
				}
			}

		} catch (IOException e) {
			throw new CSVToolException(ErrorCase.NO_ACCESS_CSV_FILE, "The file '" + fileName + "' is not reachable.");
		}
		return csvObjects;
	}
}
