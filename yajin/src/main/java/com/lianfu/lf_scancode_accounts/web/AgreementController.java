package com.lianfu.lf_scancode_accounts.web;

import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.util.BytePictureUtils;
import com.lianfu.lf_scancode_accounts.model.*;
import com.lianfu.lf_scancode_accounts.service.CashoutService;
import com.lianfu.lf_scancode_accounts.utils.FileUrl;
import com.lianfu.lf_scancode_accounts.utils.HttpUtil;
import com.lianfu.lf_scancode_accounts.utils.RotateImageUtil;
import com.lianfu.lf_scancode_accounts.utils.WeixinOauth2Token;
import com.lianfu.lf_scancode_accounts.utils.email.IEmailService;
import com.lianfu.lf_scancode_accounts.web.response.BuildResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("agreement")
public class AgreementController {
    private final Logger logger = LoggerFactory.getLogger(AgreementController.class);

    @Autowired
    IEmailService iEmailService;

    @Autowired
    CashoutService ca;

    private static final String LOGIN_URL = "http://core.lianfuteam.com/lfpay/public/api/lwechat/getMerId";
    private static final String SELECT_URL = "http://core.lianfuteam.com/lfpay/public/api/lwechat/queryOrders";
    //    private static final String SELECT_URL = "http://192.168.0.54/public/api/lwechat/queryOrders";
    private static final String REFUND_URL = "http://core.lianfuteam.com/lfpay/public/api/lwechat/reject";


    @RequestMapping("/code")
    public void code(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx028fe0fdaf2811a0&redirect_uri=http://wuye.lianfubiz.com/lf_scancode_accounts/agreement/index&response_type=code&scope=snsapi_base#wechat_redirect");
    }

    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request, HttpSession session) {
        try {
            HttpUtil httpUtil = new HttpUtil();
            String appid = "wx028fe0fdaf2811a0";
            String appSecret = "fc4d995c3d761fdfbdb4ce142514ec95";
            String code = request.getParameter("code");
            String accessToken = null;    // 2020-6-22 齐亮 写死 方便查看
            String openId = null;
            if (!"authdeny".equals(code)) {
                JSONObject json = WeixinOauth2Token.getOauth2AccessToken(appid, appSecret, code);   // 获取网页授权access_token
                accessToken = json.getString("access_token");                             // 网页授权接口访问凭证
                openId = json.getString("openid");                                           // 用户标识
            }





            // 2020-6-22 齐亮 写死openid  方便查看
           /* openId = "oN5Ye1CSbYyg897lOLyAiYBbfq4I";
            String nickname = "A董刚";
            String headimgurl = "http://thirdwx.qlogo.cn/mmopen/PiajxSqBRaEIeP70dmSnrT0tcLIpib83K20Oyb58fw3GbqHMpxicAjE1C28x5U2tqm0TWjnOp1tRH8UaXOvGQSKOg/132";
*/
            //2020-6-22 齐亮注释 写死 方便登录进来 查看
            //获取access_token
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
            session.setAttribute("openid", openId);
            session.setAttribute("nickname", nickname);
            session.setAttribute("headimgurl", headimgurl);

            return "protocol.html";
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("err_code", "500");
            model.addAttribute("err_msg", "服务器错误，请联系管理员");
            return "pages_error.html";
        }
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        return format + "/";
    }




    @RequestMapping("getPreFileName")
    @ResponseBody
    private String getPreFileName(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String companyName = (String) session.getAttribute("companyName");
        return getDate() + URLEncoder.encode(companyName) + ".docx";
    }

    /**
     * 根据传入参数生成预览word文档
     *
     * @param request
     * @param protocol
     * @return
     */
    @RequestMapping("generateWordFile")
    private String generateWordFile(HttpServletRequest request, Protocol protocol) {
        HttpSession session = request.getSession();
        String openid = (String) session.getAttribute("openid");
        String companyName = protocol.getCompanyName();
        session.setAttribute("companyName", companyName);
        session.setAttribute("protocol",protocol);


        String originFilePath = FileUrl.origin;
        String outputFileName = FileUrl.preview + getDate();
        File f = new File(outputFileName);
        if (!f.exists())
            f.mkdirs();
        outputFileName += companyName + ".docx";

        logger.info("输入文件：" + originFilePath);
        logger.info("输出文件：" + outputFileName);
        Style style = new Style();
        style.setBold(true);
        TextRenderData textRenderData1 = new TextRenderData("d0d0d0", "序号");
        TextRenderData textRenderData2 = new TextRenderData("d0d0d0", "设备名称");
        TextRenderData textRenderData3 = new TextRenderData("d0d0d0", "单价");
        TextRenderData textRenderData4 = new TextRenderData("d0d0d0", "数量");
        TextRenderData textRenderData5 = new TextRenderData("d0d0d0", "合计");
        TextRenderData textRenderData6 = new TextRenderData("d0d0d0", "备注");
        textRenderData1.setStyle(style);
        textRenderData2.setStyle(style);
        textRenderData3.setStyle(style);
        textRenderData4.setStyle(style);
        textRenderData5.setStyle(style);
        textRenderData6.setStyle(style);
        RowRenderData header = RowRenderData.build(
                textRenderData1,
                textRenderData2,
                textRenderData3,
                textRenderData4,
                textRenderData5,
                textRenderData6
        );
        ArrayList<RowRenderData> trtds = new ArrayList<>();
        List<ProtocolItem> items = protocol.getProtocolItems();
        int index = 1;
        try{
            for (ProtocolItem item : items) {
                if (item.getDevType().equals("1") || item.getDevType().equals("3")) {
                    protocol.setYingjianfei(protocol.getYingjianfei() + item.getTotalMoney());
                } else {
                    protocol.setRuanjianfei(protocol.getRuanjianfei() + item.getTotalMoney());
                }
                trtds.add(RowRenderData.build(index + "", item.getDevName(), item.getMoney() + "", item.getNumber() + "", item.getTotalMoney() + "", "无"));
                session.setAttribute("deavname",item.getDevName());

                index++;
            }
        }catch (NullPointerException n){

        }
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("qiandingshijian",protocol.getAtime());
        datas.put("qiandingdidian",protocol.getAsite());
        datas.put("songdadizhi",protocol.getAddressOfService());
        datas.put("lianxiren",protocol.getThecontact());
        datas.put("lianxirendianhua",protocol.getContactphonenumber());
        datas.put("shifoudianzisongda",protocol.getElectronicdelivery());
        datas.put("dianziyouxiang",protocol.getEMail());
        datas.put("hezuozhengce", protocol.getCooperationPolicy());
        datas.put("yajinfanhuanfangshi", protocol.getDepositRefund());
        datas.put("gongsimingcheng", protocol.getCompanyName());
        datas.put("shuihao", protocol.getTaxId());
        datas.put("dizhi", protocol.getAddress());
        datas.put("dianhua", protocol.getPhone());
        datas.put("kaihuhang", protocol.getBank());
        datas.put("zhanghao", protocol.getAccountNumber());
        datas.put("zongjine", protocol.getZongjine());
        datas.put("ruanjianfei", protocol.getRuanjianfei());
        datas.put("yingjianfei", protocol.getYingjianfei());
        datas.put("startdate", protocol.getStartDate());
        datas.put("enddate", protocol.getEndDate());
        datas.put("wx",protocol.getWx_zhi());
        datas.put("zfb",protocol.getZfb());
        datas.put("sign", "{{@sign}}");
        datas.put("qiandingshijian", protocol.getStartDate());
        datas.put("tables", new MiniTableRenderData(header, trtds));
        XWPFTemplate template = XWPFTemplate.compile(originFilePath)
                .render(datas);

        FileOutputStream out;
        try {
            out = new FileOutputStream(outputFileName);
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "sign.html";
    }
    /* 存放预览word文档 /usr/preview/date/uuid.docx
        存放签名 /usr/signpic/date/uuid.png
        存放签名后word文档 /usr/signedword/date/uuid.docx
        存放转换后的pdf /usr/finalpdf/date/uuid.docx
     */

    /**
     * 接收签名图片，存放，合并
     */
    @RequestMapping("signPic")
    @ResponseBody
    public ResponseModel singPic(@RequestParam(value = "file") MultipartFile file, HttpSession session) {
        String companyName = (String) session.getAttribute("companyName");
        String urlCompanyName = URLEncoder.encode(companyName);

        // 生成签名后最后文件时 往 lf_gl 联付后台管理表 增加数据 start
        Lf_gl lf=new Lf_gl();
        //end
        try {
            InputStream inputStream = file.getInputStream();
            if (!new File(FileUrl.signpic + getDate()).exists())
                new File(FileUrl.signpic + getDate()).mkdirs();
            String outPutFileName = FileUrl.signpic + getDate() + companyName + ".png";
//            IOUtils.copy(inputStream, new FileOutputStream(outPutFileName));
            // 将网络图片转为BufferedImage
            try {
                BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                // 调用图片旋转工具类，旋转图片
                BufferedImage rotateImage = RotateImageUtil.rotateImage(bufferedImage, 270);
                // 将旋转后的图片保存到D盘根目录下
                ImageIO.write(rotateImage, "png", new File(outPutFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String originFilePath = FileUrl.preview + getDate() + companyName + ".docx";
            String outDocFileName = FileUrl.signedword + getDate();
          //start
            lf.setFilename(companyName + ".docx");
            lf.setFilepath(outDocFileName);
          //end
            File f = new File(outDocFileName);
            if (!f.exists()){
                f.mkdirs();
            }

           outDocFileName += urlCompanyName + ".docx";
            logger.info("签名保存路径：" + outPutFileName);
            logger.info("输入文件：" + originFilePath);
            logger.info("输出文件：" + outDocFileName);
            Map<String, Object> datas = new HashMap<String, Object>();

            byte[] localByteArray = BytePictureUtils.getLocalByteArray(new File(outPutFileName));


            datas.put("sign", new PictureRenderData(250, 150, ".png", localByteArray));

            XWPFTemplate template = XWPFTemplate.compile(originFilePath)
                    .render(datas);

            FileOutputStream out;
            try {
                out = new FileOutputStream(outDocFileName);
                System.out.println("out"+outDocFileName);
                template.write(out);
                out.flush();
                out.close();
                template.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
            return BuildResponse.error("不知道出啥问题了");
        }

        //start  添加数据
        Protocol pro=(Protocol)session.getAttribute("protocol");
          String deavname=session.getAttribute("deavname").toString();
         if(deavname.equals("请选择")){
            lf.setContractname(companyName+"设备合同");
         }else {
             lf.setContractname(companyName+deavname+"设备合同");
         }
         String name=(String) session.getAttribute("nickname");
         lf.setState("0");
         lf.setElectronicdelivery(pro.getElectronicdelivery());
         lf.setPhone(pro.getPhone());
         lf.setEmail(pro.getEMail());
         lf.setThecontact(pro.getThecontact());
         lf.setStardate(getDate().replace("/",""));
         lf.setDownload("0");
         lf.setLfname(name);
         lf.setEndDate(pro.getEndDate());
         ca.setLf_gl(lf);
       //end

        return BuildResponse.success("ok");
    }

 /*   *//**
     * 将签名后word文档转换成pdf
     *//*
    @RequestMapping("wordToPdf")
    public String wordToPdf(HttpServletRequest request, String userEmail) {
        HttpSession session = request.getSession();
        String companyName = (String) session.getAttribute("companyName");
        companyName = URLEncoder.encode(companyName);
        String docPath = FileUrl.signedword + getDate() + companyName + ".docx";
        String outputPDF = FileUrl.finalpdf + getDate();
        File f = new File(outputPDF);
        if (!f.exists()) {
            f.mkdirs();
        }
        logger.info("输入文件：" + FileUrl.signedword + getDate() + companyName + ".docx");
        logger.info("输出文件：" + outputPDF);

        File outputFile = new File(outputPDF);
        try {
            // libreoffice --headless --invisible --convert-to pdf /usr/signedword/2020-05-22/1.docx --outdir /usr/finalpdf/2020-05-22
            String cmd = "libreoffice --headless --invisible --convert-to pdf " + docPath + " --outdir " + outputPDF;
            Runtime run = Runtime.getRuntime();
            Process process = run.exec(cmd);
            String line;
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer out = new StringBuffer();
            while ((line = stdoutReader.readLine()) != null) {
                out.append(line);
            }
            process.waitFor();
            process.destroy();
            logger.info(out.toString());


            outputPDF += companyName + ".pdf";
            logger.info(outputPDF);

            iEmailService.sendAttachmentMail(userEmail, "协议", "见附件", outputPDF, URLDecoder.decode(companyName));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "sign.html";
    }*/




      // 暂时 无用  下载时 查询管理表数据
 /*   @RequestMapping(value = "getlf_gl",method = RequestMethod.GET)
   public  @ResponseBody  List<Lf_gl>   getlf_gl(HttpSession session, String name, HttpServletResponse response,Model model) throws IOException {
        System.out.println("进入getlf_gl"+name);
       List<Lf_gl> lf=ca.getlfgl(name);

          return lf;

   }*/



      //下载 暂时不需要了
   /* @RequestMapping("oput")
    public String systemmanagementoput(MultipartFile file1,HttpServletRequest request,HttpSession session,HttpServletResponse response,String name) throws IOException {
        System.out.println("进入onput");
         int id=Integer.parseInt(request.getParameter("name"));
        Lf_gl getfile=ca.getfilep(id);
        //下载文件名
        String fileName=getfile.getFilename();
        String FILEPATH=getfile.getFilepath();
        //通过文件的保存文件夹路径加上文件的名字来获得文件
        File file=new File(FILEPATH,fileName);
        if(file.exists()){  //判断文件是否存在
            //设置响应的内容格式，force-download表示只要点击下载按钮就会自动下载文件
            response.setContentType("application/force-download");
            //设置头信息，filename表示前端下载时的文件名
            System.out.println(file.getName());
            response.addHeader("Content-Disposition",String.format("attachment; filename=\"%s\"", file.getName()));
            //进行读写操作
            byte[] buffer=new byte[1024];
            FileInputStream fileInputStream=null;
            BufferedInputStream bufferedInputStream=null;
            try{
                fileInputStream=new FileInputStream(file);
                bufferedInputStream=new BufferedInputStream(fileInputStream);
                //获取输出流
                OutputStream os=response.getOutputStream();
                //读取并且写入文件到输出流
                int i=bufferedInputStream.read(buffer);
                while(i!=-1){
                    os.write(buffer,0,i);
                    i=bufferedInputStream.read(buffer);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                //关闭输入输出流
                try {
                    if(bufferedInputStream!=null){
                        bufferedInputStream.close();
                    }
                    if(fileInputStream!=null){
                        fileInputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("end");
        return null;
    }
*/






}
