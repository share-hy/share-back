package com.share.hy.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.refund.RefundService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class WechatPayConfig {

    /**
     * 商户号
     */
    public static String merchantId = "1641410524";
    /**
     * 商户API私钥路径(不要泄露该内容！！！)
     */
    public static String privateKey = "-----BEGIN PRIVATE KEY-----\n" + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCtJLqH6FJA2LWY\n" + "6Luhik5P5dPomD0udWgH4eTJ4yREmfZXTslePCG5a7UOL4qhOrPZ5YX/FXTiu9SN\n" + "lraGHVDpWg9cD28sk9v7RaScEtx1wuYLkDxAM/LsqwVVNvvQlwQlG7KeBEfmlkP7\n" + "FJvvaOMHrdpi62+6zj/Y1eiONCLX25NpZEK8zvslfwbgBbWNDm9fo3oJbfk0hAs2\n" + "vQwtH0yWEDbNpR47ohA+cQdjaXaVnZvJnJ4rLvjtNWVLyknDiwAnGu+84irIVkVR\n" + "jl4pb0hTmvomX2b6xpl6dzBTRcJWSqSK0oGy4EZT8d+AblSNcIRaV++jbxUNpXvi\n" + "gUbiGeZ7AgMBAAECggEAUqOlJBkOqUTF6CWs6H1Kw3e+nNWM3g/Lh3eZ64IuXU/e\n" + "8+z2z7ODpSxzTkICuNF75hRW0Fk9YLqlyK6Wj4yOgdIqYpY7SduIs3WaC6Q09UAA\n" + "7KP7HemtmKhH81Q6/GQX5l5B8om4h6ZQ7BX6F1rIgXHJOk2FFnQ6pRPz22um9b9e\n" + "BeK6ZfiGlBl+3NqL1pdT3/3mTyBOtsw7g8BdVaIDe3TwIxfNEqes8QalAXabSDtK\n" + "c1T3vIqVMRjpx7ti9YCmUJRnpehP92J+3vdk0bZwUQfcPvUwvqN8Ofp1gF5VG1bw\n" + "SMHLvvnZIRIq69O0NYWhBnLwM4aR1UnfR2UrwmLLgQKBgQDkiWQ6qnmZtXuQmf2c\n" + "XqDtrz82eT09XSNsSAOZxuIflnrim6CUf7kcngSMPx7mr9Nn0OhkIwb9NRtrsMPb\n" + "lLm4Xi13V92cow4vS/HtAGEepdAFu6c383XbTHEaJuJ2B4dGUw0Nme1m5OaQjK4r\n" + "pjPjHUHApVj/S0/4oNN2+AWA9wKBgQDB8z46i7JhdxWCI0D9Jkh+fIW8DuRo5OgA\n" + "GESiRtEzkP7oXGEdniVzwHnrS995g+tPe0MhI8RRp7+p/CUdgz+SSIM0nGiM1rGT\n" + "2W5RZZdgvwLCmfq09I4y36nGE5uPZ2nWKspkgi8KsAgy0KsTvtMqHA5FuWo9Vrs4\n" + "edGI4S/pnQKBgEGKcXctqr/9PxmiQAxXSxm/N4a80GZHzr/gC0BPvEvI0Scp8KyF\n" + "0KW3X6CE/ay04hEU5YDKVtfJIjsJL7JKfdS1FZ7zUPf5/zhrNE9+21t5vCXYwkUW\n" + "ikdBLfKNEkc9M3tkmAq9iUsH/GOaJN0ZximtG01PXi2hAKdSIDMvQAzTAoGAKbyF\n" + "fKXJOi4dI8cgWhmd6hIz2Tc/Mc9omXSfeGhS5oziPfB7OZtppdIaqmbGbWjU/2Kg\n" + "HIaM3aGCnjrTZdlzmbAfA/hQi1Y8GnvkwIFjJP+VwXjPOZeS7iGxcB2SETcI0Hye\n" + "1ygopV0iNoUu+23KFTq6W4i98vsyCMI2NAV5gQUCgYEAo5+3wFC5IVBT0NGsikEQ\n" + "qYbZwHKiwBfApdA2zdkMapH10c0ZMzlEmHYACdR6prh0KM1qEQFn7oy8ZuWcBweq\n" + "f2BV8U342SA0JIoqmo4YlfkNuVMCUE9QuRpjO/eRt7zOmrFnE/USsjqn2FzUQnIx\n" + "uNe2+fGg0nok+vCBHDs56IE=\n" + "-----END PRIVATE KEY-----\n";
    /**
     * 商户证书序列号
     */
    public static String merchantSerialNumber = "172DD2D396C68DF462A828E22468BA8CFCDA3C21";
    /**
     * 商户APIV3密钥(不要泄露该内容！！！)
     */
    public static String apiV3key = "BXwcZUB8pauVcNA5zwZ5DXZeKVLjPmQc";

    public static String appId = "wx7a47165b2980342d";


    public static String notifyUrl;

    @Value("${payment.wechat-pay.notify-url:https://aiot-test.aqara.com/app/v1.0/lumi/payment/callback/wechat-pay}")
    private String __notifyURL;


    @PostConstruct
    private void init() {
        WechatPayConfig.notifyUrl = __notifyURL;
    }

    @Bean
    public Config config() {
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(merchantId)
                .privateKey(privateKey)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(apiV3key)
                .build();
    }

    @Bean
    public H5Service h5Service() {
        return new H5Service.Builder().config(this.config()).build();
    }

    @Bean
    public NativePayService nativePayService(){
        return new NativePayService.Builder().config(this.config()).build();
    }

    @Bean
    public RefundService refundService() {
        return new RefundService.Builder().config(this.config()).build();
    }


    @Bean
    public NotificationParser notificationConfig() {
        return new NotificationParser((NotificationConfig) this.config());
    }
}
