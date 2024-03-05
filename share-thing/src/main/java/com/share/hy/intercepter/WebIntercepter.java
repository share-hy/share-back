package com.share.hy.intercepter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class WebIntercepter extends BaseOpenController {

    @Autowired
    private DeveloperService developerService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${temp.server:30}")
    private Byte tempServer;
    @Autowired
    private RedisLockService redisLockService;
    /**
     * 控制台访问流量限制
     */
    @Value("${console.limit:50}")
    private Integer consoleLimitTimes;

    /**
     * 控制台访问流量限制 unit:s
     */
    @Value("${console.time.range:10}")
    private Integer consoleTimeRange;

    private static final int GRANTED_NO = 0;

    /**
     * 以下接口需要判断当前用户所处地区，选择注册、登录、获取验证码以及图片上传的服务器
     */
    @Before("")
    public void before() {


    }

    @Around("")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        HttpCommomHeader httpCommomHeader = getHttpCommomHeader();
        String token = httpCommomHeader.getToken();
        if (StringUtils.isBlank(token)) {
            // 如果从header中读取不到,则从cookie中进行获取试试
            token = CookieConstant.getCookieValue(CookieConstant.TOKEN_COOKIE_NAME);
            log.info("get token from cookie token:{}", token);
            if (StringUtils.isBlank(token)) {
                return failed(ErrorCodeUtils.CommonErrorCode.ERROR_TOKEN_IS_ABSENCE);
            }
        }
        String userId = stringRedisTemplate.opsForValue().get(App3rdRedisKeyConstant.getUserRedisKey(token));
        if (StringUtils.isBlank(userId)) {
            return failed(ErrorCodeUtils.CommonErrorCode.ERROR_TOKEN_IS_EXPRIED);
        }
        //记录同个userId，短时间内只能访问一定的次数
        boolean limit = redisLockService.counterLimit(App3rdRedisKeyConstant.getConsoleLimitKey(userId), consoleTimeRange, consoleLimitTimes);
        if (limit){
            log.warn("the developer request too many times:{}",userId);
            return failed(ErrorCodeUtils.CommonErrorCode.ERROR_REQUEST_FLOW_LIMIT);
        }
        String headerUserId = httpCommomHeader.getUserid();
        if (!StringUtils.equals(userId,headerUserId)) {
            return failed(ErrorCodeUtils.CommonErrorCode.ERROR_TOKEN_IS_ABSENCE);
        }
        return pjp.proceed();
    }

    @Around("execution(* com.lumi.aiot.cloud.open.controller.manage..*(..))")
    public Object aroundAdmin(ProceedingJoinPoint pjp) throws Throwable {
        String userId = getHttpCommomHeader().getUserid();
        String lang = getHttpCommomHeader().getLang();
        IotApp3rdDeveloper developer = developerService.getDeveloperInfo(userId);
        if (developer == null || developer.getState() == GRANTED_NO || developer.getRole() != 1) {
            return failed(AbstractOpenCloudErrorUtil.ErrorCodeEnum.ERROR_PERMISSION_DENIED, lang);
        }

        return pjp.proceed();
    }

}
