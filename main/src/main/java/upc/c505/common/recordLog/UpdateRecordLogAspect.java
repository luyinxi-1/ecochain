package upc.c505.common.recordLog;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;
import upc.c505.common.UserUtils;
import upc.c505.modular.supenterprise.entity.SupOperateLog;
import upc.c505.modular.supenterprise.mapper.SupOperateLogMapper;
import upc.c505.utils.ApplicationContextUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Aspect
public class UpdateRecordLogAspect {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SupOperateLogMapper supOperateLogMapper;

    //定义一个切入点
    @Pointcut("@annotation( upc.c505.common.recordLog.RecordLog)")
    public void pointCut() {
    }

    //在方法执行后执行 可以打印返回的数据 判断数据是否是自己需要的
    @AfterReturning("pointCut()")
    public void afterReturn(JoinPoint joinPoint) throws Throwable {
        try {
            Vector<SupOperateLog> list = new Vector<>();
            list = setBaseLog(joinPoint, list);
            if (list != null && list.size() > 0) {
                supOperateLogMapper.insertBatch(list);
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置基础的参数
     *
     * @param joinPoint 切入点
     */
    private Vector<SupOperateLog> setBaseLog(JoinPoint joinPoint, Vector<SupOperateLog> list) {

        //获取请求
        ServletRequestAttributes servletRequestAttributes = ApplicationContextUtil.getServletActionContext();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // 获取Session信息 - 如果你需要的话
        // HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        //获取切入点所在目标对象
        Object targetObj = joinPoint.getTarget();
        //System.out.println(targetObj.getClass().getName());
        // 获取切入点方法的名字
        String methodName = joinPoint.getSignature().getName();
        //  获取方法上的注解
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            RecordLog recordLog = method.getAnnotation(RecordLog.class);
            //     System.out.println("切入方法注解的title:" + recordLog.operateMode());

            //1.这里获取到所有的参数值的数组
            Object[] args = joinPoint.getArgs();
            //最关键的一步:通过这获取到方法的所有参数名称的字符串数组
            String[] parameterNames = methodSignature.getParameterNames();
            String aim = "";
            try {
                //通过你需要获取的参数名称的下标获取到对应的值
                int index = ArrayUtils.indexOf(parameterNames, recordLog.targetParam());
                if (index != -1) {
//                    Object obj = FieldUtils.getAllFields(args[index].getClass());
                    List<Object> strsToList2 = new ArrayList<>();
                    //参数是List
                    if (args[index] instanceof List) {
                        Collections.addAll(strsToList2, args[index]);
                        if (strsToList2 != null && strsToList2.size() > 0) {
                            for (Object field : (List<?>) strsToList2.get(0)) {
                                Object targetAttribute = getFieldValueByName(recordLog.targetAttribute(), field);
                                SupOperateLog testRecord = new SupOperateLog();
                                //  int index1 = ArrayUtils.indexOf(declaredFields, recordLog.targetParam());
                                //操作时间
                                testRecord.setOperateDate(LocalDateTime.now());
                                //操作i类型 如数据导入、API导入、数据修改
                                testRecord.setOperateMode(recordLog.operateMode());
                                //操作描绘  如增、删、查、改
                                testRecord.setOperateDesc(recordLog.operateDesc());
                                //记录操作人
                                testRecord.setOperateUserName(UserUtils.get().getUsername() == null ? UserUtils.get().getUserCode() : UserUtils.get().getUsername());
                                // todo 记录操作人ID（userinfotoredis中没有userid）
//                                testRecord.setOperateUserId(UserSession.getUserId().toString());
                                //记录操作IP
                                testRecord.setOperateIp(getIpAddr(request));
                                //记录请求方法名
                                testRecord.setOperateMethod(methodName);
                                //记录参数中某个元素值
                                testRecord.setSocialCreditCode(targetAttribute.toString());
                                list.add(testRecord);
                            }

                        }
                    } else {
                        //参数不是
                        Field[] fields = FieldUtils.getAllFields(args[index].getClass());
                        for (Field field : fields) {
                            field.setAccessible(true);
                            //获取字段的名称
                            Object fieldValue = field.getName();
                            if (StringUtils.compare(fieldValue.toString(), recordLog.targetAttribute()) == 0) {
                                //获取字段值
                                Object file = field.get(args[index]);
                                SupOperateLog testRecord = new SupOperateLog();
                                //操作时间
                                testRecord.setOperateDate(LocalDateTime.now());
                                //操作i类型 如数据导入、API导入、数据修改
                                testRecord.setOperateMode(recordLog.operateMode());
                                //操作描绘  如增、删、查、改
                                testRecord.setOperateDesc(recordLog.operateDesc());
                                //记录操作人
                                testRecord.setOperateUserName(UserUtils.get().getUsername() == null ? UserUtils.get().getUserCode() : UserUtils.get().getUsername());
                                //记录操作人ID
//                                testRecord.setOperateUserId(UserSession.getUserId().toString());
                                //记录操作IP
                                testRecord.setOperateIp(getIpAddr(request));
                                //记录请求方法名
                                testRecord.setOperateMethod(methodName);
                                //记录参数中某个元素值
                                testRecord.setSocialCreditCode(file.toString());
                                list.add(testRecord);
//                                System.out.println(aim);
                            }
                        }

                    }
                    System.out.println(aim);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //      }
        return list;
    }

    /**
     * 根据属性名获取属性值
     */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private static final String COMMA = ",";
    private static final String IP = "127.0.0.1";
    private static final String LOCAL_IP = "0:0:0:0:0:0:0:1";
    /**
     * x-forwarded-for是识别通过HTTP代理或负载均衡方式连接到Web服务器的客户端
     * 最原始的IP地址的HTTP请求头字段
     */
    private static final String HEADER = "x-forwarded-for";
    private static final String UNKNOWN = "unknown";
    /**
     * 经过apache http服务器的请求才会有
     * 用apache http做代理时一般会加上Proxy-Client-IP请求头
     * 而WL-Proxy-Client-IP是它的weblogic插件加上的头。
     */
    private static final String WL_IP = "WL-Proxy-Client-IP";
    private static final Integer IP_LENGTH = 15;

    /**
     * 获取IP地址
     *
     * @param request HttpServletRequest
     * @return IP地址
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader(HEADER);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals(IP) || ip.equals(LOCAL_IP)) {
                // 根据网卡获取本机配置的IP地址
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (Objects.nonNull(inetAddress)) {
                    ip = inetAddress.getHostAddress();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实的IP地址，多个IP按照','分割
        if (null != ip && ip.length() > IP_LENGTH) {
            // "***.***.***.***".length() = 15
            if (ip.indexOf(COMMA) > 0) {
                ip = ip.substring(0, ip.indexOf(COMMA));
            }
        }
        return ip;
    }

}
