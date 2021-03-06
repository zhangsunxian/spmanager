package com.eking.spmanager.implement;

import com.eking.spmanager.dao.OrderDetailDAO;
import com.eking.spmanager.domain.OrderDetail;
import com.eking.spmanager.service.OrderDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Yulin.Wang
 * @Date 2019-02-14
 * @Description
 **/

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static String entity = "ODetail";

    @Autowired
    OrderDetailDAO orderDetailDAO;

    @Transactional
    public void addOrderDetail(OrderDetail od) {
        orderDetailDAO.save(od);
        LOGGER.info("[eKing log]: {}  {}: - {}", entity, "NEW ", od.toString());
    }

    @Transactional
    public List<OrderDetail> findByOrderId(Integer id) {
        LOGGER.info("[eKing log]: {}  {}: - {}", entity, "FIND BY OID ", id.toString());
        return orderDetailDAO.findByOrderId(id);
    }

    @Transactional
    public void update(OrderDetail order) {
        orderDetailDAO.save(order);
        LOGGER.info("[eKing log]: {}  {}: - {}", entity, "UPDATE ", order.toString());
    }

}
