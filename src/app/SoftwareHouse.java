package app;
//Authors: Harald, Martin, Markus, Nicklas

import java.util.ArrayList;
import java.util.List;

import designPatterns.Date;
import designPatterns.Observable;
import designPatterns.Observer;

public class SoftwareHouse implements Observable {

	private static SoftwareHouse softwareHouse;
	private Worker loggedIn;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private List<Project> listOfProjects = new ArrayList<Project>();
	private ArrayList<Worker> listOfWorkers = new ArrayList<Worker>();
	private boolean exitRequest = false;
	private SoftwareHouse() {
		
	}
//	Author: Nicklas
	public static SoftwareHouse getInstance() {
		if(softwareHouse == null) {
			softwareHouse = new SoftwareHouse();
		}
		return softwareHouse;
	}
//	Author: Harald
	public static void deleteSoftwareHouse() {
		if(softwareHouse != null)
			softwareHouse = null;
	}
//	Author: Martin
	public void createProject(Date startDate, Date endDate) {
		Project project = new Project(startDate, endDate,generateProjectID(startDate));
		listOfProjects.add(project);
	}

//	Author: Markus
	public String generateProjectID(Date startDate) {
		String year = Integer.toString(startDate.getYear()).substring(2, 4);
		String runningCount = "";

		for (int i = 0; i < 4 - Integer.toString(listOfProjects.size()).length(); i++) {
			runningCount += "0";
		}
		runningCount += listOfProjects.size();

		return year + runningCount;
	}

//	Author: Martin
	public void logIn(String ID) {
		if (loggedIn()) {
			logOut();
		}
		
		assert ID.length() > 0 : "Precondition for logIn()";
		assert ID.length() < 5 : "Precondition for logIn()";
		
		for (int i = 0; i < listOfWorkers.size(); i++) {
			if (listOfWorkers.get(i).getID().equals(ID)) {
				loggedIn = listOfWorkers.get(i);
				notifyObserver();
			}
		}

		if (!loggedIn()) {
			throw new IllegalArgumentException("Login failed.");
		}
		assert loggedIn != null : "Postcondition for logIn()";
	}

	public void logOut() {

		if (loggedIn()) {
			loggedIn = null;
			notifyObserver();
		}
	}

//	Author: Markus
	public void getAllWorkersActivities(Date startDate, Date endDate) {

		quickSort(listOfWorkers, 0, listOfWorkers.size() - 1, startDate, endDate);
		
		for (int i = 0; i < listOfWorkers.size(); i++) {
			System.out.println(
					listOfWorkers.get(i).getID() + " will be working on " + listOfWorkers.get(i).getNumActivities(startDate, endDate) + " activities.");
		}
	}

//	Author: Markus
	public void quickSort(ArrayList<Worker> arr, int low, int high, Date startDate, Date endDate) {

		if (arr == null || arr.size() == 0)
			return;

		if (low >= high)
			return;

		int middle = low + (high - low) / 2;
		int pivot = arr.get(middle).getNumActivities(startDate, endDate);

		int i = low, j = high;
		while (i <= j) {
			while (arr.get(i).getNumActivities(startDate, endDate) < pivot) {
				i++;
			}

			while (arr.get(j).getNumActivities(startDate, endDate) > pivot) {
				j--;
			}

			if (i <= j) {
				Worker temp = arr.get(i);
				arr.set(i, arr.get(j));
				arr.set(j, temp);
				i++;
				j--;
			}
		}

		if (low < j)
			quickSort(arr, low, j, startDate, endDate);

		if (high > i)
			quickSort(arr, i, high, startDate, endDate);

	}

	public boolean loggedIn() {
		if (this.loggedIn == null) {
			return false;
		}
		return true;
	}

	public Worker getWorkerLoggedIn() {
		return loggedIn;
	}

	public void createWorker(String ID) {
		Worker worker = new Worker(ID);

		this.listOfWorkers.add(worker);
	}

	public int getNbWorkers() {
		return listOfWorkers.size();
	}

	public Worker getWorker(int index) {

		return listOfWorkers.get(index);
	}

	public Worker getWorker(String ID) throws Exception {

		for (Worker w : listOfWorkers) {
			if (w.getID().equals(ID)) {
				return w;
			}
		}

		throw new Exception("Worker not found");
	}

//	Author: Harald
	public Project getProject(String projectID) throws Exception {

		for (Project p : listOfProjects) {
			if (p.getID().equals(projectID)) {
				return p;
			}
		}

		throw new Exception("Project not found");
	}

	public List<Project> getListOfProjects() {
		return listOfProjects;

	}
	

	public List<Worker> getListOfWorkers() {
		return this.listOfWorkers;
	}

	public void exit() {
		this.exitRequest = true;
	}

	public boolean getExitRequest() {
		return this.exitRequest;
	}
	


	/*Observer pattern:
	 * Author: Nicklas 
	 */
	
	@Override
	public void unregister(Observer o) {
		observers.remove(o);
	}

	@Override
	public void register(Observer o) {
		observers.add(o);
	}

	@Override
	public void notifyObserver() {
		if (!observers.isEmpty()) {
			for (Observer o : observers) {
				o.update(loggedIn);
			}
		}
			}

//	Author: Harald
	public Worker getWorkerByIndex(String ID) {
		for (Worker worker : listOfWorkers) {
			if(worker.getID().equals(ID))
				return worker;
		}
		return null;
	}

//	Author: Martin
	public Project getProjectByID(String ID) {
		for (Project project : listOfProjects) {
			if(project.getID().equals(ID))
				return project;
		}
		return null;
	}
}
