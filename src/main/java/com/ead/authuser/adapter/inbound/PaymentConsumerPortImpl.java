package com.ead.authuser.adapter.inbound;

import com.ead.authuser.core.domain.PaymentEventDomain;
import com.ead.authuser.core.domain.enumeration.PaymentControl;
import com.ead.authuser.core.port.PaymentConsumerPort;
import com.ead.authuser.core.port.RoleQueryServicePort;
import com.ead.authuser.core.port.UserQueryServicePort;
import com.ead.authuser.core.port.UserServicePort;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;

import static com.ead.authuser.core.domain.enumeration.RoleType.ROLE_STUDENT;
import static com.ead.authuser.core.domain.enumeration.UserType.STUDENT;
import static com.ead.authuser.core.domain.enumeration.UserType.USER;
import static org.springframework.amqp.core.ExchangeTypes.FANOUT;

@Log4j2
@AllArgsConstructor
@Component
public class PaymentConsumerPortImpl implements PaymentConsumerPort {

    private final UserServicePort userServicePort;
    private final UserQueryServicePort userQueryServicePort;
    private final RoleQueryServicePort roleQueryServicePort;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.payment-event-queue.name}", durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.payment-event}", type = FANOUT, ignoreDeclarationExceptions = "true"))
    )
    @Override
    public void listenPaymentEvent(final PaymentEventDomain paymentEventDto) {
        var user = userQueryServicePort.findById(paymentEventDto.userId());
        var roleModel = roleQueryServicePort.findByRoleName(ROLE_STUDENT);
        switch (PaymentControl.valueOf(paymentEventDto.PaymentControl())){
            case EFFECTED -> {
                var roles = new HashSet<>(user.roles());
                roles.add(roleModel);
                if (user.isUser()){
                    userServicePort.save(user.toBuilder()
                                    .userType(STUDENT)
                                    .roles(roles)
                            .build());
                }
            }
            case REFUSED -> {
                var roles = new HashSet<>(user.roles());
                roles.remove(roleModel);
                if (user.isStudent()){
                    userServicePort.save(user.toBuilder()
                            .userType(USER)
                            .roles(roles)
                            .build());
                }
            }
            case ERROR -> log.warn("Error to process payment {}", paymentEventDto);
        }
    }
}
