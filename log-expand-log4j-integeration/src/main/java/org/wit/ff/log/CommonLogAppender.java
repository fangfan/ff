package org.wit.ff.log;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import javax.jms.*;

/**
 * Created by F.Fang on 2015/11/10.
 * Version :2015/11/10
 */
public class CommonLogAppender extends AppenderSkeleton {

    private static final String COMMON_LOG_QUEUE = "demo.common.log";
    private PooledConnectionFactory pooledConnectionFactory;

    @Override
    public void activateOptions() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        pooledConnectionFactory = new PooledConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(1);
        pooledConnectionFactory.setMaximumActiveSessionPerConnection(2);
    }

    @Override
    protected void append(LoggingEvent event) {
        Connection connection = null;
        Session session = null;
        try {
            connection = pooledConnectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(session.createQueue(COMMON_LOG_QUEUE));
            if(event.getMessage()!=null){
                TextMessage txtMsg = session.createTextMessage(event.getMessage().toString());
                producer.send(txtMsg);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void close() {
        System.out.println("close!!!");
        pooledConnectionFactory.stop();
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}
