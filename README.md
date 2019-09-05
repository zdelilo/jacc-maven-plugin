# jacc-maven-plugin
A custom maven plugin for easy use with jacc (just another comipler compiler)


The following plugin performs the same function as a call to jacc (just another compiler compiler). The following XML can be 
used in maven POM:

 \<plugin>
   <br />&nbsp;&nbsp;&nbsp;&nbsp;\<groupId>jacc\</groupId>
    <br />&nbsp;&nbsp;&nbsp;&nbsp;\<artifactId>jacc-maven-plugin\</artifactId>
    <br />&nbsp;&nbsp;&nbsp;&nbsp;\<version>1.0-SNAPSHOT\</version>
    <br />&nbsp;&nbsp;&nbsp;&nbsp;\<executions>
    <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\<execution>
    <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\<configuration>
    <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\<sourceDirectory>(full path to a folder containing .jacc files or a .jacc file)\</sourceDirectory>
       <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\<timestamp>false\</timestamp> 
   <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\<!-- optional | default: true, prints timestamp header | false: skips timestamp header -->
   <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\</configuration>
   <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\<goals>
   <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\<goal>generate\</goal>
   <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\</goals>
   <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\</execution>
   <br />&nbsp;&nbsp;&nbsp;&nbsp;\</executions>
<br />\</plugin>
