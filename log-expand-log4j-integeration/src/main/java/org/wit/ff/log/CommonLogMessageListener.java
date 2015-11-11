package org.wit.ff.log;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by F.Fang on 2015/11/10.
 * Version :2015/11/10
 */
public class CommonLogMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        // 处理消息.
        TextMessage txtMsg = (TextMessage) message;
        try {
            System.out.println(txtMsg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
