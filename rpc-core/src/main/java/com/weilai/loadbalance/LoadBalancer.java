package com.weilai.loadbalance;

import java.util.List;

public interface LoadBalancer {

    String select(List<String> addressList);

}
