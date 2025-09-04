package com.ruoyi.common.constant;

/**
 * 缓存的key 常量
 * 
 * @author ruoyi
 */
public class CacheConstants
{
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 短信验证码 redis key
     */
    public static final String SMS_CODE_KEY = "sms_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 学员编号 redis key
     */
    public static final String GEN_STUDENT_NO_KEY = "gen_student_no_key:";

    /**
     * 课程编号 redis key
     */
    public static final String GEN_COURSE_NO_KEY = "gen_course_no_key:";

    /**
     * 收款账户编号 redis key
     */
    public static final String GEN_ACCOUNT_NO_KEY = "gen_account_no_key:";

    /**
     * 订单编号 redis key
     */
    public static final String ORDER_NO_KEY = "order_no_key:";

    /**
     * 课程已下单数量 redis key
     */
    public static final String COURSE_ORDER_NUM_KEY = "course_order_num:";

    /**
     * 课程下单 redis key
     */
    public static final String CREATE_COURSE_ORDER_KEY = "create_course_order_key:";

    /**
     * 短信验证码错误次数 redis key
     */
    public static final String SMS_CODE_ERROR_NUM_KEY = "sms_code_error_num_key:";

    /**
     * 短信验证码短时间内发送的次数 redis key
     */
    public static final String SMS_CODE_NUM_KEY = "sms_code_num_key:";


    /**
     * 订单锁 redis key
     */
    public static final String ORDER_LOCK_KEY = "order_lock_key:";

    /**
     * 支付配置 redis key
     */
    public static final String PAY_CONFIG_KEY = "pay_config_key:";

    /**
     * 聚合支付获取二维码链接请求信息rediskey前缀
     */
    public static final String QR_PAY_INFO_KEY_PREFIX = "qr_pay_info_key:";
}
