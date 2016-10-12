package main;

import com.tableview.manager.annotation.Column;
import com.tableview.manager.annotation.SpecialCase;

public class TestObject {

	@Column(fgColor={255,255,255}, bgColor={222,111,111})
	private String homePage;
	@Column(fgColor={255,255,255}, bgColor={222,111,111}, parent="Jesus")
	private String cedric;
	@Column(bgForGivenConditions={
					@SpecialCase(value = {"name0","-fx-background-color:blue;"}),
					@SpecialCase(value = {"name3","-fx-background-color:red;"})}
	, parent="Jesus")
	private String christelle;
	private int alt;
	@Column(bgForGivenConditions={
					@SpecialCase(value = {"name0","-fx-background-color:blue;"}),
					@SpecialCase(value = {"name3","-fx-background-color:red;"})
	})
	private String name;
	
	public TestObject (String name, String cedric, String christelle, int alt, String homePage){
		this.name = name;
		this.cedric = cedric;
		this.christelle = christelle;
		this.alt = alt;
		this.homePage = homePage;
	}
	
	
	
	public String getCedric() {
		return cedric;
	}


	
	public void setCedric(String cedric) {
		this.cedric = cedric;
	}


	
	public String getChristelle() {
		return christelle;
	}


	
	public void setChristelle(String christelle) {
		this.christelle = christelle;
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
