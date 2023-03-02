package org.pdaodao.spring;

/**
 *  dispatcherServlet的doDispatch方法，
 *  第一步：遍历handleMapper通过request取出程序处理器mappedHandler，
 *  第二步：进而在获取到HandlerAdapter，调用handler获取到ModelAndView,
 *  第三步：通过试图解析器封装View对象返回给dispatcherServlet，
 *  第四步：view对象将数据写入HttpResponse，返回给浏览器。
 */
public interface SpringMvc {
}
