package demo.springBoot.tool.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Sebastien Nicaisse
 * 
 * This annotation allows to link a field and a method.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CSVAttributAnnotation {

	String fieldName();
}
