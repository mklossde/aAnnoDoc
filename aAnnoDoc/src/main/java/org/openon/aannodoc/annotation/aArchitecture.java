package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document a architecture design of application
 * e.g. define the layer or components 
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="generator/AppDoc/aArchitecture")
@Retention( RetentionPolicy.RUNTIME )
/**
 * 
 * @author cq2klos
 *
 */
public @interface aArchitecture {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	@aAttribute(value="DEFAULT=title attribute from java METHOD/FIELD name")
	String title() default "DEFAULT";
	
	/** description (alternative to java-comment) **/
	String description() default "";
	/** simple or short information **/
	String simple() default "";
	
	/** author of this description **/
	String author() default "";
	/** date of this description **/
	String date() default ""; 
	/** description belongs to version **/
	String version() default ""; 
	/** description is no longer correct **/
	String deprecated() default ""; 
}
