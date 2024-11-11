import java.awt.image.BufferedImage;

/**
 * IMGKit / 2023.9.7 by_Maxtrix
 */
public class IMGKit {
    private IMGKit() {}

    public static int[] dxs_8 = {-1, 0, 1, 1, 1, 0, -1, -1};
    public static int[] dys_8 = {-1, -1, -1, 0, 1, 1, 1, 0};

    public static boolean outsideArea(BufferedImage BI, int x, int y) {
        return (x < 0 || y < 0 || x >= BI.getWidth() || y >= BI.getHeight());
    }

    public static void paint(BufferedImage BI, int a, int r, int g, int b, int x, int y) {
        if (outsideArea(BI, x, y)) return;
        int p = (a << 24) | (r << 16) | (g << 8) | b;
        BI.setRGB(x, y, p);
    }
    public static void paint(BufferedImage BI, int[] ps, int x, int y) {
        paint(BI, ps[0], ps[1], ps[2], ps[3], x, y);
    }

    public static void paintRev(BufferedImage BI, int x, int y) {
        int[] ARGB = getPixel(BI, x, y);
        paint(BI, ARGB[0], 255-ARGB[1], 255-ARGB[2], 255-ARGB[3], x, y);
    }

    /**
     * Gives the image color value of IMG_DO at (x,y) and returns it with int[4] <br/>
     * (the 4 int numbers are the values of 'a, r, g, b').
     */
    public static int[] getPixel(BufferedImage BI, int x, int y) {
        if (IMGKit.outsideArea(BI, x, y)) return new int[4];
        return getPixel(BI.getRGB(x, y));
    }
    public static int[] getPixel(int p) {
        int[] ARGB = new int[4];
        ARGB[0] = (p >> 24) & 0xff;
        ARGB[1] = (p >> 16) & 0xff;
        ARGB[2] = (p >> 8) & 0xff;
        ARGB[3] = p & 0xff;
        return ARGB;
    }

    public static BufferedImage CloneNewIMG(BufferedImage OriBI) {
        BufferedImage NewBI = new BufferedImage(OriBI.getWidth(), OriBI.getHeight(), OriBI.getType());
        IMG_clone(OriBI, NewBI);
        return NewBI;
    }

    public static void IMG_clone(BufferedImage OriBI, BufferedImage NewBI) {
        NewBI.setData(OriBI.getData());
    }

}
