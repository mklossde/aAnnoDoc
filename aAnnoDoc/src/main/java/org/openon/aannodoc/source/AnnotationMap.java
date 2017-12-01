package org.openon.aannodoc.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationMap {

	/** Map annotationName to List of Annotations (TagAttribute=List AnnotationMap) **/
	protected Map<String,List<AnnotationDoc>> annotationClass=new HashMap<String, List<AnnotationDoc>>();	
	protected List<String> annotationNamesNotInline=new ArrayList<String>();
		
//	protected List<AnnotationDoc> all=new ArrayList<AnnotationDoc>();
//	protected Map<String,AnnotationDoc> annotationNames=new HashMap<String, AnnotationDoc>();
	
	public AnnotationMap() {}
	
//FIXME: wieso wurde erst gesammelt ??	
//	public void add(AnnotationDoc anno) { all.add(anno); }
//	public void init() {
//		for (AnnotationDoc annotationDoc : all) {
//			addAnnotation(annotationDoc);
//		}
//	}
	public void init() {}
	
	//------------------------------------------------------------------
	
	public void add(AnnotationDoc annotation) {		
		String name=annotation.getName();
		if(name==null) { System.err.println("annotion without name "+name); return ; }
		name=name.toLowerCase();
		
		List<AnnotationDoc> list=annotationClass.get(name);
		if(list==null) {
			list=new ArrayList<AnnotationDoc>();
			annotationClass.put(name, list);
		}
		list.add(annotation);
		
		if(!annotation.isInline() && !annotationNamesNotInline.contains(name)) {
			annotationNamesNotInline.add(name);
		}
//		String valueName=annotation.getValueNameString();
//		if(valueName!=null) {
//			AnnotationDoc ann=annotationNames.get(valueName);
//			if(ann==null) annotationNames.put(valueName,annotation);
//		}
//		
//		String refName=(String)annotation.get("reference");
//		if(refName!=null) {
//			annotationNames.put(refName,annotation);
//		}
	}
	
	//------------------------------------------------------------------
	
	public Map<String,List<AnnotationDoc>> getAnnotationMap() { return annotationClass; }
	public List<String> getAnnotationNamesNotInline() { return annotationNamesNotInline; }
	
	
//	/** get annotation doc by valueName **/
//	public AnnotationDoc getAnnotation(String valueName) {		
//		return annotationNames.get(valueName);
//	}
	
//	//------------------------------------------------------------------
//	/** get annotation of annotationSimpleClass by path **/
//	public AnnotationDoc getAnnotation(String path,String annotationSimpleClass)  {
//		List<AnnotationDoc> list=findAnnotation(annotationSimpleClass);
//		for(int i=0;list!=null && i<list.size();i++) {
//			AnnotationDoc doc=list.get(i);
//			if(path==null || doc.is(null,path)) return doc;
//		}
//		return null;
//	}
	
	/** list of annotations,in all classes, by annotationClass **/
	public List<AnnotationDoc> findAnnotation(String annotationSimpleClass) {
		return annotationClass.get(annotationSimpleClass.toLowerCase());
	}
	

}
