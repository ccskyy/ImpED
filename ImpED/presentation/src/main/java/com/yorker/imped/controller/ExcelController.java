package com.yorker.imped.controller;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yorker.imped.exceptions.XINEServiceException;
import com.yorker.imped.util.ExcelReaderV4;
import com.yorker.imped.util.XINEUtil;
import com.yorker.imped.vo.ExcelMetadataVO;
import com.yorker.imped.vo.UploadExcelVO;

/**
 * The Class ExcelController.
 */
@Controller
@RequestMapping(value = "/services/excel")
public class ExcelController {

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(ExcelController.class);
	
	/**
	 * Upload excel file.
	 *
	 * @param request the request
	 * @return the upload excel VO
	 */
	@RequestMapping(value = "/describeData", method = RequestMethod.POST)
	public @ResponseBody UploadExcelVO uploadExcelFile(MultipartHttpServletRequest request) {
		UploadExcelVO vo = new UploadExcelVO();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = request.getFile(itr.next());
		/*if(mpf.getSize() > 40000000) {
			vo.setResponseCode("fail");
			vo.setResponseDesc("File size should not exceed more than 40 MB.");
			return vo;
		}*/
		System.out.println(mpf.getSize());
		List<ExcelMetadataVO> list = null;
		try {
			vo.setFileName(mpf.getOriginalFilename().substring(0, mpf.getOriginalFilename().indexOf(".")));
			list = ExcelReaderV4.loadExcelMetadata(mpf.getInputStream());
			if(list != null){
				vo.setMetadata(list);
			}
			vo.setResponseCode("success");
		} catch (XINEServiceException e) {
			logger.error("Could not able to describeData. Error : " + e.getErrorMessage());
			vo.setResponseCode("fail");
			vo.setResponseDesc("ERROR ::" +e.getErrorCode()+" :: " +e.getErrorMessage());
		} catch (Exception e) {
			logger.error("ExcelController.describeData failed. Error details :: "+ XINEUtil.getErrorStackTrace(e));
		}
		return vo;
	}
	
}