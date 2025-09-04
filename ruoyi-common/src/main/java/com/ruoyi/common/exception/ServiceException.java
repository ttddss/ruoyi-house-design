package com.ruoyi.common.exception;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.enums.ResultEnum;

/**
 * 业务异常
 * 
 * @author ruoyi
 */
public final class ServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 字符错误码
     */
    private String scode;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException()
    {
        this.code = ResultEnum.BIZ_ERROR.getCode();
        this.scode = ResultEnum.BIZ_ERROR.getScode();
    }

    public ServiceException(String message)
    {
        this.message = message;
        this.code = ResultEnum.BIZ_ERROR.getCode();
        this.scode = ResultEnum.BIZ_ERROR.getScode();
    }

    public ServiceException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
        this.scode = code + "";
    }


    public ServiceException(String message, Integer code, String scode)
    {
        this.message = message;
        this.code = code;
        this.scode = scode;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }


    public ServiceException setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }
}