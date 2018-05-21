package com.yyhh.overall.injector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by yh on 2017/11/8.
 */

//单例
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFrament {
}
