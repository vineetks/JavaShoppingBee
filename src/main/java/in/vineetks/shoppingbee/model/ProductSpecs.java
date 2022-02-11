package in.vineetks.shoppingbee.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ProductSpecs {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int productSpecsId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;
	private String specName;
	private String specValue;
	
	//Getters and Setters
	public int getProductSpecsId() {
		return productSpecsId;
	}
	public void setProductSpecsId(int productSpecsId) {
		this.productSpecsId = productSpecsId;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public String getSpecValue() {
		return specValue;
	}
	public void setSpecValue(String specvalue) {
		this.specValue = specvalue;
	}
}
