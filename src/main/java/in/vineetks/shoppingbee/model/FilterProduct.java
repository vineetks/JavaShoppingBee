package in.vineetks.shoppingbee.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class FilterProduct {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int filterProductId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="filterId")
	private Filter filter;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="productId")
	private Product product;

	//Getters and Setters
	public int getFilterProductId() {
		return filterProductId;
	}

	public void setFilterProductId(int filterProductId) {
		this.filterProductId = filterProductId;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
