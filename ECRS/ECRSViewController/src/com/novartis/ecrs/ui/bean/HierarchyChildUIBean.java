package com.novartis.ecrs.ui.bean;

import java.sql.Timestamp;

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
    private Timestamp tmsDictContentEntryTs;
    private Long tmsDictContentId;
    private Timestamp tmsEndTs;
    private String qual;
    private String qualFlag;
    private List<HierarchyChildUIBean> children;
    
    public HierarchyChildUIBean(Row row){
        this.term = (String)row.getAttribute("Term");
        this.dictContentCode = (String)row.getAttribute("DictContentCode");
        this.levelName = (String)row.getAttribute("LevelName");
        this.dictShortName = (String)row.getAttribute("DictShortName");
        this.dictContentAltCode = (String)row.getAttribute("DictContentAltCode");
        this.level = (Long)row.getAttribute("Level");
        this.tmsDictContentEntryTs = (Timestamp)row.getAttribute("DictContentEntryTs");
        this.tmsDictContentId = new Long((String)row.getAttribute("DictContentId"));
        this.tmsEndTs = (Timestamp)row.getAttribute("CEndTs");
        this.qual = (String)row.getAttribute("Qual");
        this.qualFlag = (String)row.getAttribute("QualFlag");
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

    public void setTmsDictContentEntryTs(Timestamp tmsDictContentEntryTs) {
        this.tmsDictContentEntryTs = tmsDictContentEntryTs;
    }

    public Timestamp getTmsDictContentEntryTs() {
        return tmsDictContentEntryTs;
    }

    public void setTmsDictContentId(Long tmsDictContentId) {
        this.tmsDictContentId = tmsDictContentId;
    }

    public Long getTmsDictContentId() {
        return tmsDictContentId;
    }

    public void setTmsEndTs(Timestamp tmsEndTs) {
        this.tmsEndTs = tmsEndTs;
    }

    public Timestamp getTmsEndTs() {
        return tmsEndTs;
    }

    public void setQual(String qual) {
        this.qual = qual;
    }

    public String getQual() {
        return qual;
    }

    public void setQualFlag(String qualFlag) {
        this.qualFlag = qualFlag;
    }

    public String getQualFlag() {
        return qualFlag;
    }
}
