# Cedict

MDBG's CC-CEDICT wrapper for Kotlin.

This is intended to work as a library.

## Installation

Add this to your dependencies.

```groovy
implementation 'io.github.patarapolw:cedict:0.1.4'
```

And, download <https://github.com/patarapolw/cedict/blob/master/cedict.db?raw=true> and put it in `resources` folder.

## Usage

```kotlin
import cedict.Cedict

val dict = Cedict("jdbc:sqlite::resource:cedict.db")
println(dict['你好'])
```
