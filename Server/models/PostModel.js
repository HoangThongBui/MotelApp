'use strict'
const mongoose = require('mongoose')

const PostSchema = new mongoose.Schema({
    title: String,
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    room: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Room'
    },
    status:{
        type: String,
        default: "u"
    },
    request_date: {
        type: Date
    }
}, {
    collection: 'posts',
    max: 1000
})

module.exports = mongoose.model('Post', PostSchema)