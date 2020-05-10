package acceptance_test.steps;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.Project;
import app.SoftwareHouse;
import app.Worker;
import designPatterns.Date;

public class createActivity_whitebox {
	private SoftwareHouse softwareHouse;
	private Project project;
	private Worker worker;
	
	public createActivity_whitebox() {
		softwareHouse = new SoftwareHouse();
		softwareHouse.createWorker("aa");
		worker = softwareHouse.getWorkerByIndex("aa");
		
		softwareHouse.createProject(new Date(2020,10),new Date(2020,20));
		project = softwareHouse.getListOfProjects().get(0);
		project.appointProjectLeader(worker);
		
		
		

		
		
	}
	@Test
	public void testInputA() {
		softwareHouse.logIn("aa");
		String eMeg = null;
		try {
			project.createActivity("testAC", new Date(2020,8), new Date(2020,14));		
		} catch (Exception e) {
			eMeg = e.getMessage(); 
		}
		assertTrue(eMeg.equals("Activity period is incongruent with project period"));
	}
	@Test
	public void testInputB() {
		softwareHouse.logIn("aa");
		String eMeg = null;
		try {
			project.createActivity("testAC", new Date(2020,10), new Date(2020,22));		
		} catch (Exception e) {
			eMeg = e.getMessage(); 
		}
		assertTrue(eMeg.equals("Activity period is incongruent with project period"));
	}
	@Test
	public void testInputC() {
		String eMeg = null;
		try {
			project.createActivity("testAC", new Date(2020,10), new Date(2020,14));		
		} catch (Exception e) {
			eMeg = e.getMessage(); 
			System.out.print(e.getMessage());
		}
		assertTrue(eMeg.equals("Only the Project Leader may add an activity."));
	}
	@Test
	public void testInputD() {
		softwareHouse.logIn("aa");
		String eMeg = null;
		try {
			project.createActivity("testAC", new Date(2020,10), new Date(2020,14));		
		} catch (Exception e) {
			eMeg = e.getMessage(); 
		}
		assertTrue(project.getActivities().size() > 0);
	}
}