package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Mapper
@Repository
public interface Usermapper {

	/**
	    * 根据用户id查询用户名是否存在，若存在，不允许注册
	 * @Param userID
	 * @return
	 */
	/*应用了ibtis框架*/
	@Select(value = "select * from user u where u.userID=#{userID}")
	@Results
			({@Result(property = "userID",column = "userID"),
			  @Result(property = "userName",column = "userName"),
	          @Result(property = "userPW",column = "userPW")})
	User findUserByName(@Param("userID") String userID);

	/**
	    * 注册  插入一条user记录
     * @param user
     * @return
     */
    @Insert("insert into user values(#{userID}, #{userPW}, #{userName})")
    @Options(useGeneratedKeys = true, keyProperty = "userID", keyColumn = "userID")
    void regist(User user);

    
    /**
                  * 登录
     * @param user
     * @return
     */
    @Select("select u.userName from user u where u.userID = #{userID} and u.userPW = #{userPW}")
    String login(User user);
}
