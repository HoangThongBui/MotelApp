'use strict'
const mongoose = require('mongoose')

const RoomSchema = new mongoose.Schema({
    address: String,
    city: String,
    district: String,
    ward: String,
    price: Number,
    description: String
})

module.exports = mongoose.model('Room', RoomSchema)