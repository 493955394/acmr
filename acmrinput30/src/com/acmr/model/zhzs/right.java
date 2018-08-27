package com.acmr.model.zhzs;


public class right {

    private String id;
    /** number */
    private String indexcode;
    /** 计划编码 */
    private String sort;
    /** 1代表组织，2代表用户 */
    private String depusercode;
    /** 分享的用户或组织 */
    private String right;
    /** 0表示查看；1表示协作；2表示管理 */
    private String createuser;
    /** 创建人 */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIndexcode(String indexcode) {
        this.indexcode = indexcode;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setDepusercode(String depusercode) {
        this.depusercode = depusercode;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getIndexcode() {
        return indexcode;
    }

    public String getSort() {
        return sort;
    }

    public String getDepusercode() {
        return depusercode;
    }

    public String getRight() {
        return right;
    }

    public String getCreateuser() {
        return createuser;
    }
}

