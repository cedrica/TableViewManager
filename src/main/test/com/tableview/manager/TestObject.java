package com.tableview.manager;


import org.controlsfx.glyphfont.FontAwesome.Glyph;

import com.tableview.manager.annotation.AddGlyphIcon;
import com.tableview.manager.annotation.Column;
import com.tableview.manager.annotation.Condition;
import com.tableview.manager.annotation.Formatter;
import com.tableview.manager.enums.FormatterTyp;

public class TestObject {

	@Column(fgColor={255,255,255}, bgColor={222,111,111})
	private String homePage;
	
	@Column(fgColor={255,255,255}, bgColor={222,111,111}, parent="Jesus")
	private String cedric;
	
	@Column(bgForGivenConditions={
					@Condition(ifFieldValue = "Christelle0", thenBackgroundColor="blue"),
					@Condition(ifFieldValue = "Christelle3",thenBackgroundColor="red")}
	, parent="Jesus", addGlyphIcon=@AddGlyphIcon(iconName=Glyph.INSTITUTION, beforeText=true))
	private String christelle;
	
	@Column(fgColor={255,255,255}, bgColor={222,111,111}, formatter=@Formatter(formatterTyp=FormatterTyp.CURRENCY_STRING_CONVERTER))
	private int alt;
	
	@Column(fgForGivenConditions={
					@Condition(ifFieldValue = "name0",thenBackgroundColor="blue"),
					@Condition(ifFieldValue = "name3",thenBackgroundColor="red")
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
