package org.openon.annodoc.test.scanner;

import org.openon.aannodoc.annotation.aDoc;

/**
 * TestInlineCommentsObj
 * 
 **/
public class TestAttributeObj {

	@aDoc(date="@{NOW}")
	public String test;
		
}
