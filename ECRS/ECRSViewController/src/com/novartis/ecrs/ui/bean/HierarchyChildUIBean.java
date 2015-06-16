package com.novartis.ecrs.ui.bean;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.List;

import javax.faces.model.SelectItem;

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
    private String formattedScope;
    private String scopeName;
    
    public HierarchyChildUIBean(Row row){
        this.term = (String)row.getAttribute("Term");
        this.dictContentCode = (String)row.getAttribute("DictContentCode");
        this.levelName = (String)row.getAttribute("LevelName");
        if(this.levelName != null 
           && ("SOC".equalsIgnoreCase(this.levelName.trim()) || "PT".equalsIgnoreCase(this.levelName.trim()) 
            || "HLGT".equalsIgnoreCase(this.levelName.trim()) || "HLT".equalsIgnoreCase(this.levelName.trim()))){
            this.dictShortName = "NMATMED";
        } else {
            this.dictShortName = "NMATSMQ";
            this.formattedScope = ((BigDecimal)row.getAttribute("FormattedScope")).toString();
            this.scopeName = getScopeName();
        }
//        this.dictShortName = (String)row.getAttribute("DictShortName");
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
    
    public String getScopeName() {
        String scopeName="";
           if (formattedScope == null) return null;
           if (formattedScope.equals("0")) scopeName = "Full";
           if (formattedScope.equals("1")) scopeName = "Broad";
           if (formattedScope.equals("2")) scopeName = "Narrow";
           if (formattedScope.equals("3")) scopeName = "Child/Narrow";
           return scopeName;
    }

    public void setFormattedScope(String formattedScope) {
        this.formattedScope = formattedScope;
    }

    public String getFormattedScope() {
        return formattedScope;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }
}
