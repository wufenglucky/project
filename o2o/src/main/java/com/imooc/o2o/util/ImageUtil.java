package com.imooc.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

//import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.imooc.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	// 获取classPath的绝对值路径
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();

	/**
	 * 处理缩略图，并返回新生成图片的相对值路径
	 * 
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());// 文件的扩展名
		makeDirPath(targetAddr);// 创建target路径的目录
		String relativeAddr = targetAddr + realFileName + extension;// 相对路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return relativeAddr;
	}
	/**
	 * 处理详情图，并返回新生成图片的相对值路径
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		// 获取不重复的随机名
		String realFileName = getRandomFileName();
		// 获取文件的扩展名如png,jpg等
		String extension = getFileExtension(thumbnail.getImageName());
		// 如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;// 相对路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f)
					.outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return relativeAddr;
	}

	/**
	 * 创建目标路径所涉及到的目录，即/home/work/wufeng/xxx.jpg， 那么home work wufeng 这三个文件夹都得自动创建
	 * 
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;// 获取绝对路径
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();// 会创建所有的目录
		}
	}

	/**
	 * 获取输入文件流的扩展名
	 * 
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String cFile) {
		return cFile.substring(cFile.lastIndexOf("."));
	}

	/**
	 * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
	 * 
	 * @return
	 */
	public static String getRandomFileName() {
		// 获取随机五位数
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}

	// waterMark(arg1,arg2,arg3)方法中有三个参数分别为：图片的位置，图片的绝对值路径，图片的透明度。
	public static void main(String[] args) throws IOException {
		Thumbnails.of(new File("/Users/Administrator/Image/xiaohuangren.jpg")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f)
				.outputQuality(0.8f).toFile("/Users/Administrator/Image/xiaohuangrennew.jpg");
	}

	/**
	 * storePath是文件的路径还是目录的路径 如果storePath是文件路径，则删除文件
	 * 如果stroePath是目录路径，则删除该目录下的所有文件
	 * 
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if (fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) {
				File[] files = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
}
