package com.tableview.manager.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.FontAwesome.Glyph;



@Target({ ElementType.FIELD })
@Retention(RUNTIME)
public @interface AddGlyphIcon {
	public FontAwesome.Glyph iconName() default Glyph.TENCENT_WEIBO; 
	public boolean beforeText() default false;
}
