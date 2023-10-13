package com.ead.authuser.core.port;

import com.ead.authuser.core.domain.PaymentEventDomain;

public interface PaymentConsumerPort {

    void listenPaymentEvent(final PaymentEventDomain paymentEventDto);

}
