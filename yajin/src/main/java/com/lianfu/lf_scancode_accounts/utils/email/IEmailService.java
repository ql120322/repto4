package com.lianfu.lf_scancode_accounts.utils.email;

public interface IEmailService {

    void sendAttachmentMail(String to, String subject, String content, String filePath, String fileName);
}
