package com.tecnologiabasica.jettyapicommons.util;

import com.tecnologiabasica.jettyapicommons.JAppCommons;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author afonso
 */
public class JFileUtil {

    public enum EFileExtension {
        TAR_GZ(".tar.gz"),
        TMP(".tmp"),
        GZ(".gz"),
        LOG(".log");

        private String value;

        private EFileExtension(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

    }

    private static JFileUtil instance = null;

    public static JFileUtil getInstance() {
        if (instance == null) {
            instance = new JFileUtil();
        }
        return instance;
    }

    private JFileUtil() {

    }

    public static boolean createDirectory(String path) {
        File theDir = new File(path);

        boolean success = false;
        if (!theDir.exists()) {
            try {
                success = theDir.mkdirs();
            } catch (SecurityException se) {
                System.err.println(se.getMessage());
            }
        }

        return success;
    }

    public void deleteTempFiles() {
        String temp;
        String path;

        File folder = new File(JAppCommons.getHomeDir());
        try {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile()) {
                    temp = fileEntry.getName();
                    if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("tmp")) {
                        path = folder.getAbsolutePath() + File.separator + fileEntry.getName();
                        //System.out.println("Deletando arquivo temporário perdido: " + fileEntry.getName());
                        File fileToDelete = new File(path);
                        fileToDelete.delete();
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }

    public void deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                //System.out.println("Arquivo apagado " + path);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    // save uploaded file to a defined location on the server
    public void saveFile(InputStream uploadedInputStream,
            String serverLocation) throws FileNotFoundException, IOException {
        try {
            OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
            int read = 0;
            boolean isRead = false;
            int offSet = 0;
            byte[] bytes = new byte[uploadedInputStream.available()]; //nao estava salvando imagem grande
            outpuStream = new FileOutputStream(new File(serverLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                if (!isRead) {
                    for (int i = 0; i < read; i++) {
                        if (bytes[i] == 31 && bytes[i + 1] == -117 && bytes[i + 2] == 8) {
                            offSet = i;
                            isRead = true;
                        }
                    }
                } else {
                    offSet = 0;
                }
                outpuStream.write(bytes, offSet, read - offSet);
            }
            outpuStream.flush();
            outpuStream.close();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            deleteFile(serverLocation);
        }
    }

    public static boolean unTarFile(String sourcePath, String destPath) {
        final int BUFFER = 2048;
        boolean result = false;
        try {
            FileInputStream fin = new FileInputStream(sourcePath);
            BufferedInputStream in = new BufferedInputStream(fin);
            GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
            TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);

            TarArchiveEntry entry = null;

            /**
             * Read the tar entries using the getNextEntry method *
             */
            while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {

                if (entry.isFile()) {
                    String filename = entry.getName().substring(entry.getName().lastIndexOf(File.separator) + 1);
                    //System.out.println("Extracting: " + filename);

                    int count;
                    byte data[] = new byte[BUFFER];

                    FileOutputStream fos = new FileOutputStream(destPath + filename);
                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = tarIn.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.close();
                }
            }

            /**
             * Close the input stream *
             */
            tarIn.close();
            result = true;
            //System.out.println("untar completed successfully!!");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public File compressFolderToTarGZ(String source, String dest, String deviceId) {

        File output = new File(dest);
        File fileDir = new File(source);
        try {
            FileOutputStream fos = new FileOutputStream(output);

            TarArchiveOutputStream taos = new TarArchiveOutputStream(
                    new GZIPOutputStream(new BufferedOutputStream(fos)));
            taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);
            // Habilita o suporte para nome extenso
            taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

            addFilesToCompression(taos, fileDir, deviceId);

            taos.close();
            fos.close();
        } catch (IOException ex) {
            System.err.println("Erro ao comprimir arquivos: " + ex.getMessage());
        }

        return output;
    }

    private void addFilesToCompression(TarArchiveOutputStream taos, File file, String deviceId)
            throws IOException {
        //Parametro deviceId é o nome da pasta que fica na raiz do tar.gz
        if (file.isFile()) {
            // Create an entry for the file
            taos.putArchiveEntry(new TarArchiveEntry(file, file.getName()));
            // Adiciona o arquivo na pasta
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            IOUtils.copy(bis, taos);
            taos.closeArchiveEntry();
            bis.close();
        } else if (file.isDirectory()) {
            //Faz a varredura dentro da pasta
            for (File childFile : file.listFiles()) {
                addFilesToCompression(taos, childFile, deviceId);
            }
        }

    }

    public static String getFileAsBase64(String path) throws IOException {
        return getFileAsBase64(new File(path));
    }

    public static String getFileAsBase64(File file) throws IOException {
        String fileAsBase64 = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);
            fileAsBase64 = Base64.getEncoder().encodeToString(bytes);
        } catch (FileNotFoundException ex) {

        }
        return fileAsBase64;
    }

    public static String getFileAsBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static Properties getPropertiesFile(String path) {
        Properties props = null;
        try {
            File fileTest = new File(path);
            if (fileTest.exists()) {
                FileInputStream file = new FileInputStream(path);
                props = new Properties();
                props.load(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return props;
    }

    public InputStream convertBase64ToInputStream(String base64) {

        return new ByteArrayInputStream(Base64.getDecoder().decode(base64.getBytes()));
    }
}
