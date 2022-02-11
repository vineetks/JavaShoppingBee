package in.vineetks.shoppingbee.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.vineetks.shoppingbee.dao.ProductDao;
import in.vineetks.shoppingbee.model.FilterProduct;
import in.vineetks.shoppingbee.model.Product;
import in.vineetks.shoppingbee.model.ProductSpecs;

@Service("productService")
@Transactional
public class ProductService {

	@Autowired
	private ProductDao dao;
	
	@Autowired
	private FilterService filterService;
	
	public List<Product> getAllProducts() {
		return dao.getAllProducts();
	}
	
	public List<Product> getProductsByCategory(String category) {
		return dao.getProductsByCategory(category);
	}
	
	public Product getProductById(int productId) {
		return dao.getProductById(productId);
	}

	public List<ProductSpecs> getProductSpecifications(int productId) {
		return dao.getProductSpecifications(productId);
	}

	public List<Product> getFilteredProducts(String category, String sqlString) {
		return dao.getFilteredProducts(category, sqlString);
	}

	public List<Product> getSearchedProducts(String searchKey) {
		return dao.getSearchProducts(searchKey);
	}
	
	public Product addProduct(String productString){
		JSONObject object = new JSONObject(productString);
		Product product = new Product();
		product.setCategory(object.getString("category"));
		product.setProductName(object.getString("productName"));
		product.setImageSRC(object.getString("imageSRC"));
		product.setPrice(Integer.parseInt(object.getString("price")));
		dao.saveProduct(product);
		saveFilters(object, product);
		saveSpecifications(object, product);
		return product;
	}
	
	private void saveSpecifications(JSONObject object, Product product) {
		JSONArray specNames = (JSONArray) object.get("specName");
		JSONArray specValues = (JSONArray) object.get("specValue");
		Collection<ProductSpecs> specification = (Collection<ProductSpecs>) product.getSpecification();
		if(specification==null)
			specification = new ArrayList<ProductSpecs>();
		for(int i = 0; i < specNames.length(); i++){
			ProductSpecs productSpecs = new ProductSpecs();
			productSpecs.setProduct(product);
			productSpecs.setSpecName(specNames.getString(i));
			productSpecs.setSpecValue(specValues.getString(i));
			specification.add(productSpecs);
		}
		product.setSpecification(specification);
		dao.saveProduct(product);		
	}

	private void saveFilters(JSONObject object, Product product) {
		JSONArray filterIdArray = (JSONArray) object.get("filterId");
		Collection<FilterProduct> filterProducts = (Collection<FilterProduct>) product.getFilterProduct();
		if(filterProducts==null)
			filterProducts = new ArrayList<FilterProduct>();
		for(int i = 0; i < filterIdArray.length(); i++){
			int filterId = Integer.parseInt(filterIdArray.getString(i));
			FilterProduct filterProduct = new FilterProduct();
			filterProduct.setFilter(filterService.getFilterById(filterId));
			filterProduct.setProduct(product);
			filterProducts.add(filterProduct);
		}
		product.setFilterProduct(filterProducts);
		dao.saveProduct(product);
	}

	public List<Product> modifyProduct(Product product){
		return dao.saveProduct(product);
	}
}
