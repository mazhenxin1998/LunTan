package com.example.myboot07.config;

import com.example.myboot07.job.TestJob1;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

@Configuration
public class QuartzJobConfig {

    /**
     * 方法调用任务明细工厂Bean
     */
    @Bean(name = "myFirstExerciseJobBean")
    public MethodInvokingJobDetailFactoryBean myFirstExerciseJobBean(TestJob1 testJob1) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        jobDetail.setConcurrent(false); // 是否并发
        jobDetail.setName("general-myFirstExerciseJob"); // 设置任务的的名字
        jobDetail.setGroup("general"); // 任务的分组
        jobDetail.setTargetObject(testJob1); // 被执行的对象
        jobDetail.setTargetMethod("test"); // 被执行的方法
        return jobDetail;
    }

    /**
     * 表达式触发器工厂Bean
     */
    @Bean(name = "myFirstExerciseJobTrigger")
    public CronTriggerFactoryBean myFirstExerciseJobTrigger(@Qualifier("myFirstExerciseJobBean") MethodInvokingJobDetailFactoryBean myFirstExerciseJobBean) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(myFirstExerciseJobBean.getObject());
        tigger.setCronExpression("0/10 10 * * * ?"); // 什么是否触发，Spring Scheduler Cron表达式 克隆表达式 每10秒执行一次
        tigger.setName("general-myFirstExerciseJobTrigger");
        return tigger;
    }


}
