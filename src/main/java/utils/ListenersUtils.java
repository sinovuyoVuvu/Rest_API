package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenersUtils implements ITestListener, ISuiteListener, IInvokedMethodListener {

	// Log4J interface
	public static Logger logName = LogManager.getLogger(ListenersUtils.class.getName());

	public void onStart(ITestContext result) {

		System.out.println("<-- NG Listener : onStart Start of Suite --> ");
		System.out.println(".getSuite().getName() = " + result.getSuite().getName());
//		System.out.println(".getClass().getName() = " + result.getClass().getName());
		
		logName.info("<-- NG Listener : onStart Start of Suite --> " + result.getSuite().getName());
		
		
	}

	public  void onTestStart(ITestResult result) {

		 System.out.println("<-- NG Listener : onTestStart -->"+ result.getName());
		System.out.println("<-- NG Listener : onTestStart -->");
//		System.out.println(".getSuite().getName() = " + result.getClass().getName());
		logName.info("<-- NG Listener : onTestStart -->" + result.getName());
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

		System.out.println("<-- NG Listener : onTestFailedButWithinSuccessPercentage -->");
		logName.warn("<-- NG Listener : onTestFailedButWithinSuccessPercentage -->" + result.getName());

	}

	public void onTestFailure(ITestResult result) {

		System.out.println("<-- NG Listener : onTestFailure -->");
		logName.error("<-- NG Listener : onTestFailure -->" + result.getName());

	}

	public void onTestSkipped(ITestResult result) {

		System.out.println("<-- NG Listener : onTestSkipped -->");
		logName.warn("<-- NG Listener : onTestSkipped -->" + result.getName());

	}

	public void onTestSuccess(ITestResult result) {

		System.out.println("<-- NG Listener : onTestSuccess -->");
		logName.info("<-- NG Listener : onTestSuccess -->" + result.getName());

	}

	public void onFinish(ITestContext result) {

		System.out.println("<-- NG Listener : onFinish -->");
		logName.info("<-- NG Listener : onFinish -->" + result.getSuite());

	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ISuite suite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ISuite suite) {
		// TODO Auto-generated method stub
		
	}

}
