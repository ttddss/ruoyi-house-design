package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xzh
 * @since 2022/4/14 3:45 下午
 */
@Getter
@AllArgsConstructor
public enum EnumBoolean {

    /**
     * 否
     */
    NO(0),
    /**
     * 是
     */
    YES(1),

    ;

    private final Integer code;

    public static EnumBoolean get(Integer code) {
        for (EnumBoolean j : EnumBoolean.values()) {
            if (j.getCode().equals(code)) {
                return j;
            }
        }
        return null;
    }

    public static EnumBoolean get(boolean b) {
        if (b) {
            return YES;
        } else {
            return NO;
        }
    }


    public boolean eq(Integer obj) {
        return this.code.equals(obj);
    }

}
