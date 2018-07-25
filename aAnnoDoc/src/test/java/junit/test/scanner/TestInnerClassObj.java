package junit.test.scanner;

import org.openon.aannodoc.annotation.aDoc;

/** aDocTestInnerClassObj **/ @aDoc  /** TestInnerClassObj **/ public class TestInnerClassObj {

	/** aDocinnerClass **/ @aDoc /** innerClass **/
	public class innerClass {
		/** aDocX **/ @aDoc /** x **/ public String x; /** doc **/
	}
	
	/** aDocox **/ @aDoc /** ox **/
	public String ox; 
	
	/** aDocinnerClass2 **/ @aDoc /** innerClass2 **/
	public class innerClass2 {
		/** aDocX **/ @aDoc /** x **/ public String x; /** doc **/
		
		/** aDocinnerClass3 **/ @aDoc 
		/** innerClass3 **/
		public class innerClass3 {
			/** aDocX **/ @aDoc /** x **/ public String x; /** doc **/
		}
	}
}

/** aDocoutClass **/ @aDoc  /** outClass **/
class outClass {
	/** aDocX **/ @aDoc /** x **/ public String x; /** doc **/
}
