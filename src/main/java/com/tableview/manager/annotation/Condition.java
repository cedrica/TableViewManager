package com.tableview.manager.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;



@Target({ ElementType.FIELD })
@Retention(RUNTIME)
public @interface Condition {
	public String ifFieldValueIsEqualTo() default "";
	/**
	 * enter the color name. Color name muss be enter either as name as value code (like for example: #0EFF) or as RGB-function (as follow: rgb(255, 190, 0))  
	 * @return
	 */
	public String thenSetBackgroundColorTo() default "";
}
