package com.bhyh.model.vo;
import java.util.Date;

/**
 * 这是一个类，用来统计识别服务器的识别结果
 * @author david_lin
 *
 */
public class ImgResSummaryVO implements Comparable<ImgResSummaryVO>{
    private int id;//主键
    private String groupId;//格式组成：type_level_parentFileName
    private String parentGroupId;//格式组成：type_level_parentFileName
    private String type;//图片识别类型hardhat, smoke, uniform, ladder, phone, car_lane, fire, walker
	private String path;//路径
	private String parentFileName;
	private int checked;//检测到数量
	private int unchecked;//未检测到数量
	private Date createTime;//创建时间
    private String remarks;//备注
	private int total;//图片总数量
	private int level;//从第几层
	private long costTime;//检测一张花费的时间
	
	public String getParentFileName() {
		return parentFileName;
	}
	public void setParentFileName(String parentFileName) {
		this.parentFileName = parentFileName;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public ImgResSummaryVO() {
		super();
		
	}
	/**
	 * 先比较属性type，相同后再比较path
	 */
	@Override
	public int compareTo(ImgResSummaryVO vo) {
		int temp = this.getType().compareTo(vo.getType());
		return temp == 0 ? this.getPath().compareTo(vo.getPath()) : temp ;
	}
    
}
