package pp.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class SendMailTest {

    @Test
    public void sendFromGmail() {
    SendMail a = new SendMail();
    a.SendFromGmail("toledoomer@gmail.com" , "OmerNLP123!", "adasd");
    }
}