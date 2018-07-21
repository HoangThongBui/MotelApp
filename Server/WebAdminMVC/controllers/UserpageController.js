'use strict'

const mongoose = require('mongoose');
const User = mongoose.model('User');

exports.go_to_user_page = async (req, res) => {
    try {
        var userList = await User.find();
        var admins = [];
        var users = [];
        for (var i = 0; i < userList.length; i++) {
            if (userList[i].role === 'admin') {
                admins.push(userList[i]);
            }
            else {
                users.push(userList[i]);
            }
        }
        res.render('user', { admins, users, page_type: 'user' });
    } catch (error) {
        console.log(error);
    }
}

exports.ban_user = async (req, res) => {
    try {
        await User.findByIdAndUpdate(req.params.user_id, { "$set": { status: false } });
        res.redirect('/admin/user');
    } catch (error) {
        console.log(error);
    }
}

exports.unban_user = async (req, res) => {
    try {
        await User.findByIdAndUpdate(req.params.user_id, { "$set": { status: true } });
        res.redirect('/admin/user');
    } catch (error) {
        console.log(error);
    }
}

exports.add_more_account = (req, res) => {
    res.render('new_user', { type: req.params.account_type });
}

exports.add_new_account = async (req, res) => {
    try {
        var account = await User.findOne({ email: req.body.email });
        if (account) {
            res.render('new_user', {
                type: req.params.account_type, fields: {
                    name: req.body.name,
                    email: req.body.email,
                    phone: req.body.phone
                }, error: 'Account existed!'
            });
        }
        else {
            var newAccount = new User({
                email: req.body.email,
                name: req.body.name,
                password: req.body.password,
                phone: req.body.phone,
                role : req.params.account_type,
                image: '/images/users/' + req.file.filename
            });
            await newAccount.save();
            res.redirect('/admin/user');
        }
    } catch (error) {
        console.log(error);
    }
}