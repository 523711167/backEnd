package com.example.springbootdemo.springbean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 *  各种postProcess调用顺序
 *      1。 InstantiationAwareBeanPostProcessor -> postProcessBeforeInstantiation
 *      2。 MergedBeanDefinitionPostProcessor -> postProcessMergedBeanDefinition
 *      3。 InstantiationAwareBeanPostProcessor -> postProcessAfterInstantiation
 *      4. InstantiationAwareBeanPostProcessor -> postProcessProperties
 *      5. BeanPostProcessor -> postProcessBeforeInitialization
 *      6. BeanPostProcessor -> postProcessAfterInitialization
 *  RootBeanDefinition
 *      描述定义Bean的元配置信息：比如（类名、bean名称、作用域、Bean之间依赖关系、注解信息）
 *  BeanWrapper
 *      包装了一个bean对象，缓存了bean的内省结果,可以通过API访问、设置bean对象的属性
 *  java内省机制
 *      java通过特定的API操作JavaBean的属性（比如get、set方法）
 *  JavaBean对象
 *      属性都是私有的
 *      存在无参构造方法
 *      私有属性的 getter/setter 方法分别为 getXxx/setXxx
 *      getter 方法必须有返回值且无入参、setter 方法必须有入参且无返回值
 *  singletonObjects属性
 *      单例一级缓存池,保存所有创建完成的bean
 *  singletonsCurrentlyInCreation属性
 *      判断完成是prototype还是single后，标记当前正在创建的singleBean对象
 *  singletonFactories属性
 *      三级缓存池,放入ObjectFactory对象，创建早期的bena引用
 *  earlySingletonObjects属性
 *      二级缓存暂池，（不知道是什么时候写入进入的，在获取循环引用的时候确实是调用了）
 *  registeredSingletons属性
 *      已经创建完成bean，添加记录
 *
 *  alreadyCreated属性
 *      标记Bean即将被创建，创建Bean代码的第一步骤
 *  mergedBeanDefinitions属性
 *      记录写入BeanFactory实现类合并成为RootBeanDefinition，因为通过@Bean还有@Import
 *      还有xml文件定义的，是不同的实现BeanFactory
 *  beanDefinitionMap属性
 *      保存各种Bean本应该生成的BeanDefinition，但是后期合并完成还是会更新回去
 *  factoryBeanInstanceCache
 *       不知道是啥
 *
 *  SpringBean生命周期
 *      1。往alreadyCreated添加正在被创建bean的记录
 *      2。getMergedLocalBeanDefinition(beanName)获取合并后的RootBeanDefinition
 *      3。查找类上是否有DepandOn注解，如果存在，优先实例化注解标注的对象。
 *      4。判断Bean的类型，单例还是原型或者其他类型,调用getSingleton
 *         先把单例beanName写入singletonsCurrentlyInCreation，然后调用getSingleton中的getObject()的doCreateBean方法
 *      5。通过createBeanInstance构建出BeanWrapper对象,然后对BeanDefinition进行
 *         统一后置处理，主要是包括对@Value @Autowire @Import @PostConstruct
 *          @PreDestroy 等解析完成之后在set到BeanDefinition的属性中
 *      6。判断是否需要解决循环依赖，三级缓存添加ObjectFactory，删除二级缓存，registeredSingletons添加BeanName
 *      7。调用populateBean,使用BeanDefinition的属性通过BeanWraper计算bean对象
 *      8。调用initializeBean， 执行各种Aware、initMethed方法,返回bean对象，后续还有几个操作但是没看懂,代码跳到第4步骤
 *      9。第4步骤getObject()后续代码，finally块的内容singletonsCurrentlyInCreation移出beanName，一级缓存添加记录，三级缓存移出，earlySingletonObjects移出，添加已经创建完成bean（这个地方感觉有BUG）
 *      10。在返回第4步getSingleton后续代码
 *
 *
 *      循环引用的情况
 *          1。getBean(animal)在调用populateBean触发getBean(Person)调用populateBean触发getBean(animal)，再次getSingleton方法的时候，singletonsCurrentlyInCreation里面记录animal
 *          然后在一级缓存，二级缓存，三级缓存依次获取，在三级缓存singletonFactory获取到匿名对象getObject获取Bean引用，加入二级缓存，删除三级缓存，返回到Person的populateBean
 */
public class P {

    public static void main(String[] args) {
        ApplicationContext  app = new AnnotationConfigApplicationContext(SpringBeanConfig.class);

        Person person = app.getBean("com.example.springbootdemo.springbean.Person", Person.class);
        System.out.println(person);
    }
}
