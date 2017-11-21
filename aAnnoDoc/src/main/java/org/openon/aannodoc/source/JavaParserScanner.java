package org.openon.aannodoc.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.openon.aannodoc.doc.AnnotationDocScanner;
import org.openon.aannodoc.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.comments.Comment;
import japa.parser.ast.comments.LineComment;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.type.ClassOrInterfaceType;

/**
 * Java-Source-Scanner 
 * 		use japa.parser.JavaParser to scan Java-Sources
 * 
 * TODO: FEHLER in JavaParser !! mehrfache Kommentare werdenen ignoriert und nur das letzte genommen 
 */
public class JavaParserScanner {
	private Logger LOG=LoggerFactory.getLogger(JavaParserScanner.class);
	
	public static boolean ANALYSEAT=true;
	
	public static String debug=null;
	
	private JarDoc unit;
	
	private CompilationUnit cu;
	private List<Comment> comments;
	private Node prev=null;

	
	protected int fileCount=0;
	
	protected static Map<String,String> defaultClass=new HashMap<String,String>();
	
	public JavaParserScanner() {		
//TODO: create a list of all classes in java.lang.*		
		defaultClass.put("String","java.lang,String");
	}
	
	//----------------------------------------------------------------------------------------------
	
	protected static JarDoc scanPackage(String dir) throws Exception {
		JavaParserScanner scnnner=new JavaParserScanner();
		scnnner.readDir(dir);
		return scnnner.getUnit();
	}
	
	protected static JarDoc scanSource(String file) throws Exception {
		JavaParserScanner scnnner=new JavaParserScanner();
		scnnner.readFile(file);
		return scnnner.getUnit();
	}
	
	protected static JarDoc scanJar(String jarPath) throws Exception  {
		JavaParserScanner scnnner=new JavaParserScanner();
		scnnner.readJar(jarPath);
		return scnnner.getUnit();
	}
	
	//----------------------------------------------------------------------------------------------
	
	public JarDoc getUnit() { return unit; }
	public void clear() { unit=null; }
//	public void setLOG.debug(boolean debug) { this.debug=debug; }
	//----------------------------------------------------------------------------------------------
	
	public void readJar(String jarPath) throws Exception  {
		long start=System.currentTimeMillis();
	 	JarFile jar = new JarFile(jarPath);
	 	Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
	 	
	    while(entries.hasMoreElements()) {
	    	JarEntry entry=entries.nextElement();
	    	String name = entry.getName();
	    	if(entry.isDirectory()) {    		
	    	}else if(name.endsWith(".java")) {
	    		LOG.debug("scan jar file "+name);	    		
	    		InputStream in=jar.getInputStream(entry);
	    		read(in,name);
	    		in.close();
	      }
	    }
	    LOG.info("scan jar "+jarPath+" in "+(System.currentTimeMillis()-start)+"ms");
	}
	
	public void readDir(String dir) throws IOException  {
		long start=System.currentTimeMillis();	
		readDirectory(dir);
		LOG.info("scan dir "+dir+" with "+fileCount+" Files in "+(System.currentTimeMillis()-start)+"ms");
	}

	public void readFile(String file) throws IOException  {
		long start=System.currentTimeMillis();	
		readOneFile(file);
		LOG.info("read file "+file+" in "+(System.currentTimeMillis()-start)+"ms");
	}
	
	//----------------------------------------------------------------------------------------------
	
	protected void readDirectory(String dir) throws IOException  {
		long start=System.currentTimeMillis();
		File file=new File(dir);
		File files[]=file.listFiles();
		for(int i=0;files!=null && i<files.length;i++) {
			File f=files[i];
			if(f.isFile() && f.getName().endsWith(".java")) { 
				readOneFile(f.getPath());
			}else if(f.isDirectory()){
				readDirectory(f.getPath());
			}
		}		
		LOG.debug("scan dir "+dir+" in "+(System.currentTimeMillis()-start)+"ms");
	}
	
	protected void readOneFile(String file) throws IOException  {
		long start=System.currentTimeMillis();	
		FileInputStream in = new FileInputStream(file);
		read(in,file);
		in.close();
		LOG.debug("read file "+file+" in "+(System.currentTimeMillis()-start)+"ms");
//System.out.println("read file "+file+" in "+(System.currentTimeMillis()-start)+"ms");
	}

	
	public void read(InputStream in,String name) throws IOException  {	
//if(name.indexOf("ButtonDesign")!=-1) {
//	System.out.println("ButtonDesign");
//}
		this.fileCount++;
		ClassDoc clSource=null;
		try {
			cu = JavaParser.parse(in);
		    comments=cu.getComments();
		    
		    List<TypeDeclaration> allCl=cu.getTypes();	   
		    for(int a=0;allCl!=null && a<allCl.size();a++) {
		    	TypeDeclaration clDeclarion=allCl.get(a);
		    	
		    	// package------------------------------------------------------------------------------
			    PackageDeclaration p=cu.getPackage();
			    String pkgName=toString(p.getName());
			    LOG.debug("package "+pkgName);
			    if(unit==null) unit=new JarDoc(pkgName); // create unit for this package
			    PackageDoc pkg=unit.addPackage(pkgName); // add apckage	
			    
			    String sourceName=clDeclarion.getName();
			    clSource=pkg.addClass(sourceName); // add Class	  
			    LOG.debug("class "+sourceName);
			    clSource.pkgComment=findComment(cu,p); // package comment in class 
			    pkg.setComment(clSource.pkgComment); // pacakge comment in package
	
			    scanComments(prev,p, pkg, clSource); // scan inside comments
			    
		    	// import------------------------------------------------------------------------------
			    prev=p;
			    // create import list
			    List<ImportDeclaration> imports=cu.getImports();
			    ArrayList<String> list=new ArrayList<String>();
			    for(int i=0;imports!=null && i<imports.size();i++) {
			    	ImportDeclaration imp=(ImportDeclaration)imports.get(i);
			    	list.add(toString(imp.getName()));
			    	LOG.debug("import "+imp.getName());
			    	prev=imp;
			    }
			    clSource.imports=list;
			    
			    // class------------------------------------------------------------------------------
			    //class annotations		   
			    setAnnotations(clSource,clDeclarion.getAnnotations(),clSource);  
			    clSource.comment=toString(clDeclarion.getComment()); //getJavaDoc());		    
			    clSource.modifiers=clDeclarion.getModifiers(); // modifierers		    		    
			    
			    if(clDeclarion instanceof ClassOrInterfaceDeclaration) {
			    	ClassOrInterfaceDeclaration td=(ClassOrInterfaceDeclaration)get(cu,ClassOrInterfaceDeclaration.class);		    	
			    	
				    // extends 
				    ArrayList<String> exList=new ArrayList<String>();
				    List<ClassOrInterfaceType> exts=td.getExtends();
				    if(exts!=null && exts.size()==1) {
				    	ClassOrInterfaceType ex=exts.get(0);
				    	exList.add(findClassName(ex.getName(),clSource));
				    	 clSource.extendName=findClassName(ex.getName(),clSource);
				    	 LOG.debug("extends "+ex.getName());
				    }else if(td.isInterface()) {
				    }else if(exts!=null && exts.size()>1) LOG.error("extends more then one ? "+exts+" ? "+clSource); 
				    
				    // implements
				    ArrayList<String> imist=new ArrayList<String>();			    
				    List<ClassOrInterfaceType> impl=td.getImplements();
				    if(td.isInterface() && (impl==null || impl.size()==0)) impl=exts; // for interface take extends as implements
				    for(int i=0;impl!=null && i<impl.size();i++) {
				    	ClassOrInterfaceType im=(ClassOrInterfaceType)impl.get(i);
				    	imist.add(findClassName(im.getName(),clSource));
				    	LOG.debug("implement "+im.getName());
				    }
				    clSource.implementList=imist;
			    }
			    
			    // Body: field && methods ------------------------------------------------------------------------------
			    prev=clDeclarion;
			    // analyse body
			    List<BodyDeclaration> mem=clDeclarion.getMembers();
			    for(int i=0;mem!=null && i<mem.size();i++) {
			    	BodyDeclaration body=mem.get(i);		    	
			    	
			    	if(body instanceof FieldDeclaration) { // Field
			    		FieldDeclaration dec=(FieldDeclaration)body;
			    		List<VariableDeclarator> vars=dec.getVariables();		    		
			    		for(int t=0;t<vars.size();t++) {
			    			VariableDeclarator v=(VariableDeclarator)vars.get(t);
			    			VariableDeclaratorId id=v.getId();
			    			Expression exp=v.getInit();
			    			String type=dec.getType().toString();
			    			
			    			FieldDoc field=new FieldDoc(id.getName(),type,clSource,clSource,toObject(exp, clSource));
			    			LOG.debug("VariableDeclarator "+id.getName());
			    			setAnnotations(field, dec.getAnnotations(), clSource);
			    			field.modifiers=dec.getModifiers();
			    			field.comment=findComment(cu, dec); // toString(dec.getJavaDoc());
			    			clSource.addField(field);		    			
			    			scanComments(prev,v, field, clSource); // scan inside comments
			    			prev=v;
			    		}
			    		
			    	}else if(body instanceof ConstructorDeclaration) { // Constructor
			    		ConstructorDeclaration con=(ConstructorDeclaration)body;
			    		ConstructorDoc cs=new ConstructorDoc(con.getName(), clSource.getTypeName(), clSource, clSource);
			    		LOG.debug("ConstructorDeclaration "+con.getName());
			    		setAnnotations(cs, con.getAnnotations(), clSource);
			    		cs.modifiers=con.getModifiers();
			    		cs.comment=findComment(cu, con);
			    		clSource.addConstructor(cs);
			    		scanComments(prev,con, cs, clSource); // scan inside comments
			    		
			    	}else if(body instanceof MethodDeclaration) {// Method	
			    		MethodDeclaration method=(MethodDeclaration)body;
			    		ParameterDoc params=getParams(method,clSource);
			    		MethodDoc mc=new MethodDoc(method.getName(), toString(method.getType()), params,clSource, clSource);
			    		LOG.debug("MethodDeclaration "+method.getName());
			    		setAnnotations(mc, method.getAnnotations(), clSource);
			    		mc.modifiers=method.getModifiers();
			    		mc.comment=findComment(cu, method);
			    		clSource.addMethod(mc);
			    		scanComments(prev,method, mc, clSource); // scan inside comments
			    		
			    	}else if(body instanceof AnnotationMemberDeclaration) {// Annotation 
			    		AnnotationMemberDeclaration an=(AnnotationMemberDeclaration)body;
			    		LOG.debug("AnnotationMemberDeclaration "+an.getName());
			    		
			    	}else if(body instanceof ClassOrInterfaceDeclaration) { // inner Class
	//TODO: scan inner class		    		
			    	}else if(body instanceof EmptyMemberDeclaration) { // ;
			    	}else if(body instanceof InitializerDeclaration) { // static initialisation 
System.out.println("InitializerDeclaration "+body);			    		
			    	}else if(body instanceof EnumDeclaration) { // enum declaration 
			    		
			    	//}else LOG.error("unkown body "+body.getClass()+" at "+body.getBeginLine()+" in "+clSource.name);
			    	}else { LOG.error("unkown body "+body.getClass()+toAtString(body, clSource)); }
			    	prev=body;
			    }
			    
			    scanComments(null,clDeclarion, clSource, clSource); // scan inside comments
			    
		    }
		     
		    // un scanned - comments 	   
		    for(int i=0;comments!=null && i<comments.size();i++) {
		    	Comment com=comments.get(i);
	    		LOG.debug("not scanned comment "+com);
	    		DocObject parent=null; //findParent(com);		    			    	
	    		scanComment(com,parent,clSource);
		    }
		    
		    LOG.debug("scanned class "+clSource.name);
		}catch(Throwable e) {
			LOG.error("scan error in "+name+" ("+e+")");
			e.printStackTrace();
		}
	}	
	
	protected ParameterDoc getParams(MethodDeclaration method,ClassDoc clSource) {		
		List<Parameter> parameter=method.getParameters();
//		List<TypeParameter> parameter2=method.getTypeParameters();
		if(parameter==null) { return new ParameterDoc(0); }
		
		ParameterDoc doc=new ParameterDoc(parameter.size());
		for(int i=0;i<parameter.size();i++) {
			Parameter p=parameter.get(i);
			VariableDeclaratorId var=p.getId();
			String name=var.getName();
//			Type type=p.getType();
			String type=p.getType().toString();
			String className=findClassName(type,clSource);
			doc.set(i, className, name);
		}
		return doc;
	}
	
	//---------------------------------------------------------------------------------------
	
	/** scan all comments insode node **/
	public void scanComments(Node prev,Node now,DocObject parent,ClassDoc clSource) throws Exception {
		if(comments==null) return ;
		Iterator<Comment> it=comments.iterator();
		while(it.hasNext()) { 
	    	Comment com=it.next();
	    	if((prev==null || com.getBeginLine()>prev.getBeginLine() || (com.getBeginLine()==prev.getBeginLine() && com.getBeginColumn()>prev.getBeginColumn()))
	    			&& (com.getEndLine()<now.getEndLine() || (com.getEndLine()==now.getEndLine() && com.getEndColumn()<now.getEndColumn()))) {	    		    			    	
	    		scanComment(com,parent,clSource);
	    		it.remove(); // remove from list
	    	}
	    }
	}
	
	
	//---------------------------------------------------------------------------------------


	
//	/** find start of param **/
//	private static int findParamStart(String str,int index) {
//		while(index!=-1 && index<str.length()) {
//			char c=str.charAt(index);
//			if(Character.isWhitespace(c)) index++;
//			else if(c=='(') return index;
//			else return -1;
//		}
//		return -1;
//	}
	
	private void scanComment(Comment c,DocObject parent,ClassDoc clSource) throws Exception {
		try {
			if(c instanceof LineComment) { return ; } // ignore line comments
			
			String str=toString(c);
			LOG.debug("scanComment ");
			int index=AnnotationDocScanner.nextAnnotation(str, 0,true);
//			if(index==-1) parent.setComment(str);
//			else if(index>0) parent.setComment(str.substring(0,index));
			parent.setComment(str);
			
			// find JavaDoc Annotions
			while(index!=-1 && index<str.length()) {								
				AnnotationDocScanner annoObject=AnnotationDocScanner.scan(str,index);
//				if(annoObject==null) { throw new IOException("annoObject missing at "+index); }
				if(annoObject!=null) {
					AnnotationDoc anno=new AnnotationDoc(annoObject.name,findClassName(annoObject.name,clSource), parent, clSource,true);
					anno.add(annoObject.attr);
					anno.comment=annoObject.value;
					
					unit.addAnnotation(anno); // add annotation to unit
					clSource.addAllAnnotations(anno); // add annotaion as source
					parent.addAllAnnotations(anno); // add annotation to parent
				
					index=annoObject.pos;
				}else { index=str.length(); }
			}
		}catch(Exception e) {LOG.error(e.getMessage(),e);}
		c.setEndLine(0); // set comment scanned)
	}
	
	public String toAtString(Node c,ClassDoc clSource) {
		String name=null;int line=0;
		if(c!=null) {
			name=c.getClass().getSimpleName();
			line=c.getBeginLine();
		}
		String simpleName=clSource.getSimpleName();
		String cl=clSource.getTypeName();
		return " at "+cl+"."+name+"("+simpleName+".java:"+line+")";
	}
	
	private void setAnnotations(DocObject source,List<AnnotationExpr> pAnno,ClassDoc clSource) throws Exception {
		List<AnnotationDoc> list=new ArrayList<AnnotationDoc>();
	    for(int i=0;pAnno!=null && i<pAnno.size();i++) {
	    	AnnotationExpr anno=pAnno.get(i);
	    	String annoName=toString(anno.getName());
//System.out.println("annoName:"+annoName);	    	
	    	AnnotationDoc as=new AnnotationDoc(annoName,findClassName(annoName,clSource),source,clSource,false);    
	    	clSource.addAllAnnotations(as); 
	    	
	    	if(anno instanceof NormalAnnotationExpr) {
	    		NormalAnnotationExpr na=(NormalAnnotationExpr)anno;
	    		List<MemberValuePair> pairs=na.getPairs();
	    		for(int t=0;pairs!=null && t<pairs.size();t++) {
	    			MemberValuePair pair=pairs.get(t);
	    			String name=pair.getName();
	    			Object val=toObject(pair.getValue(),clSource);
	    			as.add(name,val);
	    			LOG.debug("Annotation param:"+name+"="+val);
	    		}
	    	}
	    	
    		if(as.getValueName()==null) as.add(AnnotationDoc.ID, ReflectUtil.removeGetSet(source.getName())); // get name from parent
    		if(as.getValuePath()==null) as.add(AnnotationDoc.PATH,shortPath(clSource.getTypePackage()));
	    	
	    	unit.addAnnotation(as); 
	    	list.add(as);
	    	
    		scanComments(prev,anno, as, clSource); // scan inside comments
	    	prev=anno;
	    }
	    	    
	    source.addAnnotations(list);
	}
	
	/** TODO: remove base dir from path **/
	public String shortPath(String path) {
		return path;
	}
	
	public Object toObject(Object obj,ClassDoc clSource) {
		if(obj==null) { return null;
		}else if(obj instanceof NullLiteralExpr) {
			return null;			
		}else if(obj instanceof StringLiteralExpr) {
			String str=((StringLiteralExpr)obj).getValue();
			return str;
		}else if(obj instanceof LiteralExpr) {
			LiteralExpr str=(LiteralExpr)obj;
			return str.getData();
		}else if(obj instanceof UnaryExpr) {
			return String.valueOf(obj);
			
		}else if(obj instanceof FieldAccessExpr) {
			FieldAccessExpr f=(FieldAccessExpr)obj;
//			return new DocReference(clSource.getTypePackage(),f.getScope().toString(),f.getField(),this.unit);
			String className=findClassName(f.getScope().toString(), clSource);
			return new DocReference(className,f.getField(),this.unit);			

		}else {
//System.out.println("unkown "+obj.getClass()); 			
			return String.valueOf(obj);
		}
	}
	
	/** get resolve ful class name of name in source **/
	private String findClassName(String name,ClassDoc source) {
		if(name==null) return null;
		else if(name.indexOf('.')!=-1) return name; // name is fulleName
		
		String dotname=defaultClass.get(name);
		if(dotname!=null) { 
			return dotname; 
		}

		dotname="."+name;
		List<String> imports=source.getImports();
		for(int i=0;i<imports.size();i++) {
			if(imports.get(i).endsWith(dotname)) return imports.get(i); // found name in imports
		}
		
		ClassDoc found=unit.findClass(name);
		if(found!=null) {
//System.out.println("x:"+found.typeName);
			return found.typeName; // found name in all know classes of unit
		}
		
		return source.getTypePackage()+dotname; // decide name is on actual soruce package 
	}
	
	private String findComment(CompilationUnit cu,Node actual) {
		Node previus=prev;
		StringBuffer sb=new StringBuffer();
	    List<Comment> comments=cu.getComments();
	    for(int i=0;comments!=null && i<comments.size();i++) {
	    	Comment c=comments.get(i);
	    	if(previus==null || c.getBeginLine()>previus.getEndLine() || (c.getBeginLine()==previus.getEndLine() && c.getBeginColumn()>previus.getEndColumn())) {
	    		if(actual==null || c.getEndLine()<actual.getBeginLine() || (c.getEndLine()==actual.getBeginLine() && c.getEndColumn()>actual.getBeginColumn())) {		    		
		    		String str=toString(c);
		    		if(str!=null && str.length()>0) { 
		    			if(sb.length()>0) sb.append("\n");
		    			sb.append(str);
		    		}
	    		}
	    	}
	    }	    
	    return sb.toString();
	}
	
	private String toString(Object obj) {
		if(obj==null) return null;
		else if(obj instanceof NameExpr) return ((NameExpr)obj).toString();
		else if(obj instanceof Comment) {
    		String str=((Comment)obj).getContent();
    		if(str!=null && str.length()>0) { 	    			
	    		return getCommentString(str);
    		}
			return str;
		}else if(obj instanceof Type) return ((Type)obj).toString();
		else return String.valueOf(obj);
	}
	
	public String getCommentString(String str) {
		if(str==null) return null;
		StringBuilder sb=new StringBuilder();
		String all[]=str.split("\n");
		for(int i=0;i<all.length;i++) {
			String text=all[i];
			text=text.replaceAll("\r", "");
			int index=indexOf(text,'*',0);
			if(index!=-1) text=text.substring(index+1);
			if(text.endsWith("*")) text=text.substring(0,text.length()-1); 
			sb.append(text).append("\n");
		}
		return sb.toString().trim();
	}
	
	private int indexOf(String s,char c,int pos) {
		for(int i=pos;i<s.length();i++) {
			char ch=s.charAt(i);
			if(ch==c) return i;
			else if(ch!=' ' && ch!='\t') return -1;
		}
		return -1;
	}
	
	private TypeDeclaration get(CompilationUnit cu,Class cl) {
		List<TypeDeclaration> types=cu.getTypes();
		for(int i=0;i<types.size();i++) {
			if(cl.isAssignableFrom(types.get(i).getClass())) return types.get(i);
		}
		return null;
	}

	
}
