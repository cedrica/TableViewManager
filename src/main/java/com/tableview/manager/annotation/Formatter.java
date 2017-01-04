package com.tableview.manager.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tableview.manager.enums.FormatterTyp;


@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD })
public @interface Formatter {
	public FormatterTyp formatterTyp() default FormatterTyp.CURRENCY_STRING_CONVERTER; 
	public String pattern() default "";
}
