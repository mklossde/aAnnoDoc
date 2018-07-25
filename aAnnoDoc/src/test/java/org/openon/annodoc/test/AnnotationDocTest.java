package org.openon.annodoc.test;

import org.junit.Test;
import org.openon.aannodoc.doc.AnnotationDocScanner;
import org.openon.aannodoc.source.AnnotationDoc;

public class AnnotationDocTest {

	public static void main(String[] args) throws Exception {
		AnnotationDocTest t=new AnnotationDocTest();
		t.test();
		System.out.println("end");
	}
	
	public void test() throws Exception {
String txt="@ApiResponses({\n"
			+"\n" 
			 +"@ApiResponse(code = 200, message = \"(0000)-\"),\n"
			 +"\n"  
			 +"@ApiResponse(code = 207, message = \"(2011)-\"),\n"
			 +"\n"  
			 +"@ApiResponse(code = 207, message = \"(2012)-\"),\n"
			 +"\n" 
			 +"@ApiResponse(code = 400, message = \"(3037)-\"),\n"
			 +"\n" 
			 +"@ApiResponse(code = 400, message = \"(3041)-\"),\n"
			 +"\n" 
			 +"@ApiResponse(code = 401, message = \"Authorization failed\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 422, message = \"(3038)-\"),\n"
			 +"\n" 
			 +"@ApiResponse(code = 422, message = \"(3043)-\"),\n"
			 +"\n" 
			 +"@ApiResponse(code = 422, message = \"(5004)-\"),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 500, message = \"Internal server error\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 501, message =\n"
			 +"\"The function is (not yet) implemented\", response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 502, message =\n"
			 +"\"Required services not available (e.g. Host, Kunig, IBAN)\",\n"
			 +"response=ErrorDTO.class),\n"
			 +"\n" 
			 +"@ApiResponse(code = 502, message =\n"
			 +"\"Required services not available (e.g. Host, Kunig, IBAN)\",\n"
			 +"response=ErrorDTO.class), })\n";

	AnnotationDocScanner sc=new AnnotationDocScanner(txt, true);
		AnnotationDoc a=sc.nextAnnotation();
		while(a!=null) {
System.out.println("a:"+a);			
			a=sc.nextAnnotation();
		}
	}	
}
