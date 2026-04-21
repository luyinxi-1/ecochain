package upc.c505.utils.validationUtils.validationImp;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import upc.c505.utils.validationUtils.IdentityNumberValidation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * @Description: 身份证号校验器实现类
 * @Author: mjh
 * @CreateTime: 2024-09-14
 */
public class IdentityNumberValidationImp implements ConstraintValidator<IdentityNumberValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!ObjectUtils.isNotEmpty(value)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("身份证号码不能为空").addConstraintViolation();
            return false;
        }
        // 验证逻辑：这里以身份证号码的验证为例
        final String format = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        if(!value.matches(format)) return false;
        try{
            int year = Integer.parseInt(value.substring(6, 10));
            int month = Integer.parseInt(value.substring(10, 12));
            int day = Integer.parseInt(value.substring(12, 14));
            LocalDate date = LocalDate.of(year, month, day);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
