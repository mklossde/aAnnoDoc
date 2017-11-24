package org.openon.aannodoc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openon.aannodoc.utils.DocFilter;
import org.openon.aannodoc.utils.ReflectUtil;

/**
 * handle options of AnnoDoc
 *
 */
public class Options {

	protected Map<String,Object> options=new HashMap<String, Object>();
	protected DocFilter filter;
	
	public Options() {}
	
	//-------------------------------------------------------------------------------------------------
	public void put(String key,Object value) { options.put(key, value); }
	public Object get(String key) { return options.get(key); }
	public Object remove(String key) { return options.remove(key); }
	
	//-------------------------------------------------------------------------------------------------
	
	public DocFilter getFilter() throws IOException {
		if(this.filter!=null) { return filter; }
		this.filter=(DocFilter)ReflectUtil.getInstance(DocFilter.class,options.get("filter"));
		if(filter!=null && !filter.isInit()) { filter.init(this); }
		return filter;
	}
	

}
