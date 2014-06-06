package de.hhu.propra14.team101.Savers;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipper {
    public static byte[] gzip(String input) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            GZIPOutputStream zipper = new GZIPOutputStream(bytes);
            zipper.write(input.getBytes("UTF-8"));
            zipper.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
        return bytes.toByteArray();
    }

    public static String gunzip (String path) throws FileNotFoundException {
        return GZipper.gunzip(new FileInputStream(path));
    }

    public static String gunzip (InputStream stream) throws FileNotFoundException {
        try {
            GZIPInputStream zipper = new GZIPInputStream(stream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(zipper, "UTF-8"));
            String out = "";
            String line;
            while ((line = reader.readLine()) != null) {
                out += line + "\n";
            }
            return out;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
