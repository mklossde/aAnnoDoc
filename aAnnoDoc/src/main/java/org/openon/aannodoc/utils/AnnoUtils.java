package org.openon.aannodoc.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.DocReference;
import org.openon.aannodoc.source.FieldDoc;
import org.openon.aannodoc.source.TypeDoc;

public class AnnoUtils {

	/** get attribute-value of annotation or name of parent (class/method/field) **/
	public static String getValueOrName(AnnotationDoc doc,String key) {
		String value=toString(doc.getValue(key),null);
		if(value==null) { value=ReflectUtil.toName(doc.getParent().getName()); }
		return value;
	}
	
	/** get attribute-value of annotation **/
	public static String getValue(AnnotationDoc doc,String key) {
		String value=toString(doc.getValue(key),null);
		return value;
	}
	
	/** get group of annotation **/
	public static String getGroup(AnnotationDoc doc) {
		return toString(doc.getValue(aDoc.fGROUP),null);
	}
	
	/** get title of annotation **/
	public static String getTitle(AnnotationDoc doc,boolean removePath) {
		String title=toString(doc.getValue(aDoc.fTITLE),null);
		if(title==null) { title=ReflectUtil.toName(doc.getParent().getName()); }
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
	
	/** write table do doc of title for list **/
	public static void writeTable(AsciiDocWriter w,String title,List<AnnotationDoc> list) throws IOException {
		if(list==null || list.size()==0) { return ; }
		
		List<String> heads=new ArrayList<String>();
		for(int i=0;i<list.size();i++) {
			AnnotationDoc doc=list.get(i);
			Map map=doc.getValues();
			Iterator<String> keys=map.keySet().iterator();
			while(keys.hasNext()) {
				String key=keys.next();
				if(!heads.contains(key)) { heads.add(key); }
			}
		}
				
		w.table(title, heads.toArray());
		for(int i=0;i<list.size();i++) {
			AnnotationDoc doc=list.get(i);
			Object cells[]=new Object[heads.size()];
			for(int t=0;t<heads.size();t++) { cells[t]=doc.getValue(heads.get(t)); }
			w.tableLine(cells);
		}
		w.tableEnd();		
	}
	
	//---------------------------------------------------------------------------------
	
	
	/** get name of annotation for anno **/
	public static String toAnnotationClassName(Object anno) {
		if(anno==null) { return null; }
		else if(anno instanceof String) {
			String name=(String)anno;if(name.startsWith("@")) { name=name.substring(1); }
			return  name;
		}else if(anno instanceof Class) { return ((Class)anno).getSimpleName(); }
		else { return anno.getClass().getSimpleName(); }
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
	/** get array of o **/
	public static Object[] toArray(Object o) { 
		if(o instanceof Object[]) { return ((Object[])o); }
		else if(o instanceof List) { return ((List)o).toArray(); }
		else if(o!=null) { return new Object[]{o}; }
		else { return null; }
	}
	/** get string array for o**/
	public static String[] toArrayString(Object o) {
		Object a[]=toArray(o); if(a==null) {return null; }
		String s[]=new String[a.length];
		for(int i=0;i<a.length;i++) { s[i]=AnnoUtils.toString(a[i],null); }
		return s;
	}
	
	public static String toString(Object obj,String def) {
		if(obj==null) { return def; }
		else if(obj instanceof DocReference) {
			DocReference ref=(DocReference)obj;
			return ref.resolve();
		}else {	return String.valueOf(obj); }
	}
	
	public static int toInteger(Object obj,int def) {
		try {
			if(obj==null) { return def; }
			else if(obj instanceof Integer) { return (Integer)obj; }
			String str=String.valueOf(obj);
			return Integer.parseInt(str);
		}catch(Exception e) { return def; }
		
		
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
	
	//------------------------------------------------------------------------------------
	
	/** get list of all authors in all files **/
	public static List<String> listAuthors(SourceAnnotations annotations) {
		List<String> list=new ArrayList<String>();
		List<AnnotationDoc> l=annotations.findAnnotation("author");
		for(int i=0;l!=null && i<l.size();i++) {
			AnnotationDoc doc=l.get(i);
			String user=doc.getComment();
			if(!list.contains(user)) { list.add(user); }
		}
		return list;
	}
	
	
}
