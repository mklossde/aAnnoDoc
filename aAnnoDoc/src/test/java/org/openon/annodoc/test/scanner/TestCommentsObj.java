package org.openon.annodoc.test.scanner;

import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aDoc.aDocs;

//ignore this inline comment

/**TestCommentsObj**/
//ignore this inline comment
public class TestCommentsObj {

	// ignore this inline comment 
	/* ttt */
	/**aDoc**/
	@aDoc
	/**aktion**/ 
	private String aktion;
	
	/**aDoc2**/
	@aDoc()
	/**aktion2**/ 
	private String aktion2;
	
	/**aDoc3**/
	@aDoc(title="title")
	/**aktion3**/ 
	private String aktion3;
		
	/**aDocs**/
	@aDocs({
		/**one**/
		@aDoc,
		/**two**/
		@aDoc(),
		/**three**/
		@aDoc(title="three")	
	})
	/**docs**/
	private String docs;
	
	/**aDocLine**/ @aDoc /**line**/  private String line;	
	/**aDocsLine**/ @aDocs({ /**oneLine**/ @aDoc, /**twoLine**/ @aDoc(), /**threeLine**/ @aDoc(title="threeLine") }) /**docsLine**/ private String docsLine;
		
}
