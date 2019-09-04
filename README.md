# jacc-maven-plugin
A custom maven plugin for easy use with jacc (just another comipler compiler)


The following plugin performs the same function as a call to jacc (just another compiler compiler). The following XML can be 
used in maven POM:

 <plugin>
    <groupId>jacc</groupId>
    <artifactId>jacc-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <configuration>
                <sourceDirectory>(full path to a folder containing .jacc files or a .jacc file)</sourceDirectory><!-- REQUIRED -->
                <!--  NOTE: the output directory is the same as the source directory -->
                <timestamp>false</timestamp><!-- an optional configuration, false: does not print timestamp header, true: prints timestamp header. DEFAULT: TRUE -->
            </configuration>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
