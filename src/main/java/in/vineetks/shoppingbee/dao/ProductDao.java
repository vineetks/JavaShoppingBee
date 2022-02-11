package in.vineetks.shoppingbee.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import in.vineetks.shoppingbee.model.Product;
import in.vineetks.shoppingbee.model.ProductSpecs;

@Repository("productDao")
@SuppressWarnings("unchecked")
public class ProductDao extends AbstractDao<Integer, Product>{

	public List<Product> getAllProducts() {
		Criteria criteria = createEntityCriteria();
		return (List<Product>) criteria.list();
	}
	
	public List<Product> getProductsByCategory(String category) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("category", category));
		return (List<Product>) criteria.list();
	}

	public Product getProductById(int productId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("productId", productId));
		return (Product) criteria.uniqueResult();
	}

	public List<ProductSpecs> getProductSpecifications(int productId) {
		Product product = getProductById(productId);
		return (List<ProductSpecs>) product.getSpecification();
	}

//	public List<Filter> getFiltersByCategory(String category) {
//		Criteria criteria = getSession().createCriteria(Filter.class);
//		criteria.add(Restrictions.eq("category", category));
//		return criteria.list();
//	}

	public List<Product> getFilteredProducts(String category, String sqlString) {
		if(sqlString=="")
			return getProductsByCategory(category);
		String[] queryParts = sqlString.split("@@");
		String sqlQuery = "";
		for(int i=0; i<queryParts.length; i++){  
			if(i==0)
				sqlQuery += "SELECT productId from FilterProduct WHERE " + queryParts[i];
			else
				sqlQuery += " AND productId IN (SELECT productId from FilterProduct WHERE " + queryParts[i] + ")"; 
		} 
		Query query = getSession().createSQLQuery(sqlQuery);
		List<Integer> productIdList = query.list();
		List<Product> productList = new ArrayList<Product>();
		for(int productId: productIdList){  
			productList.add(getProductById(productId));
		}  
		return productList;
	}

	public List<Product> getSearchProducts(String searchKey) {
		String[] keys = searchKey.split(" ");
		Criteria criteria = createEntityCriteria();;
		for(String key: keys){
			criteria.add(Restrictions.ilike("productName", "%"+key+"%"));
		}
		criteria.setProjection(Projections.projectionList()
			      .add(Projections.property("productId"), "productId")
			      .add(Projections.property("productName"), "productName"));
		criteria.setMaxResults(10);
		return criteria.list();
	}

	public List<Product> saveProduct(Product product) {
		persist(product);
		return getAllProducts();
	}
	
}
