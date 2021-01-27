package org.dataochen.mapper;

import org.dataochen.entity.Demo;
import org.dataochen.entity.DemoExample;

import java.util.List;

/**
 * Created by Mybatis Generator
 *
 * @author Mybatis Generator
 * @date 2021-01-26
 */
public interface DemoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Demo record);

    int insertSelective(Demo record);

    List<Demo> selectByExample(DemoExample example);

    Demo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Demo record);

    int updateByPrimaryKey(Demo record);
}