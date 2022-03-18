package vn.hqhung.demo.chess_server.helpers;

import java.awt.*;
import java.net.URL;

/**
 * @System: demo
 * @Title: Resource Helper
 * @Version: 1.0.0
 * @Author: Hung-HQ
 * @CreateOn: 2022/03/15
 */
public class ResourceHelper {
    // hide constructor
    private ResourceHelper() {
    }

    /**
     * load image from resource
     *
     * @param imgLink img
     * @return Image
     */
    public static Image loadImage(String imgLink) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        URL url = ResourceHelper.class.getResource("/img/" + imgLink);
        return tk.getImage(url);
    }
}
