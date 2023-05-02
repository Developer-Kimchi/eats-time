package com.eatsTime.main.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eatsTime.Action;
import com.eatsTime.Result;
import com.eatsTime.main.dao.MainDAO;
import com.eatsTime.main.domain.Criteria;
import com.sun.tools.javac.Main;



public class ListOkController implements Action {
	
	@Override
	public Result execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		MainDAO mainDAO = new MainDAO();
		Result result = new Result();
		JSONArray jsonArray = new JSONArray();

		String sort = req.getParameter("sort");
		String category = req.getParameter("category");
		String keyword = req.getParameter("keyword");

		sort = sort == null ? "recent" : sort;

		System.out.println(keyword);
		

		req.setAttribute("category", category);
		String temp = req.getParameter("page");
		int page = temp == null ? 1 : Integer.parseInt(temp);
		HashMap<String, Object> pagable = new HashMap<String, Object>();
		Criteria criteria = new Criteria(page, MainDAO.getTotal(pagable), sort, category);
		pagable.put("offset", criteria.getOffset());
		pagable.put("rowCount", criteria.getRowCount());
		pagable.put("sort", criteria.getSort());
		pagable.put("category", criteria.getCategory());
		

		criteria = new Criteria(page, MainDAO.getTotal(pagable), sort, category);

		mainDAO.selectAll(pagable).stream().map(product -> new JSONObject(product)).forEach(jsonArray::put);

		req.setAttribute("realEndPage", criteria.getRealEndPage());
		req.setAttribute("product", jsonArray.toString());
		req.setAttribute("total", MainDAO.getTotal(pagable));
		
		req.setAttribute("page", page);
		req.setAttribute("startPage", criteria.getStartPage());
		req.setAttribute("endPage", criteria.getEndPage());
		req.setAttribute("prev", criteria.isPrev());
		req.setAttribute("next", criteria.isNext());
		req.setAttribute("sort", sort);

		// 정보를 담고 가니깐 forward 사용
		result.setRedirect(false);
		// forward여서 경로 앞에 추가로 안붙혀도 된다
		// 프론트컨트롤러 안거치고 컨트롤러에서 바로 jsp로 게시글리스트쪽으로 이동
		result.setPath("templates/index.jsp");
		return result;
	}
}
