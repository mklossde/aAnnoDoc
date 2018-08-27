package org.openon.aannodoc.generator.writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openon.aannodoc.Options;
import org.openon.aannodoc.annotation.aApplication;
import org.openon.aannodoc.annotation.aBug;
import org.openon.aannodoc.annotation.aChange;
import org.openon.aannodoc.annotation.aDoc;
import org.openon.aannodoc.annotation.aFeature;
import org.openon.aannodoc.annotation.aVersion;
import org.openon.aannodoc.asciidoc.AsciiDocWriter;
import org.openon.aannodoc.scanner.SourceAnnotations;
import org.openon.aannodoc.source.AnnotationDoc;
import org.openon.aannodoc.utils.AnnoUtils;
import org.openon.aannodoc.utils.DocUtils;

/**
 * AsciiDoc Version-Writer
 * 
 *
 */
public class VersionWriter extends ApplicationWriter {


	
//	protected List<AnnotationDoc> versions,versionList;
	
	public VersionWriter(AsciiDocWriter w,SourceAnnotations adoc,Options options) throws IOException {
		super(w, adoc,options);
	}
	

	
	/** get versions of application **/
	public List<AnnotationDoc> versions()   throws IOException {
		AnnotationDoc application=application();
		if(application==null) { return null; }
		// get versions from @aAppliction
		 List<AnnotationDoc> versions=(List<AnnotationDoc>)application.getValueObject(aDoc.fVERSIONS); 
		//  find all @aVersion in all classes	
		List<AnnotationDoc> versionList=versions=annotations.findAnnotation(aVersion.class); 
		if(versions==null) { versions=versionList; versionList=null; }  // without versions in @aAppliction =>	user versionList			
		
		AnnoUtils.sortDate(versions);
		return versions;
	}
	
	/** get previous version **/
	public AnnotationDoc previusVersion(AnnotationDoc version) throws IOException  {
		List<AnnotationDoc> versions=versions();
		for(int i=0;versions!=null && i<versions.size();i++) {
			if(isVersion(version,versions.get(i))) {
				if(i<versions.size()-1) { return versions.get(i+1); }
				else { return null; }
			}
		}
		return null;
	}
	
	/** get date of previous version **/
	public String prevDate(AnnotationDoc version) throws IOException  {
		AnnotationDoc prevVersion=previusVersion(version);
		if(prevVersion==null) { return null; } 
		return prevVersion.getResolveString(aDoc.fDATE);
	}
	
	public boolean isVersion(AnnotationDoc v1,AnnotationDoc v2) throws IOException  {
		if(v1==null || v2==null) { return false; }
		else if(v1==v2) { return true; }
		return false; // TODO: implemnt version match
	}
	//----------------------------------------------------------------------------------------
	
	/** write versions/changes to document **/
	public void wrtieVersionTable(Options options) throws IOException {
		try {
			List<AnnotationDoc> versions=versions();
			if(versions==null || versions.size()==0) { return ; }
			
			/** do versions have released **/
			boolean onlyReleased=false; 
			for(int i=0;onlyReleased && i<versions.size();i++) {
				if(AnnoUtils.get(versions.get(i),aDoc.fRELEASED)!=null) { onlyReleased=true; } 
			}
									
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
	
	public void wrtieVersions(Options options) throws IOException {
		List<AnnotationDoc> versions=versions();
		for(int i=0;versions!=null && i<versions.size();i++) {
			String fromDate=null;
			if(i<versions.size()-1) { fromDate=versions.get(i+1).getResolveString(aDoc.fDATE); }
			AnnotationDoc version=versions.get(i);
			writeVersion(version);			
		}
	}
		
	public void writeVersion(AnnotationDoc version) throws IOException {

		String ver=AnnoUtils.getVersion(version, 1);
		
		w.subTitle("Version "+ver);
		w.italic(AnnoUtils.getDate(version,1)+" "+AnnoUtils.getTitle(version));		
//		w.warning(AnnoUtils.get(version, aDoc.fDEPRECATED));		
		w.literalBlock(AnnoUtils.getDoc(version));
		
		List<AnnotationDoc> changes=changes(version,true, true, true);
		writeVersionTable(changes);
		writeVersionDetails(changes);
	}	
	
	public List<AnnotationDoc> changes(AnnotationDoc version,
			boolean includeChanges,boolean includeBugs,boolean includeFeatures) throws IOException {
		
		String ver=AnnoUtils.getVersion(version, 1);
		String fromDate=prevDate(version);
		String toDate=version.getResolveString(aDoc.fDATE);
		
		List<AnnotationDoc> changes=new ArrayList<AnnotationDoc>();
		if(includeFeatures) { changes.addAll(AnnoUtils.findFeatures(annotations, ver,fromDate,toDate));	} // Features
		if(includeChanges) {changes.addAll(AnnoUtils.findChanges(annotations, ver,fromDate,toDate)); }// changes
		if(includeBugs) { changes.addAll(AnnoUtils.findBugs(annotations, ver,fromDate,toDate)); }// Bugs
		AnnoUtils.sortDate(changes);
		return changes;
	}
		
	public void writeVersionTable(List<AnnotationDoc> changes) throws IOException {
		
		w.table("Changes");
		for(int i=0;changes!=null && i<changes.size();i++) {
			AnnotationDoc anno=changes.get(i);
			String type=anno.getSimpleName();
			w.tableLine(type,AnnoUtils.getDate(anno,1),AnnoUtils.getTitle(anno)); 			
		}
		w.tableEnd();
		
	}
	
	public void writeVersionDetails(List<AnnotationDoc> changes) throws IOException {
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
