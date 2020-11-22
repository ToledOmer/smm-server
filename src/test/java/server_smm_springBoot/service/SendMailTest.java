package server_smm_springBoot.service;

import org.junit.Test;

public class SendMailTest {

    @Test
    public void sendFromGmail() {
    SendMail a = new SendMail();
    a.SendFromGmail("toledoomer@gmail.com" , "OmerNLP123!", "adasd");
    }
}