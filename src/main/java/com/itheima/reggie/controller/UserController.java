package com.itheima.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 1. @ClassDescription:
 * 2. @author: ZhangL
 * 3. @date: 2022年09月16日 20:01
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        String phone = user.getPhone();

        if(!StringUtils.isEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
//            SMSUtils.sendMessage("", "", phone, code);
            session.setAttribute(phone, code);
            log.info("手机验证码：{}", code);
            return R.success("手机验证码发送成功");
        }
        return R.error("手机短信验证码发送失败");
    }

    /**
     * 获取验证码后的登录方法
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpSession session){

        //获取手机号和验证码
        String phone = user.get("phone").toString();
        String code = user.get("code").toString();

        Object codeInSession = session.getAttribute(phone);
        if(codeInSession == null || !codeInSession.equals(code)){
            return R.error("登陆失败");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User one = userService.getOne(queryWrapper);
        if(one == null){
            one = new User();
            one.setPhone(phone);
            one.setStatus(1);
            userService.save(one);
        }
        session.setAttribute("user", one.getId());
        return R.success(one);
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){

//        Long currentId = BaseContext.getCurrentId();
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }

}
