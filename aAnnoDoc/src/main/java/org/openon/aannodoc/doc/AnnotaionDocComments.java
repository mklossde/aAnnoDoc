package org.openon.aannodoc.doc;

import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.JarDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotaionDocComments {
	private Logger LOG=LoggerFactory.getLogger(AnnotationDocDefinition.class);	
	
	public static boolean RESOLVE_REFERENCES=false;
	
	protected JarDoc unit;
	
	public AnnotaionDocComments(JarDoc unit) {
		this.unit=unit;
	}
	
	public String comment(DocObject doc) {
		String com=comment(doc,100);
		com=resolveHtmlTags(com);
		return com;
	}
	
	/** parse the comment, resolve all links and show as html **/
	public String comment(DocObject doc,int deep) {
		String com=doc.comment;			
		if(RESOLVE_REFERENCES) {
			com=resolve(com,"@"+AnnotationDocDefinition.DOC_INCLUDE,1,deep);		
			com=resolve(com,"@"+AnnotationDocDefinition.DOC_SEE,1,deep);
		}
		return com; 
	}
	
	//-----------------------------------------------------------------------------
	
	public String resolveHtmlTags(String str) {
		int pos=0; 
		StringBuilder sb=new StringBuilder(); 
		while(pos!=-1 && pos<str.length()) {
			char c=str.charAt(pos);
			char next=' ';if(pos+1<str.length()) next=str.charAt(pos+1);
			if(c=='<' && next!=' ') {
				int end=str.indexOf(">",pos);
				if(end==-1) { sb.append(str.substring(pos)); pos=end; }
				else { sb.append(str.substring(pos, end+1)); pos=end; }
			}else if(c=='<') sb.append("&lt;");
			else if(c=='>') sb.append("&gt;");
			else if(c=='&') sb.append("&amp;");
			else if(c=='\n') sb.append("<br>");
			else if(c=='\r') ;
			else if(c==' ') sb.append("&nbsp;"); 
			else sb.append(c);
			pos++;
		}
		return sb.toString();
	}
	
	//-----------------------------------------------------------------------------
	
	public String resolve(String str,String find,int param,int deep) {
		if(str==null || str.length()==0 || find==null) return "";
		int findLength=find.length();
		
		StringBuilder sb=new StringBuilder();
		int index=str.indexOf(find);
		if(index==-1) return str;
		else sb.append(str.substring(0,index));
		while(index!=-1 && deep-->0) {			
			
			int end=findEnd(str,index+1);
			String word=next(str,index,end);
			
			String ref=null;
			if(param>0 && end!=-1) {
				int end2=findEnd(str,end+1);
				ref=next(str,end+1,end2);
				index=end2;				
			}else {
				index=end;
			}
			
			String replace=resolveString(word,ref,deep-1);
			if(replace!=null) sb.append(replace);
						
			if(index!=-1) {
				int next=str.indexOf(find,index);
				if(next==-1) sb.append(str.substring(index));
				else sb.append(str.substring(index,next));
				index=next; 
			}
		}
		return sb.toString();
	}
	
	public String resolveString(String find,String ref,int deep) {
		if(find.equals("@"+AnnotationDocDefinition.DOC_SEE)) {
			DocObject anno=resolveReference(ref);			
			if(anno!=null) return anno.toAnker(AnnotationDocDefinition.DOCTYPE_SEE); 
			else return "---SEE NOT FOUND "+find+"("+ref+")---";
		}else if(find.equals("@"+AnnotationDocDefinition.DOC_INCLUDE)) {
			DocObject anno=resolveReference(ref);
			if(anno!=null) return comment(anno,deep-1);
			else return "---INCLUDE NOT FOUND "+find+"("+ref+")---";
		}else {
			return "---UNKOWN "+find+"("+ref+")---";
		}
	}
	
	public DocObject resolveReference(String ref) {
		if(ref==null || unit==null) return null;
		int dotIndex=ref.indexOf("#");
		if(dotIndex!=-1) {				
			String cl=ref.substring(0,dotIndex); 
			String name=ref.substring(dotIndex+1); 
			DocObject docObject=unit.findClass(cl);
			if(docObject==null) {
				LOG.error("unkown reference class "+cl);
				return null;
			}			
			docObject=docObject.getAnnotation(name);
			if(docObject==null) LOG.error("unkown annotation "+name+" in "+cl);
			return docObject;
		}else {
			return unit.getAnnotation(ref);				
		}
	}
	
	//------------------------------------------------------------
	
	protected int findEnd(String str,int index) {
		if(index==str.length()) return -1;
		char c=str.charAt(index); 
		while(Character.isLetterOrDigit(c) || c=='.'  || c=='#') {
			if(++index==str.length()) return -1;
			c=str.charAt(index);
		}
		return index;
	}
	
	protected String next(String str,int start,int end) {
		if(start==-1) return null;
		else if(end==-1 && start<str.length()) return str.substring(start);
		else return str.substring(start,end);
	}
}
