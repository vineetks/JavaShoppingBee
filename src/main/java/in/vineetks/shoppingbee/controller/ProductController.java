package in.vineetks.shoppingbee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.vineetks.shoppingbee.model.Product;
import in.vineetks.shoppingbee.model.ProductSpecs;
import in.vineetks.shoppingbee.service.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Product> listAllProducts() {
		return productService.getAllProducts();
	}
	
	@RequestMapping(value = "/{category}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Product> listProductsByCategory(@PathVariable("category") String category) {
		return productService.getProductsByCategory(category);
	}
	
	@RequestMapping(value = "/getById/{productId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Product listProductById(@PathVariable("productId") int productId) {
		return productService.getProductById(productId);
	}
	
	@RequestMapping(value = "/getById/{productId}/specs", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<ProductSpecs> listProductSpecifications(@PathVariable("productId") int productId) {
		return productService.getProductSpecifications(productId);
	}
	
	//@RequestMapping(value = "/{category}/getFilteredProducts", method = RequestMethod.POST,	consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/{category}/getFilteredProducts", method = RequestMethod.GET,	produces = "application/json")
	public @ResponseBody List<Product> listFilteredProducts(@PathVariable("category") String category, @RequestParam("sqlString") String sqlString
			/*, @RequestBody String sqlString*/) {
		return productService.getFilteredProducts(category, sqlString);
	}
	
	@RequestMapping(value = "/search/{searchKey}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Product> listSearchedProducts(@PathVariable("searchKey") String searchKey) {
		return productService.getSearchedProducts(searchKey);
	}
}
