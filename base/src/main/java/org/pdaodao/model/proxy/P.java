package org.pdaodao.model.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class P {

    /**
     * 描述一下动态代理
     *      作用：在不修改源代码的情况下，对方法进行增强
     *      实现关键点：
     *          1。实现InvotionHandler接口，同时在实例化的同时会传入需要增强对象，在invoke中编写增强的代码
     *          2。通过JDK的getProxyClass方法传入增强对象的接口创建代理对象的class对象，
     *          在找到入参是invotionHandler的构造方法，调用newInstance创建代理对象
     *          3。通过代理对象调用接口方法，实际上是invotionhandler对象调用invoke方法
     *
     *
     */


    /**
     * 代理对象你需要知道
     *
     * 代理对象public final class $Proxy0 extends Proxy implements Person
     *
     * 通过Proxy.getProxyClass(TestProxy.class.getClassLoader(), Person.class)生成代理对象
     * 关键点
     *  1。代理对象的父类Proxy持有invocationHandler对象
     *  2。代理对象有invocationHandler形参的构造方法
     *  3。代理对象静态块代码会获取所有方法的Method对象
     *  4。代理对象会实现接口的所有方法
     *
     * 通过proxyClass.getConstructor(InvocationHandler.class)返回构造函数对象
     * 通过constructor.newInstance(handler);创建代理Person类的实例对象
     * o.work("pxx");执行方法
     *  关键点
     *  1。此时的o是代理对象，不是Pxx对象
     *  2。代理对象实现了Person方法，h.invoke(this, m3, null)调用了的InvocationHandler的方法
     *
     */
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Handler handler = new Handler(new Pxx());

        Class<?> proxyClass = Proxy.getProxyClass(P.class.getClassLoader(), Person.class);
        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
        Person o = (Person) constructor.newInstance(handler);
        o.toString();
    }

    /**
     * //这个静态块本来是在最后的，我把它拿到前面来，方便描述
     *    static
     *   {
     *     try
     *     {
     *       //看看这儿静态块儿里面有什么，是不是找到了giveMoney方法。请记住giveMoney通过反射得到的名字m3，其他的先不管
     *       m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[] { Class.forName("java.lang.Object") });
     *       m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
     *       m3 = Class.forName("proxy.Person").getMethod("giveMoney", new Class[0]);
     *       m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
     *       return;
     *     }
     *     catch (NoSuchMethodException localNoSuchMethodException)
     *     {
     *       throw new NoSuchMethodError(localNoSuchMethodException.getMessage());
     *     }
     *     catch (ClassNotFoundException localClassNotFoundException)
     *     {
     *       throw new NoClassDefFoundError(localClassNotFoundException.getMessage());
     *     }
     *   }
     *
     *   public final void work()
     *     throws
     *   {
     *     try
     *     {
     *       this.h.invoke(this, m3, null);
     *       return;
     *     }
     *     catch (Error|RuntimeException localError)
     *     {
     *       throw localError;
     *     }
     *     catch (Throwable localThrowable)
     *     {
     *       throw new UndeclaredThrowableException(localThrowable);
     *     }
     *   }
     */
}
