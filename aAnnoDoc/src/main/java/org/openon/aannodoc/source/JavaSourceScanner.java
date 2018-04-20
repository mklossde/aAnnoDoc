package org.openon.aannodoc.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.openon.aannodoc.utils.DocFilter;
import org.openon.aannodoc.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.AnnotationDeclaration;
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
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;

/**
 * Java-Source-Scanner 
 * 		use japa.parser.JavaParser to scan Java-Sources
 * 
 * TODO: FEHLER in JavaParser !! mehrfache Kommentare werdenen ignoriert und nur das letzte genommen 
 */
public class JavaSourceScanner {
	private Logger LOG=LoggerFactory.getLogger(JavaSourceScanner.class);
	
	public static boolean ANALYSEAT=true;
	
//	protected String charset="UTF-8";
	protected String charset="windows-1252"; //
	
	public static String debug=null;
	
	private JarDoc unit;
	
	private CompilationUnit cu;
	
	private Node prev=null;

	protected DocFilter filter;
	protected int fileCount=0;
	
	protected static Map<String,String> defaultClass=new HashMap<String,String>();
		
	//----------------------------------------------------------------------------------------------
	
	protected static JarDoc scanPackage(String dir,DocFilter filter) throws Exception {
		JavaSourceScanner scnnner=new JavaSourceScanner(filter);
		scnnner.readDir(dir);
		return scnnner.getUnit();
	}
	
	protected static JarDoc scanSource(String file,DocFilter filter) throws Exception {
		JavaSourceScanner scnnner=new JavaSourceScanner(filter);
		scnnner.readFile(file);
		return scnnner.getUnit();
	}
	
	protected static JarDoc scanJar(String jarPath,DocFilter filter) throws Exception  {
		JavaSourceScanner scnnner=new JavaSourceScanner(filter);
		scnnner.readJar(jarPath);
		return scnnner.getUnit();
	}
	
	//----------------------------------------------------------------------------------------------
	
	public JavaSourceScanner(DocFilter filter) {		
		this.filter=filter;
//TODO: create a list of all classes in java.lang.*				
		defaultClass.put("String","java.lang,String");
	}
	
	//----------------------------------------------------------------------------------------------
	
	public JarDoc getUnit() { return unit; }
	public void clear() { unit=null; }

	//----------------------------------------------------------------------------------------------
	
	public void readJar(String jarPath) throws Exception  {
		long start=System.currentTimeMillis();
	 	JarFile jar = new JarFile(jarPath);
	 	Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
	 	
	    while(entries.hasMoreElements()) {
	    	JarEntry entry=entries.nextElement();
	    	String name = entry.getName();
	    	if(entry.isDirectory()) {    	    		
	    	}else if(filter!=null && !filter.scanClass(name)) { // ignroe by fitler
	    	}else if(name.endsWith(".java")) {
	    		LOG.debug("scan jar java "+name);	    		
	    		InputStream fin=jar.getInputStream(entry);
	    		InputStreamReader in=new InputStreamReader(fin,charset);
	    		readJava(in,name);
	    		in.close();
	    	}else if(name.endsWith(".adoc")) {
	    		LOG.debug("scan jar text "+name);	    		
	    		InputStream fin=jar.getInputStream(entry);
	    		InputStreamReader in=new InputStreamReader(fin,charset);
	    		readText(in,name);
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

//	public void readFile(String file) throws IOException  {
//		readOneFile(file);
//	}
	
	//----------------------------------------------------------------------------------------------
	
	protected void readDirectory(String dir) throws IOException  {
		long start=System.currentTimeMillis();
		File file=new File(dir);
		File files[]=file.listFiles();
		for(int i=0;files!=null && i<files.length;i++) {
			File f=files[i];
			if(filter!=null && !filter.scanClass( f.getPath())) { // ignroe by fitler
			}else if(f.isFile() && f.getName().endsWith(".java")) { 
				readFile(f.getPath());
			}else if(f.isDirectory()){
				readDirectory(f.getPath());
			}
		}		
		LOG.debug("scan dir "+dir+" in "+(System.currentTimeMillis()-start)+"ms");
	}
	
	public void readFile(String file) throws IOException  {
		long start=System.currentTimeMillis();	
		LOG.trace("start read file {}",file);
		FileInputStream fin = new FileInputStream(file);
		InputStreamReader in=new InputStreamReader(fin,charset);
		try {
			if(filter!=null && !filter.scanClass(file)) { // ignroe by fitler
			}else if(file.endsWith(".java")) { readJava(in,file); }
			else if(file.endsWith(".adoc")) { readText(in,file); }
			else { throw new IOException("unkown file type "+file); }
		}finally{
			if(in!=null) { in.close(); }
		}
		LOG.debug("read file "+file+" in "+(System.currentTimeMillis()-start)+"ms");
//System.out.println("read file "+file+" in "+(System.currentTimeMillis()-start)+"ms");
	}

	public void readText(InputStreamReader in,String name) throws IOException  {	
		String text=ReflectUtil.read(in);
				
		try {
			String pkgName=name,clName=name;
			if(unit==null) unit=new JarDoc(pkgName); // create unit for this package
			PackageDoc pkg=unit.addPackage(pkgName); // add apckage	
			ClassDoc clSource=pkg.addClass(clName); // add Class	
			DocObject parent=clSource;
//FIXME: read file here !!!!			
			scanComment(text, parent, clSource);
		}catch(Throwable e) {
			LOG.error("scan error in "+name+" ("+e+")",e);
		}		
	}
	
	public void readJava(InputStreamReader in,String name) throws IOException  {	
//if(name.indexOf("ButtonDesign")!=-1) {
//	System.out.println("ButtonDesign");
//}
		this.fileCount++;

		try {
			boolean considerComments=true;			
			cu = JavaParser.parse(in,considerComments);
			List<Comment> comments=cu.getComments();

	    	// package------------------------------------------------------------------------------
		    PackageDeclaration p=cu.getPackage();
		    String pkgName="";
		    if(p!=null) { 
		    	pkgName=toString(p.getName());
		    	LOG.trace("pkgName {}",pkgName);
		    }
		    
		    if(unit==null) unit=new JarDoc(pkgName); // create unit for this package
		    PackageDoc pkg=unit.addPackage(pkgName); // add apckage	
		    
		    List<TypeDeclaration> allCl=cu.getTypes();	   
		    for(int a=0;allCl!=null && a<allCl.size();a++) {
		    	TypeDeclaration clDeclarion=allCl.get(a);
		    	
			    String sourceName=clDeclarion.getName();
				ClassDoc clSource=pkg.addClass(sourceName); // add Class	  
			    LOG.trace("class "+sourceName);
			    clSource.pkgComment=findComment(cu,p); // package comment in class 
			    pkg.setComment(clSource.pkgComment); // pacakge comment in package
	
			    scanComments(prev,p, pkg, clSource,comments); // scan inside comments
			    
		    	// import------------------------------------------------------------------------------
			    prev=p;
			    // create import list
			    List<ImportDeclaration> imports=cu.getImports();
			    ArrayList<String> list=new ArrayList<String>();
			    for(int i=0;imports!=null && i<imports.size();i++) {
			    	ImportDeclaration imp=(ImportDeclaration)imports.get(i);
			    	list.add(toString(imp.getName()));
			    	LOG.trace("import "+imp.getName());
			    	prev=imp;
			    }
			    clSource.imports=list;
			    
			    // class------------------------------------------------------------------------------
			    //class annotations		   
			    setAnnotations(clSource,clDeclarion.getAnnotations(),clSource,comments);  
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
				    	 LOG.trace("extends "+ex.getName());
				    }else if(td.isInterface()) {
				    }else if(exts!=null && exts.size()>1) LOG.error("extends more then one ? "+exts+" ? "+clSource); 
				    
				    // implements
				    ArrayList<String> imist=new ArrayList<String>();			    
				    List<ClassOrInterfaceType> impl=td.getImplements();
				    if(td.isInterface() && (impl==null || impl.size()==0)) impl=exts; // for interface take extends as implements
				    for(int i=0;impl!=null && i<impl.size();i++) {
				    	ClassOrInterfaceType im=(ClassOrInterfaceType)impl.get(i);
				    	imist.add(findClassName(im.getName(),clSource));
				    	LOG.trace("implement "+im.getName());
				    }
				    clSource.implementList=imist;
			    }
			    
			    // Body: field && methods ------------------------------------------------------------------------------
			    prev=clDeclarion;
			    
//			    scanComments(null,clDeclarion, clSource, clSource); // scan inside comments
			    
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
//			    			String type=dec.getType().toString();			    			
			    			String type=toString(dec.getType());
			    			
			    			FieldDoc field=new FieldDoc(id.getName(),type,clSource,clSource,toObject(exp, clSource));
			    			LOG.trace("VariableDeclarator "+id.getName());
			    			setAnnotations(field, dec.getAnnotations(), clSource,comments);
			    			field.modifiers=dec.getModifiers();
			    			field.comment=findComment(cu, dec); // toString(dec.getJavaDoc());
			    			clSource.addField(field);		    			
			    			scanComments(prev,v, field, clSource,comments); // scan inside comments
			    			prev=v;
			    		}
			    		
			    	}else if(body instanceof ConstructorDeclaration) { // Constructor
			    		ConstructorDeclaration con=(ConstructorDeclaration)body;
			    		ConstructorDoc cs=new ConstructorDoc(con.getName(), clSource.getTypeName(), clSource, clSource);
			    		LOG.trace("ConstructorDeclaration "+con.getName());
			    		setAnnotations(cs, con.getAnnotations(), clSource,comments);
			    		cs.modifiers=con.getModifiers();
			    		cs.comment=findComment(cu, con);
			    		clSource.addConstructor(cs);
			    		scanComments(prev,con, cs, clSource,comments); // scan inside comments
			    		
			    	}else if(body instanceof MethodDeclaration) {// Method	
			    		MethodDeclaration method=(MethodDeclaration)body;
			    		ParameterDoc params=getParams(method,clSource);
			    		MethodDoc mc=new MethodDoc(method.getName(), toString(method.getType()), params,clSource, clSource);
			    		LOG.trace("MethodDeclaration "+method.getName());
			    		setAnnotations(mc, method.getAnnotations(), clSource,comments);
			    		mc.modifiers=method.getModifiers();
			    		mc.comment=findComment(cu, method);
			    		clSource.addMethod(mc);
			    		scanComments(prev,method, mc, clSource,comments); // scan inside comments
			    		
			    	}else if(body instanceof AnnotationMemberDeclaration) {// Annotation 
			    		AnnotationMemberDeclaration an=(AnnotationMemberDeclaration)body;
			    		LOG.trace("AnnotationMemberDeclaration "+an.getName());			    		
			    	}else if(body instanceof AnnotationDeclaration) {// Annotation 
			    		AnnotationDeclaration an=(AnnotationDeclaration)body;
			    		LOG.trace("AnnotationDeclaration "+an.getName());
			    		
			    		
			    	}else if(body instanceof ClassOrInterfaceDeclaration) { // inner Class
//TODO: scan inner class		    		
			    	}else if(body instanceof EmptyMemberDeclaration) { // ;
			    	}else if(body instanceof InitializerDeclaration) { // static initialisation 			    		
			    	}else if(body instanceof EnumDeclaration) { // enum declaration 
			    		
			    	}else { LOG.warn("not parsed body '"+body.getClass()+"' "+toAtString(body, clSource)+" in "+clDeclarion.getName()); }
			    	prev=body;
			    }

//TODO: add comments of class before			    
			    scanComments(null,clDeclarion, clSource, clSource,comments); // scan inside comments	
			    
			    LOG.trace("scanned class {}",clSource.name);			    
		    }
		    
		    // un scanned - comments 	   
		    for(int i=0;comments!=null && i<comments.size();i++) {
		    	Comment com=comments.get(i);
	    		LOG.trace("ignored comment "+com);	    			    	
	    		scanComment(com,pkg,pkg);
		    }		    			    	
		     
		}catch(Throwable e) {
			LOG.error("scan error in "+name+" ("+e+")",e);
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
			String type=toString(p.getType());
			String className=findClassName(type,clSource);
			doc.set(i, className, name);
		}
		return doc;
	}
	
	//---------------------------------------------------------------------------------------
	
	/** scan all comments insode node **/
	public void scanComments(Node prev,Node now,DocObject parent,ClassDoc clSource, List<Comment> comments) throws Exception {
		if(comments==null) return ;
		Iterator<Comment> it=comments.iterator();
		while(it.hasNext()) { 
	    	Comment com=it.next();
	    	if((prev==null || com.getBeginLine()>prev.getBeginLine() || (com.getBeginLine()==prev.getBeginLine() && com.getBeginColumn()>prev.getBeginColumn()))
	    			&& (now==null || com.getEndLine()<now.getEndLine() || (com.getEndLine()==now.getEndLine() && com.getEndColumn()<now.getEndColumn()))) {	    		    			    	
//System.out.println("c:"+com.getBeginLine()+","+com.getBeginColumn()+" p:"+prev.getBeginLine()+","+prev.getBeginColumn()+" n:"+now.getBeginLine()+","+now.getBeginColumn());
	    		if(!have(prev,com)) {
	    			scanComment(com,parent,clSource);
	    			it.remove(); // remove from list
	    		}
	    	}
	    }
	}

//TODO: hack find comment in prev and ignore it 
	public boolean have(Node prev,Comment com) {
		if(prev==null) { return false; }
		List<Comment> cc=prev.getOrphanComments();
		for(int i=0;cc!=null && i<cc.size();i++) {
			if(cc.get(i)==com) { return true; }
		}
		return false;
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
	
	private void scanComment(Comment c,DocObject parent,DocObject clSource) throws Exception {
		if(c instanceof LineComment) { return ; } // ignore line comments
		String str=toString(c);
//		parent.setComment(str);
		scanComment(str, parent, clSource);
		c.setEndLine(0); // set comment scanned)
	}
	
	private void scanComment(String str,DocObject parent,DocObject clSource) throws Exception {
		try {
			LOG.trace("scanComment "+str);
//			int index=AnnotationDocScanner.nextAnnotation(str, 0,true);
			AnnotationDocScanner aDocScanner=new AnnotationDocScanner(str,true);			
			parent.setComment(str);
			
			// find JavaDoc Annotions
//			while(index!=-1 && index<str.length()) {
//			AnnotationDocScanner annoObject=AnnotationDocScanner.scan(str,index);
//			if(annoObject!=null) {
//			AnnotationDoc anno=new AnnotationDoc(annoObject.name,findClassName(annoObject.name,clSource), parent, clSource,true);
//			anno.add(annoObject.attr);
//			anno.comment=annoObject.value;

			AnnotationDoc anno=aDocScanner.nextAnnotation();
			while(anno!=null) {
					anno.setTypeName(findClassName(anno.name,clSource));
					anno.setParent(parent); 
					anno.setGrup(clSource);
					
					unit.addAnnotation(anno); // add annotation to unit
					clSource.addAllAnnotations(anno); // add annotaion as source
					parent.addAllAnnotations(anno); // add annotation to parent
				
					anno=aDocScanner.nextAnnotation();
//					index=annoObject.pos;
//				}else { index=str.length(); }
			}
		}catch(Exception e) {LOG.error(e.getMessage(),e);}
		
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
	
	private void setAnnotations(DocObject source,List<AnnotationExpr> pAnno,ClassDoc clSource,List<Comment> comments) throws Exception {
		List<AnnotationDoc> list=new ArrayList<AnnotationDoc>();
	    for(int i=0;pAnno!=null && i<pAnno.size();i++) {
	    	AnnotationExpr anno=pAnno.get(i);	    	
	    	AnnotationDoc as=toAnnotationDoc(source, anno, clSource);
	    	
	    	list.add(as);
	    	
    		scanComments(prev,anno, as, clSource,comments); // scan inside comments
	    	prev=anno;
	    }
	    	    
	    source.addAnnotations(list);
	}
	
	public static final String ANNO_VALUE="value";
	private AnnotationDoc toAnnotationDoc(DocObject source,AnnotationExpr anno,ClassDoc clSource) {
    	String annoName=toString(anno.getName());
//System.out.println("annoName:"+annoName);	    	
    	AnnotationDoc as=new AnnotationDoc(annoName,findClassName(annoName,clSource),source,clSource,false);    
    	clSource.addAllAnnotations(as); 
    	
    	if(anno instanceof NormalAnnotationExpr) { // @ANNO(key="value",..)
    		NormalAnnotationExpr na=(NormalAnnotationExpr)anno;
    		List<MemberValuePair> pairs=na.getPairs();
    		for(int t=0;pairs!=null && t<pairs.size();t++) {
    			MemberValuePair pair=pairs.get(t);
    			String name=pair.getName();
    			Object val=toObject(pair.getValue(),clSource);
    			as.add(name,val);
    			LOG.trace("Annotation param:"+name+"="+val);
    		}
    	}else if(anno instanceof SingleMemberAnnotationExpr) { // @ANNO("VALUE")
    		SingleMemberAnnotationExpr na=(SingleMemberAnnotationExpr)anno;
    		String name=ANNO_VALUE;
    		Object val=toObject(na.getMemberValue(),clSource);
    		as.add(name,val);
    		LOG.trace("Annotation param:"+name+"="+val);
    	}else if(anno instanceof MarkerAnnotationExpr) { // @ANNO   		
    	}else {
    		LOG.warn("unparsed annotion "+anno);
    	}
    	
//		if(as.getValueName()==null) as.add(AnnotationDoc.ID, ReflectUtil.removeGetSet(source.getName())); // get name from parent
//		if(as.getValuePath()==null) as.add(AnnotationDoc.PATH,shortPath(clSource.getTypePackage()));
    	as.setRef(clSource.getTypePackage(), ReflectUtil.removeGetSet(source.getName()));
    	
    	unit.addAnnotation(as);     
    	
    	return as;
	}
	
	/** TODO: remove base dir from path **/
	public String shortPath(String path) {
		return path;
	}
	
	public Object toObject(Object obj,ClassDoc clSource) {
		if(obj==null) { return null;
		}else if(obj instanceof NullLiteralExpr) {
			return null;	
		}else if(obj instanceof BooleanLiteralExpr) {
			return ((BooleanLiteralExpr)obj).getValue();
		}else if(obj instanceof StringLiteralExpr) {
			String str=((StringLiteralExpr)obj).getValue();
			return str;
		}else if(obj instanceof LiteralExpr) {
			LiteralExpr str=(LiteralExpr)obj;
			return str.getData();
		}else if(obj instanceof UnaryExpr) {
			return String.valueOf(obj);
		}else if(obj instanceof ArrayInitializerExpr) {
			ArrayInitializerExpr a=(ArrayInitializerExpr)obj;
			List<Expression> exp=a.getValues();
			List l=new ArrayList();
			for(int i=0;i<exp.size();i++) {
				Object o=toObject(exp.get(i),clSource);
				l.add(o);
			}
			return l;
		}else if(obj instanceof NormalAnnotationExpr) {
			NormalAnnotationExpr a=(NormalAnnotationExpr)obj;
			return toAnnotationDoc(clSource, a, clSource);
			
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
	private String findClassName(String name,DocObject source) {
		if(name==null) return null;
		else if(name.indexOf('.')!=-1) return name; // name is fulleName
		
		String dotname=defaultClass.get(name);
		if(dotname!=null) { 
			return dotname; 
		}

		dotname="."+name;
		if(source instanceof ClassDoc) {
			List<String> imports=((ClassDoc)source).getImports();
			for(int i=0;imports!=null && i<imports.size();i++) {
				if(imports.get(i).endsWith(dotname)) return imports.get(i); // found name in imports
			}
		}
		
		ClassDoc found=unit.findClass(name);
		if(found!=null) {
			return found.typeName; // found name in all know classes of unit
		}
		
		if(source instanceof ClassDoc) {
			return ((ClassDoc)source).getTypePackage()+dotname; // decide name is on actual soruce package
		}else { 
			return name;
		}
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
		}else if(obj instanceof Type) { return ((Type)obj).toString(); 
		}else if(obj instanceof ReferenceType) { return ((ReferenceType)obj).getType().toString();
//		}else if(obj instanceof japa.parser.ast.type.Type) {
//			japa.parser.ast.type.Type type=(japa.parser.ast.type.Type)obj;
//			return type.getChildrenNodes().get(0).toString();
		}else { return String.valueOf(obj); }
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
