package demo.springBoot.tool.csv;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.sun.org.apache.xpath.internal.operations.Bool;

import demo.springBoot.tool.csv.CSVToolException.ErrorCase;

/**
 * 
 * @author Sebastien Nicaisse
 * 
 *         This class provides methods to handle CSV files.
 *
 */
public class CSVTool<T> {

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
	public List<T> getCSVObjects(String fileName, Class<T> csvObjectClass) throws CSVToolException {
		Map<String, Method> csvFieldName2objectMethod = new HashMap<String, Method>();
		for (Method method : csvObjectClass.getMethods()) {
			CSVAttributAnnotation csvAttributAnnotation = method.getAnnotation(CSVAttributAnnotation.class);
			if (csvAttributAnnotation != null) {
				csvFieldName2objectMethod.put(csvAttributAnnotation.fieldName(), method);
			}
		}
		return getCSVObjects(fileName, csvObjectClass, csvFieldName2objectMethod);
	}

	/**
	 * Build objects using informations from a CSV file. The file is parsed according to <a href=https://tools.ietf.org/html/rfc4180 RFC4180>RFC4180</a>
	 * 
	 * @param fileName
	 *            CSV file name where the informations are extracted.
	 * @param csvObjectClass
	 *            Class of objects to build.
	 * @param csvFieldName2objectMethod
	 *            Map linking the field name of CSV file and the method of the object to build.
	 * @return The list of built objects
	 * @throws CSVToolException
	 *             This exception is launched: <li>when the file is not reachable,</li> <li>when the constructor is not reachable,</li> <li>when a method is not reachable.</li>
	 */
	public List<T> getCSVObjects(String fileName, Class<T> csvObjectClass, Map<String, Method> csvFieldName2objectMethod) throws CSVToolException {
		URL resourceFileUrl = getClass().getResource(fileName);
		if (resourceFileUrl == null) {
			throw new CSVToolException(ErrorCase.NO_ACCESS_CSV_FILE, "The file '" + fileName + "' doesn't exist.");
		}
		File file = null;
		try {
			file = new File(resourceFileUrl.toURI());
		} catch (URISyntaxException e1) {
			throw new CSVToolException(ErrorCase.NO_ACCESS_CSV_FILE, "The file '" + fileName + "' is not reachable.");
		}
		CSVParser csvParser;
		try {
			csvParser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader());
		} catch (IOException e) {
			throw new CSVToolException(ErrorCase.NO_ACCESS_CSV_FILE, "The file '" + fileName + "' is not reachable.");
		}
		List<T> csvObjects = new ArrayList<T>();
		try {
			for (CSVRecord csvRecord : csvParser.getRecords()) {
				T csvObject;
				try {
					csvObject = csvObjectClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new CSVToolException(ErrorCase.BUILDING_CONSTRUCTOR_ERROR, "The constructor with no argument is not reachable.");
				}
				for (String csvFieldName : csvFieldName2objectMethod.keySet()) {
					if (csvRecord.isSet(csvFieldName)) {
						Method method = csvFieldName2objectMethod.get(csvFieldName);
						String value = csvRecord.get(csvFieldName);
						if (method.getParameterTypes().length != 1) {
							throw new CSVToolException(ErrorCase.BAD_METHOD_SIGNATURE, "The method '" + method.getName() + "' has a bad signature because it isn't composed of one parameter.");
						}
						if (!Modifier.isPublic(method.getModifiers())) {
							throw new CSVToolException(ErrorCase.NO_ACCESS_METHOD, "The method '" + method.getName() + "' is not reachable.");
						}
						Class<?> valueClass = method.getParameterTypes()[0];
						Object valueObject = null;
						if (valueClass.isPrimitive()) {
							String type = valueClass.getTypeName();
							if (type.equals(Boolean.TYPE.getName())) {
								valueObject = Boolean.parseBoolean(value);
							} else if (type.equals(Integer.TYPE.getName())) {
								valueObject = Integer.parseInt(value);
							} else if (type.equals(Double.TYPE.getName())) {
								valueObject = Double.parseDouble(value);
							} else if (type.equals(Float.TYPE.getName())) {
								valueObject = Float.parseFloat(value);
							} else if (type.equals(Long.TYPE.getName())) {
								valueObject = Long.parseLong(value);
							} else if (type.equals(Byte.TYPE.getName())) {
								valueObject = Byte.parseByte(value);
							} else if (type.equals(Short.TYPE.getName())) {
								valueObject = Short.parseShort(value);
							} else if (type.equals(Character.TYPE.getName())) {
								valueObject = value.charAt(0);
							}
						} else {
							try {
								Constructor<?> valueConstructor = valueClass.getConstructor(String.class);
								try {
									valueObject = valueConstructor.newInstance(value);
								} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									throw new CSVToolException(ErrorCase.EXECUTE_METHOD_ERROR, "An error has been bring about by the building of new instance with the constructor '"
											+ valueConstructor.getName() + "'");
								}
							} catch (NoSuchMethodException e) {
								throw new CSVToolException(ErrorCase.BAD_METHOD_SIGNATURE, "The class '" + valueClass.getName() + "' is not existing or its signature is bad.");
							} catch (SecurityException e) {
								throw new CSVToolException(ErrorCase.NO_ACCESS_METHOD, "The constructor of the class '" + valueClass.getName() + "' is not reachable.");
							}
						}
						try {
							method.invoke(csvObject, valueObject);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							throw new CSVToolException(ErrorCase.EXECUTE_METHOD_ERROR, "An error has been bring about by the running of the method '" + method.getName() + "'");
						}
					}
				}
				csvObjects.add(csvObject);
			}

		} catch (IOException e) {
			throw new CSVToolException(ErrorCase.NO_ACCESS_CSV_FILE, "The file '" + fileName + "' is not reachable.");
		}
		return csvObjects;
	}
}
