package com.lianfu.lf_scancode_accounts.web;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lianfu.lf_scancode_accounts.model.CashoutOrder;
import com.lianfu.lf_scancode_accounts.model.Lf_gl;
import com.lianfu.lf_scancode_accounts.model.ResponseModel;
import com.lianfu.lf_scancode_accounts.service.CashoutService;
import com.lianfu.lf_scancode_accounts.utils.HttpUtil;
import com.lianfu.lf_scancode_accounts.web.response.BuildResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/checkcashout")
public class CheckController {

    private final Logger logger = LoggerFactory.getLogger(CashoutController.class);

    @Autowired
    private CashoutService service;

    private static final String LOGIN_URL = "http://core.lianfuteam.com/lfpay/public/api/lwechat/getMerId";

    @RequestMapping("/code")
    public void code(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx028fe0fdaf2811a0&redirect_uri=http://wuye.lianfubiz.com/lf_scancode_accounts/cashout/index&response_type=code&scope=snsapi_base#wechat_redirect");
    }

    @RequestMapping(value = "/index")
    public String index(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,String code, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String c = (String) session.getAttribute("code");
        String openid = (String) session.getAttribute("openid");
        logger.info("code =" + code + "c=" + c);
        logger.info(openid);
        if (c == null) {
            session.setAttribute("code", code);
        } else {
            logger.info("二次访问，直接返回");
            return "cashout_index.html";
        }
        try {
            HttpUtil httpUtil = new HttpUtil();
            String appid = "wx028fe0fdaf2811a0";
            String accessToken = null;
            String openId = null;

            openId = "oN5Ye1KwftnOviBuqVBpUo5TDO4U";
            String nickname = "发财";
            String headimgurl = "http://thirdwx.qlogo.cn/mmopen/Zb2yOJenIGiaaRdgugKzzd8NNibkzm77zEfOsSXsIibTicMphmMXosAYGLlB28VuYq9v6zYrEVUpicF3ur1PibctBal1YrGibaXVejt/132";

          /*  if (!"authdeny".equals(code)) {
               JSONObject json = WeixinOauth2Token.getOauth2AccessToken(appid, appSecret, code); // 获取网页授权access_token
                accessToken = json.getString("access_token");
            String appSecret = "fc4d995c3d761fdfbdb4ce142514ec95";                               // 网页授权接口访问凭证
                openId = json.getString("openid");
            }
            if (openid != null && "".equals(openid)) {
                openId = openid;
            }*/
            //获取access_token
          /* String res = httpUtil.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + appSecret);
           JSONObject resJson = JSONObject.parseObject(res);
            String access_token = resJson.getString("access_token");*/

            //获取用户头像和昵称
           /* String nickname = null;
            String headimgurl = null;
           if (access_token != null && !access_token.equals("")) {
                String userinfo = httpUtil.doGet("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + openId);
               JSONObject userinfoJson = JSONObject.parseObject(userinfo);
               nickname = userinfoJson.getString("nickname");
               headimgurl = userinfoJson.getString("headimgurl");
            }*/


            if (nickname != null && headimgurl != null && openId != null) {
                session.setAttribute("openid", openId);
                session.setAttribute("nickname", nickname);
                session.setAttribute("headimgurl", headimgurl);

                PageHelper.startPage(pageNum,5);
                List<CashoutOrder> list= service.getCashoutOrder();

                PageInfo<CashoutOrder> pageInfo = new PageInfo<CashoutOrder>(list);
                List<CashoutOrder> Lf_list= pageInfo.getList();
                model.addAttribute("lf_list",Lf_list);
                model.addAttribute("list",pageInfo);




                return "auditdeposit.html";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("err_code", "403");
            model.addAttribute("err_msg", "您暂无浏览权限");
            return "pages_error.html";
        }
        model.addAttribute("err_code", "400");
        model.addAttribute("err_msg", "无法获取openid");
        return "pages_error.html";
    }



    @RequestMapping(value = "getCashout",method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel show(String id){

        CashoutOrder cashoutOrder =service.getCashoutOrder(Integer.parseInt(id));
        System.out.println(cashoutOrder.getCashoutmode()+"----");
        String mode=service.getmode(Integer.parseInt(cashoutOrder.getCashoutmode()));

        JSONObject js= new JSONObject();
        String []name=new  String[cashoutOrder.getShop().size()];
        String []merid=new String[cashoutOrder.getShop().size()];
       /* String []money=new  String[cashoutOrder.getShop().size()];*/
        String []meridmoney=cashoutOrder.getCashoutmeridmoney().split(",");
        String []oldmoney=cashoutOrder.getCashoutoldbalance().split(",");
        System.out.println(cashoutOrder.getCashoutnewmoney());
        String []newmoney=cashoutOrder.getCashoutnewmoney().split(",");
         for (int i=0;i<=cashoutOrder.getShop().size()-1;i++){
             name[i]=cashoutOrder.getShop().get(i).getShopName();
             merid[i]=cashoutOrder.getShop().get(i).getMerId();
           /*  money[i]=cashoutOrder.getShop().get(i).getRemainderCash();*/
         }
         js.put("name",name);
         js.put("merid",merid);
         js.put("newmoney",newmoney);
         js.put("nickName",cashoutOrder.getCashoutnickName());
         js.put("meridmoney",meridmoney);
         js.put("oldbalance",oldmoney);
         js.put("CashoutMoney",cashoutOrder.getCashoutMoney());
         js.put("mode",cashoutOrder.getCashoutmode());
         js.put("date",cashoutOrder.getCashoutDate());
         js.put("modename",mode);

        return BuildResponse.success(js.toJSONString());
    }




}
