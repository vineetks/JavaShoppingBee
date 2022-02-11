package in.vineetks.shoppingbee.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="Orders")
public class Order {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int orderId;
	
	@Temporal(TemporalType.DATE)
	private Date orderDate;
	private String orderStatus;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="userId")
	private UserProfile user;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="userOrder", cascade=CascadeType.ALL)
	private Collection<OrderItem> orderItem;
	
	@OneToOne
	@JoinColumn(name="addressId")
	private Address orderAddress;
	
	//Getters and Setters
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public Collection<OrderItem> getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(Collection<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}

	public Address getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(Address orderAddress) {
		this.orderAddress = orderAddress;
	}
}
