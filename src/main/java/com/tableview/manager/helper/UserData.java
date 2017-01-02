package com.tableview.manager.helper;

import com.tableview.manager.annotation.AddGlyphIcon;
import com.tableview.manager.annotation.Condition;

public class UserData {
	private String foreGround;
	private String backGround;
	private AddGlyphIcon icon;
	private Condition[] bgForGivenConditions;
	private Condition[] fgForGivenConditions;
	private boolean euroNumber;

	
	
	public UserData( int[] bgColorRGB, int[] fgColorRGB, boolean isBold, boolean isItalic, String fontFamily, int fontSize,
					AddGlyphIcon glyphIcon, boolean isEuroNumber, Condition[] bgForGivenConditions, Condition[] fgForGivenConditions) {
		backGround = ""; foreGround = "";
		euroNumber = isEuroNumber;
		if (bgColorRGB != null) {
			if (bgColorRGB[0] != 255 || bgColorRGB[1] != 255 || bgColorRGB[2] != 255) {
				backGround += "-fx-background-color:rgb(" + bgColorRGB[0] + "," + bgColorRGB[1] + "," + bgColorRGB[2] + ");";
			}
		}
		if (fgColorRGB != null) {
			if (fgColorRGB[0] != 255 || fgColorRGB[1] != 255 || fgColorRGB[2] != 255) {
				foreGround += "-fx-text-fill:rgb(" + fgColorRGB[0] + "," + fgColorRGB[1] + "," + fgColorRGB[2] + ");";
			}
		}
		if (isItalic) {
			foreGround += "-fx-font-style:italic;";
		}
		if (isBold) {
			foreGround += "-fx-font-weight:bold;";
		}

			foreGround += "-fx-font-size:" + fontSize + ";";

		if (fontFamily != null && !fontFamily.isEmpty()) {
			foreGround += "-fx-font-family:" + fontFamily + ";";
		}
		
		this.icon = glyphIcon;
		this.fgForGivenConditions = fgForGivenConditions;
		this.bgForGivenConditions = bgForGivenConditions;
	}

	
	
	public boolean isEuroNumber() {
		return euroNumber;
	}


	
	public void setEuroNumber(boolean euroNumber) {
		this.euroNumber = euroNumber;
	}

	public AddGlyphIcon getIcon() {
		return icon;
	}

	public void setIcon(AddGlyphIcon icon) {
		this.icon = icon;
	}

	
	public Condition[] getBgForGivenConditions() {
		return bgForGivenConditions;
	}



	
	public void setBgForGivenConditions(Condition[] bgForGivenConditions) {
		this.bgForGivenConditions = bgForGivenConditions;
	}



	
	public Condition[] getFgForGivenConditions() {
		return fgForGivenConditions;
	}



	
	public void setFgForGivenConditions(Condition[] fgForGivenConditions) {
		this.fgForGivenConditions = fgForGivenConditions;
	}



	public String getForeGround() {
		return foreGround;
	}

	
	public void setForeGround(String foreGround) {
		this.foreGround = foreGround;
	}

	
	public String getBackGround() {
		return backGround;
	}

	
	public void setBackGround(String backGround) {
		this.backGround = backGround;
	}
	
	
}
