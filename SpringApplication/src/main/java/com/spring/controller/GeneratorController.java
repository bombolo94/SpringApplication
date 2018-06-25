package com.spring.controller;


import java.awt.List;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

	public GeneratorController() {}
	
	@RequestMapping(value={"/generator"})
	public String initGenerator() {
		return "generator";
	}
	
	@RequestMapping(value= {"/inputForm"}, method={RequestMethod.POST})
	public ModelAndView setFormGenerator(@RequestParam("input") String input) {
		ModelAndView modelAndView = new ModelAndView("generator");
		modelAndView.addObject("input",input);
		return modelAndView;
		
	}
	
	@RequestMapping(value= {"/templateGeneratorU"}, method={RequestMethod.POST})
	public ModelAndView handleTemplateU(@RequestParam("template") String[] template, @RequestParam("datasetName")  String datasetName) {
		ModelAndView modelAndView = new ModelAndView("generator");
		UrbanDataset ud = new UrbanDataset(datasetName);
		for(String templ : template) {
			if(templ.equals("templateMessage")) {

				MessageBuilder mB = new MessageBuilder(ud);
				String content = mB.buildS();
				String fileNameOut =ud.getName()+".xml";
				modelAndView.addObject("fileNameOutM", fileNameOut);
				modelAndView.addObject("input","inpU");
				modelAndView.addObject("fileContentM", content);
				
				
			}else if(templ.equals("schematron")) {
				
				SchematronBuilder sB = new SchematronBuilder(ud);
				String content = sB.buildS();
				String fileNameOut =ud.getName()+".sch";
				modelAndView.addObject("fileNameOutS", fileNameOut);
				modelAndView.addObject("input","inpU");
				modelAndView.addObject("fileContentS", content);
			}
		}
		
		
		return modelAndView;
	}
	
	@RequestMapping(value= {"/templateGeneratorF"}, method={RequestMethod.POST})
	@ResponseBody
	public void handleTemplateF(HttpServletResponse response,@RequestParam("template") String[] template, @RequestParam("fileContent") String fileUploaded) {
		
		
		/*TODO
		 * MessageBuilder
		 * */
		
		String lines[] = fileUploaded.split("\\r?\\n");
		
		
		for(String fileN : lines) {
			UrbanDataset ud = new UrbanDataset(fileN);
			SchematronBuilder sB = new SchematronBuilder(ud);
			sB.build();
		}
		
		try {
			byte[] zip = zipFiles(lines);
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=\"DATA.ZIP\"");
			response.getOutputStream().write(zip);
			response.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	
	private byte[] zipFiles(String [] files) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        byte bytes[] = new byte[2048];
        
        String dir = ".\\output\\";

        for (String fileName : files) {
        	UrbanDataset ud = new UrbanDataset(fileName);
            FileInputStream fis = new FileInputStream(dir + ud.getName()+".sch");
            BufferedInputStream bis = new BufferedInputStream(fis);

            zos.putNextEntry(new ZipEntry(fileName));

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
