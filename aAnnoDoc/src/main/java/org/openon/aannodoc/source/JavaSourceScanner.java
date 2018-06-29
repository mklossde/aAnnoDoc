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

import org.openon.aannodoc.Options;
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
import japa.parser.ast.comments.BlockComment;
import japa.parser.ast.comments.Comment;
import japa.parser.ast.comments.JavadocComment;
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
	
	public static final String ANNO_VALUE="value";
	
//	public static final String IMPLEMENT_FILES=".adoc$";
	public static final String IMPLEMENT_FILES=".sdoc$"; // sdoc = sourceDoc
	
	public static boolean ANALYSEAT=true;
	
//	protected String charset="UTF-8";
	protected String charset="windows-1252"; //
	
	public static String debug=null;
	
	private Options options;
	
	private JarDoc unit;	
	
	private CompilationUnit cu;

//	private Node prev=null;

	protected DocFilter filter;
	protected int fileCount=0;
	
	protected static Map<String,String> defaultClass=new HashMap<String,String>();
		
	//----------------------------------------------------------------------------------------------
	
	protected static JarDoc scanPackage(String dir,DocFilter filter,Options options) throws Exception {
		JavaSourceScanner scnnner=new JavaSourceScanner(filter,options);
		scnnner.readDir(dir);
		return scnnner.getUnit();
	}
	
	protected static JarDoc scanSource(String file,DocFilter filter,Options options) throws Exception {
		JavaSourceScanner scnnner=new JavaSourceScanner(filter,options);
		scnnner.readFile(file);
		return scnnner.getUnit();
	}
	
	protected static JarDoc scanJar(String jarPath,DocFilter filter,Options options) throws Exception  {
		JavaSourceScanner scnnner=new JavaSourceScanner(filter,options);
		scnnner.readJar(jarPath);
		return scnnner.getUnit();
	}
	
	//----------------------------------------------------------------------------------------------
	
	public JavaSourceScanner(DocFilter filter,Options options) {		
		this.filter=filter;
		this.options=options;
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
	    	}else if(filter!=null && !filter.scanDir(name)) { // ignroe by fitler
	    	}else if(name.endsWith(".java")) {
	    		LOG.debug("scan jar java "+name);	    		
	    		InputStream fin=jar.getInputStream(entry);
	    		InputStreamReader in=new InputStreamReader(fin,charset);
	    		readJava(in,name);
	    		in.close();
	    	}else if(name.matches(IMPLEMENT_FILES)) {
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
			if(filter!=null && !filter.scanDir( f.getPath())) { // ignroe by fitler
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
			else if(file.matches(IMPLEMENT_FILES)) { readText(in,file); }
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
			if(unit==null) unit=new JarDoc(pkgName,options); // create unit for this package
			PackageDoc pkg=unit.addPackage(pkgName); // add apckage	
			ClassDoc clSource=pkg.addClass(clName); // add Class	
			DocObject parent=clSource;
//FIXME: read file here !!!!			
			scanComment(text, parent, clSource,true);
		}catch(Throwable e) {
			LOG.error("scan error in "+name+" ("+e+")",e);
		}		
	}
	
	/** actual list of comments in unit **/
	protected List<Comment> comments;
	
	public void readJava(InputStreamReader in,String name) throws IOException  {	
//if(name.indexOf("ButtonDesign")!=-1) {
//	System.out.println("ButtonDesign");
//}
		this.fileCount++;

		try {
			boolean considerComments=true;			
			cu = JavaParser.parse(in,considerComments);
//TODO: JavaParserBug: comments with some conent will not be doubled add. ?? why ??
			comments=cu.getComments();
			Iterator<Comment> it=comments.iterator();
			while(it.hasNext()) { if(!useComment(it.next())) { it.remove(); } } // remove unused comments (e.g. line commends) 
			
	    	// package------------------------------------------------------------------------------
		    PackageDeclaration p=cu.getPackage();
		    String pkgName="";
		    if(p!=null) { 
		    	pkgName=toString(p.getName());
		    	LOG.trace("pkgName {}",pkgName);
		    }
		    
		    if(unit==null) unit=new JarDoc(pkgName,options); // create unit for this package
		    PackageDoc pkg=unit.addPackage(pkgName); // add apckage	
		    
		    Node prev=null;
		    
		    List<TypeDeclaration> allCl=cu.getTypes();	   
		    for(int a=0;allCl!=null && a<allCl.size();a++) {
		    	TypeDeclaration clDeclarion=allCl.get(a);
		    	
			    String sourceName=clDeclarion.getName();
				ClassDoc clSource=pkg.addClass(sourceName); // add Class	  
			    LOG.trace("class "+sourceName);
			    clSource.pkgComment=findComment(prev,cu,p,comments); // package comment in class 
//			    pkg.setComment(clSource.pkgComment); // pacakge comment in package				    
//			    scanComments(prev,p, pkg, clSource,comments); // scan inside comments
//			    scanComments(prev, p, clSource, clSource, comments);
			    
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
			    setAnnotations(prev,clSource,clDeclarion.getAnnotations(),clSource,comments);  
//			    clSource.setComment(toString(clDeclarion.getComment())); //getJavaDoc());		  
				setComment(clSource,clDeclarion.getComment(),clSource,true);
				
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
			    			
			    			FieldDoc field=new FieldDoc(id.getName(),type,clSource,clSource,toObject(prev,exp, clSource));
			    			LOG.trace("VariableDeclarator "+id.getName());
			    			setAnnotations(prev,field, dec.getAnnotations(), clSource,comments);
			    			field.modifiers=dec.getModifiers();
			    			field.setComment(findComment(prev,cu, dec,comments)); // toString(dec.getJavaDoc());
			    			clSource.addField(field);		    			
			    			scanComments(prev,v, field, clSource,comments); // scan inside comments
			    			prev=v;
			    		}
			    		
			    	}else if(body instanceof ConstructorDeclaration) { // Constructor
			    		ConstructorDeclaration con=(ConstructorDeclaration)body;
			    		ConstructorDoc cs=new ConstructorDoc(con.getName(), clSource.getTypeName(), clSource, clSource);
			    		LOG.trace("ConstructorDeclaration "+con.getName());
			    		setAnnotations(prev,cs, con.getAnnotations(), clSource,comments);
			    		cs.modifiers=con.getModifiers();
			    		cs.setComment(findComment(prev,cu, con,comments));
			    		clSource.addConstructor(cs);
			    		scanComments(prev,con, cs, clSource,comments); // scan inside comments
			    		
			    	}else if(body instanceof MethodDeclaration) {// Method	
			    		MethodDeclaration method=(MethodDeclaration)body;
			    		
			    		MethodDoc mc=new MethodDoc(method.getName(), toString(method.getType()), clSource, clSource);
			    		ParametersDoc params=getParams(prev,method,mc,clSource); 
			    		mc.setParameter(params);
			    		
			    		LOG.trace("MethodDeclaration "+method.getName());
			    		setAnnotations(prev,mc, method.getAnnotations(), clSource,comments);
			    		mc.modifiers=method.getModifiers();
			    		mc.setComment(findComment(prev,cu, method,comments));
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
	    		scanComment(com,pkg,pkg,true);
		    }		    			    	
		     
		}catch(Throwable e) {
			e.printStackTrace();
			LOG.error("scan error in "+name+" ("+e+")",e);
		}
	}	
	
	protected ParametersDoc getParams(Node prev,MethodDeclaration method,MethodDoc methodDoc,ClassDoc clSource) throws Exception {
		List<Parameter> parameter=method.getParameters();
//		List<TypeParameter> parameter2=method.getTypeParameters();
		if(parameter==null) { return new ParametersDoc(methodDoc,clSource,0); }
		
		ParametersDoc doc=new ParametersDoc(methodDoc,clSource,parameter.size());
		for(int i=0;i<parameter.size();i++) {
			Parameter p=parameter.get(i);
			List<AnnotationExpr> annos=p.getAnnotations();
			if(annos!=null) {				
				List<AnnotationDoc> annoList=new ArrayList();
				ParameterDoc param=doc.get(i);
				for(int t=0;annos!=null && t<annos.size();t++) {	
					AnnotationDoc anno=toAnnotationDoc(prev,param, annos.get(t), clSource,comments);
			    	clSource.addAllAnnotations(anno);  // class add annotation
			    	unit.addAnnotation(anno);  // unit add annotation
			    	doc.addAllAnnotations(anno); // param add anoation
					annoList.add(anno);					
					
				}
				doc.set(i, annoList);
			}
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

	
	private String findComment(Node prev,CompilationUnit cu,Node actual,List<Comment> comments) {
		if(comments==null) { return null; }
		Node previus=prev;
		StringBuffer sb=new StringBuffer();		
		Iterator<Comment> it=comments.iterator();
	    while(it!=null && it.hasNext()) {
	    	Comment c=it.next();
	    	String str=toString(c);	    	
	    	if(previus==null || c.getBeginLine()>previus.getEndLine() || (c.getBeginLine()==previus.getEndLine() && c.getBeginColumn()>previus.getEndColumn())) {
	    		if(actual==null || c.getEndLine()<actual.getBeginLine() || (c.getEndLine()==actual.getBeginLine() && c.getEndColumn()>actual.getBeginColumn())) {
	    			it.remove(); // reomve comment 
	    			if(str!=null && str.length()>0) {  
	    				if(sb.length()>0) sb.append("\n");
	    				sb.append(str);
	    				it=null; // only first comment
	    			}
	    			
	    		}
	    	}
	    }	    
	    return sb.toString();
	}
	
	
	/** scan all comments inside node **/
	public void scanComments(Node prev,Node now,DocObject parent,ClassDoc clSource, List<Comment> comments) throws Exception {
		if(comments==null) return ;
		
		int nowLine=now.getBeginLine(),nowCol=now.getBeginColumn(),nowEndLine=now.getEndLine(),nowEndCol=now.getEndColumn();
		int prevLine=-1,prevCol=-1; if(prev!=null) { prevLine=prev.getBeginLine(); prevCol=prev.getBeginColumn(); } //PROBLEM class begin=ClassBegin,end=ClassEnd
		Iterator<Comment> it=comments.iterator();
		while(it.hasNext()) { 
	    	Comment c=it.next(); 
	    	int comStart=c.getBeginLine(), comEnd=c.getEndLine(),comEndCol=c.getEndColumn();  	
	    	if(prevLine==-1 || (c.getBeginLine()>prevLine || (c.getBeginLine()==prevLine && c.getBeginColumn()>prevCol))) {
	    		if(comEnd<nowLine || ( comEnd==nowLine && comEndCol<=nowCol)) {
		    		if(!have(prev,c) && useComment(c)) {
		    			scanComment(c,parent,clSource,true);
		    			it.remove(); // remove from list
		    		}
//	    		}else if(comEnd<nowEndLine || ( comEnd==nowEndLine && comEndCol<=nowEndCol)) {
//		    		if(!have(prev,c) && useComment(c)) {
//		    			scanComment(c,parent,clSource,false);
//		    			it.remove(); // remove from list
//		    		}
	    		}
	    	}	
	    }
	}

	private void scanComment(Comment c,DocObject parent,DocObject clSource,boolean setInParent) throws Exception {
		if(c instanceof LineComment) { return ; } // ignore line comments
		String str=toString(c);
		scanComment(str, parent, clSource,setInParent);
		c.setEndLine(0); // set comment scanned)
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
	
	/** use comment or ignore **/
	public boolean useComment(Comment c) {
		if(c==null) { return false; }
		else if(c.getContent()==null || c.getContent().trim().length()==0) { return false; } // remove empty
		else if(c instanceof BlockComment) { return false; } // ignore blocks
		else if(c instanceof LineComment) { return false; } // ignore blocks
//		if(c instanceof JavadocComment) { return true; }
		return true;
	}
	
	/** set comment to object **/
	protected void setComment(DocObject clDoc,Comment cDoc,DocObject clSource,boolean setInParent) throws Exception {	
		if(cDoc==null) { return ; }
//		String comment=toString(cDoc);
//		clDoc.setComment(comment);
		scanComment(cDoc, clDoc, clSource,setInParent);
		
		// remove seted commet from list
		Iterator<Comment> it=comments.iterator();
		while(it!=null && it.hasNext()) { 
			Comment com=it.next();
			if(com==cDoc) { 
				it.remove();
				it=null; // remove only first
			}
		}
	}
	
	//---------------------------------------------------------------------------------------
	


	
	private void scanComment(String str,DocObject parent,DocObject clSource,boolean setInParent) throws Exception {
		try {
			LOG.trace("scanComment "+str);
			AnnotationDocScanner aDocScanner=new AnnotationDocScanner(str,true);			
//			parent.setComment(str);
			if(setInParent) { parent.setComment(aDocScanner.getComment()); }// get comment before first annotation
			
			AnnotationDoc anno=aDocScanner.nextAnnotation();
			while(anno!=null) {
					anno.setTypeName(findClassName(anno.name,clSource));
					anno.setParent(parent); 
					anno.setGrup(clSource);
					
					unit.addAnnotation(anno); // add annotation to unit
					clSource.addAllAnnotations(anno); // add annotaion as source
					parent.addAllAnnotations(anno); // add annotation to parent
				
					anno=aDocScanner.nextAnnotation();
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
	
	private void setAnnotations(Node prev,DocObject source,List<AnnotationExpr> pAnno,ClassDoc clSource,List<Comment> comments) throws Exception {
		List<AnnotationDoc> list=new ArrayList<AnnotationDoc>();
	    for(int i=0;pAnno!=null && i<pAnno.size();i++) {
	    	AnnotationExpr anno=pAnno.get(i);	    	
	    	
	    	AnnotationDoc as=toAnnotationDoc(prev,source, anno, clSource,comments);
	    	
	    	clSource.addAllAnnotations(as);  // class add annotation
	    	unit.addAnnotation(as);  // unit add annotation
	    	list.add(as);
	    	
//	    	scanComments(prev,anno, as, clSource,comments); // scan inside comments
    		
	    	prev=anno;
	    }
	    	    
	    source.addAnnotations(list);
	}
	
	private AnnotationDoc toAnnotationDoc(Node prev,DocObject source,AnnotationExpr anno,ClassDoc clSource,List<Comment> comments) throws Exception {
		AnnotationDoc as=toAnnotationDoc(prev,source, anno, clSource);
//		scanComments(prev,anno, as, clSource,comments); // scan inside comments
		return as;
	}
	
	/** create annotation **/
	private AnnotationDoc toAnnotationDoc(Node prev,DocObject parent,AnnotationExpr anno,ClassDoc clSource) throws Exception{
    	String annoName=toString(anno.getName());	
    	String annoFullName=findClassName(annoName,clSource);
    	AnnotationDoc as=new AnnotationDoc(annoName,annoFullName,parent,clSource,false);        	
//    	as.setParent(clSource.getTypePackage(), ReflectUtil.removeGetSet(source.getName()));
    	scanComments(prev,anno, as, clSource,comments); // scan inside comments
    	
    	if(anno instanceof NormalAnnotationExpr) { // @ANNO(key="value",..)
    		NormalAnnotationExpr na=(NormalAnnotationExpr)anno;
    		List<MemberValuePair> pairs=na.getPairs();
    		for(int t=0;pairs!=null && t<pairs.size();t++) {
    			MemberValuePair pair=pairs.get(t);
    			String name=pair.getName();
    			Object val=toObject(prev,pair.getValue(),clSource);
    			as.add(name,val);
    			LOG.trace("Annotation param:"+name+"="+val);
    		}
    	}else if(anno instanceof SingleMemberAnnotationExpr) { // @ANNO("VALUE")
    		SingleMemberAnnotationExpr na=(SingleMemberAnnotationExpr)anno;
    		String name=ANNO_VALUE;
    		Object val=toObject(prev,na.getMemberValue(),clSource);
    		as.add(name,val);
    		LOG.trace("Annotation param:"+name+"="+val);
    	}else if(anno instanceof MarkerAnnotationExpr) { // @ANNO   		
    	}else {
    		LOG.warn("unparsed annotion "+anno);
    	}
    	     	    	
    	return as;
	}
	
	/** TODO: remove base dir from path **/
	public String shortPath(String path) {
		return path;
	}
	
	public Object toObject(Node prev,Object obj,ClassDoc clSource) throws Exception {
		if(obj==null) { return null;
		}else if(obj instanceof String) { return (String)obj;
		
		}else if(obj instanceof NullLiteralExpr) {
			return null;	
		}else if(obj instanceof BooleanLiteralExpr) {
			return ((BooleanLiteralExpr)obj).getValue();
		}else if(obj instanceof StringLiteralExpr) {
			String str=((StringLiteralExpr)obj).getValue();
			if(AttributeReference.isAttributeReference(str)) {
				return new AttributeReference(clSource.getClassName(), str, unit);
			}else { return str; }
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
				Object o=toObject(prev,exp.get(i),clSource);
				l.add(o);
			}
			return l;	
		}else if(obj instanceof AnnotationExpr) {
			AnnotationExpr a=(AnnotationExpr)obj;	
			AnnotationDoc as=toAnnotationDoc(prev,clSource, a, clSource, comments);
	    	clSource.addAllAnnotations(as);  // class add annotation
	    	unit.addAnnotation(as);  // unit add annotation
	    	return as;
			
		}else if(obj instanceof FieldAccessExpr) {
			FieldAccessExpr f=(FieldAccessExpr)obj;
			String className=findClassName(f.getScope().toString(), clSource);
			return new ClassReference(className,f.getField(),this.unit);			

		}else if(obj instanceof NameExpr) {
			NameExpr exp=(NameExpr)obj;
			String className=clSource.getClassName();
			return new ClassReference(className,exp.getName(),this.unit);
			

			
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
