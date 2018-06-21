package org.openon.aannodoc.source;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openon.aannodoc.doc.AnnotationDocScanner;
import org.openon.aannodoc.utils.AnnoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
//	public PackageDoc pgkDoc;
//	public ClassDoc classDoc;
	
	public DocObject parent;
	public String name;
	protected String comment;
		
	public List<AnnotationDoc> annotations=new ArrayList<AnnotationDoc>();
	
	public DocObject(String name,DocObject parent,DocObject group) { 
		this.name=name; 	
		setParent(parent); setGrup(group);
	}
	
	public void setParent(DocObject parent) { this.parent=parent; }
	public void setGrup(DocObject group) { this.group=group; }
	
	//--------------------------------------------------------------------------
	
	public void setComment(String comment) {
		if(comment==null || comment.length()==0) { return ; }
		if(this.comment==null || this.comment.length()==0) {
			this.comment=comment; 
		}else {
			LOG.warn("double set comment old:'{}' and new:'{}' to {}",this.comment,comment,this);
			this.comment=this.comment+"\n"+comment;
		}
	}
	
	public String getComment() { return comment; }
	
	public void addAllAnnotations(AnnotationDoc anno) { annotations.add(anno);}
	public void addAnnotations(List<AnnotationDoc> annos) { annotations.addAll(annos); }

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
	public DocObject findClass(String name) { JarDoc unit=findJarDoc(); if(unit!=null) { return unit.findClass(name); } else { return null; }}
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
	

	
		
	//--------------------------------------------------------------------------
	/** get first annotation for type **/
	public AnnotationDoc getAnnotationType(String typeName) {
		for(int i=0;annotations!=null && i<annotations.size();i++) {
			AnnotationDoc anno=annotations.get(i);
			if(typeName.equals(anno.getName())) return anno;
		}
		return null;
	}
	
	/** get list of annotations for type **/
	public List<AnnotationDoc> listAnnotationType(String typeName) { return listAnnotationType(typeName,null,null); }
		
	/** get list of annotations for type, with key and value **/
	public List<AnnotationDoc> listAnnotationType(String typeName,String key,String value) {
		List list=new ArrayList();
		for(int i=0;annotations!=null && i<annotations.size();i++) {
			AnnotationDoc anno=annotations.get(i);
			if(typeName.equals(anno.getName())) {
				if(key==null || anno.is(key, value)) list.add(anno);
			}
		}
		return list;
	}
	
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
	/** get first annotation with key==value **/
	public AnnotationDoc getAnnotation(Object annotationClassObject,String key,String value) {
		String annoName=AnnoUtils.toAnnotationClassName(annotationClassObject);
		for(int i=0;annotations!=null && i<annotations.size();i++) {
			AnnotationDoc anno=annotations.get(i);
			if((annoName==null || annoName.equals(anno.name)) 
					&& anno.has(key, value)) { return anno; }
		}
		return null;
	}
	
	public List<AnnotationDoc> findAnnotation(Object annotationClassObject) { return findAnnotation(annotationClassObject,null,null); }
	public List<AnnotationDoc> findAnnotation(Object annotationClassObject,String key,String value) {
		String annoName=AnnoUtils.toAnnotationClassName(annotationClassObject);
		List<AnnotationDoc> list=new ArrayList<AnnotationDoc>();
		for(int i=0;annotations!=null && i<annotations.size();i++) {
			AnnotationDoc anno=annotations.get(i);
			if((annoName==null || annoName.equals(anno.name)) 
					&& (key==null || anno.has(key, value))) { list.add(anno); }
		}
		return list;
	}
	
	/** get list of all annotations in object **/
	public List<AnnotationDoc> getAnnotations() { return annotations; }
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
	
	
	public String toJava() { return toString(); }
	public abstract String toString();
}
