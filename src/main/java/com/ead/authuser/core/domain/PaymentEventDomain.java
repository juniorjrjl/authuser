package com.ead.authuser.core.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PaymentEventDomain(UUID paymentId,
                                 String PaymentControl,
                                 OffsetDateTime paymentRequestDate,
                                 OffsetDateTime paymentCompletionDate,
                                 OffsetDateTime paymentExpirationDate,
                                 String lastDigitsCreditCard,
                                 BigDecimal valuePaid,
                                 String paymentMessage,
                                 boolean recurrence,
                                 UUID userId) {
}
