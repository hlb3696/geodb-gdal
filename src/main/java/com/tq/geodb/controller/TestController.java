package com.tq.geodb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.ogr;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tq.geodb.gdal.GdalHolder;
import com.tq.geodb.tool.JsonResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestController extends BaseController{
	 /*
     * 首页
     */
    @RequestMapping(value= {"/","/index"})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        return getView("index");
    }
	/*
     * 加载动态库
     */
    @RequestMapping("/api/gdal/load")
    public JsonResult<String> load() {
    	GdalHolder.loadLibrary();
    	return this.renderSuccess("加载完成！");
    }
	/*
     * 注册驱动
     */
    @RequestMapping("/api/gdal/register")
    public JsonResult<String> register() {
    	GdalHolder.registerAll();
    	return this.renderSuccess("注册驱动！");
    }
	/*
     * 测试shp转geojson
     */
    @RequestMapping("/api/gdal/test/shp")
    public JsonResult<String> test() {
    	//String shpFileName="D:\\usr\\test\\zzt_headCells.shp";
		//String geojsonFileName="D:\\usr\\test\\zzt_headCells.geojson";

		String shpFileName="/opt/shp/线.shp";
		String geojsonFileName="/opt/shp/线.geojson";

		
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
	    	return JsonResult.FAIL;
	    }
		System.out.println("打开文件成功！" );
		org.gdal.ogr.Driver dv = ogr.GetDriverByName("GeoJSON");
		if (dv == null){
			System.out.println("打开驱动失败！" );
			return JsonResult.FAIL;
		}
		System.out.println("打开驱动成功！" );
		//输出geojson的位置及文件名
		dv.CopyDataSource(ds, geojsonFileName);
		System.out.println("转换成功！");
		return JsonResult.SUCCESS;
    }


    //根据经纬度查询海拔
	@RequestMapping("/api/gdal/selectAltitude")
	public JsonResult<Integer> selectAltitude(double lon,double lat){
		Integer altitude = GdalHolder.selectAltitude(lon, lat);
		return this.renderSuccess(altitude);
	}





}
