package com.share.hy.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
//TODO set condition.
public class AlipayConfig {

    public static String APPID = "2021003194664455";

    public static String RSA_PRIVATE_KEY;


    public static String notify_url;

    public static String return_url;
    public static String URL = "https://openapi.alipay.com/gateway.do";

    public static String CHARSET = "UTF-8";
    public static String FORMAT = "json";

    public static String ALIPAY_PUBLIC_KEY;
    public static String SIGNTYPE = "RSA2";

    @Bean
    public AlipayClient alipayClient( @Value("${payment.alipay.appid:2021003194664455}") String appid,
                                      @Value("${payment.alipay.rsa-private-key:MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCWRB8pi4+4LwRlGf2DPvIJqf6u1DsZEtt1ClqoC6mqKGNEdxr4aZkbHx3G0FI30lgY0SvRzQjHTmOFrVYPjqsK9YEzFisD9OO9r6uCDu89KSkPQfXJwqbz/PpVUDTHN4WAE6FrUjBiM741k1HoFt+hzfCBRTAL/hvkD/XDY2JCNWwqyQ86KkrdCP0bVUZBfW1GrV2fcrLRUyH2DX1QxUCo7gsL0oImrCIYM8nM2j1ozZ+bpLbjR8KoLtv/u0c2CtxA1c/R/w2ZpAX0Q67aAO6WKX61hxB11iH+x+6Nv5Xgw2NOJHRtnn4JW//lm6x6EhhoEyoaNQTkYZFdcq2aa9KNAgMBAAECggEAYnfte8oKugWFecMYT6M19INDc8l7mKi0yvOoCKrr2adxtXHfaJ26SP2RA7GLzyy1sj2P24HimMiTiwvVc4hoKVxL9/A06/jULH8rgqTKvWd1kpwEttwpj4Vdff57N3j4sL8sbimDrzZ9FJqfd9lVXrCXQ/7Ux2cTW9XhlNJRl1RqMLFFmaIRip6AO7YJc8hVVXw08HXJtTzGHJcAx84D9V+l8M3ea7aHnGRITVnu6iKcm1rydQAqPqJUaCgzmlUDMai5iol2gP7FZgk6IXR5jZKqPv29sDV1e2xd8uqHtpzQdKfo/Y1ErvIXEjExPxdXAl2KYZ4N89MjP+rjhSSTwQKBgQDnIgm/pvfG8DT/yBHgUSFy8aQ7p7AhvNJfXzf5GrUofhE79rhp/0BDJxmpa668Z5P7f8jRoKPV650RWla657HsrF7PG609+GIRVWntM4jjqUwc0H5W03RlFGe7vLEOZeu05vAUvNNHb/swYBJCxoutLQ7Kbf3dAwwBrwVccGeKGQKBgQCmbtB2+8phzOaeVHQwE50f6Uair2Oz6O/ytcDAjiY/LgAXDXL9MGSXMKWMrkSM4/vC/7Cn3+ANOBnvHMaJNw9nc30+I3J1hWZ+dQ95yukj47gmt1ZbWAqmY8QVlpm//rhO6VenJ2pn2PD+nJSQMg+m3ao6OA6r4XiFnOmwrr5ClQKBgHp79UKVDvVj0hJCSgbft6QcHfZnFNWsJKfZ4+Wkm6ALvsJSKsQxOjLcA6Nep0i8cxVyUE+Wm3v/0kqh3xhJNGbBqbSDbDlM6VtaMjo7dy1+SBF1A56rjCEMwTQh+IoyxMgnIc87iZNv6mNmZgbxBJAPMqlfxIpc1P4HbAXio1vxAoGAY3kj+5luUpnm1IbhksKGTMZeQ2nUi3kTwNrEw9GyJlzWDPEq4w0DciHPMJAPxK2WkevEp/F0tjHrvjBSsTxJZqluF6+T/42lvrs0brTKN3spW2qEoL+0TdKfJdAHQOt7OR7aFrvGKx9MmsD3Ql/JXxBpZ7aNfkmagTFRsxod+SkCgYB6S76ZrMBfygEwYC1YRe3xHyQ7jVJXZE33HePWcISmeHoGcRqcmcTTX9jyX0PDeA8AJU7wrap35QdmLuYfFYos5NcBDT2+oc/BMB5gnzvGIiomBjl61mKspeHLAB6CIE55CIWiR4SMbA7sVNyQiO5RPQFRSTysAeg8WIFcBDmV5Q==}") String rsaPrivateKey,
                                      @Value("${payment.alipay.notify-url:https://aiot-test.aqara.com/app/v1.0/lumi/payment/callback/alipay}") String notify_url,
                                      @Value("${payment.alipay.return-url:}") String return_url,
                                      @Value("${payment.alipay.rsa-public-key:MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhMvXQZ3rBiWLgwWSZZ6WNIP5m5TRNsIuDnSue61scYoqSvTe17ePy6axnS+eBz6SUeXylDZCLIyIeBM54+JE7OGRsEzEDdPbbSvB2ilHbVdIY8NAHcbtJpVqNWAPA8SwzDM5SiNdBokSpQnNaXkH1gCcwCroADAFVmduOa74XO/9Ev6w1d7e8taMKe6Yt3i+7dASJcJcSeuSoy4jeQseZoUQhj3hlwPDvNx54tRCI9dJYOXRBSnjFSnylgD7EZP7fJWFbaCKJRJfaktg0tPddW7wMMjiY2HX3ra57gfAwm659PzCBTgI1fZoYjaXbQ2EfmjIJQBdX7B/WFLylGmqswIDAQAB}") String alipayPublicKey) {

        RSA_PRIVATE_KEY = rsaPrivateKey;
        APPID = appid;
        AlipayConfig.notify_url = notify_url;
        AlipayConfig.return_url = return_url;
        ALIPAY_PUBLIC_KEY = alipayPublicKey;

        return new DefaultAlipayClient(AlipayConfig.URL,
                AlipayConfig.APPID,
                AlipayConfig.RSA_PRIVATE_KEY,
                AlipayConfig.FORMAT,
                AlipayConfig.CHARSET,
                AlipayConfig.ALIPAY_PUBLIC_KEY,
                AlipayConfig.SIGNTYPE);

    }

}
