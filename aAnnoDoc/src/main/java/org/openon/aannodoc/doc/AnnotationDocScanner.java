package org.openon.aannodoc.doc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.source.AnnotationDoc;

/** 
 * Annotation-Scanner in text
 * 		
 * scanns for any annotation at first position and add text blow to it
 * 
 * \@aDoc(title="example")
 * This is text of a example @see "my exmaples"
 * until a new Annotation at first position starts		
 * 
 */
@aDoc(title="scanner/Text scanner")
public class AnnotationDocScanner {

	/** to scanned text **/
	public String text;
	/** actual scan pos **/	
	public int pos=0,x=0;
	/** is this scann inline **/
	protected boolean inline=false;
	protected int minAnnoPos=1;
	
	/** instance new scanner for text **/
	public AnnotationDocScanner(String text,boolean inline) { this.text=text; this.inline=inline; pos=0; }
	
	//------------------------------------------------------------------------------
	
//	public static void main(String[] args) throws Exception { 
//		String text=ReflectUtil.read(new FileInputStream("C:/Data/ws/gitaAnnoDoc/aAnnoDoc/aAnnoDoc/src/main/java/org/openon/aannodoc/aAnnoDoc.adoc"));
//		AnnotationDocScanner a=new AnnotationDocScanner(text,false);
//		AnnotationDoc doc=a.nextAnnotation();
//		while(doc!=null) {
//			System.out.println("############################################################################");
//System.out.println("doc:"+doc);			
//			doc=a.nextAnnotation();
//		}
//		System.out.println("end");
//	}
//	
	//------------------------------------------------------------------------------
	
	/** get next annotation (unitl null return=no more annotations in text) **/
	public AnnotationDoc nextAnnotation() throws IOException {
		int start=nextAnnotationStart(pos);
		if(start==-1) { return null; }
		
		// get name
		int nameEnd=findAnnotationNameEnd(text,start+1);
		if(nameEnd==-1) { pos=start+1; return nextAnnotation(); } // skip singel \@ 
		String name=text.substring(start+1,nameEnd);
//System.out.println("name:"+name);		
		
		// get attribtues
		Map attr=new HashMap();
		int attrPos=isNextChar(text,nameEnd,'(');
		if(attrPos!=-1) {			
			int aPos=attrPos+1;
			while(aPos<text.length()) {				
				int endPos=isNextChar(text,aPos, ')');
				if(endPos!=-1) { aPos=endPos+1; break; }
				aPos=nextKeyStart(text,aPos);					
				int keyEnd=nextKey(text,aPos);
				if(keyEnd==-1) { throw new IOException("wrong attribute-key '"+text.substring(nameEnd,aPos)+"'");}
				String attrKey=text.substring(aPos,keyEnd).trim();
				aPos=nextChar(text,keyEnd, '=');
				int valEnd=nextKey(text,aPos);
				if(valEnd==-1) { throw new IOException("wrong attribute-value for "+attrKey);}
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
	
	/** convert value to object **/
	public Object toObject(String value) {
//System.out.println("toObject:"+value+":");		
		if(value==null) { return null; }
		else if(value.startsWith("\"") && value.endsWith("\"")) { return value.substring(1, value.length()-1); }
		else if(value.startsWith("\'") && value.endsWith("\'")) { return value.substring(1, value.length()-1); }
		else return value;
	}
	
	//-------------------------------------------------------------------------
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
	
	//-------------------------------------------------------------------------
	
	public static int findValueEnd(String text,int pos) {	
		while(pos<text.length()) {
			char c=text.charAt(pos);		
//			if(!Character.isAlphabetic(c)) { return pos; }
			if(c=='\n' || c=='@') { return pos; } 
			else { pos++; }
		}
		return text.length();
	}
	
	/** find end of annotaion name **/
	public static int findAnnotationNameEnd(String text,int pos) {		
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
	public static int isNextChar(String text, int index,char nextChar) {
		while(index<text.length()) {
			char c=text.charAt(index);
			if(Character.isWhitespace(c)) { index++; }
			else if(c==nextChar) { return index; } //nextChar(str,index,')'); }
			else { return -1; }
		}
		return -1;
	}
	
	public static int nextKeyStart(String text,int pos) throws IOException {
		while(pos<text.length()) {
			char c=text.charAt(pos);
			if(Character.isWhitespace(c) || c==',') { pos++; }
			else { return pos; }
		}
		throw new IOException("keyStart not found"); 
	}
	
	protected static int nextKey(String text,int pos) {
		pos=nextChar(text,pos);
		int start=pos;
		while(pos!=-1 && pos<text.length()) {
			char c=text.charAt(pos);
			if(Character.isWhitespace(c) || c=='=' || c==',' || c==')') { return pos; }
//			else if(c=='\"' || c=='\''|| c=='{' || c=='(') { // end of ""
			else if(c=='{' || c=='(' || c=='"' || c=='\'') { // end of ""
				start=pos+1; pos=nextChar(text,start,c);
				if(pos==-1) { return -1; }
				else { return pos; } 
			} 
			else { pos++; }
		}
		return -1;
	}
	
	public static int nextChar(String text,int index) {
		while(index<text.length() && index!=-1) {
			char c=text.charAt(index);
			if(Character.isWhitespace(c)) { index++; }
			else { return index; }
		}
		return -1;
	}
		
	public static int nextChar(String text,int index,char findChar) {
		while(index!=-1 && index<text.length()) {
			char c=text.charAt(index++);
			if(c==findChar) { return index; }
//			else if(c=='\'') { index=nextChar(index+1,'\''); } // end of ''
//			else if(c=='\"') { index=nextChar(index+1,'\"'); } // end of ""
			else if(c=='{') { index=nextChar(text,index+1,'}'); } // end of {}
			else if(c=='(') { index=nextChar(text,index+1,')'); } // end of ()
			else { }
		}
		return -1;
	}
}
