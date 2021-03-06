package com.eking.spmanager.implement;

import com.eking.spmanager.domain.Permission;
import com.eking.spmanager.dao.PermissionDAO;
import com.eking.spmanager.service.PmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Yulin.Wang
 * @Date 2019-01-29
 * @Description
 **/

@Service
public class PmsServiceImpl implements PmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmsServiceImpl.class);
    private static String entity = "PERMISSION";

    @Autowired
    private PermissionDAO pd;

    @Override
    public Permission findByGroupid(Integer id) {
        LOGGER.info("[eKing log]: {}  {}: - {}", entity, "FIND BY GPID", id.toString());
        return pd.findByGroupid(id);
    }

    @Transactional
    @Override
    public void addPermission(Permission p) {
        pd.save(p);
        LOGGER.info("[eKing log]: {}  {}: - {}", entity, "NEW", p.toString());
    }

}
