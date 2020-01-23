package com.example.myboot07.service.impl;

import com.example.myboot07.bean.Tag;
import com.example.myboot07.dao.ITagDao;
import com.example.myboot07.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements ITagService {

    @Autowired
    private ITagDao tagDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Tag> queryTagAll() {

        List<Tag> tags = tagDao.queryTagAll();

        if(tags == null){
            // 这里应该抛出异常
            return null;
        }else{
            // 正常返回
            return tags;
        }

    }
}
