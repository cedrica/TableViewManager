package com.tableview.manager.helper;


public class UserData {
	private String foreGround;
	private String backGround;
	private  double columnSize;

	public UserData(double columnSize, int[] bgColorRGB, int[] fgColorRGB, boolean isBold, boolean isItalic, String fontFamily, int fontSize) {
		this.columnSize = columnSize;
		backGround = ""; foreGround = "";
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
	}

	
	
	public double getColumnSize() {
		return columnSize;
	}


	
	public void setColumnSize(double columnSize) {
		this.columnSize = columnSize;
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
