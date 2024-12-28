package com.lanxiu.mianshiya.job.cycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * 增量同步帖子到 es
 *
 * 蓝朽
 * 
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class IncSyncPostToEs {


    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {

    }
}
