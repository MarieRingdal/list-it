package com.example.listit.data

data class TodoListItem(val title:String, var isChecked:Boolean = false){
    constructor() :
            this("", false)
}

data class TodoList(val title:String){
    constructor() :
            this("")
}