# React Native Calendar Native Module in Kotlin

This repository shows how to create native modules to React Native using Kotlin, creating an event in the smartphone's calendar.

### React Native setup to create native modules using Kotlin
For this project, the focus is to use Kotlin to create native modules. Before start ***is very recommended update the gradle version***.

#### build.gradle
In this [build.gradle](https://github.com/EzequielDeOliveira/react-native-calendar-kotlin-module/blob/main/android/build.gradle), we need to add a Kotlin version and add it as a dependency.
```
buildscript {
    ext {
        ...
        kotlin_version = "1.6.10"
    }
    ...
    dependencies {
        ...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        ...
    }
}
```

### app/build.gradle
The following file is [android/app/build.gradle](https://github.com/EzequielDeOliveira/react-native-calendar-kotlin-module/blob/main/android/app/build.gradle), Here just apply the Kotlin plugin as a dependency
```
apply plugin: "com.android.application"
apply plugin: "kotlin-android" // Add this line
...
dependencies {
    ...
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version" // Add this line
    implementation "com.facebook.react:react-native:+"  // From node_modules
    ...
}
```

### CalendarModule.kt
The [CalendarModule.kt](./android/app/src/main/java/com/calendar_native_module_kotlin/CalendarModule.kt) is the core of the native module.
It contains the logic to receive data from the Javascript side, open the calendar, and fill the fields.

After extending ***ReactContextBaseJavaModule*** is necessary to override the function getName to return the component's name to be imported on the Javascript side.

To create a callable function to React Native, use ***@ReactMethod*** after the method.

```
class CalendarModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private val context: ReactApplicationContext = reactContext;

    override fun getName(): String {
        return "CalendarModule"
    }

    @ReactMethod
    fun createCalendarEvent(name: String, location: String) {
        Log.d("CalendarModuleKotlin", "Create event called with name: " + name
                + " and location: " + location);
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CONTENT_URI)
            .putExtra(TITLE, "Kotlin")
            .putExtra(EVENT_LOCATION, "Remote")
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis())
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, System.currentTimeMillis() + (60 * 60 * 1000))
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }
}
```

### CalendarPackage.kt
The [CalendarPackage.kt](/android/app/src/main/java/com/calendar_native_module_kotlin/CalendarPackage.kt) must expose the created module to the react native 
modules, extending ***ReactPackage*** and overriding ***createViewManager*** to native components and ***createNativeModules*** to modules.

```
class CalendarPackage: ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        val modules = ArrayList<NativeModule>()
        modules.add(CalendarModule(reactContext))
        return modules
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return Collections.emptyList<ViewManager<*, *>>()
    }
}
```

### MainApplication.java
In the ***MainApplication.java***, in the function [getPackages](https://github.com/EzequielDeOliveira/react-native-calendar-java-module/blob/7df0c1241c6f19a1331fb1970ea1fae71967ddf7/android/app/src/main/java/com/calendarnativemodulejava/MainApplication.java#L28) it's time to import our package.

If you wonder why this file is ***.java*** 🤔, the answer is simple Kotlin and Java have interoperability. Then if you did the first step, this works perfectly.

```
@Override
protected List<ReactPackage> getPackages() {
    @SuppressWarnings("UnnecessaryLocalVariable")
    List<ReactPackage> packages = new PackageList(this).getPackages();
    // Packages that cannot be autolinked yet can be added manually here, for example:
    packages.add(new CalendarPackage());
    return packages;
}
```

### CalendarModule.js
The file [CalendarModule.js](./CalendarModule.js) handles the native module and exposes the function on the Javascript side.
```
import { NativeModules } from 'react-native';
const { CalendarModule } = NativeModules;
export default CalendarModule;
```
To use import the function and pass the title of the event and the location, like this:
```
CalendarModule.createCalendarEvent('testName', 'testLocation');
```
### Result
![ezgif com-gif-maker](https://user-images.githubusercontent.com/37127457/167336147-8b07ef94-5afe-4eef-9a85-9be5093b9425.gif)

** This project is based on the [React Native's documentation](https://reactnative.dev/docs/native-modules-android)
