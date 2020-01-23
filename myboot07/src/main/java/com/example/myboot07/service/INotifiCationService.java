package com.example.myboot07.service;

import com.example.myboot07.bean.NotifiCation;
import com.example.myboot07.bean.User;
import com.example.myboot07.dto.NotifiCationDTO;

import java.util.List;

public interface INotifiCationService {

    void insertNotification(NotifiCation notifiCation);


    List<NotifiCation> queryNotificationByReceiver(Integer id);

    Integer updateNotificationStatus(int type, User user, Integer id);

    NotifiCation queryNotifiCationByViewFunction(NotifiCation notifiCation);

}
