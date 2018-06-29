package org.openon.aannodoc.generator;

import java.io.IOException;
import java.util.List;

import org.openon.aannodoc.annotation.aApplication;
import org.openon.aannodoc.annotation.aAttribute;
import org.openon.aannodoc.annotation.aBug;
import org.openon.aannodoc.annotation.aChange;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.annotation.aVersion;
import org.openon.aannodoc.asciidoc.SequenceDiagramWriter;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.utils.AnnoUtils;
import org.openon.aannodoc.utils.DocUtils;

/**
 * Generator for History-Documenation via AsciiDocumentation
 * 
 * @author Michael Kloss
 *
 */
@aDoc(title="generator/AsciiHistoryDoc")
public class AsciiHistoryDoc extends AsciiDocGeneratorImpl implements DocGenerator {
	
	public static final String NAME="HistoryDoc";
	
	public static String TITLE="History Documentation";
	
	//-----------------------------------------------------------------------------
	
	/** aAplication doc **/
	protected AnnotationDoc application;
	protected String title=this.TITLE,author=null,version=null,date=null,depcrecated=null,copyright=null;
	
	protected List<AnnotationDoc> versions,versionList;
	
	//-----------------------------------------------------------------------------
	
	public AsciiHistoryDoc() { super(); }
	
	/** document head **/
	public void head(String outputName) throws IOException {	
		init();		
		// Doc Title
		w.title(title+" "+TITLE,author,null,version);		
		String genLabel=(String)options.get("genlabel"); if(genLabel==null) { genLabel="aAnnoDoc created on"; }
		w.nl().w(":last-update-label: "+genLabel).nl();	
		if(depcrecated!=null) { w.warning(depcrecated); }		
		// App Doc		
		w.literalBlock(AnnoUtils.getDoc(application)); 
		// Copyright
		w.paragraph(copyright);
	}
	
	/** document body **/
	public void body(String outputName) throws IOException {
		wrtieVersionTable();
		wrtieVersions();
	}
	
	/** document bottom **/
	public void bottom(String outputName) throws IOException {		
		w.close();
	}
	
	//--------------------------------------------------------------------------------------
	
	public void init() throws IOException {		
		application=annotations.getAnnotation(aApplication.class);
		if(application!=null) {			
			title=AnnoUtils.getTitle(application,true);
			author=AnnoUtils.getAuthor(application, 1);version=AnnoUtils.getVersion(application, 1);
			date=AnnoUtils.getDate(application, 1); depcrecated=AnnoUtils.getDeprecated(application, 1);
			copyright=application.getValueString("copyright");
		}
	}
	
	//--------------------------------------------------------------------------------------

	
	/** write versions/changes to document **/
	public void wrtieVersionTable() throws IOException {
		try {
			if(application==null) { return ; }
			// get versions from @aAppliction
			versions=(List<AnnotationDoc>)application.getValueObject(aDoc.fVERSIONS); 
			//  find all @aVersion in all classes	
			versionList=versions=annotations.findAnnotation(aVersion.class); 
			if(versions==null) { this.versions=versionList; versionList=null; }  // without versions in @aAppliction =>	user versionList			
			if(versions==null || versions.size()==0) { return ; }
			
			/** do versions have released **/
			boolean onlyReleased=false; 
			for(int i=0;onlyReleased && i<versions.size();i++) {
				if(AnnoUtils.get(versions.get(i),aDoc.fRELEASED)!=null) { onlyReleased=true; } 
			}
			
			AnnoUtils.sortDate(versionList);
			
			w.table("Versions", "Version","Date","Title","Author","Approved","Released","Deprecated");
			for(int i=0;i<versions.size();i++) {
				AnnotationDoc doc=versions.get(i);
				String released=AnnoUtils.get(doc,aDoc.fRELEASED);
				if(!onlyReleased || released!=null) {
					String deprecated=AnnoUtils.get(doc,aDoc.fDEPRECATED);
					String version=AnnoUtils.getVersion(doc, 1);
					if(!DocUtils.e(deprecated)) { version="[.line-through]#"+version+"#"; }
					w.tableLine(version,AnnoUtils.getDate(doc, 1),AnnoUtils.getTitle(doc,true),AnnoUtils.getAuthor(doc),
							AnnoUtils.get(doc,aDoc.fAPPROVED),AnnoUtils.get(doc,aDoc.fRELEASED),deprecated);
				}
			}
			w.tableEnd();
			
			
			// AnnoUtils.getDoc(doc)
		}catch(Exception e) { throw new IOException("doc changes excetpion "+e,e); } 
	}
	
	public void wrtieVersions() throws IOException {
		for(int i=0;versions!=null && i<versions.size();i++) {
			String fromDate=null;
			if(i<versions.size()-1) { fromDate=versions.get(i+1).getValueString(aDoc.fDATE); }
			AnnotationDoc version=versions.get(i);
			writeVersion(version,fromDate);			
		}
	}
	
	public void writeVersion(AnnotationDoc version,String fromDate) throws IOException {
		String ver=AnnoUtils.getVersion(version, 1);
		String toDate=version.getValueString(aDoc.fDATE);
			
		w.subTitle("Version "+ver);
		w.italic(AnnoUtils.getDate(version,1)+" "+AnnoUtils.getTitle(version));		
//		w.warning(AnnoUtils.get(version, aDoc.fDEPRECATED));		
		w.literalBlock(AnnoUtils.getDoc(version));
		
//		AnnoUtils.writeTable(w, "Informations", version);						
			
		
		List<AnnotationDoc> changes=AnnoUtils.findFeatures(annotations, ver,fromDate,toDate);	 // Features
		changes.addAll(AnnoUtils.findChanges(annotations, ver,fromDate,toDate)); // Cahnges
		changes.addAll(AnnoUtils.findBugs(annotations, ver,fromDate,toDate)); // Bugs
		AnnoUtils.sortDate(changes);

		w.table("Changes");
		for(int i=0;changes!=null && i<changes.size();i++) {
			AnnotationDoc anno=changes.get(i);
			String type=anno.getSimpleName();
			w.tableLine(type,AnnoUtils.getDate(anno,1),AnnoUtils.getTitle(anno)); 			
		}
		w.tableEnd();
		
		
		if(changes!=null && changes.size()>0) {			
			for(int i=0;changes!=null && i<changes.size();i++) {
				AnnotationDoc anno=changes.get(i);
				String type=anno.getSimpleName();
				if(type.equals(aBug.class.getSimpleName())) { writeBugs(anno); } 
				if(type.equals(aChange.class.getSimpleName())) { writeChange(anno); } 
				if(type.equals(aFeature.class.getSimpleName())) { writeFeature(anno); } 
			}			
		}
				
		w.subTitleEnd();
	}
	
	public void writeFeature(AnnotationDoc feature) throws IOException {
		String title=AnnoUtils.getTitle(feature);
//		w.paragraph(AnnoUtils.getDoc(feature));
//		AnnoUtils.writeTable(w, "Informations", feature);

		w.subTitle("Feature "+title);
//		w.list("Feature "+featureTitle);
//		w.list("Feature "+featureTitle+" ["+w.toString(AnnoUtils.getDate(feature,1),"")+" "+w.toString(AnnoUtils.getVersion(feature,1),"")+"]");
//		w.list("["+w.toString(AnnoUtils.getDate(feature,1),"")+" "+w.toString(AnnoUtils.getVersion(feature,1),"")+"] Feature "+featureTitle);
		w.literalBlock(AnnoUtils.getDoc(feature));
		w.subTitleEnd();	
	}
	
	public void writeChange(AnnotationDoc change) throws IOException {
		String title=AnnoUtils.getTitle(change);
//		w.paragraph(AnnoUtils.getDoc(feature));
//		AnnoUtils.writeTable(w, "Informations", feature);
//			
		w.subTitle("Change "+title);
//		w.list("Change "+featureTitle);
//		w.list("Feature "+featureTitle+" ["+w.toString(AnnoUtils.getDate(feature,1),"")+" "+w.toString(AnnoUtils.getVersion(feature,1),"")+"]");
//		w.list("["+w.toString(AnnoUtils.getDate(feature,1),"")+" "+w.toString(AnnoUtils.getVersion(feature,1),"")+"] Feature "+featureTitle);
		w.literalBlock(AnnoUtils.getDoc(change));
		w.subTitleEnd();
	}
	
	public void writeBugs(AnnotationDoc bug) throws IOException {
		String title=AnnoUtils.getTitle(bug);
		w.subTitle("Bug "+title);
//		w.list("Bug "+title);
//		w.paragraph(AnnoUtils.getDoc(feature));
//		AnnoUtils.writeTable(w, "Informations", feature);
//		w.subTitleEnd();			
		w.caution(AnnoUtils.get(bug, aDoc.fLEVEL)); 
		w.literalBlock(AnnoUtils.getDoc(bug));
		w.italic(AnnoUtils.get(bug, aDoc.fFIX));
		w.subTitleEnd();
	}
}
