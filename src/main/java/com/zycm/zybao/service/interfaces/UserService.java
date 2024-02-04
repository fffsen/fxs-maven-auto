package com.zycm.zybao.service.interfaces;

import com.zycm.zybao.model.vo.UserVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserService {

	/** 分页查询系统用户信息
	* @Title: selectPage
	* @Description:
	* @author sy
	* @param @param map
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String, Object> selectPage(Map<String, Object> map,Integer pageSize,Integer page);

	/** 新加系统用户
	* @Title: insert
	* @Description:
	* @author sy
	* @param @param userModel
	* @return void
	*
	*/
	void insert(UserVo userVo);

	/** 根据用户id查询信息
	* @Title: selectByPrimaryKey
	* @Description:
	* @author sy
	* @param @param uid
	* @param @return
	* @return Map<String,Object>
	*
	*/
	Map<String,Object> selectByPrimaryKey(Integer uid);

	Map<String,Object> selectByOpenId(String openId);

	/** 验证用户名是否已存在
	* @Title: selectByUserName
	* @Description:
	* @author sy
	* @param @param userName
	* @param @return
	* @return List<Map<String,Object>>
	*
	*/
	Map<String,Object> selectByUserName(Map<String,Object> param);

	/** 根据用户id 修改用户信息
	* @Title: updateByPrimaryKey
	* @Description:
	* @author sy
	* @param @param userModel
	* @return void
	*
	*/
	void updateByPrimaryKey(UserVo userVo);

	/**
	* @Title: updateByPrimaryKey2
	* @Description: 只修改基本信息   不修改组配置、角色信息
	* @param userVo    参数
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateByPrimaryKey2(UserVo userVo);

	/** 修改用户密码  用户修改
	* @Title: updatePwd
	* @Description:
	* @author sy
	* @param @param param
	* @return void
	*
	*/
	void updatePwd(Integer uid,String oldPassword,String newPassword) throws Exception;

	/** 修改用户密码  管理员重置密码
	* @Title: updatePwd
	* @Description:
	* @author sy
	* @param @param param
	* @return void
	*
	*/
	void adminUpdatePwd(Integer uid,Integer operatorId,String newPassword) throws Exception;

	/**  修改用户登录状态
	* @Title: updateStatus
	* @Description:
	* @author sy
	* @param @param param
	* @return void
	*
	*/
	void updateStatus(Integer uid,Integer userStatus,Date date);

	/** 逻辑删除用户信息
	* @Title: updateIsDelete
	* @Description:
	* @author sy
	* @param @param param
	* @return void
	*
	*/
	void updateIsDelete(Integer[] uids);

	/** 后台登录
	* @Title: backLogin
	* @Description:
	* @author sy
	* @param @param userName
	* @param @param pwd
	* @param @return
	* @return UserModel
	*
	*/
	Map<String, Object> backLogin(String userName, String pwd);

	/**查询用户能操作的终端组
	 * @param uid
	 * @return
	 */
	List<Map<String, Object>> selectGroupByUid(Integer uid);

	/**
	* @Title: selectUsersByUgroupId
	* @Description: 根据用户id 查询与用户同用户组的多个用户id
	* @param uid
	* @return    参数
	* @author sy
	* @throws
	* @return String    返回类型  多个用户id 用逗号隔开的
	*
	*/
	String selectUsersByUgroupId(Integer uid);

	List<Map<String,Object>> selectByCondition(Map<String,Object> param);

	/**
	* @Title: updateWarnNotice
	* @Description: 修改用户预警通知状态
	* @author sy
	* @throws
	* @return void    返回类型
	*
	*/
	void updateWarnNotice(Integer uid,Integer warnNotice );
}
