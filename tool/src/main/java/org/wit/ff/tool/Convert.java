package org.wit.ff.tool;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import jodd.io.FileNameUtil;
import jodd.io.FileUtil;

/**
 * @author  <a href="mailto:ketayao@gmail.com">ketayao</a>，作者并非本人，这里copy了他的代码，表示感谢.
 * @since   2014年2月20日 上午10:49:33
 * 将GBK格式文件转换成UTF-8
 */
public class Convert {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //GBK编码格式源码路径
        String srcDirPath = "D:\\workspace-github\\jmockit";
        //转为UTF-8编码格式源码路径
        String utf8DirPath = "E:\\bak";


        //获取所有java文件
        Collection<File> javaGbkFileCol = listAll(srcDirPath);

        for (File javaGbkFile : javaGbkFileCol) {
            //UTF8格式文件路径
            String utf8FilePath = utf8DirPath+javaGbkFile.getAbsolutePath().substring(srcDirPath.length());
            //使用GBK读取数据，然后用UTF-8写入数据
            String path = FileNameUtil.getFullPath(utf8FilePath);
            File pf = new File(path);
            if (!pf.exists()) {
                pf.mkdirs();
            }

            FileUtil.writeString(utf8FilePath, FileUtil.readString(javaGbkFile, "GBK"), "UTF-8");
            //FileUtils.writeLines(new File(utf8FilePath), "UTF-8", FileUtils.readLines(javaGbkFile, "GBK"));
        }

    }

    public static Collection<File> listAll(String srcDirPath) {
        Collection<File> files = new HashSet<File>();
        File rootDir = new File(srcDirPath);
        if (rootDir.isDirectory()) {
            File[] allFiles = rootDir.listFiles();

            for (File file : allFiles) {
                if (file.isDirectory()) {
                    files.addAll(listAll(file.getAbsolutePath()));
                } else if (FileNameUtil.getExtension(file.getName()).equalsIgnoreCase("java")) {
                    files.add(file);
                }
            }
        }

        return files;
    }
}
