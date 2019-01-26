package com.eking.spmanager.service;

import com.eking.spmanager.entity.UserGroup;

import java.util.List;

/**
 * @Author Yulin.Wang
 * @Date 2019-01-23
 * @Description
 **/

public interface UserGroupService {
    public void addGroup(UserGroup userGroup);
    public UserGroup deleteById(Integer id);
    public UserGroup findById(Integer id);
    public List<UserGroup> findAllGroup();
}
