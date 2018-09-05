/*
 * OpenSimplex Noise sample class.
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OpenSimplexNoiseTest
{
    private static final int WIDTH = 2048;
    private static final int HEIGHT = 2048;
    private static final double[] SPICYNESS = {128, 64, 32, 16, 4, 2, 1};

    private static final int imgNum = 15;

    static double heightMod = 1.65;

    public static void main(String[] args)
            throws Exception {

        /*
        for (int i = 1; i < imgNum; i++) {
            SPICYNESS = new double[i];
            for (int j = i - 1, k = 0; j >= 0; j--, k++) {
                SPICYNESS[k] = Math.pow(2.0, j);
            }

            System.out.println(Arrays.toString(SPICYNESS));
        }
        */

        final SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");

        final OpenSimplexNoise noise = new OpenSimplexNoise(rand.nextLong());
        final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        final int threadCount = WIDTH / 256;

        long start = System.currentTimeMillis();

        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int z = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    for (int y = 0; y < HEIGHT; y++)
                    {
                        for (int x = (WIDTH / threadCount) * z; x < (WIDTH / threadCount) * (z + 1); x++)
                        {
                            double value = 0;

                            for (int i = 0; i < SPICYNESS.length; i++) {
                                value += (noise.eval(x / SPICYNESS[i], y / SPICYNESS[i], 0.0))/ (Math.pow(2, i + 1));
                            }

                            value += (1 / (Math.pow(2, SPICYNESS.length)));

                            value = (value + 1.0) / 2.0;

                            int valMoved = (int)((Math.pow(value, heightMod)) * 127.5);

                            int colo = 255 - valMoved;
                            Color color = new Color(colo, colo, colo);
                            image.setRGB(x, y, color.getRGB());

                            //int rgb = 0x010101 * (int)((value + 1) * 127.5);


                            /*

                                value += (noise.eval(x / SPICYNESS[i], y / SPICYNESS[i], 0.0) * 3 )/ (Math.pow(4, i + 1));
                            }

                            value += (3 / (Math.pow(4, SPICYNESS.length)));
                             */
                        }
                    }
                }
            });
        }

        threadPool.shutdown();
        threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        /*
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                double value = 0;

                for (int i = 0; i < SPICYNESS.length; i++) {
                    value -= value / (i + 1);
                    value += noise.eval(x / SPICYNESS[i], y / SPICYNESS[i], 0.0) / (i + 1);
                }

                int rgb = 0x010101 * (int)((value + 1) * 127.5);
                image.setRGB(x, y, rgb);
            }
        }
        */



        System.out.println("Generation took " + (System.currentTimeMillis() - start) + " ms");
        ImageIO.write(image, "png", new File("iwannadie.png"));
    }
}