package cn.study.mapper;

import cn.study.entity.TtMail;

import java.util.List;

public interface TtMailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tt_mail
     *
     * @mbggenerated Mon Dec 06 22:18:03 CST 2021
     */
    int insert(TtMail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tt_mail
     *
     * @mbggenerated Mon Dec 06 22:18:03 CST 2021
     */
    int insertSelective(TtMail record);


    List<Object> getAll();
}