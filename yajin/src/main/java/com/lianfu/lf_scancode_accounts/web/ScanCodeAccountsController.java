package com.lianfu.lf_scancode_accounts.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lianfu.lf_scancode_accounts.utils.HttpUtil;
import com.lianfu.lf_scancode_accounts.utils.WeixinOauth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ScanCodeAccountsController {

    private final Logger logger = LoggerFactory.getLogger(ScanCodeAccountsController.class);

    private static final String LOGIN_URL = "http://core.lianfuteam.com/lfpay/public/api/lwechat/getMerId";
    private static final String SELECT_URL = "http://core.lianfuteam.com/lfpay/public/api/lwechat/queryOrders";
    //    private static final String SELECT_URL = "http://192.168.0.54/public/api/lwechat/queryOrders";
    private static final String REFUND_URL = "http://core.lianfuteam.com/lfpay/public/api/lwechat/reject";

    @RequestMapping("/code")
    public void code(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx028fe0fdaf2811a0&redirect_uri=http://wuye.lianfubiz.com/lf_scancode_accounts/index&response_type=code&scope=snsapi_base#wechat_redirect");
    }

    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request, HttpSession session) {
        try {
            HttpUtil httpUtil = new HttpUtil();
            String appid = "wx028fe0fdaf2811a0";
            String appSecret = "fc4d995c3d761fdfbdb4ce142514ec95";
            String code = request.getParameter("code");
            String accessToken = null;
            String openId = null;

           /* openId = "oN5Ye1Od5UvSj_ax8gpjzMKzSfb0";
            String nickname = "壹度。";
            String headimgurl = "http://thirdwx.qlogo.cn/mmopen/edHcbr54DPqV3Jcd0tnibtFia47aBlV3bjU1v13so5Z1AzBdjja6z5NwNTSAGXKLJdxibnULXQJEPejnMIBGh0jRD2wK42GBG9e/132";*/

           if (!"authdeny".equals(code)) {
                JSONObject json = WeixinOauth2Token.getOauth2AccessToken(appid, appSecret, code); // 获取网页授权access_token
                accessToken = json.getString("access_token");                              // 网页授权接口访问凭证
                openId = json.getString("openid");                                         // 用户标识
            }
            //获取access_token*/
            String res = httpUtil.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + appSecret);
            JSONObject resJson = JSONObject.parseObject(res);
            String access_token = resJson.getString("access_token");
            //获取用户头像和昵称
           String nickname = null;
            String headimgurl = null;
            if (access_token != null && !access_token.equals("")) {
                String userinfo = httpUtil.doGet("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + access_token + "&openid=" + openId);
                JSONObject userinfoJson = JSONObject.parseObject(userinfo);
                nickname = userinfoJson.getString("nickname");
                headimgurl = userinfoJson.getString("headimgurl");
            }



            if (nickname != null && headimgurl != null && openId != null) {

       /* String openId = "oN5Ye1Od5UvSj_ax8gpjzMKzSfb0";
        String nickname = "壹度。";
        String headimgurl = "http://thirdwx.qlogo.cn/mmopen/edHcbr54DPqV3Jcd0tnibtFia47aBlV3bjU1v13so5Z1AzBdjja6z5NwNTSAGXKLJdxibnULXQJEPejnMIBGh0jRD2wK42GBG9e/132";*/

                session.setAttribute("openid", openId);

                String url = LOGIN_URL + "?open_id=" + openId + "&nick_name=" + nickname + "&avatar_url=" + headimgurl;
                logger.info("发送登录查询请求：" + url);
                String res1 = httpUtil.doPost(url, "");
                logger.info("登录查询请求返回：" + res1);
                JSONObject resJson1 = JSONObject.parseObject(res1);


                if (resJson1.getBoolean("success")) {
                    JSONArray mersArr = resJson1.getJSONArray("mers");
                    JSONArray mers = new JSONArray();
                    if (mersArr != null) {
                        mers = mersArr;
                    } else {
                        String mer_id = resJson1.getString("mer_id");
                        String mer_name = resJson1.getString("mer_name");
                        JSONObject mer = new JSONObject();
                        mer.put("mer_id", mer_id);
                        mer.put("mer_name", mer_name);
                        mers.add(mer);
                    }
                    model.addAttribute("mers", mers.toJSONString());
                    return "index.html";
                } else {
                    model.addAttribute("err_code", "403");
                    model.addAttribute("err_msg", "您暂无浏览权限");
                    return "pages_error.html";
                }
            } else {
                model.addAttribute("err_code", "403");
                model.addAttribute("err_msg", "您暂无浏览权限");
                return "pages_error.html";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("err_code", "500");
            model.addAttribute("err_msg", "服务器错误，请联系管理员");
            return "pages_error.html";
        }
    }

    @ResponseBody
    @RequestMapping("/getAccountByDate")
    public String getAccountByDate(String mer_id, String start_date, String end_date, String page) {
        String param = "";
        if (start_date.equals("") || end_date.equals("")) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String dateStr = formatter.format(date);
            start_date = dateStr + " 00:00:00";
            end_date = dateStr + " 23:59:59";
            param = "mer_id=" + mer_id + "&start_date=" + start_date + "&end_date=" + end_date + "&page=" + page;
        } else {
            start_date = start_date + " 00:00:00";
            end_date = end_date + " 23:59:59";
            param = "mer_id=" + mer_id + "&start_date=" + start_date + "&end_date=" + end_date + "&page=" + page;
        }
        logger.info("发送查询请求：" + param);
        HttpUtil httpUtil = new HttpUtil();
        String res = httpUtil.doPost(SELECT_URL, param);
        logger.info("查询返回：" + res);
        return res;
    }

    @ResponseBody
    @RequestMapping("/getAllAccount")
    public String getAllAccount(String mers, String page, String startDate, String endDate) {
        String param = "";
        String start_date;
        String end_date;
        if (startDate == "" && endDate == "") {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String dateStr = formatter.format(date);
            start_date = dateStr + " 00:00:00";
            end_date = dateStr + " 23:59:59";
        } else {
            start_date = startDate + " 00:00:00";
            end_date = endDate + " 23:59:59";
        }
        param = "mers=" + mers + "&start_date=" + start_date + "&end_date=" + end_date + "&page=" + page;
        logger.info("发送查询请求：" + param);
        HttpUtil httpUtil = new HttpUtil();
        String res = httpUtil.doPost(SELECT_URL, param);
        logger.info("查询返回：" + res);
        return res;
    }

    @ResponseBody
    @RequestMapping("/refund")
    public String refund(HttpSession session, String pay_amount, String mer_id, String order_no) {
        String openid = (String) session.getAttribute("openid");
        String param = "pay_amount=" + pay_amount + "&mer_id=" + mer_id + "&order_no=" + order_no + "&open_id=" + openid;
        logger.info("发送退款请求：" + param);
        HttpUtil httpUtil = new HttpUtil();
        String res = httpUtil.doPost(REFUND_URL, param);
        logger.info("退款请求返回：" + res);
        JSONObject resJson = JSONObject.parseObject(res);
        return resJson.toJSONString();
    }

}
