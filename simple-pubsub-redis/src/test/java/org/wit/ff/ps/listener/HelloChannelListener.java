package org.wit.ff.ps.listener;

import org.slf4j.Logger;

public class HelloChannelListener implements IMessageListener<String> {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(HelloChannelListener.class);

    @Override
    public Class<String> getMsgBodyType() {
        return String.class;
    }

    @Override
    public void doMessage(String msg) {
        LOG.info("hello world! channel:hello.world|msg:" + msg);
    }

}
