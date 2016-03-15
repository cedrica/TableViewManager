package com.tableview.manager;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import panix.panic.persistence.annotations.Id;

public class Task {
	@Id
	private SimpleLongProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private ObjectProperty<Date> start_date;
	private ObjectProperty<Date> end_date;
	private SimpleLongProperty userID;
	private SimpleLongProperty projectID;
	private SimpleIntegerProperty projectActiv;

	public Task(long id, String name, String desc, Date std, Date eDate, long uID, long pID, int pa) {
		this.id = new SimpleLongProperty(id);
		this.name = new SimpleStringProperty(name);
		description = new SimpleStringProperty(desc);
		userID = new SimpleLongProperty(uID);
		projectID = new  SimpleLongProperty(pID);
		projectActiv = new SimpleIntegerProperty(pa);
		start_date = new SimpleObjectProperty<>(std);
		end_date = new SimpleObjectProperty<>(eDate);
	}

	public Task() {
		this.id = new SimpleLongProperty();
		this.name = new SimpleStringProperty();
		description = new SimpleStringProperty();
		userID = new SimpleLongProperty();
		projectID = new  SimpleLongProperty();
		projectActiv = new SimpleIntegerProperty();
		start_date = new SimpleObjectProperty<>();
		end_date = new SimpleObjectProperty<>();
	}

	public long getId() {
		return id.get();
	}
	public void setId(long id) {
		this.id.set(id);
	}
	public void setId(SimpleLongProperty id) {
		this.id = id;
	}

	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}

	public String getDescription() {
		return description.get();
	}
	public void setDescription(String description) {
		this.description.set(description);
	}

	public Date getStart_date() {
		return start_date.get();
	}

	public void setStart_date(Date start_date) {
		this.start_date.set(start_date);
	}

	public Date getEnd_date() {
		return end_date.get();
	}

	public void setEnd_date(Date end_date) {
		this.end_date.set(end_date);
	}

	public long getUserID() {
		return userID.get();
	}
	public void setUserID(long userID) {
		this.userID.set(userID);
	}

	public long getProjectID() {
		return projectID.get();
	}
	public void setProjectID(long projectID) {
		this.projectID.set(projectID);
	}


	public int getProjectActiv() {
		return projectActiv.get();
	}

	public void setProjectActiv(int projectActiv) {
		this.projectActiv.set(projectActiv);
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", description=" + description + ", start_date=" + start_date + ", end_date=" + end_date
						+ ", userID=" + userID + ", projectID=" + projectID + ", projectActiv=" + projectActiv + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((end_date == null) ? 0 : end_date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((projectActiv == null) ? 0 : projectActiv.hashCode());
		result = prime * result + ((projectID == null) ? 0 : projectID.hashCode());
		result = prime * result + ((start_date == null) ? 0 : start_date.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (end_date == null) {
			if (other.end_date != null)
				return false;
		} else if (!end_date.equals(other.end_date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (projectActiv == null) {
			if (other.projectActiv != null)
				return false;
		} else if (!projectActiv.equals(other.projectActiv))
			return false;
		if (projectID == null) {
			if (other.projectID != null)
				return false;
		} else if (!projectID.equals(other.projectID))
			return false;
		if (start_date == null) {
			if (other.start_date != null)
				return false;
		} else if (!start_date.equals(other.start_date))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}

}

