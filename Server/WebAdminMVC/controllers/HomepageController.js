'use strict'

const mongoose = require('mongoose');
const Room = mongoose.model('Room');
const Post = mongoose.model('Post');
const User = mongoose.model('User');

exports.go_to_home_page = async (req,res) => {
    var unconfirmedPosts = await Post.find({status : "u"}).sort({request_date : -1});
    for (var i = 0; i < unconfirmedPosts.length; i++){
        unconfirmedPosts[i].user = await User.findById(unconfirmedPosts[i].user, {status : 0, password : 0, role : 0});
        unconfirmedPosts[i].room = await Room.findById(unconfirmedPosts[i].room);
    }
    res.render('home', {
        page_type : 'home',
        posts : unconfirmedPosts
    });
}

exports.home_redirect = (req,res) => {
    res.redirect('/admin/home');
}