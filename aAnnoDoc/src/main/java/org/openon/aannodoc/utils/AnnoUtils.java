package org.openon.aannodoc.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openon.aannodoc.annotation.aBug;
import org.openon.aannodoc.annotation.aBug.aBugs;
import org.openon.aannodoc.annotation.aChange;
import org.openon.aannodoc.annotation.aChange.aChanges;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.annotation.aFeature.aFeatures;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.DocReference;
import org.openon.aannodoc.source.FieldDoc;
import org.openon.aannodoc.source.TypeDoc;

import net.sourceforge.plantuml.Log;

public class AnnoUtils {

	public static void sortDate(List<AnnotationDoc> list) {
		Comparator comp=newComparatorDate(aDoc.fDATE, null, null);
		Collections.sort(list,comp);
	}
	
	//---------
	
	public static DateFormat df=new SimpleDateFormat("dd.MM.yyyy");
	
	public static Comparator<AnnotationDoc> newComparatorDate(final String key,final String from,final String to) {
		final long f=toTime(from),t=toTime(to);
		return new Comparator<AnnotationDoc>() { 
			@Override public int compare(AnnotationDoc a,AnnotationDoc b) { // for sort
				String da=((AnnotationDoc)a).getValueString(key); long la=toTime(da);
				String db=((AnnotationDoc)b).getValueString(key); long lb=toTime(db);
				return Long.compare(lb,la);
			}
			@Override public boolean equals(Object an) { // for find 
				String d=((AnnotationDoc)an).getValueString(key); long l=toTime(d);	
//System.out.println("compare "+from+" >= "+d+" <= "+to+" "+an);	
				if(l==-1) { return false; }
				else if((f==-1 || l>f) && (t==-1 || l<=t)) {return true;
				}else { return false; }
		}};	
	}	
	
	public static long toTime(String date) {
		if(date==null || date.length()==0) { return -1; }	
		try {
			Date d=df.parse(date);					
			long l=d.getTime();
//System.out.println("d:"+date+" => "+l+" x:"+new Date(l)+" d:"+d);			
			return l;
		}catch(Exception e) { Log.error("parse date exception "+date); return -1; }
	}	
	
	//------------------------------------------------------------------------------------

	/** add all, not doubleto list, create list if need  **/
	public static List add(List list,Object o) {		
		if(o==null) { return list; }
		if(o instanceof List) { for(int i=0;i<((List)o).size();i++) { list=add(list,((List)o).get(i)); }} 
		else if(o instanceof Object[]) { for(int i=0;i<((Object[])o).length;i++) { list=add(list,((Object[])o)[i]); }}
		else {
			for(int i=0;list!=null && i<list.size();i++) { Object a=list.get(i); if(a==o) { return list; }}
			if(list==null) { list=new ArrayList(); }
			list.add(o);
		}
		return list;
	}
	
	//------------------------------------------------------------------------------------
	
	/** find alls changes **/
	public static List<AnnotationDoc> findChanges(SourceAnnotations annoations,String version,String dateFrom,String dateTo) {
		return findAnnotation(annoations, aChange.class, aChanges.class, version, dateFrom, dateTo);
	}
	
	/** find alls features **/
	public static List<AnnotationDoc> findFeatures(SourceAnnotations annoations,String version,String dateFrom,String dateTo) {
		return findAnnotation(annoations, aFeature.class, aFeatures.class, version, dateFrom, dateTo);
	}
	
	public static List<AnnotationDoc> findBugs(SourceAnnotations annoations,String version,String dateFrom,String dateTo) {
		return findAnnotation(annoations, aBug.class, aBugs.class, version, dateFrom, dateTo);
	}
	
	public static List<AnnotationDoc> findAnnotation(SourceAnnotations annoations,Class an,Class anGroup,String version,String dateFrom,String dateTo) {
		List list=new ArrayList();	
		if(version!=null) { list=AnnoUtils.add(list,annoations.findAnnotation(an, anGroup, aDoc.fVERSION, version)); }
		if(dateFrom!=null || dateTo!=null) {
			Comparator comp=newComparatorDate(aDoc.fDATE, dateFrom, dateTo);
			AnnoUtils.add(list,annoations.findAnnotation(an, anGroup, comp)); 
		}		
		return list;
		
	}
	
	//------------------------------------------------------------------------------------
	
	/** get attribute-value of annotation or name of parent (class/method/field) **/
	public static String getValueOrName(AnnotationDoc doc,String key) {
		String value=doc.getValueString(key);
		if(value==null) { value=ReflectUtil.toName(doc.getParent().getName()); }
		return value;
	}
	
	/** get attribute-value of annotation **/
	public static String getValue(AnnotationDoc doc,String key) {
		String value=doc.getValueString(key);
		return value;
	}
	
	/** get group of annotation **/
	public static String getGroup(AnnotationDoc doc) {
		return doc.getValueString(aDoc.fGROUP);
	}
	
	/** get title of annotation **/
	public static String getTitle(AnnotationDoc doc) { return getTitle(doc, false); }
	public static String getTitle(AnnotationDoc doc,boolean removePath) {
		String title=doc.getValueString(aDoc.fTITLE);
		if(title==null && doc.isInline()) { // title of inline doc from comment
			title=doc.getComment();
		}else {
			if(title==null) { title=ReflectUtil.toName(doc.getParent().getName()); }
			if(title!=null && removePath) { int index=title.lastIndexOf('/'); if(index!=-1) { title=title.substring(index+1); } }
		}
		
		if(title!=null) { 
			title=title.trim(); 
			int ret=title.indexOf('\n'); if(ret>5) { title=title.substring(0,ret); }
			if(title.length()>50) { title=title.substring(0,50)+"..."; }
		}
		return title;
	}
	
	public static final String getVersion(AnnotationDoc doc,int deep) {
		String version=doc.getValueString(aDoc.fVERSION);
		if(version==null) { return getVersion((DocObject)doc,deep-1); } else { return version; }
	}
	
	public static final String getAuthor(AnnotationDoc doc,int deep) {
		String author=doc.getValueString(aDoc.fAUTHOR);
		if(author==null) { return getAuthor((DocObject)doc,deep-1); } else { return author; }
	}
	
	public static final String getAuthor(AnnotationDoc doc) {
		String author=doc.getValueString(aDoc.fAUTHOR);
		return author;
	}
	
	public static final String getDeprecated(AnnotationDoc doc,int deep) {
		String dep=doc.getValueString(aDoc.fDEPRECATED);
		if(dep==null) { return getDeprecated((DocObject)doc,deep-1); } else { return dep; }
	}
	
	public static final String getDate(AnnotationDoc doc,int deep) {
		String date=doc.getValueString(aDoc.fDATE);
//		if(date==null) { return getDate((DocObject)doc,deep-1); } else { return date; }
		return date;
	}
	
	/** get default-attribute or field-value **/
	public static final String getDefaultOrValue(AnnotationDoc doc) {
		String value=doc.getValueString("value");
		if(value!=null && value.length()>0) { return value; }
		Object parent=doc.getParent();
		if(parent instanceof FieldDoc) {
			FieldDoc field=(FieldDoc)parent;
			return toString(field.getValue(),null);
		}
		return null;
	}
	
	/** get parameter (key) of annoation as string, or null for empty **/ 
	public static final String get(AnnotationDoc doc,String key) { return doc.getValueString(key); }
		
	//---------------------------------------------------------------------------------
	
	/** write table do doc of title for list **/
	public static void writeTable(AsciiDocWriter w,String title,List<AnnotationDoc> list) throws IOException {
		if(list==null || list.size()==0) { return ; }
		
		List<String> heads=new ArrayList<String>();
		for(int i=0;i<list.size();i++) {
			AnnotationDoc doc=list.get(i);
			Iterator<String> keys=doc.getValueKeys();
			while(keys.hasNext()) {
				String key=keys.next();
				if(!heads.contains(key)) { heads.add(key); }
			}
		}
				
		w.table(title, heads.toArray());
		for(int i=0;i<list.size();i++) {
			AnnotationDoc doc=list.get(i);
			Object cells[]=new Object[heads.size()];
			for(int t=0;t<heads.size();t++) { cells[t]=doc.getValueString(heads.get(t)); }
			w.tableLine(cells);
		}
		w.tableEnd();		
	}
	 
	/** write all parameter as table **/
	public static void writeTable(AsciiDocWriter w,String title,AnnotationDoc doc) throws IOException {
		if(doc==null) { return ; }
		
		List<String> heads=new ArrayList<String>();
		Iterator<String> keys=doc.getValueKeys();
		while(keys.hasNext()) {
			String key=keys.next();
			if(!heads.contains(key)) { heads.add(key); }
		}
				
		w.table(title);
		Object cells[]=new Object[heads.size()];
		for(int t=0;t<heads.size();t++) {
			w.tableLine(heads.get(t),doc.getValueString(heads.get(t)));
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
	public static String getDoc(DocObject doc) { return getComment(doc); } 
	public static String getComment(DocObject doc) {
		if(doc==null) { return null; }
		String text=doc.getComment();
		// add descibtoon of annotation **/
		if(doc instanceof AnnotationDoc) { text=addString(text, ((AnnotationDoc)doc).getValueString(aDoc.fDESCIPTION));}
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
	
	public static final String getAuthor(DocObject doc) { return get(doc,"author",99); }
	public static final String getAuthor(DocObject doc,int deep) {   return get(doc,"author",deep); }
		
	public static final String getVersion(DocObject doc) { return get(doc,"version",99 ); }
	public static final String getVersion(DocObject doc,int deep) { return get(doc,"version",deep); }
	
	public static final String getDeprecated(DocObject doc) {  return get(doc,"deprecated",99); }
	public static final String getDeprecated(DocObject doc,int deep) {  return get(doc,"deprecated",deep); }
	
	public static final String get(DocObject doc,String key,int deep) {
		if(doc==null || deep<0) { return null; }
		String value=null;
		if(doc instanceof AnnotationDoc) { 
			if(doc.getName().equalsIgnoreCase(key)) { value=doc.getComment(); }
		}else {
			AnnotationDoc aDoc=doc.getAnnotation(key); 
			if(aDoc!=null) { value=aDoc.getComment(); }
		}
		if(value!=null) { value=value.trim(); return value; }
		return get(doc.getParent(),key,deep-1); 
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
			if(obj instanceof Integer) { return (Integer)obj; }
			String str=toString(obj,null);
			if(obj==null) { return def; }
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
