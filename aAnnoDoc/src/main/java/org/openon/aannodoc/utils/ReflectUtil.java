package org.openon.aannodoc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ReflectUtil {
	
	public static final String GET="get";
	public static final String SET="set";
	public static final String IS="is";

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
	public static String read(InputStream in)  throws IOException {
		StringBuilder result = new StringBuilder();
	    try {
	        byte[] buf = new byte[1024]; int r = 0;
	        while ((r = in.read(buf)) != -1) {result.append(new String(buf, 0, r));}
	    } finally { in.close();}
		String text=result.toString();
		return text;
	}
}
