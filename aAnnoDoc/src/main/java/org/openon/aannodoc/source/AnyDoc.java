package org.openon.aannodoc.source;

/** Document any java **/
public class AnyDoc extends DocObject {

	public String anyType;
	
	public AnyDoc(String anyType,String name,PackageDoc pkg,JarDoc unit) { 
		super(name, pkg, unit);
		this.anyType=anyType;
	}
	
	@Override
	public String toString() { return "Any "+anyType+" "+name; }

}
