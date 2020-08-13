package com.bhyh.model.po;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 这是一个统计识别图片结果PO
 * @author david_lin
 *
 */
@Entity
@Table(name = "bh_img_res_summary")
public class ImgResSummaryPO implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private int id;//主键
    @Column(name = "group_id")
    private String groupId;//格式组成：type_level_parentFileName
    @Column(name = "parent_group_id")
    private String parentGroupId;//格式组成：type_level_parentFileName
    @Column(name = "parent_file_name")
    private String parentFileName;
    @Column(name = "type")
    private String type;//图片识别类型hardhat, smoke, uniform, ladder, phone, car_lane, fire, walker
    @Column(name = "path")
	private String path;//路径
    @Column(name = "checked")
	private int checked;//检测到数量
    @Column(name = "unchecked")
	private int unchecked;//未检测到数量
    @Column(name = "create_time")
	private Date createTime;//创建时间
    @Column(name = "remarks")
    private String remarks;//备注
    @Column(name = "total")
	private int total;//图片总数量
    @Column(name = "level")
	private int level;//从第几层
    @Column(name = "cost_time")
	private long costTime;//检测一张花费的时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getParentGroupId() {
		return parentGroupId;
	}
	public void setParentGroupId(String parentGroupId) {
		this.parentGroupId = parentGroupId;
	}
	public String getParentFileName() {
		return parentFileName;
	}
	public void setParentFileName(String parentFileName) {
		this.parentFileName = parentFileName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getCostTime() {
		return costTime;
	}
	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public int getUnchecked() {
		return unchecked;
	}
	public void setUnchecked(int unchecked) {
		this.unchecked = unchecked;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public ImgResSummaryPO() {
		super();
		
	}
    
}
