'use strict'
const mongoose = require('mongoose')

const PostSchema = new mongoose.Schema({
    title: String,
    user_id: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    room_id: {
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
})

module.exports = mongoose.model('Post', PostSchema)