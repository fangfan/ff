package org.wit.ff.ps;

/**
 * 应该考虑增加元信息,例如定义Meta结构,并增加获取Meta的接口.
 * 需要在后期广泛调研各类消息服务器才能定义好.
 * eg: Meta getMeta();
 * @param <M>
 */
public interface IMessage<M> {

    /**
     * 获取消息体.
     * @return
     */
    M getBody();
    
}
