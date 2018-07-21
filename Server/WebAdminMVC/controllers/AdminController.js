'use strict'
const mongoose = require('mongoose');
const User = mongoose.model('User');

exports.go_to_confirm_current_password = (req,res) => {
    res.render('old_password');
}

exports.confirm_current_password = async (req,res) => {
    try {
        var currentPassword = (await User.findById(req.session.admin._id)).password;
        if (currentPassword === req.body.password){
            res.render('new_password');
        } else {
            res.render('old_password', {error : 'Your current password is wrong'});
        }
    } catch (error) {
        console.log(error);
    }   
}

exports.create_new_password = async (req,res) => {
    try {
        await User.findByIdAndUpdate(req.session.admin._id, {"$set" : {
            password : req.body.password
        }});
        res.redirect('/admin/logout');
    } catch (error) {
        console.log(error);
    }
}