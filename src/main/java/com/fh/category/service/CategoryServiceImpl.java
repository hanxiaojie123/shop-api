package com.fh.category.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fh.category.mapper.CategoryMapper;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public ServerResponse queryList() {
        boolean exist = RedisUtil.exist(SystemConstant.REDIS_CATEGORY_KEY);
        if (exist){
            String gets = RedisUtil.get(SystemConstant.REDIS_CATEGORY_KEY);
            List<Map> mapList = JSONArray.parseArray(gets,Map.class);
            return ServerResponse.success(mapList);
        }
        List<Map<String ,Object>>  allList =   categoryMapper.queryList();
        List<Map<String ,Object>>  parentList = new ArrayList<Map<String, Object>>();

        for (Map  map : allList) {
            if(map.get("pid").equals(0)){
                parentList.add(map);
            }
        }
        selectChildren(parentList,allList);
        String toJSONString = JSONArray.toJSONString(parentList);
        //吧查询出来的数据放入到缓存中
        RedisUtil.set(SystemConstant.REDIS_CATEGORY_KEY,toJSONString);
        return ServerResponse.success(parentList);
    }

    public void selectChildren(List<Map<String ,Object>>  parentList, List<Map<String ,Object>>  allList){
        for (Map<String, Object> pmap : parentList) {
            List<Map<String ,Object>>  childrenList = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> amap : allList) {
                if(pmap.get("id").equals(amap.get("pid"))){
                    childrenList.add(amap);
                }
            }
            if(childrenList!=null && childrenList.size()>0){
                pmap.put("children",childrenList);
                selectChildren(childrenList,allList);
            }

        }
    }

}
