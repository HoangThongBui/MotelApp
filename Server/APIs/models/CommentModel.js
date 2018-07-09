'use strict'
const mongoose = require('mongoose')

const CommentSchema = new mongoose.Schema({
    user_id: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    detail: String,
    comment_time: {
        type: Date,
        default: Date.now()
    }
}, {
    collection: 'comments',
    max: 1000
})

module.exports = mongoose.model('Comment', CommentSchema)