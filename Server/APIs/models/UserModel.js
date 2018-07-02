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
})

module.exports = mongoose.model('User', UserSchema)