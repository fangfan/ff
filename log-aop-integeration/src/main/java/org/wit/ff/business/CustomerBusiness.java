package org.wit.ff.business;

import org.springframework.stereotype.Service;
import org.wit.ff.model.Customer;

/**
 * Created by F.Fang on 2015/11/10.
 * Version :2015/11/10
 */

@Service
public class CustomerBusiness {

    public Customer getCustomer(int appId, int customerId){
        Customer customer = new Customer();
        customer.setCompanyId(10010);
        customer.setId(customerId);
        customer.setTitle("hnb");
        customer.setName("cxb");
        customer.setLevel(Integer.MAX_VALUE);
        return new Customer();
    }

    public void saveCustomer(int appId, Customer customer){
        System.out.println("appId is:"+appId);
        System.out.println("customer is:"+customer);
    }

}
