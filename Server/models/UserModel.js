'use strict'
const mongoose = require('mongoose')

const UserSchema = new mongoose.Schema({
    name: String,
    email: {
        type: String,
        unique: true
    },
    password: String,
    phone: String,
    status: {
        type: Boolean,
        default: true
    },
    facebook: String,
    role: {
        type: String,
        default: "user"
    },
    image: {
        type: String,
        default: ""
    }
}, {
    collection: 'users',
    max: 1000
})

module.exports = mongoose.model('User', UserSchema)