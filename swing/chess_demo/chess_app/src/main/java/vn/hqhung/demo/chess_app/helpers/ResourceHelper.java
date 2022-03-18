package vn.hqhung.demo.chess_app.helpers;

import lombok.extern.log4j.Log4j;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * @System: demo
 * @Title: Resource Helper
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/16
 */
@Log4j
public class ResourceHelper {
    // hide constructor
    private ResourceHelper() {
    }

    public static Image loadImage(String imgName) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        URL url = ResourceHelper.class.getResource("/img/" + imgName);
        return tk.getImage(url);
    }
}
