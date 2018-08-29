package org.openon.aannodoc.source;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openon.aannodoc.annotation.aBug;

public class PackageDoc extends DocObject implements Serializable {
	private static final long serialVersionUID = 1369472051565228810L;
	
	public List<ClassDoc> classes=new ArrayList<ClassDoc>();
	
	public PackageDoc(String name,DocObject parent) { super(name,parent,null); }
	
	//------------------------------------------------------------------------------------
	
	/** get list of all class definitaion in this package **/
	public List<ClassDoc> getClasses() { return classes; }
	
	/** get list of all class-names in this package **/
	public List<String> getClassNames() {
		List<String> list=new ArrayList<String>();
		for(int i=0;classes!=null && i<classes.size();i++) {
			list.add(classes.get(i).getName());
		}
		return list;
	}

	@aBug(author="mk",date="02.08.2017",title="double add not allowd. but inner classes add with same name",fix="allow double name adds")
	public ClassDoc addClass(String name) {
//		ClassDoc cl=getClass(name);
//		if(cl==null) {
			ClassDoc cl=new ClassDoc(name,this,null);
			classes.add(cl);
//		}
		return cl;
	}
	
	//------------------------------------------------------------------------------------
	
	/** get class definitaion by name **/
	public ClassDoc getClass(String name) {
		if(name==null) return null;
		else if(name.endsWith(".class")) { name=name.substring(0,name.length()-6);}		
		else if(name.endsWith(".java")) { name=name.substring(0,name.length()-5); }
		for(int i=0;i<classes.size();i++) {
			ClassDoc cl=classes.get(i);
			if(cl.equals(name)) return cl;  
		}
		return null;
	}
	
	
	
	//------------------------------------------------------------------------------------
	
	public String toString() { return "Package "+name; }
}
