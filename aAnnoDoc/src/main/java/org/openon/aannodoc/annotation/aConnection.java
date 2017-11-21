package org.openon.aannodoc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** defien a bug/fix **/
@Retention( RetentionPolicy.RUNTIME ) 
public @interface aConnection {
	String name() default "";
	String desciption() default "";
}