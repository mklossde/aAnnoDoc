package org.openon.aannodoc.source;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openon.aannodoc.utils.AnnoUtils;

public class ClassDoc extends TypeDoc implements Serializable {
	private static final long serialVersionUID = 5724507364403134031L;
	
	public PackageDoc pkg;
	public String pkgComment;
	
	public List<String> imports;
	public String extendName;
	public List<String> implementList;
	
	public List<FieldDoc> fields=new ArrayList<FieldDoc>(); // list of local fields
	public List<MethodDoc> methods=new ArrayList<MethodDoc>();
	public List<ConstructorDoc> constructors=new ArrayList<ConstructorDoc>();
	public List<ClassDoc> subClasses=new ArrayList<ClassDoc>();
	
	public transient List<ClassDoc> parentList;  // reference-list of this and all extends/imports (as a transient-one-time-resolve)
	
	public ClassDoc(String name,PackageDoc pkg,JarDoc unit) { 
		super(name,pkg.getName()+"."+name,pkg,unit);
		this.pkg=pkg;
	}
	
	public void addMethod(MethodDoc method) { methods.add(method); addChild(method); }
	public void addField(FieldDoc field) { fields.add(field); addChild(field); }
	public void addConstructor(ConstructorDoc constructor) { constructors.add(constructor);  addChild(constructor);  }
	public void addSubClass(ClassDoc subClass) { 
		subClasses.add(subClass);
		//TODO: add subClass as child result in double or recursive machtes (e.g. annotaions of fields)
		//addChild(subClass);  
	}
	
	/** get a list of relevant (this and parent classes, e.g. to search for all fields) **/
	public List<ClassDoc> relevantClasses(JarDoc doc) {
		if(parentList==null) { parentList=doc.getParentList(this,true); }
		return parentList; 
	}
			
	public String getAuthor() { return AnnoUtils.getAuthor(this,0); }
	public String getVersion() { return AnnoUtils.getVersion(this,0); }
	public String getDeprecated() { return AnnoUtils.getDeprecated(this,0); }

	public List<ClassDoc> getSubClasses() { return subClasses; }
	
	//------------------------------------------------------------------
	
	public PackageDoc getPackage() { return pkg; }
	
	public String getPackageComment() { return pkgComment; }
	
	public List<String> getImports() { return imports; }
	public String getExtends() { return extendName; }
	public List<String> getImplements() { return implementList; }
	
	/** get a list of local fields **/
	public List<FieldDoc> getLocalFields() { return fields; }
	public List<FieldDoc> getAllFields(JarDoc doc) {
		ArrayList<FieldDoc> all=new ArrayList<FieldDoc>();
		List<ClassDoc> allCl=relevantClasses(doc);
		for (ClassDoc classDoc : allCl) {
			List<FieldDoc> fs=classDoc.getLocalFields();
			for(int t=0;fs!=null && t<fs.size();t++) {
				FieldDoc fd=fs.get(t);
				if(!all.contains(fd)) all.add(fd);
			}
		}
		return all;
	}
	/** get local field by Name **/
	public FieldDoc getLocalField(String name) { 
		if(fields==null) { return null; }
		for (FieldDoc f : fields) {
			if(name.equals(f.getName())) { return f; }
		}
		return null;
	}
	
	/** find field in all relevant classes (extends,imports,..) **/
	public FieldDoc getField(JarDoc doc,String name) { 
		List<ClassDoc> allCl=relevantClasses(doc);
		for (ClassDoc classDoc : allCl) {
			List<FieldDoc> fs=classDoc.getLocalFields();
			for(int t=0;fs!=null && t<fs.size();t++) {
				FieldDoc fd=fs.get(t);
				if(fd.equals(name)) { return fd; }
			}
		}
		return null;
	}
	
	public List<MethodDoc> getLocalMethods() { return methods; }
	public List<MethodDoc> getAllMethods(JarDoc doc) {
		ArrayList<MethodDoc> all=new ArrayList<MethodDoc>();
		List<ClassDoc> allCl=relevantClasses(doc);
		for (ClassDoc classDoc : allCl) {
			List<MethodDoc> fs=classDoc.getLocalMethods();
			for(int t=0;fs!=null && t<fs.size();t++) {
				MethodDoc fd=fs.get(t);
				if(!all.contains(fd)) all.add(fd);
			}
		}
		return all;
	}
	
	/** get all possible methods for this call **/
	public List<MethodDoc> getMethods(JarDoc doc,CallDoc call) {
		List<MethodDoc> list=new ArrayList<MethodDoc>();
		List<ClassDoc> allCl=relevantClasses(doc);
		for (ClassDoc classDoc : allCl) {	
			List<MethodDoc> fs=classDoc.getAllMethods(doc);
			for(int t=0;fs!=null && t<fs.size();t++) {
				MethodDoc fd=fs.get(t);		
				if(fd.is(call)) { list.add(fd); }				
			}
		}
		return list;
	}
	
	public MethodDoc getMethod(JarDoc doc,CallDoc call) { 
		List<ClassDoc> allCl=relevantClasses(doc);
		for (ClassDoc classDoc : allCl) {	
			List<MethodDoc> fs=classDoc.getAllMethods(doc);
			for(int t=0;fs!=null && t<fs.size();t++) {
				MethodDoc fd=fs.get(t);		
				if(fd.is(call)) { return fd; }				
			}
		}
		return null;
	}
	
	/** find first method by name and aprams (all relevant classes (extends,imports,..)) **/
	public MethodDoc getMethod(JarDoc doc,String methodName) { return  getMethod(doc,methodName,null); }
	public MethodDoc getMethod(JarDoc doc,String methodName,String params[]) { 
		List<ClassDoc> allCl=relevantClasses(doc);
		for (ClassDoc classDoc : allCl) {	
			List<MethodDoc> fs=classDoc.getAllMethods(doc);
			for(int t=0;fs!=null && t<fs.size();t++) {
				MethodDoc fd=fs.get(t);		
				if(fd.is(methodName,params)) { return fd; }				
			}
		}
		return null;
	}
	
	public List<ConstructorDoc> getConstructor() { return constructors; }
	public List<AnnotationDoc> getAnnotationsInClass() { return annotations; }
	
	public List<String> getExternalImports(boolean excludeJre) {
		JarDoc src=(JarDoc)pkg.getParent();
		return src.externClasses(this, excludeJre);
//		List<String> list=new ArrayList<String>();
//		
//		String pkg=src.getName();
//		for(int i=0;imports!=null && i<imports.size();i++) {
//			String im=imports.get(i);
//			if(!im.startsWith(pkg)) list.add(im); 
//		}
//		return list;
	}
	
	/** find doc for name **/
	public DocObject findClass(String name) {
		if(name==null || name.length()==0) { return null; }
		
		JarDoc unit=findJarDoc();  if(unit==null) { return null; }
		//TODO: exclude java.lang ?
		if(unit.isJavaLangClass(name)) { return null; }
		
		// find in sub-classes
		for(int i=0;subClasses!=null && i<subClasses.size();i++) {
			ClassDoc cl=subClasses.get(i); 
			if(cl.equals(name)) { return cl; }
		}
		
		String fullName=getFullName(name);
		// find via unit		
		return unit.findClass(fullName);
	}
	
	/** get fullName, package plus simpleName **/
	public String getFullName(String name) {
		if(name==null || name.length()==0) { return null; }
		else if(name.indexOf('.')!=-1) { return name; } // alrady fullName
		String key="."+name; 
		for(int i=0;imports!=null && i<imports.size();i++) { // search inputs
			String in=imports.get(i);
			if(in.endsWith(key)) { return in; }
		}
//TODO: java.lang.name is not used, always use package.name. ok ? 
		return typePackage+key; // get name for class-package
//		return name;
	}
	
	//------------------------------------------------------------------
	
	/** get anker to docType **/
	@Override public String toAnker(String docType) {
		if(typeSimpleName==null || typeSimpleName.length()==0) return "<a href=\""+link(docType)+"\">LINK</a>";
		else return "<a href=\""+link(docType)+"\">"+typeSimpleName+"</a>";
	}
	
	//------------------------------------------------------------------
	
	public boolean equals(Object obj) {
		if(obj instanceof ClassDoc) {
			return (((ClassDoc)obj).typeName!=null && ((ClassDoc)obj).typeName.equals(this.typeName));
		}else return super.equals(obj);
	}
	
	public String toString() { return "Class "+name; }
}
