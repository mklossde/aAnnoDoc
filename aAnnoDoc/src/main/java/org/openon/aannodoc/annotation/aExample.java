package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** defien a paremter reference **/
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aExample {
	/** name of exmaple **/
	String name() default "";
	String desciption() default "";
	
	String attribute() default "";
	
	String design() default "";
	String reference() default "";
}