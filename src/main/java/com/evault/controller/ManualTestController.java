package com.evault.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.evault.utils.FileUploadForm;
import com.evault.utils.TestLinkUtils;

@Controller
public class ManualTestController {
	
	@RequestMapping(value = "/manualTest", method = RequestMethod.GET)
	public ModelAndView manualTestsHome(Model model) {
		String buildNo;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String date = sdf.format(new Date());
		buildNo = date;
		model.addAttribute("buildNo", buildNo);

		return new ModelAndView("selectManualTest");
	}

	@RequestMapping(value = "/uploadManualTests", method = RequestMethod.POST)
	public ModelAndView displayManualTests(
			@ModelAttribute("uploadForm") FileUploadForm uploadForm,Model model) {
		TestLinkUtils tcUtils=new TestLinkUtils();
		InputStream inputStream = null;
		String fileName = null;
		String status = null;
		try {

			// FileUpload file = (FileUpload)command;

			List<MultipartFile> files = uploadForm.getFiles();

			if (null != files && files.size() > 0) {
				for (MultipartFile multipartFile : files) {
					inputStream = multipartFile.getInputStream();
					fileName = multipartFile.getOriginalFilename();
				}
				if ((!(fileName.isEmpty()) && (fileName
						.toLowerCase().endsWith("xlsx") || fileName
						.toLowerCase().endsWith("xls")))) {

					status = tcUtils.reportManualTests(inputStream,
							fileName);

				} else {
					status = "Incorrect File Format";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("status", status);
		return new ModelAndView("selectManualTest");
	}


}
