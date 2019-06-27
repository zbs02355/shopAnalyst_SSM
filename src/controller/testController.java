package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("testController")
public class testController {
	@RequestMapping(value = "test")
	public String toTest(HttpServletRequest request) {
		request.setAttribute("testChinese", "³É¶¼");
		return "test2";
	}
}
