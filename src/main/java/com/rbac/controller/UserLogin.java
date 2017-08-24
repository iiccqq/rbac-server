package com.rbac.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Producer;
import com.rbac.constant.Constant;
import com.rbac.dao.SysUserDao;
import com.rbac.dao.SysUserSessionDao;
import com.rbac.dao.SysUserTokenrDao;
import com.rbac.model.R;
import com.rbac.model.SysUser;
import com.rbac.model.SysUserSession;
import com.rbac.model.SysUserToken;

@RestController
public class UserLogin {

	@Autowired
	private Producer producer;

	@Resource
	private SysUserDao sysUserDao;

	@Resource
	private SysUserTokenrDao sysUseTokenrDao;

	@Resource
	private SysUserSessionDao sysUserSessionDao;

	//12小时后过期
	private final static int TOKEN_EXPIRE = 3600 * 12;

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 保存图片信息

		SysUserSession sysUserSession = new SysUserSession();
		sysUserSession.setSessionid(request.getSession().getId());

		sysUserSession.setCaptcha(text);
		Date updateTime = new Date();
		sysUserSession.setUpdateTime(updateTime);
		Date expireTime = new Date(updateTime.getTime() + 60000);
		sysUserSession.setExpireTime(expireTime);
		sysUserSessionDao.save(sysUserSession);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletResponse response,
			HttpServletRequest request) throws IOException {
		String sessionid = request.getSession().getId();
		SysUserSession sysUserSession = sysUserSessionDao.queryObject(sessionid);

		if (sysUserSession == null || !captcha.equalsIgnoreCase(sysUserSession.getCaptcha())) {
			return R.error(Constant.ERROR_CODE_COMMON_ERROR, "验证码不正确");
		}
		
		// 用户信息
		SysUser user = sysUserDao.queryByUserName(username);
		// 账号不存在、密码错误
		if (user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
			return R.error(Constant.ERROR_CODE_COMMON_ERROR, "账号或密码不正确");
		}
		// 账号锁定
		if (user.getStatus() == 0) {
			return R.error(Constant.ERROR_CODE_COMMON_ERROR, "账号已被锁定,请联系管理员");
		}

		// 生成token，并保存到数据库
		SysUserToken sut = new SysUserToken();
		sut.setUserId(user.getUserId());
		sut.setUpdateTime(new Date());
		Date expireTime = new Date(new Date().getTime() + TOKEN_EXPIRE * 1000);	
		sut.setExpireTime(expireTime);
		String token = UUID.randomUUID().toString().toLowerCase();
		sut.setToken(token);
		sysUseTokenrDao.saveOrUpdate(sut);
		return R.ok().put("token", token).put("expire", Constant.EXPIRE).put("userid", user.getUserId());

	}

	/**
	 * 退出
	 */
	@RequestMapping(value = "/sys/users/{userid}/logout", method = RequestMethod.POST)
	public R logout(@PathVariable Long userid) {
		sysUseTokenrDao.deleteTokenById(userid);
		return R.ok();
	}

}
