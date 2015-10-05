package testLinkXMLRPC;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.model.*;
//import testlink.api.java.client.TestLinkAPIClient;
//import testlink.api.java.client.TestLinkAPIException;
//import testlink.api.java.client.TestLinkAPIResults;
import br.eti.kinoshita.testlinkjavaapi.*;
import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIResults;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Map;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: CipherCloud
 * Date: 21/8/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class execute {


    public static void main(String[] args) throws  MalformedURLException {

        TestCaseIntegration tci = new TestCaseIntegration();

       // URL url = new URL("http://192.168.1.250/testlink/lib/api/xmlrpc.php");
        TestLinkAPI API = tci.getAPI("http://192.168.1.250/testlink/lib/api/xmlrpc.php", "d8e9a8809115cf4c7fb014d164f79b06");
        PrintWriter out=new PrintWriter(System.out);

        try
        {



          TestProject[] projects = tci.getProjects(API);
            for (TestProject project : projects)
            {
                //System.out.println(project);
            }


          int projectId= tci.getProjectId(API,"SFDC");
            System.out.println(projectId);
          int projectPlanId= tci.getTestSuiteId(API,"SFDC","4.2");
            System.out.println(projectPlanId);
          int testSuiteId = tci.getTestSuiteId(API,"SFDC","Accounts");

          TestCase[] testCases =tci.getTestCases(API,testSuiteId,false,TestCaseDetails.SIMPLE);
            for(TestCase tcase: testCases)
            {
                boolean contains = tcase.getName().contains("ACC-02");
                if(contains==true)
                {
                    int id =tcase.getId();
                    System.out.println(id);
                }
            }


            tci.getTestCaseDetailsToTxtFile("test.txt",API,projectId,projectPlanId,testSuiteId);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            out.flush();
        }








    }
}
