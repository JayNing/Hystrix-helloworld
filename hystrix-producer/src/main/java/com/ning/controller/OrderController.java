package com.ning.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.ning.entity.OrderEntity;
import com.ning.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * author JayNing
 * created by 2019/12/23 14:25
 **/
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @HystrixCommand(commandKey = "testCommand", groupKey = "testGroup",
            fallbackMethod = "fail1",threadPoolKey = "testThreadKey",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "3"),
                    /**
                     * BlockingQueue的最大队列数，当设为-1，会使用SynchronousQueue，值为正时使用LinkedBlcokingQueue。
                     */
                    @HystrixProperty(name = "maxQueueSize", value = "5"),
                    /**
                     * 设置存活时间，单位分钟。如果coreSize小于maximumSize，那么该属性控制一个线程从实用完成到被释放的时间.
                     */

                    /**
                     我们知道，线程池内核心线程数目都在忙碌，再有新的请求到达时，线程池容量可以被扩充为到最大数量。
                     等到线程池空闲后，多于核心数量的线程还会被回收，此值指定了线程被回收前的存活时间，默认为 2，即两分钟。
                     */
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    /**
                     * 设置队列拒绝的阈值,即使maxQueueSize还没有达到
                     */
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "5"),

                    // 滑动统计的桶数量
                    /**
                     * 设置一个rolling window被划分的数量，若numBuckets＝10，rolling window＝10000，
                     *那么一个bucket的时间即1秒。必须符合rolling window % numberBuckets == 0。默认1
                     */
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5"),
                    // 设置滑动窗口的统计时间。熔断器使用这个时间
                    /** 设置统计的时间窗口值的，毫秒值。
                     circuit break 的打开会根据1个rolling window的统计来计算。
                     若rolling window被设为10000毫秒，则rolling window会被分成n个buckets，
                     每个bucket包含success，failure，timeout，rejection的次数的统计信息。默认10000
                     **/
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
            },
            commandProperties = {
                    // 熔断器在整个统计时间内是否开启的阀值
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    // 至少有3个请求才进行熔断错误比率计算
                    /**
                     * 设置在一个滚动窗口中，打开断路器的最少请求数。
                     比如：如果值是20，在一个窗口内（比如10秒），收到19个请求，即使这19个请求都失败了，断路器也不会打开。
                     */
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    //当出错率超过50%后熔断器启动
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    // 熔断器工作时间，超过这个时间，先放一个请求进去，成功的话就关闭熔断，失败就再等一段时间
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
             })
    @GetMapping("orderList")
    public List<OrderEntity> findOrderByUsername(String username) throws Exception {
        return orderService.findOrderList(username);
    }

    @GetMapping("orderList2")
    public List<OrderEntity> orderList2(String username) throws Exception {
        return orderService.findOrderList(username);
    }

    @HystrixCommand(
            fallbackMethod = "fail1",
            threadPoolProperties = {
                    //10个核心线程池,超过20个的队列外的请求被拒绝; 当一切都是正常的时候，线程池一般仅会有1到2个线程激活来提供服务
                @HystrixProperty(name = "coreSize", value = "10"),
                @HystrixProperty(name = "maxQueueSize", value = "100"),
                @HystrixProperty(name = "queueSizeRejectionThreshold", value = "20")},
            commandProperties = {
                 @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                 @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"), //命令执行超时时间
                 @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"), //若干10s一个窗口内失败三次, 则达到触发熔断的最少请求量
                 @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "30000") //断路30s后尝试执行, 默认为5s
            })
    @GetMapping("orderList3")
    public List<OrderEntity> orderList3(String username) throws Exception {
        return orderService.findOrderList(username);
    }


    private List<OrderEntity> fail1(String username) {
        System.out.println("fail1 : " + username);
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "fail2")
    @GetMapping("/test2")
    public String test2() {
        throw new RuntimeException();
    }

    @HystrixCommand(fallbackMethod = "fail3")
    private String fail2() {
        System.out.println("fail2");
        throw new RuntimeException();
    }

    @HystrixCommand(fallbackMethod = "defaultFail")
    private String fail3() {
        System.out.println("fail3");
        throw new RuntimeException();
    }

    private String defaultFail() {
        System.out.println("default fail");
        return "default fail";
    }
}
