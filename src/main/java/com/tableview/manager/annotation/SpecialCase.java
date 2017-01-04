package com.tableview.manager.annotation;
/**@Deprecated and replaced by Condition
 * @author ca.leumaleu
 */
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Deprecated
@Retention(CLASS)
@Target({FIELD})
public @interface SpecialCase {
	public String[] value() default {"", ""};
}
