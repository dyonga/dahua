package com.bhyh.dao.mapper;


import com.bhyh.model.vo.VideoDataVO;
//@Mapper
public interface VideoDataVOMapper {
   
	int deleteByPrimaryKey(Integer id);

    int insert(VideoDataVO record);

    int insertSelective(VideoDataVO record);

    VideoDataVO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VideoDataVO record);

    int updateByPrimaryKey(VideoDataVO record);

}
