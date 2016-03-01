# OpenClinica XSLTs

## Goals
- Enhancement for existing OpenClinica XSLT job processing.
- Test harness for existing and new XSLTs.


## Description
This project is mostly a proof of concept at this stage, and might be 
a bit wrong since it's the first thing I've written in Java.

The lstevens.xslt.XLSTTransform class is meant to be a replacement for the part 
of org.akaza.openclinica.job.XsltTransformJob. Specifically, in the while loop 
at line 226, the XLST transform is done to a FileOutputStream instead of File. 

An XSLT result-document instruction will only work if a File object is used. 
Result-document is useful for outputting multiple documents from a transform, 
instead of having separate stylesheets that do mostly the same stuff, or 
creating one big output file that needs to be cut up afterwards.


## Tests
The testing is a little bit magical, but not too much. The intention is that 
the current tests will be run as unit tests, with some later integration tests 
that do the same stuff but with the Spring app context loaded, and/or with 
test data from a database instead of an XML file.

How to make new tests, and run the existing tests.
- Put any test input XML documents into xml_input.
- For any XLST, create a subfolder "expected".
- Inside the expected folder, make a folder with the same name as an output.
- Put inside that [output_name] folder the expected / desired output.
- Make a new JUnit @Test function inside of XsltTransformTest.
- In the test, specify the XSLT path, name and output files.
- Run the test(s) with JUnit.

