package com.ruoyi.blueprints.domain.vo;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.enums.DictTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 图纸分类vo对象
 * 
 * @author tds
 * @date 2025-03-03
 */
@Data
@NoArgsConstructor
public class BlueprintsCategoryVO implements Serializable
{

    private static final long serialVersionUID = 9154350937124343299L;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 激活的子类索引
     */
    private Integer activeChildrenIndex = 0;


    /**
     * 子分类集合
     */
    private List<BlueprintsCategoryVO> children;

    public BlueprintsCategoryVO(DictTypeEnum dictTypeEnum, List<SysDictData> dictDatas) {
        this.code = dictTypeEnum.getCode();
        this.name = dictTypeEnum.getName();
        List<BlueprintsCategoryVO> children = new ArrayList<>();
        this.children = children;
        // 默认子类型都加一个全部的类型
        children.add(new BlueprintsCategoryVO("", "全部"));
        for (SysDictData dictData : dictDatas) {
            children.add(new BlueprintsCategoryVO(dictData.getDictValue(), dictData.getDictLabel()));
        }
    }

    public BlueprintsCategoryVO(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
