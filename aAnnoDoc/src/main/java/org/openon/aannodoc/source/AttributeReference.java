package org.openon.aannodoc.source;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.annotation.aFeature;

/**
 * Attribute values are resolved 
 * 		@{ATTRIBUTE} 
 * are resolved via options.getAttribute("ATTRIBUE") 
 * 
 * Predefined Attributes:
 * 		@{NOW} = aktual-date
 * 
 * e.g. 
 * 		\@aVersion(date="@{NOW}") 
 * => will actual date of documentation creation for date
 * 
 *
 */
@aFeature(author="mk",date="28.06.2018",title="Doucment Attibutes")
public class AttributeReference implements DocReference { 

	public static final String REF_START="@{";
	public static final String REF_END="}";
	
	protected JarDoc unit;
	protected String className;
	protected String key;
	/** resolved reference **/
	protected Object resolved=null;
	
	/** is string a Attribute reference **/
	public static boolean isAttributeReference(String value) {
		if(value==null) { return false; }
		else { return value.startsWith(REF_START) && value.endsWith(REF_END); }
	}
	
	public AttributeReference(String className,String value,JarDoc unit) {
		this.className=className;
		if(value.startsWith(REF_START) && value.endsWith(REF_END)) {
			this.key=value.substring(REF_START.length(),value.length()-REF_END.length());
		}else {
			this.key=value;
		}
		this.unit=unit;
	}
	
	@Override public Object resolve()  {
		if(resolved!=null) { return resolved; }
//		throw new RuntimeException("AttributeReference unkown "+toString());
		if(this.unit==null) { return null; }
		Options options=this.unit.getOptions();
		if(options==null) { return null; }
		this.resolved=options.getAttribute(key);
		return this.resolved;
	}
	
	public String toString() {
		return className+" "+REF_START+key+REF_END; 
	}
	
}
