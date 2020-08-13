package com.lianfu.lf_scancode_accounts.service;

import com.lianfu.lf_scancode_accounts.mapper.*;
import com.lianfu.lf_scancode_accounts.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class CashoutService {

    private final Logger logger = LoggerFactory.getLogger(CashoutService.class);

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private CashoutModeMapper cashoutModeMapper;
    @Autowired
    private CashoutOrderMapper cashoutOrderMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private Lf_glMapper lfglmapper;


    public CashoutQueryVO getCashoutMessages(int[] mers) {
        int a[]={109,122,125};
        List<Shop> shops = cashoutModeMapper.selectCashoutModeByMersId(a);

        CashoutQueryVO queryVO = new CashoutQueryVO();
        queryVO.setShopConfigs(shops);
        BigDecimal totalDeposit = new BigDecimal("0.00");
        BigDecimal totalCashOut = new BigDecimal("0.00");
        BigDecimal ttotalCashOut;
        BigDecimal totalRem = new BigDecimal("0.00");
        //--------------------------------

        String oldmoney="";
        String merid="";
        String mode="";
        String meridmoney="";
        String newmoney="";

        //--------------
        for (Shop shop : shops) {
            //-------------------------
            merid+=shop.getMerId()+",";
            //------------------------
            totalDeposit = totalDeposit.add(new BigDecimal(shop.getReceivableCash()));
            String lastCashout = shop.getLastCashout();
            Calendar lastCalendar = Calendar.getInstance();
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTime(new Date());
            if (lastCashout == null || "".equals(lastCashout)) {
                shop.setCashstate(true);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date lastCashoutDate = null;
                try {
                    lastCashoutDate = sdf.parse(lastCashout);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                lastCalendar.setTime(lastCashoutDate);
                if (lastCalendar.get(Calendar.MONTH) < nowCalendar.get(Calendar.MONTH)) {
                    shop.setCashstate(true);
                } else {
                    shop.setCashstate(false);
                }
            }

            if (shop.getCashoutMode().equals("1")) {
                //--------------
                mode=shop.getCashoutMode();
                oldmoney+=shop.getRemainderCash()+",";
                //-------------------------
               logger.info("模式一");
                ttotalCashOut=new BigDecimal("0.00");
                if (lastCashout == null || "".equals(lastCashout)) {
                    logger.info("mer_id:+="+shop.getMerId());
                    ttotalCashOut=ttotalCashOut.add(new BigDecimal(shop.getFirstMonthReturn()));
                    totalCashOut = totalCashOut.add(ttotalCashOut);
                    totalRem = totalRem.add(new BigDecimal(shop.getRemainderCash()));
                    //--------------------
                    newmoney+=new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString()+",";
                    meridmoney+=ttotalCashOut+",";
                    //-----------------
                    shop.setRemainderCash(new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString());
                    shop.setRealReturnMoney(new BigDecimal(shop.getRemainderCash()).toString());

                }else {
                    if (lastCalendar.get(Calendar.MONTH)+1 < nowCalendar.get(Calendar.MONTH)+1){
                        if (new BigDecimal(shop.getNormalMonthReturn()).compareTo(new BigDecimal(shop.getRemainderCash())) == 1) {
                            ttotalCashOut=ttotalCashOut.add(new BigDecimal(shop.getNormalMonthReturn()));
                            if (ttotalCashOut.compareTo(new BigDecimal(shop.getRemainderCash()))==1){
                                ttotalCashOut=new BigDecimal(shop.getRemainderCash());
                            }
                            totalCashOut =totalCashOut.add(ttotalCashOut);
                            totalRem = totalRem.add(new BigDecimal(shop.getRemainderCash()));
                            //--------------
                            newmoney+=new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString()+",";
                            meridmoney+=ttotalCashOut+",";

                            //-----------
                            shop.setRemainderCash(new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString());
                            shop.setRealReturnMoney(new BigDecimal(shop.getRemainderCash()).toString());
                        }else {
                            ttotalCashOut=ttotalCashOut.add(new BigDecimal(shop.getNormalMonthReturn()));
                            totalCashOut = totalCashOut.add(ttotalCashOut);
                            totalRem = totalRem.add(new BigDecimal(shop.getRemainderCash()));
                            //--------------
                            newmoney+=new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString()+",";
                            meridmoney+=totalCashOut+",";

                            //-----------
                            shop.setRemainderCash(new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString());
                            shop.setRealReturnMoney(new BigDecimal(shop.getRemainderCash()).toString());
                        }

                    }else {
                        totalRem = totalRem.add(new BigDecimal(shop.getRemainderCash()));
                        shop.setRealReturnMoney(new BigDecimal(shop.getRemainderCash()).toString());
                    }

                }

            }
            if (shop.getCashoutMode().equals("2")) {
                 mode=shop.getCashoutMode();
                oldmoney+=shop.getRemainderCash()+",";
                String cashOutNum = orderMapper.selectCashoutMoneyModel2(shop.getMerId());
                logger.info("模式二");
                if (cashOutNum != null && cashOutNum != "") {
                    ttotalCashOut=new BigDecimal("0.00");
                    ttotalCashOut = ttotalCashOut.add(new BigDecimal(cashOutNum).multiply(new BigDecimal("0.3")).setScale(2, BigDecimal.ROUND_DOWN)); // 千1 除100 真实金额
                    if (ttotalCashOut.compareTo(new BigDecimal(shop.getRemainderCash())) == 1) {
                        ttotalCashOut =new BigDecimal(shop.getRemainderCash());
                    }
                    totalCashOut = totalCashOut.add(ttotalCashOut); // 千1 除100 真实金额
                    totalRem = totalRem.add(new BigDecimal(shop.getRemainderCash()));
                    System.out.println(shop.getRemainderCash()+","+ttotalCashOut);
                    //------------------------------
                    meridmoney+=ttotalCashOut+",";
                    newmoney+=new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString()+",";
                    //-------------------------
                    shop.setRemainderCash(new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString());
                }
                if (totalCashOut.compareTo(totalRem) == 1) {
                    totalCashOut = totalRem;
                }

                shop.setRealReturnMoney(totalCashOut.toString());
            }
            if (shop.getCashoutMode().equals("3")) {
                 mode=shop.getCashoutMode();
                oldmoney+=shop.getRemainderCash()+",";
                logger.info("进入模式3");
                if (shop.getReceivableCash().equals("0.00") || (shop.getReceivableCash() == null || shop.getReceivableCash() == "")) {
                    //----------------
                    ttotalCashOut=new BigDecimal("0.00");
                    //---------------------
                    String cashOut = orderMapper.selectCashoutMoney(shop.getMerId());
                    if (cashOut != null && cashOut != "") {
                        //-----------------------
                        ttotalCashOut = ttotalCashOut.add(new BigDecimal(cashOut).divide(new BigDecimal("100000")).setScale(2, BigDecimal.ROUND_DOWN)); // 千1 除100 真实金额
                        //-----------------------
                        totalCashOut = totalCashOut.add(new BigDecimal(cashOut).divide(new BigDecimal("100000")).setScale(2, BigDecimal.ROUND_DOWN)); // 千1 除100 真实金额

                    }
                    //-------------------------------
                    meridmoney+=ttotalCashOut+",";
                    newmoney+=shop.getRemainderCash()+",";
                    //----------------------
                    shop.setRealReturnMoney(totalCashOut.toString());
                } else if (shop.getRemainderCash() != null && shop.getRemainderCash() != "") {
                    String cashOut = orderMapper.selectCashoutMoney(shop.getMerId());
                    ttotalCashOut=new BigDecimal("0.00");
                    if (cashOut != null && cashOut != "") {
                        ttotalCashOut = ttotalCashOut.add(new BigDecimal(cashOut).divide(new BigDecimal("100000")).setScale(2, BigDecimal.ROUND_DOWN)); // 千1 除100 真实金额
                        if (ttotalCashOut.compareTo(new BigDecimal(shop.getRemainderCash())) == 1) {
                            ttotalCashOut = new BigDecimal(shop.getRemainderCash());
                        }
                        totalCashOut = totalCashOut.add(ttotalCashOut);

                    }
                    totalRem = totalRem.add(new BigDecimal(shop.getRemainderCash()));
                    //-------------------------------
                    meridmoney+=ttotalCashOut+",";
                    newmoney+=new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString()+",";
                    //----------------------
                    shop.setRemainderCash(new BigDecimal(shop.getRemainderCash()).subtract(ttotalCashOut).toString());

                    if (totalCashOut.compareTo(totalRem) == 1) {
                        totalCashOut = totalRem;
                    }

                    shop.setRealReturnMoney(new BigDecimal(shop.getRemainderCash()).toString());
                }


            }


        }
        queryVO.setNewmoney(newmoney);
        queryVO.setCashoutmeridmoney(meridmoney);
        queryVO.setCashoutold_balance( oldmoney); // 后添 提现前余额 和返现模式 目的 修改提现记录表里新加字段
        queryVO .setCashoutmode(mode);
        queryVO.setCashoutmerid(merid);
        //------------------------------------
        //可提现金额 set 进属性
        queryVO.setTotalCashout(totalCashOut.toString());

        // 实收押金 set 进属性
        queryVO.setTotalDeposit(totalDeposit.toString());

        //冻结金额 set 进属性
        queryVO.setTotalRem(totalRem.toString());
        return queryVO;
    }

    public List<CashoutOrder> getCashoutOrders(String openid) {
        return cashoutOrderMapper.getCashoutOrders(openid);
    }

    public void placeOrder(CashoutOrder co, List<Shop> cashoutMessages) {


        logger.info("需修改shop" + cashoutMessages.toString());
        String time = new Date().getTime() + "";
        String orderNo = time.substring(time.length() - 6) + (int) (1 + Math.random() * 10000);
        String openid = co.getCashoutOpenId();
        co.setCashoutOrderNo(orderNo);
        co.setCashoutDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        co.setCashoutStatus("2");

        try {
            cashoutOrderMapper.addCashoutOrder(co);
            for (Shop shop : cashoutMessages) {
                shop.setLastCashout(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                cashoutOrderMapper.updateDevice(shop);
                if ("3".equals(shop.getCashoutMode()) || "2".equals(shop.getCashoutMode())) {
                    cashoutOrderMapper.updateOrders(shop);
                }
            }
        } catch (Exception e) {
            logger.info("修改押金剩余金额回滚");
            throw new RuntimeException(e.getMessage());
        }
    }

    //2020-6-23 齐亮创建   添加 数据
    public  int setLf_gl(Lf_gl lf){
        return lfglmapper.insetoLf_gl(lf);
    }


   //------------------------押金审核
    public List<CashoutOrder> getCashoutOrder(){ return cashoutOrderMapper.seletCashoutOrders();}

    public CashoutOrder getCashoutOrder(int id){
       CashoutOrder cashoutOrder=cashoutOrderMapper.seletCashoutOrdersWhereId(id);

        String merid[]=cashoutOrder.getCashoutmerid().split(",");



        int mid[]=new int [merid.length];
        for (int i=0;i<=merid.length-1;i++){
              mid[i]=Integer.parseInt(merid[i]);
        }
        if(mid.length>0){
            List<Shop> shops = cashoutModeMapper.selectCashoutModeByMersId(mid);
            cashoutOrder.setShop(shops);
        }

        return cashoutOrder;
    }
   public  String getmode(int id){return cashoutModeMapper.seletmodes(id);}















}
