package com.kangyonggan.blog.web.shiro;

import com.kangyonggan.api.common.service.RedisService;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author kangyonggan
 * @since 2016/12/31
 */
@Log4j2
public class MyEnterpriseCacheSessionDAO extends EnterpriseCacheSessionDAO {

    @Autowired
    private RedisService redisService;

    /**
     * 创建session，保存到redis数据库
     *
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        log.error("创建session，保存到redis数据库,sessionId:" + sessionId);
        redisService.set(sessionId.toString(), session);

        return sessionId;
    }

    /**
     * 获取session
     *
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        log.error("获取session,sessionId:" + sessionId);
        // 先从缓存中获取session，如果没有再去数据库中获取
        Session session = super.doReadSession(sessionId);
        log.error("获取session,session:" + session);
        if (session == null) {
            session = (Session) redisService.get(sessionId.toString());
        }
        log.error("获取session2,session2:" + session);
        return session;
    }

    /**
     * 更新session的最后一次访问时间
     *
     * @param session
     */
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        redisService.set(session.getId().toString(), session);
    }

    /**
     * 删除session
     *
     * @param session
     */
    @Override
    protected void doDelete(Session session) {
        log.error("删除session,session:" + session);
        log.error("删除session,sessionId:" + session.getId());
        super.doDelete(session);
        redisService.delete(session.getId().toString());
    }

}
