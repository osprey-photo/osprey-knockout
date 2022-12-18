
https://stackoverflow.com/questions/23796404/could-not-find-method-compile-for-arguments-gradle


Update gralde verson directly in the properties
 ./gradlew wrapper --gradle-version=7.6 --distribution-type=bin


AILURE: Build failed with an exception.

* Where:
Settings file '/home/matthew/github.com/osprey-photo/osprey-knockout/settings.gradle'


* What went wrong:
org.codehaus.groovy.runtime.InvokerHelper (initialization failure)
> org.codehaus.groovy.reflection.ReflectionCache (initialization failure)


* What went wrong:
Could not compile settings file '/home/matthew/github.com/osprey-photo/osprey-knockout/settings.gradle'.
> startup failed:
  General error during semantic analysis: Unsupported class file major version 61

  java.lang.IllegalArgumentException: Unsupported class file major version 61