package com.tq.geodb.gdal;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.ogr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class HelloGDAL {
	private static final Logger log = LoggerFactory.getLogger(HelloGDAL.class);
	static {
		log.info("---LibraryUtil--gdal载入动态库");
		try {
			//根据系统环境加载资源
    		String systemType = System.getProperty("os.name");   
    	    String file="";  
    	    boolean isWin=systemType.toLowerCase().indexOf("win")!=-1;
    	    if(isWin) {
    	    	file="/gdal/win32/gdalalljni.dll";
    	    }
    	    else {
    	    	file="/gdal/linux/libgdalalljni.so";
    	    }
    	    //从资源文件加载动态库
    	    LibraryUtil.loadFromResource(file);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
	
	public static void main(String[] args) {
		String shpFileName="\\usr\\test\\zzt_headCells.shp";
		String geojsonFileName="\\usr\\test\\zzt_headCells.geojson";
		shpToGeojson(shpFileName,geojsonFileName);
	}
	/**
	 * shp转换geojson
	 * @param shpFileName
	 * @param geojsonFileName
	 */
	public static void shpToGeojson(String shpFileName,String geojsonFileName) {
		log.info("---gdal注册所有的驱动开始");
		// 注册所有的驱动
		ogr.RegisterAll();
		log.info("---gdal注册所有的驱动结束");
		
	    // 为了支持中文路径，请添加下面这句代码  
	    gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
	    // 为了使属性表字段支持中文，请添加下面这句  
	    gdal.SetConfigOption("SHAPE_ENCODING","");
	    //打开数据
	    DataSource ds = ogr.Open(shpFileName,0);
	    if (ds == null){
	    	System.out.println("打开文件"+shpFileName+"失败！" );
	    	return;
	    }
		System.out.println("打开文件成功！" );
		org.gdal.ogr.Driver dv = ogr.GetDriverByName("GeoJSON");
		if (dv == null){
			System.out.println("打开驱动失败！" );
			return;
		}
		System.out.println("打开驱动成功！" );
		//输出geojson的位置及文件名
		dv.CopyDataSource(ds, geojsonFileName);
		System.out.println("转换成功！");
	}
	
	
	
	
	/**
	 * 读取tif文件的相关信息
	 * @param fileName
	 */
	public static void readGDAL(String fileName) {
		gdal.AllRegister();
		//读取影像数据
		Dataset dataset = gdal.Open(fileName,gdalconstConstants.GA_ReadOnly);
		if(dataset == null){
			System.err.println("GDALOpen failed - "+gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());
			System.exit(1);
		}
		
		Driver driver = dataset.GetDriver();
		System.out.println("Driver:"+driver.getShortName()+"/"+driver.getLongName());
		
		//读取影像信息
		int xSize = dataset.getRasterXSize();
		int ySize = dataset.getRasterYSize();
		int bandCount = dataset.GetRasterCount();
		System.out.println("size is "+xSize+","+ySize+",光带数："+bandCount);
		
		Band band = dataset.GetRasterBand(1);
		int type = band.GetRasterDataType();
		//type为1，代表的是Eight bit unsigned integer
		System.out.println(type);
		
		dataset.delete();
		gdal.GDALDestroyDriverManager();
	}
	
	
	/**
	 * 获取每个点的高程值
	 * @param fileName_tif
	 */
	public static void getElevationFromTif(String fileName_tif) {
		gdal.AllRegister();
		Dataset hDataset = gdal.Open(fileName_tif, gdalconstConstants.GA_ReadOnly);
		if (hDataset == null)
		{
			System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());

			System.exit(1);
		}

		Driver hDriver = hDataset.GetDriver();
		System.out.println("Driver: " + hDriver.getShortName() + "/" + hDriver.getLongName());
		int iXSize = hDataset.getRasterXSize();
		int iYSize = hDataset.getRasterYSize();
		System.out.println("Size is " + iXSize + ", " + iYSize);

		Band band = hDataset.GetRasterBand(1);
		//这里是DEM数据，所以声明一个int数组来存储，如果是其他数据类型，声明相应的类型即可
		int buf[] = new int[iXSize];	
		
		for(int i=0; i<50/*iYSize*/; i++)
		{
			band.ReadRaster(0, i, iXSize, 1, buf);	//读取一行数据
			
			// 下面是输出像元值，为了方便，我只输出了左上角 10×10的范围内的数据
			for(int j=0; j<50/*iXSize*/; j++){
				System.out.print(buf[j] + ", ");
			}
				
			System.out.println("\n");
		}
		hDataset.delete();
		
		// 可选
		gdal.GDALDestroyDriverManager();
	}
}
