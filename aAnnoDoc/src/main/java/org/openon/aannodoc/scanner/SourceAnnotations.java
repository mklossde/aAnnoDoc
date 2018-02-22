package org.openon.aannodoc.scanner;

import java.io.IOException;
import java.text.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.DocObject;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.utils.DocFilter;

import java.util.Set;

/**
 * Utils to get annotations from scanned-unit
 * 
 * 
 * @author michael
 *
 */
public class SourceAnnotations {

	private JarDoc unit;
	
	public SourceAnnotations(String javaSourceFileOrDirectory,DocFilter filter) throws IOException {
		SourceScanner scanner=new SourceScanner(javaSourceFileOrDirectory,filter);
		this.unit=scanner.getUnit();
	}
	
	public SourceAnnotations(JarDoc unit) {
		this.unit=unit;
	}
	
	public JarDoc doc() { return unit; }
	
	//------------------------------------------------------------------------------------------------
	
	/** list extern class in given pkg (without pkg= list all extern classes) **/
	public List<String> listExternClasses(String pkg,boolean excludeJre) {
		List<String> list=new ArrayList<String>();
		List<String> internClasses=unit.getAllClassNames();
		List<ClassDoc> all=unit.getAllClasses();
		
		for(int i=0;i<all.size();i++) {
			ClassDoc src=all.get(i);	
			externClasses(src, pkg, excludeJre, list, internClasses);
		}
		return list;
	}
	
	/** list extern class in given pkg (without pkg= list all extern classes) **/
	public static void externClasses(ClassDoc src,String pkg,boolean excludeJre,List<String> list,List<String> internClasses) {
		List<String> imports=src.getImports();
		for(int t=0;imports!=null && t<imports.size();t++) {
			String imp=imports.get(t);
			if(pkg!=null && !imp.startsWith(pkg)) ; // wrong package
			else if(excludeJre && imp.startsWith("java.")) ; // exclude 
			else if(internClasses.contains(imp)) ; // internal
			else if(list.contains(imp)) ; // alrady in list
			else list.add(imp); // add
		}
	}
	
	/** get list of all found annotations **/
	public List<String> listAnnotions(boolean includeInlide) {
		if(includeInlide) { return new ArrayList(unit.anno().getAnnotationMap().keySet()); }
		else { return unit.anno().getAnnotationNamesNotInline(); }
	}
	
	//----------------------------------------------
	
	/** 
	 * 	get a list of values for key of all alltantionClass in className 
	 * 		e.g. list all attribute paths =listAnnotationValues("InputComponent","aAttributeTag","path");
	 * @param className
	 * @param annotationClass
	 * @param key
	 * @return
	 */
	public List<Object> listAnnotationValues(String className,String annotationClass,String key) {
		List<Object> list=new ArrayList<Object>();
		List<AnnotationDoc> annos=listAnnotation(className, annotationClass, null, null,true);
		for (AnnotationDoc aDoc : annos) {
			Object val=aDoc.getValue(key);
			if(val!=null && !list.contains(val)) list.add(val);
		}
		return list;
	}
	
	/** list all annotationClass in className **/
	public List<AnnotationDoc> listAnnotation(String className,String annotationClass) {return listAnnotation(className, annotationClass, null, null,true);}
	public List<AnnotationDoc> listAnnotation(String className,String annotationClass,String key,String value) {return listAnnotation(className, annotationClass, key, value,true);}
	public List<AnnotationDoc> listAnnotation(String className,String annotationClass,String key,String value,boolean deep) {
		ClassDoc cs=unit.findClass(className);
		if(cs==null) return null;
		return listAnnotation(cs, annotationClass, key, value, deep);	
	}
		
	public List<AnnotationDoc> listAnnotation(ClassDoc cs,String annotationClass){return listAnnotation(cs, annotationClass, null, null, true); }
	
	public List<AnnotationDoc> listAnnotation(ClassDoc cs,String annotationClass,String key,String value,boolean deep) {
		List<AnnotationDoc> list=new ArrayList<AnnotationDoc>();
		if(cs==null) return list;
		List<AnnotationDoc> all=cs.getAnnotationsInClass();
		for(int i=0;all!=null && i<all.size();i++) {
			AnnotationDoc s=all.get(i);
			if(annotationClass==null || s.equals(annotationClass)) {
				if(key==null) addAnnotation(list,s);
				else if(s.has(key,value)) addAnnotation(list,s);
			}
		}
		
		if(deep) {		
			// include extends 
			String extendName=cs.getExtends();
			if(extendName!=null) {
				List<AnnotationDoc> extendList=listAnnotation(extendName, annotationClass, key, value, deep);
				for(int i=0;extendList!=null && i<extendList.size();i++) {
					AnnotationDoc sc=extendList.get(i);
					addAnnotation(list,sc);
				}
			}
				
			List<String> prevList=cs.getImplements();
			for(int i=0;prevList!=null && i<prevList.size();i++) {
				List<AnnotationDoc> extendList=listAnnotation(prevList.get(i), annotationClass, key, value, deep);
				for(int t=0;extendList!=null && t<extendList.size();t++) {
					AnnotationDoc sc=extendList.get(t);
					addAnnotation(list,sc);
				}
			}
		}
		
		// remove disabled/hidden/ignore		
		Iterator<AnnotationDoc> it=list.iterator();
		while(it.hasNext()) {
			AnnotationDoc anno=it.next();
			if(anno.isHide()) it.remove(); 
		}
		return list;		
	}
	
	private void addAnnotation(List<AnnotationDoc> list,AnnotationDoc anno) { 	
		String name=toString(anno.getTitle());
		for(int i=0;name!=null && i<list.size();i++) {
			AnnotationDoc a=list.get(i);
			if(name.equals(a.getTitle())) 
				return ; // newer know
		}		
		list.add(anno);
	}
	
	/** get first annotaion of  annotationClass **/
	public AnnotationDoc getAnnotation(Object annotationClassObject) throws IOException { return getAnnotation(annotationClassObject,null,null); }
	/** get first annotaion of  annotationClass with key=value **/
	public AnnotationDoc getAnnotation(Object annotationClassObject,String key,String value) throws IOException {
		if(unit==null) return null;
		String annotationClass=toAnnotationClassName(annotationClassObject);
		if(annotationClass==null || annotationClass.length()==0) {
			Iterator<String> keys=unit.anno().getAnnotationMap().keySet().iterator();
			while(keys.hasNext()) {
				String k=keys.next();
				if(k!=null && k.length()>0) return getAnnotation(k,key,value);
			}
		}else {
			List<AnnotationDoc> list=unit.anno().findAnnotation(annotationClass);
			for(int i=0;list!=null && i<list.size();i++) {
				AnnotationDoc an=list.get(i);
				if(key==null) return an;
				else if(an.has(key,value)) return an;
			}					
		}
		return null;
	}
	
	/** find all annotations by annotationClass (e.g. all aTag-Annotations) **/ 
	public List<AnnotationDoc> findAnnotation(Object annotationClassObject) { return findAnnotation(annotationClassObject, null, null); }
	
	public List<AnnotationDoc> findAnnotationIn(DocObject doc,Object annotationClassObject,String key,String value) {
		List<AnnotationDoc> list=new ArrayList<AnnotationDoc>(); 
		String annotationClass=toAnnotationClassName(annotationClassObject);
		List sub=doc.listAnnotationType(annotationClass, key, value);
		if(sub!=null) { list.addAll(sub); }
		return list;
	}
	
	/** list of annotations, in all classes, with a key==value **/
	public List<AnnotationDoc> findAnnotation(Object annotationClassObject,String key,String value) {
		List<AnnotationDoc> l=new ArrayList<AnnotationDoc>();
		if(unit==null) return l;
		String annotationClass=toAnnotationClassName(annotationClassObject);
		if(annotationClass==null || annotationClass.length()==0) {
			Iterator<String> keys=unit.anno().getAnnotationMap().keySet().iterator();
			while(keys.hasNext()) {
				String k=keys.next();
				if(k!=null && k.length()>0) l.addAll(findAnnotation(k,key,value));
			}
		}else {
			List<AnnotationDoc> list=unit.anno().findAnnotation(annotationClass);
			for(int i=0;list!=null && i<list.size();i++) {
				AnnotationDoc an=list.get(i);
				if(key==null) { l.add(an); }
				else if(an.has(key,value)) { l.add(an); }
			}					
		}
		return l;
	}
	
	/** wind annotation wich are newer then days **/
	public List<AnnotationDoc> findNewAnnotation(String annotationClass,long newerThendays) {
		List<AnnotationDoc> l=new ArrayList<AnnotationDoc>();
		if(unit==null) return l;
		
		if(annotationClass==null || annotationClass.length()==0) {
			Iterator<String> keys=unit.anno().getAnnotationMap().keySet().iterator();
			while(keys.hasNext()) {
				String key=keys.next();
				if(key!=null && key.length()>0) l.addAll(findNewAnnotation(key,newerThendays));
			}
		}else {
			List<AnnotationDoc> list=unit.anno().getAnnotationMap().get(annotationClass);
			for(int i=0;list!=null && i<list.size();i++) {
				AnnotationDoc an=list.get(i);
				if(an.isNewer(newerThendays)) l.add(an);
			}
		}
		return l;
	}
	
	//---------------------------------------------------------------------
	
	/** get name of annotation for anno **/
	public String toAnnotationClassName(Object anno) {
		if(anno==null) { return null; }
		else if(anno instanceof String) { return (String)anno; }
		else if(anno instanceof Class) { return ((Class)anno).getSimpleName(); }
		else { return anno.getClass().getSimpleName(); }
	}
	
	//---------------------------------------------------------------------
	
	public String toString(Object obj) {
		if(obj==null) { return null; }
		else if(obj instanceof String) { return (String)obj; }
//TODO: resolve;		
		return obj.toString();
	}
}
