package com.example.listit.data

data class ToDoListItem(
    val title:String,
    var isChecked:Boolean = false){
    constructor() :
            this("", false)
}

data class ToDoList(
    val title:String){
    constructor() :
            this("")
}