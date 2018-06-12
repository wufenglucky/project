package com.imooc.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exception.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Override
	@Transactional
	// 1、处理缩略图，获取缩略图的相对路径并product
	// 2、往tb_product写入商品信息，获取productId
	// 3、结合productId批量处理商品详情图
	// 4、将商品详情图列表批量插入tb_product_img中
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		if (product != null && product.getShop() != null && product.getShop().getShopId() > 0) {
			// 给商品设置上默认的属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 默认为上架的状态
			product.setEnableStatus(1);
			// 若商品缩略图不为空，则添加
			if (thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			try {
				// 创建商品信息
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品失败" + e.toString());
			}
			// 若商品详情图不为空，则添加
			if (productImgHolderList != null && productImgHolderList.size() > 0) {
				addproductImgList(product, productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/**
	 * 批量添加商品图片
	 * 
	 * @param product
	 * @param productImgHolderList
	 */
	private void addproductImgList(Product product, List<ImageHolder> productImgHolderList) {
		// 获取图片存取路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		// 遍历图片一次，去处理，并添加进ProductImg实体类里面
		for (ImageHolder productImgHolder : productImgHolderList) {
			// 处理图片并返回图片的相对路径
			String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		// 如果确实有图片需要添加，就执行批量添加操作
		if (productImgList.size() > 0) {
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败" + e.toString());
			}
		}

	}

	/**
	 * 添加缩略图
	 * 
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		// 通过shop_id获取店铺图片的相对路径
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}

	@Override
	public Product getProductById(long productId) {
		// TODO Auto-generated method stub
		return productDao.queryProductById(productId);
	}

	@Override
	@Transactional
	// 1、若缩略图参数有值，则处理缩略图
	// 若原先存在缩略图，则先删除在添加新图，之后获取缩略图的相对路径，并赋值给product
	// 2、若商品详情图列表参数有值，对商品详情图列表进行步骤2操作
	// 3、将tr_product_img下面的，该商品原先的商品详情图记录全部清除
	// 4、更新tb_product以及tb_product_img的信息
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() > 0) {
			// 给商品设置上默认的属性
			product.setLastEditTime(new Date());
			// 若商品缩略图不为空，且原有缩略图不为空，则删除原有的缩略图并添加新的
			if (thumbnail != null) {
				// 现获取一遍原有的信息，因为原来的信息里有原图片的地址
				Product temProduct = productDao.queryProductById(product.getProductId());
				if (temProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(temProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			// 如果有新存入的商品详情图，则将原先的删除，并添加新的图片
			if (productImgHolderList != null && productImgHolderList.size() > 0) {
				deleteProductImgList(product.getProductId());
				addproductImgList(product, productImgHolderList);
			}
			try {
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new ProductOperationException("更新商品信息失败" + e.toString());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/**
	 * 删除某个商品下的所有详情图
	 * 
	 * @param productId
	 */
	private void deleteProductImgList(long productId) {
		// 根据productId获取原来的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		// 干掉原来的图片
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		// 删除数据库里原有的图片信息
		productImgDao.deleteProductImgByProductId(productId);
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// 将页码转换为数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		// 基于同样的查询条件，返回该查询条件下的商品总数
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		if(productList != null){
			pe.setProductList(productList);
			pe.setCount(count);
		}else {
			pe.setState(ProductStateEnum.INNER_ERROR.getState());
		}
		return pe;
	}

}
