package org.openon.aannodoc.doc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.utils.ReflectUtil;

/** 
 * Annotation Object and Scanner in javaDoc
 * 		
 * 
 */

public class AnnotationDocScanner {

	/** to scanned text **/
	public String text;
	/** actual scan pos **/	
	public int pos=0,x=0;
	/** is this scann inline **/
	protected boolean inline=false;
	protected int minAnnoPos=1;
	
	public AnnotationDocScanner(String text,boolean inline) { this.text=text; this.inline=inline; pos=0; }
	
	//------------------------------------------------------------------------------
	
	public static void main(String[] args) throws Exception { 
		String text=ReflectUtil.read(new FileInputStream("C:/Data/ws/gitaAnnoDoc/aAnnoDoc/aAnnoDoc/src/main/java/org/openon/aannodoc/aAnnoDoc.adoc"));
		AnnotationDocScanner a=new AnnotationDocScanner(text,false);
		AnnotationDoc doc=a.nextAnnotation();
		while(doc!=null) {
			System.out.println("############################################################################");
System.out.println("doc:"+doc);			
			doc=a.nextAnnotation();
		}
		System.out.println("end");
	}
	
	//------------------------------------------------------------------------------
	
	public AnnotationDoc nextAnnotation() throws IOException {
		int start=nextAnnotationStart(pos);
		if(start==-1) { return null; }
		
		// get name
		int nameEnd=findAnnotationNameEnd(start+1);
		if(nameEnd==-1) { pos=start+1; return nextAnnotation(); } // skip singel \@ 
		String name=text.substring(start+1,nameEnd);
//System.out.println("name:"+name);		
		
		// get attribtues
		Map attr=new HashMap();
		int attrPos=isNextChar(nameEnd,'(');
		if(attrPos!=-1) {			
			int aPos=attrPos+1;
			while(aPos<text.length()) {				
				int endPos=isNextChar(aPos, ')');
				if(endPos!=-1) { aPos=endPos+1; break; }
				aPos=nextKeyStart(aPos);					
				int keyEnd=nextKey(aPos);
				if(keyEnd==-1) { throw new IOException("wrong attribute-key ");}
				String attrKey=text.substring(aPos,keyEnd).trim();
				aPos=nextChar( keyEnd, '=');
				int valEnd=nextKey(aPos);
				if(valEnd==-1) { throw new IOException("wrong attribute-value ");}
				Object attrVal=toObject(text.substring(aPos,valEnd).trim());
				aPos=valEnd;			
//System.out.println("attr:"+attrKey+"="+attrVal+":");					
				if(attrKey!=null && attrKey.length()>0 && attrVal!=null) {
					if(attr==null) { attr=new HashMap<String, String>(); }
					attr.put(attrKey, attrVal);
				}
			}
			start=aPos;
		}else {
			start=nameEnd;
		}
		
		int end=nextAnnotationStart(start+1);
		if(end==-1) { end=text.length(); }		
		String aText=text.substring(start, end);
//System.out.println("a:"+aText);		
		this.pos=end;

		AnnotationDoc doc=new AnnotationDoc(name, attr, aText, inline);
		return doc;
	}
	
	
	//-------------------------------------------------------------------------
	
	public Object toObject(String value) {
//System.out.println("toObject:"+value+":");		
		if(value==null) { return null; }
		else if(value.startsWith("\"") && value.endsWith("\"")) { return value.substring(1, value.length()-1); }
		else if(value.startsWith("\'") && value.endsWith("\'")) { return value.substring(1, value.length()-1); }
		else return value;
	}
	
	/** next \@ for annotation **/
	protected int nextAnnotationStart(int pos) {
		while(text!=null && pos<text.length()) {
			char c=text.charAt(pos);
			if(c=='@' && x<=minAnnoPos) { return pos; }
			else if(c=='\n') { x=0; pos++; }
			else { pos++; x++; }
		}
		return -1;
	}
	
	protected int findAnnotationNameEnd(int pos) {		
		while(pos<text.length()) {
			char c=text.charAt(pos);			
			if(Character.isWhitespace(c)) { return pos; }
			else if(!Character.isAlphabetic(c)) { return pos; }
//			else if(c=='(') { return index; } //nextChar(str,index,')'); }
			else { pos++; }
		}
		return -1;
	}
	
	/** is next (not space) char ==nextChar => return index of this char **/
	protected int isNextChar(int index,char nextChar) {
		while(index<text.length()) {
			char c=text.charAt(index);
			if(Character.isWhitespace(c)) { index++; }
			else if(c==nextChar) { return index; } //nextChar(str,index,')'); }
			else { return -1; }
		}
		return -1;
	}
	
	protected int nextKeyStart(int pos) throws IOException {
		while(pos<text.length()) {
			char c=text.charAt(pos);
			if(Character.isWhitespace(c) || c==',') { pos++; }
			else { return pos; }
		}
		throw new IOException("keyStart not found"); 
	}
	
	protected int nextKey(int pos) {
		pos=nextChar(pos);
		int start=pos;
		while(pos!=-1 && pos<text.length()) {
			char c=text.charAt(pos);
			if(Character.isWhitespace(c) || c=='=' || c==',' || c==')') { return pos; }
//			else if(c=='\"' || c=='\''|| c=='{' || c=='(') { // end of ""
			else if(c=='{' || c=='(') { // end of ""
				start=pos+1; pos=nextChar(start,c);
				if(pos==-1) { return -1; }
				else { return pos-1; } 
			} 
			else { pos++; }
		}
		return -1;
	}
	
	protected int nextChar(int index) {
		while(index<text.length() && index!=-1) {
			char c=text.charAt(index);
			if(Character.isWhitespace(c)) { index++; }
			else { return index; }
		}
		return -1;
	}
		
	protected int nextChar(int index,char findChar) {
		while(index!=-1 && index<text.length()) {
			char c=text.charAt(index++);
			if(c==findChar) { return index; }
//			else if(c=='\'') { index=nextChar(index+1,'\''); } // end of ''
//			else if(c=='\"') { index=nextChar(index+1,'\"'); } // end of ""
			else if(c=='{') { index=nextChar(index+1,'}'); } // end of {}
			else if(c=='(') { index=nextChar(index+1,')'); } // end of ()
			else { }
		}
		return -1;
	}
}
