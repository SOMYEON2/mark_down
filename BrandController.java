package com.pcwk.ehr.brand;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pcwk.ehr.MessageVO;
import com.pcwk.ehr.SearchVO;
import com.pcwk.ehr.StringUtil;

@Controller
public class BrandController {

	final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	BrandServiceImpl service;
	
	public BrandController() {}
	
	
	
	/**
	 * 브랜드 검색
	 * @param 
	 * @return JSON(1:성공, 0:실패)
	 * @throws RuntimeException
	 * @throws SQLException
	 */	
	@RequestMapping(value="brand/doRetrieve.do"
			,method=RequestMethod.GET
			,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doRetrieve(SearchVO searchVO)throws SQLException{
		
		if(0==searchVO.getPageNum()) {
			searchVO.setPageNum(1);
		}
		      
		//pageSize
		if(0==searchVO.getPageSize()) {
			searchVO.setPageSize(10);
		}	
		
		LOG.debug("================================");
		LOG.debug("=param:" + searchVO);
		LOG.debug("================================");

		List<BrandVO> list = (List<BrandVO>) this.service.doRetrieve(searchVO);
	
		for(BrandVO vo: list) {
			LOG.debug("=vo="+vo);
		}
		
		Gson gson = new Gson();
		
		String jsonList = gson.toJson(list);
		LOG.debug("================================");
		LOG.debug("=jsonList:" + jsonList);
		LOG.debug("================================");
		
		return jsonList;
	}

	
	
	
	/**
	 * 브랜드 단건 조회
	 * @param 
	 * @return JSON(Brand)
	 * @throws RuntimeException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@RequestMapping(value="brand/doSelectone.do"
			,method=RequestMethod.GET
			,produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String doSelectOne(BrandVO inVO, Model model) throws SQLException, ClassNotFoundException {
		LOG.debug("================================");
		LOG.debug("=doSelectOne=");
		LOG.debug("=param:" + inVO);
		LOG.debug("================================");
		
		BrandVO outVO = service.doSelectOne(inVO);
		LOG.debug("=outVO:" + outVO);
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(outVO);		
		LOG.debug("================================");
		LOG.debug("=jsonStr:" + jsonStr);
		LOG.debug("================================");
		
		return jsonStr;
	
	}


	
	
	/**
	 * 브랜드 수정
	 * @param 
	 * @return JSON(1:성공, 0:실패)
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	@RequestMapping(value="brand/doUpdate.do"
			,method = RequestMethod.POST
			,produces = "application/json;charset=UTF-8"
			)
	@ResponseBody
	public String doUpdate(BrandVO brand) throws SQLException{
        MessageVO  messageVO = new MessageVO();
		LOG.debug("=====================================");
		LOG.debug("=brand="+brand);
		LOG.debug("=====================================");	
		
		int flag  = service.doUpdate(brand);
		
		String messageStr = "";
		
		if(1==flag) {
			messageStr = "수정 되었습니다.";
		}else {
			messageStr = "수정 실패";
		}
		
		messageVO.setMsgId(String.valueOf(flag));
		messageVO.setMsgContents(messageStr);
		LOG.debug("=====================================");
		LOG.debug("=messageVO="+messageVO);
		LOG.debug("=====================================");	
		
		Gson gson=new Gson();
		String jsonStr = gson.toJson(messageVO);
		LOG.debug("=jsonStr="+jsonStr);
		return jsonStr;
	}
	
	
	

	/**
	 * 브랜드 삭제
	 * @param 
	 * @return JSON(1:성공, 0:실패)
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	@RequestMapping(value="brand/doDelete.do"
			,method = RequestMethod.GET
			,produces ="application/json;charset=UTF-8" )
	@ResponseBody	
	public String doDelete(HttpServletRequest req) throws SQLException{
		BrandVO inVO =new BrandVO();
		MessageVO messageVO=new MessageVO();
		LOG.debug("=====================================");
		LOG.debug("=doSelectOne=");
		
		LOG.debug("=====================================");
		
		String brandCode = req.getParameter("bCode");
		inVO.setbCode(brandCode);
		LOG.debug("=param="+inVO);
		int flag = this.service.doDelete(inVO);
		
		String resultMsg = "";
		if(1==flag) {
			resultMsg = "삭제 되었습니다.";
		}else {
			resultMsg = "삭제 실패.";
		}
		messageVO.setMsgId(String.valueOf(flag));
		messageVO.setMsgContents(resultMsg);
		
		Gson gson=new Gson();
		String jsonStr = gson.toJson(messageVO);
		LOG.debug("=====================================");
		LOG.debug("=jsonStr="+jsonStr);
		LOG.debug("=====================================");		
		
		return jsonStr;
	}
	
	
	@RequestMapping(value="brand/doInsert.do"
			,method=RequestMethod.POST
			,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doInsert(HttpServletRequest req) throws ClassNotFoundException, SQLException{
		BrandVO inVO = new BrandVO();
		MessageVO messageVO = new MessageVO();
		
		inVO.setbCode(req.getParameter("bCode"));			//브랜드 번호
		inVO.setbLogoImg(req.getParameter("bLogoImg"));		//브랜드 로고
		inVO.setbUrl(req.getParameter("bUrl"));				//브랜드 사이트
		inVO.setbItr(req.getParameter("bItr"));				//브랜드 소개
		inVO.setbName(req.getParameter("bName"));			//브랜드 이름
		
		String regNumStr  = req.getParameter("regNum");
		inVO.setRegNum(Integer.valueOf(regNumStr));			//등록자
		            
		LOG.debug("=====================================");
		LOG.debug("=inVO="+inVO);
		LOG.debug("=====================================");
		
		int flag = this.service.doInsert(inVO);
		
		String messageStr = "";
		if(1==flag) {
			messageStr = "등록 되었습니다.";
		}else {
			messageStr = "등록 실패.";
		}
		
		messageVO.setMsgId(String.valueOf(flag));
		messageVO.setMsgContents(messageStr);
		
		Gson gson=new Gson();
		String gsonStr = gson.toJson(messageVO);
		LOG.debug("=====================================");
		LOG.debug("=gsonStr="+gsonStr);
		LOG.debug("=====================================");		
		return gsonStr;
	}
	
}


