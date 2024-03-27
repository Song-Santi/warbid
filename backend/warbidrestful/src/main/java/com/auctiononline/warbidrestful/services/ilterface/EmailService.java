package com.auctiononline.warbidrestful.services.ilterface;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
