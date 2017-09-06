# http://developer.android.com/guide/developing/tools/proguard.html
# Add any project specific keep options here:
-keepattributes Signature
-keepclassmembers class uk.co.j15t98j.simplechatapp.** {
  *;
}
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}