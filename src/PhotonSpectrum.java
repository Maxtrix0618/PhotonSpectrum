import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Photon Spectrum <br/>
 * by_Maxtrix, 2023.11
 */
public class PhotonSpectrum extends JFrame {
    private final int width;
    private JLabel imageLabel;
    private JLabel ColorBox;
    private JLabel ARGBText;
    private JLabel LambdaText;
    private JLabel AppendText;

    private BufferedImage SpectrumIMG;
    private BufferedImage ExhibitIMG;
    private BufferedImage ColorBoxIMG;

    private int[] tagARGB = new int[4];

    public PhotonSpectrum(String FrameTitle) {
        this.width = 1020;
        int height = 340;
        setTitle(FrameTitle);
        setSize(width, height);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        addSpectrum();
        addColorBlock();
        addARGBText();
        addLambdaText();
        addTimer();
        setVisible(true);
    }

    private void addSpectrum() {
        SpectrumIMG = new BufferedImage(801, 201, 6);

        for (int x = 0; x < 801; x++) {
            double w = ((double) x) / 2 + 380.0;
            int[] argb = ARGBs(w);
            for (int y = 0; y < SpectrumIMG.getHeight(); y++)
                IMGKit.paint(SpectrumIMG, argb, x, y);
        }
        ExhibitIMG = IMGKit.CloneNewIMG(SpectrumIMG);

        imageLabel = new JLabel();
        imageLabel.setLocation(20, 15);
        reloadImage();
        add(imageLabel);
    }

    /**
     * Calculates and returns the computer ARGB color value
     * which is closest to the color of visible light at the entered wavelength
     */
    private static int[] ARGBs(double wavelength) {
        double gamma = 0.8;
        double IMax = 255.0;
        double r, g, b, alpha;

        if (wavelength < 380.0) {
            r = 0.0;
            g = 0.0;
            b = 0.0;
        } else if (wavelength < 440.0) {
            r = -1.0 * (wavelength - 440.0) / (440.0 - 380.0);
            g = 0.0;
            b = 1.0;
        } else if (wavelength < 490.0) {
            r = 0.0;
            g = (wavelength - 440.0) / (490.0 - 440.0);
            b = 1.0;
        } else if (wavelength < 510.0) {
            r = 0.0;
            g = 1.0;
            b = -1.0 * (wavelength - 510.0) / (510.0 - 490.0);
        } else if (wavelength < 580.0) {
            r = (wavelength - 510.0) / (580.0 - 510.0);
            g = 1.0;
            b = 0.0;
        } else if (wavelength < 645.0) {
            r = 1.0;
            g = -1.0 * (wavelength - 645.0) / (645.0 - 580.0);
            b = 0.0;
        } else if (wavelength <= 780.0) {
            r = 1.0;
            g = 0.0;
            b = 0.0;
        } else {
            r = 0.0;
            g = 0.0;
            b = 0.0;
        }

        // The intensity is lower at the edges of the visible spectrum
        if (wavelength < 380.0) {alpha = 0.0;}
        else if (wavelength < 420.0) {alpha = 0.30 + 0.70 * (wavelength - 380.0) / (420.0 - 380.0);}
        else if (wavelength < 701.0) {alpha = 1.0;}
        else if (wavelength <= 780.0) {alpha = 0.30 + 0.70 * (780.0 - wavelength) / (780.0 - 700.0);}
        else {alpha = 0.0;}

        int R = r == 0.0 ? 0 : (int) (IMax * Math.pow(r * alpha, gamma));
        int G = g == 0.0 ? 0 : (int) (IMax * Math.pow(g * alpha, gamma));
        int B = b == 0.0 ? 0 : (int) (IMax * Math.pow(b * alpha, gamma));
        int A = (int) (alpha * 255);

        return new int[]{A, R, G, B};
    }


    private void addColorBlock() {
        ColorBox = new JLabel();
        ColorBox.setLocation(width -170, 30);
        ColorBoxIMG = new BufferedImage(128, 128, 6);
        reloadColorBox();
        add(ColorBox);
    }

    private void addARGBText() {
        JLabel TARGBText = new JLabel(" A | R | G | B ");
        TARGBText.setLocation(width -160, 150);
        TARGBText.setSize(128, 64);
        TARGBText.setFont(new Font("", Font.PLAIN, 20));
        TARGBText.setForeground(Color.LIGHT_GRAY);
        add(TARGBText);

        ARGBText = new JLabel();
        ARGBText.setLocation(width -175, 180);
        ARGBText.setSize(256, 64);
        ARGBText.setForeground(Color.LIGHT_GRAY);
        ARGBText.setFont(new Font("", Font.PLAIN, 18));
        add(ARGBText);
    }
    /**
     * Wavelength, frequency, energy information text
     */
    private void addLambdaText() {
        LambdaText = new JLabel("");
        LambdaText.setLocation(10, 210);
        LambdaText.setSize(128, 64);
        LambdaText.setForeground(Color.WHITE);
        LambdaText.setFont(new Font("", Font.PLAIN, 18));
        add(LambdaText);

        AppendText = new JLabel("");
        AppendText.setLocation(10, 205);
        AppendText.setSize(256, 128);
        AppendText.setForeground(Color.LIGHT_GRAY);
        AppendText.setFont(new Font("", Font.PLAIN, 13));
        add(AppendText);

    }

    private void addTimer() {
        java.util.Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainTimerRun();
            }
        }, 0, 10);
    }

    private final Point PrPoint = new Point(0, 0);
    private void mainTimerRun() {
        Point absPoint = MouseInfo.getPointerInfo().getLocation();
        Point relPoint = new Point(absPoint.x-this.getLocation().x-30, absPoint.y-this.getLocation().y-50);
        if (!relPoint.equals(PrPoint))
            tryShowPixelMessage(relPoint.x, relPoint.y);
        PrPoint.setLocation(relPoint);
    }

    private void tryShowPixelMessage(int x, int y) {
        extractIMG();
        if (x < 0 || y < 0 || x >= ExhibitIMG.getWidth() || y >= ExhibitIMG.getHeight()) return;
        int bgC = ExhibitIMG.getRGB(x, y);
        tagARGB = IMGKit.getPixel(bgC);
        ColorBox.setBackground(new Color(tagARGB[1], tagARGB[2], tagARGB[3], tagARGB[0]));
        ARGBText.setText(tagARGB[0] + " | " + tagARGB[1] + " | " + tagARGB[2] + " | " + tagARGB[3]);
        TextUpdate(x);
        paintPixelBox(x, y);
        reloadImage();
        reloadColorBox();
    }

    private static final DecimalFormat fdf = new DecimalFormat("##.################");

    static int Lightspeed = 299792458;
    static double PlanckC = 6.62606896E-34;
    static double JeV = 1.6021766208E-19;
    private void TextUpdate(int x) {
        double wavelength = ((double) x)/2 + 380.0;
        double frequency = Lightspeed / wavelength * 1E9;
        double energy_I = PlanckC * frequency / JeV;
        LambdaText.setText(wavelength + " nm");
        LambdaText.setLocation(x+10,210);
        AppendText.setText("<html>" + fdf.format(frequency * 1E-14) + "×10^14 s⁻¹<br/>" + energy_I + " eV");
        AppendText.setLocation(x+10,215);
    }

    private void paintPixelBox(int x, int y) {
        for (int i = 0; i < 8; i++)
            IMGKit.paintRev(ExhibitIMG, x + IMGKit.dxs_8[i], y + IMGKit.dys_8[i]);
        for (int cy = 0; cy < ExhibitIMG.getHeight(); cy++)
            IMGKit.paintRev(ExhibitIMG, x, cy);
        IMGKit.paintRev(ExhibitIMG, x, y);
    }

    private void extractIMG() {IMGKit.IMG_clone(SpectrumIMG, ExhibitIMG);}

    private void reloadImage() {
        imageLabel.setIcon(new ImageIcon(ExhibitIMG));
        imageLabel.setSize(ExhibitIMG.getWidth(), ExhibitIMG.getHeight());
    }
    private void reloadColorBox() {
        for (int x = 0; x < ColorBoxIMG.getWidth(); x++)
            for (int y = 0; y < ColorBoxIMG.getHeight(); y++)
                IMGKit.paint(ColorBoxIMG, tagARGB, x, y);
        ColorBox.setIcon(new ImageIcon(ColorBoxIMG));
        ColorBox.setSize(ColorBoxIMG.getWidth(), ColorBoxIMG.getHeight());
    }

    public static void main(String[] args) {
        new PhotonSpectrum("Photon Spectrum v1.0");
    }

}
