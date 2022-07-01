package com.weilai.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @ClassName RandomLoadBalance
 * @Description: 随机负载均衡
 */
public class RandomLoadBalance implements LoadBalancer {
    @Override
    public String select(List<String> addressList) {
        int size = addressList.size();
        return addressList.get(new Random().nextInt(size));
    }
}
