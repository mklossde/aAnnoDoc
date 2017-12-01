package org.openon.aannodoc.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tree<T> { //implements List<E> {
    protected List leaves = new ArrayList(); /** nodes **/
    protected Tree parent = null; /** parent reference **/
    protected T data; /** data of node **/
    protected String name; /** label of tree **/
    
    public String del="/"; /** tree delemiter **/
    
    public Tree() {}
    public Tree(String name) { this.name=name; }
    
    /** get root-tree **/
    public Tree<T> getRoot() { if(parent!=null){ return parent.getRoot(); } else { return this; } }
    /** get sub-tree for name (aa/bb/cc) - create= create tree- exclude:remove last**/
    public Tree<T> getTreeOf(String name,boolean create,boolean excludeName) {
    	if(name==null || name.length()==0) { return this; }
    	else if(name.startsWith(del)) { name=name.substring(1); } //return getRoot().getTreeOf(name.substring(1),create); }
    	Tree sub=this; String all[]=name.split(del);
    	int len=all.length; if(excludeName) { len--; }
    	for(int t=0;t<len;t++) { sub=sub.getLeaveOf(all[t],create);}
    	return sub;
    }
    /** get or create leave-tree for name **/
    protected Tree<T> getLeaveOf(String name,boolean create) {
    	if(name==null || name.length()==0) { return this; }
    	for(int i=0;i<leaves.size();i++) { 
    		Object c=leaves.get(i);	    		
    		if(c instanceof Tree && name.equals(((Tree)c).name)) { return (Tree<T>)leaves.get(i); }
    	}
    	if(create) { Tree sub=new Tree(name); sub.parent=this; leaves.add(sub); return sub; } 
    	else { return null; }
    }	    
    public boolean is(String name) { return name.equals(name); }
    public String getName() { return (String)name; }
    public T getData() { return data; }
    public Tree set(T data) { this.data=data; return this; }
    // 
    public Tree add(T data) {leaves.add(data); return this; }
    public Tree add(int index,T data) { if(index>=0) {leaves.add(index,data); } else if(index!=-1) {leaves.add(data); } return this; }
    public int size() { return leaves.size(); }
    public Object get(int index) { return leaves.get(index); }
    public void toString(StringBuilder sb,int deep) {
    	if(name!=null) { for(int t=0;t<deep-1;t++) { sb.append(' '); } sb.append(name).append("\n"); }
    	for(int i=0;i<leaves.size();i++) { 	    		
    		if(leaves.get(i) instanceof Tree) {	((Tree)leaves.get(i)).toString(sb,deep+1); } 
    		else { for(int t=0;t<deep;t++) { sb.append(' '); } sb.append(leaves.get(i)).append("\n"); }
    	}
    }
    
    public Tree sort(Comparator comp) {
    	if(leaves==null) { 
    		Collections.sort(leaves, comp);
    		for(int i=0;i<leaves.size();i++) { Object o=leaves.get(i); if(o instanceof Tree) { ((Tree) o).sort(comp); } } 
    	}
    	return this;
    }
    public String toString() { StringBuilder sb=new StringBuilder(); toString(sb,0); return sb.toString(); }
    public String[] splitTreeFile(String a) { int i=a.lastIndexOf(del); if(i==-1) { return new String[]{null,a}; }return new String[]{a.substring(0,i),a.substring(i+1)}; }
}
