package com.lyj.core.extension

val Any.simpleTag : String
    get() = this.javaClass.simpleName

val Any.permissionTag : String
    get() = "${this.javaClass.simpleName} PERMISSION"
val Any.mapTag : String
    get() = "${this.javaClass.simpleName} MAP"
