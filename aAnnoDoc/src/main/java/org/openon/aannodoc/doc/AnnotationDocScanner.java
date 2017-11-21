package org.openon.aannodoc.doc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.source.JavaParserScanner;

/** 
 * Annotation Object and Scanner in javaDoc
 * 
 * 	 @anno		- ok 
 * 	 x@anno 	- only annotation with space in front
 * 	 @ anno		- only annotation with no space behind
 * 
 * 	 @exampe @  	- annotation until next annotation or empty @
 *   @author xxx 	- annotation until end of line
 *   
 * default annotations are ignores
 * 	@see,@include,@author,@sample,@version,@todo - are ignored			
 * 
 */

public class AnnotationDocScanner {

	
	/** name of annotation without \@NAME **/
	public String name;
	/** attribtues in annotation \@see(ATTRKEY=ATTRVALUE) **/
	public Map<String,String> attr;
	
	/** value of annotation \@see VALUE **/
	public String value;
	
	/** index of end after annotation in string **/	
	public int pos;
	
	/** sca to list of string and annotationObjects **/
	public static List<Object> scanToList(String str) {
		List list=new ArrayList();
		int pos=0;
		while(str!=null && pos!=-1 && pos<str.length()) {
			int next=nextAnnotation(str, pos,true);
			
			if(next==-1) { 
				list.add(str.substring(pos));  
//System.out.println("T:"+str.substring(pos));
				pos=next;
			}else {
				if(next>pos) { 
					list.add(str.substring(pos,next)); 
//System.out.println("T:"+str.substring(pos,next));
				}
				AnnotationDocScanner obj=new AnnotationDocScanner(str,next);
//System.err.println("A:"+str.substring(next,obj.pos));
				if(obj!=null) { list.add(obj); pos=obj.pos; }
				else { pos=pos+1; }
			}
		}
		return list;
	}
	
	public static AnnotationDocScanner scan(String str) { return scan(str,0); }
	
	public static AnnotationDocScanner scan(String str,int pos) {
		int next=nextAnnotation(str, pos,true);
		if(next==-1) { return null; }
		else { return new AnnotationDocScanner(str,next); }
	}
	
	//------------------------------------------------------------
	
	public AnnotationDocScanner() {}
	
	public  AnnotationDocScanner(String str) { this(str,0); }
	public  AnnotationDocScanner(String str,int startPos) {
		scanString(str,startPos);
	}
	
	public String get(String name) {
		if(attr==null) { return null; }
		else { return attr.get(name); }
	}
	//------------------------------------------------------------
	
	protected void scanString(String str,int startPos) {
		this.pos=startPos;
		int next;
		if(str.charAt(pos+1)=='<') { 
			next=pos+2; 
		}else {
			next=findAnnotationNameEnd(str, pos+1);
			if(next==pos+1) { pos=next; return ; } // ignore single @
		}
					
			if(next>0 && next>pos+1) { name=str.substring(pos+1,next); }
			else if(next==-1){ name=str.substring(pos+1); }	
//System.out.println("name:" +name+" pos:"+pos);
			pos=next;
			if(pos==-1) { return ; }
	
			// read attributes
			int attrPos=isNextChar(str,pos,'(');
			if(attrPos!=-1) {			
				pos=attrPos+1;
				while(pos>=0 && pos<str.length() && str.charAt(pos)!=')') {
					String attrKey=nextKey(str);		
					pos=nextChar(str, pos, '=');
					String attrVal=nextKey(str);
//System.out.println("attr:"+attrKey+":"+attrVal+":");					
					if(attrKey!=null && attrKey.length()>0 && attrVal!=null) {
						if(attr==null) { attr=new HashMap<String, String>(); }
						attr.put(attrKey, attrVal);
					}
					if(pos!=-1 && pos<str.length() && str.charAt(pos)==',') { pos++; }
				}
				if(pos!=-1) { pos++; } // skip )
			}
			
			pos=nextChar(str,pos);
			if(pos==-1 || pos>=str.length()) { return ; }
			
			// display as source until @
			if(AnnotationDocDefinition.DOC_CODE.equalsIgnoreCase(name) || AnnotationDocDefinition.DOC_EXAMPLE.equalsIgnoreCase(name)) {
//			if(JavaParserScanner.DOC_CODE.equalsIgnoreCase(name)) {
				value=findCodeValue(str, pos);			
			
			}else if(name.startsWith(AnnotationDocDefinition.DOC_HTML)) {
//				if(!name.endsWith(">")) {
//					value=name.substring(1)+" "+findAnnotationValue(str, pos,">");	
//				}else {
//					value=name.substring(1);					
//				}
				name=AnnotationDocDefinition.DOC_HTML;
				value=findHtmlValue(str, pos);	
				
//			}else if(JavaParserScanner.DOC_EXAMPLE.equalsIgnoreCase(name) ) { // @code .... @
//				value=findAnnotationValue(str, pos);
				
			}else if(AnnotationDocDefinition.DOC_AUTHOR.equalsIgnoreCase(name) ){ // @autho my name \n
				value=findAnnotationValue(str, pos, "\n");	
				
			}else {
				value=findSpace(str, pos);
				
//			}else { 
//				value=null;
			}
		
	}
	
	public void add(String key,String value) {
		if(attr==null) { attr=new HashMap<String, String>(); }
		attr.put(key, value);
	}
	
	public String toString() {
//		return "Annotation "+name+" ("+attr+") = '"+value+"'";
		StringBuilder sb=new StringBuilder("@");
		sb.append(name);
		if(this.attr!=null) {
			sb.append('(');
			Iterator<String> it=attr.keySet().iterator();
			while(it.hasNext()) {
				String key=it.next();
				String value=attr.get(key);
				sb.append(key).append("='");
				if(value!=null) sb.append(value);
				sb.append("'");
				if(it.hasNext()) { sb.append(','); }
			}
			 sb.append(')');
		}				 
		if(value!=null) { sb.append(' ').append(value); } 
		sb.append(' ');
		return sb.toString();
	}
	
	//----------------------------------------------
	
	/** find next annotation start **/
	public static int nextAnnotation(String str,int index,boolean onlyNamedAnnotation) {
		int next=str.indexOf("@",index);
		int deep=100;
		while(deep-->0) {
			if(next==-1) { return -1; }		
//			else if(next==str.length()-1) { return enxt; }
			else if(next>0 && (!Character.isWhitespace(str.charAt(next-1))) ) { // without space infront " @" => next
				next=str.indexOf("@",next+1);
			} else if(onlyNamedAnnotation && (next+1>=str.length() || Character.isWhitespace(str.charAt(next+1)))) { // only with letter behind annotation "@ X" => next
				next=str.indexOf("@",next+1);
				// ignore default annotations			
			}else {
				return next;
			}
		}
		return -1;
	}
	
	/** find next unnamed annotation " @ " **/
	public static int nextUnnnamedAnnotation(String str,int index) {
		int next=str.indexOf("@",index);
		int deep=100;
		while(deep-->0) {			
			if(next==-1) { return -1; }		
			else if(next>0 && Character.isWhitespace(str.charAt(next-1)) ) { // without space infront " @" => next
				if(next+1>=str.length()) {
					return next;
				}else if(Character.isWhitespace(str.charAt(next+1))) { // only with letter behind annotation "@ X" => next
					return next;
				}
			}
			next=str.indexOf("@",next+1);
		}
		return -1;
	}
	
	//-----------------------------------------------------------
	
	private int findAnnotationNameEnd(String str, int index) {		
		while(index<str.length()) {
			char c=str.charAt(index);			
			if(Character.isWhitespace(c)) { return index; }
			else if(!Character.isAlphabetic(c)) { return index; }
//			else if(c=='(') { return index; } //nextChar(str,index,')'); }
			else { index++; }
		}
		return -1;
	}
	
	/** find annotation-value (end of annotation) by annoEnd-string **/
	protected String findAnnotationValue(String str,int index,String annoEnd) {
		int end=str.indexOf(annoEnd,index);
		String anValue;
		if(end==-1) {  anValue=str.substring(index); pos=str.length(); }
		else { 
			anValue=str.substring(index,end); 			
			pos=end+annoEnd.length(); 
		}
		return anValue.trim();
	}
		
	/** find value of annotation (end of annotation by next @) **/
	protected String findAnnotationValue(String str,int index) {			
		int end=nextAnnotation(str, index, false); // find " @"
		String anValue;
		if(end==-1) {  anValue=str.substring(index); pos=str.length(); }
		else { 
			anValue=str.substring(index,end); 
			pos=end-1; 
		}
		return anValue.trim();
	}
	
	protected String findCodeValue(String str,int index) {			
		int end=nextUnnnamedAnnotation(str, index); // find " @ "
		String anValue;
		if(end==-1) {  anValue=str.substring(index); pos=str.length(); }
		else { 
			anValue=str.substring(index,end); 
			pos=end+1; 
		}
		return anValue.trim();
	}
	
	protected String findHtmlValue(String str,int index) {			
		int end=str.indexOf('@',index); // find " @ "
		String anValue;
		if(end==-1) {  anValue=str.substring(index); pos=str.length(); }
		else { 
			anValue=str.substring(index,end); 
			pos=end+1; 
		}
		return anValue.trim();
	}
	
	//----------------------------------------------
	//----------------------------------------------
	//----------------------------------------------
	
	//----------------------------------------------
	
//	protected String findLineEnd(String str,int index) {
//		int end=nextChar(str, index, '\n');
//		if(end==-1) { pos=str.length(); return str.substring(index); }
//		else { pos=end; return str.substring(index, end); }
//	}
	
	protected String findSpace(String str,int index) {
//		int end=nextSpace(str, index);
//		if(end==-1) { pos=str.length(); return str.substring(index); }
//		else { pos=end; return str.substring(index, end); }
				
		int start=index;
		while(index<str.length()) {
			char c=str.charAt(index);
			if(Character.isWhitespace(c)) { pos=index; return str.substring(start,pos); }
			else if(c=='@') {  pos=index; return str.substring(start,pos); }
			else if(c=='\'' || c=='\"' || c=='{' || c=='(') { 
//				pos=nextChar(str,index+1,c); 
				pos=str.indexOf(c,index+1);
				if(pos==-1) { return str.substring(index); }
				else { pos++; return str.substring(index+1,pos-1); }
			} else { index++; }
		}		
		
		pos=str.length(); 
		return str.substring(start); 
	}
	
	private static int nextSpace(String str, int index) {
		while(index<str.length()) {
			char c=str.charAt(index);
			if(Character.isWhitespace(c)) { return index; }
			else if(c=='@') { return index; }
			else if(c=='\'') { return nextChar(str,index+1,'\''); } // end of ''
			else if(c=='\"') { return nextChar(str,index+1,'\"'); } // end of ""
			else if(c=='{') { return nextChar(str,index+1,'}'); } // end of {}
			else if(c=='(') { return nextChar(str,index+1,')'); } // end of ()
			else { index++; }
		}
		return -1;
	}
	

	

	
	/** is next (not space) char ==nextChar => return index of this char **/
	private static int isNextChar(String str, int index,char nextChar) {
		while(index<str.length()) {
			char c=str.charAt(index);
			if(Character.isWhitespace(c)) { index++; }
			else if(c==nextChar) { return index; } //nextChar(str,index,')'); }
			else { return -1; }
		}
		return -1;
	}
		

	
	private static int nextChar(String str, int index,char findChar) {
		while(index!=-1 && index<str.length()) {
			char c=str.charAt(index++);
			if(c==findChar) { return index; }
			else if(c=='\'') { index=nextChar(str,index+1,'\''); } // end of ''
			else if(c=='\"') { index=nextChar(str,index+1,'\"'); } // end of ""
			else if(c=='{') { index=nextChar(str,index+1,'}'); } // end of {}
			else if(c=='(') { index=nextChar(str,index+1,')'); } // end of ()
			else { }
		}
		return -1;
	}
	
	//-------------------------------------------------
	
	/** **/
	private String nextKey(String str) {
		pos=nextChar(str,pos);
		int start=pos;
		while(pos!=-1 && pos<str.length()) {
			char c=str.charAt(pos);
			if(Character.isWhitespace(c) || c=='=' || c==',' || c==')') { return str.substring(start,pos).trim(); }
			else if(c=='\"' || c=='\''|| c=='{' || c=='(') { // end of "" 
				start=pos+1; pos=nextChar(str,start,c);
				if(pos==-1) { return null; }
				else { return str.substring(start,pos-1); } 
			} 
			else { pos++; }
		}
		return null;
	}
	
	private static int nextChar(String str,int index) {
		while(index<str.length() && index!=-1) {
			char c=str.charAt(index);
			if(Character.isWhitespace(c)) { index++; }
			else { return index; }
		}
		return -1;
	}
	
}
