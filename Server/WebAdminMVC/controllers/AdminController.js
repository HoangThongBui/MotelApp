'use strict'
const mongoose = require('mongoose');
const User = mongoose.model('User');

exports.go_to_confirm_current_password = (req, res) => {
    res.render('old_password');
}

exports.confirm_current_password = async (req, res) => {
    try {
        var currentPassword = (await User.findById(req.session.admin._id)).password;
        if (currentPassword === req.body.password) {
            res.render('new_password');
        } else {
            res.render('old_password', { error: 'Your current password is wrong' });
        }
    } catch (error) {
        console.log(error);
    }
}

exports.create_new_password = async (req, res) => {
    try {
        await User.findByIdAndUpdate(req.session.admin._id, {
            "$set": {
                password: req.body.password
            }
        });
        res.redirect('/admin/logout');
    } catch (error) {
        console.log(error);
    }
}

exports.go_to_edit_profile = (req, res) => {
    res.render('profile_edit');
}

exports.update_profile = async (req, res) => {
    try {
        var set;
        if (req.file) {
            set = {
                name: req.body.name,
                phone: req.body.phone,
                image: '/images/users/' + req.file.filename

            }
        } else {
            set = {
                name: req.body.name,
                phone: req.body.phone
            }
        }
        var user_id = req.session.admin._id;
        await User.findByIdAndUpdate(user_id, {"$set" : set});
        var user = await User.findById(user_id);
        delete user.password;
        req.session.admin = user;
        res.redirect('/admin/home');
    } catch (error) {
        console.log(error);
    }
}