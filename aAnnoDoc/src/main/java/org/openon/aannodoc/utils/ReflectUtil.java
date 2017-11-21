package org.openon.aannodoc.utils;

import java.io.IOException;
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
	
}
