package com.hilmi.mail.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hilmi.mail.model.OrderMessage;

@ExtendWith(MockitoExtension.class)
class OrderMailListenerTests {

    @Mock
    private OrderMailSender orderMailSender;

    @InjectMocks
    private OrderMailListener orderMailListener;

    @Test
    void receivesReferenceOrderPayload() {
        String payload = "{\"id\":1,\"produkId\":2,\"pelangganId\":3,"
                + "\"email\":\"user@example.com\",\"tglTransaksi\":\"2026-10-16\","
                + "\"jumlah\":2,\"total\":5000.0}";

        orderMailListener.receiveOrder(payload);

        ArgumentCaptor<OrderMessage> order = ArgumentCaptor.forClass(OrderMessage.class);
        verify(orderMailSender).sendOrderMail(order.capture());
        assertThat(order.getValue().getTglTransaksi().toString()).isEqualTo("2026-10-16");
    }

    @Test
    void propagatesMalformedPayload() {
        assertThatThrownBy(() -> orderMailListener.receiveOrder("not-json"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to read order message");
    }
}
