
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PictureView extends JPanel {
    private Image image;
    private Color bgColor;

    public PictureView() {
    }

    public void setBackgroundColor(Color color) {  //绘制背景颜色
        this.bgColor = color;
        this.repaint();
    }

    public void setImage(Image image) { //添加图片
        this.image = image;
        this.repaint();
    }

    public void setImage(File file) {  //读取图片
        try {
            this.image = ImageIO.read(file);
            this.repaint();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public void setImage(String resourcePath) {
        try {
            InputStream res = this.getClass().getResourceAsStream(resourcePath);
            this.image = ImageIO.read(res);
            this.repaint();
        } catch (Exception var3) {
            throw new RuntimeException("资源路径有误!或者不能解析图片!" + resourcePath);
        }
    }

    protected void paintComponent(Graphics g) { //添加图片
        super.paintComponent(g);
        int width = this.getWidth();
        int height = this.getHeight();
        if (this.bgColor != null) {
            g.setColor(this.bgColor);
            g.fillRect(0, 0, width, height);
        }

        g.setColor(new Color(4210752));
        g.drawRect(0, 0, width - 1, height - 1);
        if (this.image != null) {
            int imgW = this.image.getWidth((ImageObserver)null);
            int imgH = this.image.getHeight((ImageObserver)null);
            Rectangle rect = new Rectangle(0, 0, width, height);
            rect.grow(-2, -2);
            Rectangle fit = this.fitCenter(rect, imgW, imgH);
            g.drawImage(this.image, fit.x, fit.y, fit.width, fit.height, (ImageObserver)null);
        }

    }

    private Rectangle fitCenter(Rectangle rect, int imgW, int imgH) { //根据原照片长宽比计算在放置空间下 保持原长宽比所能放下的最大长宽
        int fitW = rect.width;
        int fitH = rect.width * imgH / imgW;
        if (fitH > rect.height) {
            fitH = rect.height;
            fitW = rect.height * imgW / imgH;
        }

        int fitX = rect.x + (rect.width - fitW) / 2;
        int fitY = rect.y + (rect.height - fitH) / 2;
        return new Rectangle(fitX, fitY, fitW, fitH);
    }
}
