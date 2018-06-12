package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductImg;

//按照方法名的升序执行test方法
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest {

	@Autowired
	private ProductImgDao productImgDao;

	@Test
	public void testABatchInsertProductImg() throws Exception {
		// productId为1的商品里添加两个详情图片记录
		ProductImg productImg = new ProductImg();
		productImg.setImgAddr("图片1");
		productImg.setImgDesc("测试图片1");
		productImg.setPriority(1);
		productImg.setCreateTime(new Date());
		productImg.setProductId(1L);

		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("图片2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(1L);

		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg);
		productImgList.add(productImg2);

		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2,effectedNum);
	}
	@Test
	public void testBQueryProductImgList() throws Exception{
		long productId = 1L;
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		for (ProductImg productImg : productImgList) {
			System.out.println(productImg.getImgAddr());
		}
		assertEquals(2, productImgList.size());
	}
	
	@Test
	public void testCDeleteProductImgByProductId() throws Exception{
		//删除新增的两条商品详情图片记录
		long productId = 1L;
		int effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectedNum);
	}
}
