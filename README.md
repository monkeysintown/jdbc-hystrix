# jdbc-hystrix

Hystrix wrapper for JDBC drivers. ![Maven Central](https://img.shields.io/maven-central/v/com.github.monkeysintown/jdbc-hystrix.svg)


## Build

If you just want to compile the project without running the tests:

```
mvn -DskipTests clean install
```

## Maven dependencies

You can find the latest releases here:

[ ![Download](https://api.bintray.com/packages/cheetah/monkeysintown/jdbc-hystrix/images/download.svg) ](https://bintray.com/cheetah/monkeysintown/jdbc-hystrix/_latestVersion)

... or setup your Maven dependencies:

```xml
<dependency>
    <groupId>com.m11n.jdbc.hystrix</groupId>
    <artifactId>jdbc-hystrix</artifactId>
    <version>1.0.0</version>
</dependency>
```

... and configure Bintray's JCenter repository in your pom.xml:
 
```xml
...
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>central</id>
        <name>bintray</name>
        <url>http://jcenter.bintray.com</url>
    </repository>
</repositories>
...
```

Get automatic notifications about new releases here:

[ ![Get automatic notifications about new "jdbc-hystrix" versions](https://www.bintray.com/docs/images/bintray_badge_color.png) ](https://bintray.com/cheetah/monkeysintown/jdbc-hystrix/view?source=watch)
