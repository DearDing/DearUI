package com.dear.ui.widget.dropview

class DialogItemBean {
    var content: String = ""
    var icon: String = ""//icon图地址
    var iconRes: Int = 0//icon图本地地址
    var key: String = ""
    var isSelected = false

    constructor(content: String) {
        this.content = content
    }

    constructor(content: String, key: String) {
        this.content = content
        this.key = key
    }

    constructor(content: String, key: String, isSelected: Boolean) {
        this.content = content
        this.key = key
        this.isSelected = isSelected
    }

    constructor(content: String, icon: String, key: String) {
        this.content = content
        this.key = key
        this.icon = icon
    }

    constructor(content: String, iconRes: Int, key: String) {
        this.content = content
        this.key = key
        this.iconRes = iconRes
    }
}