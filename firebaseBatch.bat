    @ECHO OFF
	set CLASSPATH=%CLASSPATH%;
	cd "C:\Users\Rahul Setty\eclipse-workspace\FirebaseAdminSDK\src\"
    javac -cp ".;C:\Users\Rahul Setty\eclipse-workspace\FirebaseAdminSDK\target\FirebaseAdminSDK-0.0.1-SNAPSHOT-jar-with-dependencies.jar" SdkTest.java
    java -cp ".;C:\Users\Rahul Setty\eclipse-workspace\FirebaseAdminSDK\target\FirebaseAdminSDK-0.0.1-SNAPSHOT-jar-with-dependencies.jar" SdkTest
    