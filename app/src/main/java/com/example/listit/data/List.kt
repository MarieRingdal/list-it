package com.example.listit.data

data class ToDoListItem(val title:String, val progress:Int, var isChecked:Boolean = false)

data class ToDoList(val title:String, val todos:ToDoListItem)


