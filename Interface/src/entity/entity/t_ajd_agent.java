package entity.entity;

public class t_ajd_agent {
	private int iAutoID;
	private int iUserID;	//�û�ID
	private String sName;	//����
	private int iBankBranchID;	//����ID
	private int iIsCooperative;	//�Ƿ������ϵ(0��1�ǣ�
	private String sMobile;	//�ֻ���
	private String sServiceNo;	//ר������ID
	private int iDispatchIndex;	//�ӵ����
	private String sIdCardPath;	//���֤������ַ
	private int iStatus;	//״̬��1��Ч��2��Ч��
	private int iCreateTime;	//����ʱ��
	private int iUpdateTime;	//����ʱ��
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
