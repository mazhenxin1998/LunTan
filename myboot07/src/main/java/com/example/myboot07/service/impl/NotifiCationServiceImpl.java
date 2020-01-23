package com.example.myboot07.service.impl;

import com.example.myboot07.bean.NotifiCation;
import com.example.myboot07.bean.User;
import com.example.myboot07.dao.INotifiCationDao;
import com.example.myboot07.dto.NotifiCationDTO;
import com.example.myboot07.service.INotifiCationService;
import com.example.myboot07.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class NotifiCationServiceImpl  implements INotifiCationService {

    @Autowired
    private INotifiCationDao notifiCationDao;

    @Autowired
    private IUserService userService;


    @Override
    public NotifiCation queryNotifiCationByViewFunction(NotifiCation notifiCation) {

        NotifiCation notifiCation1 = notifiCationDao.queryNotifiCationByViewFunction(notifiCation);

        return notifiCation1;
    }

    @Override
    public Integer updateNotificationStatus(int type, User user, Integer id) {
        // user.getID 为接受者的id  id为outerid
        Integer count = notifiCationDao.updateNotificationStatus(type,user.getId(),id);
        if( count == 1 ){
            return count;
        }else{
            //  抛出异常
            return null;
        }
    }

    @Override
    public List<NotifiCation> queryNotificationByReceiver(Integer id) {

        List<NotifiCation> notifiCations = notifiCationDao.queryNotificationByReceiver(id);

        if(notifiCations.size() == 0 || notifiCations == null ){
            // 应该抛异常
            return null;
        }else{
            return notifiCations;
        }

    }

    @Override
    public void insertNotification(NotifiCation notifiCation) {

        Integer count = notifiCationDao.insertNotification(notifiCation);

        if(count != 1){
            //  如果查出的count不等于1 那么应该在这里抛出异常  以至于在controller中不用在做判断
        }
    }

}
