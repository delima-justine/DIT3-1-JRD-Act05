package com.example.notekeeperapp

class Note {
    var id: Int?
    var title: String
    var content: String
    var timestamp: String

    constructor(id: Int, title: String, content: String, timestamp: String) {
        this.id = id
        this.title = title
        this.content = content
        this.timestamp = timestamp
    }

    constructor(title: String, content: String, timestamp: String) {
        this.id = null
        this.title = title
        this.content = content
        this.timestamp = timestamp
    }
}