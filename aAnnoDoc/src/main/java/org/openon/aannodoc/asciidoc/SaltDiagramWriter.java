package org.openon.aannodoc.asciidoc;

import java.io.IOException;

import org.openon.aannodoc.annotation.aDoc;

/**
 * Design graphical interface
 * 
 * @author mk@almi.de
 *
 */
@aDoc(title="diagram/salt diagram")
public class SaltDiagramWriter {

	public static final String treedeeps="++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++";
	public enum Align { left,center,right; }
//	public static final String plutgin="plantuml";
	public static final String salt="salt";
	public static final String empty="                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    ";
	protected AsciiDocWriter wr;
	
	public int index=0,cols=0;
	
	public SaltDiagramWriter(AsciiDocWriter wr)  throws IOException { this(wr,null); }
	public SaltDiagramWriter(AsciiDocWriter wr,String title)  throws IOException { 
		this.wr=wr;
		if(!wr.e(title)) {start(title); }
	}
	
	//--------------------------------------------------------------------
	//diagram
	
	/** start diagram with title of it **/
	public SaltDiagramWriter start(String name) throws IOException {		
		wr.nnl2().w('[').w(salt).w(',').w(name).w(']').nl();
		wr.w("----").nl();
		wr.w("{+").nl();
		return this;
	}
	
	
	
	/** end diagram **/
	public SaltDiagramWriter end()  throws IOException {
		wr.nnl().w("}").nl();
		wr.w("----").nl();
		return this;		
	}
	
	//--------------------------------------------------------------------
	public SaltDiagramWriter empty() { return empty(1); }
	public SaltDiagramWriter empty(int count) { for(int i=0;i<count;i++) { wr.w(".");grid(); } return this; }	
	
	public SaltDiagramWriter text(Object text) { wr.w(text); return grid(); }	
	public SaltDiagramWriter text(String text,int len,Align align) {
		if(text==null) { text=""; }if(len<0) { len=text.length(); }
		
		if(len>empty.length()) { len=empty.length(); } // simple hack for empty	
		StringBuilder sb=new StringBuilder(empty.substring(0,len));		
		if(align==Align.center) { int pos=(len/2)-(text.length()/2); int max=pos+text.length(); if(max>len) { max=len; } sb.replace(pos, max, text); }
		else if(align==Align.right) { int max=text.length(); if(max>len) { max=len; }  sb.replace(len-max, len, text); }
		else { int max=text.length(); if(max>len) { max=len; } sb.replace(0, max, text); }
		wr.w(sb.toString()); 
		return this; 		
	}
	
	public SaltDiagramWriter button(String... text) { return button(-1,text); }
	public SaltDiagramWriter button(int len,String... text) { for(int i=0;i<text.length;i++) { button(text[i],len,null); } return this; }
	
	public SaltDiagramWriter button(String text) { return button(text,-1,null); }
	public SaltDiagramWriter button(String text,int len) { return button(text,len,null); }
	public SaltDiagramWriter button(String text,int len,Align align) {
		grid(); wr.w(" ["); text(text,len,align); wr.w("]").w(" ");  return this;
	}
	
	public SaltDiagramWriter radio(Object text,boolean checked) { return radio(null,text,checked); }
	public SaltDiagramWriter radio(String label,Object text,boolean checked) {
		label(label);
		if(checked) { wr.w(" (X) "); } else {wr.w(" () "); }
		wr.w(text).w(" ");  return grid();
	}
	public SaltDiagramWriter radio(String label,String selected,String... options) { 
		label(label); group();
		if(selected==null) { selected=options[0]; }
		for(int i=0;options!=null && i<options.length;i++) { 
			grid();
			if(options[i].equalsIgnoreCase(selected)) { wr.w(" (X) ").w(options[i]); } else {wr.w(" () ").w(options[i]); }				
		}
		return groupEnd().grid(); 
	}
	
	public SaltDiagramWriter checkbox(Object text,boolean checked) { return checkbox(null,text,checked); }
	public SaltDiagramWriter checkbox(String label,Object text,boolean checked) {
		label(label);
		if(checked) { wr.w(" [X] "); } else {wr.w(" [] "); }
		wr.w(text).w(" ");  return grid();
	}
	public SaltDiagramWriter checkbox(String label,String selected,String... options) { 
		label(label); group();
		for(int i=0;options!=null && i<options.length;i++) { 
			grid();
			if(options[i].equalsIgnoreCase(selected)) { wr.w(" [X] ").w(options[i]); } else {wr.w(" [] ").w(options[i]); }
			
		}
		return groupEnd().grid(); 
	}
	
	
	public SaltDiagramWriter select(Object select) { return select(null,select); }
	public SaltDiagramWriter select(String label,Object select) {
		label(label); wr.w("^").w(select).w("^ ");  return grid();
	}
	
	public SaltDiagramWriter input(String value) { return input(null, value, -1,null ) ; }
	public SaltDiagramWriter input(String value,int len) { return input(null, value, len,null ) ; }
	public SaltDiagramWriter input(String label,String value,int len) { return input(label, value, len,null ) ; }
	public SaltDiagramWriter input(String label,String value,int len,Align align) {
		label(label);
		wr.w("\"");text(value,len,align);wr.w("\" "); return grid();
	}
	
	public SaltDiagramWriter label(String label) { if(label!=null) { wr.w(label).w(" "); } return grid(); }
	
	//------------------------------------------------------------------
	// Group 
	
	public int groupCount=-1;
	
	/** group of radios,checkbox,buttons **/
	public SaltDiagramWriter group() {groupCount=0; wr.w(" { "); return this; }
	public SaltDiagramWriter groupEnd() { groupCount=-1; wr.w(" } "); return this; }
	
	//------------------------------------------------------------------
	// Form 
	
	/** start form +=border, !sendkrechte,-waagerechte,+=alle**/
	public SaltDiagramWriter form() { return form(-1,'+'); }
	public SaltDiagramWriter form(int cols) { return form(cols,'+'); }
	public SaltDiagramWriter form(int cols,boolean boarder) { if(boarder) { return form(cols,'+');}else { return form(cols,' ');} }
	public SaltDiagramWriter form(int cols,char boarders) {
		wr.nnl().w("{");
		if(boarders!=' ') { wr.w(boarders); }
		wr.nl(); this.cols=cols; return this;
	}
	
	/** end form **/
	public SaltDiagramWriter formEnd() {  cols=-1; wr.nnl().w("}").nl(); return this;}
	
	public SaltDiagramWriter formLine() { return formLine("--"); }
	public SaltDiagramWriter formLine(String type) { 
		for(int i=index;i<cols;i++) {  wr.w(" ").w(type).w(" "); grid(); } 
		return this;
	}
	
	/** paint grid of from **/
	protected SaltDiagramWriter grid() {
		if(groupCount>=0) { if(groupCount++>0) {  wr.w(" | "); } return this; } 
		if(cols<0) {} else if(index++<cols-1) { wr.w(" | "); } else { wr.nl(); index=0; } return this; }

	//------------------------------------------------------------------
	// Menu
	
	public int menuCount=0;
	
	/** write a tab line **/
	public SaltDiagramWriter menu(String selected,Object... entrys) {
		menu(); for(int i=0;entrys!=null && i<entrys.length;i++) {
			if(entrys[i]!=null) { menu(entrys[i],selected!=null && wr.equlsIgnoreCase(selected,entrys[i])); }} return menuEnd();
	}
	public SaltDiagramWriter subMenu(String menu,String selected,String... entrys) {
		for(int i=0;i<entrys.length;i++) {  wr.nl(); menu(menu,true); 
			menu(entrys[i],selected.equalsIgnoreCase(entrys[i]));
			
		} return menuEnd();
	}
	
	/** star new tab line **/	
	public SaltDiagramWriter menu() { wr.nnl().w("{* "); return this; }
	/** write one tab **/
	public SaltDiagramWriter menu(Object entrys,boolean selected) {
		if(menuCount++>0) { wr.w(" | "); } if(selected) { wr.w("<b>"); } wr.w(entrys); return this;
	}
	/** end tab line **/
	public SaltDiagramWriter menuEnd() { wr.w(" }").nl(); return this; }
	
	//------------------------------------------------------------------
	// Tabs
	public int tabCount=0;
	
	/** write a tab line **/
	public SaltDiagramWriter tab(String selected,String... tabs) {
		tab(); for(int i=0;i<tabs.length;i++) {  tab(tabs[i],selected.equalsIgnoreCase(tabs[i])); } return tabsEnd();
	}
	/** star new tab line **/	
	public SaltDiagramWriter tab() { tabCount=0; wr.nnl().w("{/ "); return this; }
	/** write one tab **/
	public SaltDiagramWriter tab(String tab,boolean selected) {
		if(tabCount++>0) { wr.w(" | "); } if(selected) { wr.w("<b>"); }wr.w(tab); return this;
	}
	/** end tab line **/
	public SaltDiagramWriter tabsEnd() { wr.w(" }").nl(); return this; }
	
	//------------------------------------------------------------------
	// Line 
	
	/** horizontal line **/
	public SaltDiagramWriter line() { wr.w(" -- "); return grid(); }	
	public SaltDiagramWriter lineDot() { wr.w(".."); return grid(); }
	public SaltDiagramWriter lineDouble() { wr.w("=="); return grid(); }
	public SaltDiagramWriter lineHalf() { wr.w("~~"); return grid(); }
		
	//------------------------------------------------------------------
	// Tree 
	public int treeDeep=1;
	/** start new tree **/
	public SaltDiagramWriter tree() { wr.nnl().w("{T").nl(); return this; }
	/** a tree line at deep **/
	public SaltDiagramWriter treeLine(int deep,Object text) { wr.nnl(); wr.w(treedeeps.substring(0,deep)).w(text).nl(); return this; }
	/** a tree line ar actual deep **/
	public SaltDiagramWriter treeLine(Object text) { wr.nnl(); wr.w(treedeeps.substring(0,treeDeep)).w(text).nl(); return this; }
	/** a tree line one down **/
	public SaltDiagramWriter treeSub(Object text) { wr.nnl(); treeDeep++; wr.w(treedeeps.substring(0,treeDeep)).w(text).nl(); return this; }
	/** tree one up **/
	public SaltDiagramWriter treeSubEnd() { wr.nnl(); treeDeep--; return this; }
	/** end of tree*/
	public SaltDiagramWriter treeEnd() { wr.nnl().w("}").nl(); return this; }
	
	//------------------------------------------------------------------
	// Table
	
//	public int tableCol=0;
	public SaltDiagramWriter table() { wr.nnl().w("{# "); return this; }
	public SaltDiagramWriter tableLine(Object... entrys) {
		wr.nnl(); for(int i=0;i<entrys.length;i++) {
			if(i>0) { wr.w(" | "); }
			wr.w(entrys[i]); 
		} 
		wr.nl(); return this;
	}
	public SaltDiagramWriter tableEnd() { wr.nnl().wnl("}"); return this; }
}
