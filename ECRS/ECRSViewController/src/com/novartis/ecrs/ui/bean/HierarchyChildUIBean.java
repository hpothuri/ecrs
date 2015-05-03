package com.novartis.ecrs.ui.bean;

import java.util.List;

import oracle.jbo.Row;

public class HierarchyChildUIBean {
    public HierarchyChildUIBean() {
        super();
    }
    
    private String term;
    private String dictContentCode;
    private String levelName;
    private String dictShortName;
    private String dictContentAltCode;
    private Long level;
    private List<HierarchyChildUIBean> children;
    
    public HierarchyChildUIBean(Row row){
        this.term = (String)row.getAttribute("Term");
        this.dictContentCode = (String)row.getAttribute("DictContentCode");
        this.levelName = (String)row.getAttribute("LevelName");
        this.dictShortName = (String)row.getAttribute("DictShortName");
        this.dictContentAltCode = (String)row.getAttribute("DictContentAltCode");
        this.level = (Long)row.getAttribute("Level");
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public void setDictContentCode(String dictContentCode) {
        this.dictContentCode = dictContentCode;
    }

    public String getDictContentCode() {
        return dictContentCode;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setDictShortName(String dictShortName) {
        this.dictShortName = dictShortName;
    }

    public String getDictShortName() {
        return dictShortName;
    }

    public void setDictContentAltCode(String dictContentAltCode) {
        this.dictContentAltCode = dictContentAltCode;
    }

    public String getDictContentAltCode() {
        return dictContentAltCode;
    }

    public void setChildren(List<HierarchyChildUIBean> children) {
        this.children = children;
    }

    public List<HierarchyChildUIBean> getChildren() {
        return children;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Long getLevel() {
        return level;
    }
}
