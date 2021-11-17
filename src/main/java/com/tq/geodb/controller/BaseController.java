package com.tq.geodb.controller;

import org.springframework.web.servlet.ModelAndView;

import com.tq.geodb.tool.JsonResult;


public class BaseController {
    protected ModelAndView getView(String viewName) {
    	ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName(viewName);
        return modelAndView;
	}
    protected <T> JsonResult<T> renderSuccess(T data) {
		return this.render(JsonResult.SUCCESS_CODE,data,"成功");
	}
	/**
	 * 返回R
	 *
	 * @param code 状态码
	 * @param data 数据
	 * @param msg  消息
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	protected <T> JsonResult<T> render(int code, T data, String msg) {
		return new JsonResult<>(code, data, msg);
	}
}
