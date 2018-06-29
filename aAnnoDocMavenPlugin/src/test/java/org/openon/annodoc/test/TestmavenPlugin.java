package org.openon.annodoc.test;

public class TestmavenPlugin { 
//	extends AbstractMojoTestCase {
//
//	AbstractMojo myMojo;
//	
//	public static void main(String[] args) throws Exception {
//    	TestmavenPlugin test=new TestmavenPlugin();    	
//    	test.setUp();
//    	
//    	System.out.println("end");
//	}
//    
//    /**
//     * @see junit.framework.TestCase#setUp()
//     */
//    @Before
//    protected void setUp() throws Exception {
//        // required for step1SkipperPackage lookups to work
//        super.setUp();
//
//        File testPom = new File( getBasedir(),"pom.xml" );
//
//        myMojo = initializeMojo(testPom, "aAnnoDocMavenPlugin");
////        step2SkipperUpload = initializeMojo(testPom, "skipper-upload");
////        step3SkipperInstall= initializeMojo(testPom, "skipper-install");
//    }
//       
//    //-----------------------------------------------------------------------------------------
//    
//    private AbstractMojo initializeMojo(File testPom, String skipperPackage) throws Exception {
//
//        MavenProject mavenProject = readMavenModel(testPom);
//
//        AbstractMojo initMojo = (AbstractMojo) lookupMojo( skipperPackage , testPom );
//        initializeMojoFields(mavenProject, initMojo);
//        return initMojo;
//    }
//
//    private AbstractMojo initializeMojoDelete(File testPom, String skipperPackage) throws Exception {
//
//        MavenProject mavenProject = readMavenModel(testPom);
//
//        AbstractMojo initMojo = new AnnoDocMojo();
//        initializeMojoFields(mavenProject, initMojo);
//        return initMojo;
//    }
//
//    private void initializeMojoFields(MavenProject mavenProject, AbstractMojo mojo) throws NoSuchFieldException, IllegalAccessException {
//        File workDirectory = new File( getBasedir(),
//                "src/test/resources/testProject/target/skipper/");
//
//        File overrideDirectory = new File( getBasedir(),
//                "src/test/resources/testProject/target/classes/META-INF/skipper");
//
//        //Set the properties
//        setPrivateField(mojo, "project", mavenProject, false);
//        setPrivateField(mojo, "workDirectory", workDirectory, true);
//        setPrivateField(mojo, "overrideDirectory", overrideDirectory, true);
//        setPrivateField(mojo, "SUBSTRING_SKIPPER_SEPARATOR",  "/skipper", true);
//
////        setPrivateField(mojo, "skipperServerUri",  SKIPPER_SERVER_URI, true);
//        setPrivateField(mojo, "repoName",  "local", true);
//    }
//
//    private void setPrivateField(Object objectInstance,String fieldName, Object fieldSetValue, boolean optional) throws NoSuchFieldException, IllegalAccessException {
//        try{
//            Field declaredField = objectInstance.getClass().getDeclaredField(fieldName);
//            declaredField.setAccessible(true);
//            declaredField.set(objectInstance, fieldSetValue);
//        }catch(java.lang.NoSuchFieldException e){
//            if(!optional){
//                throw e;
//            }
//        }
//    }
//
//    private MavenProject readMavenModel(File testPom) {
//        Model model = null;
//        FileReader reader = null;
//        MavenXpp3Reader mavenreader = new MavenXpp3Reader();
//        try {
//            reader = new FileReader(testPom);
//            model = mavenreader.read(reader);
//            model.setPomFile(testPom);
//        }catch(Exception ex){}
//
//        return new MavenProject(model);
//    }    
}
