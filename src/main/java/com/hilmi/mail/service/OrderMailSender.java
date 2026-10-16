package com.hilmi.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hilmi.mail.model.OrderMessage;

@Service
public class OrderMailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendOrderMail(OrderMessage order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(order.getEmail());
        message.setSubject("Order Baru #" + order.getId());
        message.setText(buildBody(order));
        mailSender.send(message);
        System.out.println("Order email sent to: " + order.getEmail());
    }

    private String buildBody(OrderMessage order) {
        return "Order ID: " + order.getId() + "\n"
                + "Produk ID: " + order.getProdukId() + "\n"
                + "Pelanggan ID: " + order.getPelangganId() + "\n"
                + "Email: " + order.getEmail() + "\n"
                + "Tanggal Transaksi: " + order.getTglTransaksi() + "\n"
                + "Jumlah: " + order.getJumlah() + "\n"
                + "Total: " + order.getTotal();
    }
}
