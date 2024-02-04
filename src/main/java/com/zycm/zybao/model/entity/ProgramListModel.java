package com.zycm.zybao.model.entity;

import java.util.Date;

/**
* @ClassName: ProgramListModel
* @Description: 节目单
* @author sy
* @date 2017年9月30日 下午2:51:52
*
*/
public class ProgramListModel {
    private Integer listId;

    private String listName;

    private Date createTime;

    private Integer creatorId;

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName == null ? null : listName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }
}
