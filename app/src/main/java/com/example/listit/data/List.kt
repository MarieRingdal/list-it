package com.example.listit.data

data class TodoListItem(val title:String, var isDone:Boolean = false){
    constructor() :
            this("", false)
}

data class TodoList(val title:String,
                    val checkedItems:Int,
                    val totalItems:Int,
                    var items:MutableList<TodoListItem>? = null){
    constructor() :
            this("", 0, 0)
}