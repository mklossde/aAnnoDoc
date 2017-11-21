package org.openon.aannodoc.example;

import java.io.IOException;
import java.io.Serializable;

import org.openon.aannodoc.annotation.aDoc;

/**
 * This is a simple Example of aDoc Annotations on top of class
 */
@aDoc(name="Example/SimpleJava")
/**
 * This is a Java Example Class-Example
 * 
 * @author michael
 * @version 11.11.2017
 * 
 */
public class SimpleJavaExample extends Object implements Serializable {
	/** java-serial-id for serialisation **/
	private static final long serialVersionUID = -8382237118870602745L;
	
	/** this define and describe a string filed **/
	protected String stringField;
	
	/** class instance **/
	public SimpleJavaExample() {	
	}
	
	
	/** this is a simple aDoc t a method **/ 
	@aDoc(name="Example/SimpleMethod")
	/**
	 * document this method API
	 * @return - return nothing 
	 * @exception - thorws a IOException
	 */
	public String simpleMethod()  throws IOException { return null;}
	
	/** this is a second method **/
	@aDoc
	public void secondMethod() {}
}
