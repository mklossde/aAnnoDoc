package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention( RetentionPolicy.RUNTIME ) 
public @interface aValue {
	String name() default "";
	String desciption() default "";
}
