package com.task.common.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)	//注解的保留策略
@Target(ElementType.FIELD)		//定义注解的作用目标
@Documented		//说明该注解将被包含在javadoc中
@Inherited 		//说明子类可以继承父类中的该注解
public @interface FieldMeta {

	boolean isField() default true;
}
