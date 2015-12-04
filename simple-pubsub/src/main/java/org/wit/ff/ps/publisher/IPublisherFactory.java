package org.wit.ff.ps.publisher;

/**
 * 
 * <pre>
 * 抽象获取Pulisher实例的工厂.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: IPublisherFactory.java, v 0.1 2014年12月22日 上午6:19:28 F.Fang Exp $
 */
public interface IPublisherFactory {
    
    IPublisher getPublisherIntance();

}
