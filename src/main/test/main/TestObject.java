package main;

import com.tableview.manager.annotation.Column;
import com.tableview.manager.annotation.SpecialCase;

public class TestObject {

	@Column(fgForGivenConditions={
					@SpecialCase(value = {"homePage0","-fx-text-fill:blue;"}),
					@SpecialCase(value = {"homePage3","-fx-text-fill:red;"})
	})
	private String homePage;

	private int alt;
	@Column(bgForGivenConditions={
					@SpecialCase(value = {"name0","-fx-background-color:blue;"}),
					@SpecialCase(value = {"name3","-fx-background-color:red;"})
	})
	private String name;
	
	public TestObject (String name, int alt, String homePage){
		this.name = name;
		this.alt = alt;
		this.homePage = homePage;
	}
	
	public String getHomePage() {
		return homePage;
	}
	
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	
	public int getAlt() {
		return alt;
	}
	
	public void setAlt(int alt) {
		this.alt = alt;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
