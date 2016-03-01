package lstevens.xslt;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertTrue;


public class XsltTransformTest {


    /**
     * Get a List of the xml_input files for running tests.
     *
     * @return A list of xml input files to test with.
     */
    public List<File> getTestXMLFiles() {
        List<File> test_xml = new ArrayList<>();
        File[] files = new File("xslt/xml_input").listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    test_xml.add(file);
                }
            }
        }
        return test_xml;
    }


    public Path getTestFilePath(String output){
        Path out = Paths.get(output);
        return Paths.get("test_" + out.getFileName());
    }

    /**
     * Delete the XSL test output files if they exist.
     *
     * Assumes the file names are the same as output but starting with "test".
     *
     * @param outputs File paths of output XML files.
     * @throws IOException if an I/O error occurs.
     */
    public void deleteOutputFiles(List<String> outputs)
            throws IOException {
        for (String file : outputs) {
            Files.deleteIfExists(getTestFilePath(file));
        }
    }

    /**
     * Run the XSL and check if the output file(s) match expected.
     *
     * @param XSLPath Path to XSL file to run.
     * @param outputs List of output files expected for the XSL.
     * @throws IOException if an I/O error occurs.
     */
    public void testSameOutput(Path XSLPath, List<String> outputs)
            throws IOException {

        // Clean up any output files from previous runs
        deleteOutputFiles(outputs);

        // Test with each of the xml_input files.
        for (File test : getTestXMLFiles()){
            String testName = test.getName().split("\\.")[0];
            Path testPath = XSLPath.getParent().resolve("expected").resolve(testName);

            // Using first since if it's a single file transform, it'll be first.
            // If it's a multi-output transform then the file name doesn't matter.
            Path testFile = getTestFilePath(outputs.get(0));
            Path testFilePath = testPath.resolve(testFile);
            try {
                new XsltTransform().transform(
                        XSLPath.toString(),
                        test.getPath(),
                        testFilePath.toString()
                );
            } catch (TransformerException e) {
                fail("Transformer exception. Check the XSL.");
                e.printStackTrace();
            }

            // For each output file, assert that it's the same as expected.
            for (String output : outputs) {
                Path testOutput = testPath.resolve(getTestFilePath(output));
                Path expectedOutput = testPath.resolve(output);
                assertTrue(
                        "Transform result doesn't match expected: " + testName + ", " + output,
                        FileUtils.contentEquals(
                                new File(testOutput.toString()),
                                new File(expectedOutput.toString()))
                );
            }
        }
    }


    /**
     * The discrepancy_notes_report_pdf.xsl output should match expected.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testDiscrepancyNotesReportPDF()
            throws IOException {
        String folder = "xslt/discrepancy_note_pdf";
        Path XSLPath = Paths.get(folder + "/discrepancy_note_pdf.xsl");
        List<String> outputs = new ArrayList<>();
        outputs.add("output.xml");
        testSameOutput(XSLPath, outputs);
    }

    /**
     * The sas_import_scripts.xsl output should match expected.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testSASImportScripts()
            throws IOException {
        String folder = "xslt/sas_import_scripts";
        Path XSLPath = Paths.get(folder + "/sas_import_scripts.xsl");
        List<String> outputs = new ArrayList<>();
        outputs.add("data.xml");
        outputs.add("map.xml");
        outputs.add("load.sas");
        testSameOutput(XSLPath, outputs);
    }
}
