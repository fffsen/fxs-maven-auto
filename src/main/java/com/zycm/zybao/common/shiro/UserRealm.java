package com.zycm.zybao.common.shiro;

import com.zycm.zybao.common.utils.MD5;
import com.zycm.zybao.model.entity.ModuleFunctionModel;
import com.zycm.zybao.model.entity.RolesModel;
import com.zycm.zybao.service.interfaces.ModuleService;
import com.zycm.zybao.service.interfaces.RolesService;
import com.zycm.zybao.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Slf4j
public class UserRealm extends AuthorizingRealm {

	//private static final Logger logger = Logger.getLogger(UserRealm.class);
	// 用户对应的角色信息与权限信息都保存在数据库中，通过UserService获取数据
	private UserService userService;

    private RolesService rolesService;

    private ModuleService moduleService;

	private static final String OR_OPERATOR = " or ";
	private static final String AND_OPERATOR = " and ";
	private static final String NOT_OPERATOR = "not ";
    /**
     * 提供用户信息返回权限信息
     * 该方法的调用时机为需授权资源被访问时
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 根据用户名查询当前用户拥有的角色
        List<RolesModel> roles = rolesService.selectByUserName(username);
        Set<String> roleNames = new HashSet<String>();
        if(roles.size() > 0){
        	Integer[] roleId = new Integer[roles.size()];
        	int t = 0;
            for (RolesModel role : roles) {
                roleNames.add(role.getRoleName());
                roleId[t] = role.getRoleId();
                t++;
            }
            // 将角色名称提供给info
            authorizationInfo.setRoles(roleNames);

            // 根据用户名查询当前用户权限
            List<ModuleFunctionModel> permissions = moduleService.selectByRoleIds(roleId);
            Set<String> permissionNames = new HashSet<String>();
            for (ModuleFunctionModel permission : permissions) {
                permissionNames.add(permission.getFunctionCode());
            }
            // 将权限名称提供给info
            authorizationInfo.setStringPermissions(permissionNames);
            log.info("加载用户【"+username+"】,角色【"+roleNames.size()+"】个，权限【"+permissionNames.size()+"】个");
        }else{
        	log.error("加载用户【"+username+"】的角色信息为空");
        }

        return authorizationInfo;
    }

    /**
     * 提供账户信息返回认证信息
     * 该方法的调用时机为执行Subject.login()时
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken tk = (UsernamePasswordToken) token;
		log.debug("验证当前Subject时获取到token为" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
		// 通过表单接收的用户名
		String username  = tk.getUsername();
		if(StringUtils.isNotBlank(username)){

			Map<String,Object> user = userService.backLogin(username, String.valueOf(tk.getPassword()));
			if (user != null) {
				SimpleAuthenticationInfo SimpleAuthenticationInfo = new SimpleAuthenticationInfo(username,MD5.MD5(String.valueOf(tk.getPassword())), getName());
				Integer uid = Integer.parseInt(user.get("uid").toString());
				String sameGroupUserId = userService.selectUsersByUgroupId(uid);
				//把用户的其他属性数据加入session
				Session sess = SecurityUtils.getSubject().getSession();
				sess.setAttribute("uGroupId",user.get("uGroupId").toString());
				sess.setAttribute("userId",uid);
				sess.setAttribute("sameGroupUserId",sameGroupUserId);
				log.debug("用户属性绑定的sessionid:"+sess.getId());
				log.debug("认证时，根据【"+username+"】、【"+ MD5.MD5AndMore(String.valueOf(tk.getPassword()))+"】、【"+getName()+"】认证通过");
				return SimpleAuthenticationInfo;
	        }else{
	        	log.error("认证时，根据【"+username+"】、【"+String.valueOf(tk.getPassword())+"】查不到用户信息");
	        }
		}else{
			log.error("认证时，用户名为空【"+username+"】");
		}

    	return null;
    }

    /**
     * 支持or and not 关键词  不支持and or混用
     * @param principals
     * @param permission
     * @return
     */
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        if(permission.contains(OR_OPERATOR)) {
            String[] permissions = permission.split(OR_OPERATOR);
            for(String orPermission : permissions) {
                if(isPermittedWithNotOperator(principals, orPermission)) {
                    return true;
                }
            }
            return false;
        } else if(permission.contains(AND_OPERATOR)) {
            String[] permissions = permission.split(AND_OPERATOR);
            for(String orPermission : permissions) {
                if(!isPermittedWithNotOperator(principals, orPermission)) {
                    return false;
                }
            }
            return true;
        } else {
            return isPermittedWithNotOperator(principals, permission);
        }
    }

    private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
        if(permission.startsWith(NOT_OPERATOR)) {
            return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
        } else {
            return super.isPermitted(principals, permission);
        }
    }

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setRolesService(RolesService rolesService) {
		this.rolesService = rolesService;
	}

	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}


}
