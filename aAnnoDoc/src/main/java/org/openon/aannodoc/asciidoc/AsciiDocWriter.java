package org.openon.aannodoc.asciidoc;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

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
	
	public AsciiDocWriter(OutputStream wr) {  this.wr=new PrintWriter(wr); }
	public AsciiDocWriter(PrintWriter wr) { this.wr=wr; }
	public AsciiDocWriter(Writer wr) { this.wr=new PrintWriter(wr); }
	public AsciiDocWriter() { this.sw=new StringWriter(); this.wr=new PrintWriter(sw); }
	
	public void close() { wr.flush(); wr.close(); }
	
	/** text **/
	public AsciiDocWriter text(Object text) { return w(text);}
	/** paragraph **/
	public AsciiDocWriter paragraph(Object text) { return nnl().w(text);}
	public AsciiDocWriter literalBlock(Object text) { return nnl().wnl("....").w(text).nnl().wnl("....");}
	public AsciiDocWriter literalBlock() { return nnl().wnl("....");}
	public AsciiDocWriter literalBlockEnd() { return nnl().wnl2("....");}
	
	public AsciiDocWriter pass() { return nnl().w("pass:");}
	public AsciiDocWriter pass(Object obj) { return nnl().w("pass:").w(obj);}
	
	/** lead paragraph **/
	public AsciiDocWriter lead(String text) { return nl().w("[.lead]").nl().w(text);}
	public AsciiDocWriter litheral(String text) { nl(); for (String line : text.split("\n")) { w(" ");w(line); } return this;}
	public AsciiDocWriter note(String text) { return nnl().w("NOTE: ").w(text).nnl2();  }
	public AsciiDocWriter tip(String text) { return nnl().w("TIP: ").w(text).nnl2(); }
	public AsciiDocWriter important(String text) { return nnl().w("IMPORTANT: ").w(text).nnl2();  }
	public AsciiDocWriter warning(String text) { return nnl().w("WARNING: ").w(text).nnl2();  }
	public AsciiDocWriter caution(String text) { return nnl().w("CAUTION: ").w(text).nnl2();  }
	
	public AsciiDocWriter bold(String text) {  return w("*").w(text).w("*"); }
	public AsciiDocWriter italic (String text) {  return w("_").w(text).w("_"); }
	public AsciiDocWriter bolditalic(String text) {  return w("*_").w(text).w("_*"); }
	public AsciiDocWriter monospace(String text) {  return w("`").w(text).w("`"); }
	
	public AsciiDocWriter cassia(String text) {  return w("#").w(text).w("#"); }
	public AsciiDocWriter small(String text) {  return w("[.small]#").w(text).w("#"); }
	public AsciiDocWriter big(String text) {  return w("[.big]##").w(text).w("##"); }
	public AsciiDocWriter underline(String text) {  return w("[.underline]#").w(text).w("#"); }
	public AsciiDocWriter linethrough(String text) {  return w("[.line-through]#").w(text).w("#"); }
	
	public AsciiDocWriter superScript(String text) {  return w("^").w(text).w("^"); }
	public AsciiDocWriter subScript(String text) {  return w("~").w(text).w("~"); }
	
	public AsciiDocWriter quotes(String text) {  return w("'`").w(text).w("`'"); }
	public AsciiDocWriter doubleQuotes(String text) {  return w("\"`").w(text).w("'\""); }
	
	public AsciiDocWriter title(Object text) {  return nnl().w("= ").w(text).nl(); }
	public AsciiDocWriter title(Object text,String author,String email) {  return title(text).w(author).w(" <").w(email).w(">").nl(); }
	public AsciiDocWriter title(Object text,String author,String email,String revision) {  return title(text, author, email).w(revision).nl(); }
	public AsciiDocWriter attr(Object text) { return nnl().w(":").w(text).w(":").nl(); }	
	
	public AsciiDocWriter title1(Object text) {  return nnl().w("== ").w(text).nl(); }
	public AsciiDocWriter title2(Object text) {  return title(2,text); }
	public AsciiDocWriter title3(Object text) {  return title(3,text); }
	public AsciiDocWriter title4(Object text) {  return title(4,text); }
	public AsciiDocWriter title5(Object text) {  return title(5,text); }
	public AsciiDocWriter title(int deep,Object text) {  return nnl().w("",deep,"="," ").w(text).nl(); }
	
	public AsciiDocWriter subTitle(Object text) { return title(titleDeep++, text); }
	public AsciiDocWriter subTitleEnd() { titleDeep--; return this; }
	
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
	
	public AsciiDocWriter checked(Object text) { return nnl().w("* [X] ").w(text).nl2(); }
	public AsciiDocWriter checked(int deep,Object text) { return nnl2().w("",deep,"*"," [x] ").w(text).nl2(); }
	public AsciiDocWriter unchecked(Object text) { return nnl().w("* [ ] ").w(text).nl2(); }
	public AsciiDocWriter unchecked(int deep,Object text) { return nnl2().w("",deep,"*"," [ ] ").w(text).nl2(); }
	
	public AsciiDocWriter label(Object text) { return nnl().w(text).w(":: ").nl(); }
	public AsciiDocWriter label(Object label,Object text) { return nnl().w(label).w(":: ").w(text).nl(); }
	
	public AsciiDocWriter reference(Object text) { return w("<<").w(text).w(">>"); }
	
	public AsciiDocWriter table(Object title,Object... heads) { nnl().w(".table ").w(title).nl().w("|===").nl();for(int i=0;heads!=null && i<heads.length;i++) { w("|").w(heads[i]); } return nl(); }
	public AsciiDocWriter tableLine(Object... cells) { for(int i=0;cells!=null && i<cells.length;i++) { nnl().w("|").w(cells[i]); } return nl(); } 
	public AsciiDocWriter tableEnd() { return nnl().wnl("|==="); }
	
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
	
	public AsciiDocWriter w(Object t) { if(t!=null) { w(String.valueOf(t)); } return this; }
	/** write with nl **/
	public AsciiDocWriter wnl(Object t) { if(t!=null) { w(String.valueOf(t)).nnl(); } return this; }
	public AsciiDocWriter wnl2(Object t) { if(t!=null) { w(String.valueOf(t)).nnl2(); } return this; }
	
	public AsciiDocWriter w(char c) { wr.print(c); lastcharNL=(c==NL); if(cacheOn) { cache.append(c); }  return this; }  
	public AsciiDocWriter w(String t) { 
		if(t!=null && t.length()>0) {  
			wr.print(t);
			lastcharNL=t.charAt(t.length()-1)==NL;
			if(cacheOn) { cache.append(t); }
		}
		return this; }
	
	public AsciiDocWriter w(String before,int count,String body,String after) { w(before) ;for(int i=0;i<count;i++) { w(body); } w(after); return this; }

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
}
