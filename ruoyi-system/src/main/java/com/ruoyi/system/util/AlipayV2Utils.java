package com.ruoyi.system.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.order.domain.PayOrder;
import com.ruoyi.order.domain.RefundOrder;
import com.ruoyi.pay.domain.dto.AlipayStatementDTO;
import com.ruoyi.pay.enums.AlipayBillTypeEnum;
import com.ruoyi.pay.enums.TransactionTypeEnum;
import com.ruoyi.system.domain.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 *
 *
 * @author tongdashuai
 * @version 1.0
 * @description 支付宝支付相关工具类
 * 支付宝扫码付官方接口文档详见：https://opendocs.alipay.com/open/02ekfk?ref=api
 * @date 2023/3/15 9:07
 */
@Slf4j
@Component
public class AlipayV2Utils {


    /**
     * 支付结果通知地址
     */
    private static String notifyUrl;

    /**
     * 是否沙箱环境
     */
    private static boolean isSandBox;

    @Value("${payConfig.alipay.notifyUrl}")
    public void setNotifyUrl(String notifyUrl) {
        AlipayV2Utils.notifyUrl = notifyUrl;
    }


    @Value("${payConfig.alipay.isSandBox:true}")
    public void setIsSandBox(boolean isSandBox) {
        AlipayV2Utils.isSandBox = isSandBox;
    }

    /**
     * 支付宝应用配置版本
     */
    private static final Map<String, Integer> APP_CONF_VERSION = new ConcurrentHashMap<>() ;

    private static final Map<String, AlipayClient> CLIENT_MAP = new ConcurrentHashMap<>();


    /**
     * 沙箱环境网关
     */
    private static final String SANDBOX_GATEWAY = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 支付宝网关
     */
    private static final String GATEWAY = "https://openapi.alipay.com/gateway.do";

    private static final Charset CHARSET_GBK =  Charset.forName("GBK");




    /**
     * 支付宝扫码支付-统一收单线下交易预创建
     * 详见：https://opendocs.alipay.com/open/02ekfg?scene=19&pathHash=d3c84596
     *
     * @param alipayConfig 支付配置信息
     * @param request 请求信息
     * @return
     */
    public static AlipayTradePrecreateResponse tradePrecreate(PayConfig alipayConfig, PayOrder payOrder) {
        // 初始化配置信息
        AlipayClient alipayClient = initAlipayClient(alipayConfig);

        AlipayTradePrecreateRequest payRequest = new AlipayTradePrecreateRequest();
        payRequest.setNotifyUrl(notifyUrl);

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        // 商户订单号,必填。 由商家自定义，64个字符以内，仅支持字母、数字、下划线且需保证在商户端不重复。
        model.setOutTradeNo(payOrder.getOrderNo());
        // 订单标题,必填。注意：不可使用特殊字符，如 /，=，& 等。
        model.setSubject(payOrder.getSubject());
        // 订单总金额，必填。单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]，金额不能为 0。如果同时传入了【可打折金额】，【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【可打折金额】+【不可打折金额】
        model.setTotalAmount(payOrder.getActualAmount().setScale(2, RoundingMode.HALF_UP).toString());
        // 订单附加信息，可空。如果请求时传递了该参数，将在异步通知、对账单中原样返回，同时会在商户和用户的pc账单详情中作为交易描述展示
        // model.setBody(payOrder.getSubject());

        // 服务商配置
//        if (alipayConfig.isServiceProvider()) {
//            // 授权token，代调用必传
//            payRequest.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, alipayConfig.getAuthToken());
//            // 系统商编号,服务商返佣必填.该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
//            ExtendParams extendParams = new ExtendParams();
//            extendParams.setSysServiceProviderId(alipayConfig.getPid());
//            model.setExtendParams(extendParams);
//        }
        payRequest.setBizModel(model);

        AlipayTradePrecreateResponse payResponse;
        try {
            payResponse = doExecute(alipayConfig, alipayClient, payRequest);
            log.info("支付宝扫码支付请求返回" + JSONObject.toJSONString(payResponse));
        } catch (AlipayApiException e) {
            log.error("调用支付宝扫码付的统一收单线下交易预创建接口失败,请求信息{}", JSONObject.toJSONString(payRequest));
            log.error("调用支付宝扫码付的错误堆栈信息：", e);
            throw new ServiceException("调用支付宝扫码付的统一收单线下交易预创建接口失败");
        }
        return payResponse;
    }

    /**
     * 支付宝手机网站支付
     * 详见：https://opendocs.alipay.com/open/02ivbs?scene=21&pathHash=0a6313c7
     *
     * @param alipayConfig 支付宝支付配置
     * @param request 请求信息
     * @return
     */
    public static AlipayTradeWapPayResponse wapPay(PayConfig alipayConfig, PayOrder payOrder) {
        // 初始化配置信息
        AlipayClient alipayClient = initAlipayClient(alipayConfig);

        AlipayTradeWapPayRequest payRequest = new AlipayTradeWapPayRequest();
        payRequest.setNotifyUrl(notifyUrl);
        payRequest.setReturnUrl("");

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        // 商户订单号,必填。 由商家自定义，64个字符以内，仅支持字母、数字、下划线且需保证在商户端不重复。
        model.setOutTradeNo(payOrder.getOrderNo());
        // 订单标题,必填。注意：不可使用特殊字符，如 /，=，& 等。
        model.setSubject(payOrder.getSubject());
        // 订单总金额，必填。单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]，金额不能为 0。如果同时传入了【可打折金额】，【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【可打折金额】+【不可打折金额】
        model.setTotalAmount(payOrder.getActualAmount().setScale(2, RoundingMode.HALF_UP).toString());
        // 订单附加信息，可空。如果请求时传递了该参数，将在异步通知、对账单中原样返回，同时会在商户和用户的pc账单详情中作为交易描述展示
        // model.setBody(payOrder.getDescription());
        // 用户付款中途退出返回商户网站的地址
        // model.setQuitUrl(payOrder.getQuitUrl());
        // 服务商配置
//        if (alipayConfig.isServiceProvider()) {
//            // 授权token，代调用必传
//            payRequest.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, alipayConfig.getAuthToken());
//            // 系统商编号,服务商返佣必填.该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
//            ExtendParams extendParams = new ExtendParams();
//            extendParams.setSysServiceProviderId(alipayConfig.getPid());
//            model.setExtendParams(extendParams);
//        }
        payRequest.setBizModel(model);

        AlipayTradeWapPayResponse payResponse;
        try {
            payResponse = alipayClient.pageExecute(payRequest, "GET");
            log.info("支付宝手机网站支付请求返回" + JSONObject.toJSONString(payResponse));
        } catch (AlipayApiException e) {
            log.error("调用支付宝手机网站支付接口失败,请求信息{}", JSONObject.toJSONString(payRequest));
            log.error("调用支付宝手机网站支付接口失败，错误堆栈信息：", e);
            throw new ServiceException("调用支付宝手机网站支付接口失败");
        }
        return payResponse;
    }



    /**
     * 支付宝订单查询-统一收单交易查询
     *
     * @param alipayConfig 支付配置信息
     * @param orderNo 订单号
     * @return
     */
    public static AlipayTradeQueryResponse tradeQuery(String orderNo, PayConfig alipayConfig) {
        // 初始化配置信息
        AlipayClient alipayClient = initAlipayClient(alipayConfig);

        AlipayTradeQueryRequest tradeQueryRequest = new AlipayTradeQueryRequest();

        // 服务商模式配置
//        if (alipayConfig.isServiceProvider()) {
//            // 授权token，代调用必传
//            tradeQueryRequest.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, alipayConfig.getAuthToken());
//        }

        AlipayTradeQueryModel tradeQueryModel = new AlipayTradeQueryModel();
        tradeQueryModel.setOutTradeNo(orderNo);
        tradeQueryRequest.setBizModel(tradeQueryModel);

        AlipayTradeQueryResponse tradeQueryResponse;
        try {
            tradeQueryResponse =  doExecute(alipayConfig, alipayClient, tradeQueryRequest);
            log.info("支付宝订单查询请求返回" + JSONObject.toJSONString(tradeQueryResponse));
        } catch (AlipayApiException e) {
            log.error("调用支付宝的统一收单交易查询接口失败,订单号：{}", orderNo);
            log.error("调用支付宝的统一收单交易查询接口失败,错误堆栈信息：", e);
            throw new ServiceException("调用支付宝的统一收单交易查询接口失败");
        }
        return tradeQueryResponse;
    }

    /**
     * 支付宝退款-统一收单交易退款接口
     *
     * @param alipayConfig 支付配置信息
     * @param refundOrder 退款单信息
     * @return
     */
    public static AlipayTradeRefundResponse tradeRefund(PayConfig alipayConfig, RefundOrder refundOrder) {
        // 初始化配置信息
        AlipayClient alipayClient = initAlipayClient(alipayConfig);

        AlipayTradeRefundRequest tradeRefundRequest = new AlipayTradeRefundRequest();
        // 服务商模式必传
//        // 授权token，代调用必传
//        tradeRefundRequest.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, alipayConfig.getAuthToken());

        AlipayTradeRefundModel tradeRefundModel = new AlipayTradeRefundModel();
        // 商户订单号。订单支付时传入的商户订单号，商家自定义且保证商家系统中唯一
        tradeRefundModel.setOutTradeNo(refundOrder.getOrderNo());
        // 退款金额。该金额不能大于订单金额，单位为元，支持两位小数。
        tradeRefundModel.setRefundAmount(refundOrder.getAmount().setScale(2, RoundingMode.HALF_UP).toString());
        // 退款请求号。，如需部分退款，则此参数必传。
        tradeRefundModel.setOutRequestNo(refundOrder.getOrderNo());
        // 退款原因，可选参数
        tradeRefundModel.setRefundReason(refundOrder.getDescription());
        tradeRefundRequest.setBizModel(tradeRefundModel);

        AlipayTradeRefundResponse refundResponse;
        try {
            refundResponse = doExecute(alipayConfig, alipayClient, tradeRefundRequest);
            log.info("支付宝订单退款请求返回" + JSONObject.toJSONString(refundResponse));
        } catch (AlipayApiException e) {
            log.error("调用支付宝统一收单交易退款接口失败,退款单信息{}", JSONObject.toJSONString(refundOrder));
            log.error("调用支付宝统一收单交易退款接口失败,错误堆栈信息：", e);
            throw new ServiceException("调用支付宝统一收单交易退款接口失败");
        }
        return refundResponse;
    }

    /**
     * 发送http接口请求
     *
     * @param alipayConfig 支付配置
     * @param alipayClient 请求client
     * @param request 请求信息
     * @return
     * @throws AlipayApiException
     */
    private static <T extends AlipayResponse> T doExecute(PayConfig alipayConfig, AlipayClient alipayClient, AlipayRequest<T> request) throws AlipayApiException {
        T response;
        response = alipayClient.execute(request);
        // 证书模式
//        response = alipayClient.certificateExecute(request);
        return response;
    }

    /**
     * 支付宝退款查询-统一收单交易退款查询
     *
     * @param orderNo 退款单号
     * @param payOrderNo  支付单号
     * @param alipayConfig 支付配置信息
     * @return
     */
    public static AlipayTradeFastpayRefundQueryResponse refundQuery(String orderNo, String payOrderNo, PayConfig alipayConfig) {
        // 初始化配置信息
        AlipayClient alipayClient = initAlipayClient(alipayConfig);

        AlipayTradeFastpayRefundQueryRequest refundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();

//        // 服务商模式必传
//        // 授权token，代调用必传
//        refundQueryRequest.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, alipayConfig.getAuthToken());

        AlipayTradeFastpayRefundQueryModel refundQueryModel = new AlipayTradeFastpayRefundQueryModel();
        // 商户订单号
        refundQueryModel.setOutTradeNo(payOrderNo);
        // 退款请求号。请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的商户订单号。
        refundQueryModel.setOutRequestNo(orderNo);
        refundQueryRequest.setBizModel(refundQueryModel);

        AlipayTradeFastpayRefundQueryResponse refundQueryResponse;
        try {
            refundQueryResponse = doExecute(alipayConfig, alipayClient, refundQueryRequest);
            log.info("支付宝订单退款查询请求返回" + JSONObject.toJSONString(refundQueryResponse));
        } catch (AlipayApiException e) {
            log.error("调用支付宝的统一收单交易退款查询接口失败,订单号:{},退款单号:{}", payOrderNo, orderNo);
            log.error("错误堆栈信息：", e);
            throw new ServiceException("调用支付宝的统一收单交易退款查询接口失败");
        }
        return refundQueryResponse;
    }

    /**
     * 支付宝查询对账单下载地址
     *
     * @param billDate 对账日期 账单时间：
     *                      日账单格式为yyyy-MM-dd，最早可下载2016年1月1日开始的日账单。不支持下载当日账单，只能下载前一日24点前的账单数
     *                  据（T+1），当日数据一般于次日 9 点前生成，特殊情况可能延迟。
     *                      月账单格式为yyyy-MM，最早可下载2016年1月开始的月账单。不支持下载当月账单，只能下载上一月账单数据，当月账单一般在
     *                  次月 3 日生成，特殊情况可能延迟。
     * @param alipayConfig 支付配置信息
     * @return
     */
    public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadurlQuery(String billDate, PayConfig alipayConfig) {
        // 初始化配置信息
        AlipayClient alipayClient = initAlipayClient(alipayConfig);

        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
//        // 服务商模式必传
//        // 授权token，代调用必传
//        request.putOtherTextParam(AlipayConstants.APP_AUTH_TOKEN, alipayConfig.getAuthToken());

        AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
        // 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型，支持：
        model.setBillType(AlipayBillTypeEnum.TRADE.getCode());
        // 账单时间：
        //  日账单格式为yyyy-MM-dd，最早可下载2016年1月1日开始的日账单。不支持下载当日账单，只能下载前一日24点前的账单数据（T+1），
        // 当日数据一般于次日 9 点前生成，特殊情况可能延迟。
        //  月账单格式为yyyy-MM，最早可下载2016年1月开始的月账单。不支持下载当月账单，只能下载上一月账单数据，当月账单一般在次月 3 日
        // 生成，特殊情况可能延迟。
        model.setBillDate(billDate);
        request.setBizModel(model);
        AlipayDataDataserviceBillDownloadurlQueryResponse response;
        try {
            response = doExecute(alipayConfig, alipayClient, request);
            log.info("支付宝订单退款查询请求返回" + JSONObject.toJSONString(response));
        } catch (AlipayApiException e) {
            log.error("调用支付宝的查询对账单下载地址接口失败,biiDate:{}, payConfig:{}", billDate, JSONObject.toJSONString(alipayConfig));
            log.error("错误堆栈信息：", e);
            throw new ServiceException("调用支付宝的查询对账单下载地址接口失败");
        }

        return response;
    }


    /**
     * 解析支付宝的账单文件
     * 代码来源于：http://www.taodudu.cc/news/show-4045410.html
     *
     * 明细字段如下：
     * 支付宝交易号,商户订单号,业务类型,商品名称,创建时间,完成时间,门店编号,门店名称,操作员,终端号,对方账户,订单金额（元）,商家实收（元）,支付宝红包（元）,集分宝（元）,支付宝优惠（元）,商家优惠（元）,券核销金额（元）,券名称,商家红包消费金额（元）,卡消费金额（元）,退款批次号/请求号,服务费（元）,分润（元）,备注
     *
     *
     * @param urlStr 账单文件下载地址
     * @param payConfig
     * @return
     */
    public static List<AlipayStatementDTO> parseBillFile(String urlStr, PayConfig payConfig) {
        List<AlipayStatementDTO> statements = new ArrayList<>();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod(HttpMethod.GET.name());
            conn.connect();

            try (
                    // 不解压直接读取,加上GBK解决乱码问题
                    ZipInputStream in = new ZipInputStream(conn.getInputStream(), CHARSET_GBK);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, CHARSET_GBK));
                    ) {
                ZipEntry zipFile;
                // 循环读取zip中的cvs文件，无法使用jdk自带，因为文件名中有中文
                while ((zipFile = in.getNextEntry()) != null) {
                    // 目录不处理
                    if (zipFile.isDirectory()) {
                        continue;
                    }
                    // 解析的单个文件
                    parseSingleZipFile(payConfig, statements, br, zipFile);
                }
            }

        } catch (IOException e) {
            log.error("解析对账单数据异常，错误信息：", e);
            throw new ServiceException("解析对账单数据异常");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return statements;
    }

    /**
     * 解析单个文件
     * @param payConfig 支付配置信息
     * @param statements 对账数据集合
     * @param br  缓冲输入流
     * @param zipFile 压缩包的子文件
     * @throws IOException
     */
    private static void parseSingleZipFile(PayConfig payConfig, List<AlipayStatementDTO> statements, BufferedReader br, ZipEntry zipFile) throws IOException {
        // 获得cvs名字，检测文件是否存在
        String fileName = zipFile.getName();
        // 只解析明细数据,汇总文件不解析
        if (isNeedParseFile(fileName)) {
            String line;
            int i = 0;
            // 按行读取数据
            while ((line = br.readLine()) != null) {
                // 注释行跳过，只读取数据行
                if (line.startsWith("#")) {
                    continue;
                }
                // 跳过标题行
                if (i > 0) {
                    String[] lines = line.split(",", -1);
                    TransactionTypeEnum transactionTypeEnum = TransactionTypeEnum.valueAliNameOf(lines[2].trim());
                    AlipayStatementDTO statement = AlipayStatementDTO.builder()
                            .thirdOrderNo(lines[0].trim())
                            .orderNo(lines[1].trim())
                            .transactionType(transactionTypeEnum.getCode())
                            .subject(lines[3].trim())
                            .thirdCreateTime(formatDateCell(lines[4]))
                            .thirdFinishTime(formatDateCell(lines[5]))
                            .storeNo(lines[6].trim())
                            .storeName(lines[7].trim())
                            .operator(lines[8].trim())
                            .terminalNo(lines[9].trim())
                            .buyAccount(lines[10].trim())
                            .amount(formatBigDecimalCell(lines[11]))
                            .realAmount(formatBigDecimalCell(lines[12]))
                            .refundNo(lines[21].trim())
                            .serviceFee(formatBigDecimalCell(lines[22]))
                            .commission(formatBigDecimalCell(lines[23]))
                            .appid(payConfig.getAppid())
//                            .subAppid(payConfig.getSubAppid())
                            .build();
                    statements.add(statement);
                }
                i++;
            }
        }
    }

    /**
     * 是否是需要解析的文件名称
     * 只解析明细数据,汇总文件不解析
     *
     * @param fileName
     * @return
     */
    private static boolean isNeedParseFile(String fileName) {
        return !Objects.isNull(fileName) && fileName.indexOf(".") != -1 && !fileName.contains("汇总");
    }

    /**
     * 格式化日期单元格
     *
     * @param str
     * @return
     */
    private static Date formatDateCell(String str) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        return DateUtil.parseDateTime(str);
    }

    /**
     * 格式化数字单元格
     *
     * @param str
     * @return
     */
    private static BigDecimal formatBigDecimalCell(String str) {
        if (StrUtil.isBlank(str)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(str.trim());
    }

    /**
     * 初始化配置
     *
     * @param alipayConfig 支付配置信息
     * @return
     */
    private static AlipayClient initAlipayClient(PayConfig alipayConfig) {
        String key = alipayConfig.getChannel() + Constants.COLON_DELIMETER + alipayConfig.getSubChannel();
        Integer version = APP_CONF_VERSION.get(key);
        if (version != null && version >= alipayConfig.getVersion()) {
            return CLIENT_MAP.get(key);
        }

        AlipayConfig config = new AlipayConfig();
        //设置网关地址
        config.setServerUrl(isSandBox ? SANDBOX_GATEWAY : GATEWAY);
        //设置应用ID
        config.setAppId(alipayConfig.getAppid());
        //设置应用私钥
        config.setPrivateKey(alipayConfig.getPrivateKey());
        //设置请求格式，固定值json
        config.setFormat(AlipayConstants.FORMAT_JSON);
        //设置字符集
        config.setCharset(AlipayConstants.CHARSET_UTF8);
        //设置签名类型
        config.setSignType(AlipayConstants.SIGN_TYPE_RSA2);

        // 设置支付宝公钥
        config.setAlipayPublicKey(alipayConfig.getPublicKey());

        // 证书模式配置
//            //   设置应用公钥证书路径
//            config.setAppCertPath(alipayConfig.getAppCertPath());
//            //设置支付宝公钥证书路径
//            config.setAlipayPublicCertPath(alipayConfig.getAlipayCertPath());
//            //设置支付宝根证书路径
//            config.setRootCertPath(alipayConfig.getAlipayRootCertPath());


        //实例化客户端
        try {
            DefaultAlipayClient client = new DefaultAlipayClient(config);
            CLIENT_MAP.put(key, client);
            APP_CONF_VERSION.put(key, alipayConfig.getVersion());
            return client;
        } catch (AlipayApiException e) {
            log.error("支付宝配置初始化错误：", e);
            throw new ServiceException("支付宝配置初始化错误");
        }

    }



    /**
     * 将异步通知的参数转化为Map
     *
     * @param request {HttpServletRequest}
     * @return 转化后的Map
     */
    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }





}
