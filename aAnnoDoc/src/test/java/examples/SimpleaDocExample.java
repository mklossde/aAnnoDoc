package examples;

import java.io.IOException;
import java.util.Date;

import org.openon.aannodoc.aAnnoDoc;
import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aBug;
import org.openon.aannodoc.annotation.aInterface;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aError;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.annotation.aService;

/**
 * This is a simple example of using aDoc 
 * 
 * Create a Document with title SimpleExample for this aDoc comment
 * and store it into the file SimpleaDocExample.FORMAT
 * 
 * @author Michael
 * @version ExampleVersion
 * @deprecated This is only a doc example, not for execute
 * 
 */
@aDoc(title="SimpleExample",group="SimpleaDocExample",date="29.11.2017")
/** This is JavaDoc and will not be included into aDoc-docum et **/
public class SimpleaDocExample {

	/**
	 * Generate this example-documentation by java-method
	 * 
	 * 		new aAnnoDoc()
	 * 			.scan("src/test/java"+"/examples"+"/SimpleaDocExample.java",null)
	 * 			.createDocFiles("SimpleaDocExample", null,aAnnoDoc.FORMAT_HTML);
	 * 
	 */
	@aDoc(title="SimpleDoc documenation generation")
	/** this is the java-doc of main-method - which create the aDoc documenation of this file **/
	public static void main(String[] args) throws IOException {
		new aAnnoDoc()
			.scan("src/test/java"+"/examples"+"/SimpleaDocExample.java",null,null)
			.createDocFiles("SimpleaDocExample",aAnnoDoc.GENERATOR_ADOC,aAnnoDoc.FORMAT_HTML);
	}
	
//	/** The programm is called by java examples.aDocExample **/
//	@aDoc(title="Programm call")
//	/** this is the java-doc of main-method **/
//	public static void main(String[] args) throws IOException  {
//		String name=null; if(args.length>0) { name=args[0]; }
//		SimpleaDocExample ex=new SimpleaDocExample(name);
//		ex.doHello(name);
//	}
	
	/** Attribute name - define the secondName how gets the greeting **/
	@aAttribute
	/** this field conaint a simple secondName - given by attribute **/
	protected String secondName="defaultName";
	
	@aAttribute(group="Hello function", title="A second Name",value="unkown",options={"name1","name2","..."}
			,optional="not need",simple="any second name"
			,deprecated="not not use - only for tet"
			,description="this is the long descibtion inside annotation")
	/** this field conaint a simple anme - given by attribute **/
	protected String name="";
	


	/** instance SimpleaDocExample with NAME **/
	public SimpleaDocExample(String name) { if(name!=null)  { this.name=name; } }
	

	/** the default function of this programm is to show Hello NAME to console **/
	@aDoc(title="Hello function")
	public void doHello(String name) throws IOException {
		// simpel show Hello NMAE - this inline code is not included into javaDoc or AnnoDoc
		System.out.println(helloService(name)); 
	}
	
	/** ther is another feature to greets a second person **/
	@aFeature(title="Greets the second name")
	/** greets second name - not realy implemented **/
	@aBug(author="MK",date="30.11.2017",fix="not yet")
	public void doHelloSecond() throws IOException {
		// simpel show Hello NMAE - this inline code is not included into javaDoc or AnnoDoc
		System.out.println(helloService(secondName)); 
	}
	

	/** This is the HelloService, sennd greetings to name **/
	@aService(title="HelloService",request="incoming name",response="greeting text"
		,author="mk",version="0.1",date="30.11.2017",deprecated="only for test")
	@aError(title="IOException",when="name is null",description="throw a ioexception")
	public String helloService(String name) throws IOException {
		if(name==null) { throw new IOException("no name");}
		return "Hello "+name+" at "+getTime();
	}
	
	/** read acutal time from system **/
	@aInterface(title="SystemTime")
	/** a exernal service to get actual time **/
	public String getTime() {
		return new Date().toString();
	}
}
