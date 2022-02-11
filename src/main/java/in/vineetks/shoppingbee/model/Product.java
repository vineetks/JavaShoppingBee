package in.vineetks.shoppingbee.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int productId;
	private String productName;
	private int price;
	private String imageSRC;
	private String category;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	private Collection<ProductSpecs> specification;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE )
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	private Collection<FilterProduct> filterProduct;
		
	private String description;
	
	//Getters and Setters
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImageSRC() {
		return imageSRC;
	}
	public void setImageSRC(String imageSRC) {
		this.imageSRC = imageSRC;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Collection<ProductSpecs> getSpecification() {
		return specification;
	}
	public void setSpecification(Collection<ProductSpecs> specification) {
		this.specification = specification;
	}
	public Collection<FilterProduct> getFilterProduct() {
		return filterProduct;
	}
	public void setFilterProduct(Collection<FilterProduct> filterProduct) {
		this.filterProduct = filterProduct;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
