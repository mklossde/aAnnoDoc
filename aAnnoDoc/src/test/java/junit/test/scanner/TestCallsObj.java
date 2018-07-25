package junit.test.scanner;

public class TestCallsObj {

	public void one() {two();}
	
	public String two() {
		String a=a("1");
		String x=three(a,"2");
		Object o=new TestCallsObj2().six(null);
		int c=new innerClass().four(3);
		return x+o+c;
	}
	
	public String three(String aa,String bb) {				
		return aa+bb ; }
	
	public String a(String aa) { return aa; }
	public Object a(Object aa) { return aa; }
	
	//------------------------------------------------------------------
	
	public class innerClass {
		public Integer four(int a) {return five(a); }
		public int five(Integer b) { return b; }
		
		public String a(String aa) { return aa; }
	}
}
