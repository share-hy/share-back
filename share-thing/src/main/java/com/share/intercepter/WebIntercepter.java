package com.share.intercepter;

import com.lumi.aiot.cloud.IPUtil;
import com.lumi.aiot.cloud.common.ErrorCodeUtils;
import com.lumi.aiot.cloud.common.HttpCommomHeader;
import com.lumi.aiot.cloud.open.common.App3rdRedisKeyConstant;
import com.lumi.aiot.cloud.open.common.HttpOpenCommonHeader;
import com.lumi.aiot.cloud.open.controller.BaseOpenController;
import com.lumi.aiot.cloud.open.domain.IotApp3rdDeveloper;
import com.lumi.aiot.cloud.open.dto.CookieConstant;
import com.lumi.aiot.cloud.open.service.DeveloperService;
import com.lumi.aiot.cloud.open.utils.AbstractOpenCloudErrorUtil;
import com.lumi.aiot.cloud.open.utils.GatewayUtils;
import com.lumi.aiot.cloud.open.utils.RedisLockService;
import com.lumi.aiot.cloud.util.SpringRequestHolderUtil;
import com.netflix.zuul.context.RequestContext;
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


/**
 * @author lvyl
 * @date 2019/8/21 18:09
 * @description
 */
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
    @Before("execution(* com.lumi.aiot.cloud.open.controller.user.LoginController.*(..)) || execution(* com.lumi.aiot.cloud.open.controller.user.RegisterController.*(..))" +
            "|| execution(* com.lumi.aiot.cloud.open.controller.user.AuthCodeController.*(..))"
            + "|| execution(* com.lumi.aiot.cloud.open.controller.user.InfoController.*(..))")
    public void before() {
        //直接设置tempServer下次就取不到初始值
        byte loginServerCode = tempServer;
        String ipAddr = null;
        if (loginServerCode == 30){
            ipAddr = GatewayUtils.getIpAddr(SpringRequestHolderUtil.getRequest());
            loginServerCode = IPUtil.getUserServerCode(ipAddr);
        }

        log.info("section get request ip:{},server code:{}",ipAddr,loginServerCode);
        CookieConstant.addUseServerCookie(String.valueOf(loginServerCode),"aqara.com",1440*60);

        RequestContext currentContext = RequestContext.getCurrentContext();
        currentContext.addZuulRequestHeader(HttpOpenCommonHeader.REQUEST_USE_SERVER, String.valueOf(loginServerCode));

    }

    @Around("execution(* com.lumi.aiot.cloud.open.controller.user.InfoController.*(..)) || " +
            "(execution(* com.lumi.aiot.cloud.open.controller.console..*(..)) " +
            "&& !execution(* com.lumi.aiot.cloud.open.controller.console.SubscriberController.response(..)) " +
            "&& !execution(* com.lumi.aiot.cloud.open.controller.console.SyncController.*(..)))" +
            "|| execution(* com.lumi.aiot.cloud.open.controller.manage..*(..)) "+
            "|| execution(* com.lumi.aiot.cloud.open.controller.v1v2..*(..)))")
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

    public static void main(String[] args) {
        Byte userServerCode = IPUtil.getUserServerCode("113.108.104.98");
        System.out.println(userServerCode);
    }

}
