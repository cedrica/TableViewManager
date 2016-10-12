package com.tableview.manager.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


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
	
	public int columnSize() default 100;
	@Deprecated
	public SpecialCase[] formatMatchers() default {@SpecialCase(value={"",""})};
	public SpecialCase[] bgForGivenConditions() default {@SpecialCase(value={"",""})};
	public SpecialCase[] fgForGivenConditions() default {@SpecialCase(value={"",""})};
}
