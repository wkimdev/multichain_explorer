package kr.doublechain.basic.explorer.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * Send Transaction Controller for 지문인식과 스피딩 스트림
 * 목적 ? 
 * 
 */
//@Profile("stomp")
@Controller
@RequestMapping("/ws")
public class SendTransactionController {
	
//	@Autowired
//    public SendTransactionController() {        
//    }
//
//    @GetMapping("/fingerPrintList")
//    public String rooms(Model model) {
//        model.addAttribute("rooms", FingerPrintWS());
//        return "/chat/room-list";
//    }


}
