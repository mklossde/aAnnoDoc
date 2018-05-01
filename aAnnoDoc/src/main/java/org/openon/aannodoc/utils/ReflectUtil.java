package org.openon.aannodoc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectUtil {
	
	public static final String GET="get";
	public static final String SET="set";
	public static final String IS="is";


	
	//-----------------------------------------------------------------
	
	/** remove get/set from string and first char toLowerCase (getHallo = hallo) **/
	public static String removeGetSet(String str) {
		if (str == null) {
			return null;
		}else if ((str.startsWith(GET) || str.startsWith(SET)) && str.length() > 3) {
			return Character.toLowerCase(str.charAt(3)) + str.substring(4);
		}else if (str.startsWith(IS) && str.length() > 3) {
			return Character.toLowerCase(str.charAt(2)) + str.substring(3);
		} else
			return str;
	}
	
	public static Object[] toArray(Object obj) throws IOException {
		if(obj==null) { return null; } 
		else if(obj instanceof List) { return ((List)obj).toArray(); }
		else if(obj instanceof Object[]) { return (Object[])obj; }
		else if(obj instanceof String) { return ((String)obj).split(";"); }
		else { throw new IOException("unkown "+obj.getClass()); }
	}
	 
    /** get instanceof of class from obj **/
	public static Object getInstance(Class cl,Object obj) throws IOException  {
		try {
			if(obj==null) { return null; }
			else if(obj instanceof String) { obj=Class.forName((String)obj).newInstance(); }
			else if(obj instanceof Class) { obj=((Class)obj).newInstance(); }
		}catch(Throwable e) { throw new IOException(e); }		
		if(cl.isAssignableFrom(obj.getClass())) { return obj; }
		else { throw new IOException("wrong obj "+obj.getClass()); }
	}
	
	/** read stream as string **/
	public static String read(Reader in)  throws IOException {
		StringBuilder result = new StringBuilder();
	    try {
	    	char[] buf = new char[1024]; int r = 0;
	        while ((r = in.read(buf)) != -1) {result.append(new String(buf, 0, r));}
	    } finally { in.close();}
		String text=result.toString();
		return text;
	}
	
	/** read stream as string **/
	public static String read(InputStream in)  throws IOException {
		StringBuilder result = new StringBuilder();
	    try {
	        byte[] buf = new byte[1024]; int r = 0;
	        while ((r = in.read(buf)) != -1) {result.append(new String(buf, 0, r));}
	    } finally { in.close();}
		String text=result.toString();
		return text;
	}
	
	public static void main(String[] args) throws Exception {
		cc c=new ReflectUtil().new cc();
//		c.x=new ReflectUtil().new cc();
//		System.out.println("x:"+get(c,"x.xx"));
		c.l.add("Hallo");
		System.out.println("x:"+get(c,"l"));
	}
	
	public class cc {
		
		public String getXx() { return "DuDa"; }
		public String xx="Hallo";
		public cc x;
		public List<String> l=new ArrayList<String>();
		
		public cc() {}
	}
	
	//-----------------------------------------------------------------
	
	protected static Map<Class,Map<String,Object>> clFields=new HashMap<Class,Map<String,Object>>(); 
	
	public static String getString(Object bean,String path) throws IOException { return String.valueOf(get(bean, path)); }
	public static Object get(Object bean,String path) throws IOException {
		String all[]=path.split("[.]");
		for(int i=0;i<all.length && bean!=null;i++) {
			bean=getBean(bean, all[i]);
		}
		return bean;
	}
	
	public static Object set(Object bean,String path,Object value) throws IOException {
		String all[]=path.split("[.]");
		for(int i=0;i<all.length-1 && bean!=null;i++) {
			bean=getBean(bean, all[i]);
		}
		if(bean!=null) { return setBean(bean, all[all.length-1], value);}
		else { return null; }
	}
	
	//------------------------
	
	public static Object setBean(Object bean,String field,Object value) throws IOException {
		if(bean instanceof Map) { return ((Map)bean).put(field,value); }
		return getFieldMethod(bean,getFields(bean).get(field));		
	}
	
	public static Object getBean(Object bean,String field) throws IOException {
		if(bean instanceof Map) { return ((Map)bean).get(field); }
		return getFieldMethod(bean,getFields(bean).get(field));		
	}
	
	public static Object getFieldMethod(Object bean,Object method) throws IOException {
		try {
			if(method instanceof Field) { return ((Field)method).get(bean); }
			else  if(method instanceof Method && ((Method)method).getName().startsWith("get")) {
				return ((Method)method).invoke(bean, new Object[]{}); }
			else { return null; }
		}catch(Throwable e) { throw new IOException(e.getMessage(),e); }
	}
	
	public static Object setFieldMethod(Object bean,Object method,Object value) throws IOException {
		try {
			if(method instanceof Field) { Field f=(Field)method; Object old=f.get(bean);f.set(bean, value); return old; }
			else if(method instanceof Method && ((Method)method).getName().startsWith("set")) {
				return ((Method)method).invoke(bean, new Object[]{value}); 
			}else{ return null; }
		}catch(Throwable e) { throw new IOException(e.getMessage(),e); }
	}
	
	
	public static Map<String,Object> getFields(Object bean) {
		Class cl; if(bean instanceof Class) { cl=(Class)bean; } else { cl=bean.getClass(); }
		Map map=clFields.get(cl); if(map!=null) { return map; } else { map=new HashMap();clFields.put(cl,map); } 		
		for(Method method: cl.getMethods()) {
			int mod=method.getModifiers(); String name=method.getName(); 
			if(!name.equals("getClass") && (name.startsWith("get") || name.startsWith("set"))) { name=removeGetSet(name); } else { name=null; }
			if(name!=null && Modifier.isPublic(mod) && !Modifier.isStatic(mod) && !map.containsKey(name)) { 
				if(!method.isAccessible()) { method.setAccessible(true); }
//System.out.println("x:"+name+" => "+method.getReturnType());					
				map.put(name,method); }
		}		
		for (Field field : cl.getFields()) {
			int mod=field.getModifiers(); String name=field.getName().toLowerCase();
			if(Modifier.isPublic(mod)  && !Modifier.isStatic(mod) && !map.containsKey(name)) { 
//System.out.println("x:"+name+" => "+field.getGenericType());				
				if(!field.isAccessible()) { field.setAccessible(true); } map.put(name,field); }
		}	
		return map;
	}	
	
	public static String toName(String methodName) {
		if(methodName==null || methodName.length()<3) { return methodName; }
		else if(methodName.startsWith("get") || methodName.startsWith("set")) { return methodName.substring(3); }
		else { return methodName; }
	}
}
