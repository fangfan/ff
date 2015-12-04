package org.wit.ff.ps.listener;

import org.slf4j.Logger;

import org.wit.ff.ps.model.User;

public class UserChannelListener implements IMessageListener<User> {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserChannelListener.class);

    @Override
    public void doMessage(User msg) {
        LOG.info("channel:hello.user|msg:" + msg);
    }

    @Override
    public Class<User> getMsgBodyType() {
        return User.class;
    }

}
