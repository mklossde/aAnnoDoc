package org.openon.aannodoc.scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.openon.aannodoc.source.ClassDoc;
import org.openon.aannodoc.source.JarDoc;
import org.openon.aannodoc.source.JavaParserScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Source Scanner
 * 
 * Scan sources by 
 * 		new SourceScanner(sourceDir);
 * or	
 * 		new SourceScanner() // instance
 * 			.scanJar(jarFile)	// scan jar file
 * 			.organize(); // organzise references
 * or
 * 		new SourceScanner().read(dumpFile)
 * 
 * @author michael
 *
 */
public class SourceScanner {
	private Logger LOG=LoggerFactory.getLogger(JavaParserScanner.class);
	
	//------------------------------------------
	
	/** cntains structur of scan **/
	private JarDoc unit;
	
	//------------------------------------------
	
	/** instance source scanner - without action **/
	public SourceScanner() {}
	
	/** instance source scanner and scan java-source directory **/
	public SourceScanner(String javaSourceDirectory) throws IOException {
		this(javaSourceDirectory,null);
	}
	
	/** instacne sourceScanner, scan java-source-directory and dump scan into file **/
	public SourceScanner(String javaSourceDirectory,String dumpFile) throws IOException {
		scan(javaSourceDirectory);
		organize();
		if(dumpFile!=null) save(dumpFile);
	}
	
	/** organzise references of soruces - this is need after scan **/
	public SourceScanner organize() {
		unit.organize();
		return this;
	}
	
	//------------------------------------------------------------------
	
	/** scan source directory **/
	public SourceScanner scan(String javaSourceDirectory) throws IOException  {	
		LOG.debug("SourceDir sacn {}",javaSourceDirectory);
		JavaParserScanner scanner=new JavaParserScanner(); 
		if(javaSourceDirectory.endsWith(".java")) { scanner.readFile(javaSourceDirectory); }
		else scanner.readDir(javaSourceDirectory);
		this.unit=scanner.getUnit();
		return this;
	}

	/** scan jar file **/
	public void scanJar(String jarFile,String dumpFile) throws Exception  {	
		LOG.debug("SourceDir jarFile {}",jarFile);
		JavaParserScanner scanner=new JavaParserScanner(); 
		scanner.readJar(jarFile);
		this.unit=scanner.getUnit();
		if(dumpFile!=null) save(dumpFile);		
	}		
		
	//------------------------------------------------------------------
	
	/** get documentation object **/
	public JarDoc getUnit() { return unit; }
	
	
	
	//------------------------------------------------------------------
	
	/** save scan to dump-file **/
	public void save(String file) throws IOException {
		LOG.debug("save unit "+file);
		FileOutputStream f=new FileOutputStream(file);
		ObjectOutputStream out=new ObjectOutputStream(f);
		out.writeObject(unit);
		out.close();
		f.close();
	}
	
	/** read scan from dump-file **/
	public boolean read(String file) throws IOException {
		long start=System.currentTimeMillis();
		File ff=new File(file);
		if(!ff.isFile()) return false;
		LOG.debug("read unit "+file);
		FileInputStream f=new FileInputStream(ff);
		ObjectInputStream in=new ObjectInputStream(f);
		try {
			unit=(JarDoc)in.readObject();
		}catch(ClassNotFoundException e) { 
			in.close(); f.close();
			throw new IOException(e);
		}
		in.close();
		f.close();	
		return true;
	}
	

	
	//--------------------------------------------------------------------------------------
	
	public String toString() {
		if(unit==null) return "SourceScanner ";
		else return "SourceScanner ("+unit+")";
	}
	
}
