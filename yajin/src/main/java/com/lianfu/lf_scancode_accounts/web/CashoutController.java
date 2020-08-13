package com.lianfu.lf_scancode_accounts.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lianfu.lf_scancode_accounts.model.CashoutOrder;
import com.lianfu.lf_scancode_accounts.model.CashoutQueryVO;
import com.lianfu.lf_scancode_accounts.model.ResponseModel;
import com.lianfu.lf_scancode_accounts.model.Shop;
import com.lianfu.lf_scancode_accounts.service.CashoutService;
import com.lianfu.lf_scancode_accounts.utils.HttpUtil;
import com.lianfu.lf_scancode_accounts.utils.WeixinOauth2Token;
import com.lianfu.lf_scancode_accounts.web.response.BuildResponse;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
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

//@Api(tags = "提现相关接口")
@Controller
@RequestMapping("/cashout")
public class CashoutController {
    private final Logger logger = LoggerFactory.getLogger(CashoutController.class);

    @Autowired
    private CashoutService service;

    private static final String LOGIN_URL = "http://core.lianfuteam.com/lfpay/public/api/lwechat/getMerId";

    @RequestMapping("/code")
    public void code(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx028fe0fdaf2811a0&redirect_uri=http://wuye.lianfubiz.com/lf_scancode_accounts/cashout/index&response_type=code&scope=snsapi_base#wechat_redirect");
    }

    @RequestMapping(value = "/index")
    public String index(String code, HttpServletRequest request, Model model) {
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
            String appSecret = "fc4d995c3d761fdfbdb4ce142514ec95";
            String accessToken = null;
            String openId = null;

            openId = "oN5Ye1KwftnOviBuqVBpUo5TDO4U";
            String nickname = "发财";
            String headimgurl = "http://thirdwx.qlogo.cn/mmopen/Zb2yOJenIGiaaRdgugKzzd8NNibkzm77zEfOsSXsIibTicMphmMXosAYGLlB28VuYq9v6zYrEVUpicF3ur1PibctBal1YrGibaXVejt/132";
            
          /*  if (!"authdeny".equals(code)) {
               JSONObject json = WeixinOauth2Token.getOauth2AccessToken(appid, appSecret, code); // 获取网页授权access_token
                accessToken = json.getString("access_token");                               // 网页授权接口访问凭证
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
                return "cashout_index.html";
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


    /*
        @ApiOperation("返回用户当前账户余额等信息")
    */
    @RequestMapping(value = "getShopConfig", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel getShopConfig(HttpServletRequest request) {
        System.out.println("进入getShopConfig");
        HttpUtil httpUtil = new HttpUtil();
        HttpSession session = request.getSession();
        String openid = (String) session.getAttribute("openid");
        String nickname = (String) session.getAttribute("nickname");
        String headimgurl = (String) session.getAttribute("headimgurl");

//        String openid = "oN5Ye1Od5UvSj_ax8gpjzMKzSfb0";
//        String nickname = "壹度。";
//        String headimgurl = "http://thirdwx.qlogo.cn/mmopen/edHcbr54DPqV3Jcd0tnibtFia47aBlV3bjU1v13so5Z1AzBdjja6z5NwNTSAGXKLJdxibnULXQJEPejnMIBGh0jRD2wK42GBG9e/132";

        String url = LOGIN_URL + "?open_id=" + openid + "&nick_name=" + nickname + "&avatar_url=" + headimgurl;
        logger.info("发送登录查询请求：" + url);
        String res1 = httpUtil.doPost(url, "");
        logger.info("登录查询请求返回：" + res1);
        JSONObject resJson1 = JSONObject.parseObject(res1);

        if (resJson1.getBoolean("success")) {
            JSONArray mersArr = resJson1.getJSONArray("mers");
            int[] mersList;
            if (mersArr != null) {
                mersList = new int[mersArr.size()];
                for (int i = 0; i < mersArr.size(); i++) {
                    if (mersArr.getJSONObject(i).get("promiss").equals("2"))
                        mersList[i] = Integer.parseInt(mersArr.getJSONObject(i).get("mer_id").toString());
                }
            } else {
                mersList = new int[1];
                String mer_id = resJson1.getString("mer_id");
                if (resJson1.getString("promiss").equals("2"))
                    mersList[0] = (Integer.parseInt(mer_id));
            }
            CashoutQueryVO cashoutMessages = service.getCashoutMessages(mersList);
            session.setAttribute("shopConfigs", cashoutMessages.getShopConfigs());
            //------------------------------------
            session.setAttribute("merid",cashoutMessages.getCashoutmerid());
            session.setAttribute("mode",cashoutMessages.getCashoutmode());
            session.setAttribute("moy",cashoutMessages.getCashoutold_balance());
            session.setAttribute("meridmoney",cashoutMessages.getCashoutmeridmoney());
            session.setAttribute("newmoney",cashoutMessages.getNewmoney());
            return BuildResponse.success(cashoutMessages);
        } else {
            return BuildResponse.error("您暂无浏览权限");
        }
    }

    /* @ApiOperation("用户提现")
     @ApiImplicitParams({
             @ApiImplicitParam(name = "money", value = "提现金额", required = true),
     })*/
    @ResponseBody
    @RequestMapping(value = "/cashoutMoney", method = RequestMethod.GET)
    public ResponseModel cashout(String money,String name,String number, HttpServletRequest request, Model model) {
        logger.info("cashoutMoney入参money:" + money);
        if (money == null || money.isEmpty()) {
            return BuildResponse.error("提现失败");
        }
        HttpSession session = request.getSession();
        String openid = (String) session.getAttribute("openid");
        String nickname = (String) session.getAttribute("nickname");
        String cashoutoldbalance=(String) session.getAttribute("moy");
        String cashoutmerid=(String) session.getAttribute("merid");
        String cashoutmode=(String) session.getAttribute("mode");
        String meridmoney=(String) session.getAttribute("meridmoney");
        String newmoney=(String) session.getAttribute("newmoney");
//        String openid = "oN5Ye1Od5UvSj_ax8gpjzMKzSfb0";
//        String nickname = "壹度。";
        List<Shop> cashoutMessages = (List<Shop>) session.getAttribute("shopConfigs");
        CashoutOrder cashoutOrder = new CashoutOrder();
        cashoutOrder.setCashoutMoney(money);
        cashoutOrder.setCashoutOpenId(openid);
        //---------------------
        cashoutOrder.setCashoutnickName(nickname);
        cashoutOrder.setCashoutoldbalance(cashoutoldbalance);
        cashoutOrder.setCashoutmerid(cashoutmerid);
        cashoutOrder.setCashoutmode(cashoutmode);
        cashoutOrder.setCashoutmeridmoney(meridmoney);
        cashoutOrder.setCashoutnewmoney(newmoney);
        try {
            service.placeOrder(cashoutOrder, cashoutMessages);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
            return BuildResponse.error("提现失败");
        }
        return BuildResponse.success("提现成功");
    }

    @RequestMapping("gotoList")
    public String gotoList() {
        return "list.html";
    }

    @RequestMapping("gotoErrorPage")
    public String gotoErrorPage(Model model) {
        model.addAttribute("err_code", "403");
        model.addAttribute("err_msg", "您暂无浏览权限");
        return "pages_error.html";
    }

    @ResponseBody
    /*@ApiOperation("返回用户提现记录")*/
    @RequestMapping(value = "/getCashoutOrders", method = RequestMethod.GET)
    public ResponseModel getCashoutOrders(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String openid = (String) session.getAttribute("openid");
//        String openid = "oN5Ye1Od5UvSj_ax8gpjzMKzSfb0";
        List<CashoutOrder> cashoutOrders = service.getCashoutOrders(openid);
        logger.info("getCashoutOrders出参" + cashoutOrders.toString());
        return BuildResponse.success(cashoutOrders);
    }






}
