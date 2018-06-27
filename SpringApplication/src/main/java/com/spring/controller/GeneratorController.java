package com.spring.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.core.UrbanDataset;
import com.spring.template.message.MessageBuilder;
import com.spring.template.schematron.SchematronBuilder;

@Controller
public class GeneratorController {

	public GeneratorController() {
	}

	@RequestMapping(value = { "/generator" })
	public String initGenerator() {
		return "generator";
	}

	@RequestMapping(value = { "/inputForm" }, method = { RequestMethod.POST })
	public ModelAndView setFormGenerator(@RequestParam("input") String input) {
		ModelAndView modelAndView = new ModelAndView("generator");
		modelAndView.addObject("input", input);
		return modelAndView;

	}

	@RequestMapping(value = { "/templateGeneratorU" }, method = { RequestMethod.POST })
	public ModelAndView handleTemplateU(@RequestParam("template") String[] template,
			@RequestParam("datasetName") String datasetName) {
		ModelAndView modelAndView = new ModelAndView("generator");
		UrbanDataset ud = new UrbanDataset(datasetName);
		for (String templ : template) {
			if (templ.equals("templateMessage")) {

				MessageBuilder mB = new MessageBuilder(ud);
				String content = mB.buildS();
				String fileNameOut = ud.getName() + ".xml";
				modelAndView.addObject("fileNameOutM", fileNameOut);
				modelAndView.addObject("input", "inpU");
				modelAndView.addObject("fileContentM", content);

			} else if (templ.equals("schematron")) {

				SchematronBuilder sB = new SchematronBuilder(ud);
				String content = sB.buildS();
				String fileNameOut = ud.getName() + ".sch";
				modelAndView.addObject("fileNameOutS", fileNameOut);
				modelAndView.addObject("input", "inpU");
				modelAndView.addObject("fileContentS", content);
			}
		}

		return modelAndView;
	}

	@RequestMapping(value = { "/templateGeneratorF" }, method = { RequestMethod.POST })
	@ResponseBody
	public void handleTemplateF(HttpServletResponse response, @RequestParam("template") String[] template,
			@RequestParam("fileContent") String fileUploaded) {

		String lines[] = fileUploaded.split("\\r?\\n");
		System.out.println(template.length);
		if (template.length > 1) {
			ArrayList<File> fileTemplate = new ArrayList<File>();
			for (String fileN : lines) {
				
				UrbanDataset ud = new UrbanDataset(fileN);
				MessageBuilder mB = new MessageBuilder(ud);
				SchematronBuilder sB = new SchematronBuilder(ud);
				File fM = mB.buildF();
				fileTemplate.add(fM);
				File fS = sB.buildF();
				fileTemplate.add(fS);
			}

			try {
				byte[] zipM = zipFilesfromFile(fileTemplate);
				response.setContentType("application/zip");
				response.setHeader("Content-Disposition", "attachment; filename=\"TemplateZip.ZIP\"");
				response.getOutputStream().write(zipM);
				response.getOutputStream().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String templ = template[0];

			if (templ.equals("templateMessage")) {
				ArrayList<File> fileMessage = new ArrayList<File>();
				for (String fileN : lines) {
					UrbanDataset ud = new UrbanDataset(fileN);
					MessageBuilder mB = new MessageBuilder(ud);
					File file = mB.buildF();
					fileMessage.add(file);
				}

				try {
					byte[] zip = zipFilesfromFile(fileMessage);
					response.setContentType("application/zip");
					response.setHeader("Content-Disposition", "attachment; filename=\"TemplateMessage.ZIP\"");
					response.getOutputStream().write(zip);
					response.getOutputStream().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else if (templ.equals("schematron")) {
				ArrayList<File> fileSchematron = new ArrayList<File>();

				for (String fileN : lines) {
					UrbanDataset ud = new UrbanDataset(fileN);
					SchematronBuilder sB = new SchematronBuilder(ud);
					File f = sB.buildF();
					fileSchematron.add(f);
				}

				try {

					byte[] zip = zipFilesfromFile(fileSchematron);
					response.setContentType("application/zip");
					response.setHeader("Content-Disposition", "attachment; filename=\"TemplateSchematron.ZIP\"");
					response.getOutputStream().write(zip);
					response.getOutputStream().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}


	private byte[] zipFilesfromFile(ArrayList<File> files) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos, StandardCharsets.UTF_8);

		byte bytes[] = new byte[2048];
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		for (File fileName : files) {
			fis = new FileInputStream(fileName.getName());
			bis = new BufferedInputStream(fis);
			zos.putNextEntry(new ZipEntry(fileName.getName()));

			
			int bytesRead;
			while ((bytesRead = bis.read(bytes)) != -1) {
				
				zos.write(bytes, 0, bytesRead);
			}
			zos.closeEntry();
			bis.close();
			fis.close();
		}
		zos.flush();
		baos.flush();
		zos.close();
		baos.close();

		return baos.toByteArray();
	}
}
