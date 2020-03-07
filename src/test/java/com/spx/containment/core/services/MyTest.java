package com.spx.containment.core.services;

import com.spx.containment.core.model.Container;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class MyTest {


    private final String test = "";

    @Mock
    private Container c;

    @Test
    public void run() {
        c.getName();
        log.info("hi");

    }

    public void t() {
        try {
            // create a JAXBContext capable of handling classes generated into package
            javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance("test.spx");
            // create an Unmarshaller
            javax.xml.bind.Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // unmarshal an instance document into a tree of Java content
            // objects composed of classes from the package.
            MyTest unmarshalledObject = (MyTest) unmarshaller.unmarshal(new java.io.FileInputStream("filename.xml"));
        } catch (javax.xml.bind.JAXBException je) {
            je.printStackTrace();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            ioe.printStackTrace(pw);
            log.error("Error occurred causing exception {}", sw.toString());

        }
    }

}





