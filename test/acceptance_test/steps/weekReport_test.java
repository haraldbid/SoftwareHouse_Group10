package acceptance_test.steps;

import static org.junit.Assert.assertTrue;

import app.Activity;
import app.Project;
import app.SoftwareHouse;
import app.WeekReport;
import app.Worker;
import designPatterns.Date;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
*@author Martin
*/

public class weekReport_test {

	private SoftwareHouse softwareHouse;
	private Activity activity;
	private Project project;
	private Worker worker;
	
	@When("a Activity is created and time is logged")
	public void aActivityIsCreatedAndTimeIsLogged() throws Exception {
		SoftwareHouse.deleteSoftwareHouse();
		softwareHouse = SoftwareHouse.getInstance();
		softwareHouse.createWorker("aa");
		worker = softwareHouse.getWorkerByIndex("aa");
		softwareHouse.createProject(new Date(2020,10), new Date(2020,14));
		project = softwareHouse.getListOfProjects().get(0);
		project.appointProjectLeader(softwareHouse.getWorkerByIndex("aa"));
		softwareHouse.logIn("aa");
		project.createActivity("Activity", new Date(2020,10), new Date(2020,14));
		
		try {
			activity = softwareHouse.getListOfProjects().get(0).getActivity("Activity");
		} catch (Exception e) {
			e.printStackTrace();
		}
		activity.setExpectedWorkingHours(10);
		activity.assignWorker(worker);
		activity.inputWorkTime(worker, 8, 30, new Date(2020,12));
	}
	@Given("a weekreport is not created for week {int}")
	public void aWeekreportIsNotCreatedForWeek(Integer int1) {
		assertTrue(activity.getWeekReport(new Date(2020,int1)) == null);

	}
	private WeekReport weekreport; 
	@Given("a weekreport for week {int} is requested")
	public void aWeekreportForWeekIsRequested(Integer int1) {
	    weekreport = activity.generateWeekReport(new Date(2020,int1));
	    assertTrue(activity.getWeekReport(new Date(2020,int1)) != null);

	}

	@Given("a weekreport is created for week {int}")
	public void aWeekreportIsCreatedForWeek(Integer int1) {
	    assertTrue(activity.getRecentWeekReport() == weekreport);

	}

	@Then("the existing week report is returned")
	public void theExistingWeekReportIsReturned() {
	    WeekReport newWeek = activity.generateWeekReport(new Date(2020,12));
	    assertTrue(newWeek == weekreport);

	}
	
}
