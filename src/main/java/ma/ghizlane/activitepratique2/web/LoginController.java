package ma.ghizlane.activitepratique2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author pc
 **/
@Controller
public class LoginController {
   @GetMapping("/login")
   public String login(){
       return "login";
   }

}
