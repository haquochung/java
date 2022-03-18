package vn.hqhung.demo.common.router.logger;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;
import java.awt.*;

/**
 * @System: demo
 * @Title: DemoSendLogAppender
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
public class SendLogAppender extends WriterAppender {
    private static final String ENCODING = "UTF-8";
    private static final String PATTERN = "%d{dd-MM-yyyy HH:mm:ss.SSS} %-5p %C{1}:%L - %m%n";

    private final JTextArea txtLog;


    public SendLogAppender(JTextArea txtLog) {
        super();
        this.txtLog = txtLog;
        setEncoding(ENCODING);
        PatternLayout layout = new PatternLayout();
        layout.setConversionPattern(PATTERN);
        setLayout(layout);
    }

    @Override
    public void append(LoggingEvent event) {
        final String message = layout.format(event);
        txtLog.append(message);
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
}
