package upc.c505.utils.validationUtils;

import upc.c505.utils.validationUtils.validationImp.IdentityNumberValidationImp;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 身份证号校验注解
 * @Author: mjh
 * @CreateTime: 2024-09-14
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // 限定该注解使用在字段和参数上
@Retention(RetentionPolicy.RUNTIME) // 运行时注解，可以通过反射访问
@Constraint(validatedBy = IdentityNumberValidationImp.class) // 绑定验证逻辑类
public @interface IdentityNumberValidation
{
    String message() default "身份证号码无效";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
