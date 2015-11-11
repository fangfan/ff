package org.wit.ff.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.wit.ff.util.JsonUtil;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by F.Fang on 2015/11/10.
 * Version :2015/11/10
 */
@Aspect
public class BusinessLogAspect {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination destination;

    @Around("execution(* org.wit.ff.business.*.*(..))")
    public Object record(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Object result = pjp.proceed();
            // 添加正常处理的日志.
            sendMsg(buildLog(pjp, null));
            return result;
        } catch (Throwable e) {
            // 增加异常处理的日志.
            sendMsg(buildLog(pjp, e));
            throw e;
        }
    }

    private TraceLog buildLog(ProceedingJoinPoint pjp, Throwable e) {
        TraceLog log = new TraceLog();
        // 要保证所有的逻辑方法在调用参数上做限定，必须保证第一个参数是appId.
        if (pjp.getArgs() != null && pjp.getArgs().length >= 1) {
            log.setAppId((int) pjp.getArgs()[0]);
        }
        log.setOperation(pjp.getSignature().getName());
        if (null != e) {
            String msg = getStackTrace(e);
            if(msg.length()>256){
                log.setDetails(msg.substring(0,256));
            } else{
                log.setDetails(msg);
            }
        }
        return log;
    }

    private void sendMsg(final TraceLog log) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session paramSession) throws JMSException {
                return paramSession.createTextMessage(JsonUtil.objectToJson(log));
            }
        });
    }

    /**
     * 获取目标异常栈信息.
     * 由于异常栈信息可能过长,如果考虑将数据入库或其它介质,最好考虑最大长度不超过一个阀值.
     *
     * @param throwable 目标异常.
     * @return
     */
    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

}
