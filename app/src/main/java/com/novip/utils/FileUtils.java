package com.novip.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * 文件操作工具
 *
 * @author chen.lin
 *
 */
public class FileUtils {

    private static final String TAG = "FileUtil";

    /**
     * 从sd卡取文件
     *
     * @param filename
     * @return
     */
    public String getFileFromSdcard(String filename) {
        ByteArrayOutputStream outputStream = null;
        FileInputStream fis = null;
        try {
            outputStream = new ByteArrayOutputStream();
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                fis = new FileInputStream(file);
                int len = 0;
                byte[] data = new byte[1024];
                while ((len = fis.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                fis.close();
            } catch (IOException e) {
            }
        }
        return new String(outputStream.toByteArray());
    }

    public static void saveFile(String fileName, String str) {


        // 创建String对象保存文件名路径
        try {
            // 创建指定路径的文件
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            // 如果文件不存在
            if (file.exists()) {
                // 创建新的空文件
                file.delete();
            }
            file.createNewFile();
            // 获取文件的输出流对象
            FileOutputStream outStream = new FileOutputStream(file);
            // 获取字符串对象的byte数组并写入文件流
            outStream.write(str.getBytes());
            // 最后关闭文件输出流
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 取得文件大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            size = fis.available();
        } else {
            f.createNewFile();
        }
        return size;
    }

    /**
     * 递归取得文件夹大小
     *
     * @param dir
     * @return
     * @throws Exception
     */
    public static long getFileSize(File dir) throws Exception {
        long size = 0;
        File flist[] = dir.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 递归求取目录文件个数
     *
     * @param f
     * @return
     */
    public static long getlist(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getlist(flist[i]);
                size--;
            }
        }
        return size;
    }

    /**
     * 在根目录下搜索文件
     *
     * @param keyword
     * @return
     */
    public static String searchFile(String keyword) {
        String result = "";
        File[] files = new File("/").listFiles();
        for (File file : files) {
            if (file.getName().indexOf(keyword) >= 0) {
                result += file.getPath() + "\n";
            }
        }
        if (result.equals("")) {
            result = "找不到文件!!";
        }
        return result;
    }

    /**
     * @detail 搜索sdcard文件
     * @param 需要进行文件搜索的目录
     * @param 过滤搜索文件类型
     * */
    public static List<String> search(File file, String[] ext) {
        List<String> list = new ArrayList<String>();
        if (file != null) {
            if (file.isDirectory()) {
                File[] listFile = file.listFiles();
                if (listFile != null) {
                    for (int i = 0; i < listFile.length; i++) {
                        search(listFile[i], ext);
                    }
                }
            } else {
                String filename = file.getAbsolutePath();
                for (int i = 0; i < ext.length; i++) {
                    if (filename.endsWith(ext[i])) {
                        list.add(filename);
                        break;
                    }
                }
            }
        }
        return list;
    }

    /**
     * 查询文件
     *
     * @param file
     * @param keyword
     * @return
     */
    public static List<File> FindFile(File file, String keyword) {
        List<File> list = new ArrayList<File>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File tempf : files) {
                    if (tempf.isDirectory()) {
                        if (tempf.getName().toLowerCase().lastIndexOf(keyword) > -1) {
                            list.add(tempf);
                        }
                        list.addAll(FindFile(tempf, keyword));
                    } else {
                        if (tempf.getName().toLowerCase().lastIndexOf(keyword) > -1) {
                            list.add(tempf);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * searchFile 查找文件并加入到ArrayList 当中去
     *
     * @param context
     * @param keyword
     * @param filepath
     * @return
     */
    public static List<Map<String, Object>> searchFile(Context context, String keyword, File filepath) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> rowItem = null;
        int index = 0;

        // 判断SD卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File[] files = filepath.listFiles();

            if (files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (file.getName().toLowerCase().lastIndexOf(keyword) > -1) {
                            rowItem = new HashMap<String, Object>();
                            rowItem.put("number", index); // 加入序列号
                            rowItem.put("fileName", file.getName());// 加入名称
                            rowItem.put("path", file.getPath()); // 加入路径
                            rowItem.put("size", file.length() + ""); // 加入文件大小
                            list.add(rowItem);
                        }
                        // 如果目录可读就执行（一定要加，不然会挂掉）
                        if (file.canRead()) {
                            list.addAll(searchFile(context, keyword, file)); // 如果是目录，递归查找
                        }
                    } else {
                        // 判断是文件，则进行文件名判断
                        try {
                            if (file.getName().indexOf(keyword) > -1 || file.getName().indexOf(keyword.toUpperCase()) > -1) {
                                rowItem = new HashMap<String, Object>();
                                rowItem.put("number", index); // 加入序列号
                                rowItem.put("fileName", file.getName());// 加入名称
                                rowItem.put("path", file.getPath()); // 加入路径
                                rowItem.put("size", file.length() + ""); // 加入文件大小
                                list.add(rowItem);
                                index++;
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "查找发生错误!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 根据后缀得到文件类型
     *
     * @param fileName
     * @param pointIndex
     * @return
     */
    public static String getFileType(String fileName, int pointIndex) {
        String type = fileName.substring(pointIndex + 1).toLowerCase();
        if ("m4a".equalsIgnoreCase(type) || "xmf".equalsIgnoreCase(type) || "ogg".equalsIgnoreCase(type) || "wav".equalsIgnoreCase(type)
                || "m4a".equalsIgnoreCase(type) || "aiff".equalsIgnoreCase(type) || "midi".equalsIgnoreCase(type)
                || "vqf".equalsIgnoreCase(type) || "aac".equalsIgnoreCase(type) || "flac".equalsIgnoreCase(type)
                || "tak".equalsIgnoreCase(type) || "wv".equalsIgnoreCase(type)) {
            type = "ic_file_audio";
        } else if ("mp3".equalsIgnoreCase(type) || "mid".equalsIgnoreCase(type)) {
            type = "ic_file_mp3";
        } else if ("avi".equalsIgnoreCase(type) || "mp4".equalsIgnoreCase(type) || "dvd".equalsIgnoreCase(type)
                || "mid".equalsIgnoreCase(type) || "mov".equalsIgnoreCase(type) || "mkv".equalsIgnoreCase(type)
                || "mp2v".equalsIgnoreCase(type) || "mpe".equalsIgnoreCase(type) || "mpeg".equalsIgnoreCase(type)
                || "mpg".equalsIgnoreCase(type) || "asx".equalsIgnoreCase(type) || "asf".equalsIgnoreCase(type)
                || "flv".equalsIgnoreCase(type) || "navi".equalsIgnoreCase(type) || "divx".equalsIgnoreCase(type)
                || "rm".equalsIgnoreCase(type) || "rmvb".equalsIgnoreCase(type) || "dat".equalsIgnoreCase(type)
                || "mpa".equalsIgnoreCase(type) || "vob".equalsIgnoreCase(type) || "3gp".equalsIgnoreCase(type)
                || "swf".equalsIgnoreCase(type) || "wmv".equalsIgnoreCase(type)) {
            type = "ic_file_video";
        } else if ("bmp".equalsIgnoreCase(type) || "pcx".equalsIgnoreCase(type) || "tiff".equalsIgnoreCase(type)
                || "gif".equalsIgnoreCase(type) || "jpeg".equalsIgnoreCase(type) || "tga".equalsIgnoreCase(type)
                || "exif".equalsIgnoreCase(type) || "fpx".equalsIgnoreCase(type) || "psd".equalsIgnoreCase(type)
                || "cdr".equalsIgnoreCase(type) || "raw".equalsIgnoreCase(type) || "eps".equalsIgnoreCase(type)
                || "gif".equalsIgnoreCase(type) || "jpg".equalsIgnoreCase(type) || "jpeg".equalsIgnoreCase(type)
                || "png".equalsIgnoreCase(type) || "hdri".equalsIgnoreCase(type) || "ai".equalsIgnoreCase(type)) {
            type = "ic_file_image";
        } else if ("ppt".equalsIgnoreCase(type) || "doc".equalsIgnoreCase(type) || "xls".equalsIgnoreCase(type)
                || "pps".equalsIgnoreCase(type) || "xlsx".equalsIgnoreCase(type) || "xlsm".equalsIgnoreCase(type)
                || "pptx".equalsIgnoreCase(type) || "pptm".equalsIgnoreCase(type) || "ppsx".equalsIgnoreCase(type)
                || "maw".equalsIgnoreCase(type) || "mdb".equalsIgnoreCase(type) || "pot".equalsIgnoreCase(type)
                || "msg".equalsIgnoreCase(type) || "oft".equalsIgnoreCase(type) || "xlw".equalsIgnoreCase(type)
                || "wps".equalsIgnoreCase(type) || "rtf".equalsIgnoreCase(type) || "ppsm".equalsIgnoreCase(type)
                || "potx".equalsIgnoreCase(type) || "potm".equalsIgnoreCase(type) || "ppam".equalsIgnoreCase(type)) {
            type = "ic_file_office";
        } else if ("txt".equalsIgnoreCase(type) || "text".equalsIgnoreCase(type) || "chm".equalsIgnoreCase(type)
                || "hlp".equalsIgnoreCase(type) || "pdf".equalsIgnoreCase(type) || "doc".equalsIgnoreCase(type)
                || "docx".equalsIgnoreCase(type) || "docm".equalsIgnoreCase(type) || "dotx".equalsIgnoreCase(type)) {
            type = "ic_file_text";
        } else if ("ini".equalsIgnoreCase(type) || "sys".equalsIgnoreCase(type) || "dll".equalsIgnoreCase(type)
                || "adt".equalsIgnoreCase(type)) {
            type = "ic_file_system";
        } else if ("rar".equalsIgnoreCase(type) || "zip".equalsIgnoreCase(type) || "arj".equalsIgnoreCase(type)
                || "gz".equalsIgnoreCase(type) || "z".equalsIgnoreCase(type) || "7Z".equalsIgnoreCase(type) || "GZ".equalsIgnoreCase(type)
                || "BZ".equalsIgnoreCase(type) || "ZPAQ".equalsIgnoreCase(type)) {
            type = "ic_file_rar";
        } else if ("html".equalsIgnoreCase(type) || "htm".equalsIgnoreCase(type) || "java".equalsIgnoreCase(type)
                || "php".equalsIgnoreCase(type) || "asp".equalsIgnoreCase(type) || "aspx".equalsIgnoreCase(type)
                || "jsp".equalsIgnoreCase(type) || "shtml".equalsIgnoreCase(type) || "xml".equalsIgnoreCase(type)) {
            type = "ic_file_web";
        } else if ("exe".equalsIgnoreCase(type) || "com".equalsIgnoreCase(type) || "bat".equalsIgnoreCase(type)
                || "iso".equalsIgnoreCase(type) || "msi".equalsIgnoreCase(type)) {
            type = "ic_file_exe";
        } else if ("apk".equalsIgnoreCase(type)) {
            type = "ic_file_apk";
        } else {
            type = "ic_file_normal";
        }
        return type;
    }

    /**
     * 改变文件大小显示的内容
     *
     * @param size
     * @return
     */
    public static String changeFileSize(String size) {
        if (Integer.parseInt(size) > 1024) {
            size = Integer.parseInt(size) / 1024 + "K";
        } else if (Integer.parseInt(size) > (1024 * 1024)) {
            size = Integer.parseInt(size) / (1024 * 1024) + "M";
        } else if (Integer.parseInt(size) > (1024 * 1024 * 1024)) {
            size = Integer.parseInt(size) / (1024 * 1024 * 1024) + "G";
        } else {
            size += "B";
        }
        return size;
    }

    /**
     * 得到所有文件
     *
     * @param dir
     * @return
     */
    public static ArrayList<File> getAllFiles(File dir) {
        ArrayList<File> allFiles = new ArrayList<File>();
        // 递归取得目录下的所有文件及文件夹
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            allFiles.add(file);
            if (file.isDirectory()) {
                getAllFiles(file);
            }
        }
        Log.i("test", allFiles.size() + "");
        return allFiles;
    }

    /**
     * 判断文件MimeType 类型
     *
     * @param f
     * @return
     */
    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

        /* 依扩展名的类型决定MimeType */
        if (end.equalsIgnoreCase("m4a") || end.equalsIgnoreCase("mp3") || end.equalsIgnoreCase("mid") || end.equalsIgnoreCase("xmf")
                || end.equalsIgnoreCase("ogg") || end.equalsIgnoreCase("wav")) {
            type = "audio";
        } else if (end.equalsIgnoreCase("3gp") || end.equalsIgnoreCase("mp4")) {
            type = "video";
        } else if (end.equalsIgnoreCase("jpg") || end.equalsIgnoreCase("gif") || end.equalsIgnoreCase("png")
                || end.equalsIgnoreCase("jpeg") || end.equalsIgnoreCase("bmp")) {
            type = "image";
        } else if (end.equalsIgnoreCase("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else if (end.equalsIgnoreCase("txt") || end.equalsIgnoreCase("java")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "text";
        } else {
            type = "*";
        }
        /* 如果无法直接打开，就跳出软件列表给用户选择 */
        if (end.equalsIgnoreCase("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    /**
     * 拷贝文件
     *
     * @param fromFile
     * @param toFile
     * @throws IOException
     */
    public static void copyFile(File fromFile, String toFile) throws IOException {

        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1)
                to.write(buffer, 0, bytesRead); // write
        } finally {
            if (from != null)
                try {
                    from.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            if (to != null)
                try {
                    to.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
        }
    }

    /**
     * 创建文件
     *
     * @param file
     * @return
     */
    public static File createNewFile(File file) {

        try {

            if (file.exists()) {
                return file;
            }

            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            Log.e(TAG, "", e);
            return null;
        }
        return file;
    }

    /**
     * 创建文件
     *
     * @param path
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        return createNewFile(file);
    }// end method createText()

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        deleteFile(file);
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        file.delete();
    }

    /**
     * 向Text文件中写入内容
     *
     * @param file
     * @param content
     * @return
     */
    public static boolean write(String path, String content) {
        return write(path, content, false);
    }

    public static boolean write(String path, String content, boolean append) {
        return write(new File(path), content, append);
    }

    public static boolean write(File file, String content) {
        return write(file, content, false);
    }

    /**
     * 写入文件
     *
     * @param file
     * @param content
     * @param append
     * @return
     */
    public static boolean write(File file, String content, boolean append) {
        if (file == null || content.isEmpty()) {
            return false;
        }
        if (!file.exists()) {
            file = createNewFile(file);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, append);
            fos.write(content.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
            fos = null;
        }

        return true;
    }

    /**
     * 获得文件名
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        if (path.isEmpty()) {
            return null;
        }
        File f = new File(path);
        String name = f.getName();
        f = null;
        return name;
    }

    /**
     * 读取文件内容，从第startLine行开始，读取lineCount行
     *
     * @param file
     * @param startLine
     * @param lineCount
     * @return 读到文字的list,如果list.size<lineCount则说明读到文件末尾了
     */
    public static List<String> readFile(File file, int startLine, int lineCount) {
        if (file == null || startLine < 1 || lineCount < 1) {
            return null;
        }
        if (!file.exists()) {
            return null;
        }
        FileReader fileReader = null;
        List<String> list = null;
        try {
            list = new ArrayList<String>();
            fileReader = new FileReader(file);
            LineNumberReader lineReader = new LineNumberReader(fileReader);
            boolean end = false;
            for (int i = 1; i < startLine; i++) {
                if (lineReader.readLine() == null) {
                    end = true;
                    break;
                }
            }
            if (end == false) {
                for (int i = startLine; i < startLine + lineCount; i++) {
                    String line = lineReader.readLine();
                    if (line == null) {
                        break;
                    }
                    list.add(line);

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "read log error!", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 创建文件夹
     *
     * @param dir
     * @return
     */
    public static boolean createDir(File dir) {
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "create dir error", e);
            return false;
        }
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public static File creatSDDir(String dirName) {
        File dir = new File(dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断SD卡上的文件是否存在
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static File write2SDFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            creatSDDir(path);
            file = createNewFile(path + "/" + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int len = -1;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 读取文件内容 从文件中一行一行的读取文件
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        Reader read = null;
        String content = "";
        String result = "";
        BufferedReader br = null;
        try {
            read = new FileReader(file);
            br = new BufferedReader(read);
            while ((content = br.readLine().toString().trim()) != null) {
                result += content + "\r\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                read.close();
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩,
     * 特点是: File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
     *
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;// 个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  将图片从本地读到内存时,进行压缩 ,即图片从File形式变为Bitmap形式
     *  特点: 通过设置采样率, 减少图片的像素, 达到对内存中的Bitmap进行压缩
     * @param srcPath
     * @return
     */
    public static Bitmap compressImageFromFile(String srcPath, float pixWidth, float pixHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);

        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        //float pixWidth = 800f;//
        //float pixHeight = 480f;//
        int scale = 1;
        if (w > h && w > pixWidth) {
            scale = (int) (options.outWidth / pixWidth);
        } else if (w < h && h > pixHeight) {
            scale = (int) (options.outHeight / pixHeight);
        }
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;// 设置采样率

        options.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        options.inPurgeable = true;// 同时设置才会有效
        options.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, options);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    /**
     *   指定分辨率和清晰度的图片压缩
     */
    public void transImage(String fromFile, String toFile, int width, int height, int quality)
    {
        try
        {
            Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            // 缩放图片的尺寸
            float scaleWidth = (float) width / bitmapWidth;
            float scaleHeight = (float) height / bitmapHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 产生缩放后的Bitmap对象
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            // save file
            File myCaptureFile = new File(toFile);
            FileOutputStream out = new FileOutputStream(myCaptureFile);
            if(resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)){
                out.flush();
                out.close();
            }
            if(!bitmap.isRecycled()){
                bitmap.recycle();//记得释放资源，否则会内存溢出
            }
            if(!resizeBitmap.isRecycled()){
                resizeBitmap.recycle();
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

}