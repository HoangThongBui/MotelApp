'use strict'

const mongoose = require('mongoose');
const User = mongoose.model('User');

exports.check_user_status = async function (req, res) {
    try {
        var id = req.params.id;
        var user = await User.findById(id, { password: 0 });
        if (user.status) {
            res.send("User is active!");
        }
        else {
            res.send("User not found!");
        }
    } catch (error) {
        res.send(error)
    }
}

exports.user_login = async function (req, res) {
    try {
        var email = req.body.email;
        var password = req.body.password;
        var user = await User.find({ email });
    } catch (error) {
        res.send(error);
    }
}