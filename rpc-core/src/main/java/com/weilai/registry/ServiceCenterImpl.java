package com.weilai.registry;

import com.weilai.loadbalance.LoadBalancer;
import com.weilai.loadbalance.RandomLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @ClassName ZkServiceRegistry
 * @Description: TODO
 */
@Slf4j
public class ServiceCenterImpl implements ServiceCenter {

    private final CuratorFramework client;
    private final LoadBalancer loadBalancer = new RandomLoadBalance();

    private static final String REGISTER_ROOT_PATH = "registry";
    private static final String DEFAULT_ADDRESS = "127.0.0.1:2181";

    /**
     * zookeeper客户端初始化，并与服务端建立连接
     */
    public ServiceCenterImpl() {
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);

        this.client = CuratorFrameworkFactory
                .builder()
                .connectString(DEFAULT_ADDRESS)
                .sessionTimeoutMs(40000)
                .retryPolicy(policy)
                .namespace(REGISTER_ROOT_PATH).build();

        this.client.start();
        System.out.println("Zookeeper连接成功.");

    }

    @Override
    public void registry(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            // serviceName创建成永久节点，服务提供者下线时，不删除服务名，只删除地址
            if (client.checkExists().forPath("/" + serviceName) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            // 路径地址
            String path = "/" + serviceName + "/" + getServiceAddress(inetSocketAddress);
            // 临时节点，服务器下线就删除节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            log.error("此服务已存在.");
        }
    }

    /**
     * 根据服务名返回地址
     *
     * @param serviceName 服务名称
     * @return null
     */
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<String> strs = client.getChildren().forPath("/" + serviceName);
            String str = loadBalancer.select(strs);
            return parseAddress(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 把地址转换为 ip:port 字符串
    private String getServiceAddress(InetSocketAddress inetSocketAddress) {
        return inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort();
    }

    // ip:port 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
