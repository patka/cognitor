package org.cognitor.consumer.web;

import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.Parameter;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * User: patrick
 * Date: 18.12.12
 */
@Controller
public class ConsumerController {

    private ConsumerManager manager;

    @Autowired
    public ConsumerController(ConsumerManager manager) {
        this.manager = manager;
    }

    @RequestMapping(value = "/", method = GET)
    public String showIndex() {
        return "index";
    }

    @RequestMapping(value = "/", method = POST)
    public void processOpenIdAnswer(HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        String handle = request.getParameter("handle");
        boolean isImmediateRequest = Boolean.parseBoolean(request.getParameter("immediate"));

        String returnUrl = request.getRequestURL().toString() + "openidAnswer";

        // perform discovery on the user-supplied identifier
        List discoveries = manager.discover(handle);

        // attempt to associate with the OpenID provider
        // and retrieve one service endpoint for authentication
        DiscoveryInformation discovered = manager.associate(discoveries);

        // store the discovery information in the user's session
        request.getSession().setAttribute("openid-disc", discovered);

        // obtain a AuthRequest message to be sent to the OpenID provider
        AuthRequest authReq = manager.authenticate(discovered, returnUrl);
        authReq.setImmediate(isImmediateRequest);

        // since our service uses OpenId Version two this should to it
        response.sendRedirect(authReq.getDestinationUrl(true));
    }

    @RequestMapping(value = "/openidAnswer", method = GET)
    public ModelAndView showAnswer(HttpServletRequest request) throws Exception {
        ParameterList response = new ParameterList(request.getParameterMap());

        // retrieve the previously stored discovery information
        DiscoveryInformation discovered =
                (DiscoveryInformation) request.getSession().getAttribute("openid-disc");

        // extract the receiving URL from the HTTP request
        StringBuffer receivingURL = request.getRequestURL();
        String queryString = request.getQueryString();
        if (queryString != null && queryString.length() > 0) {
            receivingURL.append("?").append(request.getQueryString());
        }

        // verify the response
        VerificationResult verification = manager.verify(
                receivingURL.toString(),
                response, discovered);

        // examine the verification result and extract the verified identifier
        Identifier identifier = verification.getVerifiedId();
        ModelAndView modelAndView = new ModelAndView("answer");
        modelAndView.addObject("identifier", identifier);
        return modelAndView;
    }
}