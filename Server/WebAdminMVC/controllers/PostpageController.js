'use strict'

const mongoose = require('mongoose');
const Post = mongoose.model('Post');
const User = mongoose.model('User');
const Room = mongoose.model('Room');

exports.go_to_post_page = async (req, res) => {
    var posts = await Post.find().sort({ request_date: -1, status: 1 });
    for (var i = 0; i < posts.length; i++){
        posts[i].user = await User.findById(posts[i].user);
        posts[i].room = await Room.findById(posts[i].room);
    }
    var confirmedPosts = [], unconfirmedPosts = [], rejectedPosts = [], removedPosts = [];
    for (var i = 0; i < posts.length; i++) {
        switch (posts[i].status) {
            case "c":
                confirmedPosts.push(posts[i]);
                break;
            case "u":
                unconfirmedPosts.push(posts[i]);
                break;
            case "r":
                rejectedPosts.push(posts[i]);
                break;
            case "d":
                removedPosts.push(posts[i]);
                break;
        }
    }
    res.render('post', { length : posts.length, confirm : confirmedPosts, unconfirm : unconfirmedPosts
        , reject : rejectedPosts, remove : removedPosts, page_type: 'post' });
}