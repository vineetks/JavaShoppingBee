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
public class Filter {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int filterId;
	private String category;
	private String filterName;
	private String filterValue;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE )
	@OneToMany(mappedBy="filter", cascade=CascadeType.ALL)
	private Collection<FilterProduct> filterProduct;
	
	//Getter and Setters
	public int getFilterId() {
		return filterId;
	}
	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	public String getFilterValue() {
		return filterValue;
	}
	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
}

enum Category { ELECTRONICS, SPORTS, BOOKS, CLOTHIONG }  
