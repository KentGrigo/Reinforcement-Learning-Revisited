package neuralnetworks;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectLoader {

    public static double[][] loadIDXImages(String imagesFilePath) {
        FileInputStream inImage = null;
        double[][] images = null;
        try {
            inImage = new FileInputStream(imagesFilePath);

            int magicNumberImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfImages = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfRows = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());
            int numberOfColumns = (inImage.read() << 24) | (inImage.read() << 16) | (inImage.read() << 8) | (inImage.read());

            int numberOfPixels = numberOfRows * numberOfColumns;
            images = new double[numberOfImages][numberOfPixels];

            for (int i = 0; i < numberOfImages; i++) {

                if (i % 1000 == 0) {
                    System.out.println("Number of images extracted: " + i);
                }

                for (int p = 0; p < numberOfPixels; p++) {
                    double grayScale = inImage.read() / 255.0;
                    images[i][p] = grayScale;
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inImage != null) {
                try {
                    inImage.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return images;
    }

    public static double[][] loadIDXLabels(String labelFilePath, int outputs) {
        FileInputStream inLabel = null;
        double[][] labels = null;

        try {
            inLabel = new FileInputStream(labelFilePath);

            int magicNumberLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());
            int numberOfLabels = (inLabel.read() << 24) | (inLabel.read() << 16) | (inLabel.read() << 8) | (inLabel.read());

            labels = new double[numberOfLabels][outputs];

            for (int i = 0; i < numberOfLabels; i++) {

                if (i % 1000 == 0) {
                    System.out.println("Number of labels extracted: " + i);
                }

                int label = inLabel.read();
                labels[i][label] = 1;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inLabel != null) {
                try {
                    inLabel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return labels;
    }

    public static void saveObject(Object obj, String fileName) {
        ObjectOutputStream oout = null;
        try {
            oout = new ObjectOutputStream(new FileOutputStream(fileName));
            oout.writeObject(obj);
        } catch (IOException ex) {
            Logger.getLogger(ObjectLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oout.close();
            } catch (IOException ex) {
                Logger.getLogger(ObjectLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Object loadObject(String fileName) {
        ObjectInputStream oin = null;
        Object obj = null;
        try {
            oin = new ObjectInputStream(new FileInputStream(fileName));
            return oin.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ObjectLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oin.close();
            } catch (IOException ex) {
                Logger.getLogger(ObjectLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obj;
    }

}
