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
            res.send("User is banned!");
        }
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}

exports.user_login = async function (req, res) {
    try {
        var email = req.body.email;
        var password = req.body.password;
        var user = await User.findOne({ email });
        if (user){
            if (password === user.password){
                res.send(user._id.toString());
            }
            else {
                res.send("Wrong password!")
            }
        }
        else {
            res.send("No account!")
        }
    } catch (error) {
        console.log(error);
        res.send("Server internal error!");
    }
}