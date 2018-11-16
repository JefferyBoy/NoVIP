@[TOC](Android的NoVIP视频解析APP开发完整过程)

# 开发灵感
国内现在视频类知名网站主要有：爱奇艺、优酷、腾讯视频、搜狐视频、乐视视频、芒果TV、Bilibili等。这些网站都有各自的付费VIP视频，我虽然很少去看这些视频啦，我主要是看一些新闻类的，这些都是免费的。但是，生活中很多同事、朋友都在追剧，在办公室在学校经常会听到某某有没有XX视频的VIP账号？生活中其实很多人都开通了这些视频网站的VIP。然而，依旧问题严重，因为很多电影&电视剧是版权独家的，只有某个视频站才能观看，那么买的其他站的VIP那不就是无效了吗？当然，对于开发者来说，网上很多浏览器插件可以解决这个问题，比如**Greasy Fork**插件就牛的一逼，各种VIP视频、网盘资源等都不在话下。对于某部分人来说，上班偷偷看看手机不方便看电脑、回家路上看手机，但是手机端并没有这些插件呀。于是，笔者想设计一个APP，让手机端可以方便的查看各种视频，正式这个原因，着手开发了，花了两天时间把APP做出来了，测试能够播放各个站点的VIP视频。下面是APP的源码：

[Github源代码](https://github.com/JafferLei/NoVIP)

# 功能需求设计
1. 解析播放各大站点的VIP视频是核心，对于视频解析来说，整个过程是比较复杂的，笔者查看很多相关资料，各大战点都做了多层的视频链接加密，还做了视频分段处理。笔者也就懒得去做了，直接调用了其他站点的解析地址。
![实现了6个站点的VIP视频解析](https://img-blog.csdn.net/2018092113095778?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTIxODQ1Mzk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
下面是视频解析播放的方法，在进入视频播放也后选择播放线路（部分播放线路可能被和谐了播放不了，可以选择其他的线路），然后点击右下角播放图片按钮会跳转到视频解析播放的页面（笔者就偷懒了，自己不做这部分了，调用的别人现成的）。
![视频解析播放方式](https://img-blog.csdn.net/20180921131258942?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTIxODQ1Mzk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
2. 界面设计优雅整洁，设计APP时间脚本，各种的图片资源都是网上直接找的，界面设计也用了第三方库，可以加快开发过程。下面两个Material Design资源的网站，大家可以去找到各种精美的界面、图标设计等。
[Material Design图标](https://www.flaticon.com/packs/material-design)
[Material Design综合大站](https://www.uplabs.com/posts/c/android/resources)
3. 为了丰富功能，添加了广告投放和APP授权功能。另外、还有加入QQ群、分析APP等功能。当然，APP是免费给大家用的，授权码只是给开发者有兴趣的去改进而已。授权码：**１２３４**
![其他功能](https://img-blog.csdn.net/20180921132318859?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTIxODQ1Mzk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

# 代码编写
APP功能需求准备好了，下面就要开始着手编程实现了。
## 框架搭建
对于一个APP来说，框架设计占有极其重要的部分，一个APP的编程框架设计好了，以后的开发可以避免很多弯路，也能节省很多的开发时间。很多程序员之所以会是程序猿就是因为在走很多弯路，浪费了大量的时间，导致经常加班加点的工作。在我看来，程序是用来设计的，不是用来干苦力的。在本APP中，为了节省开发周期，很多的工作都没有做，比如网络请求、注解、MVP、MVVM等。只用简单的原生API快速完成功能，代码中的字符串也都是写死的，没有使用xml去配置，在企业的APP是不能这么写的。所以本APP基本是没有框架的，整个APP的代码文件也不超过十个，总代码量不超过2000行，实在是精简的不能再说了。
## 第三方库引入
程序中使用部分第三方库，都是行业比较成熟的，经过市场的认证，不用担心出什么问题。下面是gradle中的配置：

```
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.android.support:appcompat-v7:28.0.0-rc02'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    implementation 'com.android.support:support-v4:28.0.0-rc02'
    implementation 'com.github.bumptech.glide:glide:4.8.0'
    implementation 'com.squareup.okhttp3:okhttp:3.11.0'
    implementation 'eu.the4thfloor.volley:com.android.volley:2015.05.28'
    implementation 'com.alibaba:fastjson:1.2.49'
    implementation 'com.youth.banner:banner:1.4.10'
}
```

## 代码编写
编程过程就不说了，太简单了，Android开发入门的人都能很清晰的看懂代码。这里说一下，对于授权码部分，调用了一个native层函数，用C++编写的，只是为了说明为了安全，部分重要的数据是需要放到C++中做的，不说了，太简单了。下面是所有代码文件：
![代码文件](https://img-blog.csdn.net/20180921134044656?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTIxODQ1Mzk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


# 开源与总结
整个开发过程耗时2天，刚好这周学校的学生军训，笔者一天也就两节课，可以有大量的时间写这个APP。笔者除了现在学校的课程外，还在准备线上课程，时间比较忙，写这个APP花了两天，周末又要加班准备课程内容了。源代码通过git托管到了github上，有兴趣或者想二次开发的朋友可以去下载。
1. [github源代码](https://github.com/JafferLei/NoVIP)
2. [APP下载](https://pan.baidu.com/s/1341k5za08_A0OaqHqW_5RA)