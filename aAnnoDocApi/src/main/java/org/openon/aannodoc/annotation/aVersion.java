package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to document version or change of program 
 * e.g. a attribute of a method or function
 * 
 * 
 * @author Michael Kloss - mk@almi.de
 *
 */
@aDoc(title="annotation/aVersion")
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aVersion {
	/** group this documenation belongs to **/
	String group() default "";	
	/** title or subTitle for documentation **/
	String title() default "";
	
	/** version this change describe **/
	String version() default "";
	/** date of this description **/
	String date() default ""; 
	
	/** description (alternative to java-comment) **/
	String description() default "";
		
	/** author of this description **/
	String author() default "";
	/** approved of version **/
	String approved() default "";
	
	/** information about release of this version **/
	String released() default "";
	/** description is no longer correct **/
	String deprecated() default "";
	
	
	/** internal group - for java below 1.7 **/
	public  @interface aVersions { aVersion[] value() default {}; }

}