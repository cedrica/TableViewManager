package com.tableview.manager;


import org.controlsfx.glyphfont.FontAwesome.Glyph;

import com.tableview.manager.annotation.AddGlyphIcon;
import com.tableview.manager.annotation.Column;
import com.tableview.manager.annotation.Condition;

public class TestObject {

	@Column(fgColor={255,255,255}, bgColor={222,111,111})
	private String homePage;
	
	@Column(fgColor={255,255,255}, bgColor={222,111,111}, parent="Jesus")
	private String cedric;
	
	@Column(bgForGivenConditions={
					@Condition(ifFieldValueIsEqualTo = "Christelle0", thenSetBackgroundColorTo="blue"),
					@Condition(ifFieldValueIsEqualTo = "Christelle3",thenSetBackgroundColorTo="red")}
	, parent="Jesus", addGlyphIcon=@AddGlyphIcon(iconName=Glyph.INSTITUTION, beforeText=true))
	private String christelle;
	
	private int alt;
	
	@Column(fgForGivenConditions={
					@Condition(ifFieldValueIsEqualTo = "name0",thenSetBackgroundColorTo="blue"),
					@Condition(ifFieldValueIsEqualTo = "name3",thenSetBackgroundColorTo="red")
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
