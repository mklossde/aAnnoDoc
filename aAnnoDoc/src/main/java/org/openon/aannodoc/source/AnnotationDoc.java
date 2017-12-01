package org.openon.aannodoc.source;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * 		
 * 		valueName:		name attribute (e.g. aTag (name='qwert') ) 		
 * 		valuePath:		path Attribute (e.g. aTag (path='qwert') )
 * 		valueDate:		date attribute (e.g. aTag (date='qwert') )
 * 		isNew(days):	boolean if valueDate newer then days 
 * 
 * 		get("value'):	get any attribute (e.g. aTag (test='qwert') )
 * 		getValues():	get all values as Map String,Object
 * 		has(String key,String value):	attribute key eqlas value 
 * 		is(String valueName,String valuePath):	is valueName and/or valuePath
 * 
 * 
 *
 */
public class AnnotationDoc extends TypeDoc implements Serializable,Comparable {
	private static final Logger LOG=LoggerFactory.getLogger(AnnotationDoc.class);
	private static final long serialVersionUID = 1307234445451097274L;
	
//	private SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");
 
//	public static final String ID="name";
//	public static final String PATH="path";
//	public static final String DATE="date";
//	public static final String HIDE="hide"; // hide/ignore/disable annotation and documentation
//	public static final String LOCAL="local"; // lcoal (not extended/implemented)
	
	/** is this a inline annotation (a annotation like author which is uses inside a comment) **/
	public boolean inline=false;
	
	public String refName,refPath;
	
	/** atttribute values **/
	private Map<String,Object> values=new HashMap<String, Object>();
//	private String valueName;
	
	public AnnotationDoc(String name,Map<String,Object> values,String comment,boolean inline) { 
		super(name, null, null, null);
		this.values=values;		
		this.comment=comment;
		this.inline=inline;
	}
		
	public AnnotationDoc(String name,String fullName,DocObject parent,ClassDoc clSource,boolean inline) { 
		super(name,fullName,parent,clSource);
		this.inline=inline;
	}
	
	//-----------------------------------------------------------------

//TODO: how to identify titel/name/id of attribtue **/	
	public String getTitle() { return getRef(); }
//TODO: how to identify hide of attribtue 
	public boolean isHide() { return false; }
//TODO: how to identify new/date of annotation
	public boolean isNewer(long time) { return true; }
	
	public void setRef(String refPath,String refName) { this.refName=refName; this.refPath=refPath; }

	/** get name of parent this annotation referenced to (e.g. name of method(without get/set)/field/class) **/
	public String getRef() {  return refName; }
//		if(parent==null) { return null; } else { return parent.getName(); }}
	
	/** get inline annotation author (\@author MYNAME) **/
	public String getAuthor() { String value=super.getAuthor(); if(value==null && parent!=null) { value=parent.getAuthor(); } return value; }
	/** get inline annotation version (\@version VERSION) **/
	public String getVersion() {  String value=super.getVersion(); if(value==null && parent!=null) { value=parent.getVersion(); } return value; }
	
	
	
	//-----------------------------------------------------------------
	
	public boolean isInline() { return inline; }
	
//	public Object getValueName() {
//		if(values!=null) { return values.get(ID); } else { return null; }
//	}
//
//	public String getValueNameString() {
//		return getValueString(ID);
//	}
//	
//	public String getValueDate() { if(values!=null) { return (String)values.get(DATE); } else { return null; } }
//	/** get annotation value-date as date object ( date="value") ....  **/
//	public Date getDate() {
//		String date=(String)values.get(DATE);
//		if(date==null) return null; 
//		try { return dateFormat.parse(date); } catch(Exception e) {
//			LOG.error("error date parse "+date);
//			return null;  
//		}
//	}
//	public String getValuePath() { if(values!=null) { return (String)values.get(PATH); } else { return null; } }
//	
//	/** is this anoation new - (noew-days &lt; valueDate) **/
//	public boolean isNew(long days) {
//		try {
////			int d=PainterObjUtil.toInt(days);
//			Date d=getDate();
//			if(d==null) return false;
//			GregorianCalendar cal=new GregorianCalendar();
//			cal.setTime(d);
//			cal.add(GregorianCalendar.DAY_OF_MONTH,(int)days);  
//			return cal.getTimeInMillis()>=System.currentTimeMillis();
//		}catch(Exception e) { e.printStackTrace(); }
//		return false;
//	}
//	
//	/** return if hide=true is enabled **/
//	public boolean isHide() {
//		String hide=(String)values.get(HIDE);
//		return "true".equalsIgnoreCase(hide);
//	}
		
	//-----------------------------------------------------------------
	// values
	
	public void add(Map values) { 
		this.values=values; 
	}
	
	public void add(String key,Object val) { 
		values.put(key,val); 
//		if(key!=null && key.equalsIgnoreCase(ID) && val instanceof String) valueName=(String)val;
	}
	
	/** get("value'):	get any attribute (e.g. aTag (test='qwert') ) **/
	public Object getValue(String key) { 
		if(values==null) { return null; }
		return values.get(key); 
	}
	/** getValues():	get all values as Map String,Object  **/
	public Map<String,Object> getValues() { return values; }
	/** get resolved value as string **/
	public String getValueString(String key) { 
		if(values==null) { return null; }
		Object obj=values.get(key);
		if(obj==null) { return null; }
		else if(obj instanceof DocReference) { return ((DocReference)obj).resolve(); }
		else { return String.valueOf(obj); }
	}
	
	//-----------------------------------------------------------------
	
	/** has(String key,String value):	attribute key eqlas value **/
	public boolean has(String key,String value) {
		if(key==null || values==null) return false;
		if(value==null) return values.containsKey(key);
		Object obj=values.get(key);
		return value.equals(obj);
	}
	
	/** do annotation havae attribute-value name=value **/
	public boolean is(String valueName,String value) {
		return Objects.equals(value, getValue(valueName));
	}
	
//	public boolean equals(Object obj) {
//		Object valueName=getValueName();
//		if(obj instanceof String) {
//			if(((String)obj).equals(valueName)) return true;
//		}else if(obj instanceof AnnotationDoc) {
//			AnnotationDoc anno=(AnnotationDoc)obj;
//			if(anno.getValueName()!=null && anno.getValueName().equals(valueName)) return true;
//		}
//		return super.equals(obj);
//	}
		
	@Override public int compareTo(Object o) {
		String id=getRef(); if(id==null) { return 0; }
		if(o instanceof AnnotationDoc) {
			AnnotationDoc ao=(AnnotationDoc)o;
			return id.compareTo(ao.getRef());
		}else {
			return 0;
		}
	}
	
	//-----------------------------------------------------------------
	
	
	@Override public String toString() { 
		return "Annotation "+name+" "+values;  
	}


	
	
}
 