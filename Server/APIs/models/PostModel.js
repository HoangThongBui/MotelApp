'use strict'
const mongoose = require('mongoose')

const PostSchema = new mongoose.Schema({
    title: String,
    user_id: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    address: String,
    city: String,
    district: String,
    ward: String,
    price: Number,
    detail: String,
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