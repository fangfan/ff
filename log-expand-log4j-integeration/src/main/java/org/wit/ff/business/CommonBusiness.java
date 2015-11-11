package org.wit.ff.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wit.ff.model.Customer;

/**
 * Created by F.Fang on 2015/11/10.
 * Version :2015/11/10
 */
@Service
public class CommonBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonBusiness.class);

    public Customer getCustomer(int appId, int customerId){
        LOGGER.info("getCustomer, appId="+appId);
        Customer customer = new Customer();
        customer.setCompanyId(10010);
        customer.setId(customerId);
        customer.setTitle("hnb");
        customer.setName("cxb");
        customer.setLevel(Integer.MAX_VALUE);
        return new Customer();
    }

    public void saveCustomer(int appId, Customer customer){
        LOGGER.info("saveCustomer, appId="+appId);
        System.out.println("appId is:"+appId);
        System.out.println("customer is:"+customer);
    }

}
