package com.bhyh.dao.mapper;

import com.bhyh.model.vo.ImageDataVO;
//@Mapper
public interface ImageDataVOMapper {
   
	int deleteByPrimaryKey(Integer id);

    int insert(ImageDataVO record);

    int insertSelective(ImageDataVO record);

    ImageDataVO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImageDataVO record);

    int updateByPrimaryKey(ImageDataVO record);

}
