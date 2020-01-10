package com.github.gxhunter.util;

import com.jcraft.jsch.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author hunter
 * ftp相关操作
 */
public class SftpTools implements AutoCloseable{

    private static final String NSF = "no such file";
    private Session session;
    private Channel channel;
    /**
     * 对外可访问 ChannelSftp对象提供的所有底层方法
     */
    private ChannelSftp chnSftp;
    /*配置的远程目录地址*/


    /**
     * 连接SFTP
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @throws JSchException
     * @throws SftpException
     * @author inber
     * @since 2012.02.09
     */
    public void connect(String host,int port,String username,String password) throws JSchException{
        JSch jsch = new JSch();
        session = jsch.getSession(username,host,port);
        session.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking","no");
        session.setConfig(sshConfig);
        session.connect();
        channel = session.openChannel("sftp");
        channel.connect();
        chnSftp = (ChannelSftp) channel;
    }


    /**
     * 进入指定的目录并设置为当前目录
     *
     * @param sftpPath
     * @throws Exception
     * @author inber
     * @since 2012.02.09
     */
    public void cd(String sftpPath) throws SftpException{
        chnSftp.cd(sftpPath);
    }

    /**
     * 得到当前用户当前工作目录地址
     *
     * @return 返回当前工作目录地址
     * @author inber
     * @since 2012.02.09
     */
    public String pwd() throws SftpException{
        return chnSftp.pwd();
    }


    /**
     * 判断目录是否存在
     *
     * @param directory 目录或文件
     * @return 是否存在
     * @author inber
     * @since 2012.02.09
     */
    public boolean isDirExist(String directory) throws SftpException{
        boolean isDirExistFlag = false;
        try{
            SftpATTRS sftpAttrs = chnSftp.lstat(directory);
            isDirExistFlag = true;
            return sftpAttrs.isDir();
        }catch(Exception e){
            if(NSF.equals(e.getMessage().toLowerCase())){
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 下载文件后返回流文件
     *
     * @param sftpFilePath 文件路径
     * @return 文件流
     * @throws SftpException
     * @author inber
     * @since 2012.02.09
     */
    public InputStream getFile(String sftpFilePath) throws SftpException{
        if(isFileExist(sftpFilePath)){
            return chnSftp.get(sftpFilePath);
        }
        return null;
    }

    /**
     * 获取远程文件流
     *
     * @param sftpFilePath 文件路径
     * @return 文件流
     * @throws SftpException
     * @author inber
     * @since 2012.02.09
     */
    public InputStream getInputStreamFile(String sftpFilePath) throws SftpException{
        return getFile(sftpFilePath);
    }

    /**
     * 获取远程文件字节流
     *
     * @param sftpFilePath 文件路径
     * @return 文件字节流
     */
    public ByteArrayInputStream getByteArrayInputStreamFile(String sftpFilePath) throws SftpException, IOException{
        if(isFileExist(sftpFilePath)){
            byte[] srcFtpFileByte = inputStreamToByte(getFile(sftpFilePath));
            return new ByteArrayInputStream(srcFtpFileByte);
        }
        return null;
    }

    /**
     * 删除远程
     * 说明:返回信息定义以:分隔第一个为代码，第二个为返回信息
     *
     * @param sftpFilePath 文件路径
     */
    public String delFile(String sftpFilePath) throws SftpException{
        String retInfo = "";
        if(isFileExist(sftpFilePath)){
            chnSftp.rm(sftpFilePath);
            retInfo = "文件已被删除";
        }else{
            retInfo = "文件不存在";
        }
        return retInfo;
    }

    /**
     * 移动远程文件到目标目录
     *
     * @param srcSftpFilePath
     * @param distSftpFilePath
     * @return 返回移动成功或者失败代码和信息
     * @throws SftpException
     * @throws IOException
     */
    public String moveFile(String srcSftpFilePath,String distSftpFilePath) throws SftpException, IOException{
        String retInfo = "";
        boolean dirExist;
        boolean fileExist;
        fileExist = isFileExist(srcSftpFilePath);
        dirExist = isDirExist(distSftpFilePath);
        if(!fileExist){
            //文件不存在直接反回.
            return "0:file not exist !";
        }
        if(!(dirExist)){
            //1建立目录
            createDir(distSftpFilePath);
            //2设置dirExist为true
            dirExist = true;
        }

        String fileName = srcSftpFilePath.substring(srcSftpFilePath.lastIndexOf("/"),srcSftpFilePath.length());
        ByteArrayInputStream srcFtpFileStreams = getByteArrayInputStreamFile(srcSftpFilePath);
        //二进制流写文件
        this.chnSftp.put(srcFtpFileStreams,distSftpFilePath + fileName);
        this.chnSftp.rm(srcSftpFilePath);
        retInfo = "1:move success!";
        return retInfo;
    }

    /**
     * 复制远程文件到目标目录
     *
     * @param srcSftpFilePath
     * @param distSftpFilePath
     * @return
     * @throws SftpException
     * @throws IOException
     */
    public String copyFile(String srcSftpFilePath,String distSftpFilePath) throws SftpException, IOException{
        String retInfo = "";
        boolean dirExist;
        boolean fileExist;
        fileExist = isFileExist(srcSftpFilePath);
        dirExist = isDirExist(distSftpFilePath);
        if(!fileExist){
            //文件不存在直接反回.
            return "0:file not exist !";
        }
        if(!(dirExist)){
            //1建立目录
            createDir(distSftpFilePath);
            //2设置dirExist为true
            dirExist = true;
        }

        String fileName = srcSftpFilePath.substring(srcSftpFilePath.lastIndexOf("/"),srcSftpFilePath.length());
        ByteArrayInputStream srcFtpFileStreams = getByteArrayInputStreamFile(srcSftpFilePath);
        //二进制流写文件
        this.chnSftp.put(srcFtpFileStreams,distSftpFilePath + fileName);
        retInfo = "1:copy file success!";
        return retInfo;
    }

    /**
     * 创建远程目录
     *
     * @param sftpDirPath
     * @return 返回创建成功或者失败的代码和信息
     * @throws SftpException
     */
    public String createDir(String sftpDirPath) throws SftpException{
        this.cd("/");
        if(this.isDirExist(sftpDirPath)){
            return "0:dir is exist !";
        }
        String[] pathArry = sftpDirPath.split("/");
        for(String path : pathArry){
            if("".equals(path)){
                continue;
            }
            if(isDirExist(path)){
                this.cd(path);
            }else{
                //建立目录
                this.chnSftp.mkdir(path);
                //进入并设置为当前目录
                this.chnSftp.cd(path);
            }
        }
        this.cd("/");
        return "1:创建目录成功";
    }

    /**
     * 判断远程文件是否存在
     *
     * @param srcSftpFilePath
     * @return
     * @throws SftpException
     */
    public boolean isFileExist(String srcSftpFilePath) throws SftpException{
        boolean isExitFlag = false;
        // 文件大于等于0则存在文件
        if(getFileSize(srcSftpFilePath) >= 0){
            isExitFlag = true;
        }
        return isExitFlag;
    }

    /**
     * 得到远程文件大小
     *
     * @param srcSftpFilePath
     * @return 返回文件大小，如返回-2 文件不存在，-1文件读取异常
     * @throws SftpException
     */
    public long getFileSize(String srcSftpFilePath) throws SftpException{
        //文件大于等于0则存在
        long fileSize = 0;
        try{
            SftpATTRS sftpAttr = chnSftp.lstat(srcSftpFilePath);
            fileSize = sftpAttr.getSize();
        }catch(Exception e){
            //获取文件大小异常
            fileSize = -1;
            if(NSF.equals(e.getMessage().toLowerCase())){
                //文件不存在
                fileSize = -2;
            }
        }
        return fileSize;
    }

    /**
     * 关闭资源
     */
    @Override
    public void close(){
        if(channel != null && channel.isConnected()){
            channel.disconnect();
        }
        if(session != null && session.isConnected()){
            session.disconnect();
        }
    }

    /**
     * inputStream类型转换为byte类型
     *
     * @param iStrm
     * @return
     * @throws IOException
     * @author inber
     * @since 2012.02.09
     */
    public byte[] inputStreamToByte(InputStream iStrm) throws IOException{
        byte[] imgdata = null;
        try(
                ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        ){
            int ch;
            while((ch = iStrm.read()) != -1){
                bytestream.write(ch);
            }
            imgdata = bytestream.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
        }
        return imgdata;
    }


    public static String uploadMultipartFile(String host,int port,String username,String password,String path,String fileName,MultipartFile multipartFile){
        //创建sftp工具对象
        try(SftpTools stpTool = new SftpTools();
            InputStream inputStream = multipartFile.getInputStream()){
            //连接远程
            stpTool.connect(host,port,username,password);
            //进入对应目录
            stpTool.cd(path);
            stpTool.chnSftp.put(inputStream,fileName);
            return fileName;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否存在指定目录
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param path
     * @return
     */
    public static boolean isExistDir(String host,int port,String username,String password,String path){
        try(SftpTools stpTool = new SftpTools()){
            //连接远程
            stpTool.connect(host,port,username,password);
            return stpTool.isDirExist(path);
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
