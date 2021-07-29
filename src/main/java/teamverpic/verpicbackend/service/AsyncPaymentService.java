package teamverpic.verpicbackend.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AsyncPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncPaymentService.class);

    @Async
    public ServletInputStream paymentService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return request.getInputStream();
    }
}
