package in.vineetks.shoppingbee.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class OrderItem {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int orderItemId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="orderId")
	private Order userOrder;

	
	@OneToOne
	@JoinColumn(name="productId")
	private Product product;
	
	private int quantity;
	
	//Getters and Setters
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
	public Order getUserOrder() {
		return userOrder;
	}
	public void setUserOrder(Order userOrder) {
		this.userOrder = userOrder;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
