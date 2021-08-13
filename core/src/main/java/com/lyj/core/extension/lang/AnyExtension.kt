package com.lyj.core.extension.lang

val Any.simpleTag : String
    get() = this.javaClass.simpleName

val Any.permissionTag : String
    get() = "${this.javaClass.simpleName} PERMISSION"

val Any.mapTag : String
    get() = "${this.javaClass.simpleName} MAP"

val Any.testTag : String
    get() = "${this.javaClass.simpleName} TEST"

val Any.socketTag : String
    get() = "${this.javaClass.simpleName} SOCKET"
