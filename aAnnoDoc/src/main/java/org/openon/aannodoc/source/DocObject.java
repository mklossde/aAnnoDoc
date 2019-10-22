package org.openon.aannodoc.source;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openon.aannodoc.doc.AnnotationDocScanner;
import org.openon.aannodoc.utils.AnnoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;


/**
 * 	name:		
 * 	group:		class of package (the class this belongs to)
 * 	parent:		this Object depends on (e.g. method of annation)
 * 	comment:	Java Comment
 * 
 * 	 
 *
 */
public abstract class DocObject implements Serializable {
	private static final Logger LOG=LoggerFactory.getLogger(DocObject.class);
	
	private static final long serialVersionUID = -7225995181546556626L;

	public DocObject group;
	
	public DocObject parent;
	public String name;
	protected String comment;
		
	/** list of all annotaitons inside this **/
	public List<AnnotationDoc> annotations=new ArrayList<AnnotationDoc>();
	
	/** list of childs docs **/
	public List<DocObject> childs=new ArrayList<DocObject>();
	
	public DocObject(String name,DocObject parent,DocObject group) { 
		this.name=name; 	
		setParent(parent); setGrup(group);
	}
	
	public void setParent(DocObject parent) { this.parent=parent; }
	public void setGrup(DocObject group) { this.group=group; }
	
	/** get lsit of all documenation childs **/
	public List<DocObject> getChilds() { return childs; }
	/**add documentation child to this **/
	public void addChild(DocObject child) { childs.add(child); }
	
	//--------------------------------------------------------------------------
	
	public void setComment(String comment) { setComment(comment,false); }
	public void setComment(String comment,boolean addDouble) {
		if(comment==null || comment.length()==0) { return ; }
		if(this.comment==null || this.comment.length()==0) {
			this.comment=comment; 
		}else if(addDouble){
			this.comment=this.comment+"\n"+comment;
		}else {
			LOG.warn("{} double comment old:'{}' new:'{}'",this,this.comment,comment);
			this.comment=comment; // use only last 
		}
	}

	
	public String getComment() { return comment; }
	
	/** get list of all annotations in object **/
	public List<AnnotationDoc> getAnnotations() { return annotations; }
	/** add annoation **/
	public void addAnnotation(AnnotationDoc anno) { annotations.add(anno);}
//	public void addAnnotations(List<AnnotationDoc> annos) { annotations.addAll(annos); }

//	//--------------------------------------------------------------------------
//	/** get inline annotation author (\@author MYNAME) **/
//	public String getAuthor() { return getCommentAnnotationValue("author"); }
//	/** get inline annotation version (\@version VERSION) **/
//	public String getVersion() { return getCommentAnnotationValue("version"); }
//	/** get inline annotation deprecated (\@deprecated DEPRECATED) **/
//	public String getDeprecated() { return getCommentAnnotationValue("deprecated"); }
	
//	public String getCommentAnnotationValue(String key) {
//		if(comment==null) { return null; }
//		String k="@"+key+" ";
//		int index=comment.indexOf(k);
//		if(index==-1) { return null; }
//		int start=index+k.length();
//		int end=AnnotationDocScanner.findValueEnd(comment,start);
//		if(end==-1) { return null; }
//		return comment.substring(start,end); 
//	}

	
	//--------------------------------------------------------------------------
	
	/** find doc for name **/
	public DocObject findClass(String name) throws IOException {
		if(name==null || name.length()==0) { return null; }
		// find via class
		ClassDoc cl=getClassDoc();
		if(cl!=null) { return cl.findClass(name); } 
		throw new IOException("no class to find "+name);
//		// find via unit
//		JarDoc unit=findJarDoc();  if(unit==null) { return null; }
//		return unit.findClass(name);
	}
	
	/** find jarDoc/unit **/
	public JarDoc findJarDoc() { DocObject parent=this.parent; while(parent!=null && !(parent instanceof JarDoc)) { parent=parent.getParent(); } return (JarDoc)parent;}
	
	//--------------------------------------------------------------------------
	// cross link/anker
	
	
	/** get link to docType **/
	public String link(String docType) { 
//		return "servlet?linkTo=type";
		if(this instanceof ClassDoc) return "apiClass.jsp?cl="+((ClassDoc)this).getTypeName();
		else if(this instanceof PackageDoc) return "apiPkg.jsp?cl="+((PackageDoc)this).getName();
		else if(group instanceof ClassDoc) return "apiPkg.jsp?cl="+((ClassDoc)group).getTypeName();
		else if(group instanceof PackageDoc) return "apiPkg.jsp?cl="+((PackageDoc)group).getName();
		else return "unkown.jsp?cl="+name;
	}
	
	/** get anker to docType **/
	public String toAnker(String docType) { 
		if(name==null || name.length()==0) return "<a href=\""+link(docType)+"\">LINK</a>";
		else return "<a href=\""+link(docType)+"\">"+name+"</a>";
	}
	
	//--------------------------------------------------------------------------
	

	
		
//	//--------------------------------------------------------------------------
//	/** get first annotation for type **/
//	public AnnotationDoc getAnnotationType(String typeName) {
//		for(int i=0;annotations!=null && i<annotations.size();i++) {
//			AnnotationDoc anno=annotations.get(i);
//			if(typeName.equals(anno.getName())) return anno;
//		}
//		return null;
//	}
//	
//	/** get list of annotations for type **/
//	public List<AnnotationDoc> listAnnotationType(String typeName) { return listAnnotationType(typeName,null,null); }
//		
//	/** get list of annotations for type, with key and value **/
//	public List<AnnotationDoc> listAnnotationType(String typeName,String key,String value) {
//		List list=new ArrayList();
//		for(int i=0;annotations!=null && i<annotations.size();i++) {
//			AnnotationDoc anno=annotations.get(i);
//			if(typeName.equals(anno.getName())) {
//				if(key==null || anno.is(key, value)) list.add(anno);
//			}
//		}
//		return list;
//	}
	
	/** get first annotaion with name **/
	public AnnotationDoc getAnnotation(Object annotationClassObject) { 
		String annoName=AnnoUtils.toAnnotationClassName(annotationClassObject);
//		return getAnnotation("name",name); 
		for(int i=0;annotations!=null && i<annotations.size();i++) {
			AnnotationDoc anno=annotations.get(i);
			if(anno.name!=null && anno.name.equals(annoName)) return anno;
		}
		return null;
	}
	/** get first annotation with key==value of this class **/
	public AnnotationDoc getAnnotation(Object annotationClassObject,String key,String value) {
		String annoName=AnnoUtils.toAnnotationClassName(annotationClassObject);
		for(int i=0;annotations!=null && i<annotations.size();i++) {
			AnnotationDoc anno=annotations.get(i);
			if((annoName==null || annoName.equals(anno.name)) 
					&& (key==null || anno.has(key, value))) { return anno; }
		}
		return null;
	}
	
	/** list annoation in this class **/
	public List<AnnotationDoc> listAnnotation(Object annotationClassObject) { return listAnnotation(annotationClassObject,null,null); }
	public List<AnnotationDoc> listAnnotation(Object annotationClassObject,String key,String value) {
		String annoName=AnnoUtils.toAnnotationClassName(annotationClassObject);
		List<AnnotationDoc> list=new ArrayList<AnnotationDoc>();
		for(int i=0;annotations!=null && i<annotations.size();i++) {
			AnnotationDoc anno=annotations.get(i);
			if((annoName==null || annoName.equals(anno.name)) 
					&& (key==null || anno.has(key, value))) { list.add(anno); }
		}
		return list;
	}
	
	/** find annoations in this and child **/
	public List<AnnotationDoc> findAnnotation(Object annotationClassObject) { return findAnnotation(annotationClassObject,null,null); }
	public List<AnnotationDoc> findAnnotation(Object annotationClassObject,String key,String value) {
		List<AnnotationDoc> list=listAnnotation(annotationClassObject, key, value);
		for(int i=0;childs!=null && i<childs.size();i++) { // find annaotions in childs
			List<AnnotationDoc> childList=childs.get(i).findAnnotation(annotationClassObject,key,value); 
			if(childList!=null) { list.addAll(childList); }
		}
		return list;
	}
	

	public String getName() { return name; }
	
	/** get this soruce depends to **/
	public DocObject getParent() { return parent; }
	
//	/** get ClassDo this depends on **/
//	public ClassDoc getClassDoc() {
//		DocObject parent=this.parent;
//		int deep=100;
//		while(parent!=null && deep-->0) {
//			if(parent instanceof ClassDoc) return (ClassDoc)parent;
//			else parent=parent.getParent();
//		}
//		return null;
//	}
	
	/** get reference to classSoruce **/
	public ClassDoc getClassDoc() { 	
		if(group instanceof ClassDoc)return (ClassDoc)group; 
		DocObject parent=this.parent;
		int deep=100;
		while(parent!=null && deep-->0) {
			if(parent instanceof ClassDoc) return (ClassDoc)parent;
			else parent=parent.getParent();
		}
		return null;
	}
	public DocObject getGroup() { return this.group; }
	
	
	public boolean equals(Object obj) {
		if(obj==null) return false;
		else if(obj==this) return true;
		else if(obj instanceof String) {
			String s=(String)obj; 
			if(s.equals(name)) return true;
			else return false;
		}else return super.equals(obj);
	}
	
	public String toJava() { return toString(); }
	public abstract String toString();
}
