package org.openon.aannodoc.source;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 	name:		
 * 	group:		class of package (the class this belongs to)
 * 	parent:		this Object depends on (e.g. method of annation)
 * 	comment:	Java Comment
 * 
 * 	 
 * 
 *
 */
public abstract class DocObject implements Serializable {
	private static final long serialVersionUID = -7225995181546556626L;

	public DocObject group;
//	public PackageDoc pgkDoc;
//	public ClassDoc classDoc;
	
	public DocObject parent;
	public String name;
	public String comment;
		
	public List<AnnotationDoc> annotations=new ArrayList<AnnotationDoc>();
	
	public DocObject(String name,DocObject parent,DocObject group) { 
		this.name=name; 	
		this.parent=parent;
		this.group=group;
	}
	
	//--------------------------------------------------------------------------
	
	public void setComment(String comment) {
		if(this.comment==null) this.comment=comment;
		else this.comment=this.comment+"\n"+comment;
	}
	
	public void addAllAnnotations(AnnotationDoc anno) { annotations.add(anno);}
	public void addAnnotations(List<AnnotationDoc> annos) { annotations.addAll(annos); }
	
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
	
	public String getComment() { return comment; }
	
		
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
	public AnnotationDoc getAnnotation(String name) { return getAnnotation("name",name); }
	/** get first annotation with key==value **/
	public AnnotationDoc getAnnotation(String key,String value) {
		for(int i=0;annotations!=null && i<annotations.size();i++) {
			AnnotationDoc anno=annotations.get(i);
			if(anno.has(key, value)) return anno;
		}
		return null;
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
