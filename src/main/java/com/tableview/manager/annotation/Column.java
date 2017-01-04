package com.tableview.manager.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.FontAwesome.Glyph;


@Target({ ElementType.FIELD })
@Retention(RUNTIME)
public @interface Column {
	
	public String customname() default "";

	public int[] bgColor() default {255, 255, 255}; //WHITE

	public int[] fgColor() default {0, 0, 0}; //BLACK

	public int fontSize() default 15;

	public String fontFamily() default "Calibri";
	
	public String parent() default "";

	public boolean isBold() default false;

	public boolean isItalic() default false;
	
	public Formatter formatter() default @Formatter();
	
	public AddGlyphIcon addGlyphIcon() default @AddGlyphIcon(iconName=Glyph.TENCENT_WEIBO, beforeText=false);
	/**
	 * has been replace by the annotation addGlyphIcon
	 * @return
	 */
	@Deprecated
	public FontAwesome.Glyph glyphIcon() default Glyph.TENCENT_WEIBO; 
	public int columnSize() default 100;
	/**
	 * this method has been replaced by bgForGivenConditions and fgForGivenConditions of type Condition
	 * 
	 */
	@Deprecated
	public SpecialCase[] formatMatchers() default {@SpecialCase(value={"",""})};
	public Condition[] bgForGivenConditions() default {@Condition()};
	public Condition[] fgForGivenConditions() default {@Condition()};
}
