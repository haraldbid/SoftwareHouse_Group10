package app;

import java.util.ArrayList;

import designPatterns.Date;

import designPatterns.Date;

public class Worker {

	private String workerID;
	private SoftwareHouse softwareHouse;
	private ArrayList<Activity> listOfActivities = new ArrayList<Activity>();

	public Worker(SoftwareHouse softwareHouse, String ID) {

		assert softwareHouse != null : "Precondition for Worker()";
		this.softwareHouse = softwareHouse;

		// ID length has to be 1, 2, 3 or 4
		if (ID.length() == 0 || ID.length() > 4) { // 1 || 2
			throw new IllegalArgumentException("ID is at minimum 1 and maximum 4 letters long.");
		}

		// ID must be composed solely of letters
		for (int i = 0; i < ID.length(); i++) { // 3
			if (!Character.isLetter(ID.charAt(i))) { // 4
				throw new IllegalArgumentException("ID must be composed of letters.");
			}
		}

		// ID must be available
		for (int i = 0; i < this.softwareHouse.getNbWorkers(); i++) { // 5
			if (this.softwareHouse.getWorker(i).getID().equals(ID)) { // 6

				throw new IllegalArgumentException("ID is already used by another worker.");
			}
		}

		this.workerID = ID;

		assert this.workerID.length() <= 4 : "Postcondition for Worker()";
		assert this.workerID.length() > 0 : "Postcondition for Worker()";
		for (Worker wo : softwareHouse.getListOfWorkers()) {
			assert !wo.workerID.equals(ID) : "Postcondition for Worker()";
		}

	}

	public String getID() {
		return this.workerID;
	}
	
	
	public int getNumActivities(Date startDate, Date endDate) {

		int count = 0;
		for (int i = 0; i < listOfActivities.size(); i++) {
			if (((listOfActivities.get(i).getStartDate().after(startDate)
					|| listOfActivities.get(i).getStartDate().equals(startDate))
					&& (listOfActivities.get(i).getStartDate().before(endDate)
							|| listOfActivities.get(i).getStartDate().equals(endDate)))
					|| ((listOfActivities.get(i).getEndDate().after(startDate)
							|| listOfActivities.get(i).getEndDate().equals(startDate))
							&& (listOfActivities.get(i).getEndDate().before(endDate)
									|| listOfActivities.get(i).getEndDate().equals(endDate)))) {
				count++;
			} else if (listOfActivities.get(i).getStartDate().before(startDate)
					&& listOfActivities.get(i).getEndDate().after(endDate)) {
				count++;
			}
		}
		return count;
	}

	// sick leave, holiday
	public void setUnavailable(Date startDate, Date endDate) {

		for (int i = 0; i < listOfActivities.size(); i++) {

			if ((listOfActivities.get(i).getStartDate().equals(startDate)
					|| listOfActivities.get(i).getStartDate().after(startDate))
					&& (listOfActivities.get(i).getEndDate().equals(endDate)
							|| listOfActivities.get(i).getEndDate().before(endDate))) {
				listOfActivities.get(i).deleteWorker(this);
				listOfActivities.remove(i);
			}
			else if ((listOfActivities.get(i).getStartDate().equals(startDate)
					|| listOfActivities.get(i).getStartDate().after(startDate))
					&& (listOfActivities.get(i).getEndDate().equals(endDate)
							|| listOfActivities.get(i).getEndDate().before(endDate))) {
				listOfActivities.get(i).deleteWorker(this);
				listOfActivities.remove(i);
			}

		}
	}

	public void addActivity(Activity activity) {
		listOfActivities.add(activity);
	}

	public ArrayList<Activity> getListActivitys() {
		return this.listOfActivities;
	}

	public void printWorkerWeek(Date date) {
		System.out.println("---- Time worked for " + getID() + ", week : " + date.getWeekNumber() + "    year : " + date.getYear() + " ---- " );
		for (Activity activity : getListActivitys()) {
			System.out.println("Activity: " + activity.getTitle());
			int sum_h = 0;
			int sum_m = 0;
			for (TimeSheet time : activity.getTimeSheets()) {
				if (time.getWorker() == this && time.getDate().equals(date)) {
					sum_h += time.getHoursWorked();
					sum_m += time.getMinutesInputed();
					
					if (sum_m == 60) {
						sum_m = 0;
						sum_h += 1;
					}
				}
			}
			System.out.println(sum_h + " Hours, " + sum_m + " minutes worked");
			System.out.println("--------------------");
		}
	}
}
