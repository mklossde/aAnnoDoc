package org.openon.aannodoc.asciidoc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * http://asciidoctor.org/docs/asciidoc-syntax-quick-reference/#section-titles
 * 
 * 
 *
 */
public class AsciiDocWriter {

//	protected StringBuilder sb=new StringBuilder();
	protected PrintWriter wr;
	protected StringWriter sw;
	
	protected boolean cacheOn=false;
	protected StringBuilder cache=new StringBuilder();
	
	public static char NL='\n';
	protected int titleDeep=1,listDeep=1;
	
	public AsciiDocWriter(String file) throws IOException { this(new FileWriter(file)); }
	
	public AsciiDocWriter(OutputStream wr) {  this.wr=new PrintWriter(wr); }
	public AsciiDocWriter(PrintWriter wr) { this.wr=wr; }
	public AsciiDocWriter(Writer wr) { this.wr=new PrintWriter(wr); }
	public AsciiDocWriter() { this.sw=new StringWriter(); this.wr=new PrintWriter(sw); }
	
	public void close() { wr.flush(); wr.close(); }
	
	/** text **/
	public AsciiDocWriter text(Object text) { return w(text);}
	/** paragraph **/
	public AsciiDocWriter paragraph(Object text) { if(text==null) {return this; } return nnl().w(text);}
	public AsciiDocWriter literalBlock(Object text) { if(text==null) {return this; } return nnl().wnl("....").w(text).nnl().wnl("....");}
	public AsciiDocWriter literalBlock() { return nnl().wnl("....");}
	public AsciiDocWriter literalBlockEnd() { return nnl().wnl2("....");}
	
	public AsciiDocWriter pass() { return nnl().w("pass:");}
	public AsciiDocWriter pass(Object text) { if(text==null) {return this; } return nnl().w("pass:").w(text);}
	
	/** lead paragraph **/
	public AsciiDocWriter lead(Object text) { if(text==null) {return this; } return nl().w("[.lead]").nl().w(text);}
	public AsciiDocWriter litheral(String text) { if(text==null) {return this; } nl(); for (String line : text.split("\n")) { w(" ");w(line); } return this;}
	public AsciiDocWriter note(Object text) { if(text==null) {return this; } return nnl().w("NOTE: ").w(text).nnl2();  }
	public AsciiDocWriter tip(Object text) { if(text==null) {return this; }return nnl().w("TIP: ").w(text).nnl2(); }
	public AsciiDocWriter important(String text) { if(text==null) {return this; } return nnl().w("IMPORTANT: ").w(text).nnl2();  }
	public AsciiDocWriter warning(Object text) { if(text==null) {return this; } return nnl().w("WARNING: ").w(text).nnl2();  }
	public AsciiDocWriter caution(Object text) { if(text==null) {return this; } return nnl().w("CAUTION: ").w(text).nnl2();  }
	
	public AsciiDocWriter bold(Object text) {  return wIf(text!=null,"*",text,"*"); }
	public AsciiDocWriter italic (Object text) {  return wIf(text!=null,"_",text,"_"); }
	public AsciiDocWriter bolditalic(Object text) {  return wIf(text!=null,"*_",text,"_*"); }
	public AsciiDocWriter monospace(Object text) {  return wIf(text!=null,"`",text,"`"); }
	
	public AsciiDocWriter cassia(Object text) {  return wIf(text!=null,"#",text,"#"); }
	public AsciiDocWriter small(Object text) {  return wIf(text!=null,"[.small]#",text,"#"); }
	public AsciiDocWriter big(Object text) {  return wIf(text!=null,"[.big]##",text,"##"); }
	public AsciiDocWriter underline(Object text) {  return wIf(text!=null,"[.underline]#",text,"#"); }
	public AsciiDocWriter linethrough(Object text) {  return w(toLinethrough(text)); }	
	
	public String toLinethrough(Object text) {  String t=toString(text); if(text==null) { return null; } return "[.line-through]#"+t+"#"; }
	
	public AsciiDocWriter superScript(Object text) {  return wIf(text!=null,"^",text,"^"); }
	public AsciiDocWriter subScript(Object text) {  return wIf(text!=null,"~",text,"~"); }
	
	public AsciiDocWriter quotes(Object text) {  return wIf(text!=null,"'`",text,"`'"); }
	public AsciiDocWriter doubleQuotes(Object text) {  return wIf(text!=null,"\"`",text,"'\""); }
	
	public AsciiDocWriter title(Object text) { titleDeep++; return nnl().w("= ").w(text).nl2();   }
	public AsciiDocWriter title(Object text,String author,String email) {  titleDeep++; return nnl().w("= ").w(text).nl().w(author).w(" <").w(email).w(">").nl2(); }
	public AsciiDocWriter title(Object text,String author,String email,String revision) {  titleDeep++; return nnl().w("= ").w(text).nl().w(author).wIf(email!=null," <",email,">").nl().w(revision).nl2(); }
	public AsciiDocWriter attr(Object text) { if(e(text)) return this; return nnl().w(":").w(text).w(":").nl(); }	
	
	public AsciiDocWriter title1(Object text) {   return title(2,text); }
	public AsciiDocWriter title2(Object text) {   return title(3,text); }
	public AsciiDocWriter title3(Object text) {   return title(4,text); }
	public AsciiDocWriter title4(Object text) {   return title(5,text); }
	public AsciiDocWriter title5(Object text) {   return title(6,text); }
	public AsciiDocWriter title(int deep,Object text) {  if(e(text)) { text="[unkown]"; }return nnl2().w("",deep,"="," ").w(text).nl(); }
	
	public AsciiDocWriter subTitle(Object text) { return title(titleDeep++, text); }
	public AsciiDocWriter subTitleEnd() { titleDeep--; return this; }
	
	public AsciiDocWriter list() { return nnl2().w("* "); }
	public AsciiDocWriter list(Object text) { return nnl2().w("* ").w(text).nl2(); }
	public AsciiDocWriter list2(Object text) { return list(2,text); }
	public AsciiDocWriter list3(Object text) { return list(3,text); }
	public AsciiDocWriter list4(Object text) { return list(4,text); }
	public AsciiDocWriter list(int deep,Object text) { return nnl2().w("",deep,"*"," ").w(text).nl2(); }
	public AsciiDocWriter listSub(Object text) { return list(listDeep++,text); }
	public AsciiDocWriter listEnd() { listDeep--; return this; }
	
	public AsciiDocWriter orderd(Object text) { return nnl().w(". ").w(text).nl2(); }
	public AsciiDocWriter orderd2(Object text) { return orderd(2,text); }
	public AsciiDocWriter orderd3(Object text) { return orderd(3,text); }
	public AsciiDocWriter orderd4(Object text) { return orderd(4,text); }
	public AsciiDocWriter orderd(int deep,Object text) { return nnl2().w("",deep,"*"," ").w(text).nl2(); }
	public AsciiDocWriter orderdSub(Object text) { return list(listDeep++,text); }
	public AsciiDocWriter orderdEnd() { listDeep--; return this; }
	
	public AsciiDocWriter checked(Object text) { return nnl().w("* [*] ").w(text).nl2(); }
	public AsciiDocWriter checked(int deep,Object text) { return nnl2().w("",deep,"*"," [*] ").w(text).nl2(); }
	public AsciiDocWriter unchecked(Object text) { return nnl().w("* [ ] ").w(text).nl2(); }
	public AsciiDocWriter unchecked(int deep,Object text) { return nnl2().w("",deep,"*"," [ ] ").w(text).nl2(); }
	
	public AsciiDocWriter label(Object text) { return nnl().w(text).w(":: ").nl(); }
	public AsciiDocWriter label(Object label,Object text) { return nnl().w(label).w(":: ").w(text).nl(); }
	
	public AsciiDocWriter reference(Object ref) { return w(toReference(ref)); }	

	/** role/css of a block **/
	public AsciiDocWriter role(Object role) { nnl(); return w("[.", 1, role, "]"); }
	
	public AsciiDocWriter table(Object title) { tableTitle(title); return tableStart(); }
	public AsciiDocWriter table(Object title,Object... heads) { tableTitle(title); tableOptions(TableWriter.OPTIONS_DEFAULT); tableStart(); return tableHead(heads); }
	public AsciiDocWriter table(Object title,String cols[],String options,Object... heads) { tableTitle(title); tableOptions(options,cols);tableStart(); return tableHead(heads); }
	
	public AsciiDocWriter tableTitle(Object title) { if(title==null) {return this; } return nnl2().w(".").w(title); } 	
	public AsciiDocWriter tableOptions(String options) { if(options==null) { return this; }return nl().w("[").w(options).w("]").nl(); }
	public AsciiDocWriter tableOptions(String options,Object...cols) {		
		if(cols==null && options==null) { return this; }  
		nl().w("[");
//		w("cols=\""); for(int i=0;i<cols.length;i++) { if(i>0) {w(",");} w(cols[i]); } w("\"");
		wList("cols=\"",",","\"",cols);
		w("options=\"",options,"\"");
		return wnl("]");
	}

	public AsciiDocWriter tableStart() { return nnl().w("|===").nl(); }
	public AsciiDocWriter tableHead(Object... heads) { for(int i=0;heads!=null && i<heads.length;i++) { w("|").w(heads[i]); } return nl(); }
	public AsciiDocWriter tableLine(Object... cells) {nnl(); for(int i=0;cells!=null && i<cells.length;i++) { w("|").w(cells[i]); } return nl(); }
	/** table line ONLY if ANY CELL NOT NULL **/
	public AsciiDocWriter tableLineNN(Object... cells) { 
		for(int t=0;cells!=null && t<cells.length;t++) { if(cells[t]==null) { return this; }}
		nnl(); for(int i=0;cells!=null && i<cells.length;i++) { w("|").w(cells[i]); } return nl(); 
	}
	public AsciiDocWriter tableEnd() {return nnl().wnl("|==="); }	
	
	public AsciiDocWriter include(String file) { return include(file,null); }
	public AsciiDocWriter includeOrg(String file) { return include(file,"[tag=b-base-h-co"); }
	public AsciiDocWriter includeXX(String file) { return include(file,"[tag=b-base-h"); }
	public AsciiDocWriter include(String file,String tags) {
		// {asciidoctor-source}  {includedir}
		nnl().w("include::").w(file); if(tags!=null) { w("[").w(tags).w("]"); } else { w("[]"); } return nl2();
//		
//		include::{includedir}/ex-table.adoc[tag=b-base-h-co]
//				----
//
//				[.result]
//				====
//				include::{includedir}/ex-table.adoc[tag=b-base-h]
	}
	
	public AsciiDocWriter footnote(Object text) { return nnl().w("footnote:").w(text).nnl(); }
	public AsciiDocWriter image(String image) { return w("image:").w(image).w("[]"); }
	public AsciiDocWriter imageBlock(Object image,Object alt) { return nnl().w("image::").w(image).w("["+alt+"]"); }

	
			
//-------------------------------------------------------------------#
	protected boolean lastcharNL=false;
	
	/** nedd nl before **/
	public AsciiDocWriter nnl() { if(!lastcharNL) {w(NL);} ;return this; }
	/** one empty line  - need nl before **/ 
	public AsciiDocWriter nnl2() { if(!lastcharNL) {w(NL);} ; w(NL); return this; }

	/** nl=new line **/
	public AsciiDocWriter nl() { w(NL); return this; }
	public AsciiDocWriter nl2() { w(NL).w(NL); return this; }
	
	public AsciiDocWriter w(Object t) { if(t!=null) { w(toString(t)); } return this; }
	public AsciiDocWriter wTrim(Object t) { if(t!=null) { w(toTrim(t)); } return this; }
	/** write with nl **/
	public AsciiDocWriter wnl(Object t) { if(t!=null) { w(toString(t)).nnl(); } return this; }
	public AsciiDocWriter wnl2(Object t) { if(t!=null) { w(toString(t)).nnl2(); } return this; }
	
	//---------------------------------------------------------------------------------------------------------
	/** write if rule **/
	public AsciiDocWriter wIf(boolean rule,Object... texts) { if(rule) { w(texts); } return this;}
	public AsciiDocWriter wIf(boolean rule,String before,Object body,String after) { if(!rule || e(body)) {return this;} w(before); wTrim(body); return w(after); }
	public AsciiDocWriter w(String before,String body,String after) { if(body==null) { return this; } w(before) ;  w(body.trim()); return w(after); }
	public AsciiDocWriter w(String before,int count,String body,String after) { if(body==null) { return this; } w(before) ;for(int i=0;i<count;i++) { w(body.trim()); } w(after); return this; }

	public AsciiDocWriter w(Object... t) { for(int i=0;t!=null && i<t.length;i++) { w((String)valueOf(t[i]));} return this; }
	
	public AsciiDocWriter wList(String before,String del,String after,Object...o) { 
		if(o==null) { return this; } 
		w(before); 
		boolean next=false; for(int i=0;i<o.length;i++) { if(!e(o[i])) { if(next) {w(del);} w(o[i]); next=true; }} 
		return w(after);  
	}
	
	//---------------------------------------------------------------------------------------------------------
	
	public AsciiDocWriter w(char c) { wr.print(c); lastcharNL=(c==NL); if(cacheOn) { cache.append(c); }  return this; }  
	public AsciiDocWriter w(String str) {
		if(str!=null && str.length()>0) {  
			wr.print(str);
			lastcharNL=str.charAt(str.length()-1)==NL;
			if(cacheOn) { cache.append(str); }
		}
		return this;
	}
	public String valueOf(Object o) { return toString(o); }
	public boolean equlsIgnoreCase(Object a,Object b) { if(a==null || b==null) {return false; }return toString(a).equalsIgnoreCase(toString(b)); }
	
	//---------------------------------------------------------------------------------------------------------

	public void flush() { wr.flush(); }
	
	//-------------------------------------------------------------------
	public AsciiDocWriter cache() { cacheOn=true; return this;}
	public String getCache() { cacheOn=false; String str=cache.toString(); cache.delete(0,cache.length()); return str; }
	//-------------------------------------------------------------------
	// Output 
	
	/** get adoc as string **/
	public String toString() { 
		if(sw!=null) { return sw.toString(); } 
		else { return "AsciiWriter "+wr; }
	}
	
	/** get text as reference **/
	public String toReference(Object text) { String t=toString(text); if(t==null || t.length()==0) {return null;} return "<<"+t+">>"; }

	//----------------------------------------------------------------------------	
	/** get string or empty **/
	public String toString(Object o) { return Objects.toString(o,null); }
	public String toString(Object o,String def) { return Objects.toString(o,def); } //if(o==null) { return ""; } else { return o.toString(); }}
	/** get trimmed string **/
	public String toTrim(Object o) {if(o==null) { return null; } else { return String.valueOf(o).trim(); }}
	
	/** is empty **/
	public boolean e(Object o) {
		return o==null || toTrim(o).length()==0;
	}
}
