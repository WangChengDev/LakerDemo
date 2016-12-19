# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-ignorewarnings                # 抑制警告
#移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用，另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
}

################################### 公共部分   start   #######################################
#(1).基本混淆

#代码混淆压缩比， 在0~7之间，默认为5，一般不需要改
-optimizationpasses 5 #指定代码的压缩级别 0 - 7
#不使用大小写混合，混淆后类名称为小写
#注:对于-dontusemixedcaseclassnames这里有点要说明一下，
# proguard会默认我们的操作系统能够区分大小写字母的文件，如b.java和B.java会被认为是两个不同的文件，
# 但是window系统并不这样认为(window系统对大小写不敏感的)。
# 因此在window系统下必须在proguard文件中指明-dontusemixedcaseclassnames选项。
# 如果没这样做并且我们的项目中类的数量超过26个的话，那么proguard就会默认混用大小写文件名，
# 进而导致class文件相互覆盖。所以为了安全起见，我们都默认设置该选项。
-dontusemixedcaseclassnames
#如果应用程序引入的有jar包，并且混淆jar包里面的class,指定不去忽略公共库的类
#注:dontskipnonpubliclibraryclasses用于高速proguard，不要跳过对非公开类的处理。
#默认情况下是跳过的，因为程序中不会引用它们，有些情况下人们编写的代码与类库中的类在同一个包下，
#并且对包中内容加以引用，此时需要加入此条声明。
-dontskipnonpubliclibraryclasses
#不做预校验，preverify是proguard的4个功能之一
#android不需要preverify，去掉这一步可以加快混淆速度
-dontpreverify
#混淆时记录日志（混淆后生产映射文件 map 类名 -> 转化后类名的映射
-verbose
#指定映射文件的名称
-printmapping proguardMapping.txt
#指定混淆时的算法，后面的参数是一个过滤器
#这个过滤器是谷歌推荐的算法，一般也不会改变
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#类型转换错误 添加如下代码以便过滤泛型（不写可能会出现类型转换错误，一般情况把这个加上就是了）,即避免泛型被混淆
-keepattributes Signature
#假如项目中有用到注解，应加入这行配置,对JSON实体映射也很重要,eg:fastjson
-keepattributes *Annotation*
# 将.class信息中的类名重新定义为"Proguard"字符串
-renamesourcefileattribute Proguard
# 并保留源文件名为"Proguard"字符串，而非原始的类名 并保留行号
-keepattributes SourceFile,LineNumberTable

#(2).需要保留的相关信息（指明不需要混淆的相关信息）

#保持 native 的方法不去混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#support-v4 ,support-v7 包下的文件都不要混淆 -dontwarn 如果有警告也不终止
#（不是所有第三方sdk都需要dontwarn，这取决于混淆时是否会出现警告）
-dontwarn android.support.**#不提示兼容库的错误警告
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-keep public class * extends android.support.**

#保留继承自Activity、Application这些类的子类，因为这些子类都有可能被外部调用
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.backup.BackupAgentHelper

#保留在Activity中的方法参数是view的方法，从而我们在layout里面编写onclick的就不会被影响
-keepclassmembers class * extends android.app.Activity {
     public void *(android.view.View);
}
#保留自定义控件指定规则的方法不被混淆
-keep public class * extends android.view.View {
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
     public void set*(...);
     *** get*();
}
#枚举类不能被混淆
-keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
}
#保留Parcelable不被混淆
-keep class * implements android.os.Parcelable {
     public static final android.os.Parcelable$Creator *;
}
#需要序列化和反序列化的类不能被混淆（注：Java反射用到的类也不能被混淆）
-keepnames class * implements java.io.Serializable
#保护实现接口Serializable的类中，指定规则的类成员不被混淆
-keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     !static !transient <fields>;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }
#保持R文件不被混淆，否则，你的反射是获取不到资源id的
-keep class **.R$* { *; }
##对于带有回调函数onXXEvent的不被混淆
#-keepclassmenbers class *{
#    void *(**On*Event)
#}
#保持自定义控件类不被混淆，指定格式的构造方法不去混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#(3).针对App的量身定做

#保留entity的实体类和成员不被混淆
-keep public class com.zejian.android4package.model.**{
    *;
}
#保留内部类不被混淆（不写内部类，一劳永逸）
-keep class com.androidapp.activity.MainActivity$*{*;}
#处理反射
#一、反射用到的类中的变量不被混淆
-keep public class com.androidapp.Expressions { *;}
#二、过滤泛型（前面已添加）
-keepattributes Signature
#三、保持R文件不被混淆（前面已添加）
-keep class **.R$* {*;}

###### 涉及到JS调用的类  start  ####################
##把注释解除，把fqcn.of.javascript.interface.for.webview换成你自己定义的那个类名
##（包名也必须有，如果定义的是内部类，则是cn.wj.ui.WebViewActivity$myInterface），
##4.2以上版本调用js接口需要在方法使用
##声明@JavascriptInterface,然后混淆时可能会弄丢该声明导致，程序无法调用js，需要继续再配置文件中添加条件，
##所以当使用了webview和js交互时，混淆文件中应该添上这些配置：
#对webview的处理
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.webView, java.lang.String);
    public *;
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
    public void *(android.webkit.WebView,java.lang.String);
}
#对JavaScript的处理
#比如我们代码定义了与js相关的回调类，这时我们也保证该类不能被混淆（项目中所有js交互的都必须确保不被混淆）
-keepclassmembers class com.androidapp.JavaScriptInterface {
    <methods>;
}
-keepattributes *JavascriptInterface*
-keep class android.webkit.JavascriptInterface {*;}
-keep class **.Webview2JsInterface { *; }  #保护WebView对HTML页面的API不被混淆
####### 涉及到JS调用的类   end   ####################
################################### 公共部分    end    #######################################



#############################################################################################
########################                 以上通用           #################################
#############################################################################################

#######################     常用第三方模块的混淆选项         ################################
#gson
#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }


######引用的其他Module可以直接在app的这个混淆文件里配置

# 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。
-keep class com.matrix.app.entity.json.** { *; }
-keep class com.matrix.appsdk.network.model.** { *; }

########################### EventBus start###########################################
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
########################### EventBus  end ###########################################

####################### nineoldandroids start########################################
-keep class com.nineoldandroids.** { *; }
-keep interface com.nineoldandroids.** { *; }
-dontwarn com.nineoldandroids.**
####################### nineoldandroids  end ########################################

############################# glide start    ########################################
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.** { *; }
############################# glide  end    ########################################
############################# okhttp3 start ########################################
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class okhttp3.** { *;}
-keep class okio.** { *;}
-dontwarn sun.security.**
-keep class sun.security.** { *;}
-dontwarn okio.**
-dontwarn okhttp3.**
############################# okhttp3  end  ########################################
########################### photoview start ########################################
-dontwarn  uk.co.senab.photoview.**
-keep class uk.co.senab.photoview.** { *; }
########################### photoview  end  ########################################

############################## ormlite  start ######################################
-keep class com.j256.** { *; }
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keep class com.colorsnap.model.**
-keepclassmembers class com.colorsnap.model.** { *; }
############################# ormlite  end ########################################