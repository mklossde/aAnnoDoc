package org.openon.annodoc.test;

public class TestMojo {
//	extends AbstractMojoTestCase {
//
//    private String SKIPPER_SERVER_URI = "http://SERVER:7577/api";
//
//    AbstractMojo step1SkipperPackage;
//    AbstractMojo step2SkipperUpload;
//    AbstractMojo step3SkipperInstall;
//    AbstractMojo step4SkipperDelete;
//
//    public static void main(String[] args) throws Exception {
//    	TestMojo test=new TestMojo();    	
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
//        File testPom = new File( getBasedir(),
//                "pom.xml" );
//
//        step1SkipperPackage = initializeMojo(testPom, "skipper-package");
//        step2SkipperUpload = initializeMojo(testPom, "skipper-upload");
//        step3SkipperInstall= initializeMojo(testPom, "skipper-install");
//        step4SkipperDelete = initializeMojoDelete(testPom, "skipper-delete");
//    }
//
//    /**
//     * @throws Exception
//     */
//    @Test
//    public void testSimpleStep1to3PackageToDelete() throws Exception
//    {
//        step1SkipperPackage.execute();
//        step2SkipperUpload.execute();
//        step3SkipperInstall.execute();
//
//        Thread.sleep(10000);
//        //TODO: Log message here
//
//        step4SkipperDelete.execute();
//    }
//
//    @Test
//    public void testStep4Delete() throws Exception
//    {
//        try{
//            step4SkipperDelete.execute();
//        }catch (Exception e){
//            if(!e.getMessage().contains("404")){    //Allow this to be run even if steps 1 to 3 are not
//                throw e;
//            }
//        }
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
//        setPrivateField(mojo, "skipperServerUri",  SKIPPER_SERVER_URI, true);
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
