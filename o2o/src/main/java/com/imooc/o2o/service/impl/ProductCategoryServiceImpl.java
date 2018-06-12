package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exception.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;

	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if (effectedNum <= 0) {
					throw new ProductCategoryOperationException("店铺类别创建失败");
				} else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new ProductCategoryOperationException("batchAddProductCategory error:" + e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	// 由于在将来需要先将商品类别id置空，再删除数据库中的product_category，所以需要调用事物来管理
	// 这样的好处：当第一步成功了之后，它并不急于提交，而是等待第二步成功之后才提交，如果第二步失败的话
	// 第一步也不会提交，这样就达到了回滚的目的
	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		//解除tb_product里的商品与该productCategory的关联
		try {
			int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
			if (effectedNum < 0) {
				throw new ProductCategoryOperationException("商品类别更新失败");
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
		}
		//删除该productCategory
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectedNum <= 0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
		}
	}

}
