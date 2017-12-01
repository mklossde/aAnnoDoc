package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.aAnnoDoc;
import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aBug;
import org.openon.aannodoc.annotation.aConnection;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aError;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.annotation.aService;
import org.openon.aannodoc.asciidoc.SequenzDiagramWriter;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.utils.AnnoUtils;
import org.openon.aannodoc.utils.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generator for AnnoDoc-Annotation documents (aDoc,aAttribtue,aBug,..) 
 * 
 * @author michael
 *
 */
@aDoc(title="generator/AnnoDocGenerator")
public class AnnotationAppDoc extends AsciiDocGeneratorImpl implements DocGenerator {
	private static final Logger LOG=LoggerFactory.getLogger(AnnotationAppDoc.class);
	

	
	public static final String NAME="AppDoc";
	
	public static final String ANNOTATIONS="annotations";
	
	
	public AnnotationAppDoc() { super(); }
	
	
	//-----------------------------------------------------------------------------
	// Options
		
	/**
	 * option: format of output 
	 * 
	 * 		adoc= AsciiDoc
	 * 		html= html (AsciiDoc)
	 * 		pdf = pdf (AsciiDoc)
	 * 
	 * 
	 * @return option-format
	 */
	@aAttribute(title="options/format")
	public String getFormat() {
		Object obj=options.get(Options.OPTION_FORMAT); if(obj==null) { obj="html"; }
		return (String)obj;
	}
	
	/**
	 * option: output-file
	 * 
	 * @return option-output-file
	 */
	@aAttribute(title="options/output")
	public String getOutput() throws IOException {
		String obj=(String)options.get(Options.OPTION_OUTPUT); 
		if(obj==null) { obj="AnnoDocOutput"; }
		if(obj.indexOf('.')==-1) { obj=obj+"."+getFormat(); }
		return (String)obj;
	}

	//---------------------------------------------
	
	public List<String> listOutputs() throws IOException {
		List<String> list=new ArrayList<String>();		

		boolean defaultAdded=false;
		List<AnnotationDoc> l=adoc.findAnnotation(aDoc.class);
		for(int i=0;i<l.size();i++) {
			AnnotationDoc doc=l.get(i);
			String file=doc.getValueString(aDoc.fFILE);
			if(file!=null && file.length()>0) { list.add(file); }
			else if(!defaultAdded) { list.add(aAnnoDoc.DEFAULT_FILE); defaultAdded=true; }	
		}
			
		if(list.size()==0) { list.add(aAnnoDoc.DEFAULT_FILE); }
		
		return list;
	}
	
	//---------------------------------------------
	
	/** document head **/
	public void head(String outputName) throws IOException {
		createStructure(outputName);
		
		String title="Docs",author=null,version=null,date=null,depcrecated=null;
		AnnotationDoc main=getMainDoc(docs);
//System.out.println("d:"+docs);
		if(main!=null) {
			title=AnnoUtils.getTitle(main,true);
			author=AnnoUtils.getAuthor(main, 1);version=AnnoUtils.getVersion(main, 1);
			date=AnnoUtils.getDate(main, 1); depcrecated=AnnoUtils.getDeprecated(main, 1);
		}
		w.title(title,author,null,version); // ,doc.getAnnotation("author"),doc.getAnnotation("date"));
		
		String genLabel=(String)options.get("genlabel"); if(genLabel==null) { genLabel="aAnnoDoc created on"; }
		w.nl().w(":last-update-label: "+genLabel).nl();	
		if(depcrecated!=null) { w.warning(depcrecated); }
		
		if(main!=null) {
			w.paragraph(AnnoUtils.getDoc(main));
			List<AnnotationDoc> attr=adoc.findAnnotationIn(main.getParent(),aAttribute.class,aDoc.fGROUP,null);
			attribtue("Attributes", attr);
		}
	}
	
	/** document bottom **/
	public void bottom(String outputName) throws IOException {
//		w.nl2().w(":last-update-label: "+(new Date())+" [<openon.org/aannodoc>] ").nl2();		
		w.close();
	}
	
	//---------------------------------------------
	
	protected Tree<AnnotationDoc> docs;
	
	protected void createStructure(String outputName) {
		List<AnnotationDoc> list=find(aDoc.class);
		String name=doc.getName();
		docs=toTree(name,list);
//System.out.println("d:"+docs);
	}
	
	/** find annotation for doc **/
	protected List<AnnotationDoc> find(Class cl) { 
		//TODO: read by file here ?? 
//		List<AnnotationDoc> list;
//		if(outputName.equals(aAnnoDoc.DEFAULT_FILE)) { list=adoc.findAnnotation(aDoc.class); }
//		else { list=adoc.findAnnotation(aDoc.class,"file",outputName); }
//		if(list==null || list.size()==0) { return ; }
		List<AnnotationDoc> list=adoc.findAnnotation(cl);
//TODO:only empty group here
//		List<AnnotationDoc> list=adoc.findAnnotation(cl,"group",null);
		return list;
	}
	
	//---------------------------------------------
	
	/** document body **/
	public void body(String outputName) throws IOException {
		docs(outputName);
		features(outputName);
		services(outputName);
		connections(outputName);
		
		error(outputName);
		bugs(outputName);
		
		// aField
		// aArchitketure
		// aExample		
	}
	
	public void services(String outputName) throws IOException {
		List<AnnotationDoc> list=find(aService.class);
		if(list==null || list.size()==0) { return ; }
//		w.subTitle("Services");
		annotationTree(toTree("Services",list));
//		w.subTitleEnd();
	}
	
	public void connections(String outputName) throws IOException {
		List<AnnotationDoc> list=find(aConnection.class);
		if(list==null || list.size()==0) { return ; }
//		w.subTitle("Services");
		annotationTree(toTree("Connections",list));
//		w.subTitleEnd();
	}
	
	public void features(String outputName) throws IOException {
		List<AnnotationDoc> list=find(aFeature.class);
		if(list==null || list.size()==0) { return ; }
//		w.subTitle("Services");
		annotationTree(toTree("Features",list));
//		w.subTitleEnd();
	}
	
	public void docs(String outputName) throws IOException {
		annotationChids(docs);
	}
	
	public void bugs(String outputName) throws IOException {				
		List<AnnotationDoc> list=find(aBug.class);
		if(list==null || list.size()==0) { return ; }
		w.subTitle("Bugs");
		annotationBugs(list);
		w.subTitleEnd();
	}
	
	public void error(String outputName) throws IOException {				
		List<AnnotationDoc> list=find(aError.class);
		if(list==null || list.size()==0) { return ; }
		w.subTitle("Error Handling");
		annotationErors(list);
		w.subTitleEnd();
	}
	
	
	//----------------------------------------------------------------
	
	public void annotationTree(Tree<AnnotationDoc> tree) throws IOException {
		AnnotationDoc a=tree.getData();
		Object name=tree.getName();
		if(!w.e(name)) { w.subTitle(name); }
		if(a!=null) { annotation(a); }
		annotationChids(tree);
		if(!w.e(name)) { w.subTitleEnd(); }
	}
	
	public void annotationChids(Tree<AnnotationDoc> tree) throws IOException{
		for(int i=0;i<tree.size();i++) { 
			Object o=tree.get(i);
			if(o instanceof Tree) { annotationTree((Tree)o); } 
			else {annotation((AnnotationDoc)o); }
		}
	}
	
	public void annotation(AnnotationDoc doc) throws IOException {
		String name=doc.getName();
		if("aDoc".equals(name)) { annotationDoc(doc); }
		else if("aService".equals(name)) { annotationService(doc); }
		else if("aFeature".equals(name)) { annotationDoc(doc); }
		else { annotationUnkown(doc); }
	}
	
	/** write aDoc Annoation **/
	public void annotationDoc(AnnotationDoc doc) throws IOException {
		String title=AnnoUtils.getTitle(doc,true); //, " ",doc.getValuePath());
		w.subTitle(title);
		w.small(AnnoUtils.getValue(doc, aDoc.fSIMPLE)); // simple 
		infos(doc); // author,date,version,deprecated
		attribtue(title,adoc.findAnnotation(aAttribute.class, aDoc.fGROUP, title)); // attributes
		w.paragraph(AnnoUtils.getDoc(doc)); // doc
		w.subTitleEnd();
	}
	
	/** write list of bus **/
	public void annotationBugs(List<AnnotationDoc> list) throws IOException {
		if(list==null || list.size()==0) { return  ; }
		w.table("Bugs", "Title","Author","Date","FIX","Describtion");
		for(int i=0;i<list.size();i++) {
			AnnotationDoc doc=list.get(i);
			w.tableLine(AnnoUtils.getTitle(doc,true),AnnoUtils.getAuthor(doc),AnnoUtils.getDate(doc, 1),AnnoUtils.getValue(doc, "fix"),AnnoUtils.getDoc(doc));
		}
		w.tableEnd();
	}
	
	/** write list of errors **/
	public void annotationErors(List<AnnotationDoc> list) throws IOException {
		if(list==null || list.size()==0) { return  ; }
		w.table("Errors", "When","Title","Describtion");
		for(int i=0;i<list.size();i++) {
			AnnotationDoc doc=list.get(i);
			w.tableLine(AnnoUtils.getValue(doc, "when"),AnnoUtils.getTitle(doc,true),AnnoUtils.getDoc(doc));
		}
		w.tableEnd();
	}
	
	/** write aDoc Annoation **/
	public void annotationService(AnnotationDoc doc) throws IOException {
		String title=AnnoUtils.addString(doc.getValue(aDoc.fTITLE)); //, " ",doc.getValuePath());
		w.subTitle(title); 
		w.small(AnnoUtils.getValue(doc, aDoc.fSIMPLE)); // simple 
		infos(doc); // author,date,version,deprecated

		// request && response
		String req=AnnoUtils.toString(doc.getValue("request"),null);
		String res=AnnoUtils.toString(doc.getValue("response"),null);
		if(req!=null || res!=null) {
			SequenzDiagramWriter seq=new SequenzDiagramWriter(w);
			seq.start("Service Request-Response");
			seq.to(req, "Extern",title);
			seq.from(res, "Extern",title);
			seq.end();
		}
		// atrributes
		List<AnnotationDoc> attr=adoc.findAnnotation(aAttribute.class, aDoc.fGROUP, title);
		attribtue(title,attr);
		
		w.paragraph(AnnoUtils.getDoc(doc)); // doc
		w.subTitleEnd();
	}
	
	public void infos(AnnotationDoc doc) throws IOException {
		w.warning(AnnoUtils.getDeprecated(doc, 1));		// deprecated
		Object author=AnnoUtils.getAuthor(doc); if(author!=null) { w.list("Author: "+author); }
		Object verson=AnnoUtils.getVersion(doc, 1); if(verson!=null) { w.list("Version: "+verson); }
		Object date=AnnoUtils.getDate(doc, 1); if(date!=null) { w.list("Date: "+date); }
	}
	
	/** write unkown Annoation **/
	public void annotationUnkown(AnnotationDoc doc) {
		String title=AnnoUtils.addString(doc.getValue(aDoc.fTITLE)); //, " ",doc.getValuePath());
		w.subTitle(title);
//		attribtue(title,adoc.findAnnotation(aAttribute.class, aDoc.fGROUP, title));
		Map<String,Object> values=doc.getValues();
		for (Entry<String,Object> it : values.entrySet()) {w.tableLine(it.getKey(), it.getValue());}
		w.table("Annoation Attrbutes", "name","value");
		
		w.tableEnd();
		w.paragraph(AnnoUtils.getDoc(doc));
		w.subTitleEnd();
	}
	

	public void attribtue(String title,List<AnnotationDoc> attr) {
		if(attr==null || attr.size()==0) { return ; }
		w.table("Attributes of "+title, "Name","value","Type","Options","Optional","simple","description","deprecated");
		for(int i=0;i<attr.size();i++) { 
			AnnotationDoc doc=attr.get(i);
			w.tableLine(AnnoUtils.getTitle(doc,true)
					,AnnoUtils.getDefaultOrValue(doc)
					,AnnoUtils.getType(doc)
					,AnnoUtils.getValue(doc,"options")
					,AnnoUtils.getValue(doc,"optional")
					,AnnoUtils.getValue(doc,"simple")
					,AnnoUtils.getDoc(doc)
					,AnnoUtils.getValue(doc,"deprecated")
				);
//			w.w("|name +2|fgdfgdf fgdf gdfgd df df|String||||").nl();
		}
		w.tableEnd();
	}
	
		
	//------------------------------------------------------------------
	
	/** get root Object of tree **/
	public AnnotationDoc getMainDoc(Tree<AnnotationDoc> tree) {
		if(tree.getData()!=null) { return tree.getData(); }
		else if(tree.size()>0 && tree.get(0) instanceof  AnnotationDoc ) { return ((AnnotationDoc)tree.get(0)); }
		return null; 
	}
	
	/** get title of tree **/
	public String getTitle(Tree<AnnotationDoc> tree) {
		AnnotationDoc doc=tree.getData();
		if(doc!=null) { return AnnoUtils.getTitle(doc,true); }
		else { return tree.getName(); }
	}
	
	//------------------------------------------------------------------
	public Tree<AnnotationDoc> toTree(String rootName,List<AnnotationDoc> list) {
		Tree<AnnotationDoc> tree=new Tree<AnnotationDoc>(rootName);
		for(int i=0;i<list.size();i++) {
			AnnotationDoc a=list.get(i);
//			String name=a.getValueNameString();
			String name=AnnoUtils.getTitle(a,false);
			if(a.getValue(aDoc.fFILE)!=null && tree.getData()==null) {  // with file as primary
				tree.set(a);
			}else {
				if(filter==null || filter.generateTitle(name,a)) {
					tree.getTreeOf(name, true, true ).add(list.get(i));
				}
			}
		}
		return tree.sort(null);
	}

}
