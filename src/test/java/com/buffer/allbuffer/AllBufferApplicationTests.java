package com.buffer.allbuffer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class AllBufferApplicationTests {

    /**
     * stringBuilder对象做拼接的时候不会重新创建空间
     */
    @Test
    public void myBuilder(){
        StringBuilder stringBuilder = new StringBuilder();
        //查看stringBuilder的内存空间
        int capacity = stringBuilder.capacity();
        System.out.println("内存空间是："+capacity);
        //stringBuilder可以拼接任何类型
        stringBuilder.append("abc");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("k","v");
        stringBuilder.append(true).append(0).append(hashMap);
        //拼接后的值：abctrue0{k=v}
        System.out.println("拼接后的值："+stringBuilder);
        //stringBuilder转string
        String s = stringBuilder.toString();
        //string转成stringBuilder  使用new StringBuilder(s)
        StringBuilder stringBuilder1 = new StringBuilder(s);
    }

    @Test
    void contextLoads() {
        for (int charInt=0;charInt<10000;charInt++){
            //将0-10000转成ASCII码表对应的字符
            System.out.println(charInt+"  "+(char) charInt);
        }
    }

    /**
     * 字符缓冲流，操作的是字符  通常读写文本
     * BufferedWriter ：字符输出流
     * BufferedReader ：字符输入流
     */
    @Test
    public void strBuffer() throws IOException {
        //创建输出缓冲流对象  并将字符串写进文本  会情况文本中原有内容
        BufferedWriter bufWriter = new BufferedWriter(new FileWriter("file/str.txt"));
        String str = "helloWorld";
        bufWriter.newLine();//换行功能
        bufWriter.write(str);
        bufWriter.close();

        //创建输入缓冲流对象 一次读取一个字符数组 高效批量读取
        BufferedReader bufReader = new BufferedReader(new FileReader("file/被读取的文本.txt"));
        char[] chars = new char[1024];
        int len;

        while ((len=bufReader.read(chars))!=-1){
            System.out.print(new String(chars,0,len));
        }
        bufReader.close();


        //创建输入缓冲流对象 一次读取一个字符，换行算一个字符
        BufferedReader bufReader2 = new BufferedReader(new FileReader("file/被读取的文本.txt"));
        int charInt;
        while ((charInt=bufReader2.read())!=-1){//每次读取一个字符，如果读取超过了长度就跳出
            System.out.print((char) charInt);//将每次读取到的字符编码转换成ASCII码字符
        }
        bufReader2.close();

        //创建输入缓冲流对象 一次读取一行字符 不读取换行符
        BufferedReader bufReader3 = new BufferedReader(new FileReader("file/被读取的文本.txt"));
        String line;
        while ((line=bufReader3.readLine())!=null){
            System.out.println(line);//将读取到的每行字符打印
        }
        bufReader2.close();

        //复制文本文件功能
        BufferedReader br = new BufferedReader(new FileReader("file/被读取的文本.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("file/copy.txt"));
        char[]chs = new char[1024];
        int length;
        while ((length=br.read(chs))!=-1){//一次读取一个字节数组  高效读写
            bw.write(chs,0,length);
        }
        bw.close();
        br.close();

    }

    /**
     *  字节流，操作的是字节  通常读写图片，视频，文件
     */
    @Test
    public void byteBuffer() throws Exception {
        BufferedInputStream bufInpStream = new BufferedInputStream(new FileInputStream("file/yanjinkuan.jpg"));
        BufferedOutputStream bufOutStream = new BufferedOutputStream(new FileOutputStream("file/字节流复制的图片.jpg"));
        //字节缓冲流的高效写入实现文件复制
        byte[] bytes = new byte[1024];
        int len;
        while ((len=bufInpStream.read(bytes))!=-1){
            bufOutStream.write(bytes,0,len);
        }
        bufInpStream.close();
        bufOutStream.close();

    }

    /**
     * 正则表达式 案例
     */
    @Test
    public void regexStr(){
        //写正则表达式
        String regex = "(.*)(\\d+)(.*)";  //表示前面任意字符若干,中间出现过数字
        // 将给定的正则表达式编译到模式中。
        Pattern pattern = Pattern.compile(regex);
        //匹配字符串
        Matcher matcher = pattern.matcher("This order was placed for QT3000! OK?");
        boolean b = matcher.find();//得到matcher后可以使用一系列方法：find、group、replaceAll。
        System.out.println(b); //1.

        String s = "akkk";//字符串
        String regex1 = "[ab](kkk)";//正则表达式 第一个字符是a或者是b，第二段字符必须是"kkk" 并且只有这两组字符
        boolean bool = s.matches(regex1);
        System.out.println(bool);//2. 返回true


        String s2 = "yj";//字符串
        String regex2 = "[^Yb][^JM]";//正则表达式 第一个字符不是Y也不是b，第二个字符不是J也不是M 并且只有这2个字符
        boolean bool2 = s2.matches(regex2);
        System.out.println(bool2);//3. 返回true

        String s3 = "abcd";//字符串
        String regex3 = "a.*d";//第一个字符是a,中间是任意字符,最后一个字符是d
        boolean bool3 = s3.matches(regex3);
        System.out.println(bool3);//4. 返回true


        String str = "今天天气好晴朗 处处好风光 今天太阳公公好明朗 放着耀眼的光";
        String regex4 = "(今天)[^\\s]*\\s"; //匹配以 "今天" 开头,中间是非空格字符,以空格结尾的句子
        //将正则表达式转成正则对象
        Pattern pat = Pattern.compile(regex4);
        //正则对象与字符串匹配
        Matcher mat = pat.matcher(str);
        while (mat.find()){
            //将匹配成功后打印匹配的内容
            System.out.println(mat.group()); //会输出两句:1.今天天气好晴朗  2.今天太阳公公好明朗
        }

    }

}
