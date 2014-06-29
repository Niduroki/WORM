package de.hhu.propra14.team101.Savers;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Class to gzip data </br></br>
 *
 * Code example:
 * <pre>
 * {@code
 *  GZipper zipper = new GZipper();
 * String longString = "Lorem ipsum dolor sit amet consetetur ..."
 * byte[] gzipped = zipper.gzip(longString);
 * // Write gzipped into a file now, or do something else
 * String unzipped = zipper.gunzip("path/to/file.gz");
 * }
 * </pre>
 */
public class GZipper {
    /**
     * Compresses a string
     *
     * @param input String to compress
     * @return Compressed data in a byte array
     */
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

    /**
     * Decompresses a file
     *
     * @param path Path to uncompress
     * @return Uncompressed String
     * @throws FileNotFoundException If file not found
     */
    public static String gunzip(String path) throws FileNotFoundException {
        return GZipper.gunzip(new FileInputStream(path));
    }

    /**
     * Decompresses a stream
     *
     * @param stream Stream to uncompress
     * @return Uncompressed String
     * @throws FileNotFoundException If file not found
     */
    public static String gunzip(InputStream stream) throws FileNotFoundException {
        try {
            GZIPInputStream zipper = new GZIPInputStream(stream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(zipper, "UTF-8"));
            String out = "";
            String line;
            while ((line = reader.readLine()) != null) {
                out += line + "\n";
            }
            try {
                stream.close();
                zipper.close();
                reader.close();
            } catch (IOException e) {
                //
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
