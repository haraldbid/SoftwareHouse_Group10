package app;
//Authors: Markus, Harald, Martin, Nicklas
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import designPatterns.Date;
import designPatterns.Observable;
import designPatterns.Observer;
import designPatterns.Reporting;

public class Project implements Observer, Reporting {

	private Worker workerLoggedIn;
	private String projectTitle;
	private String projectNumberID;
	private Worker projectLeader;
	private ArrayList<Activity> listOfActivities = new ArrayList<Activity>();
	private Date startDate = new Date();
	private Date endDate = new Date();
	private Observable softwareHouse;
	private List<WeekReport> weekReports = new ArrayList<WeekReport>();

	public Project(Date startDate, Date endDate, String projectNumberID) {
		this.softwareHouse = SoftwareHouse.getInstance();
		this.softwareHouse.register(this);
		this.startDate = startDate;
		this.endDate = endDate;
		this.projectNumberID = projectNumberID;
	}

//	Author: Markus
	public void modifyProjectNumberID() {

		String yearStr = Integer.toString(startDate.getYear()).substring(2,4);
		String runningCount = projectNumberID.substring(2, 6);

		this.projectNumberID = yearStr + runningCount;
	}

//	Author: Martin
	public void appointProjectLeader(Worker appointedProjectLeader) {

		if (!this.hasProjectLeader())
			this.projectLeader = appointedProjectLeader;
		else
			throw new IllegalArgumentException("Project has leader assigned");
	}

//Author: Markus
	public void createActivity(String title, Date startDate, Date endDate) {

		assert title.length() > 0 : "Precondition for createActivity()";
		if(this.startDate.after(startDate) || this.endDate.before(endDate)) {
			throw new IllegalArgumentException("Activity period is incongruent with project period");
		}
		
		for (int i = 0; i < listOfActivities.size(); i++) {
			if (listOfActivities.get(i).getTitle().contentEquals(title)) {
				throw new IllegalArgumentException("Title is already taken.");
			}
		}

		if (isProjectLeaderLoggedIn()) {

			Activity activity = new Activity(title, startDate, endDate, this);

			listOfActivities.add(activity);

		} else {
			throw new IllegalArgumentException("Only project leader may add an activity.");

		}
		assert getActivities().size() > 0 : "Postcondition for createActivity()";
	}

	public boolean isProjectLeaderLoggedIn() {
		if (workerLoggedIn == projectLeader) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void update(Worker loggedIn) {
		this.workerLoggedIn = loggedIn;
	}

//Author: Nicklas
	public WeekReport generateWeekReport(Date date) {

		boolean weekReportExists = false;

		for (WeekReport r : weekReports) {
			if (r.getDate().equals(date)) {
				weekReportExists = true;
				return r; 
			}
		}
	
		if (!weekReportExists) {
			WeekReport report = new WeekReport(this, date);

			weekReports.add(report);
			return report;
		}
		return null;
	}

//	Author: Nicklas
	@Override
	public int[] numMinSpent(Date date) {
		/*
		 * 1. Entry of NumMinSpent is the total hours spent on project 2. Entry of
		 * NumMinSpent is the hours spent at the specified week
		 */
		int[] numMinSpent = { 0, 0 };

		/*
		 * For each activity, a report is generated for the given date. Then the total
		 * number of hours spent on each activity is added to the array.
		 */
		for (Activity a : listOfActivities) {
			a.generateWeekReport(date);
			
			if(a.getWeekReport(date) != null) {
				numMinSpent[0] += a.getWeekReport(date).numMinSpent[0];
				numMinSpent[1] += a.getWeekReport(date).numMinSpent[1];
			}
		}
		return numMinSpent;
	}

//	Author Nicklas
	public void printWeekReport(Date date) throws IllegalArgumentException {
		if(date.after(this.endDate))
			throw new IllegalArgumentException("Date incongruent with activity date");

		generateWeekReport(date);
		for (WeekReport r : weekReports) {
			if (r.getDate().equals(date))
				r.printWeekReport();
		}
	}
//Author: Harald
	@Override
	public int getExpectedWorkingHours() {
		int expectedWorkingHours = 0;

		for (Activity a : listOfActivities) {
			expectedWorkingHours += a.getExpectedWorkingHours();
		}
		return expectedWorkingHours;
	}

	public ArrayList<Activity> getActivities() {
		return this.listOfActivities;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		modifyProjectNumberID();
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(int year, int week) {
		startDate.setDate(year, week);
		modifyProjectNumberID();
	}

	public void setEndDate(int year, int week) {
		endDate.setDate(year, week);
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public boolean hasProjectTitle() {

		if (projectTitle == null) {
			return false;
		} else {
			return true;
		}
	}

	public String getTitle() {

		return this.projectTitle;
	}

	public String getID() {
		return projectNumberID;
	}

	public boolean hasProjectLeader() {
		if (projectLeader == null) {
			return false;
		} else {
			return true;
		}
	}

//	Author: Harald
	public Activity getActivity(String title) throws Exception {
		for(Activity a : listOfActivities) {
			if (a.getTitle().equals(title)) {
				return a;
			}
		}

		throw new Exception("Activity not found");
	}

	public Worker getProjectLeader() {
		if (hasProjectLeader()) {
			return projectLeader;
		} else {
			throw new IllegalArgumentException("Project has no leader yet.");
		}
	}

}
