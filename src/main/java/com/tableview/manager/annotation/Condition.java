package com.tableview.manager.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * To set background color depending on the declared condition
 * @author ca.leumaleu
 *
 */

@Target({ ElementType.FIELD })
@Retention(RUNTIME)
public @interface Condition {
	public String ifFieldValue() default "";
	/**
	 * Color can be entered either as name like for example: thenSetBackgroundColorTo="blue", 
	 * as value code like for example: thenSetBackgroundColorTo="#0EFF"
	 * or as RGB-function as follow: thenSetBackgroundColorTo="rgb(255, 190, 0)"  
	 * @return
	 */
	public String thenBackgroundColor() default "";
	public String thenForegroundColor() default "";
}
