package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** define a paremter reference **/
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aFeature {
	String name() default "";
	String desciption() default "";	
}