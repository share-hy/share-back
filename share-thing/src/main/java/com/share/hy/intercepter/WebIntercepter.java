package com.share.hy.intercepter;
import com.share.hy.common.HttpCommonHeader;
import com.share.hy.common.constants.CookieConstant;
import com.share.hy.common.constants.RedisKeyConstant;
import com.share.hy.common.controller.BaseController;
import com.share.hy.common.enums.ErrorCodeEnum;
import com.share.hy.domain.ShareUser;
import com.share.hy.manager.IUserManager;
import com.share.hy.utils.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class WebIntercepter extends BaseController {
    /**
     * 控制台访问流量限制
     */
    @Value("${console.limit:50}")
    private Integer consoleLimitTimes;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisLockService redisLockService;

    @Autowired
    private IUserManager userManager;

    /**
     * 控制台访问流量限制 unit:s
     */
    @Value("${console.time.range:10}")
    private Integer consoleTimeRange;

    private static final int GRANTED_NO = 0;

    @Around("execution(* com.share.hy.controller.user..*(..))" +
            "&& !(execution(* com.share.hy.controller.user.UserController.login(..))) "+
            "&& !(execution(* com.share.hy.controller.user.UserController.register(..))) "+
            "&& !(execution(* com.share.hy.controller.user.UserController.logout(..)))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        String token = httpCommonHeader.getToken();
        if (StringUtils.isBlank(token)) {
            // 如果从header中读取不到,则从cookie中进行获取试试
            token = CookieConstant.getCookieValue(CookieConstant.TOKEN_COOKIE_NAME);
            log.info("get token from cookie token:{}", token);
            if (StringUtils.isBlank(token)) {
                return failed(ErrorCodeEnum.ERROR_TOKEN_IS_ABSENCE);
            }
        }
        String userId = stringRedisTemplate.opsForValue().get(RedisKeyConstant.getUserTokenKey(token));
        if (StringUtils.isBlank(userId)) {
            return failed(ErrorCodeEnum.ERROR_TOKEN_IS_EXPIRED);
        }
        //记录同个userId，短时间内只能访问一定的次数
        boolean limit = redisLockService.counterLimit(RedisKeyConstant.getConsoleLimit(userId), consoleTimeRange, consoleLimitTimes);
        if (limit){
            log.warn("the developer request too many times:{}",userId);
            return failed(ErrorCodeEnum.ERROR_REQUEST_FLOW_LIMIT);
        }
        String headerUserId = httpCommonHeader.getUserId();
        if (!StringUtils.equals(userId,headerUserId)) {
            return failed(ErrorCodeEnum.ERROR_TOKEN_IS_ABSENCE);
        }
        return pjp.proceed();
    }

    @Around("execution(* com.share.hy.controller.admin..*(..))")
    public Object aroundAdmin(ProceedingJoinPoint pjp) throws Throwable {
        String userId = getHttpCommonHeader().getUserId();
        String lang = getHttpCommonHeader().getLang();
        ShareUser adminUser = userManager.getAdminUser(userId);
        if (adminUser == null || adminUser.getState() == GRANTED_NO) {
            return failed(ErrorCodeEnum.ERROR_PERMISSION_DENIED);
        }
        return pjp.proceed();
    }

}
