-printmapping 'mapping.txt'
# this is very dangerous that cause bug
-ignorewarnings
-dontskipnonpubliclibraryclassmembers
-dontshrink
-dontoptimize
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-keepclasseswithmembers class * extends cn.gundam.sdk.shell.even.SDKEventReceiver
-keep class cn.uc.**{
<methods>;
<fields>;
}
-keep class cn.gundam.**{
<methods>;
<fields>;
}
-keep class android.**{
<methods>;
<fields>;
}
-keep class org.json.**{
<methods>;
<fields>;
}