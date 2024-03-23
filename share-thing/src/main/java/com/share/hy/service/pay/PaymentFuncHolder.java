package com.share.hy.service.pay;

import com.share.hy.common.enums.PaymentPlatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentFuncHolder {

    @Autowired(required = false)
    private List<AbstractEventHandler<?>> eventHandlers;

    private static final Map<String, AbstractEventHandler<?>> eventHandlerMap = new HashMap<>();



    @Autowired(required = false)
    private List<PaymentService> paymentServices;

    private static final Map<PaymentPlatEnum, PaymentService> paymentServiceMap = new HashMap<>();

    @PostConstruct
    private void init() {

        for (AbstractEventHandler<?> eventHandler : eventHandlers) {
            List<String> eventNames = eventHandler.getEventNames();
            for (String eventName :eventNames) {
                eventHandlerMap.put(eventHandler.getPlatName()+"::"+eventName, eventHandler);
            }
        }


        for (PaymentService paymentService : paymentServices) {
            paymentServiceMap.put(paymentService.getTradePlat(), paymentService);
        }
    }


    public static PaymentService getPaymentService(PaymentPlatEnum paymentPlatEnum) {
        return paymentServiceMap.get(paymentPlatEnum);
    }

    public static AbstractEventHandler getEventHandler(String tradePlat, String eventType) {
        return eventHandlerMap.get(tradePlat + "::" + eventType);
    }

}