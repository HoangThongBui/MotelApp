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
        type: Boolean,
        default: false
    },
    request_date: {
        type: Date,
        default: Date.now()
    }
}, {
    collection: 'posts',
    max: 1000
})

module.exports = mongoose.model('Post', PostSchema)