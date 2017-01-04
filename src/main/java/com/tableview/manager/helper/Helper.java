package com.tableview.manager.helper;

import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.controlsfx.glyphfont.FontAwesome.Glyph;

import com.tableview.manager.annotation.AddGlyphIcon;
import com.tableview.manager.annotation.Condition;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Helper  {
	public static String generateBgFont(Object item, HBox hb, String bg, UserData userData, Condition[] bgForGivenConditions) {
		if ((bgForGivenConditions != null) && (!bgForGivenConditions[0].ifFieldValue().trim().equals(""))) {
			for (Condition matcher2 : bgForGivenConditions) {
				if (matcher2.ifFieldValue().equals(item.toString())) {
					bg += "-fx-background-color:" + matcher2.thenBackgroundColor() + ";";
					break;
				} else {
					hb.setStyle(null);// important!!
				}
			}
		} else {
			bg = userData.getBackGround();
		}
		return bg;
	}
	
	public static String generateFgFont(Object item, HBox hb, String fg, UserData userData, Condition[] fgForGivenConditions) {
		if ((fgForGivenConditions != null) && (!fgForGivenConditions[0].ifFieldValue().trim().equals(""))) {
			for (Condition matcher : fgForGivenConditions) {
				if (matcher.ifFieldValue().equals(item.toString())) {
					fg += "-fx-text-fill:" + matcher.thenBackgroundColor() + ";";
					break;
				} else {
//					label.setStyle(null);
					hb.setStyle(null);// important!!
				}
			}
		} else {
			fg += userData.getForeGround();
		}
		return fg;
	}
	
	public static HBox assignGlyphIcon(HBox hb, Label label, AddGlyphIcon addGlyphIcon) {
		Label icon = new Label();
		if (addGlyphIcon != null) {
			if (addGlyphIcon.iconName().equals(Glyph.TENCENT_WEIBO) && addGlyphIcon.showDefaultIcon()) {
				icon.setGraphic(GlyphFontRegistry.font("FontAwesome").create(addGlyphIcon.iconName()));
				if (addGlyphIcon.beforeText()) {
					hb.getChildren().add(icon);
					hb.getChildren().add(label);
				} else {
					hb.getChildren().add(label);
					hb.getChildren().add(icon);
				}
			} else if (addGlyphIcon.iconName().equals(Glyph.TENCENT_WEIBO) && !addGlyphIcon.showDefaultIcon()) {
				hb.getChildren().add(label);
			} else {
				icon.setGraphic(GlyphFontRegistry.font("FontAwesome").create(addGlyphIcon.iconName()));
				if (addGlyphIcon.beforeText()) {
					hb.getChildren().add(icon);
					hb.getChildren().add(label);
				} else {
					hb.getChildren().add(label);
					hb.getChildren().add(icon);
				}
			}
		}
		return hb;
	}
}
