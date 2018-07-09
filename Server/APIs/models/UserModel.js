'use strict'
const mongoose = require('mongoose')

const UserSchema = new mongoose.Schema({
    email: String,
    password: String,
    phone: String,
    status: {
        type: Boolean,
        default: true
    },
    facebook: {
        id: String,
        name: String,
        image: String
    }
}, {
    collection: 'users',
    max: 1000
})

module.exports = mongoose.model('User', UserSchema)