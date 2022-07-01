package com.weilai.loadbalance;

import java.util.List;

/**
 * @ClassName RoundRobinLoadBalance
 * @Description: 轮询负载均衡
 */
public class RoundRobinLoadBalance implements LoadBalancer {

    private int pos = -1;

    @Override
    public String select(List<String> addressList) {
        pos++;
        pos = pos % addressList.size();

        return addressList.get(pos);
    }
}
