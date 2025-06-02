package Model;

import Config.date;

public class AdminModel {
    private String adm_id;
    private String admName;
    private String email;
    private date hiringDate;
    private boolean isManager;
    private boolean isActive;
    private boolean isAgreement;
    private String dep_id;
    private String post_id;
    private String password;
    private String depName;
    
	public AdminModel(int int1, String string, String string2) {
		// TODO Auto-generated constructor stub
	}
	public String getAdm_id() {
		return adm_id;
	}
	public void setAdm_id(String adm_id) {
		this.adm_id = adm_id;
	}
	public String getAdmName() {
		return admName;
	}
	public void setAdmName(String admName) {
		this.admName = admName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public date getHiringDate() {
		return hiringDate;
	}
	public void setHiringDate(date hiringDate) {
		this.hiringDate = hiringDate;
	}
	public boolean isManager() {
		return isManager;
	}
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isAgreement() {
		return isAgreement;
	}
	public void setAgreement(boolean isAgreement) {
		this.isAgreement = isAgreement;
	}
	public String getDep_id() {
		return dep_id;
	}
	public void setDep_id(String dep_id) {
		this.dep_id = dep_id;
	}
	public String getPost_id() {
		return post_id;
	}
	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
    
}

