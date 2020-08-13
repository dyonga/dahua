package com.bhyh.dao.mapper;

import com.bhyh.model.vo.ImgResSummaryVO;

public interface ImgResSummaryVOMapper {
	int deleteByPrimaryKey(Integer id);

    int insert(ImgResSummaryVO vo);

	
    int insertSelective(ImgResSummaryVO vo);

    ImgResSummaryVO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImgResSummaryVO vo);

    int updateByPrimaryKey(ImgResSummaryVO vo);
}
