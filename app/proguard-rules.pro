# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.google.android.gms.internal.** { *; }
-keep class com.google.gson.** { *; }
-keep public class com.google.gson.** {public private protected *;}
-keep class retrofit.** { *; }
-dontwarn com.google.android.gms.internal.zzhu

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

-dontwarn com.squareup.okhttp.*
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

-keepclassmembers class * implements javax.net.ssl.SSLSocketFactory {
         private javax.net.ssl.SSLSocketFactory delegate;
    }