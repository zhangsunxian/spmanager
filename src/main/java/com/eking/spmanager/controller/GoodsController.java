package com.eking.spmanager.controller;

import com.eking.spmanager.dao.GoodsIdxDAO;
import com.eking.spmanager.dao.GoodsTypeDAO;
import com.eking.spmanager.Utils.Box;
import com.eking.spmanager.Utils.Tools;
import com.eking.spmanager.domain.Goods;
import com.eking.spmanager.domain.GoodsIndex;
import com.eking.spmanager.domain.GoodsType;
import com.eking.spmanager.service.GoodsService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Yulin.Wang
 * @Date 2019-01-30
 * @Description  商品详情列表
 **/

@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static final int G_PAGE = 0;
    private static final int G_SIZE = 3;
    private Integer gType = -1;

    @Autowired
    GoodsService goodsService;

    @Autowired
    GoodsTypeDAO gtDAO;

    @Autowired
    GoodsIdxDAO gIdxDAO;

    @Autowired
    Tools utils;

    public List<Box> makeList(Integer type) {
        List<Box> list = new ArrayList<>();
        List<Goods> glist;

        if (type == -1) {
            glist = goodsService.findAllGoods();
        } else {
            GoodsType gt = gtDAO.findById(type).get();
            glist = goodsService.findByType(gt.getName());
        }

        for (Goods g: glist) {
            Integer count = gIdxDAO.findByGoodsid(g.getId()).getCount();
            list.add(new Box(g, count));
        }
        return list;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String initGoodsList(ModelMap map) {
        gType = -1;
        map.addAttribute("gtList", gtDAO.findAll());
        map.addAttribute("datas", utils.convertPage(G_PAGE, G_SIZE, makeList(gType)));
        return "goodsList";
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String freshList(ModelMap map) {
        //map.addAttribute("goodsList", glist);
        return "goodsList::goodsTable";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public String postList(ModelMap map, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        try {
            map.addAttribute("datas", utils.convertPage(page, G_SIZE, makeList(gType)));
            return "goodsList::goodsTable";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/bytype", method = RequestMethod.POST)
    public String getListByType(ModelMap map,
                                @RequestParam(value = "type", defaultValue = "-1") Integer type) {
        gType = type;
        return "Success";
    }

    /** 模糊搜索商品 **/
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchGoods(ModelMap map, @RequestParam(value = "keyword") String key) {

        return "ok";
    }

    @RequestMapping(value = "/addGM", method = RequestMethod.GET)
    public String getGTModal(ModelMap map) {
        map.addAttribute("gtList", gtDAO.findAll());
        return "modalAddGoods";
    }

    @RequestMapping(value = "/addCart", method = RequestMethod.GET)
    public String getCartModal(ModelMap map, @RequestParam(value = "gid") Integer gid) {

        map.addAttribute("goods", goodsService.findById(gid));
        return "modalAddCart";
    }

    @ResponseBody
    @RequestMapping(value = "/addCart", method = RequestMethod.POST)
    public String postCartModal(ModelMap map, @RequestParam(value = "gid") Integer gid,
                                @RequestParam(value = "amount") Integer amount,
                                @RequestParam(value = "price") double price,
                                HttpServletResponse response) {
        /**URLEncoder.encode(value,"utf-8") 解决中文乱码问题**/
        Cookie cookie = new Cookie("cart" + gid, gid + "-" + amount + "-" + price);
        cookie.setPath("/"); // 跨域 记得 setDomain
        response.addCookie(cookie);
        return "SUCCESS";
    }

    @ResponseBody
    @RequestMapping(params = "add", method = RequestMethod.POST)
    public String postGoods(@RequestBody String str, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ERROR";
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(str);
            Goods goods = mapper.convertValue(node.get("Goods"), Goods.class);
            Integer amount = mapper.convertValue(node.get("Amount"), Integer.class);

            GoodsType gt = gtDAO.findById(Integer.valueOf(goods.getType())).get();
            goods.setType(gt.getName());
            goodsService.addGoods(goods);

            GoodsIndex goodsIndex = new GoodsIndex();
            goodsIndex.setGoodsid(goodsService.findByName(goods.getName()).getId());
            goodsIndex.setCount(amount);
            gIdxDAO.save(goodsIndex);
            return "add " + goods.toString() + " Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: CREATE GOODS FAILED!";
        }
    }

}
