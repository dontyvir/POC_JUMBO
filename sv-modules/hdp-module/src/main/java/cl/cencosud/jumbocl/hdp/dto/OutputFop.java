package cl.cencosud.jumbocl.hdp.dto;

public class OutputFop {
	
	private String id;
	private String name;
	private String descriptionHtml;
	private String logoUrl;
	private String useDiscountAmount;
	private String isLoyalty;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescriptionHtml() {
		return descriptionHtml;
	}
	public void setDescriptionHtml(String descriptionHtml) {
		this.descriptionHtml = descriptionHtml;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getUseDiscountAmount() {
		return useDiscountAmount;
	}
	public void setUseDiscountAmount(String useDiscountAmount) {
		this.useDiscountAmount = useDiscountAmount;
	}
	public String getIsLoyalty() {
		return isLoyalty;
	}
	public void setIsLoyalty(String isLoyalty) {
		this.isLoyalty = isLoyalty;
	}
	public String toString() {
		return "OutputFop [id=" + id + ", name=" + name + ", descriptionHtml="
				+ descriptionHtml + ", logoUrl=" + logoUrl
				+ ", useDiscountAmount=" + useDiscountAmount + ", isLoyalty="
				+ isLoyalty + "]";
	}
	
	
	
}
