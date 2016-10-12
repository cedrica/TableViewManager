package com.tableview.manager.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Retention(CLASS)
@Target({ TYPE, FIELD, PARAMETER })
public @interface SpecialCase {
	public String[] value() default {"", ""};
}
