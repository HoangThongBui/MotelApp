'use strict'

const mongoose = require('mongoose');
const User = mongoose.model('User');

exports.go_to_user_page = async (req, res) => {
    try {
        var userList = await User.find();
        var admins = [];
        var users = [];
        for (var i = 0; i < userList.length; i++){
            if (userList[i].role === 'admin'){
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

exports.ban_user = async (req,res) => {
    try {
        await User.findByIdAndUpdate(req.params.user_id, {"$set" : {status : false}});
        res.redirect('/admin/user');
    } catch (error) {
        console.log(error);
    }
}

exports.unban_user = async (req,res) => {
    try {
        await User.findByIdAndUpdate(req.params.user_id, {"$set" : {status : true}});
        res.redirect('/admin/user');
    } catch (error) {
        console.log(error);
    }
}