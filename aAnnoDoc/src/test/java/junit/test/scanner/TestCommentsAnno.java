package junit.test.scanner;

import org.openon.aannodoc.annotation.aDoc;

/**aDocTestCommentsAnno**/
@aDoc
/** TestCommentsAnno **/
public @interface TestCommentsAnno {

	/**aDoc**/
	@aDoc
	/**aktion**/
	public String aktion() default "";
	
	/**aDoc**/
	@aDoc
	/**aktion2**/
	public String aktion2() default "";
	
	/** Deprecated **/
	@Deprecated
	/**
	 * length
	 */	
	public int length() default 0;	
}
