import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Static utility class that is responsible for transforming the images.
 * Each function (or at least most functions) take in an Image and return
 * a transformed image.
 */
public class ImageManipulator {
    /**
     * Loads the image at the given path
     *
     * @param path path to image to load
     * @return an Img object that has the given image loaded
     * @throws IOException
     */
    public static Img LoadImage(String path) throws IOException {
        return new Img(path);
    }

    /**
     * Saves the image to the given file location
     *
     * @param image image to save
     * @param path  location in file system to save the image
     * @throws IOException
     */
    public static void SaveImage(Img image, String path) throws IOException {
        image.Save(path.substring(path.length() - 3), path);
    }

    /**
     * Converts the given image to grayscale (black, white, and gray). This is done
     * by finding the average of the RGB channel values of each pixel and setting
     * each channel to the average value.
     *
     * @param image image to transform
     * @return the image transformed to grayscale
     */
        public static Img ConvertToGrayScale(Img image) {
            for(int l = 0; l < image.GetHeight(); l++){
                for(int c = 0; c < image.GetWidth(); c++){
                    int r = image.GetRGB(c, l).GetRed();
                    int b = image.GetRGB(c, l).GetBlue();
                    int g = image.GetRGB(c, l).GetGreen();
                    int avg = (r+g+b)/3;
                    image.SetRGB(c, l, new RGB(avg, avg, avg));

                }
            }
            // Implement this method and remove the line below
            return image;
        }

    /**
     * Inverts the image. To invert the image, for each channel of each pixel, we get
     * its new value by subtracting its current value from 255. (r = 255 - r)
     *
     * @param image image to transform
     * @return image transformed to inverted image
     */
    public static Img InvertImage(Img image) {
        for (int i = 0; i < image.GetWidth(); i++) {
            for (int j = 0; j < image.GetHeight(); j++) {
                RGB temp = image.GetRGB(i, j);
                temp.SetRed(255 - temp.GetRed());
                temp.SetGreen(255 - temp.GetGreen());
                temp.SetBlue(255 - temp.GetBlue());
                image.SetRGB(i,j,temp);
            }
        }
        return image;
    }

    /**
     * Converts the image to sepia. To do so, for each pixel, we use the following equations
     * to get the new channel values:
     * r = .393r + .769g + .189b
     * g = .349r + .686g + .168b
     * b = 272r + .534g + .131b
     *
     * @param image image to transform
     * @return image transformed to sepia
     */
    public static Img ConvertToSepia(Img image) {
        for(int l = 0; l < image.GetHeight(); l++) {
            for (int c = 0; c < image.GetWidth(); c++) {
                int r = image.GetRGB(c, l).GetRed();
                int b = image.GetRGB(c, l).GetBlue();
                int g = image.GetRGB(c, l).GetGreen();
                int nr = (int)(.393*r + .769*g + .189*b);
                int ng = (int)(.349*r + .686*g + .168*b);
                int nb = (int)(.272*r + .534*g + .131*b);
                image.SetRGB(c, l, new RGB(nr, ng, nb));
            }
        }
        // Implement this method and remove the line below
        return image;
    }

    /**
     * Creates a stylized Black/White image (no gray) from the given image. To do so:
     * 1) calculate the luminance for each pixel. Luminance = (.299 r^2 + .587 g^2 + .114 b^2)^(1/2)
     * 2) find the median luminance
     * 3) each pixel that has luminance >= median_luminance will be white changed to white and each pixel
     * that has luminance < median_luminance will be changed to black
     *
     * @param image image to transform
     * @return black/white stylized form of image
     */
    public static Img ConvertToBW(Img image) {
        ArrayList<Double> lums = new ArrayList<Double>();
        int x = image.GetWidth();
        int y = image.GetHeight();
        for (int r = 0; r < x; r++) {
            for (int c = 0; c < y; c++) {
                RGB rgb1 = image.GetRGB(r, c);
                int red = rgb1.GetRed();
                int green = rgb1.GetGreen();
                int blue = rgb1.GetBlue();
                double luminance = Math.sqrt((rgb1.GetRed() * rgb1.GetRed() * 0.299) + (rgb1.GetBlue() * rgb1.GetBlue() * 0.114) + (rgb1.GetGreen() * rgb1.GetGreen() * 0.587));
                lums.add(luminance);
            }
        }
        Collections.sort(lums);
        double median = 0;
        int index1 = lums.size()/2;
        int index2 = (lums.size()/2)+1;
        if (lums.size() % 2 == 1)
            median = lums.get((lums.size() / 2));
        else
            median = ((lums.get(index1) + (lums.get(index2))) / 2);
        for (int i = 0; i < image.GetWidth(); i++) {
            for (int j = 0; j < image.GetHeight(); j++) {
                RGB rgb = image.GetRGB(i, j);
                double luminance = Math.sqrt((rgb.GetRed() * rgb.GetRed() * 0.299) + (rgb.GetBlue() * rgb.GetBlue() * 0.114) + (rgb.GetGreen() * rgb.GetGreen() * 0.587));
                if (luminance >= median) {
                    rgb.SetBlue(255);
                    rgb.SetGreen(255);
                    rgb.SetRed(255);
                } else if (luminance < median){
                    rgb.SetBlue(0);
                    rgb.SetRed(0);
                    rgb.SetGreen(0);
                }
                image.SetRGB(i,j,rgb);
            }

        }
        return image;
    }

    /**
     * Rotates the image 90 degrees clockwise.
     *
     * @param image image to transform
     * @return image rotated 90 degrees clockwise
     */
    public static Img RotateImage(Img image) {
        Img newImg = new Img(image.GetHeight(), image.GetWidth());
        for (int i = 0; i < image.GetWidth(); i++) {
            for (int j = 0; j < image.GetHeight(); j++) {
                RGB temp = image.GetRGB(i, j);
                newImg.SetRGB(image.GetHeight() - 1 - j, i, temp);
            }
        }
        return newImg;
    }

    /**
     * Applies an Instagram-like filter to the image. To do so, we apply the following transformations:
     * 1) We apply a "warm" filter. We can produce warm colors by reducing the amount of blue in the image
     * and increasing the amount of red. For each pixel, apply the following transformation:
     * r = r * 1.2
     * g = g
     * b = b / 1.5
     * 2) We add a vignette (a black gradient around the border) by combining our image with an
     * an image of a halo (you can see the image at resources/halo.png). We take 65% of our
     * image and 35% of the halo image. For example:
     * r = .65 * r_image + .35 * r_halo
     * 3) We add decorative grain by combining our image with a decorative grain image
     * (resources/decorative_grain.png). We will do this at a .95 / .5 ratio.
     *
     * @param image image to transform
     * @return image with a filter
     * @throws IOException
     */
    public static Img InstagramFilter(Img image) throws IOException {
        Img haloImage = new Img(("resources/halo.png"));
        Img grainImage = new Img(("resources/decorative_grain.png"));
        //Get Ratios
        double ratioWidthHalo = (haloImage.GetWidth() / (double) image.GetWidth());
        double ratioHeightHalo = (haloImage.GetHeight() / (double) image.GetHeight());
        double ratioWidthGrain = (grainImage.GetWidth() / (double) image.GetWidth());
        double ratioHeightGrain = (grainImage.GetHeight() / (double) image.GetHeight());
        for (int i = 0; i < image.GetWidth(); i++) {
            for (int j = 0; j < image.GetHeight(); j++) {
                //Warm Filter:
                RGB rgb = image.GetRGB(i, j);
                int rVal = (int) (rgb.GetRed() * 1.2);
                int gVal = rgb.GetGreen();
                int bVal = (int) (rgb.GetBlue() / 1.5);
                image.SetRGB(i, j, new RGB(rVal, gVal, bVal));
            }
        }
        for (int i = 0; i < image.GetWidth(); i++) {
            for (int j = 0; j < image.GetHeight(); j++) {
                //Halo Vignette
                RGB rgb2 = image.GetRGB(i, j);
                RGB rgbHalo = haloImage.GetRGB(((int) (i * ratioWidthHalo)), ((int) (j * ratioHeightHalo)));
                int rVal = (int) (0.65 * rgb2.GetRed() + 0.35 * rgbHalo.GetRed());
                int gVal = (int) (0.65 * rgb2.GetGreen() + 0.35 * rgbHalo.GetGreen());
                int bVal = (int) (0.65 * rgb2.GetBlue() + 0.35 * rgbHalo.GetBlue());
                image.SetRGB(i, j, new RGB(rVal, gVal, bVal));
            }
        }
        for (int i = 0; i < image.GetWidth(); i++) {

            for (int j = 0; j < image.GetHeight(); j++) {
                //Grain
                RGB rgb3 = image.GetRGB(i, j);
                RGB rgbGrain = grainImage.GetRGB(((int) (i * ratioWidthGrain)), ((int) (j * ratioHeightGrain)));
                int rVal = (int) (0.95 * rgb3.GetRed() + 0.05 * rgbGrain.GetRed());
                int gVal = (int) (0.95 * rgb3.GetGreen() + 0.05 * rgbGrain.GetGreen());
                int bVal = (int) (0.95 * rgb3.GetBlue() + 0.05 * rgbGrain.GetBlue());
                image.SetRGB(i, j, new RGB(rVal, gVal, bVal));
            }
        }
        return image;

    }

    /**
     * Sets the given hue to each pixel image. Hue can range from 0 to 360. We do this
     * by converting each RGB pixel to an HSL pixel, Setting the new hue, and then
     * converting each pixel back to an RGB pixel.
     *
     * @param image image to transform
     * @param hue   amount of hue to add
     * @return image with added hue
     */
    public static Img SetHue(Img image, int hue) {
            int x = image.GetWidth();
            int y = image.GetHeight();
            for (int r = 0; r < x; r++) {
                for (int c = 0; c < y; c++) {
                    RGB pixel = image.GetRGB(r, c);
                    HSL pixel2 = pixel.ConvertToHSL();
                    pixel2.SetHue(hue);
                    pixel = pixel2.GetRGB();
                    image.SetRGB(r, c, pixel);
                }
            }
            return image;
        }


    /**
     * Sets the given saturation to the image. Saturation can range from 0 to 1. We do this
     * by converting each RGB pixel to an HSL pixel, setting the new saturation, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param saturation amount of saturation to add
     * @return image with added hue
     */
    public static Img SetSaturation(Img image, double saturation) {
            int x = image.GetWidth();
            int y = image.GetHeight();
            for(int r = 0; r < x; r++)
            {
                for(int c = 0; c < y; c++)
                {
                    RGB pixel = image.GetRGB(r,c);
                    HSL pixel2 = pixel.ConvertToHSL();
                    pixel2.SetSaturation(saturation);
                    pixel = pixel2.GetRGB();
                    image.SetRGB(r, c, pixel);
                }
            }
            return image;
        }

    /**
     * Sets the lightness to the image. Lightness can range from 0 to 1. We do this
     * by converting each RGB pixel to an HSL pixel, setting the new lightness, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param lightness amount of hue to add
     * @return image with added hue
     */
    public static Img SetLightness(Img image, double lightness) {
            int x = image.GetWidth();
            int y = image.GetHeight();
            for(int r = 0; r < x; r++)
            {
                for(int c = 0; c < y; c++)
                {
                    RGB pixel = image.GetRGB(r,c);
                    HSL pixel2 = pixel.ConvertToHSL();
                    pixel2.SetLightness(lightness);
                    pixel = pixel2.GetRGB();
                    image.SetRGB(r, c, pixel);
                }
            }
            return image;
        }
    }
