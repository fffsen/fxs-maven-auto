/**
 * Copyright 2005 Jasper Systems, Inc. All rights reserved.
 *
 * This software code is the confidential and proprietary information of
 * Jasper Systems, Inc. ("Confidential Information"). Any unauthorized
 * review, use, copy, disclosure or distribution of such Confidential
 * Information is strictly prohibited.
 */
package com.zycm.zybao.iot.unicom;

import javax.xml.soap.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author Sunil Sheshadri
 * @version $Id: //depot/jasper_dev/module/ProvisionApp/web/secure/apidoc/java/com/jasperwireless/ws/client/sample/EchoClient.java#4 $
 */

public class EchoClient implements ApiClientConstant {
    private SOAPConnectionFactory connectionFactory;
    private MessageFactory messageFactory;
    private URL url;
    private String licenseKey;

    public EchoClient(String url, String licenseKey) throws SOAPException, MalformedURLException {
        connectionFactory = SOAPConnectionFactory.newInstance();
        messageFactory = MessageFactory.newInstance();
        this.url = new URL(url);
        this.licenseKey = licenseKey;
    }

    /**
     * <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sch="http://api.jasperwireless.com/ws/schema">
     * <soapenv:Header/>
     * <soapenv:Body>
     * <sch:EchoRequest>
     * <sch:messageId>TCE-100-ABC-34084</sch:messageId>
     * <sch:version>0.1</sch:version>
     * <sch:licenseKey>license key</sch:licenseKey>
     * <sch:value>hello world</sch:value>
     * </sch:EchoRequest>
     * </soapenv:Body>
     * </soapenv:Envelope>
     *
     * @return
     * @throws SOAPException
     */
    private SOAPMessage createEchoRequest() throws SOAPException {
        SOAPMessage message = messageFactory.createMessage();
        message.getMimeHeaders().addHeader("SOAPAction", "http://api.jasperwireless.com/ws/service/echo/Echo");
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name echoRequestName = envelope.createName("EchoRequest", PREFIX, NAMESPACE_URI);
        SOAPBodyElement echoRequestElement = message.getSOAPBody()
                .addBodyElement(echoRequestName);
        Name msgId = envelope.createName("messageId", PREFIX, NAMESPACE_URI);
        SOAPElement msgElement = echoRequestElement.addChildElement(msgId);
        msgElement.setValue("TCE-100-ABC-34084");
        Name version = envelope.createName("version", PREFIX, NAMESPACE_URI);
        SOAPElement versionElement = echoRequestElement.addChildElement(version);
        versionElement.setValue("1.0");
        Name license = envelope.createName("licenseKey", PREFIX, NAMESPACE_URI);
        SOAPElement licenseElement = echoRequestElement.addChildElement(license);
        licenseElement.setValue(licenseKey);
        Name value = envelope.createName("value", PREFIX, NAMESPACE_URI);
        SOAPElement valueElement = echoRequestElement.addChildElement(value);
        valueElement.setValue("Testing Echo API");
        return message;
    }

    public void callWebService() throws SOAPException, IOException {
        SOAPMessage request = createEchoRequest();
        System.out.println("Request: ");
        request.writeTo(System.out);
        System.out.println("");
        String header = request.getSOAPHeader().toString();
        SOAPConnection connection = connectionFactory.createConnection();
        SOAPMessage response = connection.call(request, url);
        System.out.println("Response: ");
        response.writeTo(System.out);
        System.out.println("");
        if (!response.getSOAPBody().hasFault()) {
            writeEchoResponse(response);
        } else {
            SOAPFault fault = response.getSOAPBody().getFault();
            System.err.println("Received SOAP Fault");
            System.err.println("SOAP Fault Code :" + fault.getFaultCode());
            System.err.println("SOAP Fault String :" + fault.getFaultString());
        }
    }

    private void writeEchoResponse(SOAPMessage message) throws SOAPException {
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name echoResponseName = envelope.createName("EchoResponse", PREFIX, NAMESPACE_URI);
        SOAPBodyElement echoResponseElement = (SOAPBodyElement) message
                .getSOAPBody().getChildElements(echoResponseName).next();
        Name value = envelope.createName("value", PREFIX, NAMESPACE_URI);
        SOAPBodyElement echoElement = (SOAPBodyElement) echoResponseElement.getChildElements(value).next();
        String echoValue = echoElement.getTextContent();
        System.out.println("Echo Response [" + echoValue + "]");
    }

    public static void main(String[] args) throws Exception {
        // Apitest URL. See "Get WSDL Files" in the API documentation for Production URL.
        String url = "https://apitest.jasperwireless.com/ws/service/echo";
        if (args.length == 0) {
            System.out.println("usage: EchoClient <license-key>");
            System.exit(-1);
        }
        EchoClient echoClient = new EchoClient(url, args[0]);
        echoClient.callWebService();
    }
}

