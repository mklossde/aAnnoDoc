package org.openon.aannodoc.source;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarDoc extends DocObject implements Serializable {
	private static final long serialVersionUID = -5448594864397580016L;
	private Logger LOG=LoggerFactory.getLogger(JarDoc.class);
	
	private List<String> errors=new ArrayList();
	/** containter for all annotations in unit **/
	private AnnotationMap annoMap=new AnnotationMap();
	
	public List<PackageDoc> subPackages=new ArrayList<PackageDoc>();
	
	/** AnnoDoc Options **/
	protected Options options;
	
	public JarDoc(String name, Options options) { 
		super(name,null,null);		
		this.options=options;
	}
	
	public void addAnnotation(AnnotationDoc anno) { annoMap.add(anno); }
	public AnnotationMap anno() { return annoMap; }
	
	public void organize() {
		annoMap.init();
	}
	
	//------------------------------------------------------------------
	
	public Options getOptions() { return this.options; }
	
	public List<PackageDoc> getPackages() { return subPackages; }
	
	/** get or add package by name **/
	public PackageDoc addPackage(String name)  throws IOException {				
		PackageDoc pkg=getPackage(name);
		if(pkg==null) {
			pkg=new PackageDoc(name,this);
			subPackages.add(pkg);
		}
		correctUnitName(name);
		return pkg;		
	}

	/** checkName unitName and package name **/
	protected void correctUnitName(String name) throws IOException {
		String n[]=this.name.split("\\.");
		String nn[]=name.split("\\.");
//		if(n.length!=nn.length) { throw new IOException("wrong length this:"+Arrays.toString(n)+" name:"+Arrays.toString(nn)); }
		for(int i=0;i<n.length && i<nn.length;i++) {		
			if(!n[i].equals(nn[i])) {		
				StringBuilder sb=new StringBuilder();
				for(int t=0;t<i;t++) { if(t>0) { sb.append('.'); } sb.append(n[t]); }
				this.name=sb.toString();
				i=n.length; // end
			}
		}
	}
	
	//------------------------------------------------------------------------------------
	// All 

	public List<String> externClasses(ClassDoc src,boolean excludeJre) {
		List<String> list=new ArrayList<String>();
		List<String> internClasses=getAllClassNames();
		List<ClassDoc> all=getAllClasses();		
		SourceAnnotations.externClasses(src, null, excludeJre, list, internClasses);
		return list;
	}
	
	
	public List<String> getAllClassNames() {
		ArrayList<String> all=new ArrayList<String>();
		for(int i=0;i<subPackages.size();i++) {
			PackageDoc sub=subPackages.get(i);			
			all.addAll(sub.getClassNames());
		}
		return all; 
	}
	
	public List<ClassDoc> getAllClasses() {
		ArrayList<ClassDoc> all=new ArrayList<ClassDoc>();
		for(int i=0;i<subPackages.size();i++) {
			PackageDoc sub=subPackages.get(i);			
			all.addAll(sub.getClasses());
		}
		return all; 
	}
	
	public PackageDoc getPackage(String name) {
		if(name==null) return null;
		for(int i=0;i<subPackages.size();i++) {			
			PackageDoc sub=subPackages.get(i);
			if(name.equals(sub.name)) return sub;
		}
		return null;
	}
	
	/** is this a class from java.lang.* **/
	public boolean isJavaLangClass(String name) {
		if(name==null || name.length()==0) { return false; }
		if(name.equals("Boolean") || name.equals("Byte") || name.equals("Character") || name.equals("Class") || name.equals("Double")
			|| name.equals("Enum") || name.equals("Float") || name.equals("Integer") || name.equals("Long") || name.equals("Math")	
			|| name.equals("Number") || name.equals("Object") || name.equals("Short") || name.equals("String") || name.equals("StringBuffer")
			|| name.equals("StringBuilder") || name.equals("Thread") || name.equals("Void")
	//TODO: add all ?
				) {return true; }
		return false;
	}
	
	
//	public ClassDoc getClass(String name) {
	public ClassDoc findClass(String name) {
		if(name==null) return null;
		else if(name.endsWith(".class")) { name=name.substring(0,name.length()-6);}		
		else if(name.endsWith(".java")) { name=name.substring(0,name.length()-5); }

		for(int i=0;i<subPackages.size();i++) {	
			PackageDoc pgk=subPackages.get(i);
			ClassDoc cl=pgk.getClass(name);
			if(cl!=null) return cl;
		}
		return null;
	}
	
	//------------------------------------------------------------------
	// references Method calls
	
	/** find all method by call **/
	public List<MethodDoc> findMethods(CallDoc call) { 
		List<MethodDoc> list=new ArrayList<MethodDoc>();
		for(int i=0;i<subPackages.size();i++) {
			PackageDoc sub=subPackages.get(i);			
			 List<ClassDoc> classes=sub.getClasses();
			 for(int t=0;classes!=null && t<classes.size();t++) {
				 ClassDoc cl=classes.get(t);
				 List<MethodDoc> mDoc=cl.getMethods(this, call);			 
				 if(mDoc!=null) { list.addAll(mDoc); }
			 }
		}
		return list;
	}
	
	/** find all method by name **/
	public List<MethodDoc> findMethods(String methodName) { return findMethods(methodName, null); }
	public List<MethodDoc> findMethods(String methodName,String params[]) {
		List<MethodDoc> list=new ArrayList<MethodDoc>();
		for(int i=0;i<subPackages.size();i++) {
			PackageDoc sub=subPackages.get(i);			
			 List<ClassDoc> classes=sub.getClasses();
			 for(int t=0;classes!=null && t<classes.size();t++) {
				 ClassDoc cl=classes.get(t);
				 MethodDoc mDoc=cl.getMethod(this, methodName);
				 if(mDoc!=null) { list.add(mDoc); }
			 }
		}
		return list;
	}
	
	public MethodDoc findMethod(CallDoc call) { 
		for(int i=0;i<subPackages.size();i++) {
			PackageDoc sub=subPackages.get(i);			
			 List<ClassDoc> classes=sub.getClasses();
			 for(int t=0;classes!=null && t<classes.size();t++) {
				 ClassDoc cl=classes.get(t);
				 MethodDoc mDoc=cl.getMethod(this, call);
				 if(mDoc!=null) { return mDoc; }
			 }
		}
		return null;
	}
	
	/** find first method by name and params **/
	public MethodDoc findMethod(String methodName) { return  findMethod(methodName,null); }
	public MethodDoc findMethod(String methodName,String params[]) {
		for(int i=0;i<subPackages.size();i++) {
			PackageDoc sub=subPackages.get(i);			
			 List<ClassDoc> classes=sub.getClasses();
			 for(int t=0;classes!=null && t<classes.size();t++) {
				 ClassDoc cl=classes.get(t);
				 MethodDoc mDoc=cl.getMethod(this, methodName,params);
				 if(mDoc!=null) { return mDoc; }
			 }
		}
		return null;
	}
	
	//------------------------------------------------------------------
	
	public void error(String err) {
		errors.add(err);
		LOG.error("DocError "+err);
	}
	
	public String toString() { return "Unit "+name+" "+subPackages.size(); }
	
	//------------------------------------------------------------------
	
	public List<ClassDoc> getParentList(ClassDoc tag,boolean addtag) {
		List<ClassDoc> parents=new ArrayList<ClassDoc>();		
		getParentList(parents,tag,addtag);
		return parents;
	}
	
	// new ArrayList<ClassDoc>
	/** get list of parents 0=parent..n=root - addtag=true = tag is added to first of list **/
	protected void getParentList(List<ClassDoc> parents,ClassDoc tag,boolean addtag) {
		// add tag	
		if(addtag) {
			boolean found=false;
			for(int i=0;i<parents.size() && !found;i++) {
				if(parents.get(i)==tag) { found=true; }
			}
			if(!found) { parents.add(tag); } 
		} 	
		// add parent
		ClassDoc parent=findClass(tag.getExtends());
		if(parent!=null) { getParentList(parents,parent,true); }
		// add impls
		List<String> impl=tag.getImplements();
		if(impl==null) { return ; }
		for (String imp : impl) {
			parent=findClass(imp);
			if(parent!=null) { getParentList(parents,parent,true); } 
		}	
	}
	
	
	
}
