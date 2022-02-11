package in.vineetks.shoppingbee.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.vineetks.shoppingbee.dao.OrderDao;
import in.vineetks.shoppingbee.model.Cart;
import in.vineetks.shoppingbee.model.CartItem;
import in.vineetks.shoppingbee.model.Order;
import in.vineetks.shoppingbee.model.OrderItem;


@Service("orderService")
@Transactional
public class OrderService {
	
	@Autowired
	private OrderDao dao;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private CartService cartService;
	
	public List<Order> getAllOrders() {
		return (List<Order>) profileService.getCurrentUser().getOrders();
	}

	public List<Order> getAdminOrders() {
		return (List<Order>) dao.getAllOrders();
	}
	
	public Order getOrderById(int orderId) {
		return dao.getOrderById(orderId);
	}
	
	public void deleteOrder(int orderId) {
		dao.deleteOrder(orderId);
	}
	
	public Order transferCart(int addressId){
		Cart cart = profileService.getCurrentUser().getUserCart();
		Collection<CartItem> cartItems = (Collection<CartItem>) cart.getCartItem();
		Order order = createOrder(addressId);
		Collection<OrderItem> orderItems = (Collection<OrderItem>) order.getOrderItem();
		if(orderItems==null)
			orderItems = new ArrayList<OrderItem>();
		for(CartItem cartItem : cartItems){
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setUserOrder(order);
			orderItems.add(orderItem);
		}
		for(CartItem cartItem : cartItems){
			cartService.removeFromCart(cartItem.getCartItemId());
		}
		order.setOrderItem(orderItems);
		dao.saveOrder(order);
		return order;
	}

	private Order createOrder(int addressId) {
		Order order = new Order();
		order.setOrderDate(new Date());
		order.setOrderAddress(profileService.getAddressById(addressId));
		order.setUser(profileService.getCurrentUser());
		order.setOrderStatus("PROCESSING");
		dao.saveOrder(order);
		return order;
	}

	public void removeOrder(int orderId) {
		dao.deleteOrder(orderId);
	}
}
