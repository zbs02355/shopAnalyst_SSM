package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("mainController")
public class mainController {
	@RequestMapping(value = "/main")
	public String toIndex() {
		return "index";
	}
}
