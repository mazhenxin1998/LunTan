package com.example.myboot07.dao;

import com.example.myboot07.bean.NotifiCation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface INotifiCationDao {

    Integer insertNotification(@Param("notification") NotifiCation notifiCation);


    List<NotifiCation> queryNotificationByReceiver(@Param("id") Integer id);

    Integer updateNotificationStatus(@Param("type") int type, @Param("receiver") Integer receiver, @Param("id") Integer id);

    NotifiCation queryNotifiCationByViewFunction(@Param("notifiCation") NotifiCation notifiCation);
}
