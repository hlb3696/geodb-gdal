package com.tq.geodb.gdal;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.ogr.ogr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class GdalHolder {
	private static final Logger log = LoggerFactory.getLogger(GdalHolder.class);
	/*
	 ** gdal初始化
	 * 1、载入c++动态库
	 * 2、注册gdal驱动
    static {
    	log.info("---GdalHolder--gdal初始化");
		loadLibrary();
		registerAll();
    }
     */
    
    public static void registerAll() {
    	log.info("---GdalHolder--gdal注册所有的驱动--开始");
		// 注册所有的驱动
        ogr.RegisterAll();
    	//支持中文路径
		gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES"); 
		//支持中文字段
		gdal.SetConfigOption("SHAPE_ENCODING","CP936");
		log.info("---GdalHolder--gdal注册所有的驱动--结束");
	}

    public static void loadLibrary() {
	    log.info("---GdalLibrary--gdal载入动态库");
		try {
			//根据系统环境加载资源
    		String systemType = System.getProperty("os.name");   
    	    String file="";  
    	    boolean isWin=systemType.toLowerCase().indexOf("win")!=-1;
    	    if(isWin) {
    	    	file="/gdal/win32/gdalalljni.dll";
				//从资源文件加载动态库
				LibraryUtil.loadFromResource(file);
    	    }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * @Description:  根据经纬度获取某个点的海拔
	 * @Param: [lon 经度, lat 纬度]
	 * @return: java.lang.Integer
	 * @Author: libin.Hao
	 * @Date: 2021/11/10
	 */
	public static Integer selectAltitude (double lon,double lat){
		//海拔
		Integer altitude =0;
		//支持所有驱动
		gdal.AllRegister();
		//支持中文路径
		gdal.SetConfigOption("gdal_FILENAME_IS_UTF8", "YES");
		//要读取的文件
		String fileName_tif = "/opt/gao_Level_17.tif";
		//只读方式读取数据
		Dataset hDataset = gdal.Open(fileName_tif, gdalconstConstants.GA_ReadOnly);
		//判断是否非空
		if (hDataset == null)
		{
			System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());
			System.exit(1);
		}
		//图像的列和行
		Driver hDriver = hDataset.GetDriver();
		System.out.println("Driver: " + hDriver.getShortName() + "/" + hDriver.getLongName());
		int iXSize = hDataset.getRasterXSize();
		int iYSize = hDataset.getRasterYSize();
		Band band = hDataset.GetRasterBand(1);

		//图像六要素
		double[] dGeoTrans = hDataset.GetGeoTransform();
		//经纬度转行列号
		double dTemp = dGeoTrans[1] * dGeoTrans[5] - dGeoTrans[2] * dGeoTrans[4];
		int	Xline = (int)((dGeoTrans[5] * (lon - dGeoTrans[0]) -dGeoTrans[2] * (lat - dGeoTrans[3])) / dTemp + 0.5) ;
		int	Yline = (int)((dGeoTrans[1] * (lat - dGeoTrans[3]) -dGeoTrans[4] * (lon - dGeoTrans[0])) / dTemp + 0.5);
		//这里是DEM数据，所以声明一个int数组来存储，如果是其他数据类型，声明相应的类型即可

		int buf[] = new int[iXSize];
		//循环遍历取出像元值
		for(int i=0; i<iYSize; i++){
			band.ReadRaster(0, i, iXSize, 1, buf);	//读取一行数据
			// 下面是输出像元值
			for(int j=0; j < iXSize; j++){
				if(i == Yline && j == Xline){
					//System.out.println("海拔是："+buf[j]+"米");
					altitude = buf[j];
				}
			}
		}
		hDataset.delete();
		// 可选
		gdal.GDALDestroyDriverManager();
		return altitude;
	}
}
