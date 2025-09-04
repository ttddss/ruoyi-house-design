package com.ruoyi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.bean.Cat;
import com.ruoyi.bean.Color;
import com.ruoyi.bean.Dog;
import com.ruoyi.bean.DogRepository;
import com.ruoyi.bean.config.MyConfiguration;
import com.ruoyi.bean.proxybean.Pa;
import com.ruoyi.bean.proxybean.Pb;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.helpers.BasicMarkerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.metrics.StartupStep;
import org.springframework.core.task.TaskExecutor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SimpleThreadPoolTaskExecutor;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author tds
 * @version 1.0.0
 * @Company
 * @ClassName SpringTest
 * @Description TODO（这里用一句话描述这个类的作用)
 * @Date 2025-07-28 12:22
 */
@Slf4j
public class SpringTest {

    @Test
    public void test1() {
        // bean定义的注册器。spring的bean工厂或上下文有的也实现了这个类。这里的SimpleBeanDefinitionRegistry就是将bean定义放到了一个
        // ConcurrentHashMap中管理了
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

        // 1.xml方式读取xml的bean定义
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
        reader.loadBeanDefinitions("classpath:bean.xml");
        BeanDefinition beanDefinition = registry.getBeanDefinition("dog");
        System.out.println(beanDefinition);

    }

    @Test
    public void test2() {
        // 2.扫描classpath下某些包下的bean定义
        // bean定义的注册器。spring的bean工厂或上下文有的也实现了这个类。这里的SimpleBeanDefinitionRegistry就是将bean定义放到了一个
        // ConcurrentHashMap中管理了
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        // spring使用asm读取字节码，将字节码信息转换成对象，对象中封装了字节码信息。读取字节码.class文件获取类信息比反射更快。
        scanner.scan("com.ruoyi.bean");
        System.out.println(registry.getBeanDefinition("dog"));
    }

    @Test
    public void test3() {
        // 3.内省api。通过反射获取bean信息
        log.info("3.内省api。通过反射获取bean信息");
        // 将字符串首字母小写
        System.out.println(Introspector.decapitalize("Dog"));
        try {
            // 获取bean信息.不自省父类Object的属性
            BeanInfo beanInfo = Introspector.getBeanInfo(Dog.class, Object.class);
            BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
            MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            log.info("beanDescriptor:{}, {}", beanDescriptor.getName(), beanDescriptor.getClass());
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                log.info("propertyDescriptor:{}, {}, {}", propertyDescriptor.getName(),
                        propertyDescriptor.getPropertyType(), propertyDescriptor.getReadMethod());
            }
            for (MethodDescriptor methodDescriptor : methodDescriptors) {
                log.info("methodDescriptor:{}", methodDescriptor.getMethod(), methodDescriptor.getName(), methodDescriptor.getClass());
            }
            Dog dog = new Dog();
            dog.setAge(100);
            Object res = propertyDescriptors[0].getReadMethod().invoke(dog);
            log.info("反射调用方法{},{}", propertyDescriptors[0].getReadMethod(), res);
        } catch (Exception e) {
            log.error("错误堆栈信息", e);
        }
    }

    @Test
    public void test4() {
        // 4 spring中的BeanUtils
        Dog dogSrc = Dog.builder()
                .age(10)
                .parent(
                        Dog.builder()
                                .name("老王王").build()
                )
                .name("旺财").build();
        Dog dogTarget = new Dog();
        BeanUtils.copyProperties(dogSrc, dogTarget);
        log.info("BeanUtils复制对象：{}", dogTarget);
        log.info("是否是浅拷贝：{}", dogSrc.getParent() == dogTarget.getParent());
    }

    @Test
    public void test5() throws ClassNotFoundException {
        log.info("测试BeanWrapper.更方便使用放射创建对象");
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
//        beanDefinition.setBeanClassName("com.ruoyi.bean.Dog");
        beanDefinition.setBeanClass(Dog.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues()
                .add("name", "旺财")
                .add("age", "10");
        beanDefinition.setPropertyValues(propertyValues);
        Class.forName(beanDefinition.getBeanClassName());

        // 使用BeanWrapper包装实例，隐藏了反射过程，更方便使用反射方法。装饰模式
        BeanWrapper beanWrapper = new BeanWrapperImpl(beanDefinition.getBeanClass());
        // 使用转化服务，不然属性转化会有问题？？实际我测试发现没有问题
//        beanWrapper.setConversionService(new DefaultConversionService());
        beanWrapper.setPropertyValues(beanDefinition.getPropertyValues());
        Object instance = beanWrapper.getWrappedInstance();
        log.info("根据BeanDifinition实例化对象:{}", instance);

    }


    private HashMap<String, List<Dog>> map;

    @Test
    public void test6() throws NoSuchFieldException {
        // 对类类型、超类类型、泛型进行获取
        ResolvableType resolvableType = ResolvableType.forField(getClass().getDeclaredField("map"));
        log.info("SuperType:{}", resolvableType.getSuperType());
        log.info("Type:{}", resolvableType.getType());
        log.info("Generics[0]:{}", resolvableType.getGenerics()[0]);
        log.info("resolveGeneric(1, 0):{}",  resolvableType.resolveGeneric(1, 0));

    }


    @Test
    public void test7 () {
        // 不同类型的环境实现，会通过重写org.springframework.core.env.StandardEnvironment.customizePropertySources方法来加载不
        // 同类型的propertiies属性集合。
        Environment environment = new StandardEnvironment();
        log.info("JAVA_HOME:{}", environment.getProperty("JAVA_HOME"));
    }

    @Test
    public void test8() throws InterruptedException {
        // 组播器。事件机制，发布/订阅，观察者模式。
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        // 如果提供了线程池，那么组播器通知监听者（观察者）时会在线程池中执行。
        eventMulticaster.setTaskExecutor(Executors.newFixedThreadPool(3));
        // 添加监听者（观察者）
        eventMulticaster.addApplicationListener(event ->
                log.info("当前线程：{},监听到事件发生：{},来源：{}, 时间：{}, 开始我的处理...", Thread.currentThread().getName(),
                        event, event.getSource(), event.getTimestamp()));
        // 创建事件
        ApplicationEvent event = new ApplicationEvent(this) {
            @Override
            public Object getSource() {
                return "测试通知事件";
            }
        };
        // 发布事件
        eventMulticaster.multicastEvent(event);
        eventMulticaster.multicastEvent(event);
        eventMulticaster.multicastEvent(event);
        ThreadUtil.sleep(1000 * 10);

    }

    @Test
    public void test9() {
        // 国际化
        ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
        rbms.setBasename("i18n/message");
        rbms.setDefaultEncoding("UTF-8");
        String message = rbms.getMessage("hello", new Object[]{"张三"}, "not found", Locale.CHINA);
        System.out.println(message);
        String message2 = rbms.getMessage("hello", new Object[]{"bob"}, "not found", Locale.US);
        System.out.println(message2);
    }

    @Test
    public void test10() {
        // 表达式
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("'hello world'");
        System.out.println(expression.getValue());
        expression = parser.parseExpression("'hello world'.bytes");
        System.out.println(((byte[])expression.getValue()).length);
        expression = parser.parseExpression("'hello world'.concat('|')");
        System.out.println(expression.getValue());
    }

    @Test
    public void test11() {
        // 父子bean工厂
        DefaultListableBeanFactory parentBeanFactory = new DefaultListableBeanFactory();
        parentBeanFactory.registerSingleton("dog", Dog.builder().name("wangcai").build());

        DefaultListableBeanFactory childBeanFacotory = new DefaultListableBeanFactory();
        childBeanFacotory.setParentBeanFactory(parentBeanFactory);

        System.out.println(childBeanFacotory.getBean("dog"));

    }


    @Test
    public void test12() {
        BufferingApplicationStartup startup = new BufferingApplicationStartup(1024);
        StartupStep startupStep = startup.start("一.初始化阶段。。。");
        startupStep.tag("步骤1", "起锅").tag("步骤2", "烧油").end();
        startup.start("二.启动阶段。。。").tag("1", "大火焖1个小时").tag("2","上菜").end();

        startup.getBufferedTimeline().getEvents().forEach((event)->{
            StartupStep step = event.getStartupStep();
            log.info("{}", step.getName());
            step.getTags().forEach((tag)->{
                log.info("{} {}", tag.getKey(), tag.getValue());
            });
        });



    }


    @Test
    public void test13() {
        GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext("classpath:bean.xml");
        log.info("{}", applicationContext.getBean("dog"));
    }

    @Test
    public void test14() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyConfiguration.class);
        // spring的三级缓存解决循环依赖
        //  1.第三级缓存使用ObjectFactory函数式接口生成匿名内部类对象，是因为早期实例化的对象，可能在后续创建过程中被一些初始化的后置处理
        // 器覆盖原来的对象（比如代理对象），此时对象时新的对象，那么ObjectFactory对象就会返回新的对象，而不是返回原始对象。
        //  2.a、b循环依赖，获取a对象，会先创建一共个ObjectFatory的工厂对象到三级缓存，后面注入属性时，发现依赖b，然后b也开始创建。b也先
        // 创建ObjectFatory的工厂对象到三级缓存,b到注入属性阶段发现需要注入a。然后获取a，获取的时候发现a存在3级缓存，然后就将a的三级
        // 缓存工厂对象拿出来创建一个新的对象，放入二级缓存，并将a新的对象注入到b，b完成bean的创建了，将b放入1级缓存，删除3级缓存。
        // b创建完后直接注入a，a也就创建完了。删除a的二级缓存，放入一
        // 级缓存。
        //  3.获取bean对象的方法是一次从1-3级缓存取，如果1/2级缓存取不到，三级缓存取得到，会将三级缓存生成的工厂对象放入二级缓存，并删除三级缓存。
        // org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(java.lang.String, boolean)
        //  4.构造器注入、prototype bean的循环依赖，会报错。
        //  5.@Async因为会在对象初始化后，AsyncAnnotationBeanPostProcessor会给其创建了一个代理对象，导致循环依赖无法解.如果它像
        //  AnnotationAwareAspectJAutoProxyCreator一样在早期生成工厂对象时生成代理，那么就不会导致循环依赖了。
        //  6.AnnotationAwareAspectJAutoProxyCreator：这个是生成切面动态代理的后置处理器。它有两个后置处理方法。实现了
        //  SmartInstantiationAwareBeanPostProcessor的getEarlyBeanReference方法，用于bean的三级缓存工厂对象生成二级缓存
        //  对象。实现BeanPostProcessor，它的postProcessAfterInitialization后置处理（并初始化后调用）会判断是否之前生成过
        //  代理对象了，如果生成过了，那么此时就不用生成了。但是二级缓存里面存的是代理对象的引用，最后会将二级缓存的引用放入一级缓存。

        //  7.假设只用1级缓存，第一次实例化后就给他生成代理，那么属性注入就注入代理了，不太合理；假设使用二级缓存，我感觉也是可以的。但是如果
        // 我需要一个完整初始化完成的bean我无法确定，因为此时一级缓存中可能存在未初始化完成的对象。
        log.info("{}", applicationContext.getBean("dog"));
        log.info("{}", applicationContext.getBean("cat"));
//        log.info("{}", applicationContext.getBean("catFactoryBean"));
//        log.info("{}", applicationContext.getBean(BeanFactory.FACTORY_BEAN_PREFIX + "catFactoryBean"));
//        applicationContext.getBean(Cat.class).async();
//        ThreadUtil.sleep(100 * 1000);
        Pa pa = applicationContext.getBean(Pa.class);
        Pb pb = applicationContext.getBean(Pb.class);
        log.info("pa:{}", pa);
        log.info("pb:{}", pb);
    }
}
