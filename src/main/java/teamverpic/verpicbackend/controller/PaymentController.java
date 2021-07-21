package teamverpic.verpicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RestController
@Controller
public class PaymentController {

    @GetMapping("/payment/kakao")
    public ModelAndView kakaopayment(){
        return new ModelAndView("/payment.html");
    }
}
