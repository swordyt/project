package entity.entity;

public class t_ajd_agent {
	private int iAutoID;
	private int iUserID;	//用户ID
	private String sName;	//姓名
	private int iBankBranchID;	//银行ID
	private int iIsCooperative;	//是否合作关系(0否，1是）
	private String sMobile;	//手机号
	private String sServiceNo;	//专属服务ID
	private int iDispatchIndex;	//接单序号
	private String sIdCardPath;	//身份证附件地址
	private int iStatus;	//状态（1有效，2无效）
	private int iCreateTime;	//创建时间
	private int iUpdateTime;	//更新时间
	public int getiAutoID() {
		return iAutoID;
	}
	public void setiAutoID(int iAutoID) {
		this.iAutoID = iAutoID;
	}
	public int getiUserID() {
		return iUserID;
	}
	public void setiUserID(int iUserID) {
		this.iUserID = iUserID;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public int getiBankBranchID() {
		return iBankBranchID;
	}
	public void setiBankBranchID(int iBankBranchID) {
		this.iBankBranchID = iBankBranchID;
	}
	public int getiIsCooperative() {
		return iIsCooperative;
	}
	public void setiIsCooperative(int iIsCooperative) {
		this.iIsCooperative = iIsCooperative;
	}
	public String getsMobile() {
		return sMobile;
	}
	public void setsMobile(String sMobile) {
		this.sMobile = sMobile;
	}
	public String getsServiceNo() {
		return sServiceNo;
	}
	public void setsServiceNo(String sServiceNo) {
		this.sServiceNo = sServiceNo;
	}
	public int getiDispatchIndex() {
		return iDispatchIndex;
	}
	public void setiDispatchIndex(int iDispatchIndex) {
		this.iDispatchIndex = iDispatchIndex;
	}
	public String getsIdCardPath() {
		return sIdCardPath;
	}
	public void setsIdCardPath(String sIdCardPath) {
		this.sIdCardPath = sIdCardPath;
	}
	public int getiStatus() {
		return iStatus;
	}
	public void setiStatus(int iStatus) {
		this.iStatus = iStatus;
	}
	public int getiCreateTime() {
		return iCreateTime;
	}
	public void setiCreateTime(int iCreateTime) {
		this.iCreateTime = iCreateTime;
	}
	public int getiUpdateTime() {
		return iUpdateTime;
	}
	public void setiUpdateTime(int iUpdateTime) {
		this.iUpdateTime = iUpdateTime;
	}
	
}
