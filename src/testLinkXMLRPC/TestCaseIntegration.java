package testLinkXMLRPC;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Uma Mahesh
 * Date: 20/8/13
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */

public class TestCaseIntegration
{

  // Connect to TestLinkXMLRPC TestCase result update Client
  public TestLinkAPIClient getTestLinkAPIClient(String ProjectAPIKey, String XmlRpcUrl)
  {
      TestLinkAPIClient testLinkAPIClient=null;
      try
      {
          /**
           *  The below reportTestCaseResult method requires the following variables
           *  Project User API Key : This requires test case assigned user API Key for a particular project
           *  URL: The TestLink XML RPC URL
           */

          testLinkAPIClient = new TestLinkAPIClient(ProjectAPIKey, XmlRpcUrl);
      }
      catch (Exception ex)
      {
          System.out.println("Got exception while connecting to TestLik XML RPC Server: "+ ex.getMessage());
      }

      return testLinkAPIClient;
  }

  // Connect to TestLinkXMLRPC Main API Client
  public TestLinkAPI getAPI(String xmlRPCUrl, String APIUserKey)
  {
        TestLinkAPI API=null;
      try
        {
            URL url = new URL(xmlRPCUrl);
            API = new TestLinkAPI(url, APIUserKey);
        }
      catch (MalformedURLException ex)
        {
              System.out.println("Got Exception while forming URL "+ ex.getMessage());
        }
        catch (Exception ex)
        {
            System.out.println("Got Exception while connecting to TestLink RPC Server"+ ex.getMessage());
        }

        return API;
    }

  // Get all project details from TestLink with Main API Client
  public TestProject[] getProjects(TestLinkAPI API)
  {
      TestProject[] projects=null;
        try
        {
            projects=API.getProjects();
        }
        catch (Exception ex)
        {
            System.out.println("Got Exception while getting test projects from test link:"+ ex.getMessage());
        }

        return projects;
    }

  // Get all project Test Plan details from TestLink with Main API Client
  public TestPlan[] getTestPlans(TestLinkAPI API, int ProjectId)
  {
        return API.getProjectTestPlans(ProjectId);
    }

  // Get all project Build details from TestLink with Main API Client
  public Build[] getBuildIds(TestLinkAPI API, int ProjectId)
  {
        return API.getBuildsForTestPlan(ProjectId);
    }

  // Get all project suite details from TestLink with Main API Client
  public TestSuite[] getTestSuites(TestLinkAPI API, int TestPlanId)
  {
      return API.getTestSuitesForTestPlan(TestPlanId);
  }

  // Get all test case list details from TestLink with Main API Client
  /*
  Details:
     TestCaseDetails.Simple
     TestCaseDetails.Summary
     TestCaseDetails.Full
  deep:
    true or false

   */
  public TestCase[] getTestCases(TestLinkAPI API, int TestSuiteId, boolean deep,TestCaseDetails Details )
  {
      return API.getTestCasesForTestSuite(TestSuiteId,deep,Details);
  }

  // Get all testlink project,testplan,testsuite and tescases details to a text file from TestLink with Main API Client
  public void getTestCaseDetailsToTxtFile(String FileName, TestLinkAPI API, int projectId, int testPlanId, int testSuiteId)
  {
      PrintWriter out=new PrintWriter(System.out);
       try
       {
           out = new PrintWriter(new FileWriter(FileName, true));
           out.print("#TestLink Projects");
           out.println();
           out.print("-------------------------------------------------------------------------------------");
           out.println();

           for (TestProject projectname : API.getProjects())
           {
               out.print(String.format("Project Id :%d Project Name:%s", projectname.getId(), projectname.getName())) ;
               out.println();
           }

           out.print("#TestLink Test Plans:" );
           out.println();
           out.print("-------------------------------------------------------------------------------------");
           out.println();
           for (TestPlan testPlan : API.getProjectTestPlans(projectId)) {

               out.print(String.format("TestPlan Id :%d TestPlan Name:%s", testPlan.getId(), testPlan.getName())) ;
               out.println();
           }
           ;
           out.print("#TestLink Test Suites");
           out.println();
           out.print("-------------------------------------------------------------------------------------");
           out.println();

           for (TestSuite testSuite : API.getTestSuitesForTestPlan(testPlanId)) {
               out.print("-------------------------------------------------------------------------------------");
               out.println();
               out.print("TestSuite Name:"+testSuite.getName() + "      "+"TestSutieId:" +testSuite.getId());
               out.println();
               for (TestCase testCase : API.getTestCasesForTestSuite(testSuite.getId(), true, TestCaseDetails.SIMPLE))
               {

                   out.print(String.format("TestCase Id :%d TestCase Name:%s", testCase.getId(), testCase.getName())) ;
                   out.println();

               }
           }
       }
       catch(Exception ex)
       {
           System.out.println("Got exception while getting TestLink Projects Data" + ex.getMessage());
       }
       finally {
           out.flush();
       }

   }

  // Get all test case id by giving test case name TestLink with Main API Client
  public int getTestCaseIdByName(TestLinkAPI API, String testCaseName, String testSuiteName, String testProjectName, String testCasePathName  )
  {
      Integer testCaseIDByName = API.getTestCaseIDByName(testCaseName,testSuiteName,testProjectName,testCasePathName);

      return testCaseIDByName;
  }

  // Get project Id by giving project Name TestLink with Main API Client
  public int getProjectId(TestLinkAPI API, String testProjectName)
  {
    TestProject project=null;

    for (TestProject testproject : getProjects(API))
    {
        if (testproject.getName().equals(testProjectName))
        {
            project=testproject;
            break;
        }
    } ;
    return project.getId();
}

  // Get project Test Plan Id Id by giving project Name TestLink with Main API Client
  public int getTestPlanId(TestLinkAPI API, String testProjectName, String testPlanName)
  {
        TestPlan testPlan=null;
        int testProjectId = getProjectId(API,testProjectName);

        for (TestPlan plan : getTestPlans(API,testProjectId))
        {
            if (plan.getName().equals(testPlanName))
            {
                testPlan=plan;
                break;
            }
        } ;
        return testPlan.getId();
    }

   // Get project Test Suite Id Id by giving project Name TestLink with Main API Client
  public int getTestSuiteId(TestLinkAPI API, String testProjectName, String testPlanName, String testSuiteName)
  {
       int testPlanId = getTestPlanId(API,testProjectName,testPlanName);
       TestSuite suite=null;

        for (TestSuite testSuite : getTestSuites(API,testPlanId))
        {
            if (testSuite.getName().equals(testSuiteName))
            {
                suite=testSuite;
                break;
            }
        } ;
        return suite.getId();
    }

  //Create Build in the TestLink using Main API
  public void createBuild(TestLinkAPI API, int testPlanId, String buildName, String buildNotes)
  {

        Build build = API.createBuild(testPlanId, buildName, buildNotes);
        System.out.println("Created Build :\t "+ build.getId()+ "Name Of the Build : " + build.getName());

    }

  //Report test case execution details to TestLink Server with TestLinkXMLRPC Client
   /*
         testResultStatus
   */
  public void reportTCResult(TestLinkAPIClient testLinkAPIClient,int testPlanId, int testCaseId, int buildId, String execNotes, String testResultStatus ) throws TestLinkAPIException
  {
        try {
            testLinkAPIClient.reportTestCaseResult(testPlanId,testCaseId,buildId,execNotes,testResultStatus);
        } catch (TestLinkAPIException ex) {
            System.out.println("Got exception while updating test case results for a test plan:\t"+testPlanId+ ex.getMessage());
        }
    }

}




