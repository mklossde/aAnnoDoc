package org.openon.aannodoc.asciidoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** write auto reduce table **/
public class TableWriter {

	public static final String OPTIONS_DEFAULT_NOHEADER="options=\"autowidth,unbreakable\"";
	public static final String OPTIONS_DEFAULT="options=\"header,autowidth,unbreakable,topbot\"";
	protected AsciiDocWriter wr;
	
	protected boolean autoReduceCells=true;
	
	protected String options=OPTIONS_DEFAULT;
	
	protected Object title;
	protected List<Object> headLine;
	protected List<Boolean> use=new ArrayList<Boolean>();
	protected List<String[]> tableLine=new ArrayList<String[]>();
	protected List<String> cols=new ArrayList<String>();
	
	protected Object cells[];
//	protected String[] cols;
	
	public TableWriter(AsciiDocWriter wr,Object title) {
		this.wr=wr;
		this.title=title;
	}
	
	public void setOptions(String options) { this.options=options; }
	
	public TableWriter tableCols(String... cols) {
		this.cols=Arrays.asList(cols);
		return this;
	}
	
	public TableWriter tableHead(Object... heads) {
		this.headLine=new ArrayList<Object>();
		for(int i=0;heads!=null && i<heads.length;i++) { 			
//			String str=use(i,heads[i]);
			String s=wr.toString(heads[i]);
			if(s!=null) {
				int index=s.indexOf('|');
				if(index!=-1) { set(cols,i,s.substring(index+1)); s=s.substring(0, index);  }
				headLine.add(s);
			}
		}
		return this;
	}	
	
//FIXME: implement  getCallValue and setCell for bean reference tables	
//	/** write table body as lines of list **/
//	public TableWriter tableBody(List list) {for(int i=0;list!=null && i<list.size();i++) { tableBodyLine(list.get(i)); } return this; }
//	/** wrtie one body line **/
//	public TableWriter tableBodyLine(Object obj) {
//		if(obj==null) { return this; }
//		String str[]=new String[this.cells.length];
//		boolean found=false;
//		for(int i=0;i<this.cells.length;i++) {
//			Object v=getCallValue(obj,this.cells[i]);
//			str[i]=use(i,v); if(str[i]!=null) { found=true; }}
//		if(found) { tableLine.add(str); }
//		return this;
//	}
//	/** get one cell value of bean **/	
//	public Object getCallValue(Object bean,Object ref) { return "x"; }
	
	
	public TableWriter tableLine(Object... cells) {
		if(cells==null) { return this; }
		String str[]=new String[cells.length];
		boolean found=false;
		for(int i=0;i<cells.length;i++) { str[i]=use(i,cells[i]); if(str[i]!=null) { found=true; }}
		if(found) { tableLine.add(str); }
		return this;
	}
		
	public TableWriter tableEnd() {
//for(int i=0;i<use.size();i++) { System.out.println(i+":"+use.get(i)); }		
		return write(); 
	}
	
	//--------------------------------------------------------------------------------------------------
	
	public TableWriter write() {
//System.out.println("title:"+title);
//		if((headLine==null || headLine.size()==0) && (tableLine==null || tableLine.size()==0) ) { return this; } // no table content
		if(tableLine==null || tableLine.size()==0) { return this; } // no table content
		wr.tableTitle(title);
		// options
		wr.nl().w("[");
		boolean kom=false;
		if(cols!=null && cols.size()>0) { 
			kom=true;
			wr.w("cols=\""); 
			boolean c=false;
			for(int i=0;cols!=null && i<cols.size();i++) { if(use.get(i)) { if(c) {wr.w(",");} wr.w(cols.get(i));c=true; }}  
			wr.w("\"");
		}
		if(options!=null && options.length()>0) { if(kom) { wr.w(",");} wr.w(options); }
		wr.wnl("]");
		
		// start table
		wr.tableStart();
		// head
		for(int i=0;headLine!=null && i<headLine.size();i++) { 
			 if(i<use.size() && use.get(i)==true) { wr.w("|").w(headLine.get(i)); } 
		} 
		wr.nl();
		
		//body
		for(int i=0;tableLine!=null && i<tableLine.size();i++) {
			String cells[]=tableLine.get(i);
//for(int z=0;z<use.size();z++) { System.out.println(z+":"+cells[z]); }				
			wr.nnl(); 
			for(int t=0;cells!=null && t<cells.length;t++) { 
				if(use.get(t)) { wr.w("|").w(cells[t]); } 
			}
			wr.nl();
			
		}
		wr.tableEnd();
		return this;
	}
	
	//-----------------------------------------------------
	
	protected void set(List l,int pos,Object o) {
		for(int i=l.size();i<pos+1;i++) { l.add(null); }
		l.set(pos, o);
	}
	
	protected String use(int col,Object o) {
		String str=wr.toString(o,null);
		Boolean ok;
		if(autoReduceCells) { ok=(str!=null && str.length()>0); }  else { ok=true; }
//System.out.println("x:"+col+" s:"+use.size());		
		if(col>=use.size()) { 
			use.add(ok);  
		}else {
			if(!use.get(col) && ok) { use.set(col, ok);} 
		}
		return str;
	}
	
	//-----------------------------------------------------
	
		
}
