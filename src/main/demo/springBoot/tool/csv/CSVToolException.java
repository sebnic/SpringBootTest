package demo.springBoot.tool.csv;

/**
 * 
 * @author Sebastien Nicaisse
 * 
 * This exception is used when an error is detected during the execution of CSVTool's methods.
 *
 */
public class CSVToolException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Type error.
	 *
	 */
	public enum ErrorCase {
		NO_ACCESS_CSV_FILE, BUILDING_CONSTRUCTOR_ERROR, NO_ACCESS_METHOD, EXECUTE_METHOD_ERROR, BAD_METHOD_SIGNATURE
	}

	private ErrorCase errorCase;

	public CSVToolException(ErrorCase errorCase, String message) {
		super(message);
		this.errorCase = errorCase;
	}

	public ErrorCase getErrorCase() {
		return errorCase;
	}
}
