
aAnnoDoc - (Java) annotation based documentation
(Apache License 2.0)

DONATE :-) to https://www.paypal.me/openonorg/5
(If you like aAnnoDoc, donates are welcome via paypal to openonorg. Every $1 is fine ;-)

Background Story, why aAnnoDoc
Normal, documentation and development are two separated parts
and it is time and coast intensive to write one after each other. 
A good idea is two sync the development and documentation process,
by put the information near by the source code. 

This is the base idea of AnnoDoc-Project to add application documentation
(like deception, bugs, features, examples, attributes,...) to (or near to) 
the source code and generate the documentation during compile-process (or maven deploy process). 
Write application information (like Manuals, installtion-guids, ReadMes, AsiccDoc,.. ) inside each project. 

IT IS NOT ANOTHER JAVA-API-DOCUMENATION ! Its a application-documenation.

The documentation is flexible by use (or development) different generators.  
For example the "AppDoc"-generator create a docs for predefined Annotations. 
Other generators are "ProjectInfo" for quick project-documents, "RestDoc" 
for Java-rest-service documents, "AnnoInfo" for Annotation-Overview. 

To work with AnnoDoc use one of this ways
- add the aAnnoDocMavenPlugin to generate documents during maven deployment
- call org.openon.aannodoc.AnnoDoc from console with options
- program a AnnoDoc Process method 

For more information or examples see directory <docs/>

Have fun... 


 
Simple Example of a aDoc documentation:
 
		- The comment above belong to the annotation/method below. 
		- Test/comment-annotations belong to the text right until next @ 
		- Inline-comments are ignored !!! 

		/** this is the AnnoDoc documentation of aDoc **/
		@aDoc
		/** this is api-documenation of myFunction **/
		public void myFunction(String arg) {
			// this inline code will be ignores
			/** this is the annoDoc-documentaiton of aFiled **/
			@aFiled
			/** this is the api-documentaiton of myVar **/
			String myVar;
		}
		/*
		  * This is the myReturn api-documenation (which include comment-annotations) 
		  *	@Bug - this is the annoDoc documentation of aBug (until next @) 
		  **/
		public void myReturn(String arg) { }



@author Michael Kloss- mk@almi.de
@version 1.0.0 - 03.12.2017