package teamverpic.verpicbackend.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.ui.Model;
import teamverpic.verpicbackend.repository.UserRepository;
import teamverpic.verpicbackend.service.AsyncPaymentService;
import teamverpic.verpicbackend.service.UserService;

import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Locale;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Controller
public class PaymentController {

    private final AsyncPaymentService service;
    private final IamportClient client;

    public PaymentController() {
        // REST API 키와 REST API secret 를 아래처럼 순서대로 입력한다.
        this.client = new IamportClient("8815314335769575", "d91cMNdEAobXvBlT86A3EnEwFKpjvXTJ7UVTH9yixJg2kBVsHwwmUbTzViTrfNamH6gpgPINyYQSAMlh");
        this.service = new AsyncPaymentService();
    }

    @GetMapping("/payment/kakao")
    public ModelAndView kakaopayment() {
        return new ModelAndView("/payment.html");
    }

    @RequestMapping(value = "/payment/kakao/verifyIamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(
            Model model, Locale locale, HttpSession session,
            @PathVariable(value = "imp_uid") String imp_uid) throws IamportResponseException, IOException {

        return client.paymentByImpUid(imp_uid);
    }

    @RequestMapping(value = "/orderCompleteMobile", method = RequestMethod.GET)
    public ModelAndView orderCompleteMobile(
            @RequestParam(required = false) String imp_uid
            , @RequestParam(required = false) String merchant_uid
            , Model model
            , Locale locale
            , HttpSession session) throws IamportResponseException, IOException {

        IamportResponse<Payment> result = client.paymentByImpUid(imp_uid);

        // 결제 가격과 검증결과를 비교한다.
        if (result.getResponse().getAmount().compareTo(BigDecimal.valueOf(10)) == 0) {
            System.out.println("검증통과");
        }
        ModelAndView mv = new ModelAndView("/index.html");

        return new ModelAndView("/index.html");
    }

    @RequestMapping(value="/billings", method = RequestMethod.GET)
    public String payRequest(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable(value = "imp_uid") String imp_uid) throws IOException, IamportResponseException {
        String customerUid = client.paymentByImpUid(imp_uid).getResponse().getCustomerUid();
        IamportResponse<AccessToken> accessToken = client.getAuth();

        AccessToken accessTokenResponse = accessToken.getResponse();

//        IamportREsponse<Acce>

        return service.paymentService(request, response).toString();
    }
}
