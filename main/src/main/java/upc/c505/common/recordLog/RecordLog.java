package upc.c505.common.recordLog;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RecordLog {

    String operateMode() default "";
    String operateDesc() default "";
    //目标参数，这里填写的是参数名字
    String targetParam() default "";
    //目标属性。这里填写的是目标参数中的属性
    String targetAttribute() default "";
}
