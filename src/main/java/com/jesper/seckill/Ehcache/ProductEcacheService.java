package com.jesper.seckill.Ehcache;


import com.jesper.seckill.bean.ProductInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/5/27 10:56
 */
@Service
@CacheConfig(cacheNames = "user")
public class ProductEcacheService {

    /**
     * 这个注解一般加在查询方法上，表示将一个方法的返回值缓存起来，默认情况下，缓存的 key 就是方法的参数，缓存的 value 就是方法的返回值。
     * @param id
     * @return
     */
    @Cacheable(value = "user", key = "#id")
    public String getUserById(Long id) {
        return "123";
    }


    /**
     * 这个注解一般加在更新方法上，当数据库中的数据更新后，缓存中的数据也要跟着更新，使用该注解，可以将方法的返回值自动更新到已经存在的 key 上
     * @return
     */
    @CachePut(value = "user", key = "#id")
    public String updateUserById(String id) {
        return "123";
    }

    /**
     * 这个注解一般加在删除方法上，当数据库中的数据删除后，相关的缓存数据也要自动清除，该注解在使用的时候也可以配置按照某种条件删除（ condition 属性）或者或者配置清除所有缓存（ allEntries 属性）
     * @param id
     */
    @CacheEvict(value = "user", key = "#id")
    public void deleteUserById(Long id) {

    }
    /**
     * 将商品信息放入本地堆缓存
     */
    @CachePut(value="user",key="'key_'+#productInfo.getId()")
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地缓存获取商品信息
     */
    @Cacheable(value="user",key="'key_'+#id")
    public ProductInfo getLocalCache(int id) {
        return null;
    }

}
