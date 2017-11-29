aAnnoDoc - Java annotation based documentation
(Apache License 2.0)

DONATE :-) to https://www.paypal.me/openonorg/5
(If you like aAnnoDoc please donate via paypal to openonorg. Every $1-$nnn is welcome. )

Normaly documenation and devleopment are two seperated parts
and it is time and coast intensive to write one after each other. 

A good idea is two sync the development and documenation process,
by put the information near by the source code. 

This is the base idea of AnnoDoc-Project to add application documenation
(like dsciption, bugs, features, examples, atrriubtes,...) to (or near to) 
the source code and generate the documenation during 
compile-process (or maven deploy process). 

IT IS NOT ANOTHER JAVA-API-DOCUMENATION ! 
The idea is write application information (like Manuals, installtion-guids, ReadMes, AsiccDoc,.. )  
inside the project. 

The document-generation is splitted into two parts
- scanner: SourceCode or JAR scanner/reader which read all the project informations 
- generator: generates the structure/documents into a readable format (like AsciiDoc,PDF,HTML,.. )  

The important part are the gernerator, which are individual programmable to 
generate individual documents. There are some base generators included
to create a Application-Manual, a Annotation-Overview,... 

To work with AnnoDoc use one of this ways
- add the aAnnoDocMavenPlugin to generate documents during deployment
- call org.openon.aannodoc.AnnoDoc from console
- program a AnnoDoc Process manual 

The Scanner scans all java-sources and use comments for documenation. 
Simple: 
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
@version 0.0.1 - 21.11.2017