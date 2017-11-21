package org.openon.aannodoc.example;

public class ExampleObject {

	public Object request;
	public Object result;
	
	
	public ExampleObject() {}
	public ExampleObject(Object request,Object result) { 
		this.request=request;
		this.result=result;
	}
	
	public ExampleObject request(Object request) {
		this.request=request;
		return this;
	}
	
	public ExampleObject result(Object result) {
		this.result=result;
		return this;
	}
}
