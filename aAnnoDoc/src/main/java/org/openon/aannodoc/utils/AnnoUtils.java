package org.openon.aannodoc.utils;

import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.FieldDoc;
import org.openon.aannodoc.source.TypeDoc;

public class AnnoUtils {

	/** get attribute-value of annotation **/
	public static String getValue(AnnotationDoc doc,String key) {
		String value=toString(doc.getValue(key),null);
		return value;
	}
	
	/** get title of annotation **/
	public static String getTitle(AnnotationDoc doc,boolean removePath) {
		String title=toString(doc.getValue(aDoc.fTITLE),null);
		if(title==null) { title=doc.getParent().getName(); }
		if(title!=null && removePath) { int index=title.lastIndexOf('/'); if(index!=-1) { title=title.substring(index+1); } }
		return title;
	}
	
	public static final String getVersion(AnnotationDoc doc,int deep) {
		String version=toString(doc.getValue(aDoc.fVERSION),null);
		if(version==null) { return getVersion((DocObject)doc,deep-1); } else { return version; }
	}
	
	public static final String getAuthor(AnnotationDoc doc,int deep) {
		String author=toString(doc.getValue(aDoc.fAUTHOR),null);
		if(author==null) { return getAuthor((DocObject)doc,deep-1); } else { return author; }
	}
	
	public static final String getAuthor(AnnotationDoc doc) {
		String author=toString(doc.getValue(aDoc.fAUTHOR),null);
		return author;
	}
	
	public static final String getDeprecated(AnnotationDoc doc,int deep) {
		String dep=toString(doc.getValue(aDoc.fDEPRECATED),null);
		if(dep==null) { return getDeprecated((DocObject)doc,deep-1); } else { return dep; }
	}
	
	public static final String getDate(AnnotationDoc doc,int deep) {
		String date=toString(doc.getValue(aDoc.fDATE),null);
		return date;
	}
	
	/** get default-attribute or field-value **/
	public static final String getDefaultOrValue(AnnotationDoc doc) {
		String value=toString(doc.getValue("value"),null);
		if(value!=null && value.length()>0) { return value; }
		Object parent=doc.getParent();
		if(parent instanceof FieldDoc) {
			FieldDoc field=(FieldDoc)parent;
			return toString(field.getValue(),null);
		}
		return null;
	}
	
	//---------------------------------------------------------------------------------
	
	/** get doc of DocObject **/
	public static String getDoc(DocObject doc) {
		if(doc==null) { return null; }
		String text=doc.getComment();
		// add descibtoon of annotation **/
		if(doc instanceof AnnotationDoc) { text=addString(text, ((AnnotationDoc)doc).getValue(aDoc.fDESCIPTION));}
		if(text==null) { return null; }
		text=removeLeadingSpace(text);
		text=removeBackslash(text);
//		if(preFormat) { text=preFormat(text); }
		return text;
	}
	

	
	//---------------------------------------------------------------------------------
	
	public static final String getType(AnnotationDoc doc) {
		DocObject parent=doc.getParent();
		if(parent instanceof TypeDoc) { return ((TypeDoc)parent).getTypeName(); }
		return null;
	}
	
	//---------------------------------------------------------------------------------
	
	public static final String getAuthor(DocObject doc,int deep) {
		if(doc==null || deep<0) { return null; }
		String author=doc.getAuthor();
		if(author==null) { author=getAuthor(doc.getParent(),deep-1); }
		return author;
	}	
		
	public static final String getVersion(DocObject doc,int deep) {
		if(doc==null || deep<0) { return null; }
		String version=doc.getVersion();
		if(version==null) { version=getVersion(doc.getParent(),deep-1); }
		return version;
	}	
	
	public static final String getDeprecated(DocObject doc,int deep) {
		if(doc==null || deep<0) { return null; }
		String dep=doc.getDeprecated();
		if(dep==null) { dep=getDeprecated(doc.getParent(),deep-1); }
		return dep;
	}
	
	public static final String getLineAnnotation(DocObject doc,String key,int deep) {
		if(doc==null || deep<0) { return null; }
		String version=doc.getCommentAnnotationValue(key);
		if(version==null) { version=getLineAnnotation(doc.getParent(),key,deep-1); }
		return version;
	}	
	
	//---------------------------------------------------------------------------------

	
	public static String toString(Object obj,String def) {
		if(obj==null) { return def; }
		return String.valueOf(obj);
	}
	
	//---------------------------------------------------------------------------------

	
	public static String removeLeadingSpace(String text) { return text.replaceAll("\n ", "\n");}
	public static String removeBackslash(String text) {  return text.replaceAll("\\\\", "");}
	public static String preFormat(String text) { return text.replaceAll("\n", " +\n");}
	
	/** add toStrings into one **/
	public static String addString(Object... objs) {
		StringBuilder sb=new StringBuilder();
		for (Object object : objs) {
			if(object!=null) { sb.append(toString(object,"")); }
		}
		if(sb.length()==0) { return null; }
		else { return sb.toString(); }
	}
	
}
