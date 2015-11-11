package org.wit.ff.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wit.ff.util.JsonUtil;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by F.Fang on 2015/11/10.
 * Version :2015/11/10
 */
public class BusinessLogMessageListener implements MessageListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessLogAspect.class);

    @Override
    public void onMessage(Message message) {
        // 处理消息.
        TextMessage txtMsg = (TextMessage) message;
        try {
            TraceLog log = JsonUtil.jsonToObject(txtMsg.getText(), TraceLog.class);
            LOGGER.info("business log:"+log.toString());
        } catch (JMSException e) {
            LOGGER.error("处理业务日志发生异常!", e);
        }
    }
}
